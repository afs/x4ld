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

    // == http:, https:
    // Including good, bad IPv4 addresses
    @Test public void parse_http_empty_host_1() { schemeViolation("http:///abc",  URIScheme.HTTP, Issue.http_empty_host); }
    @Test public void parse_http_empty_host_2() { schemeViolation("https:///abc", URIScheme.HTTPS, Issue.http_empty_host); }
// //   @Test public void parse_addr_v4_1() { bad("http://10.11.12.1300/abc"); }
    @Test public void parse_http_usersInfo_1()   { schemeViolation("http://user@host/file/name.txt", URIScheme.HTTP, Issue.http_userinfo); }
    @Test public void parse_http_usersInfo_2()   { schemeViolation("http://password:user@host/file/name.txt", URIScheme.HTTP, Issue.http_userinfo, Issue.http_password); }

    // == file:
    // Expects an absolute file path.
    @Test public void parse_file_00()   { good("file:///path"); }
    @Test public void parse_file_01()   { schemeViolation("file:", URIScheme.FILE, Issue.file_relative_path); }
    @Test public void parse_file_02()   { schemeViolation("file:/", URIScheme.FILE, Issue.file_bad_form); }
    // Empty host, no path.
    @Test public void parse_file_03()   { schemeViolation("file://", URIScheme.FILE, Issue.file_bad_form); }
    @Test public void parse_file_10()   { schemeViolation("file:file/name.txt", URIScheme.FILE, Issue.file_relative_path); }
    @Test public void parse_file_11()   { schemeViolation("file:/file/name.txt", URIScheme.FILE, Issue.file_bad_form); }
    // Host name.
    @Test public void parse_file_12()   { schemeViolation("file://file/name.txt", URIScheme.FILE, Issue.file_bad_form); }

    // == did:
    // More in TestparseDID

    @Test public void parse_did_01() { good("did:method:specific"); }
    @Test public void parse_did_02() { schemeViolation("did::", URIScheme.DID, Issue.did_bad_syntax); }

    //== example: No violations
    //== urn:example: No violations

    // == urn:
    // Not a know namespace

    @Test public void parse_urn_good_01() { good("urn:ns:nss"); }
    @Test public void parse_urn_good_02() { good("urn:123:nss"); }
    @Test public void parse_urn_good_03() { good("urn:1-2:nss"); }
    @Test public void parse_urn_good_04() { good("urn:X-local:nss"); }  // OK by URN syntax, forbidden by RFC 8141 section 5.1
    @Test public void parse_urn_good_05() { good("urn:urn-abc:nss"); }  // OK by URN syntax, forbidden by RFC 8141 section 5.1
    @Test public void parse_urn_good_06() { good("urn:urn-7:nss"); }
    @Test public void parse_urn_good_10() { good("urn:nid:a"); }

    @Test public void parse_urn_bad_ns_01() { schemeViolation("urn:-12:nss", URIScheme.URN, Issue.urn_bad_nid); }
    @Test public void parse_urn_bad_ns_02() { schemeViolation("urn:and-:nss", URIScheme.URN, Issue.urn_bad_nid); }

    @Test public void parse_urn_bad_01() { schemeViolation("urn:", URIScheme.URN, Issue.urn_bad_nid); }
    @Test public void parse_urn_bad_02() { schemeViolation("urn:x:abc", URIScheme.URN, Issue.urn_bad_nid); }
    @Test public void parse_urn_bad_03() { schemeViolation("urn:abc:", URIScheme.URN, Issue.urn_bad_nss); }
    // 32 char NID
    @Test public void parse_urn_good_nid_length() { good("urn:12345678901234567890123456789012:a"); }
    // 33 char NID
    @Test public void parse_urn_bad_04() { schemeViolation("urn:abcdefghij-123456789-123456789-yz:a", URIScheme.URN, Issue.urn_bad_nid); }

    // Bad by URN specific rule for the query components.
    @Test public void parse_urn_bad_05() { schemeViolation("urn:local:abc/def?query=foo", URIScheme.URN, Issue.urn_bad_components); }
    // Two f-components = two fragments
    @Test public void parse_urn_bad_06() { LibTestURI.badSyntax("urn:local:abc/def#f1#f2"); }
    @Test public void parse_urn_bad_07() { schemeViolation("urn:αβγ:abc", URIScheme.URN, Issue.urn_bad_nid); }

    // == urn:uuid:

    @Test public void parse_urn_uuid_good_01() { good("urn:uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9"); }
    @Test public void parse_urn_uuid_good_02() { good("urn:uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9?+r?=q#frag"); }

    @Test public void parse_urn_uuid_01() { schemeViolation("urn:uuid:0000", URIScheme.URN_UUID, Issue.uuid_bad_pattern); }
    @Test public void parse_urn_uuid_02() { schemeViolation("urn:UUID:06e775ac-2c38-11b2-801c-8086f2cc00c9", null, Issue.uuid_not_lowercase); }

    @Test public void parse_urn_uuid_03() {
        schemeViolation("urn:UUID:06e775ac-2c38-11b2-801c-8086f2cc00c9?query=foo", null, Issue.urn_bad_components, Issue.uuid_not_lowercase);
    }

    @Test public void parse_urn_uuid_04() {
        schemeViolation("urn:uuid:06e775ac-2c38-11b2-ZZZZ-8086f2cc00c9?query=foo", URIScheme.URN_UUID, Issue.uuid_bad_pattern, Issue.urn_bad_components);
    }

    // == uuid:

    @Test public void parse_uuid_01() { schemeViolation("uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9", URIScheme.UUID, Issue.uuid_scheme_not_registered); }
    @Test public void parse_uuid_02() { schemeViolation("UUID:06e775ac-2c38-11b2-801c-8086f2cc00c9", null, Issue.iri_scheme_name_is_not_lowercase,  Issue.uuid_scheme_not_registered); }

    @Test public void parse_uuid_bad_01() {
        schemeViolation("uuid:0000-1111", URIScheme.UUID, Issue.uuid_scheme_not_registered, Issue.uuid_bad_pattern);
    }
    @Test public void parse_uuid_bad_02() {
        schemeViolation("UUID:0000-1111", null, Issue.iri_scheme_name_is_not_lowercase, Issue.uuid_scheme_not_registered, Issue.uuid_bad_pattern);
    }

    // No URN components in UUID
    @Test public void parse_uuid_bad_03() {
        schemeViolation("uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9?+r", URIScheme.UUID, Issue.uuid_scheme_not_registered, Issue.uuid_has_query);
    }

    @Test public void parse_uuid_bad_04() {
        schemeViolation("uuid:06e775AC-2c38-11b2-801c-8086f2cc00c9?+r", URIScheme.UUID, Issue.uuid_scheme_not_registered, Issue.uuid_has_query, Issue.uuid_not_lowercase);
    }

    // == urn:oid:
    // More in TestParseOID
    @Test public void parse_urn_oid_1() { good("urn:oid:2.3.4"); }
    @Test public void parse_urn_oid_2() { schemeViolation("urn:oid:Z", URIScheme.URN_OID, Issue.oid_bad_syntax); }

    // == oid:
    @Test public void parse_oid_1() { schemeViolation("oid:2.3.4", URIScheme.OID, Issue.oid_scheme_not_registered); }
    @Test public void parse_oid_2() { schemeViolation("oid:Z", URIScheme.OID, Issue.oid_bad_syntax, Issue.oid_scheme_not_registered); }
}
