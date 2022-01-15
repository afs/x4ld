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

public class ParseDID {

    private static char EOF = Chars3986.EOF ;

    /*
    ==== "did:" rules
    https://www.w3.org/TR/did-core/
    @context https://www.w3.org/ns/did/v1
    did:
    did                = "did:" method-name ":" method-specific-id
    method-name        = 1*method-char
    method-char        = %x61-7A / DIGIT     [x61 is 'a', x7A is 'z']
    method-specific-id = *( ":" *idchar ) 1*idchar
    idchar             = ALPHA / DIGIT / "." / "-" / "_" / pct-encoded
    pct-encoded        = "%" HEXDIG HEXDIG

    At least one char for method-name.
    At least one char for method-specific-id; end char not ":"

    April 2021: At-risk : add: empty  method-specific-id

*/
    static
    public void parse(String string, boolean allowPercentEncoding) {
        if ( ! string.startsWith("did:") )
            error(string, "Not a DID");

        //int end = length;
        int end = string.length();
        int p = "did:".length();

        // XXX if string is empty or single space.
        // XXX if string starts "."
        // XXX if string ends ":"

        int q = methodName(string, p);
        if ( q <= p+1)
            error(string, "No method name");
        p = q ;

        q = methodSpecificId(string, p);
        if ( q <= p )
            error(string, "No method-specific-id");

        if ( q != end )
            error(string, "Trailing characters after method-specific-id");
    }

    private static int methodName(String str, int p) {
        //int end = length;
        int end = str.length();
        int start = p;
        while (p < end) {
            char ch = charAt(str, p);
            if ( ch == EOF ) // Internal error.
                return p;
            if ( ch == ':' ) {
                p++;
                return p;
            }
            if ( ! methodChar(ch) )
                error(str, "Bad character: '"+Character.toString(ch)+"'");
            p++;
        }
        if ( p != end && start+1 == p )
            error(str, "Zero length methodName");
        // Still may be zero,
        return p;
    }

    private static int methodSpecificId(String str, int p) {
        //int end = length;
        int end = str.length();
        int start = p;
        while (p < end) {
            char ch = charAt(str, p);
            if ( ch == EOF ) {}
            if ( ch == ':' ) { p++; continue; }

            if ( ! idchar(ch, str, p) ) {
                error(str, "Bad character: '"+Character.toString(ch)+"'");
            }
            p++;
        }
        if ( p != end )
            error(str, "Traing charactser after method specific id");
        if ( start == p )
            error(str, "Zero length method specific id");
        char chLast = charAt(str, p-1);
        if ( chLast == ':' )
            error(str, "Final method specifc id character is a ':'");
        return p;

    }


    private static boolean methodChar(char ch) {
        return (ch >= 'a' && ch <= 'z');
    }

    private static boolean idchar(char ch, String str, int p) {
        return (ch >= 'a' && ch <= 'z') ||
               (ch >= 'A' && ch <= 'Z') ||
               (ch >= '0' && ch <= '9') ||
               ch == '.' || ch == '-' || ch == '_' ||
               Chars3986.isPctEncoded(ch, str, p);
    }

    static class DIDParseException extends IRIParseException {
        DIDParseException(String msg) {super(msg);}
    }

    private static void error(String didString, String msg) {
        throw new DIDParseException(msg);
    }

    /** String.charAt except with an EOF character, not an exception. */
    private static char charAt(String str, int x) {
        if ( x >= str.length() )
            return EOF;
        return str.charAt(x);
    }


//    // <letter> [ [ <ldh-str> ] <let-dig> ]
//    // Modified to allow start digit.
//    // Ends at "." or end of string.
//    // so it does not need to backoff for "-" in the last letter.
//
//    // End of label happens in two ways - find a "." or end of string.
//    private int label(int p) {
//        int end = length;
//        int start = p;
//        boolean charIsHyphen = false;
//        while (p < end) {
//            char ch = charAt(p);
//            //System.out.println("Char = "+Character.toString(ch));
//            if ( ch == '.' ) {
//                if ( charIsHyphen )
//                    // From last round.
//                    throw new DNSParseException("Bad last character of subdomain: '"+Character.toString(ch)+"'");
//                p++;
//                break;
//            }
//            charIsHyphen = ( ch == HYPHEN );
//
//            if ( ! letter_digit(ch) && ! charIsHyphen )
//                throw new DNSParseException("Bad character: '"+Character.toString(ch)+"'");
//            if ( p == start && charIsHyphen )
//                throw new DNSParseException("Bad first character of subdomain: '"+Character.toString(ch)+"'");
//            p++;
//        }
//        if ( p != end && start+1 == p )
//            throw new DNSParseException("Zero length subdomain");
//        return p;
//    }
//
//    private static boolean let_dig_hyp(char ch) { return letter_digit_hyphen(ch); }

}
