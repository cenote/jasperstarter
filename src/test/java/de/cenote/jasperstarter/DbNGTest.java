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
import java.sql.Connection;
import java.sql.DriverManager;
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
public class DbNGTest {

    public DbNGTest() {
    }

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

    @AfterClass
    public static void tearDownClass() throws Exception {
        Connection c = DriverManager.getConnection(
                "jdbc:hsqldb:mem:mymemdb;shutdown=true", "SA", "");
    }

    @BeforeMethod
    public void setUpMethod() throws Exception {
    }

    @AfterMethod
    public void tearDownMethod() throws Exception {
    }

    /**
     * Test of getConnection method, of class Db.
     */
    @Test
    public void testGetConnection() throws Exception {
        System.out.println("getConnection");
        Config config = new Config();
        config.dbType = DbType.generic;
        config.dbDriver = "org.hsqldb.jdbc.JDBCDriver";
        config.dbUrl = "jdbc:hsqldb:mem:mymemdb;ifexists=true";
        config.dbUser = "SA";
        config.dbPasswd = "";
        Db instance = new Db();
        Connection result = instance.getConnection(config);
        assertNotNull(result);
    }
}
