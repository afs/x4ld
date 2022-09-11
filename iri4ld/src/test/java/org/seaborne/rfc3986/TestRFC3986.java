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

import java.util.Locale;

import org.apache.jena.iri.IRI;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.seaborne.rfc3986.SystemIRI3986.Compliance;


/**
 * General parsing of URIs, not scheme specific rules.
 * @see TestRFC3986_Scheme
 * @see TestRFC3986_Features
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestRFC3986 {
    // Detailed testing IPv6 parsing is in {@link TestParseIPv6Address}
    // Assumes full authority parsing and not scheme-specific checks.

    @Before public void setup() {
        SystemIRI3986.strictMode("all", Compliance.STRICT);
    }

    @AfterClass public static void reset() {
        SystemIRI3986.strictMode("all", Compliance.STRICT);
    }

    // ---- Compare to jena-iri
    @Test public void parse_00() { good("http://host"); }

    @Test public void parse_01() { good("http://host:8081/abc/def?qs=ghi#jkl"); }

    @Test public void parse_02() { good("http://[::1]:8080/abc/def?qs=ghi#jkl"); }

    // %XX in host added at RFC 3986.
    @Test public void parse_03() { goodNoIRICheck("http://ab%AAdef/xyzβ/abc"); }

    @Test public void parse_04() { good("/abcdef"); }

    @Test public void parse_05() { good("/ab%FFdef"); }

    // Uppercase preferred.
    @Test public void parse_06() { goodNoIRICheck("/ab%ffdef"); }

    @Test public void parse_07() { good("http://host/abcdef?qs=foo#frag"); }

    @Test public void parse_08() { good(""); }

    @Test public void parse_09() { good("."); }

    @Test public void parse_10() { good(".."); }

    @Test public void parse_11() { good("//host:8081/abc/def?qs=ghi#jkl"); }

    @Test public void parse_12() { goodNoIRICheck("a+.-9://h/"); }

    // No path.

    @Test public void parse_13() { good("http://host"); }

    @Test public void parse_14() { good("http://host#frag"); }

    @Test public void parse_15() { good("http://host?query"); }

    // : in first segment in path.
    @Test public void parse_16() { good("http://host/a:b/"); }

    @Test public void parse_17() { good("/a:b/"); }

    @Test public void parse_18() { good("/z/a:b"); }

    // Characters / and ? in trailer
    @Test public void parse_19() { good("http://host/ab?query=abc?def"); }
    @Test public void parse_20() { good("http://host/ab#abc?def"); }
    @Test public void parse_21() { good("http://host/path?q=abc/def#abc/def"); }
    @Test public void parse_22() { good("http://host/path?q=abc/def#abc?def"); }

    @Test public void parse_http_04()   { good("nothttp://users@host/file/name.txt"); }

    @Test public void parse_http_05()   { good("nothttp://users@/file/name.txt"); }

    @Test public void parse_file_01() { good("file:///file/name.txt"); }

    // This is legal by RFC 8089 (jena-iri, based on the original RFC 1738, fails this with missing authority).
    @Test public void parse_file_03() { goodNoIRICheck("file:/file/name.txt"); }

    @Test public void parse_urn_01() { good("urn:x-local:abc/def"); }

    // rq-components = [ "?+" r-component ]
    //                 [ "?=" q-component ]

    @Test public void parse_urn_02()        { good("urn:x-local:abc/def?+more"); }

    @Test public void parse_urn_03()        { good("urn:x-local:abc/def?=123"); }

    @Test public void parse_urn_04()        { good("urn:x-local:abc/def?+resolve?=123#frag"); }

    @Test public void parse_urn_05()        { good("urn:abc0:def"); }

    private static String testUUID = "0fa0c738-a789-11eb-b471-abdc7e01c508";

    @Test public void parse_uuid_01()   { good("uuid:"+testUUID); }

    @Test public void parse_uuid_02()   { good("uuid:"+(testUUID.toUpperCase(Locale.ROOT))); }

    @Test public void parse_uuid_03()   { goodNoIRICheck("UUID:"+testUUID); }

    @Test public void parse_uuid_04()   { good("urn:uuid:"+testUUID); }

    @Test public void parse_uuid_05()   { good("urn:uuid:"+(testUUID.toUpperCase(Locale.ROOT))); }

    @Test public void parse_uuid_06()   { goodNoIRICheck("URN:UUID:"+testUUID); }

    @Test public void parse_ftp_01()    { good("ftp://user@host:3333/abc/def?qs=ghi#jkl"); }

    @Test public void parse_ftp_02()    { good("ftp://[::1]/abc/def?qs=ghi#jkl"); }

    // ---- bad

    // Leading ':'
    @Test public void bad_scheme_1() { bad(":segment"); }

    // Bad scheme
    @Test public void bad_scheme_2() { bad("://host/xyz"); }

    // Bad scheme
    @Test public void bad_scheme_3() { bad("1://host/xyz"); }

    // Bad scheme
    @Test public void bad_scheme_4() { bad("a~b://host/xyz"); }

    // Bad scheme
    @Test public void bad_scheme_5() { bad("aβ://host/xyz"); }

    // Bad scheme
    @Test public void bad_scheme_6() { bad("_:xyz"); }

    // Bad scheme
    @Test public void bad_scheme_7() { bad("a_b:xyz"); }

    // Space!
    @Test public void bad_chars_1() { bad("http://abcdef:80/xyz /abc"); }

    // colons
    @Test public void bad_host_1() { bad("http://abcdef:80:/xyz"); }

    // Bad IPv6
    @Test public void bad_ipv6_1() { bad("http://[::80/xyz"); }

    // Bad IPv6
    @Test public void bad_ipv6_2() { bad("http://host]/xyz"); }

    // Bad IPv6
    @Test public void bad_ipv6_3() { bad("http://[]/xyz"); }

    // Multiple @
    @Test public void bad_authority_1() { bad("ftp://abc@def@host/abc"); }

    // Multiple colon in authority
    @Test public void bad_authority_2() { bad("http://abc:def:80/abc"); }

    // Bad %-encoding.
    @Test public void bad_percent_1() { bad("/abc%ZZdef"); }

    @Test public void bad_percent_2() { bad("http://abc%ZZdef/"); }

    // Bad %-encoded
    @Test public void bad_percent_3() { bad("http://example/xyz%"); }

    // Bad %-encoded
    @Test public void bad_percent_4() { bad("http://example/xyz%A"); }

    // Bad %-encoded
    @Test public void bad_percent_5() { bad("http://example/xyz%A?"); }

    // [] not allowed.
    @Test public void bad_frag_1() { bad("http://eg.com/test.txt#xpointer(/unit[5])"); }
    @Test public void equals_01()           {
        IRI3986 iri1 = IRI3986.create("http://example/");
        IRI3986 iri2 = IRI3986.create("http://example/");
        assertTrue(iri1.equals(iri2));
        assertEquals(iri1.hashCode(), iri2.hashCode());
    }

    @Test public void equals_02()           {
        IRI3986 iri1 = IRI3986.create("http://example/.");
        IRI3986 iri2 = iri1.normalize();
        assertFalse(iri1.equals(iri2));
    }

    // str tested in good()
    @Test public void str_01()           {
        IRI3986 iri1 = IRI3986.create("http://example/.");
        IRI3986 iri2 = iri1.normalize();
        assertNotEquals(iri1.str(), iri2.str());
    }

    private void good(String string) {
        RFC3986.check(string);
        IRI3986 iri = RFC3986.create(string);
        if ( true ) {
            IRI iri1 = JenaIRI.iriFactory().create(string);
            if ( iri1.hasViolation(true) ) {
                iri1.violations(true).forEachRemaining(v-> System.err.println("IRI = "+string + " :: "+v.getLongMessage()));
                fail("Violations "+string);
            }
        }
        iri.schemeSpecificRules();
        java.net.URI javaURI = java.net.URI.create(string);
        assertEquals(string, iri.rebuild());
        assertEquals(string, iri.str());
    }

    private void goodNoIRICheck(String string) {
        RFC3986.check(string);
        IRI3986 iri = RFC3986.create(string);
        java.net.URI javaURI = java.net.URI.create(string);
    }

    // Expect an IRIParseException
    private void bad(String string) {
        try {
            RFC3986.check(string);
            fail("Did not fail: "+string);
        } catch (IRIParseException ex) {}
    }
}
