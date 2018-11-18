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
 * <p>Command class.</p>
 *
 * @author Volker Voßkämper
 * @version $Id: $Id
 */
public enum Command {

    COMPILE,
    CP, // alias for COMPILE
    PROCESS,
    PR, // alias for PROCESS
    LIST_PRINTERS,
    PRINTERS, // alias for LIST_PRINTERS
    LPR, // alias for LIST_PRINTERS
    LIST_PARAMETERS,
    PARAMS, // alias for LIST_PARAMS
    LPA; // alias for LIST_PARAMS

    /**
     * <p>getCommand.</p>
     *
     * @param name a {@link java.lang.String} object.
     * @return a {@link de.cenote.jasperstarter.types.Command} object.
     */
    public static Command getCommand(String name) {
        return valueOf(name.toUpperCase());
    }
}
