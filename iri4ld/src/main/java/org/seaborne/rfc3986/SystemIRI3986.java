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

/** Setup and configuration of the IRI3986 parser package. */
public class SystemIRI3986 {

//    public enum Compliance { STRICT, NOT_STRICT }
//
//    /* package*/ static Compliance Compliance_HTTPx_SCHEME      = Compliance.STRICT;
//    /* package*/ static Compliance Compliance_URN_SCHEME        = Compliance.STRICT;
//    /* package*/ static Compliance Compliance_FILE_SCHEME       = Compliance.STRICT;
//
//    public static void strictMode(String scheme, Compliance compliance) {
//        if ( "all".equals(scheme) ) {
//            Compliance_HTTPx_SCHEME      = compliance;
//            Compliance_URN_SCHEME        = compliance;
//            Compliance_FILE_SCHEME       = compliance;
//            return;
//        }
//
//        switch (scheme) {
//            case "http" :
//                Compliance_HTTPx_SCHEME = compliance;
//                break;
//            case "urn" :
//                Compliance_URN_SCHEME = compliance;
//                break;
//            case "file" :
//                Compliance_FILE_SCHEME = compliance;
//                break;
//        }
//    }
//
//    public static Compliance getStrictMode(String scheme) {
//        switch (scheme) {
//            case "http" :
//                return SystemIRI3986.Compliance_HTTPx_SCHEME;
//            case "urn" :
//                return SystemIRI3986.Compliance_URN_SCHEME;
//            case "file" :
//                return SystemIRI3986.Compliance_FILE_SCHEME;
//            default:
//                return SystemIRI3986.Compliance.NOT_STRICT;
//        }
//    }

    /** System default : throw exception on errors, silent about warnings. */
    private static final ErrorHandler errorHandlerSystemDefault =
            ErrorHandler.create(s -> { throw new IRIParseException(s); }, null);

    /**
     * System error handler.
     * The initial setting is one that throws errors, and ignore warnings.
     */
    private static ErrorHandler errorHandler = errorHandlerSystemDefault;

    public static void setErrorHandler(ErrorHandler errHandler) {
        errorHandler = errHandler;
    }

    public static ErrorHandler getErrorHandler() {
        return errorHandler;
    }
}
