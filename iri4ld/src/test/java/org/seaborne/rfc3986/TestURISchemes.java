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

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/** Test IRIs for the scheme validation process. */
public class TestURISchemes {

    // http:, https:
    // Including good, bad IPv4 addresses
    @Test public void parse_http_empty_host_1() { parse("http:///abc", 1, URIScheme.HTTP); }
    @Test public void parse_http_empty_host_2() { parse("https:///abc", 1, URIScheme.HTTPS); }
 //   @Test public void parse_addr_v4_1() { bad("http://10.11.12.1300/abc"); }

    // file:

    // did:

    //example:

    // urn:

    // urn:uuid:

    // uuid:
    private static final String testUUID = "4ca12587-3598-43e0-a286-299b16e2d270";
    // No URN components.
    @Test public void parse_uuid_bad_01()   { parse("uuid:"+testUUID+"?=abc", 1, URIScheme.UUID); }
//    @Test public void parse_uuid_bad_02()   { bad("uuid:"+testUUID+"#frag"); }





    // urn:oid:

    // oid:

    // urn:example:

    private void parse(String iristr, int expectedCount, URIScheme expectedUriScheme) {
        // No RFc 3986/7 syntax errors in this suite. See TestRFC3986_Syntax
        IRI3986 iri = RFC3986.create(iristr);
        int violations = countViolations(iri);
        assertEquals("Violation count", expectedCount, violations);

        String schemeName = iri.scheme();
        URIScheme uriScheme = URIScheme.get(schemeName);

        if ( expectedUriScheme != null ) {
            iri.forEachViolation(a->{
                URIScheme v_uriScheme = a.scheme();
                assertEquals(v_uriScheme, expectedUriScheme);
            });
        }
    }

    private int countViolations(IRI3986 iri) {
        //class Ref { int vCount = 0;  }
        var x = new Object() { int vCount = 0;  };
        iri.forEachViolation(a->x.vCount++);
        return x.vCount;
    }


}
