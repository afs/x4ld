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

public record Violation (String iriStr, URIScheme scheme, Issue issue, String message) {
    @Override
    public String toString() {
        return iriStr + " : " + scheme.getPrefix() + " " + message;
    }
}

// In case we go back to Java11...
//class Violation {
//    private final String iriStr;
//    private final URIScheme scheme;
//    private final Issue issue;
//    private final String message;
//
//    Violation2(String iriStr, URIScheme scheme, Issue violation, String message) {
//        this.iriStr = iriStr;
//        this.scheme = scheme;
//        this.violation = null;
//        this.message = message;
//    }
//
//    @Override
//    public String toString() {
//        return iriStr + " : " + message;
//    }
//
//    public String getIriStr()     { return iriStr; }
//    public URIScheme getScheme()  { return scheme; }
//    public Issue getIssue()       { return violation; }
//    public String getMessage()    { return message; }
//}
