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

import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * Scheme specific tests
 *
 * @see TestRFC3986_Syntax -- for parsing the URI grammar.
 * @see TestRFC3986_Features
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestRFC3986_Scheme {

    // HTTP scheme specific rules.
    @Test public void parse_http_01()   { schemeViolation("http:///file/name.txt"); }

    // HTTP scheme specific rules.
    @Test public void parse_http_02()   { schemeViolation("HTTP:///file/name.txt"); }

    // This is treated as legal with path and no authority.
    //@Test public void parse_http_02a()   { badSpecific("http:/file/name.txt"); }

    @Test public void parse_http_03()   { schemeViolation("http://users@host/file/name.txt"); }

    @Test public void parse_file_01()   { schemeViolation("file:"); }
    @Test public void parse_file_02()   { schemeViolation("file:/"); }
    @Test public void parse_file_03()   { schemeViolation("file://"); }
    @Test public void parse_file_10()   { schemeViolation("file:file/name.txt"); }
    @Test public void parse_file_11()   { schemeViolation("file:/file/name.txt"); }
    @Test public void parse_file_12()   { schemeViolation("file://file/name.txt"); }

    @Test public void parse_uuid_07()   { schemeViolation("urn:uuid:0000"); }

    @Test public void parse_uuid_08()   { schemeViolation("uuid:0000-1111"); }

    // ---- bad by scheme.
    @Test public void parse_http_bad_01() { schemeViolation("http://user@host:8081/abc/def?qs=ghi#jkl"); }

    //  urn:2char:1char
    // urn:NID:NSS where NID is at least 2 alphas, and at most 32 long
    @Test public void parse_urn_bad_01() { schemeViolation("urn:"); }
    @Test public void parse_urn_bad_02() { schemeViolation("urn:x:abc"); }

    @Test public void parse_urn_bad_03() { schemeViolation("urn:abc:"); }
    // 33 chars
    @Test public void parse_urn_bad_04() { schemeViolation("urn:abcdefghij-123456789-123456789-yz:a"); }

    // Bad by URN specific rule for the query components.
    @Test public void parse_urn_bad_05() { schemeViolation("urn:local:abc/def?query=foo"); }

    // URNs and Unicode.
    // Strictly URNs are ASCII but in keeping with internationalization, allow
    // Unicode in the NSS and components but not in the NID

    @Test public void parse_urn_unicode_bad_01() { schemeViolation("urn:αβγ:xyz"); }

    // Not an r- or q- component.
    @Test public void parse_urn_components_bad_01() { schemeViolation("urn:ns:xyz?notAComponent"); }

    // --- urn:uuid:

    @Test public void parse_urn_uuid_bad_01() {
        schemeViolation("urn:uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9?query=foo");
    }

    @Test public void parse_urn_uuid_bad_02() {
        schemeViolation("urn:uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9#frag");
    }

    @Test public void parse_urn_uuid_bad_03() {
        // Bad length
        schemeViolation("urn:uuid:06e775ac");
    }

    @Test public void parse_urn_uuid_bad_04() {
        // Bad character
        schemeViolation("urn:uuid:06e775ac-ZZZZ-11b2-801c-8086f2cc00c9");
    }

    @Test public void parse_uuid_bad_01() {
        schemeViolation("uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9?query=foo");
    }

    @Test public void parse_uuid_bad_02() {
        schemeViolation("uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9#frag");
    }

    @Test public void parse_uuid_bad_03() {
        schemeViolation("uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9?query#frag");
    }

    @Test public void parse_uuid_bad_04() {
        schemeViolation("uuid:06e775ac-2c38-11b2");
    }

    private static String testUUID = "0fa0c738-a789-11eb-b471-abdc7e01c508";

    // RFC 8141 allows query and fragment in urn: (limited character set).
    // It even permits retrospectively applying to older schemes,
    // However, the r- (?+"), p- ("?=") or f- (#) components do not play a part in URN equivalence.

    // RFC 4122 (uuid namespace definition) does not mention r- p- or f- component
    @Test public void parse_uuid_bad_8141_01() {
        schemeViolation("urn:uuid:" + testUUID + "#frag");
    }

    // RFC 8141 allows query string must be ?=<one+ char> or ?+<one+ char>
    @Test public void parse_uuid_bad_8141_03() {
        schemeViolation("urn:uuid:" + testUUID + "?+chars");
    }

    @Test public void parse_uuid_bad_8141_04() {
        schemeViolation("urn:uuid:" + testUUID + "?=chars");
    }

    @Test public void parse_uuid_bad_8141_05() {
        schemeViolation("urn:uuid:" + testUUID + "?+chars#frag");
    }

    @Test public void parse_uuid_bad_8141_06() {
        schemeViolation("urn:uuid:" + testUUID + "?=chars#frag");
    }

    // Always bad.
    @Test public void parse_uuid_bad_8141_10() {
        schemeViolation("urn:uuid:" + testUUID + "?abc");
    }

    // Always bad.
    @Test public void parse_uuid_bad_8141_11() {
        schemeViolation("urn:uuid:" + testUUID + "?");
    }

    @Test public void parse_uuid_bad_8141_12() {
        schemeViolation("urn:uuid:" + testUUID + "?+");
    }

    @Test public void parse_uuid_bad_8141_13() {
        schemeViolation("urn:uuid:" + testUUID + "?=");
    }

    @Test public void parse_uuid_bad_8141_14() {
        // Not ASCII
        schemeViolation("urn:uuid:" + testUUID + "#αβγ");
    }

    @Test public void parse_uuid_bad_8141_15() {
        schemeViolation("urn:uuid:" + testUUID + "#");
    }

    @Test public void parse_urn_oid_1() {
        schemeViolation("urn:oid:Z");
    }

    @Test public void parse_urn_oid_2() {
        // It's "urn:oid:..."
        schemeViolation("oid:1.2.3");
    }

    private void schemeViolation(String string) {
        IRI3986 iri = IRI3986.createAny(string);
        assertTrue("Expected a scheme-specific warning or error: '"+string+"'", iri.hasViolations());
    }
}
