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

/** Operations related to parsing IRIs */
/*package*/ class ParseLib {

    private static int CASE_DIFF = 'a'-'A';     // 0x20. Only for ASCII.
    /*
     * Case insensitive "startsWith" for ASCII.
     * Check whether the character and the next character match the expected characters.
     * "chars" array  should be lower case.
     *     scheme = ALPHA *( ALPHA / DIGIT / "+" / "-" / "." )
     */
    /*package*/ static boolean containsAtIgnoreCase(CharSequence string, int x, char[] chars) {
        // Avoid creating any objects.
        int n = string.length();
        if ( x+chars.length-1 >= n )
            return false;
        for ( int i = 0 ; i < chars.length ; i++ ) {
            char ch = string.charAt(x+i);
            char chx = chars[i];
            if ( ch == chx )
                continue;
            // URI scheme names are ASCII.
            if ( Chars3986.range(ch, 'A', 'Z' ) && ( chx - ch == CASE_DIFF ) )
                continue;
            return false;
        }
        return true;
    }

    /** Check whether the character and the next character match the expected characters. */
    public static boolean peekFor(CharSequence string, int x, char x1, char x2) {
        int n = string.length();
        if ( x+1 >= n )
            return false;
        char ch1 = string.charAt(x);
        char ch2 = string.charAt(x+1);
        return ch1 == x1 && ch2 == x2;
    }

    public static char charAt(CharSequence string, int x) {
        if ( x >= string.length() )
            return Chars3986.EOF;
        return string.charAt(x);
    }

    // Copied from jena-base to make this package dependency-free.
    /** Hex digits : upper case **/
    final private static char[] hexDigitsUC = {
        '0' , '1' , '2' , '3' , '4' , '5' , '6' , '7' , '8' ,
        '9' , 'A' , 'B' , 'C' , 'D' , 'E' , 'F' } ;

    /* package */ static void encodeAsHex(StringBuilder buff, char marker, char ch) {
        if ( ch < 256 ) {
            buff.append(marker);
            int lo = ch & 0xF;
            int hi = (ch >> 4) & 0xF;
            buff.append(hexDigitsUC[hi]);
            buff.append(hexDigitsUC[lo]);
            return;
        }
        int n4 = ch & 0xF;
        int n3 = (ch >> 4) & 0xF;
        int n2 = (ch >> 8) & 0xF;
        int n1 = (ch >> 12) & 0xF;
        buff.append(marker);
        buff.append(hexDigitsUC[n1]);
        buff.append(hexDigitsUC[n2]);
        buff.append(marker);
        buff.append(hexDigitsUC[n3]);
        buff.append(hexDigitsUC[n4]);
    }
}
