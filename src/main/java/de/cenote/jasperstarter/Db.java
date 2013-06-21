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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Volker Voßkämper <vvo at cenote.de>
 * @version $Revision: 5b92831f1a80:54 branch:default $
 */
public class Db {

    public JRCsvDataSource getCsvDataSource(Config config) throws JRException {
        JRCsvDataSource ds = new JRCsvDataSource(
                JRLoader.getInputStream(config.getCsvFile()));

        ds.setUseFirstRowAsHeader(config.getCsvUse1Row());
        if (!config.getCsvUse1Row()) {
            ds.setColumnNames(config.getCsvColumns());
        }

        ds.setRecordDelimiter(
                StringEscapeUtils.unescapeJava(config.getCsvRecordDel()));
        ds.setFieldDelimiter(config.getCsvFieldDel());

        if (config.isVerbose()) {
            System.out.println("Use first row: " + config.getCsvUse1Row());
            System.out.print("CSV Columns:");
            for (String name : config.getCsvColumns()) {
                System.out.print(" " + name);
            }
            System.out.println("");
            System.out.println("-----------------------");
            System.out.println("Record delimiter literal: " + config.getCsvRecordDel());
            System.out.println("Record delimiter: " + ds.getRecordDelimiter());
            System.out.println("Field delimiter: " + ds.getFieldDelimiter());
            System.out.println("-----------------------");
        }

        return ds;
    }

    public Connection getConnection(Config config) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        DbType dbtype = config.getDbType();
        String host = config.getDbHost();
        String user = config.getDbUser();
        String passwd = config.getDbPasswd();
        String driver = null;
        String dbname = null;
        String port = null;
        String sid = null;
        String connectString = null;
        if (DbType.mysql.equals(dbtype)) {
            driver = DbType.mysql.getDriver();
            port = config.getDbPort().toString();
            dbname = config.getDbName();
            connectString = "jdbc:mysql://" + host + ":" + port + "/" + dbname;
        } else if (DbType.postgres.equals(dbtype)) {
            driver = DbType.postgres.getDriver();
            port = config.getDbPort().toString();
            dbname = config.getDbName();
            connectString = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
        } else if (DbType.oracle.equals(dbtype)) {
            driver = DbType.oracle.getDriver();
            port = config.getDbPort().toString();
            sid = config.getDbSid();
            connectString = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;
        } else if (DbType.generic.equals(dbtype)) {
            driver = config.getDbDriver();
            connectString = config.getDbUrl();
        }
        if (config.isVerbose()) {
            System.out.println("JDBC driver: " + driver);
            System.out.println("Connectstring: " + connectString);
            System.out.println("db-user: " + user);
            if (passwd.isEmpty()) {
                System.out.println("db-password is empty");
            }
        }

        Class.forName(driver);
        conn = DriverManager.getConnection(connectString, user, passwd);

        return conn;
    }
}
