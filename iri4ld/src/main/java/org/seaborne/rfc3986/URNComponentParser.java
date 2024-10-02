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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parse URN components.
 *
 * <pre>
 * [ "?+" r-component ] [ "?=" q-component ] [ "#" f-component ]
 * </pre>
 */
public class URNComponentParser {

    static class URNComponentException extends RuntimeException {
        URNComponentException(String msg) { super(msg); }
        @Override public URNComponentException fillInStackTrace() { return this; }
    }

    private int AnyIChar = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS | Pattern.UNICODE_CASE;
    // Non-collecting
    private static Pattern pattern = Pattern.compile("^(?:\\?\\+[0-9a-z]+)?(?:\\?=[0-9a-z]+)?(?:#[0-9a-z]*)?$", Pattern.CASE_INSENSITIVE);
    // Collecting
    private static Pattern patternComponents = Pattern.compile("^(\\?\\+[0-9a-z]+)?(\\?=[0-9a-z]+)?(#[0-9a-z]*)?$", Pattern.CASE_INSENSITIVE);

    public static URNComponents parseURNcomponents(String componentsString) {
        return parseURNcomponents(componentsString, 0);
    }

    static URNComponents parseURNcomponentsRegex(String componentsString) {
        Matcher m = patternComponents.matcher(componentsString);
        if ( ! m.matches() ) {
            System.out.println("No match: "+componentsString);
            return null;
        }
        String r = m.group(1);
        String q = m.group(2);
        String f = m.group(3);
        return new URNComponents(r, q, f);
    }

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

    static URNComponents parseURNcomponents(String componentsString, int startIdx) {
        int N = componentsString.length();
        int x = startIdx;
        // Find the indexes
        int rCompStart = -1;    // Index of the '+' in ?+
        int rCompFinish = -1;
        int qCompStart = -1;
        int qCompFinish = -1;
        int fCompStart = -1;
        int fCompFinish = -1;

        // May, or may not, start with "?"
        // IRI components strings do not. The 'query' production is chars after the '?'.

        if ( x < N && componentsString.charAt(x) == '?' ) {
            x++;
        }

        if ( x >= N ) {
            // zero length string.
            throw new URNComponentException("Zero-length URN component string");
        }

        char ch = componentsString.charAt(x);
        if ( ch == '+' ) {
            rCompStart = x+1 ;
            x++;
            // At least one char in the component tested later.
        }

        if ( rCompStart == -1 ) {
            char ch2 = componentsString.charAt(x);
            if ( ch2 == '=' ) {
                qCompStart = x+1 ;
                x++;
            }
        } else {
            // q-component ?
            int idx = componentsString.indexOf("?=", x);
            if ( idx > 0 ) {
                x = idx+2;
                qCompStart = x;
                if ( rCompStart >= 0 )
                    rCompFinish = idx;
                // At least one char
            }
        }

        int fStart = componentsString.indexOf("#", x);
        if ( fStart < 0 ) {
            // No f-component.
            if ( qCompStart >= 0 )
                qCompFinish = N;
            else if ( rCompStart >= 0 )
                rCompFinish = N;
        } else {
            fCompStart = fStart+1;
            fCompFinish = N;
            if ( qCompStart >= 0 )
                qCompFinish = fStart;
            else if ( rCompStart >= 0 )
                rCompFinish = N;
        }
        if ( rCompStart > 0 ) {
            if ( rCompFinish - rCompStart <= 1 ) {
                throw new URNComponentException("r-component must be one or more characters");
            }

        }
        if ( qCompStart > 0 ) {
            if ( qCompFinish - qCompStart <= 1 ) {
                throw new URNComponentException("q-component must be one or more characters");
            }
        }

        String r = rCompStart < 0 ? null : componentsString.substring(rCompStart, rCompFinish);
        String q = qCompStart < 0 ? null : componentsString.substring(qCompStart, qCompFinish);
        String f = fCompStart < 0 ? null : componentsString.substring(fCompStart);
        if ( r == null && q == null && r == null )
            return null;
        return new URNComponents(r, q, f);
    }
}
