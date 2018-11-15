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

import de.cenote.jasperstarter.types.AskFilter;
import de.cenote.jasperstarter.types.DsType;
import de.cenote.jasperstarter.types.Dest;
import de.cenote.jasperstarter.types.OutputFormat;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRLoader;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import net.sourceforge.argparse4j.annotation.Arg;
import org.apache.commons.lang.LocaleUtils;

/**
 * This POJO is intended to contain all command line parameters and other
 * configuration values.
 *
 * @author Volker Voßkämper
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
    @Arg(dest = Dest.DS_TYPE)
    DsType dbType;
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
    @Arg(dest = Dest.CSV_FIRST_ROW)
    boolean csvFirstRow;
    @Arg(dest = Dest.CSV_COLUMNS)
    String csvColumns;
    @Arg(dest = Dest.CSV_RECORD_DEL)
    String csvRecordDel;
    @Arg(dest = Dest.CSV_FIELD_DEL)
    String csvFieldDel; //representing a char
    @Arg(dest = Dest.CSV_CHARSET)
    String csvCharset;
    @Arg(dest = Dest.XML_XPATH)
    String xmlXpath;
    @Arg(dest = Dest.JSON_QUERY)
    String jsonQuery;
    @Arg(dest = Dest.JSONQL_QUERY)
    String jsonQLQuery;
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
    @Arg(dest = Dest.COPIES)
    Integer copies;
    @Arg(dest = Dest.OUT_FIELD_DEL)
    String outFieldDel;
    @Arg(dest = Dest.OUT_CHARSET)
    String outCharset;
    
    // end of argparse4j arguments
    /**
     *
     * 
     */
    public Config() {
        String jasperStarterVersion = "";
        String jasperStarterRevision = "";
        jasperStarterVersion = this.getClass().getPackage().getSpecificationVersion();
        jasperStarterRevision = this.getClass().getPackage().getImplementationVersion();

        String jasperReportsVersion = "";
        try {
        	jasperReportsVersion = Package.getPackage("net.sf.jasperreports.engine").
                    getImplementationVersion();
        } catch (NullPointerException e) {
            // ignore NullPointerException while running TestNG
            // @todo: solve problem in test
        }

        StringBuffer sb = new StringBuffer("JasperStarter ").append(jasperStarterVersion)
        		.append(" Rev ").append(jasperStarterRevision).append("\n")
        		.append(" - JasperReports: ").append(jasperReportsVersion);
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

    public void setAskFilter(AskFilter value) { askFilter = value; }

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

    public void setCommand(String value) { command = value; }

    public String getDbDriver() {
        return dbDriver;
    }

    public void setDbDriver(String value) { dbDriver = value; }

    public String getDbHost() {
        return dbHost;
    }

    public void setDbHost(String value) { dbHost = value; }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String value) { dbName = value; }

    public String getDbPasswd() {
        return dbPasswd;
    }

    public void setDbPasswd(String value) { dbPasswd = value; }

    public Integer getDbPort() {
        return dbPort;
    }

    public void setDbPort(Integer value) { dbPort = value; }

    public String getDbSid() {
        return dbSid;
    }

    public void setDbSid(String value) { dbSid = value; }

    public DsType getDbType() {
        return dbType;
    }

    public void setDbType(DsType value) { dbType = value; }

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

    public void setDbUrl(String value) { dbUrl = value; }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String value) { dbUser = value; }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean value) { verbose = value; }

    public String getInput() {
        return input;
    }

    public void setInput(String value) { input = value; }

    public File getJdbcDir() {
        return jdbcDir;
    }

    public void setJdbcDir(File value) { jdbcDir = value; }

    public boolean hasJdbcDir() {
        if (jdbcDir != null && !jdbcDir.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public File getDataFile() { return dataFile; }

    public void setDataFile(File value) { dataFile = value; }

    /**
     * Get InputStream corresponding to the configured dataFile.
     */
    public InputStream getDataFileInputStream() throws JRException {
        //
        // Are we using stdin?
        //
        if (getDataFileName().equals("-")) {
            return System.in;
        } else {
            return JRLoader.getInputStream(dataFile);
        }
    }

    /**
     * Get name of the configured dataFile.
     */
    public String getDataFileName() {
        return dataFile.getName();
    }

    public boolean getCsvFirstRow() {
        return csvFirstRow;
    }

    public void setCsvFirstRow(boolean value) { csvFirstRow = value; }

    public String[] getCsvColumns() {
        if (csvColumns == null) {
            // return an empty array instead of null
            return new String[0];
        } else {
            return csvColumns.split(",");
        }
    }

    public void setCsvColumns(String value) { csvColumns = value; }

    public String getCsvRecordDel() {
        return csvRecordDel;
    }

    public void setCsvRecordDel(String value) { csvRecordDel = value; }

    public char getCsvFieldDel() {
        return csvFieldDel.charAt(0);
    }

    public void setCsvFieldDel(String value) { csvFieldDel = value; }

    public String getCsvCharset() {
        return csvCharset;
    }

    public void setCsvCharset(String value) { csvCharset = value; }

    public String getXmlXpath() { return xmlXpath; }

    public void setXmlXpath(String value) { xmlXpath = value; }

    public String getJsonQuery() { return jsonQuery; }

    public void setJsonQuery(String value) { jsonQuery = value; }

    public String getJsonQLQuery() { return jsonQLQuery; }

    public void setJsonQLQuery(String value) { jsonQLQuery = value; }

    public Locale getLocale() {
        return LocaleUtils.toLocale(locale);
    }

    public void setLocale(String value) { locale = value; }

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

    public void setOutput(String value) { output = value; }

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

    public void setOutputFormats(List<OutputFormat> value) { outputFormats = value; }

    public List<String> getParams() {
        return params;
    }

    public void setParams(List<String> value) { params = value; }

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

    public void setPrinterName(String value) { printerName = value; }

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

    public void setReportName(String value) { reportName = value; }

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

    public void setResource(String value) { resource = value; }

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

    public void setWithPrintDialog(boolean value) { withPrintDialog = value; }

    public boolean isWriteJasper() {
        return writeJasper;
    }

    public void setWriteJasper(boolean value) { writeJasper = value; }

    public Integer getCopies() {
        return copies;
    }

    public void setCopies(Integer value) { copies = value; }

    public boolean hasCopies() {
        if (copies != null) {
            return true;
        } else {
            return false;
        }
    }

    public String getOutFieldDel() {
        return outFieldDel;
    }

    public void setOutFieldDel(String value) { outFieldDel = value; }

    public String getOutCharset() {
        return outCharset;
    }

    public void setOutCharset(String value) { outCharset = value; }

    // END argparse4j arguments
    // TODO: overwrite toString()
}
