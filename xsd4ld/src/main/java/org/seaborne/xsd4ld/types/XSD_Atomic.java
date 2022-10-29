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
import org.seaborne.xsd4ld.XSDConst;
import org.seaborne.xsd4ld.XSDDatatype;


public class XSD_Atomic extends XSDDatatype {

    public XSD_Atomic() {
        super(XSDConst.xsd_atomic, XSDConst.xsd_simple, ValueClass.ANY, null);
    }

    @Override
    protected Object valueOrException(String lex) {
        return lex;
    }
}
