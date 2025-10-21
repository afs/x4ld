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

package org.seaborne.mt4ld;

import java.util.ArrayList;
import java.util.List;

/*
 MIME type
    == RFC 2045: Full grammar below
        Basic shape: allows :
            type "/" subtype *(";" parameter)
            type is one of some fixed names, an extension token (in the registry), or "x-"
        Syntax: https://tools.ietf.org/html/rfc2045#section-5.1

    Modified by RFC 4288 and RFC 6838
    4288 -> does not refer to the registry.
    6838 -> restricts the type to start with an alphanumeric.

    == RFC 4288:
         type "/" subtype ["+" suffix] *[";" parameter]

    Type and subtype names MUST conform to the following ABNF:

       type-name = reg-name
       subtype-name = reg-name

       reg-name = 1*127reg-name-chars
       reg-name-chars = ALPHA / DIGIT / "!" /
                       "#" / "$" / "&" / "." /
                       "+" / "-" / "^" / "_"

    == RFC 6838 (4.2)
      type-name = restricted-name
      subtype-name = restricted-name

      restricted-name = restricted-name-first *126restricted-name-chars
      restricted-name-first  = ALPHA / DIGIT
      restricted-name-chars  = ALPHA / DIGIT / "!" / "#" /
                               "$" / "&" / "-" / "^" / "_"
      restricted-name-chars =/ "." ; Characters before first dot always
                                   ; specify a facet name
      restricted-name-chars =/ "+" ; Characters after last plus always
                                   ; specify a structured syntax suffix

    ==== RFC2045 Grammar
        https://tools.ietf.org/html/rfc2045#section-5.1
     content := "Content-Type" ":" type "/" subtype
                *(";" parameter)
                ; Matching of media type and subtype
                ; is ALWAYS case-insensitive.

     type := discrete-type / composite-type

     discrete-type := "text" / "image" / "audio" / "video" /
                      "application" / extension-token

     composite-type := "message" / "multipart" / extension-token

     extension-token := ietf-token / x-token

     ietf-token := <An extension token defined by a
                    standards-track RFC and registered
                    with IANA.>

     x-token := <The two characters "X-" or "x-" followed, with
                 no intervening white space, by any token>

     subtype := extension-token / iana-token

     iana-token := <A publicly-defined extension token. Tokens
                    of this form must be registered with IANA
                    as specified in RFC 2048.>

     parameter := attribute "=" value

     attribute := token
                  ; Matching of attributes
                  ; is ALWAYS case-insensitive.

     value := token / quoted-string

     token := 1*<any (US-ASCII) CHAR except SPACE, CTLs,
                 or tspecials>

     tspecials :=  "(" / ")" / "<" / ">" / "@" /
                   "," / ";" / ":" / "\" / <">
                   "/" / "[" / "]" / "?" / "="
                   ; Must be in quoted-string,
                   ; to use within parameter values
 */

public class MimeType {
    private int startType = -1;
    private int endType = -1;
    private int startSubType = -1;
    private int endSubType = -1;

    // Parameters attribute-value pairs are extracted on-demand
    private int startParameters = -1;


    private final String str;
    private final int length;
    // Unicode - not a character
    private static final char EOF = 0xFFFF; //ParseLib.EOF;

    public static MimeType create(String str) {
        MimeType mt = new MimeType(str);
        mt.parse();
        return mt;
    }

    public static class MimeTypeParseException extends RuntimeException {
        public MimeTypeParseException(String msg) { super(msg); }
    }

    private interface ParameterAction { void parameter(int startAttribute, int endAttribute, int startValue, int endValue); }

    private MimeType(String str) {
        this.str = str;
        this.length = str.length();
    }

    public boolean hasType() {
        return startType >= 0 && endType > 0;
    }

    public boolean hasSubType() {
        return startSubType >= 0 && endSubType > 0;
    }

    public boolean hasParameters() {
        return startParameters >= 0 ;
    }
    public String getType() {
        if ( ! hasType() )
            return null;
        return str.substring(startType, endType);
    }

    public String getSubType() {
        if ( ! hasSubType() )
            return null;
        return str.substring(startSubType, endSubType);
    }

    public List<Parameter> getParameters() {
        if( ! hasParameters() )
            return null;
        List<Parameter> params = new ArrayList<>();
        parseParameters(startParameters, (a0, a1, v0, v1)->{
                            String attr = str.substring(a0, a1);
                            String val = str.substring(v0, v1);
                            // Maybe a quoted string with escapes.
                            if ( val.startsWith("\"") )
                                val = unQuoteUnEscape(val);
                            params.add(new Parameter(attr,  val));
                        });
        return params;
    }

    @Override
    public String toString() {
        // Rebuild, rather than return str. Looses quoted-strings.
        StringBuilder sb = new StringBuilder();
        if ( startType == -1 )
            return "unset";
        sb.append(getType());
        if ( hasSubType() ) {
            sb.append('/');
            sb.append(str.substring(startSubType,  endSubType));
        }
        if ( hasParameters() ) {
            List<Parameter> x = getParameters();
            x.forEach(p->{
                sb.append(';');
                sb.append(p.attribute);
                sb.append('=');
                String val = p.value;
                val = quoteEscape(val);
                sb.append(val);
            });
        }
        return sb.toString();
    }

    public record Parameter(String attribute, String value) { }

    private void parse() {
        // type "/" subtype ["+" suffix] *[";" parameter]

        int idx = 0;
        int j1 = restrictedName(idx);
        if ( j1 == idx )
            // No first character. type is at least one char.
            throw new MimeTypeParseException("Failed to find 'type' at posn "+idx);
        // type
        startType = idx;
        endType = j1;

        if ( idx >= length )
            return;

        // Separator
        idx = j1;
        char ch = charAt(idx);
        if ( ch != '/' )
            throw new MimeTypeParseException("Unexpected character after type field: '"+ch+"'");
        idx++;

        // Subtype
        int j2 = restrictedName(idx);
        if ( j2 == idx )
            // No first character. subtype is at least one char.
            throw new MimeTypeParseException("Failed to find 'subtype' at posn "+idx);

        startSubType = idx;
        endSubType = j2;
        idx = j2;
        if ( idx >= length )
            return;
        startParameters = idx;
        int x = parseParameters(idx, (_, _, _, _)->{});
        if ( x > idx )
            startParameters = idx;
        idx = x ;

        // Strictly unnecessary if "parseParameters" runs to end of string.
        if ( idx != length )
            throw new MimeTypeParseException("Trailing characters: index="+idx);
    }

    private int parseParameters(int idx, ParameterAction handler) {
        // parameters
        // Parses until end of string.
        while(true) {
            if ( idx >= length )
                break;
            // -- Separator for parameters
            char ch = charAt(idx);
            // Separator
            if ( ch == EOF ) {
                // Not separator.
                throw new MimeTypeParseException("Unexpected end of string");
            }
            if ( ch != ';' )
                throw new MimeTypeParseException("Unexpected character: '"+ch+"'");
            idx++;
            if ( idx >= length ) {}
            // -- attribute
            int aEnd = token(idx);
//            if ( aEnd < 0 ) {}
//            if ( aEnd == idx ) {}

            int startAttr = idx;
            int endAttr = aEnd;

            idx = aEnd;
            ch = charAt(idx);
            if ( ch == EOF )
                throw new MimeTypeParseException("No '=' after attribute name");

            // -- =value
            if ( ch != '=' )
                throw new MimeTypeParseException("Unexpected character after attribute name: '"+ch+"'");
            idx++;
            int startValue = idx;
            ch = charAt(idx);
            int vEnd;
            if ( ch == '"' ) {
                vEnd = quotedString(idx);
                if ( vEnd < 0 )
                    throw new MimeTypeParseException("Malformed quoted string");
            } else {
                vEnd = token(idx);
            }
            // We allow this. The strict grammar does not but it seems to occur in the wild sometimes.
            // In struct terms, it can be written attr="".
//            if ( vEnd == idx )
//                throw new MimeTypeParseException("Zero length value");

            int endValue = vEnd;
            idx = vEnd;
            handler.parameter(startAttr, endAttr, startValue, endValue);
        }
        return idx;
    }

    private int restrictedName(int startIdx) {
        int idx = startIdx;
        char ch = charAt(idx);
        if ( ! restrictedNameCharFirst(ch) )
            throw new MimeTypeParseException("Bad char as start of a tytpe or subtype: 'ch'");
        idx++ ;

        while(true) {
            ch = charAt(idx);
            if ( ch == EOF )
                return idx;
            if ( ! restrictedNameChar(ch) )
                return idx;
            idx++;
        }
    }

    //token := 1*<any (US-ASCII) CHAR except SPACE, CTLs, or tspecials>
    private int token(int idx) {
        // not quoted string
        while(true) {
            char ch = charAt(idx);
            if ( ch == EOF )
                break;
            // quoted string
            if ( ! tokenChar(ch) )
                break;
            idx++;
        }
        return idx;
    }

    // The quoted string position return excludes the trailing quotes.
    // RFC 822
    // quoted-string = <"> *(qtext/quoted-pair) <">; Regular qtext or
    //                                             ; quoted chars.
    // quoted-pair =  "\" CHAR -- quote any character.
    //
    // qtext       =  <any CHAR excepting <">,     ; => may be folded
    //                  "\" & CR, and including
    //                  linear-white-space>

    private int quotedString(int startIdx) {
        // RFC 822
        int idx = startIdx;
        char ch = charAt(idx);
        if ( ch != '"' )
            return idx;
        while(true) {
            idx++;
            ch = charAt(idx);
            if ( ch == EOF )
                return -1;
            if ( ch == '"' ) {
                idx++;
                break;
            }
            if ( ch == '\\' ) {
                idx++;
                ch = charAt(idx);
                if ( ch == EOF )
                    // No char
                    return -1;
            }
        }
        return idx;
    }

    private char charAt(int x) {
        if ( x == -1 )
            throw new IndexOutOfBoundsException("Negative index");

        if ( x >= length )
            return EOF;
        return str.charAt(x);
    }

    private static char charAt(String str, int x) {
        if ( x == -1 )
            throw new IndexOutOfBoundsException("Negative index");
        if ( x >= str.length() )
            return EOF;
        return str.charAt(x);
    }

    // Produce a quoted-string if necessary
    private static String quoteEscape(String val) {
        if ( val.isEmpty() )
            // Strict - tokens can't be zero chars. Empty quoted-string is legal though.
            return "\"\"";
        if ( isToken(val) )
            return val;
        val = val.replace("\"",  "\\\"");
        return "\""+val+"\"";
    }

    // \\ Anychar
    private static String unQuoteUnEscape(String val) {
        StringBuilder sb = new StringBuilder();
        for(int i = 1 ; i < val.length()-1; i++ ) {
            char ch = val.charAt(i);
            if ( ch == '\\' )
                continue;
            sb.append(ch);
        }
        return sb.toString();
    }

    private static boolean restrictedNameCharFirst(char ch) {
        return isA2ZN(ch);
    }

    private static boolean restrictedNameChar(char ch) {
        if ( isA2ZN(ch) )
            return true;
        // Specials for RFC 6838
        switch(ch) {
            case '!': case '#': case '$': case '&': case '-': case '^': case  '_':
            case '.': case '+':
                return true;
            default:
        }
        return false;
    }

    private static boolean facetSepChar(char ch) {
        return ch == '.';
    }

    private static boolean suffixSepChar(char ch) {
        return ch == '+';
    }

    private static boolean isToken(String x) {
        // quoted string
        int idx = 0;
        while(true) {
            char ch = charAt(x, idx);
            if ( ch == EOF )
                return true;
            if ( ! tokenChar(ch) )
                return false;
            idx++;
        }
    }


    //    token := 1*<any (US-ASCII) CHAR except SPACE, CTLs,
    //            or tspecials>
    private static boolean tokenChar(char ch) {
        if ( isA2ZN(ch) )
            return true;
        if ( ch > 127 )
            // Not ASCII
            return false;
        if ( ch == ' ')
            return false;
        if ( ch < 0x20 || ch == 0x7F)
            // CTL
            return false;
        if ( isTSpecial(ch) )
            return false;
        return true;
    }

    /** ASCII A-Z */
    private static boolean isA2Z(int ch) {
        return range(ch, 'a', 'z') || range(ch, 'A', 'Z');
    }

    /** ASCII A-Z or 0-9 */
    private static boolean isA2ZN(int ch) {
        return range(ch, 'a', 'z') || range(ch, 'A', 'Z') || range(ch, '0', '9');
    }

    /** Test whether a codepoint is a given range (both ends inclusive)*/
    private static boolean range(int ch, int a, int b) {
        return (ch >= a && ch <= b);
    }

    //  tspecials :=  "(" / ")" / "<" / ">" / "@" /
    //                "," / ";" / ":" / "\" / <">
    //                "/" / "[" / "]" / "?" / "="
    private static boolean isTSpecial(char ch) {
        switch(ch) {
            case '(': case ')': case '<': case '>': case '@':
            case ',': case ';': case ':': case '\\': case '"':
            case '/': case '[': case ']': case '?': case '=':
                return true;
        }
        return false;
    }

}
