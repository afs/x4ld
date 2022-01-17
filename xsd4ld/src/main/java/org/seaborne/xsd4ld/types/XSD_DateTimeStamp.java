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

import java.time.temporal.Temporal;

import org.seaborne.xsd4ld.XSDConst;
import org.seaborne.xsd4ld.lib.DateTimeStruct;

public class XSD_DateTimeStamp extends BaseDateTime {
    public XSD_DateTimeStamp() {
        super(XSDConst.xsd_dateTimeStamp, XSDConst.xsd_dateTime, DateTimeStruct::parseDateTimeStamp);
    }

    @Override
    protected Temporal valueOrException(String lex) {
        Temporal obj = super.valueOrException(lex);
        if ( obj == null )
            return null;
        // OK as an xsd:dateTime - check it has a timezone.
        //  timezoneFrag ::= 'Z' | ('+' | '-') (('0' digit | '1' [0-3]) ':' minuteFrag | '14:00')
        if ( lex.indexOf('Z') != -1 )
            return obj;
        // Check a legal xsd:dateTime ends with timezoneFrag
        // Z or a +/- at length-6

        // Avoid regex!
        int n = lex.length();
        char z = lex.charAt(n-6);
        if ( z != '+' && z != '-' )
            throw new IllegalArgumentException("Not valid as xsd:dateTimeStamp: "+lex);
        return obj;
    }
}
