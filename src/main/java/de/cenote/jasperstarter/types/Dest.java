/*
 * Copyright 2012-2015 Cenote GmbH.
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
    public static String OUTPUT_FORMATS = "output-formats"; // -f
    public static String INPUT = "input";                   // <input>
    public static String OUTPUT = "output";                 // -o
    public static String LOCALE = "locale";                 // --locale
    public static String DEBUG = "debug";                   // -v
    public static String ASK = "ask";                       // -a
    public static String PARAMS = "params";                 // -P
    public static String RESOURCE = "resource";             // -r
    public static String DS_TYPE = "db-type";               // -t
    public static String DB_HOST = "db-host";               // -H
    public static String DB_USER = "db-user";               // -u
    public static String DB_PASSWD = "db-passwd";           // -p
    public static String DB_NAME = "db-name";               // -n
    public static String DB_SID = "db-sid";                 // --db-sid
    public static String DB_PORT = "db-port";               // --db-port
    public static String DB_DRIVER = "db-driver";           // --db-driver
    public static String DB_URL = "db-url";                 // --db-url
    public static String JDBC_DIR = "jdbc-dir";             // --jdbc-dir
    public static String DATA_FILE = "data-file";           // --data-file
    public static String CSV_FIRST_ROW = "csv-first-row";     // --csv-first-row
    public static String CSV_COLUMNS = "csv-columns";       // --csv-columns
    public static String CSV_RECORD_DEL = "csv-record-del"; // --csv-record-del
    public static String CSV_FIELD_DEL = "csv-field-del";   // --csv-field-del
    public static String CSV_CHARSET = "csv-charset";       // --csv-charset
    public static String XML_XPATH = "xml-xpath";           // --xml-xpath
    public static String OUT_FIELD_DEL = "out-field-del"; // --out-field-del
    public static String OUT_CHARSET = "out-charset";       // --out-charset
    public static String PRINTER_NAME = "printer-name";     // -N
    public static String WITH_PRINT_DIALOG = "with-print-dialog"; // -d
    public static String REPORT_NAME = "set-report-name";   // -s
    public static String WRITE_JASPER = "write-jasper";     // -w, --write-jasper
    public static String COPIES = "copies";                 // -c
}
