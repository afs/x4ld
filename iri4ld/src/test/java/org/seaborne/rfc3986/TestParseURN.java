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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

import java.util.Objects;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.Test;

import org.seaborne.rfc3986.ParseURN.URN8141;
import org.seaborne.rfc3986.ParseURN.URNParseException;

/** Test the class ParseDID */
public class TestParseURN {
    @Test public void parseURN_01() { goodURN("urn:nid:nss", "nid", "nss"); }
    @Test public void parseURN_02() { goodURN("urn:a-b:nss", "a-b", "nss"); }
    @Test public void parseURN_03() { goodURN("URN:OID:1.2.3", "OID", "1.2.3"); }
    @Test public void parseURN_04() { goodURN("urn:nid:nss?+R?=Q#F", "nid", "nss", "R", "Q", "F"); }
    @Test public void parseURN_05() { goodURN("urn:123456789-123456789-123456789-12:nss", "123456789-123456789-123456789-12", "nss"); }

    // Parse by regex.
    // Check good outcome.

    @Test public void parseURN_bad_01() { badURN("cat:ns:s"); }
    @Test public void parseURN_bad_02() { badURN("urn:ns"); }
    @Test public void parseURN_bad_03() { badURN("urn:ns:"); }
    @Test public void parseURN_bad_04() { badURN("urn:n:s"); }

    @Test public void parseURN_bad_05() { badURN("urn:n:s"); }
    @Test public void parseURN_bad_06() { badURN("urn:-ns:123"); }
    @Test public void parseURN_bad_07() { badURN("urn:ns-:123"); }

    @Test public void parseURN_bad_08() { badURN("urn:123456789-123456789-123456789-123:nss"); }

    private void goodURN(String string, String nid, String nss) {
        URN8141 x = ParseURN.parseURN(string);
        assertNotNull(x);
        assertEquals(nid, x.NID());
        assertEquals(nss, x.NSS());
        assertNull(x.components());
    }

    private void goodURN(String string, String nid, String nss, String rComp, String qComp, String fComp) {
        URN8141 x = ParseURN.parseURN(string);
        assertNotNull(x);
        assertEquals(nid, x.NID());
        assertEquals(nss, x.NSS());
        assertEquals(rComp, x.components().rComponent());
        assertEquals(qComp, x.components().qComponent());
        assertEquals(fComp, x.components().fComponent());
    }

    private void badURN(String string) {
        URN8141 x = ParseURN.parseURN(string);
        assertNull(x, "Not null: "+Objects.toString(x));

        // Again, with handler.
        BiConsumer<Issue, String> handler = (issue, msg) -> { throw new URNParseException(string, msg); };
        assertThrowsExactly(URNParseException.class, ()->ParseURN.parseURN(string, handler));
    }
}
