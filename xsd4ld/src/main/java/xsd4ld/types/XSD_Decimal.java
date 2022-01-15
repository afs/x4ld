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

package xsd4ld.types;

import static xsd4ld.XSDConst.xsd_atomic;
import static xsd4ld.XSDConst.xsd_decimal;

import java.math.BigDecimal;

import xsd4ld.XSDTypeRegistry;

public class XSD_Decimal extends BaseDecimal {
    public XSD_Decimal() {
        super(xsd_decimal, xsd_atomic, XSDTypeRegistry.getRegex(xsd_decimal));
    }

    @Override
    protected BigDecimal valueOrException(String lex) {
        // This parses it
        BigDecimal decimal = new BigDecimal(lex);
        // Java allows "1e0" as a BigDecimal.
        if ( lex.indexOf('e') != -1 || lex.indexOf('E') != -1 )
            return null;
        return decimal; 
    }
}

