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

import java.util.Objects;
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

    private static int AnyIChar = Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CHARACTER_CLASS | Pattern.UNICODE_CASE;
    private static int AnyASCIIChar = Pattern.CASE_INSENSITIVE;
    // Non-collecting (?: ... ) is a non-binding regex group
    private static Pattern pattern = Pattern.compile("^(?:\\?\\+[0-9a-z]+)?(?:\\?=[0-9a-z]+)?(?:#[0-9a-z]*)?$", AnyIChar);
    // Collecting - the group is the inner component part without the marker ?+,?=, #
    private static Pattern patternComponents = Pattern.compile("^(?:\\?\\+([0-9a-z]+))?(?:\\?=([0-9a-z]+))?(?:#([0-9a-z]*))?$", AnyIChar);

    public static URNComponents parseURNcomponents(String componentsString) {
        return parseURNcomponents(componentsString, 0);
    }

    static URNComponents parseURNcomponentsRegex(String componentsString) {
        Matcher m = patternComponents.matcher(componentsString);
        if ( ! m.matches() )
            return null;
        String r = m.group(1);
        String q = m.group(2);
        String f = m.group(3);
        return createValidURNComponents(r, q, f);
    }

    private static String trimQuery(String r) {
        return null;
    }

    public static final char CH_QMARK    = '?' ;
    public static final char CH_HASH     = '#' ;
    public static final char CH_EQUALS   = '=' ;
    public static final char CH_PLUS     = '+' ;

    public static final String R_STR     = "?+";
    public static final String Q_STR     = "?=";
    public static final String F_STR     = "#";
    static URNComponents parseURNComponents(IRI3986 iri) {
        return parseURNComponents(iri.query(), iri.fragment());
    }

    // Parse by URI components.
    static URNComponents parseURNComponents(String rqString, String fString) {
        if ( rqString == null || fString == null )
            return null;
        if ( rqString == null ) {
            String f = adjustLeading(fString, F_STR);
            return createValidURNComponents(null, null, f);
        }
        // Parse the r- and q- components
        URNComponents rqComponents = parseRQ(rqString, 0, rqString.length());
        String rComponent = rqComponents.rComponent();
        String qComponent = rqComponents.qComponent();
        String fComponent = adjustLeading(fString, F_STR);
        // Return all three.
        return createValidURNComponents(rComponent, qComponent, fComponent);
    }

    // Trim leading chars
    private static String adjustLeading(String str, String prefix) {
        if ( str == null )
            return null;
        if ( str.startsWith(prefix) )
            return str.substring(prefix.length());
        return str;
    }

    // Parse the string between start (inclusive) and finish (exclusive) as
    // rq-components = [ "?+" r-component ] [ "?=" q-component ]
    private static URNComponents parseRQ(String rqString, int start, int finish) {
        Objects.requireNonNull(rqString);
        if ( start < 0 )
            throw new IllegalArgumentException("Start index must be positive");
        if ( finish < start )
            throw new IllegalArgumentException("Finish index must be greater than start");

        int strLength = rqString.length();
        if ( start > strLength )
            throw new IllegalArgumentException("Start index("+start+") out of range("+strLength+")");
        if ( finish > strLength )
            throw new IllegalArgumentException("End index("+start+") out of range("+strLength+")");

        int limit = finish;
        int x = start;
        // Find the indexes
        int rCompStart = -1;    // Index after the '+' in ?+
        int rCompFinish = -1;
        int qCompStart = -1;    // Index after the '=' in ?=
        int qCompFinish = -1;

        if ( rqString.startsWith(R_STR, x) ) {
            x += R_STR.length();
            rCompStart = x;
        }

        int idx = rqString.indexOf(Q_STR, x);
        if ( idx < 0 ) {
            // No q-component
            if ( rCompStart < 0 ) {
                // Nothing.
                return null;
            }
            // Remainder of string.
            String rComp = rqString.substring(rCompStart, limit);
            return createValidURNComponents(rComp, null, null);
        }
        // q-component.
        if ( rCompStart < 0 ) {
            // No r-component.
            String qComp = rqString.substring(idx+2, limit);
            return createValidURNComponents(null, qComp , null);
        }

        String rComp = rqString.substring(rCompStart, idx);
        String qComp = rqString.substring(idx+2, limit);
        return createValidURNComponents(rComp, qComp , null);
    }

    private static URNComponents createValidURNComponents(String rComp, String qComp, String fComp) {
        if ( rComp != null && rComp.isEmpty() )
            return null;
        if ( qComp != null && qComp.isEmpty() )
            return null;
        return new URNComponents(rComp, qComp, fComp);
    }

    // Parse all three components from the tail of a string.
    public static URNComponents parseURNcomponents(String componentsString, int startIdx) {
        if ( componentsString == null )
            return null;
        int N = componentsString.length();
        int x = startIdx;
        if ( x < 0 )
            throw new IllegalArgumentException("Start index must be positive");
        if ( x > N )
            throw new IllegalArgumentException("Start index greater than string length");

        int fStart = componentsString.indexOf(CH_HASH, x);
        if ( fStart >= 0 ) {
            if ( fStart == x+1 ) {
                // Check for illegal ?#frag
                return null;
            }
            String fComp = componentsString.substring(fStart+1);
            URNComponents rq = parseRQ(componentsString, startIdx, fStart);
            if ( rq == null )
                return null;
            return createValidURNComponents(rq.rComponent(), rq.qComponent(), fComp);
        }

        URNComponents rq = parseRQ(componentsString, startIdx, N);
        return rq;
    }
}
