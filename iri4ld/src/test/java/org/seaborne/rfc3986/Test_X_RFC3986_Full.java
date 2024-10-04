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
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * General parsing of URIs, not scheme specific rules.
 * @see Test_X_RFC3986_Scheme_Full
 * @see TestRFC3986_Features
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test_X_RFC3986_Full {
    // Detailed testing IPv4 parsing is in {@link TestParseIPv4Address}
    // Detailed testing IPv6 parsing is in {@link TestParseIPv6Address}

    // ---- Compare to jena-iri
    @Test public void parse_00() { good("http://host"); }
    @Test public void parse_01() { good("http://host/"); }
    @Test public void parse_02() { goodNoIRICheck("http://host:/"); }
    @Test public void parse_03() { goodNoIRICheck("http://host:10/"); }
    @Test public void parse_04() { goodNoIRICheck("http://host:80/"); }
    @Test public void parse_05() { goodNoIRICheck("https://host:443/"); }

    @Test public void parse_11() { good("http://host:8081/abc/def?qs=ghi#jkl"); }

    @Test public void parse_12() { good("http://[::1]:8080/abc/def?qs=ghi#jkl"); }

    // %XX in host added at RFC 3986.
    @Test public void parse_13() { goodNoIRICheck("http://ab%AAdef/xyzβ/abc"); }

    @Test public void parse_14() { good("/abcdef"); }

    @Test public void parse_15() { good("/ab%FFdef"); }

    // Uppercase preferred.
    @Test public void parse_16() { goodNoIRICheck("/ab%ffdef"); }

    @Test public void parse_17() { good("http://host/abcdef?qs=foo#frag"); }

    @Test public void parse_18() { good(""); }

    @Test public void parse_19() { good("."); }

    @Test public void parse_20() { good(".."); }

    @Test public void parse_21() { good("//host:8081/abc/def?qs=ghi#jkl"); }

    @Test public void parse_22() { goodNoIRICheck("a+.-9://h/"); }

    // No path.

    @Test public void parse_23() { good("http://host"); }

    @Test public void parse_24() { good("http://host#frag"); }

    @Test public void parse_25() { good("http://host?query"); }

    // : in first segment in path.
    @Test public void parse_26() { good("http://host/a:b/"); }

    @Test public void parse_27() { good("/a:b/"); }

    @Test public void parse_28() { good("/z/a:b"); }

    // Characters / and ? in trailer
    @Test public void parse_29() { good("http://host/ab?query=abc?def"); }
    @Test public void parse_30() { good("http://host/ab#abc?def"); }
    @Test public void parse_31() { good("http://host/path?q=abc/def#abc/def"); }
    @Test public void parse_32() { good("http://host/path?q=abc/def#abc?def"); }

    @Test public void parse_http_04()   { good("nothttp://users@host/file/name.txt"); }

    @Test public void parse_http_05()   { good("nothttp://users@/file/name.txt"); }

    @Test public void parse_file_01() { good("file:///file/name.txt"); }

    // This is legal by RFC 8089 (jena-iri, based on the original RFC 1738, fails this with missing authority).
    @Test public void parse_file_03() { goodNoIRICheck("file:/file/name.txt"); }

    @Test public void parse_urn_01() { good("urn:x-local:abc/def"); }

    @Test public void parse_urn_02()        { good("urn:abc0:def"); }

    // rq-components = [ "?+" r-component ]
    //                 [ "?=" q-component ]
    // f-component   = fragment

    @Test public void parse_urn_component_01()        { good("urn:ns:abc/def?+more"); }
    @Test public void parse_urn_component_02()        { good("urn:ns:abc/def?=123"); }
    @Test public void parse_urn_component_03()        { good("urn:ns:abc/def?=rComp?+qComp"); }
    @Test public void parse_urn_component_04()        { good("urn:ns:abc/def?=resolve?+123#frag"); }
    @Test public void parse_urn_component_05()        { good("urn:ns:abc/def#frag"); }

    // Allow Unicode in the NSS and components.
    // (Strictly, URNs are ASCII)
    @Test public void parse_urn_unicode_01()        { good("urn:ns:αβγ"); }
    @Test public void parse_urn_unicode_02()        { good("urn:ns:x?=αβγ"); }
    @Test public void parse_urn_unicode_03()        { good("urn:ns:x?+α?=βγ"); }
    @Test public void parse_urn_unicode_04()        { good("urn:ns:x?+α?=β#γ"); }

    private static final String testUUID = "326f63ea-7447-11ee-b715-0be26fda5b37";

    @Test public void parse_uuid_01()   { goodNoIRICheck("uuid:"+testUUID); }

    @Test public void parse_uuid_02()   { goodNoIRICheck("uuid:"+(testUUID.toUpperCase(Locale.ROOT))); }

    @Test public void parse_uuid_03()   { goodNoIRICheck("UUID:"+testUUID); }

    @Test public void parse_urn_uuid_01()   { good("urn:uuid:"+testUUID); }

    @Test public void parse_urn_uuid_02()   { goodNoIRICheck("urn:uuid:"+(testUUID.toUpperCase(Locale.ROOT))); }

    @Test public void parse_urn_uuid_03()   { goodNoIRICheck("URN:UUID:"+testUUID); }

    @Test public void parse_urn_uuid_04()   { goodNoIRICheck("URN:UUID:"+testUUID+"?=abc"); }

    @Test public void parse_urn_uuid_05()   { goodNoIRICheck("URN:UUID:"+testUUID+"?=αβγ"); }

    @Test public void parse_urn_uuid_06()   { goodNoIRICheck("URN:UUID:"+testUUID+"+=α?=β#γ"); }

    @Test public void parse_oid_1()     { goodNoIRICheck("oid:1.2.3"); }

    @Test public void parse_ftp_01()    { good("ftp://user@host:3333/abc/def?qs=ghi#jkl"); }

    @Test public void parse_ftp_02()    { good("ftp://[::1]/abc/def?qs=ghi#jkl"); }

    // ---- bad

    // Leading ':'
    @Test public void bad_uri_scheme_1() { bad(":segment"); }

    // Bad scheme
    @Test public void bad_uri_scheme_2() { bad("://host/xyz"); }

    // Bad scheme
    @Test public void bad_uri_scheme_3() { bad("1://host/xyz"); }

    // Bad scheme
    @Test public void bad_uri_scheme_4() { bad("a~b://host/xyz"); }

    // Bad scheme
    @Test public void bad_uri_scheme_5() { bad("aβ://host/xyz"); }

    // Bad scheme
    @Test public void bad_uri_scheme_6() { bad("_:xyz"); }

    // Bad scheme
    @Test public void bad_uri_scheme_7() { bad("a_b:xyz"); }

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
        RFC3986.checkSyntax(string);
        IRI3986 iri = RFC3986.create(string);
        if ( iri.hasViolations() )
            fail("Violations "+string);
        else {
            // No IRI3986 violations
            IRI iri1 = JenaIRI.iriFactory().create(string);
            if ( iri1.hasViolation(true) ) {
                iri1.violations(true).forEachRemaining(v-> System.err.println("IRI = "+string + " :: "+v.getLongMessage()));
                fail("Violations "+string);
            }
        }
        // Check parses by JDK.
        java.net.URI javaURI = java.net.URI.create(string);
        assertEquals(string, iri.rebuild());
        assertEquals(string, iri.str());

        IRI3986 iriByRegex = RFC3986.createByRegex(string);
        assertTrue("Identical: ", iri.identical(iriByRegex, false));
        assertTrue(iri.identical(iriByRegex, true));
    }

    private void goodNoIRICheck(String string) {
        RFC3986.checkSyntax(string);
        IRI3986 iri = RFC3986.create(string);
        java.net.URI javaURI = java.net.URI.create(string);
    }

    // Expect an IRIParseException
    private void bad(String string) {
        try {
            RFC3986.checkSyntax(string);
            fail("Did not fail: "+string);
        } catch (IRIParseException ex) {}
    }
}
