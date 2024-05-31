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

import org.junit.Test;
import static org.junit.Assert.fail;

public class TestParseOID {

    @Test public void oid_01() { test("urn:oid:1", true); }

    @Test public void oid_02() { test("urn:oid:0", true);}

    @Test public void oid_03() { test("urn:oid:153.8.0.981.0", true); }

    @Test public void oid_04() { test("urn:oid:1.2.840.113674.514.212.200", true); }

    @Test public void oid_05() { test("urn:oid:", false); }


    @Test public void oid_06() { test("urn:oid:", false); }

    @Test public void oid_07() { test("urn:oid:01", false); }

    @Test public void oid_08() { test("urn:oid:1.2.3.01", false); }

    @Test public void oid_09() { test("urn:oid:Z", false); }

    @Test public void oid_10() { test("urn:oid:Z", false); }

    static IRI3986 test(String string, boolean valid) {
        try {
            IRI3986 iri = ParseOID.parse(string);
            if ( valid ) {
                return iri;
            }
            fail("Should have failed");
        } catch (IRIParseException ex) {
            if ( valid )
                throw ex;
        }
        return null;
    }

}
