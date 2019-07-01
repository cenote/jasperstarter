/*
 * Copyright 2012-2015 Cenote GmbH.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.cenote.jasperstarter;

import de.cenote.jasperstarter.types.AskFilter;
import de.cenote.jasperstarter.types.Command;
import de.cenote.jasperstarter.types.DsType;
import de.cenote.jasperstarter.types.Dest;
import de.cenote.jasperstarter.types.OutputFormat;
import de.cenote.tools.classpath.ApplicationClasspath;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintStream;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.Argument;
import net.sourceforge.argparse4j.inf.ArgumentGroup;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 * <p>App class.</p>
 *
 * @author Volker Voßkämper
 * @version $Revision: 349bcea5768c:59 branch:default $
 */
public class App {

    private Namespace namespace = null;
    private Map<String, Argument> allArguments = null;
    private static PrintStream configSink = System.err;
    private static PrintStream debugSink = System.err;
    private static PrintStream errSink = System.err;

    /**
     * <p>main.</p>
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Config config = new Config();
        App app = new App();
        // create the command line parser
        ArgumentParser parser = app.createArgumentParser(config);
        if (args.length == 0) {
            System.out.println(parser.formatUsage());
            System.out.println("type: jasperstarter -h to get help");
            System.exit(0);
        }
        try {
            app.parseArgumentParser(args, parser, config);
        } catch (ArgumentParserException ex) {
            parser.handleError(ex);
            System.exit(1);
        }
        if (config.isVerbose()) {
            configSink.print("Command line:");
            for (String arg : args) {
                configSink.print(" " + arg);
            }
            // @todo: this makes sense only if Config.toString() is overwitten
//            configSink.print("\n");
//            configSink.println(config);
        }

        // @todo: main() will not be executed in tests...
        // setting locale if given
        if (config.hasLocale()) {
            Locale.setDefault(config.getLocale());
        }

        try {
            switch (Command.getCommand(config.getCommand())) {
                case COMPILE:
                case CP:
                    app.compile(config);
                    break;
                case PROCESS:
                case PR:
                    app.processReport(config);
                    break;
                case LIST_PRINTERS:
                case PRINTERS:
                case LPR:
                    app.listPrinters();
                    break;
                case LIST_PARAMETERS:
                case PARAMS:
                case LPA:
                    App.listReportParams(config, new File(config.getInput()).getAbsoluteFile());
                    break;
            }
        } catch (IllegalArgumentException ex) {
            errSink.println(ex.getMessage());
            System.exit(1);
        } catch (InterruptedException ex) {
            errSink.println(ex.getMessage());
            // Restore interrupted state...
            Thread.currentThread().interrupt();
            System.exit(1);
        } catch (JRException ex) {
            errSink.println(ex.getMessage());
            System.exit(1);
        }
    }

    private void compile(Config config) {
        IllegalArgumentException error = null;
        File input = new File(config.getInput());
        if (input.isFile()) {
            try {
                Report report = new Report(config, input);
                report.compileToFile();
            } catch (IllegalArgumentException ex) {
                error = ex;
            }
        } else if (input.isDirectory()) {
            // compile all .jrxml files in this directory
            FileFilter fileFilter = new WildcardFileFilter("*.jrxml", IOCase.INSENSITIVE);
            File[] files = input.listFiles(fileFilter);
            for (File file : files) {
                try {
                    System.out.println("Compiling: \"" + file + "\"");
                    Report report = new Report(config, file);
                    report.compileToFile();
                } catch (IllegalArgumentException ex) {
                    error = ex;
                }
            }
        } else {
            error = new IllegalArgumentException("Error: not a file: " + input.getName());
        }
        if (error != null) {
            throw error;
        }
    }

    private void processReport(Config config)
            throws IllegalArgumentException, InterruptedException, JRException {
        // add the jdbc dir to classpath
        try {
            if (config.hasJdbcDir()) {
                File jdbcDir = config.getJdbcDir();
                if (config.isVerbose()) {
                    configSink.println("Using jdbc-dir: " + jdbcDir.getAbsolutePath());
                }
                ApplicationClasspath.addJars(jdbcDir.getAbsolutePath());
            } else {
                ApplicationClasspath.addJarsRelative("../jdbc");
            }
        } catch (IOException ex) {
            throw new IllegalArgumentException("Error adding jdbc-dir", ex);
        } catch (URISyntaxException ex) {
            throw new IllegalArgumentException("Error adding jdbc-dir: \"../jdbc\"", ex);
        }

        // add optional resources to classpath
        if (config.hasResource()) {
            try {
                if ("".equals(config.getResource())) { // the default
                    // add the parent of input to classpath
                    File res = new File(config.getInput()).getAbsoluteFile().getParentFile();
                    if (res.isDirectory()) {
                        ApplicationClasspath.add(res);
                        if (config.isVerbose()) {
                            configSink.println(
                                    "Added resource \"" + res + "\" to classpath");
                        }
                    } else {
                        throw new IllegalArgumentException(
                                "Resource path \"" + res + "\" is not a directory");
                    }
                } else {
                    // add file or dir to classpath
                    File res = new File(config.getResource());
                    ApplicationClasspath.add(res);
                    if (config.isVerbose()) {
                        configSink.println(
                                "Added resource \"" + res + "\" to classpath");
                    }
                }
            } catch (IOException ex) {
                throw new IllegalArgumentException("Error adding resource \""
                        + config.getResource() + "\" to classpath", ex);
            }
        }
        File inputFile = new File(config.getInput()).getAbsoluteFile();
        if (config.isVerbose()) {
            configSink.println("Original input file: " + inputFile.getAbsolutePath());
        }
        inputFile = locateInputFile(inputFile);
        if (config.isVerbose()) {
            configSink.println("Using input file: " + inputFile.getAbsolutePath());
        }
        Report report = new Report(config, inputFile);

        report.fill();

        List<OutputFormat> formats = config.getOutputFormats();
        Boolean viewIt = false;
        Boolean printIt = false;

        if (formats.size() > 1 && config.getOutput().equals("-")) {
            throw new IllegalArgumentException(
                    "output file \"-\" cannot be used with multiple output formats: " + formats);
        }

        for (OutputFormat f : formats) {
            if (OutputFormat.print.equals(f)) {
                printIt = true;
            } else if (OutputFormat.view.equals(f)) {
                viewIt = true;
            } else if (OutputFormat.pdf.equals(f)) {
                report.exportPdf();
            } else if (OutputFormat.docx.equals(f)) {
                report.exportDocx();
            } else if (OutputFormat.odt.equals(f)) {
                report.exportOdt();
            } else if (OutputFormat.rtf.equals(f)) {
                report.exportRtf();
            } else if (OutputFormat.html.equals(f)) {
                report.exportHtml();
            } else if (OutputFormat.xml.equals(f)) {
                report.exportXml();
            } else if (OutputFormat.xls.equals(f)) {
                report.exportXls();
            } else if (OutputFormat.xlsMeta.equals(f)) {
                report.exportXlsMeta();
            } else if (OutputFormat.xlsx.equals(f)) {
                report.exportXlsx();
            } else if (OutputFormat.csv.equals(f)) {
                report.exportCsv();
            } else if (OutputFormat.csvMeta.equals(f)) {
                report.exportCsvMeta();
            } else if (OutputFormat.ods.equals(f)) {
                report.exportOds();
            } else if (OutputFormat.pptx.equals(f)) {
                report.exportPptx();
            } else if (OutputFormat.xhtml.equals(f)) {
                report.exportXhtml();
            } else if (OutputFormat.jrprint.equals(f)) {
                report.exportJrprint();
            } else {
            	throw new IllegalArgumentException("Error output format \"" + f +  "\" not implemented!");
            }
        }
        if (viewIt) {
            report.view();
        } else if (printIt) {
            // print directly only if viewer is not activated
            report.print();
        }

    }

    /**
     *
     * @param inputFile file or basename of a JasperReports file
     * @return a valid file that is not a directory and has a fileending of
     * (jrxml, jasper, jrprint)
     */
    private File locateInputFile(File inputFile) {

        if (!inputFile.exists()) {
            File newInputfile;
            // maybe the user omitted the file extension
            // first trying .jasper
            newInputfile = new File(inputFile.getAbsolutePath() + ".jasper");
            if (newInputfile.isFile()) {
                inputFile = newInputfile;
            }
            if (!inputFile.exists()) {
                // second trying .jrxml
                newInputfile = new File(inputFile.getAbsolutePath() + ".jrxml");
                if (newInputfile.isFile()) {
                    inputFile = newInputfile;
                }
            }
        }
        if (!inputFile.exists()) {
            throw new IllegalArgumentException("Error: file not found: " + inputFile.getAbsolutePath());
        } else if (inputFile.isDirectory()) {
            throw new IllegalArgumentException("Error: " + inputFile.getAbsolutePath() + " is a directory, file needed");
        }

        return inputFile;
    }

    private void listPrinters() {
        PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
        System.out.println("Default printer:");
        System.out.println("-----------------");
        System.out.println((defaultService == null) ? "--- not set ---" : defaultService.getName());
        System.out.println("");
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        System.out.println("Available printers:");
        System.out.println("--------------------");
        for (PrintService service : services) {
            System.out.println(service.getName());
        }
    }

    private ArgumentParser createArgumentParser(Config config) {
        this.allArguments = new HashMap<String, Argument>();

        ArgumentParser parser = ArgumentParsers.newArgumentParser("jasperstarter", false, "-", "@")
                .version(config.getVersionString());

        //ArgumentGroup groupOptions = parser.addArgumentGroup("options");

        parser.addArgument("-h", "--help").action(Arguments.help()).help("show this help message and exit");
        parser.addArgument("--locale").dest(Dest.LOCALE).metavar("<lang>")
                .help("set locale with two-letter ISO-639 code"
                + " or a combination of ISO-639 and ISO-3166 like de_DE");
        parser.addArgument("-v", "--verbose").dest(Dest.DEBUG).action(Arguments.storeTrue()).help("display additional messages");
        parser.addArgument("-V", "--version").action(Arguments.version()).help("display version information and exit");

        Subparsers subparsers = parser.addSubparsers().title("commands").
                help("type <cmd> -h to get help on command").metavar("<cmd>").
                dest(Dest.COMMAND);

        Subparser parserCompile =
                subparsers.addParser("compile", true).aliases("cp")
                .help("compile reports");
        createCompileArguments(parserCompile);

        Subparser parserProcess =
                subparsers.addParser("process", true).aliases("pr")
                .help("view, print or export an existing report");
        createProcessArguments(parserProcess);

        Subparser parserListPrinters =
                subparsers.addParser("list_printers", true).aliases("printers", "lpr")
                .help("lists available printers");

        Subparser parserListParams =
                subparsers.addParser("list_parameters", true).aliases("params", "lpa").
                help("list parameters from a given report");
        createListParamsArguments(parserListParams);

        return parser;
    }

    private void createCompileArguments(Subparser parser) {
        ArgumentGroup groupOptions = parser.addArgumentGroup("options");
        groupOptions.addArgument("input").metavar("<input>").dest(Dest.INPUT).required(true).help("input file (.jrxml) or directory");
        groupOptions.addArgument("-o").metavar("<output>").dest(Dest.OUTPUT).help("directory or basename of outputfile(s)");
    }

    private void createListParamsArguments(Subparser parser) {
        ArgumentGroup groupOptions = parser.addArgumentGroup("options");
        groupOptions.addArgument("input").metavar("<input>").dest(Dest.INPUT).required(true).help("input file (.jrxml) or (.jasper)");
    }

    private void createProcessArguments(Subparser parser) {
        ArgumentGroup groupOptions = parser.addArgumentGroup("options");
        groupOptions.addArgument("-f").metavar("<fmt>").dest(Dest.OUTPUT_FORMATS).
                required(true).nargs("+").type(Arguments.enumType(OutputFormat.class)).
                help("view, print, pdf, rtf, xls, xlsMeta, xlsx, docx, odt, ods, pptx, csv, csvMeta, html, xhtml, xml, jrprint");
        groupOptions.addArgument("input").metavar("<input>").dest(Dest.INPUT).required(true).help("input file (.jrxml|.jasper|.jrprint)");
        groupOptions.addArgument("-o").
                metavar("<output>").
                dest(Dest.OUTPUT).
                help("directory or basename of outputfile(s), use '-' for stdout");
        //groupOptions.addArgument("-h", "--help").action(Arguments.help()).help("show this help message and exit");

        ArgumentGroup groupCompileOptions = parser.addArgumentGroup("compile options");
        groupCompileOptions.addArgument("-w", "--write-jasper").
                dest(Dest.WRITE_JASPER).action(Arguments.storeTrue()).help("write .jasper file to input dir if jrxml is processed");

        ArgumentGroup groupFillOptions = parser.addArgumentGroup("fill options");
        groupFillOptions.addArgument("-a").metavar("<filter>").dest(Dest.ASK)
                .type(Arguments.enumType(AskFilter.class)).nargs("?")
                .setConst(AskFilter.p)
                .help("ask for report parameters. Filter: a, ae, u, ue, p, pe"
                + " (see usage)");
        groupFillOptions.addArgument("-P").metavar("<param>").dest(Dest.PARAMS)
                .nargs("+").help(
                "report parameter: name=value [...]");
        groupFillOptions.addArgument("-r").metavar("<resource>").dest(Dest.RESOURCE)
                .nargs("?").setConst("").help(
                "path to report resource dir or jar file. If <resource> is not"
                + " given the input directory is used.");

        ArgumentGroup groupDatasourceOptions = parser.addArgumentGroup("datasource options");
        groupDatasourceOptions.addArgument("-t").metavar("<dstype>").dest(Dest.DS_TYPE).
                required(false).type(Arguments.enumType(DsType.class)).setDefault(DsType.none).
                help("datasource type: none, csv, xml, json, jsonql, mysql, postgres, oracle, generic (jdbc)");
        Argument argDbHost = groupDatasourceOptions.addArgument("-H").metavar("<dbhost>").dest(Dest.DB_HOST).help("database host");
        Argument argDbUser = groupDatasourceOptions.addArgument("-u").metavar("<dbuser>").dest(Dest.DB_USER).help("database user");
        Argument argDbPasswd = groupDatasourceOptions.addArgument("-p").metavar("<dbpasswd>").dest(Dest.DB_PASSWD).setDefault("").help("database password");
        Argument argDbName = groupDatasourceOptions.addArgument("-n").metavar("<dbname>").dest(Dest.DB_NAME).help("database name");
        Argument argDbSid = groupDatasourceOptions.addArgument("--db-sid").metavar("<sid>").dest(Dest.DB_SID).help("oracle sid");
        Argument argDbPort = groupDatasourceOptions.addArgument("--db-port").metavar("<port>").dest(Dest.DB_PORT).type(Integer.class).help("database port");
        Argument argDbDriver = groupDatasourceOptions.addArgument("--db-driver").metavar("<name>").dest(Dest.DB_DRIVER).help("jdbc driver class name for use with type: generic");
        Argument argDbUrl = groupDatasourceOptions.addArgument("--db-url").metavar("<jdbcUrl>").dest(Dest.DB_URL).help("jdbc url without user, passwd with type:generic");
        groupDatasourceOptions.addArgument("--jdbc-dir").metavar("<dir>").dest(Dest.JDBC_DIR).type(File.class).help("directory where jdbc driver jars are located. Defaults to ./jdbc");
        Argument argDataFile = groupDatasourceOptions.addArgument("--data-file").
            metavar("<file>").
            dest(Dest.DATA_FILE).
            type(Arguments.fileType().acceptSystemIn().verifyCanRead()).
            help("input file for file based datasource, use '-' for stdin");
        groupDatasourceOptions.addArgument("--csv-first-row").metavar("true", "false").dest(Dest.CSV_FIRST_ROW).action(Arguments.storeTrue()).help("first row contains column headers");
        Argument argCsvColumns = groupDatasourceOptions.addArgument("--csv-columns").metavar("<list>").dest(Dest.CSV_COLUMNS).help("Comma separated list of column names");
        groupDatasourceOptions.addArgument("--csv-record-del").metavar("<delimiter>").dest(Dest.CSV_RECORD_DEL).setDefault(System.getProperty("line.separator")).help("CSV Record Delimiter - defaults to line.separator");
        groupDatasourceOptions.addArgument("--csv-field-del").metavar("<delimiter>").dest(Dest.CSV_FIELD_DEL).setDefault(",").help("CSV Field Delimiter - defaults to \",\"");
        groupDatasourceOptions.addArgument("--csv-charset").metavar("<charset>").dest(Dest.CSV_CHARSET).setDefault("utf-8").help("CSV charset - defaults to \"utf-8\"");
        Argument argXmlXpath = groupDatasourceOptions.addArgument("--xml-xpath").metavar("<xpath>").dest(Dest.XML_XPATH).help("XPath for XML Datasource");
        Argument argJsonQuery = groupDatasourceOptions.addArgument("--json-query").metavar("<jsonquery>").dest(Dest.JSON_QUERY).help("JSON query string for JSON Datasource");
        Argument argJsonQLQuery = groupDatasourceOptions.addArgument("--jsonql-query").metavar("<jsonqlquery>").dest(Dest.JSONQL_QUERY).help("JSONQL query string for JSONQL Datasource");

        ArgumentGroup groupOutputOptions = parser.addArgumentGroup("output options");
        groupOutputOptions.addArgument("-N").metavar("<printername>").dest(Dest.PRINTER_NAME).help("name of printer");
        groupOutputOptions.addArgument("-d").dest(Dest.WITH_PRINT_DIALOG).action(Arguments.storeTrue()).help("show print dialog when printing");
        groupOutputOptions.addArgument("-s").metavar("<reportname>").dest(Dest.REPORT_NAME).help("set internal report/document name when printing");
        groupOutputOptions.addArgument("-c").metavar("<copies>").dest(Dest.COPIES)
                .type(Integer.class).choices(Arguments.range(1, Integer.MAX_VALUE))
                .help("number of copies. Defaults to 1");
        groupOutputOptions.addArgument("--out-field-del").metavar("<delimiter>").dest(Dest.OUT_FIELD_DEL).setDefault(",").help("Export CSV (Metadata) Field Delimiter - defaults to \",\"");
        groupOutputOptions.addArgument("--out-charset").metavar("<charset>").dest(Dest.OUT_CHARSET).setDefault("utf-8").help("Export CSV (Metadata) Charset - defaults to \"utf-8\"");
        
        allArguments.put(argDbHost.getDest(), argDbHost);
        allArguments.put(argDbUser.getDest(), argDbUser);
        allArguments.put(argDbPasswd.getDest(), argDbPasswd);
        allArguments.put(argDbName.getDest(), argDbName);
        allArguments.put(argDbSid.getDest(), argDbSid);
        allArguments.put(argDbPort.getDest(), argDbPort);
        allArguments.put(argDbDriver.getDest(), argDbDriver);
        allArguments.put(argDbUrl.getDest(), argDbUrl);
        allArguments.put(argDataFile.getDest(), argDataFile);
        allArguments.put(argCsvColumns.getDest(), argCsvColumns);
        allArguments.put(argXmlXpath.getDest(), argXmlXpath);
        allArguments.put(argJsonQuery.getDest(), argJsonQuery);
        allArguments.put(argJsonQLQuery.getDest(), argJsonQLQuery);
    }

    private void parseArgumentParser(String[] args, ArgumentParser parser, Config config) throws ArgumentParserException {
        parser.parseArgs(args, config);
        // change some arguments to required depending on db-type
        if (config.hasDbType()) {
            if (config.getDbType().equals(DsType.none)) {
                // nothing to do here
            } else if (config.getDbType().equals(DsType.mysql)) {
                allArguments.get(Dest.DB_HOST).required(true);
                allArguments.get(Dest.DB_USER).required(true);
                allArguments.get(Dest.DB_NAME).required(true);
                allArguments.get(Dest.DB_PORT).setDefault(DsType.mysql.getPort());
            } else if (config.getDbType().equals(DsType.postgres)) {
                allArguments.get(Dest.DB_HOST).required(true);
                allArguments.get(Dest.DB_USER).required(true);
                allArguments.get(Dest.DB_NAME).required(true);
                allArguments.get(Dest.DB_PORT).setDefault(DsType.postgres.getPort());
            } else if (config.getDbType().equals(DsType.oracle)) {
                allArguments.get(Dest.DB_HOST).required(true);
                allArguments.get(Dest.DB_USER).required(true);
                allArguments.get(Dest.DB_PASSWD).required(true);
                allArguments.get(Dest.DB_SID).required(true);
                allArguments.get(Dest.DB_PORT).setDefault(DsType.oracle.getPort());
            } else if (config.getDbType().equals(DsType.generic)) {
                allArguments.get(Dest.DB_DRIVER).required(true);
                allArguments.get(Dest.DB_URL).required(true);
            } else if (DsType.csv.equals(config.getDbType())) {
                allArguments.get(Dest.DATA_FILE).required(true);
                if (!config.getCsvFirstRow()) {
                    allArguments.get(Dest.CSV_COLUMNS).required(true);
                }
            } else if (DsType.xml.equals(config.getDbType())) {
              allArguments.get(Dest.DATA_FILE).required(true);
            } else if (DsType.json.equals(config.getDbType())) {
              allArguments.get(Dest.DATA_FILE).required(true);
            } else if (DsType.jsonql.equals(config.getDbType())) {
              allArguments.get(Dest.DATA_FILE).required(true);
            }
        }
        // parse again so changed arguments become effectiv
        parser.parseArgs(args, config);
    }

    /**
     * <p>listReportParams.</p>
     *
     * @param config a {@link de.cenote.jasperstarter.Config} object.
     * @param input a {@link java.io.File} object.
     * @throws java.lang.IllegalArgumentException if any.
     */
    public static void listReportParams(Config config, File input) throws IllegalArgumentException {
        boolean all;
        Report report = new Report(config, input);
        JRParameter[] params = report.getReportParameters();
        int maxName = 1;
        int maxClassName = 1;
        int maxDesc = 1;
        all = false; // this is a default for now
        // determine proper length of stings for nice alignment
        for (JRParameter param : params) {
            if (!param.isSystemDefined() || all) {
                if (param.getName() != null) {
                    maxName = Math.max(maxName, param.getName().length());
                }
                if (param.getValueClassName() != null) {
                    maxClassName = Math.max(maxClassName, param.getValueClassName().length());
                }
                if (param.getDescription() != null) {
                    maxDesc = Math.max(maxDesc, param.getDescription().length());
                }
            }
        }
        for (JRParameter param : params) {
            if (!param.isSystemDefined() || all) {
                System.out.printf("%s %-" + maxName + "s %-" + maxClassName + "s %-" + maxDesc + "s %n",
                        //(param.isSystemDefined() ? "S" : "U"),
                        (param.isForPrompting() ? "P" : "N"),
                        param.getName(),
                        param.getValueClassName(),
                        (param.getDescription() != null ? param.getDescription() : ""));
            }
        }
    }
}
