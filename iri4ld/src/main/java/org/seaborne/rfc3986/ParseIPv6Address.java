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

import static org.seaborne.rfc3986.Chars3986.charAt;

/**
<pre>
    IP-literal    = "[" ( IPv6address / IPvFuture  ) "]"

    IPvFuture     = "v" 1*HEXDIG "." 1*( unreserved / sub-delims / ":" )

    IPv6address   =                            6( h16 ":" ) ls32
                  /                       "::" 5( h16 ":" ) ls32
                  / [               h16 ] "::" 4( h16 ":" ) ls32
                  / [ *1( h16 ":" ) h16 ] "::" 3( h16 ":" ) ls32
                  / [ *2( h16 ":" ) h16 ] "::" 2( h16 ":" ) ls32
                  / [ *3( h16 ":" ) h16 ] "::"    h16 ":"   ls32
                  / [ *4( h16 ":" ) h16 ] "::"              ls32
                  / [ *5( h16 ":" ) h16 ] "::"              h16
                  / [ *6( h16 ":" ) h16 ] "::"

    h16           = 1*4HEXDIG
    ls32          = ( h16 ":" h16 ) / IPv4address
</pre>
    HEXDIG is '0' to '9 , 'A' to 'F' together with lower case, (RFC3986 - the normalized form is uppercase).
 */

public class ParseIPv6Address {
    // We parse an IPv6 by
    //   look for repeated "(h16 ':')",
    //   look for another ":",
    //   look for repeated (h16 ':')
    //   look for a IPv4 address or another h16
    //   check the whole char sequence was parsed
    //   check the numbers of h16 units does not exceed the grammar restrictions.

    // IPv6address   =                            6( h16 ":" ) ls32
    //               /                       "::" 5( h16 ":" ) ls32
    //               / [               h16 ] "::" 4( h16 ":" ) ls32
    //               / [ *1( h16 ":" ) h16 ] "::" 3( h16 ":" ) ls32
    //               / [ *2( h16 ":" ) h16 ] "::" 2( h16 ":" ) ls32
    //               / [ *3( h16 ":" ) h16 ] "::"    h16 ":"   ls32
    //               / [ *4( h16 ":" ) h16 ] "::"              ls32
    //               / [ *5( h16 ":" ) h16 ] "::"              h16
    //               / [ *6( h16 ":" ) h16 ] "::"
    // h16           = 1*4HEXDIG
    // ls32          = ( h16 ":" h16 ) / IPv4address

    /** Check an IPv6 address (including any delimiting []) */
    public static void checkIPv6(CharSequence string) {
        checkIPv6(string, 0, string.length());
    }

    public static void checkIPv6(CharSequence string, int start, int end) {
        int length = string.length();
        if ( start < 0 || end < 0 || end > length )
            throw new IllegalArgumentException();
        if ( length == 0 || start >= end )
            throw new IRIParseException("Empty IPv6 address");
        parseIPv6(string, start, end);
    }

    private static int parseIPv6(CharSequence string, int start, int end) {
        if ( charAt(string, start) != '[' || charAt(string, end-1) != ']' )
            throw new IRIParseException("IPv6 (or later) address not properly delimited");
        // end must be > start+1 by the above and checkIPv6 so no risk of missing here.
        if ( charAt(string, start+1) == 'v' ) {
            // IPvFuture  = "v" 1*HEXDIG "." 1*( unreserved / sub-delims / ":" )
            return parseIPFuture(string, start+2, end-1);
        }
        return parseIPv6Sub(string, start+1, end-1);
    }

    private static int parseIPFuture(CharSequence string, int start, int end) {
        int p = start;
        if ( p >= end )
            throw new IRIParseException("Short IPFuture");
        char ch = string.charAt(p);
        if ( ! Chars3986.isHexDigit(ch) )
            throw new IRIParseException("IPFuture: no version hexdigit");
        p++;
        ch = string.charAt(p);
        if ( ch != '.' )
            throw new IRIParseException("IPFuture: no dot after version hexdigit");
        p++;
        // One or more.
        while (p < end) {
            ch = string.charAt(p);
            if ( ch == ']' )
                break;
            if ( ! Chars3986.unreserved(ch) && !Chars3986.subDelims(ch) && ch != ':' )
                break;
            p++;
        }
        if ( p != end )
            // Only one ']' at index end.
            throw new IRIParseException("IPFuture: extra ']'");
        return p;
    }

    private static int parseIPv6Sub(CharSequence string, int start, int end) {
        // start-end Without "[...]";
        int p = start;

        // Before the ::
        int h16c1 = -1;
        int h16c2 = -1;
        int h16c = 0;

        //starting ::
        boolean b = ParseLib.peekFor(string, p, ':',  ':');
        if ( b ) {
            h16c1 = h16c;
            h16c = 0 ;
            p += 2;
        }

        // Move forward over h16:
        for (;;) {
            int x = ipv6_h16(string, p, end);
            if ( x == p )
                break;
            if ( x >= end )
                break;
            h16c++;
            char ch = charAt(string, x);
            if ( ch == ':' ) { // "::"
                //System.out.printf("h16 %d\n", h16c);
                h16c1 = h16c;
                h16c = 0 ;
                x++;
            }
            p = x;
        }
        if ( h16c1 >= 0 )
            // After ::
            h16c2 = h16c;
        else
            h16c1 = h16c;

        //h16c2 == -1 => Didn't see ::
        //System.out.printf("(%d, %d)\n", h16c1, h16c2);

        // Lookahead
        boolean IPv4 = false;
        for ( int i = 0 ; i < 4 ; i++ ) {
            int z = p  + i ;
            if ( z >= end )
                // End.
                break;
            char ch = charAt(string, z);
            if ( Chars3986.range(ch, 'a', 'f') || Chars3986.range(ch, 'A', 'F') )
                break;
            if ( ch == '.' ) {
                IPv4 = true;
                break;
            }
            // Unsure yet - loop
        }
        if ( IPv4 ) {
            // Seen "NNN."

            // ":" Validity rule.
            if ( h16c2 == -1 ) {
                // h16c1 must be 6
                if ( h16c1 != 6 )
                    throw new IRIParseException("Malformed IPv6 address with IPv4 part [case 1]");
            } else {
                // h16c1+h16c2 <= 4
                if ( h16c1+h16c2 > 4 )
                    throw new IRIParseException("Malformed IPv6 address with IPv4 part [case 2]");
            }
            int x = ipv4(string, p, end);
            p = x ;
            if ( p != end )
                throw new IRIParseException("Bad end of IPv4 address");
        } else {
            // ":" Validity rule.
            if ( h16c2 == -1 ) {
                // h16c1 must be 7
                if ( h16c1 != 7 )
                    throw new IRIParseException("Malformed IPv6 address [case 1]");
            } else {
                // h16c1+h16c2 <= 5
                // or h16c1 <= 6, and h16c2 = 0
                if ( h16c2 == 0 ) {
                    if ( h16c1 > 6 )
                        throw new IRIParseException("Malformed IPv6 address [case 2]");
                }
                else if ( h16c1+h16c2 > 5 )
                    throw new IRIParseException("Malformed IPv6 address [case 3]");
            }
            int x = ipv6_hex4(string, p, end);
            p = x;
            if ( p != end )
                throw new IRIParseException("Bad end of IPv6 address");
        }

        return p;
    }

    // (h16 ":")*
    // Returns index of just after the ":"
    // Does not accept h16 , no ":"
    private static int ipv6_h16(CharSequence input, int start, int end) {
        int p = start;
        int x = ipv6_hex4(input, p, end);
        if ( x < 0 )
            throw new IRIParseException("hex4 error at "+p);
        if ( x == p )
            // No progress.
            return p;
        if ( x >= end )
            // No ":"
            return p;
        char ch1 = input.charAt(x);
        if ( ch1 != ':' )
            return p;
        x++;
        // New "start".
        p = x;
        return p;
    }

    /** h16 - 1 to 4 hex digits.
     * Return character position after the digits or the start position if no hex digits seen.
     * That is, it may make no progress so in effect it is lookign for 0 to 4 hex digits.
     */
    private static int ipv6_hex4(CharSequence string, int start, int end) {
        int p = start;
        for (int i = 0 ; i < 4 ; i++ ) {
            if ( p+i >= end )
                return p+i;
            char ch = charAt(string, p+i);
            if ( ! Chars3986.isHexDigit(ch) )
                return p+i;
        }
        return p+4;
    }

    /** Match exactly an IPv4 address. */
    private static int ipv4(CharSequence string, int start, int end) {
        return ParseIPv4Address.ipv4(string, start, end);
    }
}
