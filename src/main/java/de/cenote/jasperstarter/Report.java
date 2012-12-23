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

import de.cenote.jasperstarter.types.DbType;
import de.cenote.jasperstarter.types.Dest;
import de.cenote.tools.printing.Printerlookup;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.oasis.JROdtExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 *
 * @author Volker Voßkämper <vvo at cenote.de>
 * @version $Revision: 5b92831f1a80:54 branch:default $
 */
public class Report {

    private File input;
    private File jrprintFile;
    private File output;

    Report() {
        Namespace namespace = App.getInstance().getNamespace();
        
        this.input = new File(namespace.getString(Dest.INPUT)).getAbsoluteFile();
        if (!this.input.isDirectory() & !this.input.isFile()) {
            // maybe the user omitted the file extension
            this.input = new File(this.input.getAbsolutePath() + ".jasper");
            if (namespace.getBoolean(Dest.DEBUG)) {
                System.out.println("Input: appending \".jasper\" to filename");
            }
        }
        // get the basename of inputfile
        String inputBasename = this.input.getName().split("\\.(?=[^\\.]+$)")[0];
        if ( namespace.getString(Dest.OUTPUT)==null ) {
            // if output is omitted, use parent dir of input file
            this.output = this.input.getParentFile();
        } else {
            this.output = new File(namespace.getString(Dest.OUTPUT)).getAbsoluteFile();
        }
        if (this.output.isDirectory()) {
            // if output is an existing directory, add the basename of input
            this.output = new File(this.output, inputBasename);
        }
        if (namespace.getBoolean(Dest.KEEP)) {
            System.out.println("option --keep found");
            this.jrprintFile = new File(this.output.getAbsolutePath() + ".jrprint");
        } else {
            try {
                this.jrprintFile = File.createTempFile("jasper", ".jrprint");
            } catch (IOException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
            this.jrprintFile.deleteOnExit();

        }
        if (namespace.getBoolean(Dest.DEBUG)) {
            System.out.println("Input absolute :  " + input.getAbsolutePath());
            try {
                System.out.println("Input canonical:  " + input.getCanonicalPath());
            } catch (IOException ex) {
                Logger.getLogger(Report.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Input:            " + input.getName());
            System.out.println("Input basename:   " + inputBasename);
            System.out.println("Jrprint:          " + jrprintFile.getAbsolutePath());
            if (namespace.get(Dest.OUTPUT)!= null) {
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
        if (!this.input.isFile()) {
            // Todo: This is an error
        }
    }

    public void fill() {
Namespace namespace = App.getInstance().getNamespace();
        try {
            Map parameters = getReportParams();
            if (DbType.none.equals(namespace.get(Dest.DB_TYPE))) {
                JasperFillManager.fillReportToFile(this.input.getAbsolutePath(), this.jrprintFile.getAbsolutePath(), parameters, new JREmptyDataSource());
            } else {
                Db db = new Db();
                Connection con = db.getConnection();
                JasperFillManager.fillReportToFile(this.input.getAbsolutePath(), this.jrprintFile.getAbsolutePath(), parameters, con);
                con.close();
            }
        } catch (Exception e) {
            Logger.getLogger(Db.class.getName()).log(Level.SEVERE, null, e);
            System.exit(1);
        }
    }

    public void print() throws JRException {
        Namespace namespace = App.getInstance().getNamespace();
        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        //printRequestAttributeSet.add(MediaSizeName.ISO_A4);
        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
        //printServiceAttributeSet.add(new PrinterName("Fax", null));
        JRPrintServiceExporter exporter = new JRPrintServiceExporter();
        JasperPrint jasperPrint = (JasperPrint) JRLoader.loadObject(this.jrprintFile);
        if (namespace.get(Dest.REPORT_NAME)!= null ) {
            jasperPrint.setName(namespace.getString(Dest.REPORT_NAME));
        }
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        //exporter.setParameter(JRExporterParameter.INPUT_FILE, this.jrprintFile);
        if (namespace.get(Dest.PRINTER_NAME)!= null) {
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
        if (namespace.getBoolean(Dest.WITH_PRINT_DIALOG) ) {
            setLookAndFeel();
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.TRUE);
        } else {
            exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, Boolean.FALSE);
        }
        exporter.exportReport();
    }

    public void view() throws JRException {
        setLookAndFeel();
        JasperViewer.viewReport(this.jrprintFile.getAbsolutePath(), false, false);
    }

    public void exportPdf() throws JRException {
        JasperExportManager.exportReportToPdfFile(this.jrprintFile.getAbsolutePath(),
                this.output.getAbsolutePath() + ".pdf");
    }

    public void exportRtf() throws JRException {
        JasperPrint jasperPrint = (JasperPrint) JRLoader.loadObject(this.jrprintFile);
        JRRtfExporter exporter = new JRRtfExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
                this.output.getAbsolutePath() + ".rtf");
        exporter.exportReport();
    }

    public void exportDocx() throws JRException {
        JasperPrint jasperPrint = (JasperPrint) JRLoader.loadObject(this.jrprintFile);
        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
                this.output.getAbsolutePath() + ".docx");
        exporter.exportReport();
    }

    public void exportOdt() throws JRException {
        JasperPrint jasperPrint = (JasperPrint) JRLoader.loadObject(this.jrprintFile);
        JROdtExporter exporter = new JROdtExporter();
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
                this.output.getAbsolutePath() + ".odt");
        exporter.exportReport();
    }

    public void exportHtml() throws JRException {
        JasperExportManager.exportReportToHtmlFile(this.jrprintFile.getAbsolutePath(),
                this.output.getAbsolutePath() + ".html");
    }

    private Map getReportParams() {
        Namespace namespace = App.getInstance().getNamespace();
        Map parameters = new HashMap();
        List<String> params;
        if (namespace.get(Dest.PARAMS) !=null ) {
            params = namespace.getList(Dest.PARAMS);
            String paramName = null;
            String paramType = null;
            String paramValue = null;

            //Preparing parameters
//		Image image = 
//			Toolkit.getDefaultToolkit().createImage(
//				JRLoader.loadBytesFromResource("dukesign.jpg")
//				);
//		MediaTracker traker = new MediaTracker(new Panel());
//		traker.addImage(image, 0);
//		try
//		{
//			traker.waitForID(0);
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}

            for (String p : params) {
                try {
                    //System.out.println(p);
                    paramName = p.split("=")[0];
                    paramType = p.split("=")[1].split(":")[0];
                    paramValue = p.split("=")[1].split(":")[1];
                    if (namespace.getBoolean(Dest.DEBUG)) {
                        System.out.println("Using report parameter: " + paramName + " " + paramType + " " + paramValue);
                    }
                } catch (Exception e) {
                    Logger.getLogger(Report.class.getName()).log(Level.SEVERE, "Wrong report param format! " + p, e);
                    System.exit(1);
                }
                try {
                    if ("string".equals(paramType.toLowerCase())) {
                        parameters.put(paramName, paramValue);
                    } else if ("int".equals(paramType.toLowerCase()) | "integer".equals(paramType.toLowerCase())) {
                        parameters.put(paramName, new Integer(paramValue));
                    } else if ("date".equals(paramType.toLowerCase())) {
                        // Date must be in ISO8601 format. Example 2012-12-31
                        DateFormat formatter = new SimpleDateFormat("yy-MM-dd");
                        parameters.put(paramName, (Date) formatter.parse(paramValue));
                    } else {
                        throw new Exception("Invalid param type!");
                    }
                } catch (Exception e) {
                    Logger.getLogger(Report.class.getName()).log(Level.SEVERE, "Invalid param type! " + p, e);
                    System.exit(1);
                }
            }
        }
        return parameters;
    }

    private static void setLookAndFeel() {
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }
    }
}
