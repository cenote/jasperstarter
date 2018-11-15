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
 * <p>OutputFormat class.</p>
 *
 * @author Volker Voßkämper
 * @version $Revision: 5b92831f1a80:54 branch:default $
 */
public enum OutputFormat {

    view,
    print,
    jrprint,
    pdf,
    // pdfa1,        // not implemented jet
    rtf,
    docx,
    odt,
    html,
    xml,
    // xmlembed,     // not implemented jet
    xls,
    xlsMeta,
    xlsx,
    // jxl,          // not implemented jet
    // jxlMetadata,  // not implemented jet
    csv,
    csvMeta,
    ods,
    pptx,
    xhtml;
}
