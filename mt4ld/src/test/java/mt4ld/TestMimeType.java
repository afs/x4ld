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

package mt4ld;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TestMimeType {
    @Test public void parse_01() { test("text/plain"); }

    @Test public void parse_02() { test("text/plain;q=0.5"); }

    @Test public void parse_03() { test("text/plain;foo=\"alpha\"", "text/plain;foo=alpha"); }

    @Test public void parse_04() { test("text/plain;foo=\"al\\\"pha\""); }

    @Test public void parse_05() { test("text/plain;q=0.5;foo=\"al\\\"pha\""); }

    @Test public void parse_06() { test("application/rdf+xml"); }

    @Test public void parse_07() { test("application/x-www-form-urlencoded"); }

    @Test public void parse_08() { test("application/vnd.opensource"); }

    @Test public void parse_09() { test("type/subtype+suffix"); }

    @Test public void parse_20() { test("type/subtype;param=\"bar\"", "type/subtype;param=bar"); }

    @Test public void parse_21() { test("type/subtype;param=\"b\\\"ar\""); }

    @Test public void parse_22() { test("type/subtype;param=\"b r\""); }

    @Test public void parse_23() { test("type/subtype;param=\"$$!!\"", "type/subtype;param=$$!!"); }

    @Test public void parse_24() { test("type/subtype;param=$$!!"); }

    @Test public void parse_25() { test("type/subtype;param=", "type/subtype;param=\"\""); }

    @Test public void parse_26() { test("type/subtype;param=\"\""); }

    @Test public void parse_50() { test("type/private.subtype+suffix;param=1;param=\"bar\"",
                                       "type/private.subtype+suffix;param=1;param=bar"); }

    @Test(expected=MimeType.MimeTypeParseException.class)
    public void parse_bad_1() { test("/plain"); }

    @Test(expected=MimeType.MimeTypeParseException.class)
    public void parse_bad_2() { test("text"); }

    @Test(expected=MimeType.MimeTypeParseException.class)
    public void parse_bad_3() { test("text/"); }

    @Test(expected=MimeType.MimeTypeParseException.class)
    public void parse_bad_4() { test("text/foo;bar=\"unclosed"); }

    @Test(expected=MimeType.MimeTypeParseException.class)
    public void parse_bad_5() { test("text/foo;bar=()"); }

    @Test(expected=MimeType.MimeTypeParseException.class)
    public void parse_bad_6() { test("type/subtype;param=@@"); }

    private void test(String string) {
        test(string, string);
    }

    private void test(String string, String expected) {
        MimeType mt = MimeType.create(string);
        assertEquals(expected, mt.toString());
    }
}

