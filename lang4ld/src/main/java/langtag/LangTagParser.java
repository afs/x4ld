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

package langtag;

import java.util.IllformedLocaleException;
import java.util.Locale;
import java.util.Locale.Builder;
import java.util.Set;

/**
 * LangTag parsing.
 * <p>
 * A thin layer over the JDK {@link Locale} and {@link Builder} to introduce a class
 * {@link LangTag}.
 */
public class LangTagParser {

    private LangTagParser() { }

    private static Locale.Builder locBuild = new Locale.Builder();

    public static LangTag parse(String x) {
        try {
            locBuild.clear();
            locBuild.setLanguageTag(x);
            return asLangTag(locBuild);
        } catch (IllformedLocaleException ex) {
            return null;
        }
    }

    public static String canonical(String str) {
        try {
            // Does not do conversion of language for ISO 639 codes that have changed.
            return locBuild.setLanguageTag(str).build().toLanguageTag();
        } catch (IllformedLocaleException ex) {
            return str;
        }
    }

    private static LangTag asLangTag(Locale.Builder locBuild) {
        Locale lc = locBuild.build();
        Set<Character> extkeys = lc.getExtensionKeys();
        StringBuilder sb = new StringBuilder();
        extkeys.forEach(k->{
            String ext = lc.getExtension(k);
            if ( sb.length() != 0 )
                sb.append('-');
            sb.append(k);
            sb.append('-');
            sb.append(ext);
        });
        String extension = sb.toString();
        return new LangTag(lc.getLanguage(),
                           lc.getScript(),
                           lc.getCountry(),
                           lc.getVariant(),
                           extension);
    }
}
