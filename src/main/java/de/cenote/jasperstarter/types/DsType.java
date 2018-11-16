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
 * Types of Datasources
 *
 * @author Volker Voßkämper
 * @version $Revision: 5b92831f1a80:54 branch:default $
 */
public enum DsType {

    none,
    csv,
    xml,
    json,
    jsonql,
    mysql("com.mysql.jdbc.Driver", 3306),
    postgres("org.postgresql.Driver", 5432),
    oracle("oracle.jdbc.OracleDriver", 1521),
    generic;
    private final String driver;
    private final Integer port;

    DsType() {
        this.driver = null;
        this.port = null;
    }

    DsType(String driver, Integer port) {
        this.driver = driver;
        this.port = port;
    }

    /**
     * <p>Getter for the field <code>driver</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDriver() {
        return this.driver;
    }

    /**
     * <p>Getter for the field <code>port</code>.</p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    public Integer getPort() {
        return this.port;
    }
}
