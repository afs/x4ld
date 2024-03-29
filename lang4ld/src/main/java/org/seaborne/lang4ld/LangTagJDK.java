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

package org.seaborne.lang4ld;

import java.util.IllformedLocaleException;
import java.util.Locale;
import java.util.Locale.Builder;
import java.util.Objects;
import java.util.Set;

/**
 * LangTag parsing.
 * <p>
 * A thin layer over the JDK {@link Locale} and {@link Builder} to introduce a class
 * {@link LangTag}.
 * <p>
 * This is not RFC 5646 compliant.
 * It's quite close -
 */
public class LangTagJDK implements LangTag {
    private final String fmtString;
    private final String lang;
    private final String script;
    private final String region;
    private final String variant;
    private final String extension;
    private final String privateUse;

    private LangTagJDK(String fmtString, String language, String script, String region, String variant, String extension, String privateUse) {
            this.fmtString  = Objects.requireNonNull(fmtString);
            this.lang       = maybe(language);
            this.script     = maybe(script);
            this.region     = maybe(region);
            this.variant    = maybe(variant);
            this.extension  = maybe(extension);
            this.privateUse = maybe(privateUse);
    }

    private static String maybe(String x) {
        // Choice.
        if ( x == null )
            return null;
        if ( x.isEmpty() )
            return null;
        return x;
    }

    @Override public String str() { return fmtString; }

    @Override public String getLanguage() { return lang; }
    @Override public String getScript() { return script; }
    @Override public String getRegion() { return region; }
    @Override public String getVariant() { return variant; }
    @Override public String getExtension() { return extension; }
    @Override public String getPrivateUse()
    { return privateUse; }

    private static Locale.Builder locBuild = new Locale.Builder();

    public static LangTag create(String x) {
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
        String privateUse = null;
        for ( Character k : extkeys ) {
            String ext = lc.getExtension(k);
            if ( sb.length() != 0 )
                sb.append('-');
            sb.append(k);
            sb.append('-');
            sb.append(ext);
        }
        String extension = sb.toString();
        return new LangTagJDK(lc.toLanguageTag(),
                              lc.getLanguage(),
                              lc.getScript(),
                              lc.getCountry(),
                              lc.getVariant(),
                              extension,
                              privateUse);
    }
}
