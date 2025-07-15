/*
 * Copyright (c) 2011-2020 Progress Software Corporation and/or its subsidiaries or affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.marklogic.contentpump.utilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xerces.util.XML11Char;

public class XMLUtil {
    public static final Log LOG = LogFactory.getLog(XMLUtil.class);
    
    /**
     * Get valid element name from a given string
     * @param name
     * @return
     */
    public static String getValidName(String name) {
        StringBuilder validname = new StringBuilder();
        char ch = name.charAt(0);
        if (!XML11Char.isXML11NameStart(ch)) {
            LOG.warn("Prepend _ to " + name);
            validname.append("_");
        }
        for (int i = 0; i < name.length(); i++) {
            ch = name.charAt(i);
            if (!XML11Char.isXML11Name(ch)) {
                LOG.warn("Character " + ch + " in " + name
                    + " is converted to _");
                validname.append("_");
            } else {
                validname.append(ch);
            }
        }

        return validname.toString();
    }
    
    public static String convertToCDATA(String arg) {
        StringBuilder sb = new StringBuilder();
        sb.append("<![CDATA[");
        sb.append(arg);
        sb.append("]]>");
        return sb.toString();
    }
}
