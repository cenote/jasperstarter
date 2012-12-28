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
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import net.sf.jasperreports.engine.JRException;
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
 *
 * @author Volker Voßkämper <vvo at cenote.de>
 * @version $Revision: 349bcea5768c:59 branch:default $
 */
public class App {

    private static App instance = null;
    private Properties applicationProperties = null;
    private Namespace namespace = null;
    private Map<String, Argument> allArguments = null;

    private App() {

        this.applicationProperties = new Properties();
        try {
            this.applicationProperties.load(this.getClass().
                    getResourceAsStream("/de/cenote/jasperstarter/application.properties"));
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

    }

    public static App getInstance() {
        if (App.instance == null) {
            App.instance = new App();
        }
        return App.instance;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        App app = App.getInstance();

        // create the command line parser
        ArgumentParser parser = app.createArgumentParser();
        if (args.length == 0) {
            System.out.println(parser.formatUsage());
            System.out.println("type: jasperstarter -h to get help");
            System.exit(0);
        }
        app.namespace = app.parseArgumentParser(args, parser);

        // setting locale if given
        if (app.namespace.get(Dest.LOCALE) != null) {
            Locale.setDefault(new Locale((String) app.namespace.get(Dest.LOCALE)));
        }

        switch (Command.getCommand(app.namespace.getString(Dest.COMMAND))) {
            case COMPILE:
            case CP:
                app.compile();
                break;
            case PROCESS:
            case PR:
                app.processReport();
                break;
            case LIST_PRINTERS:
            case LP:
                app.listPrinters();
                break;
        }
    }

    private void compile() {
        boolean error = false;
        App app = App.getInstance();
        File input = new File(app.namespace.getString(Dest.INPUT));
        if (input.isFile()) {
            try {
                Report report = new Report(input);
                report.compileToFile();
            } catch (IllegalArgumentException ex) {
                System.err.println(ex.getMessage());
                error = true;
            }
        } else if (input.isDirectory()) {
            // compile all .jrxml files in this directory
            FileFilter fileFilter = new WildcardFileFilter("*.jrxml", IOCase.INSENSITIVE);
            File[] files = input.listFiles(fileFilter);
            for (File file : files) {
                try {
                    System.out.println("Compiling: \"" + file + "\"");
                    Report report = new Report(file);
                    report.compileToFile();
                } catch (IllegalArgumentException ex) {
                    System.err.println(ex.getMessage());
                    error = true;
                }
            }
        } else {
            System.err.println("Error: not a file: " + input.getName());
            error = true;
        }
        if (error) {
            System.exit(1);
        } else {
            System.exit(0);
        }
    }

    private void processReport() {
        App app = App.getInstance();
        // add the jdbc dir to classpath
        try {
            if (app.namespace.get(Dest.JDBC_DIR) != null) {
                File jdbcDir = new File(app.namespace.get(Dest.JDBC_DIR).toString());
                if (app.namespace.getBoolean(Dest.DEBUG)) {
                    System.out.println("Using jdbc-dir: " + jdbcDir.getAbsolutePath());
                }
                ApplicationClasspath.addJars(jdbcDir.getAbsolutePath());
            } else {
                ApplicationClasspath.addJarsRelative("../jdbc");
            }
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        } catch (URISyntaxException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }

        Report report = null;
        try {
            report = new Report(new File(app.namespace.getString(Dest.INPUT)).getAbsoluteFile());
        } catch (IllegalArgumentException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        report.fill();  // produces visible output file if OutputFormat.jrprint is set
        List<OutputFormat> formats = app.namespace.getList(Dest.OUTPUT_FORMATS);
        Boolean viewIt = false;
        Boolean printIt = false;
        try {
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
        } catch (JRException ex) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(1);
        }
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

    private Properties getApplicationProperties() {
        return this.applicationProperties;
    }

    private ArgumentParser createArgumentParser() {
        this.allArguments = new HashMap<String, Argument>();
        String jasperversion = Package.getPackage("net.sf.jasperreports.engine").
                getImplementationVersion();
        StringBuffer sb = new StringBuffer("JasperStarter ")
                .append(applicationProperties.getProperty("application.version"))
                .append(" Rev ").append(applicationProperties.getProperty("application.revision"))
                .append(" ").append(applicationProperties.getProperty("application.revision.date"))
                .append("\n").append(" - JasperReports: ").append(jasperversion);

        // @todo on windows prfixChars should be "-/" but on Linux this
        //       is the path separator
        ArgumentParser parser = ArgumentParsers.newArgumentParser("jasperstarter", false, "-", "@")
                .version(sb.toString());

        //ArgumentGroup groupOptions = parser.addArgumentGroup("options");

        parser.addArgument("-h", "--help").action(Arguments.help()).help("show this help message and exit");
        parser.addArgument("--locale").dest(Dest.LOCALE).metavar("<lang>").help("set locale with two-letter ISO-639 code");
        parser.addArgument("-v", "--verbose").dest(Dest.DEBUG).action(Arguments.storeTrue()).help("display additional messages");
        parser.addArgument("-V", "--version").action(Arguments.version()).help("display version information and exit");

        Subparsers subparsers = parser.addSubparsers().title("commands").
                help("type <cmd> -h to get help on command").metavar("<cmd>").
                dest(Dest.COMMAND);

        Subparser parserCompile = subparsers.addParser("cp", true).help("compile - compile reports");
        createCompileArguments(parserCompile);
        Subparser parserProcess = subparsers.addParser("pr", true).help("process - view, print or export an existing report");
        createProcessArguments(parserProcess);
        // @todo: creating aliases does not work for now because of the ambigoius allArguments elements !!
        // This does NOT work:
        //Subparser parserProc    = subparsers.addParser("proc", true).help("alias for command process");
        //createProcessArguments(parserProc);

        Subparser parserListPrinters = subparsers.addParser("lp", true).
                help("list printers - lists available printers on this system");

        return parser;
    }

    private void createCompileArguments(Subparser parser) {
        ArgumentGroup groupOptions = parser.addArgumentGroup("options");
        groupOptions.addArgument("-i").metavar("<file>").dest(Dest.INPUT).required(true).help("input file (.jrxml) or directory");
        groupOptions.addArgument("-o").metavar("<file>").dest(Dest.OUTPUT).help("directory or basename of outputfile(s)");
    }

    private void createProcessArguments(Subparser parser) {
        ArgumentGroup groupOptions = parser.addArgumentGroup("options");
        groupOptions.addArgument("-f").metavar("<fmt>").dest(Dest.OUTPUT_FORMATS).
                required(true).nargs("+").type(Arguments.enumType(OutputFormat.class)).
                help("view, print, pdf, rtf, xls, xlsx, docx, odt, ods, pptx, csv, html, xhtml, xml, jrprint");
        groupOptions.addArgument("-i").metavar("<file>").dest(Dest.INPUT).required(true).help("input file (.jrxml|.jasper|.jrprint)");
        groupOptions.addArgument("-o").metavar("<file>").dest(Dest.OUTPUT).help("directory or basename of outputfile(s)");
        //groupOptions.addArgument("-h", "--help").action(Arguments.help()).help("show this help message and exit");

        ArgumentGroup groupCompileOptions = parser.addArgumentGroup("compile options");
        groupCompileOptions.addArgument("-w", "--write-jasper").
                dest(Dest.WRITE_JASPER).action(Arguments.storeTrue()).help("write .jasper file to imput dir if jrxml is prcessed");

        ArgumentGroup groupFillOptions = parser.addArgumentGroup("fill options");
        groupFillOptions.addArgument("-P").metavar("<p>").dest(Dest.PARAMS).nargs("+").help("report parameter: name=type:value [...] | types: string, int, double, date, image");
        groupFillOptions.addArgument("-k", "--keep").dest(Dest.KEEP).action(Arguments.storeTrue()).
                help("don't delete the temporary .jrprint file. OBSOLETE use output format jrprint");

        ArgumentGroup groupDbOptions = parser.addArgumentGroup("db options");
        groupDbOptions.addArgument("-t").metavar("<dbtype>").dest(Dest.DB_TYPE).
                required(false).type(Arguments.enumType(DbType.class)).setDefault(DbType.none).
                help("database type: none, mysql, postgres, oracle, generic");
        Argument argDbHost = groupDbOptions.addArgument("-H").metavar("<dbhost>").dest(Dest.DB_HOST).help("database host");
        Argument argDbUser = groupDbOptions.addArgument("-u").metavar("<dbuser>").dest(Dest.DB_USER).help("database user");
        Argument argDbPasswd = groupDbOptions.addArgument("-p").metavar("<dbpasswd>").dest(Dest.DB_PASSWD).setDefault("").help("database password");
        Argument argDbName = groupDbOptions.addArgument("-n").metavar("<dbname>").dest(Dest.DB_NAME).help("database name");
        Argument argDbSid = groupDbOptions.addArgument("--db-sid").metavar("<sid>").dest(Dest.DB_SID).help("oracle sid");
        Argument argDbPort = groupDbOptions.addArgument("--db-port").metavar("<port>").dest(Dest.DB_PORT).type(Integer.class).help("database port");
        Argument argDbDriver = groupDbOptions.addArgument("--db-driver").metavar("<name>").dest(Dest.DB_DRIVER).help("jdbc driver class name for use with type: generic");
        Argument argDbUrl = groupDbOptions.addArgument("--db-url").metavar("<jdbcUrl>").dest(Dest.DB_URL).help("jdbc url without user, passwd with type:generic");
        groupDbOptions.addArgument("--jdbc-dir").metavar("<dir>").dest(Dest.JDBC_DIR).type(File.class).help("directory where jdbc driver jars are located. Defaults to ./jdbc");

        ArgumentGroup groupPrintOptions = parser.addArgumentGroup("print options");
        groupPrintOptions.addArgument("-N").metavar("<printername>").dest(Dest.PRINTER_NAME).help("name of printer");
        groupPrintOptions.addArgument("-d").dest(Dest.WITH_PRINT_DIALOG).action(Arguments.storeTrue()).help("show print dialog when printing");
        groupPrintOptions.addArgument("-s").metavar("<reportname>").dest(Dest.REPORT_NAME).help("set internal report/document name when printing");

        allArguments.put(argDbHost.getDest(), argDbHost);
        allArguments.put(argDbUser.getDest(), argDbUser);
        allArguments.put(argDbPasswd.getDest(), argDbPasswd);
        allArguments.put(argDbName.getDest(), argDbName);
        allArguments.put(argDbSid.getDest(), argDbSid);
        allArguments.put(argDbPort.getDest(), argDbPort);
        allArguments.put(argDbDriver.getDest(), argDbDriver);
        allArguments.put(argDbUrl.getDest(), argDbUrl);
    }

    private Namespace parseArgumentParser(String[] args, ArgumentParser parser) {
        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
            // change some arguments to required depending on db-type
            if (ns.get(Dest.DB_TYPE) != null) {
                if (ns.get(Dest.DB_TYPE).equals(DbType.none)) {
                } else if (ns.get(Dest.DB_TYPE).equals(DbType.mysql)) {
                    allArguments.get(Dest.DB_HOST).required(true);
                    allArguments.get(Dest.DB_USER).required(true);
                    allArguments.get(Dest.DB_NAME).required(true);
                    allArguments.get(Dest.DB_PORT).setDefault(DbType.mysql.getPort());
                } else if (ns.get(Dest.DB_TYPE).equals(DbType.postgres)) {
                    allArguments.get(Dest.DB_HOST).required(true);
                    allArguments.get(Dest.DB_USER).required(true);
                    allArguments.get(Dest.DB_NAME).required(true);
                    allArguments.get(Dest.DB_PORT).setDefault(DbType.postgres.getPort());
                } else if (ns.get(Dest.DB_TYPE).equals(DbType.oracle)) {
                    allArguments.get(Dest.DB_HOST).required(true);
                    allArguments.get(Dest.DB_USER).required(true);
                    allArguments.get(Dest.DB_PASSWD).required(true);
                    allArguments.get(Dest.DB_SID).required(true);
                    allArguments.get(Dest.DB_PORT).setDefault(DbType.oracle.getPort());
                } else if (ns.get(Dest.DB_TYPE).equals(DbType.generic)) {
                    allArguments.get(Dest.DB_USER).required(true);
                    allArguments.get(Dest.DB_DRIVER).required(true);
                    allArguments.get(Dest.DB_URL).required(true);
                }
            }
            // parse again so changed arguments become effectiv
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException ex) {
            parser.handleError(ex);
            System.exit(1);
        }
        if (ns.getBoolean(Dest.DEBUG)) {
            System.out.print("Command line:");
            for (String arg : args) {
                System.out.print(" " + arg);
            }
            System.out.print("\n");

            System.out.println(ns);
        }
        return ns;
    }

    /**
     * @return the namespace
     */
    public Namespace getNamespace() {
        return namespace;
    }
}
