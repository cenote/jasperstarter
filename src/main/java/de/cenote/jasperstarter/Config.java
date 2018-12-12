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
     * <p>Constructor for Config.</p>
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
     * <p>Getter for the field <code>versionString</code>.</p>
     *
     * @return JasperStarter version string including JasperReports library
     * version
     */
    public String getVersionString() {
        return versionString;
    }

    // argparse4j argument getters
    /**
     * <p>Getter for the field <code>askFilter</code>.</p>
     *
     * @return a {@link de.cenote.jasperstarter.types.AskFilter} object.
     */
    public AskFilter getAskFilter() {
        return askFilter;
    }

    /**
     * <p>Setter for the field <code>askFilter</code>.</p>
     *
     * @param value a {@link de.cenote.jasperstarter.types.AskFilter} object.
     */
    public void setAskFilter(AskFilter value) {
        askFilter = value;
    }

    /**
     * <p>hasAskFilter.</p>
     *
     * @return a boolean.
     */
    public boolean hasAskFilter() {
        if (askFilter != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>Getter for the field <code>command</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCommand() {
        return command;
    }

    /**
     * <p>Setter for the field <code>command</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setCommand(String value) {
        command = value;
    }

    /**
     * <p>Getter for the field <code>dbDriver</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDbDriver() {
        return dbDriver;
    }

    /**
     * <p>Setter for the field <code>dbDriver</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setDbDriver(String value) {
        dbDriver = value;
    }

    /**
     * <p>Getter for the field <code>dbHost</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDbHost() {
        return dbHost;
    }

    /**
     * <p>Setter for the field <code>dbHost</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setDbHost(String value) {
        dbHost = value;
    }

    /**
     * <p>Getter for the field <code>dbName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * <p>Setter for the field <code>dbName</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setDbName(String value) {
        dbName = value;
    }

    /**
     * <p>Getter for the field <code>dbPasswd</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDbPasswd() {
        return dbPasswd;
    }

    /**
     * <p>Setter for the field <code>dbPasswd</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setDbPasswd(String value) {
        dbPasswd = value;
    }

    /**
     * <p>Getter for the field <code>dbPort</code>.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getDbPort() {
        return dbPort;
    }

    /**
     * <p>Setter for the field <code>dbPort</code>.</p>
     *
     * @param value a {@link java.lang.Integer} object.
     */
    public void setDbPort(Integer value) {
        dbPort = value;
    }

    /**
     * <p>Getter for the field <code>dbSid</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDbSid() {
        return dbSid;
    }

    /**
     * <p>Setter for the field <code>dbSid</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setDbSid(String value) {
        dbSid = value;
    }

    /**
     * <p>Getter for the field <code>dbType</code>.</p>
     *
     * @return a {@link de.cenote.jasperstarter.types.DsType} object.
     */
    public DsType getDbType() {
        return dbType;
    }

    /**
     * <p>Setter for the field <code>dbType</code>. This setting determines what
     * other configuration options may apply. For example, if <code>dbType</code>
     * is {@link DsType#jsonql}, then {@link Config#setJsonQLQuery(String)}
     * may be used to set the query string.</p>
     *
     * @param value a {@link de.cenote.jasperstarter.types.DsType} object.
     */
    public void setDbType(DsType value) {
        dbType = value;
    }

    /**
     * <p>hasDbType.</p>
     *
     * @return a boolean.
     */
    public boolean hasDbType() {
        if (dbType != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>Getter for the field <code>dbUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDbUrl() {
        return dbUrl;
    }

    /**
     * <p>Setter for the field <code>dbUrl</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setDbUrl(String value) {
        dbUrl = value;
    }

    /**
     * <p>Getter for the field <code>dbUser</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDbUser() {
        return dbUser;
    }

    /**
     * <p>Setter for the field <code>dbUser</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setDbUser(String value) {
        dbUser = value;
    }

    /**
     * <p>isVerbose.</p>
     *
     * @return a boolean.
     */
    public boolean isVerbose() {
        return verbose;
    }

    /**
     * <p>Setter for the field <code>verbose</code>.</p>
     *
     * @param value a boolean.
     */
    public void setVerbose(boolean value) {
        verbose = value;
    }

    /**
     * <p>Getter for the field <code>input</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getInput() {
        return input;
    }

    /**
     * <p>Setter for the field <code>input</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setInput(String value) {
        input = value;
    }

    /**
     * <p>Getter for the field <code>jdbcDir</code>.</p>
     *
     * @return a {@link java.io.File} object.
     */
    public File getJdbcDir() {
        return jdbcDir;
    }

    /**
     * <p>Setter for the field <code>jdbcDir</code>.</p>
     *
     * @param value a {@link java.io.File} object.
     */
    public void setJdbcDir(File value) {
        jdbcDir = value;
    }

    /**
     * <p>hasJdbcDir.</p>
     *
     * @return a boolean.
     */
    public boolean hasJdbcDir() {
        if (jdbcDir != null && !jdbcDir.getName().equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>Getter for the field <code>dataFile</code>.</p>
     *
     * @return a {@link java.io.File} object.
     */
    public File getDataFile() {
        return dataFile;
    }

    /**
     * <p>Setter for the field <code>dataFile</code>.</p>
     *
     * @param value a {@link java.io.File} object.
     */
    public void setDataFile(File value) {
        dataFile = value;
    }

    /**
     * Get InputStream corresponding to the configured dataFile.
     *
     * @return a {@link java.io.InputStream} object.
     * @throws net.sf.jasperreports.engine.JRException if any.
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
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDataFileName() {
        return dataFile.getName();
    }

    /**
     * <p>Getter for the field <code>csvFirstRow</code>.</p>
     *
     * @return a boolean.
     */
    public boolean getCsvFirstRow() {
        return csvFirstRow;
    }

    /**
     * <p>Setter for the field <code>csvFirstRow</code>.</p>
     *
     * @param value a boolean.
     */
    public void setCsvFirstRow(boolean value) {
        csvFirstRow = value;
    }

    /**
     * <p>Getter for the field <code>csvColumns</code>.</p>
     *
     * @return an array of {@link java.lang.String} objects.
     */
    public String[] getCsvColumns() {
        if (csvColumns == null) {
            // return an empty array instead of null
            return new String[0];
        } else {
            return csvColumns.split(",");
        }
    }

    /**
     * <p>Setter for the field <code>csvColumns</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setCsvColumns(String value) {
        csvColumns = value;
    }

    /**
     * <p>Getter for the field <code>csvRecordDel</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCsvRecordDel() {
        return csvRecordDel;
    }

    /**
     * <p>Setter for the field <code>csvRecordDel</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setCsvRecordDel(String value) {
        csvRecordDel = value;
    }

    /**
     * <p>Getter for the field <code>csvFieldDel</code>.</p>
     *
     * @return a char.
     */
    public char getCsvFieldDel() {
        return csvFieldDel.charAt(0);
    }

    /**
     * <p>Setter for the field <code>csvFieldDel</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setCsvFieldDel(String value) {
        csvFieldDel = value;
    }

    /**
     * <p>Getter for the field <code>csvCharset</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCsvCharset() {
        return csvCharset;
    }

    /**
     * <p>Setter for the field <code>csvCharset</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setCsvCharset(String value) {
        csvCharset = value;
    }

    /**
     * <p>Getter for the field <code>xmlXpath</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getXmlXpath() {
        return xmlXpath;
    }

    /**
     * <p>Setter for the field <code>xmlXpath</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setXmlXpath(String value) {
        xmlXpath = value;
    }

    /**
     * <p>Getter for the field <code>jsonQuery</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getJsonQuery() {
        return jsonQuery;
    }

    /**
     * <p>Setter for the field <code>jsonQuery</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setJsonQuery(String value) {
        jsonQuery = value;
    }

    /**
     * <p>Getter for the field <code>jsonQLQuery</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getJsonQLQuery() {
        return jsonQLQuery;
    }

    /**
     * <p>Setter for the field <code>jsonQLQuery</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setJsonQLQuery(String value) {
        jsonQLQuery = value;
    }

    /**
     * <p>Getter for the field <code>locale</code>.</p>
     *
     * @return a {@link java.util.Locale} object.
     */
    public Locale getLocale() {
        return LocaleUtils.toLocale(locale);
    }

    /**
     * <p>Setter for the field <code>locale</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setLocale(String value) {
        locale = value;
    }

    /**
     * <p>hasLocale.</p>
     *
     * @return a boolean.
     */
    public boolean hasLocale() {
        if (locale != null && !locale.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>Getter for the field <code>output</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getOutput() {
        return output;
    }

    /**
     * <p>Setter for the field <code>output</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setOutput(String value) { output = value; }

    /**
     * <p>hasOutput.</p>
     *
     * @return a boolean.
     */
    public boolean hasOutput() {
        if (output != null && !output.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>Getter for the field <code>outputFormats</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<OutputFormat> getOutputFormats() {
        return outputFormats;
    }

    /**
     * <p>Setter for the field <code>outputFormats</code>.</p>
     *
     * @param value a {@link java.util.List} object.
     */
    public void setOutputFormats(List<OutputFormat> value) { outputFormats = value; }

    /**
     * <p>Getter for the field <code>params</code>.</p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<String> getParams() {
        return params;
    }

    /**
     * <p>Setter for the field <code>params</code>. Each entry in the list is
     * a {@link java.lang.String} of the form:</p>
     *
     * <pre>
     *     name=value
     * </pre>
     *
     * <p>where <i>name</i> is the name of a parameter defined in the .jrxml
     * and <i>value</i> is the Java representation (e.g. boolean truth is
     * "true" or "false").</p>
     *
     * @param value a {@link java.util.List} object.
     */
    public void setParams(List<String> value) { params = value; }

    /**
     * <p>hasParams.</p>
     *
     * @return a boolean.
     */
    public boolean hasParams() {
        if (params != null && !params.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>Getter for the field <code>printerName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getPrinterName() {
        return printerName;
    }

    /**
     * <p>Setter for the field <code>printerName</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setPrinterName(String value) { printerName = value; }

    /**
     * <p>hasPrinterName.</p>
     *
     * @return a boolean.
     */
    public boolean hasPrinterName() {
        if (printerName != null && !printerName.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>Getter for the field <code>reportName</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getReportName() {
        return reportName;
    }

    /**
     * <p>Setter for the field <code>reportName</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setReportName(String value) { reportName = value; }

    /**
     * <p>hasReportName.</p>
     *
     * @return a boolean.
     */
    public boolean hasReportName() {
        if (reportName != null && !reportName.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>Getter for the field <code>resource</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getResource() {
        return resource;
    }

    /**
     * <p>Setter for the field <code>resource</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setResource(String value) { resource = value; }

    /**
     * <p>hasResource.</p>
     *
     * @return a boolean.
     */
    public boolean hasResource() {
        // the resource default if set is "" .constant("")
        if (resource != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>isWithPrintDialog.</p>
     *
     * @return a boolean.
     */
    public boolean isWithPrintDialog() {
        return withPrintDialog;
    }

    /**
     * <p>Setter for the field <code>withPrintDialog</code>.</p>
     *
     * @param value a boolean.
     */
    public void setWithPrintDialog(boolean value) { withPrintDialog = value; }

    /**
     * <p>isWriteJasper.</p>
     *
     * @return a boolean.
     */
    public boolean isWriteJasper() {
        return writeJasper;
    }

    /**
     * <p>Setter for the field <code>writeJasper</code>.</p>
     *
     * @param value a boolean.
     */
    public void setWriteJasper(boolean value) { writeJasper = value; }

    /**
     * <p>Getter for the field <code>copies</code>.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getCopies() {
        return copies;
    }

    /**
     * <p>Setter for the field <code>copies</code>.</p>
     *
     * @param value a {@link java.lang.Integer} object.
     */
    public void setCopies(Integer value) { copies = value; }

    /**
     * <p>hasCopies.</p>
     *
     * @return a boolean.
     */
    public boolean hasCopies() {
        if (copies != null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * <p>Getter for the field <code>outFieldDel</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getOutFieldDel() {
        return outFieldDel;
    }

    /**
     * <p>Setter for the field <code>outFieldDel</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setOutFieldDel(String value) { outFieldDel = value; }

    /**
     * <p>Getter for the field <code>outCharset</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getOutCharset() {
        return outCharset;
    }

    /**
     * <p>Setter for the field <code>outCharset</code>.</p>
     *
     * @param value a {@link java.lang.String} object.
     */
    public void setOutCharset(String value) { outCharset = value; }

    // END argparse4j arguments
    // TODO: overwrite toString()
}
