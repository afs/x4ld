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

package org.seaborne.lang4ld;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TestLangTag {

    @Test public void test_lang_basic_01() { test("en", "en",               "en", null, null, null, null); }
    @Test public void test_lang_basic_02() { test("en-us", "en-US",            "en", null, "US", null, null); }
    @Test public void test_lang_basic_03() { test("en-latn-us", "en-Latn-US",  "en", "Latn", "US", null, null); }
    @Test public void test_lang_basic_04() { test("en-123", "en-123", "en", null, "123", null, null); }
    @Test public void test_lang_basic_05() { test("en-1234", "en-1234", "en", null, null, "1234", null); }
    @Test public void test_lang_basic_06() { test("en-latn", "en-Latn", "en", "Latn", null, null, null); }
    @Test public void test_lang_basic_07() { test("en-latn-gb", "en-Latn-GB", "en", "Latn", "GB", null, null); }
    @Test public void test_lang_basic_08() { testNotJDK("en-brs-xxx-latn-gb", "en-brs-xxx-Latn-GB", "en-brs-xxx", "Latn", "GB", null, null); }
    @Test public void test_lang_basic_09() { test("de-CH-w-extend", "de-CH-w-extend", "de", null, "CH", null, "w-extend"); }
    @Test public void test_lang_basic_10() { test("de-CH-x-phonebk-morech", "de-CH-x-phonebk-morech", "de", null, "CH", null, "x-phonebk-morech"); }
    @Test public void test_lang_basic_11() { testNotJDK("x-private", "x-private", "x-private", null, null, null, null); }
    @Test public void test_lang_basic_12() { test("az-Latn-x-latn", "az-Latn-x-latn", "az", "Latn", null, null, "x-latn"); }

    @Test public void test_lang_bad_01() { testBad("123"); }
    @Test public void test_lang_bad_02() { testBad("abcdefghijklmn"); }
    @Test public void test_lang_bad_03() { testBad("abcdefghijklmn-123"); }
    @Test public void test_lang_bad_04() { testBad("abcdefghijklmn-latn"); }

    @Test public void test_lang_bad_05() { testBad("a?"); }
    @Test public void test_lang_bad_06() { testBad("a b"); }

    // A additional test for formatting. Tests above also do format testing.
    @Test public void test_langtag_fmt_01() { testFormatting("en-GB-oed", "en-GB-oed"); }
    @Test public void test_langtag_fmt_02() { testFormatting("EN-gb-OED", "en-GB-oed"); }

    // The examples from RFC 5646
    @Test public void test_lang_10() { test("de", "de", "de", null, null, null, null); }
    @Test public void test_lang_11() { test("fr", "fr", "fr", null, null, null, null); }
    @Test public void test_lang_12() { test("ja", "ja", "ja", null, null, null, null); }
    @Test public void test_lang_13() { testNotJDK("i-enochian", "i-enochian", "i-enochian", null, null, null, null); }
    @Test public void test_lang_14() { test("zh-Hant", "zh-Hant", "zh", "Hant", null, null, null); }
    @Test public void test_lang_15() { test("zh-Hans", "zh-Hans", "zh", "Hans", null, null, null); }
    @Test public void test_lang_16() { test("sr-Cyrl", "sr-Cyrl", "sr", "Cyrl", null, null, null); }
    @Test public void test_lang_17() { test("sr-Latn", "sr-Latn", "sr", "Latn", null, null, null); }
    @Test public void test_lang_18() { testNotJDK("zh-cmn-Hans-CN", "zh-cmn-Hans-CN", "zh-cmn", "Hans", "CN", null, null); }
    @Test public void test_lang_19() { test("cmn-Hans-CN", "cmn-Hans-CN", "cmn", "Hans", "CN", null, null); }
    @Test public void test_lang_20() { testNotJDK("zh-yue-HK", "zh-yue-HK", "zh-yue", null, "HK", null, null); }
    @Test public void test_lang_21() { test("yue-HK", "yue-HK", "yue", null, "HK", null, null); }
    @Test public void test_lang_22() { test("zh-Hans-CN", "zh-Hans-CN", "zh", "Hans", "CN", null, null); }
    @Test public void test_lang_23() { test("sr-Latn-RS", "sr-Latn-RS", "sr", "Latn", "RS", null, null); }
    @Test public void test_lang_24() { test("sl-rozaj", "sl-rozaj", "sl", null, null, "rozaj", null); }
    @Test public void test_lang_25() { testNotJDK("sl-rozaj-biske", "sl-rozaj-biske", "sl", null, null, "rozaj-biske", null); }
    @Test public void test_lang_26() { test("sl-nedis", "sl-nedis", "sl", null, null, "nedis", null); }
    @Test public void test_lang_27() { test("de-CH-1901", "de-CH-1901", "de", null, "CH", "1901", null); }
    @Test public void test_lang_28() { test("sl-IT-nedis", "sl-IT-nedis", "sl", null, "IT", "nedis", null); }
    @Test public void test_lang_29() { test("hy-Latn-IT-arevela", "hy-Latn-IT-arevela", "hy", "Latn", "IT", "arevela", null); }
    @Test public void test_lang_30() { test("de-DE", "de-DE", "de", null, "DE", null, null); }
    @Test public void test_lang_31() { test("en-US", "en-US", "en", null, "US", null, null); }
    @Test public void test_lang_32() { test("es-419", "es-419", "es", null, "419", null, null); }
    @Test public void test_lang_33() { test("de-CH-x-phonebk", "de-CH-x-phonebk", "de", null, "CH", null, "x-phonebk"); }
    @Test public void test_lang_34() { test("az-Arab-x-AZE-derbend", "az-Arab-x-aze-derbend", "az", "Arab", null, null, "x-aze-derbend"); }
    @Test public void test_lang_35() { testNotJDK("x-whatever", "x-whatever", "x-whatever", null, null, null, null); }
    @Test public void test_lang_36() { test("qaa-Qaaa-QM-x-southern", "qaa-Qaaa-QM-x-southern", "qaa", "Qaaa", "QM", null, "x-southern"); }
    @Test public void test_lang_37() { test("de-Qaaa", "de-Qaaa", "de", "Qaaa", null, null, null); }
    @Test public void test_lang_38() { test("sr-Latn-QM", "sr-Latn-QM", "sr", "Latn", "QM", null, null); }
    @Test public void test_lang_39() { test("sr-Qaaa-RS", "sr-Qaaa-RS", "sr", "Qaaa", "RS", null, null); }
    @Test public void test_lang_40() { test("en-US-u-islamcal", "en-US-u-islamcal", "en", null, "US", null, "u-islamcal"); }
    @Test public void test_lang_41() { test("zh-CN-a-myext-x-private", "zh-CN-a-myext-x-private", "zh", null, "CN", null, "a-myext-x-private"); }
    @Test public void test_lang_42() { test("en-a-myext-b-another", "en-a-myext-b-another", "en", null, null, null, "a-myext-b-another"); }

    /** Run tests on the langString  */
    private static void test(String langString, String formatted, String lang, String script, String region, String variant, String extension) {
        runTest(langString, formatted, lang, script, region, variant, extension, null, true);
    }

    /** Run a test which is not properly supported by the JDK-Locale based implementation. */
    private static void testNotJDK(String langString, String formatted, String lang, String script, String region, String variant, String extension) {
        runTest(langString, formatted, lang, script, region, variant, extension, null, false);
    }

    private void testBad(String string) {
        try {
            LangTagRFC5646.create(string);
            fail("Expected a LangTagException");
        } catch (LangTagException ex) {}
    }

    private static void runTest(String langString,
                                String formatted,
                                String lang, String script, String region, String variant, String extension, String privateuse,
                                boolean jdkSupported) {
        // Run the test with varied case of the input string.
        test1(langString,               formatted, lang, script, region, variant, extension, privateuse);
        test1(langString.toLowerCase(), formatted, lang, script, region, variant, extension, privateuse);
        test1(langString.toUpperCase(), formatted, lang, script, region, variant, extension, privateuse);

        // Formatting.
        testFormatting(langString, formatted);

        // JDK
        if ( jdkSupported ) {
            LangTag jdk = LangTagJDK.create(langString);
            assertEquals(lang, jdk.getLanguage());
            assertEquals(script, jdk.getScript());
            assertEquals(region, jdk.getRegion());
            assertEquals(variant, jdk.getVariant());
            assertEquals(extension, jdk.getExtension());
            assertEquals(privateuse, jdk.getPrivateUse());
            // Private languages are not supported by JDK Locale.
        }
    }

    // Test execution for LangTagRFC5646 on one exact input string.
    private static void test1(String langString, String formatted, String lang, String script, String region, String variant, String extension, String privateuse) {
        LangTag langTag = LangTagRFC5646.create(langString);
        assertNotNull(langTag);
        assertEquals(lang, langTag.getLanguage());
        assertEquals(script, langTag.getScript());
        assertEquals(region, langTag.getRegion());
        assertEquals(variant, langTag.getVariant());
        assertEquals(extension, langTag.getExtension());
        assertEquals(privateuse, langTag.getPrivateUse());
        String f = langTag.str();
        assertEquals(formatted, f);
    }

    private static void testFormatting(String langString, String expected) {
        // Formatting.
        // Already in test1 but redoing it allows a check between the two formatters.
        LangTag langTag = LangTagRFC5646.create(langString);
        // Build formatted language tag.
        String fmt1 = langTag.str();
        assertEquals("RFC5646 parser format", expected, fmt1);
        // Formatting using the general algorithm of RFC5646.
        String fmt2 = LangTagOps.format(langString);
        assertEquals("RFC5646 basic algoithm", expected, fmt2);
    }
}
