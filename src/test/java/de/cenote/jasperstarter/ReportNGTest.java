/*
 * Copyright 2013-2015 Cenote GmbH.
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

import de.cenote.jasperstarter.types.DsType;
import de.cenote.jasperstarter.types.OutputFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import net.sf.jasperreports.engine.JRParameter;
import static org.testng.Assert.*;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 *
 * @author Volker Voßkämper <vvo at cenote.de>
 */
public class ReportNGTest {

    public ReportNGTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of compileToFile method, of class Report.
     */
    @Test
    public void testCompileToFile() throws Exception {
        System.out.println("compileToFile");
        Config config = null;
        config = new Config();
        //config.input = "target/test-classes/reports/report1.jrxml";
        config.input = "target/test-classes/reports/csv.jrxml";
        config.output = "target/test-classes/reports/compileToFile";
        Report instance = new Report(config, new File(config.getInput()));
        instance.compileToFile();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.jasper")).exists(), true);
    }
    
    /**
     * Test of compileToFile method, of class Report.
     * This report uses funktions with dependency to jasperreports-functions 
     */
    @Test
    public void testCompileToFileJasperreportsFunctions() throws Exception {
        System.out.println("compileToFileJasperreportsFunctions");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/charactersetTestWithStudioBuiltinFunctions.jrxml";
        config.output = "target/test-classes/reports/charactersetTestWithStudioBuiltinFunctions";
        Report instance = new Report(config, new File(config.getInput()));
        instance.compileToFile();
        assertEquals(((File) new File("target/test-classes/reports/charactersetTestWithStudioBuiltinFunctions.jasper")).exists(), true);
    }

    /**
     * Test of compileToFile method, of class Report.
     * This report uses funktions with dependency to jasperreports-functions
     */
    @Test
    public void testCompileToFileJavaScript() throws Exception {
        System.out.println("compileToFileJavaScript");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/charactersetTestWithJavaScript.jrxml";
        config.output = "target/test-classes/reports/charactersetTestWithJavaScript";
        Report instance = new Report(config, new File(config.getInput()));
        instance.compileToFile();
        assertEquals(((File) new File("target/test-classes/reports/charactersetTestWithJavaScript.jasper")).exists(), true);
    }

    /**
     * Test of fill method, of class Report.
     * This report uses funktions with dependency to jasperreports-functions 
     */
    @Test(dependsOnMethods = {"testCompileToFileJasperreportsFunctions"})
    public void testFillJasperreportsFunctions() throws Exception {
        System.out.println("fillJasperreportsFunctions");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/charactersetTestWithStudioBuiltinFunctions.jasper";
        //config.dbType = DbType.none;
        config.dbType = DsType.csv;
        config.dataFile = new File("target/test-classes/csvExampleHeaders.csv");
        config.csvCharset = "utf8";
        config.csvFieldDel = "|";
        config.csvRecordDel = "\r\n";
        config.csvFirstRow = true;
        config.outputFormats = new ArrayList<OutputFormat>(Arrays.asList(OutputFormat.jrprint));
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        assertEquals(((File) new File("target/test-classes/reports/charactersetTestWithStudioBuiltinFunctions.jrprint")).exists(), true);
    }
    
    /**
     * Test of compileToFile method, of class Report.
     * This report uses functions with dependency to jasperreports-functions 
     */
    @Test
    public void testCompileToFileJasperreportsFunctions2() throws Exception {
        System.out.println("compileToFileJasperreportsFunctions2");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/Blank_A4_1.jrxml";
        config.output = "target/test-classes/reports/Blank_A4_1";
        Report instance = new Report(config, new File(config.getInput()));
        instance.compileToFile();
        assertEquals(((File) new File("target/test-classes/reports/charactersetTestWithStudioBuiltinFunctions.jasper")).exists(), true);
    }    

    /**
     * Test of fill method, of class Report.
     * This report uses functions with dependency to jasperreports-functions 
     */
    @Test(dependsOnMethods = {"testCompileToFileJasperreportsFunctions2"})
    public void testFillJasperreportsFunctions2() throws Exception {
        System.out.println("fillJasperreportsFunctions2");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/Blank_A4_1.jasper";
        //config.dbType = DbType.none;
        config.dbType = DsType.none;
        config.outputFormats = new ArrayList<OutputFormat>(Arrays.asList(OutputFormat.jrprint));
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        assertEquals(((File) new File("target/test-classes/reports/Blank_A4_1.jrprint")).exists(), true);
    }  
    
    /**
     * Test of fill method, of class Report.
     */
    @Test(dependsOnMethods = {"testCompileToFile"})
    public void testFill() throws Exception {
        System.out.println("fill");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jasper";
        //config.dbType = DbType.none;
        config.dbType = DsType.csv;
        config.dataFile = new File("target/test-classes/csvExampleHeaders.csv");
        config.csvCharset = "utf8";
        config.csvFieldDel = "|";
        config.csvRecordDel = "\r\n";
        config.csvFirstRow = true;
        config.outputFormats = new ArrayList<OutputFormat>(Arrays.asList(OutputFormat.jrprint));
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.jrprint")).exists(), true);
    }

    /**
     * Test of fill method for metadata export, of class Report.
     */
    @Test
    public void testFillMeta() throws Exception {
        System.out.println("fillMeta");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/csvMeta.jrxml";
        config.dbType = DsType.csv;
        config.dataFile = new File("target/test-classes/csvExampleHeaders.csv");
        config.csvCharset = "utf8";
        config.csvFieldDel = "|";
        config.csvRecordDel = "\r\n";
        config.csvFirstRow = true;
        config.outputFormats = new ArrayList<OutputFormat>(Arrays.asList(OutputFormat.jrprint));
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        assertEquals(((File) new File("target/test-classes/reports/csvMeta.jrprint")).exists(), true);
    }

    /**
     * Test of print method, of class Report.
     */
//    @Test
//    public void testPrint() throws Exception {
//        System.out.println("print");
//        Report instance = null;
//        instance.print();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Test of view method, of class Report.
     */
//    @Test
//    public void testView() throws Exception {
//        System.out.println("view");
//        Report instance = null;
//        instance.view();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Test of exportPdf method, of class Report.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportPdf() throws Exception {
        System.out.println("exportPdf");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.exportPdf();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.pdf")).exists(), true);
    }

    /**
     * Test of exportRtf method, of class Report.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportRtf() throws Exception {
        System.out.println("exportRtf");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.exportRtf();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.rtf")).exists(), true);
    }

    /**
     * Test of exportDocx method, of class Report.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportDocx() throws Exception {
        System.out.println("exportDocx");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.exportDocx();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.docx")).exists(), true);
    }

    /**
     * Test of exportOdt method, of class Report.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportOdt() throws Exception {
        System.out.println("exportOdt");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.exportOdt();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.odt")).exists(), true);
    }

    /**
     * Test of exportHtml method, of class Report.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportHtml() throws Exception {
        System.out.println("exportHtml");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.exportHtml();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.html")).exists(), true);
    }

    /**
     * Test of exportXml method, of class Report.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportXml() throws Exception {
        System.out.println("exportXml");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.exportXml();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.xml")).exists(), true);
    }

    /**
     * Test of exportXls method, of class Report.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportXls() throws Exception {
        System.out.println("exportXls");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.exportXls();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.xls")).exists(), true);
        // Read the content of a cell:
        InputStream inputStream = new FileInputStream("target/test-classes/reports/compileToFile.xls");
        HSSFWorkbook wb = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = wb.getSheetAt(0);       // first sheet
        // select cell C12
        HSSFRow row     = sheet.getRow(11);
        HSSFCell cell   = row.getCell(2);
        assertEquals(cell.getStringCellValue(), "Carl Grant");
    }
    
    /**
     * Test of exportXlsMeta method, of class Report.
     */
    @Test(dependsOnMethods = {"testFillMeta"})
    public void testExportXlsMeta() throws Exception {
        System.out.println("exportXlsMeta");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/csvMeta.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.exportXlsMeta();
        assertEquals(((File) new File("target/test-classes/reports/csvMeta.xls")).exists(), true);
        // Read the content of a cell:
        InputStream inputStream = new FileInputStream("target/test-classes/reports/csvMeta.xls");
        HSSFWorkbook wb = new HSSFWorkbook(inputStream);
        HSSFSheet sheet = wb.getSheetAt(0);       // first sheet
        // select cell C12
        HSSFRow row     = sheet.getRow(11);
        HSSFCell cell   = row.getCell(2);
        assertEquals(cell.getStringCellValue(), "Dampremy");
    }

    /**
     * Test of exportXlsx method, of class Report.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportXlsx() throws Exception {
        System.out.println("exportXlsx");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.exportXlsx();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.xlsx")).exists(), true);
    }

    /**
     * Test of exportCsv method, of class Report.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportCsv() throws Exception {
        System.out.println("exportCsv");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        config.outCharset = "utf-8";
        Report instance = new Report(config, new File(config.getInput()));
        instance.exportCsv();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.csv")).exists(), true);
        // now read the file - it could have 0 bytes if something goes wrong
        BufferedReader in = new BufferedReader(new FileReader(
        		"target/test-classes/reports/compileToFile.csv"));
        // check the 3. line
        in.readLine();
        in.readLine();
        assertEquals(in.readLine(), ",Name,Street,,City,Phone,");
    }

    /**
     * Test of exportCsvMeta method, of class Report.
     */
    @Test(dependsOnMethods = {"testFillMeta"})
    public void testExportCsvMeta() throws Exception {
        System.out.println("exportCsvMeta");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/csvMeta.jrprint";
        config.outCharset = "utf-8";
        config.outFieldDel = "|";
        Report instance = new Report(config, new File(config.getInput()));
        instance.exportCsvMeta();
        assertEquals(((File) new File("target/test-classes/reports/csvMeta.csv")).exists(), true);
        // now read the file - it could have 0 bytes if something goes wrong
        BufferedReader in = new BufferedReader(new FileReader(
        		"target/test-classes/reports/csvMeta.csv"));
        // check the 3. line
        in.readLine();
        in.readLine();
        assertEquals(in.readLine(), "Carl Grant|Ap #507-5431 Consectetuer, Avenue|Chippenham|1-472-350-4152");
    }

    /**
     * Test of exportOds method, of class Report.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportOds() throws Exception {
        System.out.println("exportOds");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.exportOds();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.ods")).exists(), true);
    }

    /**
     * Test of exportPptx method, of class Report.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportPptx() throws Exception {
        System.out.println("exportPptx");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.exportPptx();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.pptx")).exists(), true);
    }

    /**
     * Test of exportXhtml method, of class Report.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportXhtml() throws Exception {
        System.out.println("exportXhtml");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.exportXhtml();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.x.html")).exists(), true);
    }

    /**
     * Test of setLookAndFeel method, of class Report.
     */
//    @Test
//    public void testSetLookAndFeel() {
//        System.out.println("setLookAndFeel");
//        Report.setLookAndFeel();
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Test of getReportParameters method, of class Report.
     */
    @Test
    public void testGetReportParameters() throws Exception {
        System.out.println("getReportParameters");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/noDB-params.jrxml";
        Report instance = new Report(config, new File(config.getInput()));
        JRParameter[] result = instance.getReportParameters();
        // there are 20 system parameters and 4 user parameters
        // take the first user parameter        
        assertEquals(result[20].getName(), "myString");
    }
    
    /**
     * Test of fill method with xml datasource, of class Report.
     */
    @Test
    public void testFillFromXmlDatasource() throws Exception {
        System.out.println("fill from xmldatasource");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/CancelAck.jrxml";
        //config.dbType = DbType.none;
        config.dbType = DsType.xml;
        config.dataFile = new File("target/test-classes/CancelAck.xml");
        config.xmlXpath = "/CancelResponse/CancelResult/ID";
        config.outputFormats = new ArrayList<OutputFormat>(Arrays.asList(OutputFormat.jrprint));
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        assertEquals(((File) new File("target/test-classes/reports/CancelAck.jrprint")).exists(), true);
    }
    
    /**
     * Test of fill method with json datasource, of class Report.
     */
    @Test
    public void testFillFromJsonDatasource() throws Exception {
        System.out.println("fill from jsondatasource");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/json.jrxml";
        config.dbType = DsType.json;
        config.dataFile = new File("target/test-classes/contacts.json");
        config.jsonQuery = "contacts.person";
        config.outputFormats = new ArrayList<OutputFormat>(Arrays.asList(OutputFormat.jrprint));
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        assertEquals(((File) new File("target/test-classes/reports/json.jrprint")).exists(), true);
    }
}