/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package langtag;

import static langtag.LangTagLib.isA2Z;
import static langtag.LangTagLib.isA2ZN;

import java.util.Locale;

/**
 * Class that represents a language tag as a tuple of 5 strings (lang, script, region,
 * variant, extension).
 * <p>
 * See {@link LangTagParser} for generating {@code LangTag}s. Note this returns the old ISO code.
 * See the javadoc of {@link Locale#getLanguage()}.
 * <p>
 * {@link LangTagParserAlt} is an alternative version which does not use the Java locale
 * built-in functionality and does not canonical language names (replace one name by another).
 * <p>
 * Language tags are BCP 47.
 * <p>
 * RFCs:
 * <ul>
 * <li><a href="https://tools.ietf.org/html/5646">RFC 5646</a> "Tags for Identifying Languages"
 * <li><a href="https://tools.ietf.org/html/4646">RFC 4646</a> "Tags for Identifying Languages"
 * <li><a href="https://tools.ietf.org/html/3066">RFC 3066</a> "Tags for the Identification of Languages"
 * </ul>
 * Related:
 * <ul>
 * <li><a href="https://tools.ietf.org/html/4647">RFC 4647</a> "Matching of Language Tags"
 * <li><a href="https://tools.ietf.org/html/4234">RFC 4232</a> "Augmented BNF for Syntax Specifications: ABNF"
 * </ul>
 */

public class LangTag {
    private final String lang;
    private final String script;
    private final String region;
    private final String variant;
    private final String extension;

    /** Create a langtag.
     *  Returns null if not valid syntax.
     */
    public static LangTag create(String string) {
        return LangTagParser.parse(string);
    }

    /**
     * Return a string that is the lang tag except in canonical form.
     * Returns null if not valid syntax.
     */
    public static String canonical(String string) {
        LangTag langTag = LangTagParser.parse(string);
        return LangTagParser.canonical(string);
    }

    /** Construct a {@code LangTag} from component parts.
     * <p>
     * null means "empty"
     */
    public
    /*package*/ LangTag(String lang, String script, String region, String variant, String extension) {
        this.lang       = nullToEmpty(lang,      "'lang' is null");
        this.script     = nullToEmpty(script,    "'script' is null");
        this.region     = nullToEmpty(region,    "'region' is null");
        this.variant    = nullToEmpty(variant,   "'variant' is null");
        this.extension  = nullToEmpty(extension, "'extension' is null");
    }

    private static String nullToEmpty(String x, String msg) {
        // Choice.
//        if ( x == null )
//            throw new NullPointerException(msg);
        return x==null ? "" : x;
    }

    @Override
    public String toString() { return asString(); }

    /** Return as a string - canonicalization is depend on whether the
     * the parser canonicalized on input.
     */
    public String asString() {

        if ( lang == null || lang.isEmpty() ) {
            // Grandfathered
            return extension;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(lang);
        appendIf(sb, script);
        appendIf(sb, region);
        appendIf(sb, variant);
        appendIf(sb, extension);
        return sb.toString();
    }

    private void appendIf(StringBuilder sb, String str) {
        if ( str == null || str.isEmpty() )
            return;
        sb.append("-");
        sb.append(str);
    }

    public String getLang()         { return lang; }
    public String getScript()       { return script; }
    public String getRegion()       { return region; }
    public String getVariant()      { return variant; }
    public String getExtension()    { return extension; }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((extension == null) ? 0 : extension.hashCode());
        result = prime * result + ((lang == null) ? 0 : lang.hashCode());
        result = prime * result + ((region == null) ? 0 : region.hashCode());
        result = prime * result + ((script == null) ? 0 : script.hashCode());
        result = prime * result + ((variant == null) ? 0 : variant.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        LangTag other = (LangTag)obj;
        if ( extension == null ) {
            if ( other.extension != null )
                return false;
        } else if ( !extension.equals(other.extension) )
            return false;
        if ( lang == null ) {
            if ( other.lang != null )
                return false;
        } else if ( !lang.equals(other.lang) )
            return false;
        if ( region == null ) {
            if ( other.region != null )
                return false;
        } else if ( !region.equals(other.region) )
            return false;
        if ( script == null ) {
            if ( other.script != null )
                return false;
        } else if ( !script.equals(other.script) )
            return false;
        if ( variant == null ) {
            if ( other.variant != null )
                return false;
        } else if ( !variant.equals(other.variant) )
            return false;
        return true;
    }

    private static char CH_DASH = '-';

    /**
     * Basic syntax check for a language tag, equivalent to what is in the Turtle and SPARQL specs
     * <pre>[a-zA-Z]+('-' [a-zA-Z0-9]+)*</pre>
     * Passing this check does <em>not</em> guarantee the language tag is correct.
     */
    public static boolean basicCheckSyntax(String languageTag) {
        int len = languageTag.length();
        int idx = 0;
        boolean first = true;
        while (idx < languageTag.length()) {
            int idx2 = checkPart(languageTag, idx, first);
            first = false;
            if ( idx2 == idx )
                // zero length part.
                return false;
            idx = idx2;
            if ( idx == len )
                return true;
            if ( languageTag.charAt(idx) != CH_DASH )
                return false;
            idx++;
            if ( idx == len )
                // trailing DASH
                return false;
        }
        return true;
    }

    private static int checkPart(String languageTag, int idx, boolean alphaOnly) {
        for (; idx < languageTag.length(); idx++) {
            int ch = languageTag.charAt(idx);
            if ( alphaOnly ) {
                if ( isA2Z(ch) )
                    continue;
            } else {
                if ( isA2ZN(ch) )
                    continue;
            }
            // Not acceptable.
            return idx;
        }
        // Off end.
        return idx;
    }
}
