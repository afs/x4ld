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

import static org.junit.Assert.*;

import java.util.function.Consumer;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.seaborne.rfc3986.SystemIRI3986.Compliance;

/**
 * Scheme specific tests
 *
 * @see TestRFC3986 -- for parsing the URI grammar.
 * @see TestRFC3986_Features
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestRFC3986_Scheme {
    @Before public void setup() {
        SystemIRI3986.strictMode("all", Compliance.STRICT);
    }

    @AfterClass public static void reset() {
        SystemIRI3986.strictMode("all", Compliance.STRICT);
    }

    // HTTP scheme specific rules.
    @Test public void parse_http_01()   { badSpecific("http:///file/name.txt"); }

    // HTTP scheme specific rules.
    @Test public void parse_http_02()   { badSpecific("HTTP:///file/name.txt"); }

    // This is treated as legal with path and no authority.
    //@Test public void parse_http_02a()   { badSpecific("http:/file/name.txt"); }

    @Test public void parse_http_03()   { badSpecific("http://users@host/file/name.txt"); }

    @Test public void parse_file_01()   { badSpecific("file:"); }
    @Test public void parse_file_02()   { badSpecific("file:/"); }
    @Test public void parse_file_03()   { badSpecific("file://"); }
    @Test public void parse_file_10()   { badSpecific("file:file/name.txt"); }
    @Test public void parse_file_11()   { badSpecific("file:/file/name.txt"); }
    @Test public void parse_file_12()   { badSpecific("file://file/name.txt"); }

    @Test public void parse_uuid_07()   { badSpecific("urn:uuid:0000"); }

    @Test public void parse_uuid_08()   { badSpecific("uuid:0000-1111"); }

    // ---- bad by scheme.
    @Test public void parse_http_bad_01() { badSpecific("http://user@host:8081/abc/def?qs=ghi#jkl"); }

    //  urn:2char:1char
    // urn:NID:NSS where NID is at least 2 alphas, and at most 32 long
    @Test public void parse_urn_bad_01() { badSpecific("urn:"); }
    @Test public void parse_urn_bad_02() { badSpecific("urn:x:abc"); }

    @Test public void parse_urn_bad_03() { badSpecific("urn:abc:"); }
    // 33 chars
    @Test public void parse_urn_bad_04() { badSpecific("urn:abcdefghij-123456789-123456789-yz:a"); }

    // Bad by URN specific rule for the query components.
    @Test public void parse_urn_bad_05() { badSpecific("urn:local:abc/def?query=foo"); }

    @Test public void parse_urn_uuid_bad_01() {
        badSpecific("urn:uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9?query=foo");
    }

    @Test public void parse_urn_uuid_bad_02() {
        badSpecific("urn:uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9#frag");
    }

    @Test public void parse_urn_uuid_bad_03() {
        // Bad length
        badSpecific("urn:uuid:06e775ac");
    }

    @Test public void parse_urn_uuid_bad_04() {
        // Bad character
        badSpecific("urn:uuid:06e775ac-ZZZZ-11b2-801c-8086f2cc00c9");
    }

    @Test public void parse_uuid_bad_01() {
        badSpecific("uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9?query=foo");
    }

    @Test public void parse_uuid_bad_02() {
        badSpecific("uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9#frag");
    }

    @Test public void parse_uuid_bad_03() {
        badSpecific("uuid:06e775ac-2c38-11b2");
    }

    public static ErrorHandler create(Consumer<String> onError, Consumer<String> onWarning) {
        return ErrorHandlerBase.create(onError, onWarning);
    }

    private static Consumer<String> onEvent = s -> { throw new IRIParseException(s); };
    private static ErrorHandler errHandlerTest = ErrorHandlerBase.create(onEvent, onEvent);

    private void badSpecific(String string) {
        IRI3986 iri = IRI3986.createAny(string);
        assertTrue("Expected a scheme-specific warning or error: '"+string+"'",
                   iri.hasViolations());
    }
}
