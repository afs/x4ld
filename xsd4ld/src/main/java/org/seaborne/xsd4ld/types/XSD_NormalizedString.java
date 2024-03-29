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

import org.seaborne.xsd4ld.XSDConst;

public class XSD_NormalizedString extends BaseString {
    // No newline (#xD), line feed (#xA) nor tab (#x9)
    public XSD_NormalizedString() {
        super(XSDConst.xsd_normalizedString, XSDConst.xsd_string);
    }
    @Override
    public boolean valid_NL_LF_TAB(String lex) {
        return test_valid_NL_LF_TAB(lex);
    }
}
