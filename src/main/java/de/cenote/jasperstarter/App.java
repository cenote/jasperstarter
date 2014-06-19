/*
 * Copyright 2012 Cenote GmbH.
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
import de.cenote.jasperstarter.types.DbType;
import de.cenote.jasperstarter.types.Dest;
import de.cenote.jasperstarter.types.OutputFormat;
import de.cenote.tools.classpath.ApplicationClasspath;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
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
import net.sourceforge.argparse4j.inf.FeatureControl;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;
import net.sourceforge.argparse4j.inf.Subparsers;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.WildcardFileFilter;

/**
 *
 * @author Volker Voßkämper <vvo at cenote.de>
 * @version $Revision: 349bcea5768c:59 branch:default $
 */
public class App {

    private Namespace namespace = null;
    private Map<String, Argument> allArguments = null;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Config config = null;
        try {
            config = new Config();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
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
            System.out.print("Command line:");
            for (String arg : args) {
                System.out.print(" " + arg);
            }
            // @todo: this makes sense only if Config.toString() is overwitten
//            System.out.print("\n");
//            System.out.println(config);
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
            System.err.println(ex.getMessage());
            System.exit(1);
        } catch (InterruptedException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        } catch (JRException ex) {
            System.err.println(ex.getMessage());
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
                    System.out.println("Using jdbc-dir: " + jdbcDir.getAbsolutePath());
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
                            System.out.println(
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
                        System.out.println(
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
            System.out.println("Original input file: " + inputFile.getAbsolutePath());
        }
        inputFile = locateInputFile(inputFile);
        if (config.isVerbose()) {
            System.out.println("Using input file: " + inputFile.getAbsolutePath());
        }
        Report report = new Report(config, inputFile);

        report.fill();  // produces visible output file if OutputFormat.jrprint is set

        List<OutputFormat> formats = config.getOutputFormats();
        Boolean viewIt = false;
        Boolean printIt = false;

        for (OutputFormat f : formats) {
            // OutputFormat.jrprint is handled in fill()
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
            } else if (OutputFormat.xlsx.equals(f)) {
                report.exportXlsx();
            } else if (OutputFormat.csv.equals(f)) {
                report.exportCsv();
            } else if (OutputFormat.ods.equals(f)) {
                report.exportOds();
            } else if (OutputFormat.pptx.equals(f)) {
                report.exportPptx();
            } else if (OutputFormat.xhtml.equals(f)) {
                report.exportXhtml();
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
                help("view, print, pdf, rtf, xls, xlsx, docx, odt, ods, pptx, csv, html, xhtml, xml, jrprint");
        groupOptions.addArgument("input").metavar("<input>").dest(Dest.INPUT).required(true).help("input file (.jrxml|.jasper|.jrprint)");
        groupOptions.addArgument("-o").metavar("<output>").dest(Dest.OUTPUT).help("directory or basename of outputfile(s)");
        //groupOptions.addArgument("-h", "--help").action(Arguments.help()).help("show this help message and exit");

        ArgumentGroup groupCompileOptions = parser.addArgumentGroup("compile options");
        groupCompileOptions.addArgument("-w", "--write-jasper").
                dest(Dest.WRITE_JASPER).action(Arguments.storeTrue()).help("write .jasper file to imput dir if jrxml is prcessed");

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

        ArgumentGroup groupDbOptions = parser.addArgumentGroup("db options");
        groupDbOptions.addArgument("-t").metavar("<dbtype>").dest(Dest.DB_TYPE).
                required(false).type(Arguments.enumType(DbType.class)).setDefault(DbType.none).
                help("database type: none, csv, mysql, postgres, oracle, generic");
        Argument argDbHost = groupDbOptions.addArgument("-H").metavar("<dbhost>").dest(Dest.DB_HOST).help("database host");
        Argument argDbUser = groupDbOptions.addArgument("-u").metavar("<dbuser>").dest(Dest.DB_USER).help("database user");
        Argument argDbPasswd = groupDbOptions.addArgument("-p").metavar("<dbpasswd>").dest(Dest.DB_PASSWD).setDefault("").help("database password");
        Argument argDbName = groupDbOptions.addArgument("-n").metavar("<dbname>").dest(Dest.DB_NAME).help("database name");
        Argument argDbSid = groupDbOptions.addArgument("--db-sid").metavar("<sid>").dest(Dest.DB_SID).help("oracle sid");
        Argument argDbPort = groupDbOptions.addArgument("--db-port").metavar("<port>").dest(Dest.DB_PORT).type(Integer.class).help("database port");
        Argument argDbDriver = groupDbOptions.addArgument("--db-driver").metavar("<name>").dest(Dest.DB_DRIVER).help("jdbc driver class name for use with type: generic");
        Argument argDbUrl = groupDbOptions.addArgument("--db-url").metavar("<jdbcUrl>").dest(Dest.DB_URL).help("jdbc url without user, passwd with type:generic");
        groupDbOptions.addArgument("--jdbc-dir").metavar("<dir>").dest(Dest.JDBC_DIR).type(File.class).help("directory where jdbc driver jars are located. Defaults to ./jdbc");
        Argument argDataFile = groupDbOptions.addArgument("--data-file").metavar("<file>").dest(Dest.DATA_FILE).type(File.class).help("input file for file based datasource");
        groupDbOptions.addArgument("--csv-first-row").metavar("true", "false").dest(Dest.CSV_FIRST_ROW).action(Arguments.storeTrue()).help("first row contains column headers");
        Argument argCsvColumns = groupDbOptions.addArgument("--csv-columns").metavar("<list>").dest(Dest.CSV_COLUMNS).help("Comma separated list of column names");
        groupDbOptions.addArgument("--csv-record-del").metavar("<delimiter>").dest(Dest.CSV_RECORD_DEL).setDefault(System.getProperty("line.separator")).help("CSV Record Delimiter - defaults to line.separator");
        groupDbOptions.addArgument("--csv-field-del").metavar("<delimiter>").dest(Dest.CSV_FIELD_DEL).setDefault(",").help("CSV Field Delimiter - defaults to \",\"");
        groupDbOptions.addArgument("--csv-charset").metavar("<charset>").dest(Dest.CSV_CHARSET).setDefault("utf-8").help("CSV charset - defaults to \"utf-8\"");

        ArgumentGroup groupPrintOptions = parser.addArgumentGroup("print options");
        groupPrintOptions.addArgument("-N").metavar("<printername>").dest(Dest.PRINTER_NAME).help("name of printer");
        groupPrintOptions.addArgument("-d").dest(Dest.WITH_PRINT_DIALOG).action(Arguments.storeTrue()).help("show print dialog when printing");
        groupPrintOptions.addArgument("-s").metavar("<reportname>").dest(Dest.REPORT_NAME).help("set internal report/document name when printing");
        groupPrintOptions.addArgument("-c").metavar("<copies>").dest(Dest.COPIES)
                .type(Integer.class).choices(Arguments.range(1, Integer.MAX_VALUE))
                .help("number of copies. Defaults to 1");

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
    }

    private void parseArgumentParser(String[] args, ArgumentParser parser, Config config) throws ArgumentParserException {
        parser.parseArgs(args, config);
        // change some arguments to required depending on db-type
        if (config.hasDbType()) {
            if (config.getDbType().equals(DbType.none)) {
                // nothing to do here
            } else if (config.getDbType().equals(DbType.mysql)) {
                allArguments.get(Dest.DB_HOST).required(true);
                allArguments.get(Dest.DB_USER).required(true);
                allArguments.get(Dest.DB_NAME).required(true);
                allArguments.get(Dest.DB_PORT).setDefault(DbType.mysql.getPort());
            } else if (config.getDbType().equals(DbType.postgres)) {
                allArguments.get(Dest.DB_HOST).required(true);
                allArguments.get(Dest.DB_USER).required(true);
                allArguments.get(Dest.DB_NAME).required(true);
                allArguments.get(Dest.DB_PORT).setDefault(DbType.postgres.getPort());
            } else if (config.getDbType().equals(DbType.oracle)) {
                allArguments.get(Dest.DB_HOST).required(true);
                allArguments.get(Dest.DB_USER).required(true);
                allArguments.get(Dest.DB_PASSWD).required(true);
                allArguments.get(Dest.DB_SID).required(true);
                allArguments.get(Dest.DB_PORT).setDefault(DbType.oracle.getPort());
            } else if (config.getDbType().equals(DbType.generic)) {
                allArguments.get(Dest.DB_USER).required(true);
                allArguments.get(Dest.DB_DRIVER).required(true);
                allArguments.get(Dest.DB_URL).required(true);
            } else if (DbType.csv.equals(config.getDbType())) {
                allArguments.get(Dest.DATA_FILE).required(true);
                if (!config.getCsvFirstRow()) {
                    allArguments.get(Dest.CSV_COLUMNS).required(true);
                }
            }
        }
        // parse again so changed arguments become effectiv
        parser.parseArgs(args, config);
    }

    /**
     * @return the namespace
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
