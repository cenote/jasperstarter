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

import de.cenote.jasperstarter.types.AskFilter;
import de.cenote.jasperstarter.types.DbType;
import de.cenote.jasperstarter.types.Dest;
import de.cenote.jasperstarter.types.OutputFormat;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import net.sourceforge.argparse4j.annotation.Arg;
import org.apache.commons.lang.LocaleUtils;

/**
 * This POJO is intended to contain all command line parameters and other
 * configuration values.
 *
 * @author Volker Voßkämper <vvo at cenote.de>
 * @version $Revision$
 */
public class Config {

    Properties applicationProperties;
    String versionString;
    // argparse4j arguments
    @Arg(dest = Dest.ASK)
    AskFilter askFilter;
    @Arg(dest = Dest.COMMAND)
    String command;
    @Arg(dest = Dest.DB_DRIVER)
    String dbDriver;
    @Arg(dest = Dest.DB_HOST)
    String dbHost;
    @Arg(dest = Dest.DB_NAME)
    String dbName;
    @Arg(dest = Dest.DB_PASSWD)
    String dbPasswd;
    @Arg(dest = Dest.DB_PORT)
    Integer dbPort;
    @Arg(dest = Dest.DB_SID)
    String dbSid;
    @Arg(dest = Dest.DB_TYPE)
    DbType dbType;
    @Arg(dest = Dest.DB_URL)
    String dbUrl;
    @Arg(dest = Dest.DB_USER)
    String dbUser;
    @Arg(dest = Dest.DEBUG)
    boolean verbose;
    @Arg(dest = Dest.INPUT)
    String input;
    @Arg(dest = Dest.JDBC_DIR)
    File jdbcDir;
    @Arg(dest = Dest.DATA_FILE)
    File dataFile;
    @Arg(dest = Dest.CSV_USE_1ROW)
    boolean csvUse1Row;
    @Arg(dest = Dest.CSV_COLUMNS)
    String csvColumns;
    @Arg(dest = Dest.CSV_RECORD_DEL)
    String csvRecordDel;
    @Arg(dest = Dest.CSV_FIELD_DEL)
    String csvFieldDel; //representing a char
    @Arg(dest = Dest.CSV_CHARSET)
    String csvCharset;
    @Arg(dest = Dest.LOCALE)
    String locale;
    @Arg(dest = Dest.OUTPUT)
    String output;
    @Arg(dest = Dest.OUTPUT_FORMATS)
    List<OutputFormat> outputFormats;
    @Arg(dest = Dest.PARAMS)
    List<String> params;
    @Arg(dest = Dest.PRINTER_NAME)
    String printerName;
    @Arg(dest = Dest.REPORT_NAME)
    String reportName;
    @Arg(dest = Dest.RESOURCE)
    String resource;
    @Arg(dest = Dest.WITH_PRINT_DIALOG)
    boolean withPrintDialog;
    @Arg(dest = Dest.WRITE_JASPER)
    boolean writeJasper;

    // end of argparse4j arguments
    /**
     *
     * @throws IOException
     */
    Config() throws IOException {
        this.applicationProperties = new Properties();
        try {
            this.applicationProperties.load(this.getClass().
                    getResourceAsStream("application.properties"));
        } catch (IOException ex) {
            throw new IOException("Unable to load application.properties!", ex);
        }
        String jasperversion = "";
        try {
            jasperversion = Package.getPackage("net.sf.jasperreports.engine").
                    getImplementationVersion();
        } catch (NullPointerException e) {
            // ignore NullPointerException while running TestNG
            // @todo: solve problem in test
        }
        StringBuffer sb = new StringBuffer("JasperStarter ")
                .append(applicationProperties.getProperty("application.version"))
                .append(" Rev ").append(applicationProperties.getProperty("application.revision"))
                .append(" ").append(applicationProperties.getProperty("application.revision.date"))
                .append("\n").append(" - JasperReports: ").append(jasperversion);
        versionString = sb.toString();
    }

    /**
     *
     * @return JasperStarter version string including JasperReports library
     * version
     */
    public String getVersionString() {
        return versionString;
    }

    // argparse4j argument getters
    public AskFilter getAskFilter() {
        return askFilter;
    }

    public boolean hasAskFilter() {
        if (askFilter != null) {
            return true;
        } else {
            return false;
        }
    }

    public String getCommand() {
        return command;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public String getDbHost() {
        return dbHost;
    }

    public String getDbName() {
        return dbName;
    }

    public String getDbPasswd() {
        return dbPasswd;
    }

    public Integer getDbPort() {
        return dbPort;
    }

    public String getDbSid() {
        return dbSid;
    }

    public DbType getDbType() {
        return dbType;
    }

    public boolean hasDbType() {
        if (dbType != null) {
            return true;
        } else {
            return false;
        }
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public boolean isVerbose() {
        return verbose;
    }

    public String getInput() {
        return input;
    }

    public File getJdbcDir() {
        return jdbcDir;
    }

    public boolean hasJdbcDir() {
        if (jdbcDir != null && !jdbcDir.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public File getDataFile() {
        return dataFile;
    }

    public boolean getCsvUse1Row() {
        return csvUse1Row;
    }

    public String[] getCsvColumns() {
        if (csvColumns == null) {
            // return an empty array instead of null
            return new String[0];
        } else {
            return csvColumns.split(",");
        }
    }

    public String getCsvRecordDel() {
        return csvRecordDel;
    }

    public char getCsvFieldDel() {
        return csvFieldDel.charAt(0);
    }

    public String getCsvCharset() {
        return csvCharset;
    }

    public Locale getLocale() {
        return LocaleUtils.toLocale(locale);
    }

    public boolean hasLocale() {
        if (locale != null && !locale.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public String getOutput() {
        return output;
    }

    public boolean hasOutput() {
        if (output != null && !output.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public List<OutputFormat> getOutputFormats() {
        return outputFormats;
    }

    public List<String> getParams() {
        return params;
    }

    public boolean hasParams() {
        if (params != null && !params.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public String getPrinterName() {
        return printerName;
    }

    public boolean hasPrinterName() {
        if (printerName != null && !printerName.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public String getReportName() {
        return reportName;
    }

    public boolean hasReportName() {
        if (reportName != null && !reportName.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public String getResource() {
        return resource;
    }

    public boolean hasResource() {
        // the resource default if set is "" .constant("")
        if (resource != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isWithPrintDialog() {
        return withPrintDialog;
    }

    public boolean isWriteJasper() {
        return writeJasper;
    }
    // END argparse4j arguments
    // @todo: overwrite toString()
}
