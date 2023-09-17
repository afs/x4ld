/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  See the NOTICE file distributed with this work for additional
 *  information regarding copyright ownership.
 */

package org.seaborne.xsd4ld.types;

import org.seaborne.xsd4ld.ValueSpace;
import org.seaborne.xsd4ld.XSDConst;
import org.seaborne.xsd4ld.XSDDatatype;
import org.seaborne.xsd4ld.XSDTypeRegistry;

/** Commonality for XSD_Double and XSD_Float */
abstract class BaseDoubleFloat extends XSDDatatype {

    public BaseDoubleFloat(String shortName, ValueSpace valueClass) {
        super(shortName, XSDConst.xsd_atomic, valueClass, XSDTypeRegistry.getRegex(shortName));
    }

    // Used by the 2 subclasses.
    // XSD to Java
    // Java spells "INF" as "Infinity"
    static String fix(String lex) {
        if ( lex.equals("INF") )
            return "Infinity";
        if ( lex.equals("+INF") )
            return "+Infinity";
        if ( lex.equals("-INF") )
            return "-Infinity";
        return lex;
    }
}
