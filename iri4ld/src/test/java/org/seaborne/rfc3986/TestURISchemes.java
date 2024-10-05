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

import static org.seaborne.rfc3986.LibTestURI.good;
import static org.seaborne.rfc3986.LibTestURI.schemeViolation;


import org.junit.Test;

/** Test IRIs for scheme-specific validation rules. */
public class TestURISchemes {

    // http:, https:
    // Including good, bad IPv4 addresses
    @Test public void parse_http_empty_host_1() { schemeViolation("http:///abc",  URIScheme.HTTP, Issue.http_empty_host); }
    @Test public void parse_http_empty_host_2() { schemeViolation("https:///abc", URIScheme.HTTPS, Issue.http_empty_host); }
// //   @Test public void parse_addr_v4_1() { bad("http://10.11.12.1300/abc"); }
    @Test public void parse_http_usersInfo_1()   { schemeViolation("http://user@host/file/name.txt", URIScheme.HTTP, Issue.http_userinfo); }
    @Test public void parse_http_usersInfo_2()   { schemeViolation("http://password:user@host/file/name.txt", URIScheme.HTTP, Issue.http_userinfo, Issue.http_password); }

    // file:
    // Expects an absolute file path.
    @Test public void parse_file_00()   { good("file:///path"); }
    @Test public void parse_file_01()   { schemeViolation("file:", URIScheme.FILE, Issue.file_relative_path); }
    @Test public void parse_file_02()   { schemeViolation("file:/", URIScheme.FILE, Issue.file_relative_path, Issue.file_bad_form); }
    // Empty host, no path.
    @Test public void parse_file_03()   { schemeViolation("file://", URIScheme.FILE, Issue.file_relative_path); }
    @Test public void parse_file_10()   { schemeViolation("file:file/name.txt", URIScheme.FILE, Issue.file_relative_path); }
    @Test public void parse_file_11()   { schemeViolation("file:/file/name.txt", URIScheme.FILE, Issue.file_bad_form); }
    // Host name.
    @Test public void parse_file_12()   { schemeViolation("file://file/name.txt", URIScheme.FILE, Issue.file_bad_form); }

    // did:
    // See also TestparseDID

    @Test public void parse_did_01() { good("did:method:specific"); }
    @Test public void parse_did_02() { schemeViolation("did::", URIScheme.DID, Issue.did_bad_syntax); }

//    //example:
//
//    // urn:
//
//    @Test public void parse_urn_bad_01() { schemeViolation("urn:"); }
//    @Test public void parse_urn_bad_02() { schemeViolation("urn:x:abc"); }
//
//    @Test public void parse_urn_bad_03() { schemeViolation("urn:abc:"); }
//    // 33 chars
//    @Test public void parse_urn_bad_04() { schemeViolation("urn:abcdefghij-123456789-123456789-yz:a"); }
//
//    // Bad by URN specific rule for the query components.
//    @Test public void parse_urn_bad_05() { schemeViolation("urn:local:abc/def?query=foo"); }
//
//
//    // urn:uuid:
//
//    @Test public void parse_uuid_07()   { schemeViolation("urn:uuid:0000"); }
//
//    @Test public void parse_uuid_08()   { schemeViolation("uuid:0000-1111"); }
//
//    @Test public void parse_urn_uuid_bad_01() {
//        schemeViolation("urn:UUID:06e775ac-2c38-11b2-801c-8086f2cc00c9?query=foo", 1, URIScheme.URN_UUID);
//    }
//
//    @Test public void parse_urn_uuid_ok_02() {
//        schemeViolation("urn:uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9#frag", 0, URIScheme.URN_UUID);
//    }
//
//    @Test public void parse_urn_uuid_bad_03() {
//        // Bad length
//        schemeViolation("urn:uuid:06e775ac");
//    }
//
//    @Test public void parse_urn_uuid_bad_04() {
//        // Bad character
//        schemeViolation("urn:uuid:06e775ac-ZZZZ-11b2-801c-8086f2cc00c9");
//    }
//
//    @Test public void parse_urn_uuid_bad_05() {
//        // Bad character
//        schemeViolation("urn:uuid:06e775ac-ZZZZ-11b2-801c-8086f2cc00c9?plain");
//    }
//
//    // DUPLICATES
//    // --- urn:uuid:
//
//
//    private static String testUUID = "0fa0c738-a789-11eb-b471-abdc7e01c508";
//
//    // RFC 8141 allows query and fragment in urn: (limited character set).
//    // It even permits retrospectively applying to older schemes,
//    // However, the r- (?+"), p- ("?=") or f- (#) components do not play a part in URN equivalence.
//
//    // RFC 4122 (uuid namespace definition) does not mention r- p- or f- component
//    @Test public void parse_uuid_bad_8141_01() {
//        schemeViolation("urn:uuid:" + testUUID + "#frag");
//    }
//
//    // RFC 8141 allows query string must be ?=<one+ char> or ?+<one+ char>
//    @Test public void parse_uuid_bad_8141_03() {
//        schemeViolation("urn:uuid:" + testUUID + "?+chars");
//    }
//
//    @Test public void parse_uuid_bad_8141_04() {
//        schemeViolation("urn:uuid:" + testUUID + "?=chars");
//    }
//
//    @Test public void parse_uuid_bad_8141_05() {
//        schemeViolation("urn:uuid:" + testUUID + "?+chars#frag");
//    }
//
//    @Test public void parse_uuid_bad_8141_06() {
//        schemeViolation("urn:uuid:" + testUUID + "?=chars#frag");
//    }
//
//
//
//    // uuid:
//    // No URN components
//
//    @Test public void parse_uuid_bad_01() {
//        schemeViolation("uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9?query=foo");
//    }
//
//    @Test public void parse_uuid_bad_02() {
//        schemeViolation("uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9#frag");
//    }
//
//    @Test public void parse_uuid_bad_03() {
//        schemeViolation("uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9?query#frag");
//    }
//
//    @Test public void parse_uuid_bad_04() {
//        schemeViolation("uuid:06e775ac-2c38-11b2");
//    }
//
//
//    // uuid:
//    private static final String testUUID = "4ca12587-3598-43e0-a286-299b16e2d270";
//    // No URN components.
//    @Test public void parse_uuid_bad_01()   { schemeViolation("uuid:"+testUUID+"?=abc", 1, URIScheme.UUID); }
//    @Test public void parse_uuid_bad_02()   { v("uuid:"+testUUID+"#frag"); }
//
//
//
    // urn:oid:
    // More in TestParseOID
    @Test public void parse_urn_oid_1() { good("urn:oid:2.3.4"); }
    @Test public void parse_urn_oid_2() { schemeViolation("urn:oid:Z", URIScheme.URN_OID, Issue.oid_bad_syntax); }

    // oid:
    @Test public void parse_oid_1() { schemeViolation("oid:2.3.4", URIScheme.OID, Issue.oid_scheme_not_registered); }
    @Test public void parse_oid_2() { schemeViolation("oid:Z", URIScheme.OID, Issue.oid_bad_syntax, Issue.oid_scheme_not_registered); }


//
//
//    @Test public void parse_urn_oid_1() {
//        schemeViolation("urn:oid:Z");
//    }
//
//    @Test public void parse_urn_oid_2() {
//        // It's "urn:oid:..."
//        schemeViolation("oid:1.2.3");
//    }
//
//
//    // urn:example:
//    // None.
//
//    // This is treated as legal with path and no authority.
//    //@Test public void parse_http_02a()   { badSpecific("http:/file/name.txt"); }
//
//
//
//    //  urn:2char:1char
//    // urn:NID:NSS where NID is at least 2 alphas, and at most 32 long
//
//    // URNs and Unicode.
//    // Strictly URNs are ASCII but in keeping with internationalization, allow
//    // Unicode in the NSS and components but not in the NID
//
//    @Test public void parse_urn_unicode_bad_01() { schemeViolation("urn:αβγ:xyz"); }
//
//    // Not an r- or q- component.
//    @Test public void parse_urn_components_bad_01() { schemeViolation("urn:ns:xyz?notAComponent"); }
//



    // -------


}
