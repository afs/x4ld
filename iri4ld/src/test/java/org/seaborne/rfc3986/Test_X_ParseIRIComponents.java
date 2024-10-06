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

import org.junit.Test;

/**
 * Test parsed components.
 * <p>
 * Detailed testing IPv6 parsing is in {@link TestParseIPv6Address}.
 */
public class Test_X_ParseIRIComponents {
    @Test public void components_http_01() {
        testComponents("http://user@host:8081/abc/def?qs=ghi#jkl", "http", "user@host:8081", "/abc/def", "qs=ghi", "jkl");
    }

    @Test public void components_http_02() {
        testComponents("http://host/abc/def?qs=ghi#jkl", "http", "host", "/abc/def", "qs=ghi", "jkl");
    }

    @Test public void components_http_03() {
        testComponents("http://host/abc/def#jkl", "http", "host", "/abc/def", null, "jkl");
    }

    @Test public void components_http_04() {
        testComponents("http://host/abc/def?q=", "http", "host", "/abc/def", "q=", null);
    }

    @Test public void components_http_05() {
        testComponents("http://host/abc/def#", "http", "host", "/abc/def", null, "");
    }

    @Test public void components_http_06() {
        testComponents("http://host", "http", "host", "", null, null);
    }

    @Test public void components_http_07() {
        testComponents("http://host#frag", "http", "host", "", null, "frag");
    }

    @Test public void components_http_08() {
        testComponents("http://host?q=s", "http", "host", "", "q=s", null);
    }

    @Test public void components_http_09() {
        testComponents("http://host?q=s#frag", "http", "host", "", "q=s", "frag");
    }

    @Test public void components_some_01() {
        testComponents("//host:8888/abc/def?qs=ghi#jkl", null, "host:8888", "/abc/def", "qs=ghi", "jkl");
    }

    @Test public void components_some_02() {
        testComponents("http:///abc/def?qs=ghi#jkl", "http", "", "/abc/def", "qs=ghi", "jkl");
    }

    @Test public void components_some_03() {
        testComponents("/abc/def?qs=ghi#jkl", null, null, "/abc/def", "qs=ghi", "jkl");
    }

    @Test public void components_some_04() {
        testComponents("abc/def?qs=ghi#jkl", null, null, "abc/def", "qs=ghi", "jkl");
    }

    @Test public void components_some_05() {
        testComponents("http:abc/def?qs=ghi#jkl", "http", null, "abc/def", "qs=ghi", "jkl");
    }

    @Test public void components_some_06() {
        testComponents("abc/def", null, null, "abc/def", null, null);
    }

    @Test public void components_some_07() {
        testComponents("abc/def?qs=ghi#jkl", null, null, "abc/def", "qs=ghi", "jkl");
    }

    @Test public void components_some_08() {
        testComponents("http:", "http", null, "", null, null);
    }

    // Important cases.
    @Test public void components_cases_01() {
        testComponents("", null, null, "", null, null);
    }

    @Test public void components_21() {
        testComponents("#foo", null, null, "", null, "foo");
    }

    @Test public void components_22() {
        testComponents(".", null, null, ".", null , null);
    }

    @Test public void components_23() {
        testComponents("..", null, null, "..", null , null);
    }

    @Test public void components_24() {
        testComponents("/", null, null, "/", null , null);
    }

    @Test public void components_25() {
        testComponents("/..", null, null, "/..", null , null);
    }

    // URN
    @Test public void components_urn_1() {
        testComponents("urn:NID:NSS", "urn", null, "NID:NSS", null, null);
    }

    @Test public void components_urn_2() {
        testComponents("urn:local:abc/def?+more", "urn", null, "local:abc/def", "+more", null);
    }

    @Test public void components_urn_3() {
        testComponents("urn:local:abc/def?=more", "urn", null, "local:abc/def", "=more", null);
    }

    @Test public void components_urn_4() {
        testComponents("urn:local:abc/def#frag", "urn", null, "local:abc/def", null, "frag");
    }

    @Test public void components_urn_5() {
        testComponents("urn:local:abc/def#frag", "urn", null, "local:abc/def", null, "frag");
    }

    @Test public void components_urn_uuid_1() {
        testComponents("urn:uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9",
                "urn", null, "uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9", null, null);
    }

    @Test public void components_uuid_1() {
        testComponents("uuid:06e775ac-2c38-11b2-801c-8086f2cc00c9",
                "uuid", null, "06e775ac-2c38-11b2-801c-8086f2cc00c9", null, null);
    }

    @Test public void components_file_1() {
        testComponents("file:///path/file.txt", "file", "", "/path/file.txt", null, null);
    }

    @Test public void components_file_2() {
        testComponents("file:/path/file.txt", "file", null, "/path/file.txt", null, null);
    }

    @Test public void components_ftp_1() {
        testComponents("ftp://user@host:3333/abc/def?qs=ghi#jkl", "ftp", "user@host:3333", "/abc/def", "qs=ghi", "jkl");
    }

    @Test public void components_mailto_1() {
        testComponents("mailto:support@example.com", "mailto", null, "support@example.com", null, null);
    }

    private void testComponents(String string, String scheme, String authority, String path, String query, String fragment) {
        IRI3986 iri = RFC3986.create(string);
        assertEquals("scheme",      scheme,     iri.scheme());
        assertEquals("authority",   authority,  iri.authority());
        assertEquals("path",        path,       iri.path());
        assertEquals("query",       query,      iri.query());
        assertEquals("fragment",    fragment,   iri.fragment());
    }
}
