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

import de.cenote.jasperstarter.gui.ParameterPanel;
import de.cenote.jasperstarter.gui.ParameterPrompt;
import de.cenote.jasperstarter.types.AskFilter;
import de.cenote.jasperstarter.types.DbType;
import de.cenote.jasperstarter.types.Dest;
import de.cenote.jasperstarter.types.InputType;
import de.cenote.jasperstarter.types.OutputFormat;
import de.cenote.tools.printing.Printerlookup;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Panel;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXhtmlExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.oasis.JROdsExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import net.sourceforge.argparse4j.inf.Namespace;
import org.apache.commons.lang.LocaleUtils;

/**
 *
 * @author Volker Voßkämper <vvo at cenote.de>
 * @version $Revision: 5b92831f1a80:54 branch:default $
 */
public class Report {

    private File inputFile;
    private InputType initialInputType;
    private JasperDesign jasperDesign;
    private JasperReport jasperReport;
    private JasperPrint jasperPrint;
    private File output;
    private Locale defaultLocale;

    /**
     *
     * @param inputFile
     * @throws IllegalArgumentException
     */
    Report(File inputFile) throws IllegalArgumentException {
        // store the given default to reset to it in fill()
        defaultLocale = Locale.getDefault();
        Namespace namespace = App.getInstance().getNamespace();

        if (namespace.getBoolean(Dest.DEBUG)) {
            System.out.println("Original input file: " + inputFile.getAbsolutePath());
        }

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
        if (namespace.getBoolean(Dest.DEBUG)) {
            System.out.println("Using input file: " + inputFile.getAbsolutePath());
        }
        this.inputFile = inputFile;

        Object inputObject = null;
        // Load the input file and try to evaluate the filetype
        // this fails in case of an jrxml file
        try {
            inputObject = JRLoader.loadObject(inputFile);
            Boolean casterror = true;
            try {
                jasperReport = (JasperReport) inputObject;
                casterror = false;
                initialInputType = InputType.JASPER_REPORT;
            } catch (ClassCastException ex) {
                // nothing to do here
            }
            try {
                jasperPrint = (JasperPrint) inputObject;
                casterror = false;
                initialInputType = InputType.JASPER_PRINT;
            } catch (ClassCastException ex) {
                // nothing to do here
            }
            if (casterror) {
                throw new IllegalArgumentException("input file: \"" + inputFile + "\" is not of a valid type");
            }
        } catch (JRException ex) {
            try {
                // now try to load it as jrxml
                jasperDesign = JRXmlLoader.load(inputFile.getAbsolutePath());
                initialInputType = InputType.JASPER_DESIGN;
                compile();
            } catch (JRException ex1) {
                throw new IllegalArgumentException("input file: \"" + inputFile + "\" is not a valid jrxml file", ex1);
            }
        }

        // generating output basename
        // get the basename of inputfile
        String inputBasename = inputFile.getName().split("\\.(?=[^\\.]+$)")[0];
        if (namespace.getString(Dest.OUTPUT) == null) {
            // if output is omitted, use parent dir of input file
            File parent = inputFile.getParentFile();
            if (parent != null) {
                this.output = parent;
            } else {
                this.output = new File(inputBasename);
            }
        } else {
            this.output = new File(namespace.getString(Dest.OUTPUT)).getAbsoluteFile();
        }
        if (this.output.isDirectory()) {
            // if output is an existing directory, add the basename of input
            this.output = new File(this.output, inputBasename);
        }
        if (namespace.getBoolean(Dest.DEBUG)) {
            System.out.println("Input absolute :  " + inputFile.getAbsolutePath());
            try {
                System.out.println("Input canonical:  " + inputFile.getCanonicalPath());
            } catch (IOException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Input:            " + inputFile.getName());
            System.out.println("Input basename:   " + inputBasename);
            if (namespace.get(Dest.OUTPUT) != null) {
                File outputParam = new File(namespace.getString(Dest.OUTPUT)).getAbsoluteFile();
                System.out.println("OutputParam:      " + outputParam.getAbsolutePath());
            }
            System.out.println("Output:           " + output.getAbsolutePath());
            try {
                System.out.println("Output canonical: " + output.getCanonicalPath());
            } catch (IOException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void compile() throws JRException {
        jasperReport = JasperCompileManager.compileReport(jasperDesign);
        Namespace namespace = App.getInstance().getNamespace();
        // this option is only available if command process is active
        if (namespace.get(Dest.WRITE_JASPER) != null) {
            if (namespace.getBoolean(Dest.WRITE_JASPER)) {
                String inputBasename = inputFile.getName().split("\\.(?=[^\\.]+$)")[0];
                String outputDir = inputFile.getParent();
                File outputFile = new File(outputDir + "/" + inputBasename + ".jasper");
                JRSaver.saveObject(jasperReport, outputFile);
            }
        }
    }

    public void compileToFile() {
        if (initialInputType == InputType.JASPER_DESIGN) {
            try {
                JRSaver.saveObject(jasperReport, this.output.getAbsolutePath() + ".jasper");
            } catch (JRException ex) {
                throw new IllegalArgumentException("outputFile" + this.output.getAbsolutePath() + ".jasper" + "could not be written");
            }
        } else {
            throw new IllegalArgumentException("input file: \"" + inputFile + "\" is not a valid jrxml file");
        }
    }

    public void fill() throws InterruptedException {
        if (initialInputType != InputType.JASPER_PRINT) {
            Namespace namespace = App.getInstance().getNamespace();
            // get commandLineReportParams
            Map parameters = getCmdLineReportParams();
            // if prompt...
            if (namespace.get(Dest.ASK) != null) {
                JRParameter[] reportParams = jasperReport.getParameters();
                parameters = promptForParams(reportParams, parameters, jasperReport.getName());
            }
            try {
                if (parameters.containsKey("REPORT_LOCALE")) {
                    Locale.setDefault((Locale) parameters.get("REPORT_LOCALE"));
                }
                if (DbType.none.equals(namespace.get(Dest.DB_TYPE))) {
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());
                } else {
                    Db db = new Db();
                    Connection con = db.getConnection();
                    jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, con);
                    con.close();
                }
                // reset to default
                Locale.setDefault(defaultLocale);
            } catch (SQLException ex) {
                throw new IllegalArgumentException("Unable to connect to database: " + ex.getMessage(), ex);
            } catch (JRException e) {
                throw new IllegalArgumentException("Error filling report" + e.getMessage(), e);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("Unable to load driver: " + e.getMessage(), e);
            } finally {
                // reset to default
                Locale.setDefault(defaultLocale);
            }
            List<OutputFormat> formats = namespace.getList(Dest.OUTPUT_FORMATS);
            try {
                if (formats.contains(OutputFormat.jrprint)) {
                    JRSaver.saveObject(jasperPrint, this.output.getAbsolutePath() + ".jrprint");
                }
                if (namespace.getBoolean(Dest.KEEP)) {
                    JRSaver.saveObject(jasperPrint, this.output.getAbsolutePath() + ".jrprint");
                }
            } catch (JRException e) {
                throw new IllegalArgumentException("Unable to write to file: " + this.output.getAbsolutePath() + ".jrprint", e);
            }
        }
    }

    public void print() throws JRException {
        Namespace namespace = App.getInstance().getNamespace();
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        //printRequestAttributeSet.add(MediaSizeName.ISO_A4);
        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
        //printServiceAttributeSet.add(new PrinterName("Fax", null));
        JRPrintServiceExporter exporter = new JRPrintServiceExporter();
        if (namespace.get(Dest.REPORT_NAME) != null) {
            jasperPrint.setName(namespace.getString(Dest.REPORT_NAME));
        }
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        //exporter.setParameter(JRExporterParameter.INPUT_FILE, this.jrprintFile);
        if (namespace.get(Dest.PRINTER_NAME) != null) {
            String printerName = namespace.getString(Dest.PRINTER_NAME);
            PrintService service = Printerlookup.getPrintservice(printerName, Boolean.TRUE, Boolean.TRUE);
            exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, service);
            if (namespace.getBoolean(Dest.DEBUG)) {

                System.out.println("printer-name: " + ((service == null) ? "No printer found with name \"" + printerName + "\"! Using default." : "found: " + service.getName()));
            }
        }
        //exporter.setParameter(JRExporterParameter.PAGE_INDEX, pageIndex);
        //exporter.setParameter(JRExporterParameter.START_PAGE_INDEX, pageStartIndex);
        //exporter.setParameter(JRExporterParameter.END_PAGE_INDEX, pageEndIndex);
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
        if (namespace.getBoolean(Dest.WITH_PRINT_DIALOG)) {
            setLookAndFeel();
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.TRUE);
        } else {
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
        }
        exporter.exportReport();
    }

    public void view() throws JRException {
        setLookAndFeel();
        JasperViewer.viewReport(jasperPrint, false);
    }

    public void exportPdf() throws JRException {
        JasperExportManager.exportReportToPdfFile(jasperPrint,
                this.output.getAbsolutePath() + ".pdf");
    }

    public void exportRtf() throws JRException {
        JRRtfExporter exporter = new JRRtfExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
                this.output.getAbsolutePath() + ".rtf");
        exporter.exportReport();
    }

    public void exportDocx() throws JRException {
        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
                this.output.getAbsolutePath() + ".docx");
        exporter.exportReport();
    }

    public void exportOdt() throws JRException {
        JROdtExporter exporter = new JROdtExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
                this.output.getAbsolutePath() + ".odt");
        exporter.exportReport();
    }

    public void exportHtml() throws JRException {
        JasperExportManager.exportReportToHtmlFile(jasperPrint,
                this.output.getAbsolutePath() + ".html");
    }

    public void exportXml() throws JRException {
        JasperExportManager.exportReportToXmlFile(jasperPrint,
                this.output.getAbsolutePath() + ".xml", false);
    }

    public void exportXls() throws JRException {
        Map dateFormats = new HashMap();
        dateFormats.put("EEE, MMM d, yyyy", "ddd, mmm d, yyyy");

        JRXlsExporter exporter = new JRXlsExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
                this.output.getAbsolutePath() + ".xls");
        exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.FORMAT_PATTERNS_MAP, dateFormats);
        exporter.exportReport();
    }

    public void exportXlsx() throws JRException {
        Map dateFormats = new HashMap();
        dateFormats.put("EEE, MMM d, yyyy", "ddd, mmm d, yyyy");

        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
                this.output.getAbsolutePath() + ".xlsx");
        //exporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
        exporter.setParameter(JRXlsExporterParameter.FORMAT_PATTERNS_MAP, dateFormats);
        exporter.exportReport();
    }

    public void exportCsv() throws JRException {
        JRCsvExporter exporter = new JRCsvExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
                this.output.getAbsolutePath() + ".csv");
        exporter.exportReport();
    }

    public void exportOds() throws JRException {
        JROdsExporter exporter = new JROdsExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
                this.output.getAbsolutePath() + ".ods");
        exporter.exportReport();
    }

    public void exportPptx() throws JRException {
        JRPptxExporter exporter = new JRPptxExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
                this.output.getAbsolutePath() + ".pptx");
        exporter.exportReport();
    }

    public void exportXhtml() throws JRException {
        JRXhtmlExporter exporter = new JRXhtmlExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
                this.output.getAbsolutePath() + ".x.html");
        exporter.exportReport();
    }

    private Map getCmdLineReportParams() {
        Namespace namespace = App.getInstance().getNamespace();
        Map parameters = new HashMap();
        List<String> params;
        if (namespace.get(Dest.PARAMS) != null) {
            params = namespace.getList(Dest.PARAMS);
            String paramName = null;
            String paramType = null;
            String paramValue = null;

            for (String p : params) {
                try {
                    paramName = p.split("=")[0];
                    paramType = p.split("=")[1].split(":", 2)[0];
                    paramValue = p.split("=", 2)[1].split(":", 2)[1];
                    if (namespace.getBoolean(Dest.DEBUG)) {
                        System.out.println("Using report parameter: " + paramName + " " + paramType + " " + paramValue);
                    }
                } catch (Exception e) {
                    throw new IllegalArgumentException("Wrong report param format! " + p, e);
                }
                try {
                    if ("string".equals(paramType.toLowerCase())) {
                        parameters.put(paramName, paramValue);
                    } else if ("int".equals(paramType.toLowerCase()) | "integer".equals(paramType.toLowerCase())) {
                        parameters.put(paramName, new Integer(paramValue));
                    } else if ("double".equals(paramType.toLowerCase())) {
                        parameters.put(paramName, new Double(paramValue));
                    } else if ("date".equals(paramType.toLowerCase())) {
                        // Date must be in ISO8601 format. Example 2012-12-31
                        DateFormat dateFormat = new SimpleDateFormat("yy-MM-dd");
                        parameters.put(paramName, (Date) dateFormat.parse(paramValue));
                    } else if ("image".equals(paramType.toLowerCase())) {
                        Image image =
                                Toolkit.getDefaultToolkit().createImage(
                                JRLoader.loadBytes(new File(paramValue)));
                        MediaTracker traker = new MediaTracker(new Panel());
                        traker.addImage(image, 0);
                        try {
                            traker.waitForID(0);
                        } catch (Exception e) {
                            throw new IllegalArgumentException("Image tracker error: " + e.getMessage(), e);
                        }
                        parameters.put(paramName, image);
                    } else if ("locale".equals(paramType.toLowerCase())) {
                        parameters.put(paramName, LocaleUtils.toLocale(paramValue));
                    } else {
                        throw new IllegalArgumentException("Invalid JasperStarter param type \"" + paramType + "\" in \"" + p + "\"");
                    }
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("NumberFormatException: " + e.getMessage() + "\" in \"" + p + "\"", e);
                } catch (java.text.ParseException e) {
                    throw new IllegalArgumentException(e.getMessage() + "\" in \"" + p + "\"", e);
                } catch (JRException e) {
                    throw new IllegalArgumentException("Unable to load image from: " + paramValue, e);
                }
            }
        }
        return parameters;
    }

    public static void setLookAndFeel() {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Map promptForParams(JRParameter[] reportParams, Map params, String reportName) throws InterruptedException {
        Namespace namespace = App.getInstance().getNamespace();
        boolean isForPromptingOnly = false;
        boolean isUserDefinedOnly = false;
        boolean emptyOnly = false;
        switch ((AskFilter) namespace.get(Dest.ASK)) {
            case ae:
                emptyOnly = true;
            case a:
                isForPromptingOnly = false;
                isUserDefinedOnly = false;
                break;
            case ue:
                emptyOnly = true;
            case u:
                isUserDefinedOnly = true;
                break;
            case pe:
                emptyOnly = true;
            case p:
                isUserDefinedOnly = true;
                isForPromptingOnly = true;
                break;
        }
        Report.setLookAndFeel();
        ParameterPrompt prompt = new ParameterPrompt(null, reportParams, params,
                reportName, isForPromptingOnly, isUserDefinedOnly, emptyOnly);
        if (JOptionPane.OK_OPTION != prompt.show()) {
            throw new InterruptedException("User aborted at parameter promt!");
        }
        if (namespace.getBoolean(Dest.DEBUG)) {
            System.out.println("----------------------------");
            System.out.println("Parameter prompt:");
            for (Object key : params.keySet()) {
                System.out.println(key + " = " + params.get(key));
            }
            System.out.println("----------------------------");
        }
        return params;
    }
}
