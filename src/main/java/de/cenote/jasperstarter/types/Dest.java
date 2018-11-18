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
 * <p>Dest interface.</p>
 *
 * @author Volker Voßkämper
 * @version $Revision: 5b92831f1a80:54 branch:default $
 */
public interface Dest {

    /** Constant <code>COMMAND="command"</code> */
    public static String COMMAND = "command";
    /** Constant <code>OUTPUT_FORMATS="output-formats"</code> */
    public static String OUTPUT_FORMATS = "output-formats"; // -f
    /** Constant <code>INPUT="input"</code> */
    public static String INPUT = "input";                   // <input>
    /** Constant <code>OUTPUT="output"</code> */
    public static String OUTPUT = "output";                 // -o
    /** Constant <code>LOCALE="locale"</code> */
    public static String LOCALE = "locale";                 // --locale
    /** Constant <code>DEBUG="debug"</code> */
    public static String DEBUG = "debug";                   // -v
    /** Constant <code>ASK="ask"</code> */
    public static String ASK = "ask";                       // -a
    /** Constant <code>PARAMS="params"</code> */
    public static String PARAMS = "params";                 // -P
    /** Constant <code>RESOURCE="resource"</code> */
    public static String RESOURCE = "resource";             // -r
    /** Constant <code>DS_TYPE="db-type"</code> */
    public static String DS_TYPE = "db-type";               // -t
    /** Constant <code>DB_HOST="db-host"</code> */
    public static String DB_HOST = "db-host";               // -H
    /** Constant <code>DB_USER="db-user"</code> */
    public static String DB_USER = "db-user";               // -u
    /** Constant <code>DB_PASSWD="db-passwd"</code> */
    public static String DB_PASSWD = "db-passwd";           // -p
    /** Constant <code>DB_NAME="db-name"</code> */
    public static String DB_NAME = "db-name";               // -n
    /** Constant <code>DB_SID="db-sid"</code> */
    public static String DB_SID = "db-sid";                 // --db-sid
    /** Constant <code>DB_PORT="db-port"</code> */
    public static String DB_PORT = "db-port";               // --db-port
    /** Constant <code>DB_DRIVER="db-driver"</code> */
    public static String DB_DRIVER = "db-driver";           // --db-driver
    /** Constant <code>DB_URL="db-url"</code> */
    public static String DB_URL = "db-url";                 // --db-url
    /** Constant <code>JDBC_DIR="jdbc-dir"</code> */
    public static String JDBC_DIR = "jdbc-dir";             // --jdbc-dir
    /** Constant <code>DATA_FILE="data-file"</code> */
    public static String DATA_FILE = "data-file";           // --data-file
    /** Constant <code>CSV_FIRST_ROW="csv-first-row"</code> */
    public static String CSV_FIRST_ROW = "csv-first-row";   // --csv-first-row
    /** Constant <code>CSV_COLUMNS="csv-columns"</code> */
    public static String CSV_COLUMNS = "csv-columns";       // --csv-columns
    /** Constant <code>CSV_RECORD_DEL="csv-record-del"</code> */
    public static String CSV_RECORD_DEL = "csv-record-del"; // --csv-record-del
    /** Constant <code>CSV_FIELD_DEL="csv-field-del"</code> */
    public static String CSV_FIELD_DEL = "csv-field-del";   // --csv-field-del
    /** Constant <code>CSV_CHARSET="csv-charset"</code> */
    public static String CSV_CHARSET = "csv-charset";       // --csv-charset
    /** Constant <code>XML_XPATH="xml-xpath"</code> */
    public static String XML_XPATH = "xml-xpath";           // --xml-xpath
    /** Constant <code>JSON_QUERY="json-query"</code> */
    public static String JSON_QUERY = "json-query";         // --json-query
    /** Constant <code>JSONQL_QUERY="jsonql-query"</code> */
    public static String JSONQL_QUERY = "jsonql-query";     // --jsonql-query
    /** Constant <code>OUT_FIELD_DEL="out-field-del"</code> */
    public static String OUT_FIELD_DEL = "out-field-del";   // --out-field-del
    /** Constant <code>OUT_CHARSET="out-charset"</code> */
    public static String OUT_CHARSET = "out-charset";       // --out-charset
    /** Constant <code>PRINTER_NAME="printer-name"</code> */
    public static String PRINTER_NAME = "printer-name";     // -N
    /** Constant <code>WITH_PRINT_DIALOG="with-print-dialog"</code> */
    public static String WITH_PRINT_DIALOG = "with-print-dialog"; // -d
    /** Constant <code>REPORT_NAME="set-report-name"</code> */
    public static String REPORT_NAME = "set-report-name";   // -s
    /** Constant <code>WRITE_JASPER="write-jasper"</code> */
    public static String WRITE_JASPER = "write-jasper";     // -w, --write-jasper
    /** Constant <code>COPIES="copies"</code> */
    public static String COPIES = "copies";                 // -c
}
