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

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;

import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.data.JsonQLDataSource;
import static org.testng.Assert.*;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * <p>DbNGTest class.</p>
 *
 * @author Volker Voßkämper
 * @version $Id: $Id
 * @since 3.4.0
 */
public class DbNGTest {

    /**
     * <p>Constructor for DbNGTest.</p>
     */
    public DbNGTest() {
    }

    /**
     * <p>setUpClass.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @BeforeClass
    public static void setUpClass() throws Exception {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (Exception e) {
            fail("ERROR: failed to load HSQLDB JDBC driver.", e);
        }
        Connection c = DriverManager.getConnection(
                "jdbc:hsqldb:mem:mymemdb", "SA", "");
    }

    /**
     * <p>tearDownClass.</p>
     *
     * @throws java.lang.Exception if any.
     */
    @AfterClass
    public static void tearDownClass() throws Exception {
        Connection c = DriverManager.getConnection(
                "jdbc:hsqldb:mem:mymemdb;shutdown=true", "SA", "");
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
     * Test of getConnection method, of class Db.
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testGetConnection() throws Exception {
        System.out.println("getConnection");
        Config config = new Config();
        config.dbType = DsType.generic;
        config.dbDriver = "org.hsqldb.jdbc.JDBCDriver";
        config.dbUrl = "jdbc:hsqldb:mem:mymemdb;ifexists=true";
        config.dbUser = "SA";
        config.dbPasswd = "";
        Db instance = new Db();
        Connection result = instance.getConnection(config);
        assertNotNull(result);
    }

    /**
     * Test of getCsvDataSource method, of class Db.
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testGetCsvDataSource() throws Exception {
        System.out.println("getCsvDataSource");
        Config config = new Config();
        config.dbType = DsType.csv;
        config.dataFile = new File("target/test-classes/csvExampleHeaders.csv");
        config.csvCharset = "utf-8";
        config.csvFieldDel = "|";
        config.csvRecordDel = "\r\n";
        config.csvFirstRow = true;
        Db instance = new Db();
        JRCsvDataSource jRCsvDataSource = instance.getCsvDataSource(config);
        jRCsvDataSource.next();
        Map names = jRCsvDataSource.getColumnNames();
        assertEquals(names.toString(), "{Name=0, Street=1, City=2, Phone=3}");
    }
    
    /**
     * Test of getXmlDataSource method, of class Db.
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testGetXmlDataSource() throws Exception {
    	System.out.println("getxmlDataSource");
    	Config config = new Config();
    	config.dbType = DsType.xml;
    	config.dataFile = new File("target/test-classes/CancelAck.xml");
    	config.xmlXpath = "/CancelResponse/CancelResult/ID";
    	Db instance = new Db();
    	JRXmlDataSource jRXmlDataSource = instance.getXmlDataSource(config);
    	jRXmlDataSource.next();
    	// ToDo: don't know jet how to get any value out of it here. 
    	// So just checking if object exists:
    	assertEquals(jRXmlDataSource.getClass().getCanonicalName(), "net.sf.jasperreports.engine.data.JRXmlDataSource");
    }

    /**
     * Test of getJsonDataSource method, of class Db.
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testGetJsonDataSource() throws Exception {
      System.out.println("getJsonDataSource");
      Config config = new Config();
      config.dbType = DsType.json;
      config.dataFile = new File("target/test-classes/contacts.json");
      config.jsonQuery = "contacts.person";
      Db instance = new Db();
      JsonDataSource jsonDataSource = instance.getJsonDataSource(config);
      assertTrue(jsonDataSource.next());
      assertEquals(jsonDataSource.getClass().getCanonicalName(), "net.sf.jasperreports.engine.data.JsonDataSource");
    }

    /**
     * Test of getJsonQLDataSource method, of class Db.
     *
     * @throws java.lang.Exception if any.
     */
    @Test
    public void testGetJsonQLDataSource() throws Exception {
      System.out.println("getJsonQLDataSource");
      Config config = new Config();
      config.dbType = DsType.json;
      config.dataFile = new File("target/test-classes/contacts.json");
      config.jsonQuery = "contacts.person";
      Db instance = new Db();
      JsonQLDataSource jsonDataSource = instance.getJsonQLDataSource(config);
      assertTrue(jsonDataSource.next());
      assertEquals(jsonDataSource.getClass().getCanonicalName(), "net.sf.jasperreports.engine.data.JsonQLDataSource");
    }
}
