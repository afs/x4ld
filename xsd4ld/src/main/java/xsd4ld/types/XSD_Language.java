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

import java.util.Locale;
import java.util.regex.Pattern;

import xsd4ld.XSDConst;
import xsd4ld.XSDTypeRegistry;

public class XSD_Language extends BaseString {
    private static Locale.Builder locBuild = new Locale.Builder();
    private Pattern pattern = XSDTypeRegistry.getRegex(XSDConst.xsd_language);

    public XSD_Language() {
        super(XSDConst.xsd_language, XSDConst.xsd_token);
    }

    @Override
    protected String valueOrException(String lex) {
        if ( isValid(lex) )
            return locBuild.setLanguageTag(lex).build().toLanguageTag();
        return null;
    }

    @Override
    public boolean isValid(String lex) {
        return pattern.matcher(lex).matches();
    }

    @Override
    public Pattern getRegex() {
        return XSDTypeRegistry.getRegex(XSDConst.xsd_language);
    }
}
