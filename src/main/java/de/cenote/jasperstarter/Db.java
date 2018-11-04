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

import de.cenote.jasperstarter.types.DsType;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.data.JsonQLDataSource;

import org.apache.commons.lang.StringEscapeUtils;

/**
 *
 * @author Volker Voßkämper <vvo at cenote.de>
 * @version $Revision: 5b92831f1a80:54 branch:default $
 */
public class Db {
    public JRCsvDataSource getCsvDataSource(Config config) throws JRException {
        JRCsvDataSource ds;
        try {
            ds = new JRCsvDataSource(config.getDataFileInputStream(), config.csvCharset);
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalArgumentException("Unknown CSV charset: "
                    + config.csvCharset
                    + ex.getMessage(), ex);
        }

        ds.setUseFirstRowAsHeader(config.getCsvFirstRow());
        if (!config.getCsvFirstRow()) {
            ds.setColumnNames(config.getCsvColumns());
        }

        ds.setRecordDelimiter(
                StringEscapeUtils.unescapeJava(config.getCsvRecordDel()));
        ds.setFieldDelimiter(config.getCsvFieldDel());

        if (config.isVerbose()) {
            System.out.println("Use first row: " + config.getCsvFirstRow());
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
    
	public JRXmlDataSource getXmlDataSource(Config config) throws JRException {
		JRXmlDataSource ds;
		ds = new JRXmlDataSource(config.getDataFileInputStream(), config.xmlXpath);
		if (config.isVerbose()) {
			System.out.println("Data file: " + config.getDataFileName());
			System.out.println("XML xpath: " + config.xmlXpath);
		}
		return ds;
	}

    public JsonDataSource getJsonDataSource(Config config) throws JRException {
		JsonDataSource ds;
		ds = new JsonDataSource(config.getDataFileInputStream(), config.jsonQuery);
		if (config.isVerbose()) {
			System.out.println("Data file: " + config.getDataFileName());
			System.out.println("JSON query : " + config.jsonQuery);
		}
		return ds;
	}

    public JsonQLDataSource getJsonQLDataSource(Config config) throws JRException {
		JsonQLDataSource ds;
		ds = new JsonQLDataSource(config.getDataFileInputStream(), config.jsonQLQuery);
		if (config.isVerbose()) {
			System.out.println("Data file: " + config.getDataFileName());
			System.out.println("JSONQL query : " + config.jsonQLQuery);
		}
		return ds;
	}

    public Connection getConnection(Config config) throws ClassNotFoundException, SQLException {
        Connection conn = null;
        DsType dbtype = config.getDbType();
        String host = config.getDbHost();
        String user = config.getDbUser();
        String passwd = config.getDbPasswd();
        String driver = null;
        String dbname = null;
        String port = null;
        String sid = null;
        String connectString = null;
        if (DsType.mysql.equals(dbtype)) {
            driver = DsType.mysql.getDriver();
            port = config.getDbPort().toString();
            dbname = config.getDbName();
            connectString = "jdbc:mysql://" + host + ":" + port + "/" + dbname;
        } else if (DsType.postgres.equals(dbtype)) {
            driver = DsType.postgres.getDriver();
            port = config.getDbPort().toString();
            dbname = config.getDbName();
            connectString = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
        } else if (DsType.oracle.equals(dbtype)) {
            driver = DsType.oracle.getDriver();
            port = config.getDbPort().toString();
            sid = config.getDbSid();
            connectString = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;
        } else if (DsType.generic.equals(dbtype)) {
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
