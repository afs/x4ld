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

import org.seaborne.xsd4ld.ValueClass;
import org.seaborne.xsd4ld.XSDDatatype;

abstract class BaseString extends XSDDatatype {

    public BaseString(String shortName, String baseType) {
        super(shortName, baseType, ValueClass.STRING, null);
    }

    // Extra validate.
    // XSD_AbstractInteger?

    @Override
    protected String valueOrException(String lex) {
        if ( isValid(lex) )
            return lex;
        return null;
    }

    @Override
    public boolean isValid(String lex) {
        return valid_NL_LF_TAB(lex);
    }

    protected boolean valid_NL_LF_TAB(String lex) {
        return true;
    }

    protected static final char NL = '\n';    // #xD
    protected static final char LF = '\r';    // #xA
    protected static final char TAB = '\u0009';

    protected static boolean test_valid_NL_LF_TAB(String lex) {
        for ( int i = 0; i < lex.length(); i++ ) {
            char ch = lex.charAt(i);
            if ( ch == NL || ch == LF || ch == TAB )
                return false;
        }
        return true;
    }

//    @Override
//  public abstract int hashCode( );
//  @Override
//  public abstract boolean equals(Object other);
}
