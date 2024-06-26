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
import org.seaborne.xsd4ld.XSDTypeRegistry;

public class XSD_Base64Binary extends BaseBinary {

    public XSD_Base64Binary() {
        super(XSDConst.xsd_base64Binary, XSDConst.xsd_atomic, XSDTypeRegistry.getRegex(XSDConst.xsd_base64Binary));
    }

    @Override
    protected byte[] valueOrException(String lex) {
        // Decodes base64, base64 with spaces (required by xsd:base64Binary) and base64url)
        return org.apache.commons.codec.binary.Base64.decodeBase64(lex);
        //return Base64.getMimeDecoder().decode(lex);
    }
}
