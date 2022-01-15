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

package xsd4ld;

import static xsd4ld.XSDConst.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/** Type registry */

public class XSDTypeRegistry {
    // Shortname to type
    private static Map<String, XSDDatatype> registry = new HashMap<>();

    // Called from XSD (avoids risk of circularity in initialization).

    /*package*/ static void register(String name, XSDDatatype type) {
        registry.put(name, type);
    }

    /** Get the XSDDatatype for a given name */
    public static XSDDatatype getType(String shortName) {
        return registry.get(shortName);
    }

    /** Get the XSDDatatype for a given URI */
    public static XSDDatatype getTypeByURI(String uriStr) {
        String ns = XSD.getURI();
        if ( ! uriStr.startsWith(ns) )
            return null;
        String x = uriStr.substring(ns.length());
        return getType(x);
    }

    /** Get the XSDDatatype for a given URI */
    public static XSDDatatype getDerivedFrom(String shortName) {
        XSDDatatype datatype = getType(shortName);
        if ( datatype == null )
            return null;
        return getDerivedFrom(datatype);
    }

    /** Get the XSDDatatype for a given URI */
    public static XSDDatatype getDerivedFrom(XSDDatatype datatype) {
        String x = datatype.derivedFrom();
        if ( x == null )
            return null;
        return getType(x);
    }

    /** Get the names of all registered types */
    public static Collection<String> registeredNames() {
        return Collections.unmodifiableCollection(registry.keySet());
    }

    /** Get the names of all registered types */
    public static Collection<XSDDatatype> registeredTypes() {
        return Collections.unmodifiableCollection(registry.values());
    }

    // -- Regular expressions (string and compiled)
    private static Map<String, Pattern> patterns = new HashMap<>();

    static {
        /* Cut-and-paste from spec, \ => \\ and wrapped as a string. */
        // short name, regex
        registerRegex(xsd_string,        null);
        registerRegex(xsd_decimal,       "(\\+|-)?([0-9]+(\\.[0-9]*)?|\\.[0-9]+)");
        registerRegex(xsd_integer,       "(\\+|-)?([0-9]+)");
        registerRegex(xsd_boolean,       "(true|false|1|0)");

        String x  = "(\\+|-)?([0-9]+(\\.[0-9]*)?|\\.[0-9]+)([Ee](\\+|-)?[0-9]+)?|(\\+|-)?INF|NaN";
        registerRegex(xsd_float,       x);
        registerRegex(xsd_double,      x);
        registerRegex(xsd_precisionDecimal, x);

        String datetimePattern =
            "-?([1-9][0-9]{3,}|0[0-9]{3})"
            +"-(0[1-9]|1[0-2])"
            +"-(0[1-9]|[12][0-9]|3[01])"
            +"T(([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?|(24:00:00(\\.0+)?))"
            +"(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?";
        registerRegex(xsd_dateTime,       datetimePattern);
        // Mandatory timestamp
        String datetimestampPattern =
            "-?([1-9][0-9]{3,}|0[0-9]{3})"
            +"-(0[1-9]|1[0-2])"
            +"-(0[1-9]|[12][0-9]|3[01])"
            +"T(([01][0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9](\\.[0-9]+)?|(24:00:00(\\.0+)?))"
            // Remove ? at end from the one above.
            +"(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))";
        registerRegex(xsd_dateTimeStamp,       datetimestampPattern);

        // dateTimeStamp : additional:
        //'.*(Z|(\+|-)[0-9][0-9]:[0-9][0-9])'.

        registerRegex(xsd_gYear,         "-?([1-9][0-9]{3,}|0[0-9]{3})(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?");
        registerRegex(xsd_gYearMonth,    "-?([1-9][0-9]{3,}|0[0-9]{3})-(0[1-9]|1[0-2])(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?");
        registerRegex(xsd_gMonthDay,     "--(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?");
        registerRegex(xsd_gMonth,        "--(0[1-9]|1[0-2])(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?");
        registerRegex(xsd_gDay,          "---(0[1-9]|[12][0-9]|3[01])(Z|(\\+|-)((0[0-9]|1[0-3]):[0-5][0-9]|14:00))?");
        registerRegex(xsd_language,      "[a-zA-Z]{1,8}(-[a-zA-Z0-9]{1,8})*");

        registerRegex(xsd_hexBinary,     "([0-9a-fA-F]{2})*");
        // Notes spaces.
        registerRegex(xsd_base64Binary,  "((([A-Za-z0-9+/] ?){4})*(([A-Za-z0-9+/] ?){3}[A-Za-z0-9+/]|([A-Za-z0-9+/] ?){2}[AEIMQUYcgkosw048] ?=|[A-Za-z0-9+/] ?[AQgw] ?= ?=))?");

        String durationPattern =
            "-?P( ( ( [0-9]+Y([0-9]+M)?([0-9]+D)?"
                + "       | ([0-9]+M)([0-9]+D)?"
                + "       | ([0-9]+D)"
                + "       )"
                + "       (T ( ([0-9]+H)([0-9]+M)?([0-9]+(\\.[0-9]+)?S)?"
                + "          | ([0-9]+M)([0-9]+(\\.[0-9]+)?S)?"
                + "          | ([0-9]+(\\.[0-9]+)?S)"
                + "          )"
                + "       )?"
                + "    )"
                + "  | (T ( ([0-9]+H)([0-9]+M)?([0-9]+(\\.[0-9]+)?S)?"
                + "       | ([0-9]+M)([0-9]+(\\.[0-9]+)?S)?"
                + "       | ([0-9]+(\\.[0-9]+)?S)"
                + "       )"
                + "    )"
                + "  )";
        durationPattern = durationPattern.replace(" ", "");
        registerRegex(xsd_duration,              durationPattern);
        registerRegex(xsd_yearMonthDuration,     "-?P((([0-9]+Y)([0-9]+M)?)|([0-9]+M))");

        // duDayTimeFrag ::= (duDayFrag duTimeFrag?) | duTimeFrag
        String duTimeFrag =
            "(T (  ([0-9]+H)([0-9]+M)?([0-9]+(\\.[0-9]+)?S)?"
            +"  | ([0-9]+M)([0-9]+(\\.[0-9]+)?S)?"
            +"  | ([0-9]+(\\.[0-9]+)?S)"
            +"  )"
            +")";
        duTimeFrag = duTimeFrag.replace(" ", "");

        String dayTimePattern = "-?P((([0-9]+D)("+duTimeFrag+")?)|("+duTimeFrag+"))";
        registerRegex(xsd_dayTimeDuration,       dayTimePattern);

        // [2]      Char       ::=      [#x1-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]
        // Java can't deal with characters outside the basic plane directly
        // It uses surrogates so we need to put the surrogate blocks back in.
        //String xmlChar = "[\u0001-\uD7FF]|[\uE000-\uFFFD]"; // |[\U00010000-\U0010FFFF]";

        String xmlChar = "[^\uFFFE\uFFFF\u0000]";
//        String xmlRestrictedChar = "[\u0001-\u0008]|[\u000B-\u000C]|[\u000E-\u001F]|[\u007F-\u0084]|[\u0086-\u009F]";
//        // Not u000D u000A - they break the line!
//        String xmlS = "(\u0020|\u0009|\r|\n)";

        registerRegex(xsd_anyURI, "((" + xmlChar + ")*)");

        /*
// any Unicode character, excluding the surrogate blocks, FFFE, and FFFF.
[2]     Char               ::=      [#x1-#xD7FF] | [#xE000-#xFFFD] | [#x10000-#x10FFFF]
[2a]    RestrictedChar     ::=      [#x1-#x8] | [#xB-#xC] | [#xE-#x1F] | [#x7F-#x84] | [#x86-#x9F]
[4]     NameStartChar      ::=      ":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] | [#x370-#x37D] | [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
[4a]    NameChar           ::=      NameStartChar | "-" | "." | [0-9] | #xB7 | [#x0300-#x036F] | [#x203F-#x2040]
[5]     Name               ::=      NameStartChar (NameChar)*
[6]     Names              ::=      Name (#x20 Name)*
[7]     Nmtoken            ::=      (NameChar)+
[8]     Nmtokens           ::=      Nmtoken (#x20 Nmtoken)*
 */

        /* Discouraged:
[#x1-#x8], [#xB-#xC], [#xE-#x1F], [#x7F-#x84], [#x86-#x9F], [#xFDD0-#xFDDF],
[#x1FFFE-#x1FFFF], [#x2FFFE-#x2FFFF], [#x3FFFE-#x3FFFF],
[#x4FFFE-#x4FFFF], [#x5FFFE-#x5FFFF], [#x6FFFE-#x6FFFF],
[#x7FFFE-#x7FFFF], [#x8FFFE-#x8FFFF], [#x9FFFE-#x9FFFF],
[#xAFFFE-#xAFFFF], [#xBFFFE-#xBFFFF], [#xCFFFE-#xCFFFF],
[#xDFFFE-#xDFFFF], [#xEFFFE-#xEFFFF], [#xFFFFE-#xFFFFF],
[#x10FFFE-#x10FFFF].
         */

    }

    private static void registerRegex(String shortName, String regex) {
        if ( regex == null )
            return;
        Pattern pattern = Pattern.compile(regex);
        patterns.put(shortName, pattern);
    }

    /** Get the XSD regex associated with the name (stort name , not URI).
     * Maybe null.
     */
    public static Pattern getRegex(String shortName) {
        return patterns.get(shortName);
    }

    /** Get the XSD regex associated with the name (stort name , not URI).
     * Maybe null.
     */
    public static String getRegexStr(String shortName) {
        Pattern p = getRegex(shortName);
        if ( p == null )
            return null;
        return p.pattern();
    }
}

