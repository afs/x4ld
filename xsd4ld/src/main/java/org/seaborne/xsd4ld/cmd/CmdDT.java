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

package org.seaborne.xsd4ld.cmd;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.seaborne.xsd4ld.XSD;
import org.seaborne.xsd4ld.XSDDatatype;
import org.seaborne.xsd4ld.XSDTypeRegistry;

public class CmdDT {

    static { XSD.init(); }


    public static void main(String... args) {
        if ( args.length == 0 ) {
            System.err.println("No string");
            System.exit(1);
        }

        // Regex to break up the string.
        // ... single quotes
        Pattern pattern1 = Pattern.compile("'((?:[^']|\\')*)'\\s*\\^\\^\\s*(\\S*)");
        // ... double quotes
        Pattern pattern2 = Pattern.compile("\"((?:[^\"]|\\\")*)\\s*\"\\^\\^\\s*(\\S*)");

        for ( String s : args ) {
            Matcher m = pattern1.matcher(s);
            if ( ! m.matches() )
                m = pattern2.matcher(s);
            if ( !m.matches() ) {
                System.out.println("Invalid input: "+s);
                continue;
            }

            String lex = m.group(1);
            lex = unescape(lex,  '\\', false);
            String dt = m.group(2);

            String shortName = dt.startsWith("xsd:") ? dt.substring("xsd:".length()) : dt ;
            XSDDatatype xsdDT = XSDTypeRegistry.getType(shortName);
            if ( xsdDT == null )
                xsdDT = XSDTypeRegistry.getTypeByURI(dt);
            if ( xsdDT == null ) {
                System.err.printf("Do not rcognized datatype: %s\n", dt);
                continue;
            }

            String printStr = xsdDT.getURIName().replaceAll("^"+XSD.getURI(), "xsd:");
            if ( xsdDT.isValid(lex) ) {
                System.out.printf("Valid: %s ^^ %s\n", lex, printStr);
            } else {
                System.out.printf("*****: %s ^^ %s\n    Regex= %s\n", lex, printStr, xsdDT.getRegexStr());
            }
        }
    }

    public static String unescape(String s, char escape, boolean pointCodeOnly) {
        int i = s.indexOf(escape) ;

        if ( i == -1 )
            return s ;

        // Dump the initial part straight into the string buffer
        StringBuilder sb = new StringBuilder(s.substring(0,i)) ;

        for ( ; i < s.length() ; i++ )
        {
            char ch = s.charAt(i) ;

            if ( ch != escape )
            {
                sb.append(ch) ;
                continue ;
            }

            // Escape
            if ( i >= s.length()-1 )
                throw new CmdException("Illegal escape at end of string") ;
            char ch2 = s.charAt(i+1) ;
            i = i + 1 ;

            // \\u and \\U
            if ( ch2 == 'u' )
            {
                if ( i+4 >= s.length() )
                    throw new CmdException("\\u escape too short") ;
                int x4 = hexStringToInt(s, i+1, 4) ;
                sb.append((char)x4) ;
                // Jump 1 2 3 4 -- already skipped \ and u
                i = i+4 ;
                continue ;
            }
            if ( ch2 == 'U' )
            {
                if ( i+8 >= s.length() )
                    throw new CmdException("\\U escape too short") ;
                int ch8 = hexStringToInt(s, i+1, 8) ;
                if ( Character.charCount(ch8) == 1 )
                    sb.append((char)ch8);
                else {
                    // See also TokenerText.insertCodepoint and TokenerText.readUnicodeEscape
                    // Convert to UTF-16. Note that the rest of any system this is used
                    // in must also respect codepoints and surrogate pairs.
                    if ( !Character.isDefined(ch8) && !Character.isSupplementaryCodePoint(ch8) )
                        throw new CmdException(String.format("Illegal codepoint: 0x%04X", ch8));
                    if ( ch8 > Character.MAX_CODE_POINT )
                        throw new CmdException(String.format("Illegal code point in \\U sequence value: 0x%08X", ch8));
                    char[] chars = Character.toChars(ch8);
                    sb.append(chars);
                }
                // Jump 1 2 3 4 5 6 7 8 -- already skipped \ and u
                i = i+8 ;
                continue ;
            }

            // Are we doing just point code escapes?
            // If so, \X-anything else is legal as a literal "\" and "X"

            if ( pointCodeOnly )
            {
                sb.append('\\') ;
                sb.append(ch2) ;
                continue ;
            }

            // Not just codepoints.  Must be a legal escape.
            char ch3 = 0 ;
            switch (ch2)
            {
                case 'n': ch3 = '\n' ;  break ;
                case 't': ch3 = '\t' ;  break ;
                case 'r': ch3 = '\r' ;  break ;
                case 'b': ch3 = '\b' ;  break ;
                case 'f': ch3 = '\f' ;  break ;
                case '\'': ch3 = '\'' ; break ;
                case '\"': ch3 = '\"' ; break ;
                case '\\': ch3 = '\\' ; break ;
                default:
                    throw new CmdException("Unknown escape: \\"+ch2) ;
            }
            sb.append(ch3) ;
        }
        return sb.toString() ;
    }

    /** Hex string to value */
    public static int hexStringToInt(String s, int i, int len) {
        int x = 0;
        for ( int j = i ; j < i + len ; j++ ) {
            char ch = s.charAt(j);
            int k = 0;
            switch (ch) {
                case '0': k = 0 ; break ;
                case '1': k = 1 ; break ;
                case '2': k = 2 ; break ;
                case '3': k = 3 ; break ;
                case '4': k = 4 ; break ;
                case '5': k = 5 ; break ;
                case '6': k = 6 ; break ;
                case '7': k = 7 ; break ;
                case '8': k = 8 ; break ;
                case '9': k = 9 ; break ;
                case 'A': case 'a': k = 10 ; break ;
                case 'B': case 'b': k = 11 ; break ;
                case 'C': case 'c': k = 12 ; break ;
                case 'D': case 'd': k = 13 ; break ;
                case 'E': case 'e': k = 14 ; break ;
                case 'F': case 'f': k = 15 ; break ;
                default:
                    throw new CmdException("Illegal hex escape: "+ch) ;
            }
            x = (x<<4)+k ;
        }
        return x ;
    }


}
