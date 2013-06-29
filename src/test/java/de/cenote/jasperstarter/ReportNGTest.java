/*
 * Copyright 2013 Cenote GmbH.
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
import de.cenote.jasperstarter.types.OutputFormat;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRParameter;
import static org.testng.Assert.*;
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
     * Test of fill method, of class Report.
     */
    @Test(dependsOnMethods = {"testCompileToFile"})
    public void testFill() throws Exception {
        System.out.println("fill");
        Config config = null;
        config = new Config();
        config.input = "target/test-classes/reports/compileToFile.jasper";
        //config.dbType = DbType.none;
        config.dbType = DbType.csv;
        config.dataFile = new File("target/test-classes/csvExampleHeaders.csv");
        config.csvCharset = "utf8";
        config.csvFieldDel = "|";
        config.csvRecordDel = "\r\n";
        config.csvFirstRow = true;
        config.outputFormats = new ArrayList(Arrays.asList(OutputFormat.jrprint));
        Report instance = new Report(config, new File(config.getInput()));
        instance.fill();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.jrprint")).exists(), true);
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
        Report instance = new Report(config, new File(config.getInput()));
        instance.exportCsv();
        assertEquals(((File) new File("target/test-classes/reports/compileToFile.csv")).exists(), true);
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
        JRParameter[] expResult = null;
        JRParameter[] result = instance.getReportParameters();
        // there are 19 system parameters
        assertEquals(result[19].getName(), "myString");
    }
}