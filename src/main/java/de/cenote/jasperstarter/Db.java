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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 *
 * @author Volker Voßkämper <vvo at cenote.de>
 * @version $Revision: 5b92831f1a80:54 branch:default $
 */
public class Db {

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Namespace namespace = App.getInstance().getNamespace();
        Connection conn = null;
        DbType dbtype = (DbType) namespace.get(Dest.DB_TYPE);
        String host = namespace.getString(Dest.DB_HOST);
        String user = namespace.getString(Dest.DB_USER);
        String passwd = namespace.getString(Dest.DB_PASSWD);
        String driver = null;
        String dbname = null;
        String port = null;
        String sid = null;
        String connectString = null;
        if (DbType.mysql.equals(dbtype)) {
            driver = DbType.mysql.getDriver();
            port = namespace.getInt(Dest.DB_PORT).toString();
            dbname = namespace.getString(Dest.DB_NAME);
            connectString = "jdbc:mysql://" + host + ":" + port + "/" + dbname;
        } else if (DbType.postgres.equals(dbtype)) {
            driver = DbType.postgres.getDriver();
            port = namespace.getInt(Dest.DB_PORT).toString();
            dbname = namespace.getString(Dest.DB_NAME);
            connectString = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
        } else if (DbType.oracle.equals(dbtype)) {
            driver = DbType.oracle.getDriver();
            port = namespace.getInt(Dest.DB_PORT).toString();
            sid = namespace.getString(Dest.DB_SID);
            connectString = "jdbc:oracle:thin:@" + host + ":" + port + ":" + sid;
        } else if (DbType.generic.equals(dbtype)) {
            driver = namespace.getString(Dest.DB_DRIVER);
            connectString = namespace.getString(Dest.DB_URL);
        }
        if (namespace.getBoolean(Dest.DEBUG)) {
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
