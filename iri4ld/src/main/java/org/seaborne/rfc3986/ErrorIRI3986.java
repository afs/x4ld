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

import static org.seaborne.rfc3986.SystemIRI3986.getErrorHandler;

/**
 * Internal functions for the IRI3986 parser for errors and warnings.
 * This makes having a common easier.
 */
/*package*/ class ErrorIRI3986 {

    //  Parse error must be exceptions.
    static void parseError(CharSequence source, int posn, String s) {
        parseException(formatMsg(source, posn, s));
    }

    static void parseError(CharSequence source, String s) {
        parseError(source, -1, s);
    }

    static void parseWarning(CharSequence source, int posn, String s) {
        warning(formatMsg(source, posn, s));
    }

    static void parseWarning(CharSequence source, String s) {
        parseWarning(source, -1, s);
    }

    static void schemeError(CharSequence source, char[] scheme, String s) {
        schemeError(source, String.copyValueOf(scheme), s);
    }

    static void schemeError(CharSequence source, String scheme, String s) {
        error(formatMsg(source, -1, scheme+" URI scheme -- "+s));
    }

    static void schemeWarning(CharSequence source, String scheme, String s) {
        warning(formatMsg(source, -1, scheme+" URI scheme -- "+s));
    }

    private static String formatMsg(CharSequence source, int posn, String s) {
        StringBuilder sb = new StringBuilder(s.length()+20);
        if ( source != null ) {
            sb.append("<");
            sb.append(source);
            sb.append("> : ");
        }
        if ( posn >= 0 ) {
            sb.append("[Posn "+posn+"] ");
        }
        sb.append(s);
        return sb.toString();
    }

    private static void parseException(String s) {
        throw new IRIParseException(s);
    }

    private static void error(String s) {
        getErrorHandler().error(s);
    }

    private static void warning(String s) {
        getErrorHandler().warning(s);
    }
}
