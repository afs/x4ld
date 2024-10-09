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

package org.seaborne.rfc3986;

import static org.seaborne.rfc3986.Chars3986.EOF;
import static org.seaborne.rfc3986.Chars3986.charAt;
import static org.seaborne.rfc3986.LibParseIRI.caseInsensitivePrefix;

import java.util.function.BiConsumer;
import java.util.regex.Pattern;

public class ParseURN {
    // RFC 8141
    // @formatter:off
    /*
     * namestring    = assigned-name
     *                  [ rq-components ]
     *                  [ "#" f-component ]
     * assigned-name = "urn" ":" NID ":" NSS
     * NID           = (alphanum) 0*30(ldh) (alphanum)
     * ldh           = alphanum / "-"
     * NSS           = pchar *(pchar / "/")
     * rq-components = [ "?+" r-component ]
     *                 [ "?=" q-component ]
     * r-component   = pchar *( pchar / "/" / "?" )
     * q-component   = pchar *( pchar / "/" / "?" )
     * f-component   = fragment
     */
    /*
     * InformalNamespaceName = "urn-" Number
     *  Number                = DigitNonZero 0*Digit
     *  DigitNonZero          = "1"/ "2" / "3" / "4"/ "5"
     *                        / "6" / "7" / "8" / "9"
     *  Digit                 = "0" / DigitNonZero
     */
    /*
     * alphanum, fragment, and pchar from RFC 3986
     *
    // pchar         = unreserved / pct-encoded / sub-delims / ":" / "@"
       alphanum        ALPHA / DIGIT
       fragment    = *( pchar / "/" / "?" )

       pct-encoded   = "%" HEXDIG HEXDIG
       unreserved    = ALPHA / DIGIT / "-" / "." / "_" / "~"
       reserved      = gen-delims / sub-delims
       gen-delims    = ":" / "/" / "?" / "#" / "[" / "]" / "@"
       sub-delims    = "!" / "$" / "&" / "'" / "(" / ")"
                           / "*" / "+" / "," / ";" / "="
    */
    // @formatter:on

    public record AssignedName(String scheme, String NID, String NSS) {}
    public static class URNParseException extends IRIParseException {
        URNParseException(String entity, String msg) { super(entity, msg); }
    }

    // V1 - regex bases
    private static Pattern URN_NAMESPACE = Pattern.compile("^urn:[0-9-a-f]{2,31}:", Pattern.CASE_INSENSITIVE);

    /**
     * Check the scheme and the namespace identifier of an IRI string assumed to be valid RFC 3986 syntax.
     * Return the start of the namespace specific string or -1 if failed to process the string.
     * Call {@code handler} to pass back scheme-specific violations.
     */
    static int analyseURN(String string, BiConsumer<Issue, String> handler) {
        int N = string.length();
        boolean urnScheme = caseInsensitivePrefix(string, "urn:");
        if ( ! urnScheme ) {
            handler.accept(Issue.urn_bad_pattern, "Failed find the URN scheme name");
            return -1;
        }
        // Start of namespace id
        int startNamespace = 4;
        int x = startNamespace;
        // First character, alpha.
        char ch = charAt(string, x);
        if ( ch == EOF ) {
            handler.accept(Issue.urn_bad_nid, "No namespace id");
            return -1;
        }
        if ( ! Chars3986.isAlphaNum(ch) ) {
            handler.accept(Issue.urn_bad_nid, "Namespace id does no start with an alphabetic ASCII character");
            return -1;
        }
        x++;
        char prevChar = EOF;
        while(x < N ) {
            prevChar = ch;
            ch = charAt(string, x);
            if ( ch == ':' ) {
                if ( prevChar == '-' ) {
                    // Can't end in hyphen
                    handler.accept(Issue.urn_bad_nid, "Namespace id end in '-'");
                    return -1;
                }
                break;
            }
            if ( ! isLDH(ch) ) {
                handler.accept(Issue.urn_bad_nid, "Bad character in Namespace id");
                return -1;
            }
            if ( x-startNamespace > 31 ) {
                handler.accept(Issue.urn_bad_nid, "Namespace id more than 32 characters");
                return -1;
            }
            x++;
        }
        int finishNamespace = x;

        if ( ch != ':' ) {
            handler.accept(Issue.urn_bad_nid, "Namespace not terminated by ':'");
            return -1;
        }
        x++;

        if ( finishNamespace-startNamespace < 2 )
            handler.accept(Issue.urn_bad_nid, "Namespace id must be at least 2 characters");

        // RFC 8141 section 5.1
        if ( LibParseIRI.caseInsensitiveRegion(string, startNamespace, "X-") )
            handler.accept(Issue.urn_bad_nid, "Namespace id styarts with 'X-'");

        // RFC 8141 section 5.2 - Informal namespace. "urn-1234"
        if ( LibParseIRI.caseInsensitiveRegion(string, startNamespace, "urn-") ) {
            boolean seenNonZero = false;
            for ( int i = startNamespace+"urn-".length() ; i < finishNamespace ; i++ ) {
                char chx = charAt(string, i);
                if ( !seenNonZero ) {
                    if ( chx == '0' ) {
                        handler.accept(Issue.urn_bad_nid, "Leading zero in an informal namepsace");
                        break;
                    } else
                        seenNonZero = true;
                }
                // Allows leading zeros.
                if ( ! Chars3986.isDigit(chx) ) {
                    handler.accept(Issue.urn_bad_nid, "Bad informal namepsace");
                    break;
                }
            }
        }

        return finishNamespace;
    }

    // LDH = letter-digit-hyphen
    static boolean isLDH(char ch) {
        return Chars3986.isAlphaNum(ch) || ch == '-';
    }

    static
    public void parse(String string) {
        int N = string.length();
        // fast path - correct
        boolean urnScheme = caseInsensitivePrefix(string, "urn:");
        if ( ! urnScheme )
            throw new URNParseException(string, "Does not start 'urn:'");

        // Start of namespace id
        int startNamespace = 4;
        int x = startNamespace;

        // First character, alpha.
        char ch = charAt(string, x);
        if ( ch == EOF )
            throw new URNParseException(string, "No namespace id");

        if ( ! Chars3986.isAlpha(ch) )
            throw new URNParseException(string, "Namespace id does no start with an alphabetic ASCII character");
        x++;

        while(x < N ) {
            ch = charAt(string, x);
            if ( ! Chars3986.isAlphaNum(ch) )
                break;
            if ( x-startNamespace > 32 )
                throw new URNParseException(string, "Namespace id more than 3 characaters");
            x++;
        }

        int finishNamespace = x;
        if ( x-startNamespace < 2 )
            throw new URNParseException(string, "Namepsace id must be at least 2 characters");
        // already done
//        if ( x-startNamespace > 32 )
//            throw new URNParseException(string, "Namespace id more than 3 characters");


        if ( ch != ':' )
            throw new URNParseException(string, "Namespace not termninated by ':'");
        x++;
        if ( x >= N )
            throw new URNParseException(string, "Zero-length namespace specific string");

        // Allow international chars
        int startNSS = x;
        // First character

        ch = charAt(string, x);
        if ( ! Chars3986.isPChar(ch, string, x) )
            throw new URNParseException(string, "First character of NSS is not a pChar");
        x++;

        while(x < N ) {
            ch = charAt(string, x);
            if ( ! Chars3986.isPChar(ch, string, x) && ( ch != '/') )
                break;
            x++;
        }
        int finishNSS = x;


        String scheme = string.substring(0, 3);
        String namespace = string.substring(startNamespace, finishNamespace);
        String nsSpecific = string.substring(startNSS, finishNSS);

        System.out.printf("  %s:%s:%s\n", scheme, namespace, nsSpecific);
    }


    static
    public void parseRegex(String string) {
//      boolean schemeNamepace = URN_NAMESPACE.matcher(string).matches();
//      if ( ! schemeNamepace )
//          throw new URNParseException(string, "Does not match scheme-namespaceid grammar - may not be a URN");


    }

}
