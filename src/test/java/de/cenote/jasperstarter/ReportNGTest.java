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

import de.cenote.jasperstarter.types.AskFilter;
import de.cenote.jasperstarter.types.DsType;
import de.cenote.jasperstarter.types.OutputFormat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
 * <p>ReportNGTest class.</p>
 *
 * @author Volker Voßkämper
 * @version $Id: $Id
 * @since 3.4.0
 */
public class ReportNGTest {

    /**
     * <p>Constructor for ReportNGTest.</p>
     */
    public ReportNGTest() {
    }

    /**
     * <p>setUpClass.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    /**
     * <p>tearDownClass.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    /**
     * <p>setUpMethod.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    /**
     * <p>tearDownMethod.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of compileToFile method, of class Report.
     *
     * @throws java.lang.Exception if any.
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
     *
     * @throws java.lang.Exception if any.
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
     *
     * @throws java.lang.Exception if any.
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
     * This report uses functions with dependency to Rhino
     *
     * @throws java.lang.Exception if any.
     */
    @Test(dependsOnMethods = {"testCompileToFileJavaScript"})
    public void testFillJavascript() throws Exception {
        System.out.println("fillJavascript");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/charactersetTestWithJavaScript.jasper";
        //config.dbType = DbType.none;
        config.dbType = DsType.none;
        config.outputFormats = new ArrayList<OutputFormat>(Arrays.asList(OutputFormat.jrprint));
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        instance.exportJrprint();
        assertEquals(((File) new File("target/test-classes/reports/charactersetTestWithJavaScript.jrprint")).exists(), true);
    }

    /**
     * Test of fill method, of class Report.
     * This report uses funktions with dependency to jasperreports-functions
     *
     * @throws java.lang.Exception if any.
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
        instance.exportJrprint();
        assertEquals(((File) new File("target/test-classes/reports/charactersetTestWithStudioBuiltinFunctions.jrprint")).exists(), true);
    }
    
    /**
     * Test of compileToFile method, of class Report.
     * This report uses functions with dependency to jasperreports-functions
     *
     * @throws java.lang.Exception if any.
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
     *
     * @throws java.lang.Exception if any.
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
        instance.exportJrprint();
        assertEquals(((File) new File("target/test-classes/reports/Blank_A4_1.jrprint")).exists(), true);
    }  
    
    /**
     * Test of fill method, of class Report.
     *
     * @throws java.lang.Exception if any.
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
        instance.exportJrprint();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.jrprint")).exists(), true);
    }

    /**
     * Test of fill method for metadata export, of class Report.
     *
     * @throws java.lang.Exception if any.
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
        instance.exportJrprint();
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
     *
     * @throws java.lang.Exception if any.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportPdf() throws Exception {
        System.out.println("exportPdf");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        instance.exportPdf();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.pdf")).exists(), true);
    }

    /**
     * Test of exportRtf method, of class Report.
     *
     * @throws java.lang.Exception if any.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportRtf() throws Exception {
        System.out.println("exportRtf");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        instance.exportRtf();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.rtf")).exists(), true);
    }

    /**
     * Test of exportDocx method, of class Report.
     *
     * @throws java.lang.Exception if any.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportDocx() throws Exception {
        System.out.println("exportDocx");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        instance.exportDocx();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.docx")).exists(), true);
    }

    /**
     * Test of exportOdt method, of class Report.
     *
     * @throws java.lang.Exception if any.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportOdt() throws Exception {
        System.out.println("exportOdt");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        instance.exportOdt();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.odt")).exists(), true);
    }

    /**
     * Test of exportHtml method, of class Report.
     *
     * @throws java.lang.Exception if any.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportHtml() throws Exception {
        System.out.println("exportHtml");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        instance.exportHtml();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.html")).exists(), true);
    }

    /**
     * Test of exportXml method, of class Report.
     *
     * @throws java.lang.Exception if any.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportXml() throws Exception {
        System.out.println("exportXml");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        instance.exportXml();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.xml")).exists(), true);
    }

    /**
     * Test of exportXls method, of class Report.
     *
     * @throws java.lang.Exception if any.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportXls() throws Exception {
        System.out.println("exportXls");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
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
     *
     * @throws java.lang.Exception if any.
     */
    @Test(dependsOnMethods = {"testFillMeta"})
    public void testExportXlsMeta() throws Exception {
        System.out.println("exportXlsMeta");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/csvMeta.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
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
     *
     * @throws java.lang.Exception if any.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportXlsx() throws Exception {
        System.out.println("exportXlsx");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        instance.exportXlsx();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.xlsx")).exists(), true);
    }

    /**
     * Test of exportCsv method, of class Report.
     *
     * @throws java.lang.Exception if any.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportCsv() throws Exception {
        System.out.println("exportCsv");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        config.outCharset = "utf-8";
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
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
     *
     * @throws java.lang.Exception if any.
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
        instance.fill();
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
     *
     * @throws java.lang.Exception if any.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportOds() throws Exception {
        System.out.println("exportOds");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        instance.exportOds();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.ods")).exists(), true);
    }

    /**
     * Test of exportPptx method, of class Report.
     *
     * @throws java.lang.Exception if any.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportPptx() throws Exception {
        System.out.println("exportPptx");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        instance.exportPptx();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.pptx")).exists(), true);
    }

    /**
     * Test of exportXhtml method, of class Report.
     *
     * @throws java.lang.Exception if any.
     */
    @Test(dependsOnMethods = {"testFill"})
    public void testExportXhtml() throws Exception {
        System.out.println("exportXhtml");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
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
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testGetReportParameters() throws Exception {
        System.out.println("getReportParameters");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/noDB-params.jrxml";
        Report instance = new Report(config, new File(config.getInput()));
        JRParameter[] result = instance.getReportParameters();
        // The result includes N system parameters and 4 user parameters. In
        // some earlier releases, N=20, while the docs suggest N=19 (at
        // https://community.jaspersoft.com/documentation/tibco-jaspersoft-studio-user-guide/v60/default-parameters).
        // Therefore, we verify the 4 user parameters by counting from the end.
        assertEquals(result[result.length - 4].getName(), "myString");
        assertEquals(result[result.length - 3].getName(), "myInt");
        assertEquals(result[result.length - 2].getName(), "myDate");
        assertEquals(result[result.length - 1].getName(), "myImage");
    }
    
    /**
     * Test of fill method with xml datasource, of class Report.
     *
     * @throws java.lang.Exception if any.
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
        instance.exportJrprint();
        assertEquals(((File) new File("target/test-classes/reports/CancelAck.jrprint")).exists(), true);
    }

    /**
     * Test of fill method with xml datasource omitting xpath, of class Report.
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testFillFromXmlDatasourceNoXpath() throws Exception {
        System.out.println("fill from xmldatasourceNoXpath");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/CancelAck.jrxml";
        config.output = "target/test-classes/reports/CancelAckNoXpath";
        config.dbType = DsType.xml;
        config.dataFile = new File("target/test-classes/CancelAck.xml");
        config.outputFormats = new ArrayList<OutputFormat>(Arrays.asList(OutputFormat.jrprint));
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        instance.exportJrprint();
        assertEquals(((File) new File("target/test-classes/reports/CancelAckNoXpath.jrprint")).exists(), true);
    }

    /**
     * Test of fill method with json datasource, of class Report.
     *
     * @throws java.lang.Exception if any.
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
        instance.compileToFile(); // this is just a source for another test
        instance.fill();
        instance.exportJrprint();
        assertEquals(((File) new File("target/test-classes/reports/json.jrprint")).exists(), true);
    }

    /**
     * Test of fill method with json datasource omitting jsonQuery, of class Report.
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testFillFromJsonDatasourceNoJsonQuery() throws Exception {
        System.out.println("fill from jsondatasource NoJsonQuery");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/json.jrxml";
        config.output = "target/test-classes/reports/jsonNoQuery";
        config.dbType = DsType.json;
        config.dataFile = new File("target/test-classes/contacts.json");
        config.outputFormats = new ArrayList<OutputFormat>(Arrays.asList(OutputFormat.jrprint));
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        instance.exportJrprint();
        assertEquals(((File) new File("target/test-classes/reports/jsonNoQuery.jrprint")).exists(), true);
    }

    /**
     * Test of fill method with jsonql datasource, of class Report.
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testFillFromJsonQLDatasource() throws Exception {
        System.out.println("fill from jsonqldatasource");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/jsonql.jrxml";
        config.dbType = DsType.jsonql;
        config.dataFile = new File("target/test-classes/contacts.json");
        config.jsonQuery = "contacts.person";
        config.outputFormats = new ArrayList<OutputFormat>(Arrays.asList(OutputFormat.jrprint));
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        instance.exportJrprint();
        assertEquals(((File) new File("target/test-classes/reports/jsonql.jrprint")).exists(), true);
    }

    /**
     * Test of fill method with jsonql datasource omitting jsonQLQuery, of class Report.
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testFillFromJsonQLDatasourceNoJsonQLQuery() throws Exception {
        System.out.println("fill from jsonqldatasource NoJsonQLQuery");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/jsonql.jrxml";
        config.output = "target/test-classes/reports/jsonqlNoQuery";
        config.dbType = DsType.jsonql;
        config.dataFile = new File("target/test-classes/contacts.json");
        config.outputFormats = new ArrayList<OutputFormat>(Arrays.asList(OutputFormat.jrprint));
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        instance.exportJrprint();
        assertEquals(((File) new File("target/test-classes/reports/jsonqlNoQuery.jrprint")).exists(), true);
    }

    /**
     * Test of fill method with stdin, of class Report.
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testFillFromStdin() throws Exception {
        System.out.println("fill from stdin");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/jsonql.jrxml";
        config.dbType = DsType.json;
        config.output = "target/test-classes/reports/jsonql_stdin";
        //
        // Use stdin as the source of data.
        //
        InputStream saved = System.in;
        try {
            System.setIn(new FileInputStream("target/test-classes/contacts.json"));
            config.dataFile = new File("-");
            config.jsonQuery = "contacts.person";
            config.outputFormats = new ArrayList<OutputFormat>(Arrays.asList(OutputFormat.jrprint));
            Report instance = new Report(config, new File(config.getInput()));
            instance.fill();
            instance.exportJrprint();
            assertEquals(((File) new File("target/test-classes/reports/jsonql_stdin.jrprint")).exists(),true);
        } finally {
            System.setIn(saved);
        }
    }

    /**
     * Test of fill method usage of stdout by default. Testing a negative is always an
     * exercise in incompleteness, but c'est la vie.
     *
     * This test is complicated because in the test environment, stdout and stderr
     * point to the same place.
     *
     * @return a int.
     * @throws java.lang.Exception if any.
     */
    @Test
    public int testStdoutIsNotUsed() throws Exception {
        System.out.println("Check stdout is not used by default");
        Config config = null;
        config = new Config();
        config.input  = "target/test-classes/reports/jsonql.jrxml";
        config.output = "target/test-classes/reports/jsonql_stdout";
        config.dbType = DsType.json;
        config.dataFile = new File("target/test-classes/contacts.json");
        config.jsonQuery = "contacts.person";
        config.outputFormats = new ArrayList<OutputFormat>(Arrays.asList(OutputFormat.jrprint));
        //
        // Do not request verbose output.
        //
        config.verbose = false;
        //
        // Capture stdout and stderr.
        //
        System.out.flush();
        System.err.flush();
        PrintStream savedStdout = System.out;
        PrintStream savedStderr = System.err;
        ByteArrayOutputStream tmpStdout = new ByteArrayOutputStream();
        ByteArrayOutputStream tmpStderr = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(tmpStdout));
            System.setErr(new PrintStream(tmpStderr));
            Report instance = new Report(config, new File(config.getInput()));
            instance.fill();
            instance.exportJrprint();
            assertEquals(((File) new File("target/test-classes/reports/jsonql_stdin.jrprint")).exists(), true);
        } finally {
            System.out.flush();
            System.err.flush();
            System.setOut(savedStdout);
            System.setErr(savedStderr);
        }
        //
        // All output should be to stderr.
        //
        assertEquals(0, tmpStdout.size());
        assertEquals(0, tmpStderr.size());
        int defaultOutputSize = tmpStderr.size();
        //
        // Now try again, using verbose mode.
        //
        config.verbose = true;
        System.out.flush();
        System.err.flush();
        savedStdout = System.out;
        savedStderr = System.err;
        tmpStdout = new ByteArrayOutputStream();
        tmpStderr = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(tmpStdout));
            System.setErr(new PrintStream(tmpStderr));
            Report instance = new Report(config, new File(config.getInput()));
            instance.fill();
            instance.exportJrprint();
            assertEquals(((File) new File("target/test-classes/reports/jsonql_stdin.jrprint")).exists(), true);
        } finally {
            System.out.flush();
            System.err.flush();
            System.setOut(savedStdout);
            System.setErr(savedStderr);
        }
        assertEquals(0, tmpStdout.size());
        assertTrue(0 < tmpStderr.size());
        int verboseOutputSize = tmpStderr.size();
        assert verboseOutputSize > defaultOutputSize;
        return verboseOutputSize;
    }

    /**
     * Test of fill method usage of stdout when "-" is specified as the output.
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testStdoutIsUsed() throws Exception {
        System.out.println("Check output to stdout");
        Config config = null;
        config = new Config();
        config.input  = "target/test-classes/reports/jsonql.jrxml";
        config.output = "-";
        config.dbType = DsType.json;
        config.dataFile = new File("target/test-classes/contacts.json");
        config.jsonQuery = "contacts.person";
        config.outputFormats = new ArrayList<OutputFormat>(Arrays.asList(OutputFormat.jrprint));
        //
        // Request verbose output.
        //
        config.verbose = true;
        //
        // Capture stdout and stderr.
        //
        System.out.flush();
        System.err.flush();
        PrintStream savedStdout = System.out;
        PrintStream savedStderr = System.err;
        ByteArrayOutputStream tmpStdout = new ByteArrayOutputStream();
        ByteArrayOutputStream tmpStderr = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(tmpStdout));
            System.setErr(new PrintStream(tmpStderr));
            Report instance = new Report(config, new File(config.getInput()));
            instance.fill();
            instance.exportJrprint();
        } finally {
            System.out.flush();
            System.err.flush();
            System.setOut(savedStdout);
            System.setErr(savedStderr);
        }
        assertTrue(0 < tmpStdout.size());
        assertTrue(0 < tmpStderr.size());
        int filePlusVerboseOutputSize = tmpStdout.size();
        //
        // Run it again without redirected output, and check the output sizes are about 12187 vs 548.
        //
        int verboseOutputSize = testStdoutIsNotUsed();
        //System.out.println("sizes="+filePlusVerboseOutputSize +" vs "+ verboseOutputSize);
        assertTrue(filePlusVerboseOutputSize > verboseOutputSize + 10000);
    }

    /**
     * Test of fill method with xml datasource with barcode4j, of class Report.
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testFillFromXmlBarcode4j() throws Exception {
        System.out.println("fill from xml barcode4j");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/barcode4j.jrxml";
        config.dbType = DsType.xml;
        config.dataFile = new File("target/test-classes/barcode4j.xml");
        config.xmlXpath = "/nalepka/ident";
        config.outputFormats = new ArrayList<OutputFormat>(Arrays.asList(OutputFormat.jrprint));
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        instance.exportJrprint();
        assertEquals(((File) new File("target/test-classes/reports/barcode4j.jrprint")).exists(), true);
    }

    /**
     * Test of exportPdf method with barcode4j, of class Report.
     *
     * @throws java.lang.Exception if any.
     */
    @Test(dependsOnMethods = {"testFillFromXmlBarcode4j"})
    public void testExportPdfBarcode4j() throws Exception {
        System.out.println("exportPdf barcode4j");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/barcode4j.jrprint";
        Report instance = new Report(config, new File(config.getInput()));
        instance.exportPdf();
        assertEquals(((File) new File("target/test-classes/reports/barcode4j.pdf")).exists(), true);
    }
    
    /**
     * Test of public API.
     */
    @Test
    public void testPublicApi() {
        System.out.println("public API");
        Config config = null;
        config = new Config();
        config.dbType = DsType.csv;
        config.dataFile = new File("target/test-classes/csvExampleHeaders.csv");
        config.csvCharset = "utf8";
        config.csvFieldDel = "|";
        config.csvRecordDel = "\r\n";
        config.csvFirstRow = true;
        config.outputFormats = new ArrayList<OutputFormat>(Arrays.asList(OutputFormat.jrprint));
        config.locale = "en";
        config.csvColumns = "a";

        AskFilter savedAskFilter = config.getAskFilter();
        config.setAskFilter(savedAskFilter);
        assertEquals(savedAskFilter, config.getAskFilter());

        String savedCommand = config.getCommand();
        config.setCommand(savedCommand);
        assertEquals(savedCommand, config.getCommand());

        String savedDbDriver = config.getDbDriver();
        config.setDbDriver(savedDbDriver);
        assertEquals(savedDbDriver, config.getDbDriver());

        String savedDbHost = config.getDbHost();
        config.setDbHost(savedDbHost);
        assertEquals(savedDbHost, config.getDbHost());

        String savedDbName = config.getDbName();
        config.setDbName(savedDbName);
        assertEquals(savedDbName, config.getDbName());

        String savedDbPasswd = config.getDbPasswd();
        config.setDbPasswd(savedDbPasswd);
        assertEquals(savedDbPasswd, config.getDbPasswd());

        Integer savedDbPort = config.getDbPort();
        config.setDbPort(savedDbPort);
        assertEquals(savedDbPort, config.getDbPort());

        String savedDbSid = config.getDbSid();
        config.setDbSid(savedDbSid);
        assertEquals(savedDbSid, config.getDbSid());

        DsType savedDbType = config.getDbType();
        config.setDbType(savedDbType);
        assertEquals(savedDbType, config.getDbType());

        String savedDbUrl = config.getDbUrl();
        config.setDbUrl(savedDbUrl);
        assertEquals(savedDbUrl, config.getDbUrl());

        String savedDbUser = config.getDbUser();
        config.setDbUser(savedDbUser);
        assertEquals(savedDbUser, config.getDbUser());

        boolean savedVerbose = config.isVerbose();
        config.setVerbose(savedVerbose);
        assertEquals(savedVerbose, config.isVerbose());

        String savedInput = config.getInput();
        config.setInput(savedInput);
        assertEquals(savedInput, config.getInput());

        File savedJdbcDir = config.getJdbcDir();
        config.setJdbcDir(savedJdbcDir);
        assertEquals(savedJdbcDir, config.getJdbcDir());

        File savedDataFile = config.getDataFile();
        config.setDataFile(savedDataFile);
        assertEquals(savedDataFile, config.getDataFile());

        boolean savedCsvFirstRow = config.getCsvFirstRow();
        config.setCsvFirstRow(savedCsvFirstRow);
        assertEquals(savedCsvFirstRow, config.getCsvFirstRow());

        String[] savedCsvColumns = config.getCsvColumns();
        config.setCsvColumns(savedCsvColumns[0]);
        assertEquals(savedCsvColumns, config.getCsvColumns());

        String savedCsvRecordDel = config.getCsvRecordDel();
        config.setCsvRecordDel(savedCsvRecordDel);
        assertEquals(savedCsvRecordDel, config.getCsvRecordDel());

        char savedCsvFieldDel = config.getCsvFieldDel();
        config.setCsvFieldDel(savedCsvFieldDel + "");
        assertEquals(savedCsvFieldDel, config.getCsvFieldDel());

        String savedCsvCharset = config.getCsvCharset();
        config.setCsvCharset(savedCsvCharset);
        assertEquals(savedCsvCharset, config.getCsvCharset());

        String savedXmlXpath = config.getXmlXpath();
        config.setXmlXpath(savedXmlXpath);
        assertEquals(savedXmlXpath, config.getXmlXpath());

        String savedJsonQuery = config.getJsonQuery();
        config.setJsonQuery(savedJsonQuery);
        assertEquals(savedJsonQuery, config.getJsonQuery());

        String savedJsonQLQuery = config.getJsonQLQuery();
        config.setJsonQLQuery(savedJsonQLQuery);
        assertEquals(savedJsonQLQuery, config.getJsonQLQuery());

        Locale savedLocale = config.getLocale();
        config.setLocale(savedLocale.toString());
        assertEquals(savedLocale, config.getLocale());

        String savedOutput = config.getOutput();
        config.setOutput(savedOutput);
        assertEquals(savedOutput, config.getOutput());

        List<OutputFormat> savedOutputFormats = config.getOutputFormats();
        config.setOutputFormats(savedOutputFormats);
        assertEquals(savedOutputFormats, config.getOutputFormats());

        List<String> savedParams = config.getParams();
        config.setParams(savedParams);
        assertEquals(savedParams, config.getParams());

        String savedPrinterName = config.getPrinterName();
        config.setPrinterName(savedPrinterName);
        assertEquals(savedPrinterName, config.getPrinterName());

        String savedReportName = config.getReportName();
        config.setReportName(savedReportName);
        assertEquals(savedReportName, config.getReportName());

        String savedResource = config.getResource();
        config.setResource(savedResource);
        assertEquals(savedResource, config.getResource());

        boolean savedWithPrintDialog = config.isWithPrintDialog();
        config.setWithPrintDialog(savedWithPrintDialog);
        assertEquals(savedWithPrintDialog, config.isWithPrintDialog());

        boolean savedWriteJasper = config.isWriteJasper();
        config.setWriteJasper(savedWriteJasper);
        assertEquals(savedWriteJasper, config.isWriteJasper());

        Integer savedCopies = config.getCopies();
        config.setCopies(savedCopies);
        assertEquals(savedCopies, config.getCopies());

        String savedOutFieldDel = config.getOutFieldDel();
        config.setOutFieldDel(savedOutFieldDel);
        assertEquals(savedOutFieldDel, config.getOutFieldDel());

        String savedOutCharset = config.getOutCharset();
        config.setOutCharset(savedOutCharset);
        assertEquals(savedOutCharset, config.getOutCharset());
    }

	/**
	 * Test of getMainDatasetQuery method, of class Report.
	 *
	 * Try to get a datasetQuery from a xml report definition.
	 *
	 */
	@Test
	public void testGetMainDatasetQueryFromXml() {
		System.out.println("getMainDatasetQueryFromXml");
		Config config = null;
		String datasetQuery = null;
		config = new Config();
		config.input = "target/test-classes/reports/CancelAck.jrxml";
		Report instance = new Report(config, new File(config.getInput()));
		datasetQuery = instance.getMainDatasetQuery();
		assertEquals(datasetQuery, "/CancelResponse/CancelResult/ID");
	}

	/**
	 * Test of getMainDatasetQuery method, of class Report.
	 *
	 * Try to get a datasetQuery from a json report definition.
	 *
	 */
	@Test
	public void testGetMainDatasetQueryFromJson() {
		System.out.println("getMainDatasetQueryFromJson");
		Config config = null;
		String datasetQuery = null;
		config = new Config();
		config.input = "target/test-classes/reports/json.jrxml";
		Report instance = new Report(config, new File(config.getInput()));
		datasetQuery = instance.getMainDatasetQuery();
		assertEquals(datasetQuery, "contacts.person");
	}

	/**
	 * Test of getMainDatasetQuery method, of class Report.
	 *
	 * Try to get a datasetQuery from a jsonql report definition.
	 *
	 */
	@Test
	public void testGetMainDatasetQueryFromJsonql() {
		System.out.println("getMainDatasetQueryFromJsonql");
		Config config = null;
		String datasetQuery = null;
		config = new Config();
		config.input = "target/test-classes/reports/jsonql.jrxml";
		Report instance = new Report(config, new File(config.getInput()));
		datasetQuery = instance.getMainDatasetQuery();
		assertEquals(datasetQuery, "contacts.person");
	}

	/**
	 * Test of getMainDatasetQuery method, of class Report.
	 *
	 * Try to get a datasetQuery from a json compiled report.
	 *
	 */
	@Test(dependsOnMethods = "testFillFromJsonDatasource")
	public void testGetMainDatasetQueryFromJsonJasper() {
		System.out.println("getMainDatasetQueryFromJsonJasper");
		Config config = null;
		String datasetQuery = null;
		config = new Config();
		config.input = "target/test-classes/reports/json.jasper";
		Report instance = new Report(config, new File(config.getInput()));
		datasetQuery = instance.getMainDatasetQuery();
		assertEquals(datasetQuery, "contacts.person");
	}

	/**
	 * Test of getMainDatasetQuery method, of class Report.
	 *
	 * Try to get a datasetQuery from a json report jrprint. This is not possible.
	 *
	 */
	@Test(dependsOnMethods = { "testFillFromJsonDatasource" })
	public void testGetMainDatasetQueryFromJsonJrprint() {
		System.out.println("getMainDatasetQueryFromJsonJrprint");
		Config config = null;
		String datasetQuery = null;
		config = new Config();
		config.input = "target/test-classes/reports/json.jrprint";
		Report instance = new Report(config, new File(config.getInput()));
		try {
			datasetQuery = instance.getMainDatasetQuery();
			fail("this point of code should never be reached");
		} catch (Exception e) {
			assertEquals(e.getMessage(), "No query for input type: JASPER_PRINT");
		}
	}
}
