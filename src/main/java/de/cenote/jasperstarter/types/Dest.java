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

package de.cenote.jasperstarter.types;

/**
 *
 * @author Volker Voßkämper <vvo at cenote.de>
 * @version $Revision: 5b92831f1a80:54 branch:default $
 */
public interface Dest {

    public static String COMMAND = "command";
    public static String OUTPUT_FORMATS = "output-formats";
    public static String INPUT = "input";
    public static String OUTPUT = "output";
    public static String LOCALE = "locale";
    public static String DEBUG = "debug";
    public static String ASK = "ask";
    public static String PARAMS = "params";
    public static String KEEP = "keep";
    public static String DB_TYPE = "db-type";
    public static String DB_HOST = "db-host";
    public static String DB_USER = "db-user";
    public static String DB_PASSWD = "db-passwd";
    public static String DB_NAME = "db-name";
    public static String DB_SID = "db-sid";
    public static String DB_PORT = "db-port";
    public static String DB_DRIVER = "db-driver";
    public static String DB_URL = "db-url";
    public static String JDBC_DIR = "jdbc-dir";
    public static String PRINTER_NAME = "printer-name";
    public static String WITH_PRINT_DIALOG = "with-print-dialog";
    public static String LIST_PRINTERS = "list-printers";
    public static String REPORT_NAME = "set-report-name";
    public static String WRITE_JASPER = "write-jasper";
}
