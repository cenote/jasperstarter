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
package de.cenote.tools.printing;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

/**
 *
 * @author Volker Voßkämper <vvo at cenote.de>
 * @version $Revision: 5b92831f1a80:54 branch:default $
 */
public class Printerlookup {

    public static PrintService getPrintservice(String printername) {
        return getPrintservice(printername, false, false);
    }

    public static PrintService getPrintservice(String printername, Boolean startWithMatch) {
        return getPrintservice(printername, startWithMatch, false);
    }

    public static PrintService getPrintservice(String printername, Boolean startWithMatch, Boolean escapeSpace) {
        //System.out.println("Printerlookup: search for: " + printername);
        PrintService returnval = null;
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        // first try a case sensitive match
        for (PrintService service : services) {
            if (service.getName().equals(printername)) {
                returnval = service;
            }
        }
        if (returnval == null) { // try a case insensitive match
            //System.out.println("Printerlookup: try a case insensitive match");
            for (PrintService service : services) {
                if (service.getName().equalsIgnoreCase(printername)) {
                    if (returnval == null) {
                        returnval = service;
                    } else {
                        //throw new Exception("Printername \"" + printername + "\" is ambiguous!");
                    }
                }
            }
        }
        if (returnval == null && startWithMatch) { // try a case insensitive startWith match
            //System.out.println("Printerlookup: try a case insensitive startWith match");
            for (PrintService service : services) {
                if (service.getName().toLowerCase().startsWith(printername.toLowerCase())) {
                    if (returnval == null) {
                        returnval = service;
                    } else {
                        //throw new Exception("Printername \"" + printername + "\" is ambiguous!");
                    }
                }
            }
        }
        if (returnval == null && escapeSpace) { // try a case insensitive match with space excaped by underline
            //System.out.println("Printerlookup: try a case insensitive match with space excaped by underline");
            for (PrintService service : services) {
                String excapedPrintername = printername.toLowerCase().replace(" ", "_");
                String escapedServiceName = service.getName().toLowerCase().replace(" ", "_");
                if (escapedServiceName.equals(excapedPrintername)) {
                    if (returnval == null) {
                        returnval = service;
                    } else {
                        //throw new Exception("Printername \"" + printername + "\" is ambiguous!");
                    }
                }
            }
        }
        if (returnval == null && startWithMatch && escapeSpace) { // try a case insensitive startWith match with space excaped by underline
            //System.out.println("Printerlookup: try a case insensitive startWith match with space excaped by underline");
            for (PrintService service : services) {
                String excapedPrintername = printername.toLowerCase().replace(" ", "_");
                String escapedServiceName = service.getName().toLowerCase().replace(" ", "_");
                if (escapedServiceName.startsWith(excapedPrintername)) {
                    if (returnval == null) {
                        returnval = service;
                    } else {
                        //throw new Exception("Printername \"" + printername + "\" is ambiguous!");
                    }
                }
            }
        }
        return returnval;
    }
}
