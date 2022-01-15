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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ParseDNS {
    /* RFC 1034:
     *
     * <domain> ::= <subdomain> | " "
     *
     * <subdomain> ::= <label> | <subdomain> "." <label>
     *
     * <label> ::= <letter> [ [ <ldh-str> ] <let-dig> ]
     * becomes
     * <label> ::= <let-dig> (<let-dig-hyp>)* <let-dig>
     *
     * <ldh-str> ::= <let-dig-hyp> | <let-dig-hyp> <ldh-str>
     *
     * <let-dig-hyp> ::= <let-dig> | "-"
     *
     * <let-dig> ::= <letter> | <digit>
     *
     * <letter> ::= any one of the 52 alphabetic characters A through Z in
     *              upper case and a through z in lower case
     * becomes
     * <letter> ::= above or UcsChar
     *
     * <digit> ::= any one of the ten digits 0 through 9
     *
     * RFC 1123: Remove leading alpha restriction.
     *   See <label>
     *   Allow %-encoded.
     *   Internationalization
     */

    /* RFC952:
     *
     * <hname> ::= <name>*["."<name>]
     * <name> ::=  <let>[*[<let-or-digit-or-hyphen>]<let-or-digit>]
     */

    /* RFCs 1034 and 1035, and as updated and clarified by RFCs 1123 and 2181.
     *
     *    https://tools.ietf.org/html/rfc1034#section-3.1
     *    https://tools.ietf.org/html/rfc1034#section-3.5
     *    Wildcards https://tools.ietf.org/html/rfc4592
     *    Punycode - https://tools.ietf.org/html/rfc3492
     *    IDNA: https://tools.ietf.org/html/rfc3490
     *    RFC3987:
     *    ihost          = IP-literal / IPv4address / ireg-name
     *    ireg-name      = *( iunreserved / pct-encoded / sub-delims )
     */


    // RFC 1034:
    //        Note that while upper and lower case letters are allowed in domain
    //        names, no significance is attached to the case.  That is, two names with
    //        the same spelling but different case are to be treated as if identical.
    //
    //        The labels must follow the rules for ARPANET host names.  They must
    //        start with a letter, end with a letter or digit, and have as interior
    //        characters only letters, digits, and hyphen.  There are also some
    //        restrictions on the length.  Labels must be 63 characters or less.
    //
    //        RFC 1123: https://tools.ietf.org/html/rfc1123#section-2
    //        Remove leading alpha restriction.
    //
    //        Host software MUST handle host names of up to 63 characters and
    //        SHOULD handle host names of up to 255 characters.
    //        "#.#.#.#"


    // allowPercentEncoding

    private final String string;
    private final int length;
    private static char HYPHEN = '-';

    private ParseDNS(String string) {
        this.string = string;
        this.length = string.length();
    }

    static class DNSParseException extends IRIParseException {
        DNSParseException(String msg) {super(msg);}
    }

    private void error(String msg) {
        throw new DNSParseException(msg);
    }

    public static String parse(String string) {
        Objects.requireNonNull(string);
        new ParseDNS(string).parse(false);
        return string;
    }

    /** String.charAt except with an EOF character, not an exception. */
    private char charAt(int x) {
        if ( x >= length )
            return Chars3986.EOF;
        return string.charAt(x);
    }

    private void parse(boolean allowPercentEncoding) {
        int end = length;
        int p = 0 ;

        if ( length == 0 )
            error("Empty string");

        if ( length == 1 && string.equals(" ") )
            // " " is the root but we don't allow it (not used as such in applications).
            error("Empty string");

        if ( length == 1 && string.equals(".") )
            // Must have one or more labels.
            error("No subdomains.");


        List<Integer> dots = new ArrayList<>(4);

        // XXX if string starts "."
        // XXX if string ends "."

        while (p < end) {
            p = label(p);
            if ( p < 0 )
                // Error
                error("No label");
            if ( p == length )
                break;
            // Separator dots
            dots.add(p-1);
        }
        //System.out.println("Dots: "+dots);
    }

    // <label> ::= <letter> [ [ <ldh-str> ] <let-dig> ]
    // becomes
    // <label> ::= <let-dig> (<let-dig-hyp>)* <let-dig>
    // Modified to allow start digit.
    // Ends at "." or end of string.
    // so it does not need to backoff for "-" in the last letter.

    // End of label happens in two ways - find a "." or end of string.
    private int label(int p) {
        int end = length;
        int start = p;
        boolean charIsHyphen = false;
        while (p < end) {
            char ch = charAt(p);
            if ( ch == '.' ) {
                if ( charIsHyphen )
                    // From last round.
                    error("Bad last character of subdomain: '-'");
                p++;
                break;
            }
            charIsHyphen = ( ch == HYPHEN );

            if ( ! letter_digit(ch, p) && ! charIsHyphen )
                error("Bad character: '"+Character.toString(ch)+"'");
            if ( p == start && charIsHyphen )
                error("Bad first character of subdomain: '"+Character.toString(ch)+"'");
            p++;
        }
        if ( charIsHyphen )
            // From final round.
            error("Bad last character of subdomain: '-'");
        if ( p != end && start+1 == p )
            error("Zero length subdomain");
        return p;
    }

    // ----

//    private static boolean letter_digit(char ch) {
//        return letter(ch) || digit(ch);
//    }
//
//    private static boolean letter_digit_hyphen(char ch) {
//        return letter(ch) || digit(ch) || ch == '-';
//    }
//
//    private static boolean letter(char ch) {
//        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
//    }
//
//    private boolean letter_digit_hyphen(char ch, int x) {
//        return letter_digit(ch, x) || ch == '-';
//    }

    // <let-dig>
    private boolean letter_digit(char ch, int x) {
        return i_letter(ch, x) || digit(ch);
    }

    private boolean i_letter(char ch, int x) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || isUcsChar(ch) || Chars3986.isPctEncoded(ch, string, x);
    }

    // DRY: IRI3986

//    ucschar        = %xA0-D7FF / %xF900-FDCF / %xFDF0-FFEF
//                   / %x10000-1FFFD / %x20000-2FFFD / %x30000-3FFFD
//                   / %x40000-4FFFD / %x50000-5FFFD / %x60000-6FFFD
//                   / %x70000-7FFFD / %x80000-8FFFD / %x90000-9FFFD
//                   / %xA0000-AFFFD / %xB0000-BFFFD / %xC0000-CFFFD
//                   / %xD0000-DFFFD / %xE1000-EFFFD

    // Surrogates are "hi-lo" : DC000-DFFF and D800-DFFF
    // We assume the java string is valid and surrogates are correctly in high-low pairs.

    private static boolean isUcsChar(char ch) {
        return Chars3986.range(ch, 0xA0, 0xD7FF)  || Chars3986.range(ch, 0xF900, 0xFDCF)  || Chars3986.range(ch, 0xFDF0, 0xFFEF)
                // Allow surrogates.
                || Character.isSurrogate(ch);
            // Java is 16 bits chars.
//            || range(ch, 0x10000, 0x1FFFD) || range(ch, 0x20000, 0x2FFFD) || range(ch, 0x30000, 0x3FFFD)
//            || range(ch, 0x40000, 0x4FFFD) || range(ch, 0x50000, 0x5FFFD) || range(ch, 0x60000, 0x6FFFD)
//            || range(ch, 0x70000, 0x7FFFD) || range(ch, 0x80000, 0x8FFFD) || range(ch, 0x90000, 0x9FFFD)
//            || range(ch, 0xA0000, 0xAFFFD) || range(ch, 0xB0000, 0xBFFFD) || range(ch, 0xC0000, 0xCFFFD)
//            || range(ch, 0xD0000, 0xDFFFD) || range(ch, 0xE1000, 0xEFFFD)
    }



    private static boolean digit(char ch) {
        return (ch >= '0' && ch <= '9');
    }

//    private static void let_dig_hyp() {}
//    private static void ldh_str() {}
}
