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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;

public class TestLangTag {

    @Test public void test_lang_parse_00() { testRFC5646("lng-scrp-rg", "lng-Scrp-RG", "lng", "Scrp", "RG", null, null); }
    @Test public void test_lang_parse_01() { testRFC5646("lng-scrp-rg-variant", "lng-Scrp-RG-variant", "lng", "Scrp", "RG", "variant", null); }
    @Test public void test_lang_parse_02() { testRFC5646("lng-scrp-rg-variant-e-abc", "lng-Scrp-RG-variant-e-abc", "lng", "Scrp", "RG", "variant", "e-abc"); }

    @Test public void test_lang_basic_01() { testRFC5646("en", "en",               "en", null, null, null, null); }
    @Test public void test_lang_basic_02() { testRFC5646("en-us", "en-US",            "en", null, "US", null, null); }
    @Test public void test_lang_basic_03() { testRFC5646("en-latn-us", "en-Latn-US",  "en", "Latn", "US", null, null); }
    @Test public void test_lang_basic_04() { testRFC5646("en-123", "en-123", "en", null, "123", null, null); }
    @Test public void test_lang_basic_05() { testRFC5646("en-1234", "en-1234", "en", null, null, "1234", null); }
    @Test public void test_lang_basic_06() { testRFC5646("en-latn", "en-Latn", "en", "Latn", null, null, null); }
    @Test public void test_lang_basic_07() { testRFC5646("en-latn-gb", "en-Latn-GB", "en", "Latn", "GB", null, null); }
    @Test public void test_lang_basic_08() { testNotJDK("en-brs-xxx-latn-gb", "en-brs-xxx-Latn-GB", "en-brs-xxx", "Latn", "GB", null, null); }
    @Test public void test_lang_basic_09() { testRFC5646("de-CH-w-extend", "de-CH-w-extend", "de", null, "CH", null, "w-extend"); }
    @Test public void test_lang_basic_10() { testPrivateUse("de-CH-x-phonebk-morech", "de-CH-x-phonebk-morech", "de", null, "CH", null, null, "x-phonebk-morech"); }
    // Private use language tag. No language!
    @Test public void test_lang_basic_11() { testPrivateUse("x-private", "x-private", null, null, null, null, null, "x-private"); }
    @Test public void test_lang_basic_12() { testPrivateUse("az-Latn-x-latn", "az-Latn-x-latn", "az", "Latn", null, null, null, "x-latn"); }

    @Test public void test_lang_bad_01() { testBad("123"); }
    @Test public void test_lang_bad_02() { testBad("abcdefghijklmn"); }
    @Test public void test_lang_bad_03() { testBad("abcdefghijklmn-123"); }
    @Test public void test_lang_bad_04() { testBad("abcdefghijklmn-latn"); }

    @Test public void test_lang_bad_05() { testBad("a?"); }
    @Test public void test_lang_bad_06() { testBad("a b"); }

    @Test public void test_lang_bad_repeated_extension() {
        // "en-a-bbb-a-ccc" is invalid because the subtag 'a' appears twice.
        testBad("en-a-bbb-a-ccc");
    }

    // Wikipedia but their private use tag is too long.
    @Test public void test_lang_bad_10() { testBad("en-x-Q123456789"); }

    // Special cases. Grandfathered etc.
    // A additional test for formatting. Tests above also do format testing.
    @Test public void test_langtag_special_01() { testFormatting("en-GB-oed", "en-GB-oed"); }
    @Test public void test_langtag_special_02() { testNotJDK("en-GB-oed", "en-GB-oed", "en", null, "GB", "oed",  null); }
    @Test public void test_langtag_special_03() { testFormatting("EN-gb-OED", "en-GB-oed"); }
    @Test public void test_langtag_special_04() { testNotJDK("EN-gb-OED", "en-GB-oed", "en", null, "GB", "oed",  null); }

    // The examples from RFC 5646
    @Test public void test_lang_10() { testRFC5646("de", "de", "de", null, null, null, null); }
    @Test public void test_lang_11() { testRFC5646("fr", "fr", "fr", null, null, null, null); }
    @Test public void test_lang_12() { testRFC5646("ja", "ja", "ja", null, null, null, null); }
    @Test public void test_lang_13() { testNotJDK("i-enochian", "i-enochian", "i-enochian", null, null, null, null); }
    @Test public void test_lang_14() { testRFC5646("zh-Hant", "zh-Hant", "zh", "Hant", null, null, null); }
    @Test public void test_lang_15() { testRFC5646("zh-Hans", "zh-Hans", "zh", "Hans", null, null, null); }
    @Test public void test_lang_16() { testRFC5646("sr-Cyrl", "sr-Cyrl", "sr", "Cyrl", null, null, null); }
    @Test public void test_lang_17() { testRFC5646("sr-Latn", "sr-Latn", "sr", "Latn", null, null, null); }

    // Extended language subtag (3 letter)
    @Test public void test_lang_18() { testNotJDK("zh-cmn-Hans-CN", "zh-cmn-Hans-CN", "zh-cmn", "Hans", "CN", null, null); }
    @Test public void test_lang_19() { testRFC5646("cmn-Hans-CN", "cmn-Hans-CN", "cmn", "Hans", "CN", null, null); }
    @Test public void test_lang_20() { testNotJDK("zh-yue-HK", "zh-yue-HK", "zh-yue", null, "HK", null, null); }
    @Test public void test_lang_21() { testRFC5646("yue-HK", "yue-HK", "yue", null, "HK", null, null); }
    @Test public void test_lang_22() { testRFC5646("zh-Hans-CN", "zh-Hans-CN", "zh", "Hans", "CN", null, null); }

    @Test public void test_lang_23() { testRFC5646("sr-Latn-RS", "sr-Latn-RS", "sr", "Latn", "RS", null, null); }
    @Test public void test_lang_24() { testRFC5646("sl-rozaj", "sl-rozaj", "sl", null, null, "rozaj", null); }
    @Test public void test_lang_25() { testNotJDK("sl-rozaj-biske", "sl-rozaj-biske", "sl", null, null, "rozaj-biske", null); }
    @Test public void test_lang_26() { testRFC5646("sl-nedis", "sl-nedis", "sl", null, null, "nedis", null); }
    @Test public void test_lang_27() { testRFC5646("de-CH-1901", "de-CH-1901", "de", null, "CH", "1901", null); }
    @Test public void test_lang_28() { testRFC5646("sl-IT-nedis", "sl-IT-nedis", "sl", null, "IT", "nedis", null); }
    @Test public void test_lang_29() { testRFC5646("hy-Latn-IT-arevela", "hy-Latn-IT-arevela", "hy", "Latn", "IT", "arevela", null); }
    @Test public void test_lang_30() { testRFC5646("de-DE", "de-DE", "de", null, "DE", null, null); }
    @Test public void test_lang_31() { testRFC5646("en-US", "en-US", "en", null, "US", null, null); }
    @Test public void test_lang_32() { testRFC5646("es-419", "es-419", "es", null, "419", null, null); }

    @Test public void test_lang_33() { testPrivateUse("de-CH-x-phonebk", "de-CH-x-phonebk", "de", null, "CH", null, null, "x-phonebk"); }
    @Test public void test_lang_34() { testPrivateUse("az-Arab-x-AZE-derbend", "az-Arab-x-aze-derbend", "az", "Arab", null, null, null, "x-aze-derbend"); }
    @Test public void test_lang_35() { testPrivateUse("x-whatever-a-abc-x-xyz", "x-whatever-a-abc-x-xyz", null, null, null, null, null, "x-whatever-a-abc-x-xyz"); }
    @Test public void test_lang_36() { testPrivateUse("qaa-Qaaa-QM-x-southern", "qaa-Qaaa-QM-x-southern", "qaa", "Qaaa", "QM", null, null, "x-southern"); }

    @Test public void test_lang_37() { testRFC5646("de-Qaaa", "de-Qaaa", "de", "Qaaa", null, null, null); }
    @Test public void test_lang_38() { testRFC5646("sr-Latn-QM", "sr-Latn-QM", "sr", "Latn", "QM", null, null); }
    @Test public void test_lang_39() { testRFC5646("sr-Qaaa-RS", "sr-Qaaa-RS", "sr", "Qaaa", "RS", null, null); }
    @Test public void test_lang_40() { testRFC5646("en-US-u-islamcal", "en-US-u-islamcal", "en", null, "US", null, "u-islamcal"); }
    @Test public void test_lang_41() { testPrivateUse("zh-CN-a-myext-x-private", "zh-CN-a-myext-x-private", "zh", null, "CN", null, "a-myext", "x-private"); }
    @Test public void test_lang_42() { testRFC5646("en-a-myext-b-another", "en-a-myext-b-another", "en", null, null, null, "a-myext-b-another"); }

    // From RFC 5646
    @Test public void test_lang_60() { testPrivateUse("en-Latn-GB-boont-r-extended-sequence-x-private", "en-Latn-GB-boont-r-extended-sequence-x-private",
                                                      "en","Latn", "GB", "boont", "r-extended-sequence", "x-private"); }

    @Test public void test_lang_61s() { testPrivateUse("en-Latn-GB-boont-r-extended-sequence-s-another-x-private", "en-Latn-GB-boont-r-extended-sequence-s-another-x-private",
                                                      "en","Latn", "GB", "boont", "r-extended-sequence-s-another", "x-private"); }

    @Test public void test_lang_50() { testPrivateUse("en-x-private", "en-x-private",    "en", null, null, null, null, "x-private"); }

    @Test public void test_lang_51() { testPrivateUse( "en-x-US",  "en-x-us",    "en", null, null, null, null, "x-us"); }
    // "Note that the tag "en-a-bbb-x-a-ccc" is valid because the second appearance of
    // the singleton 'a' is in a private use sequence."
    @Test public void test_lang_52() { testPrivateUse( "en-a-bbb-x-a-ccc" ,  "en-a-bbb-x-a-ccc" ,    "en", null, null, null, "a-bbb", "x-a-ccc"); }

    /** General test - include JDK */
    private static void testRFC5646(String langString, String formatted, String lang, String script, String region, String variant, String extension) {
        runTest(langString, formatted, lang, script, region, variant, extension, null, true);
    }

    /** Has a private use part - not JDK */
    private static void testPrivateUse(String langString, String formatted, String lang, String script, String region, String variant, String extension, String privateUse) {
        // Private use is not supported by the JDK.
        runTest(langString, formatted, lang, script, region, variant, extension, privateUse, false);
    }


    /** Run a test which is not properly supported by the JDK-Locale based implementation. */
    private static void testNotJDK(String langString, String formatted, String lang, String script, String region, String variant, String extension) {
        runTest(langString, formatted, lang, script, region, variant, extension, null, false);
    }

    /** Run a test which illgeal by RFC 5646 */
    private void testBad(String string) {
        try {
            LangTag langTag = LangTagRFC5646.create(string);
            // Parser throws an exception. In case that changes ...
            assertNull(langTag);
            fail("Expected a LangTagException");
        } catch (LangTagException ex) {
            //ex.printStackTrace();
        }
    }

    private static void runTest(String langString, String formatted,
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
        assertEquals("Lang", lang, langTag.getLanguage());
        assertEquals("Script", script, langTag.getScript());
        assertEquals("Region", region, langTag.getRegion());
        assertEquals("Variant", variant, langTag.getVariant());
        assertEquals("Extension", extension, langTag.getExtension());
        assertEquals("Private use", privateuse, langTag.getPrivateUse());
        String f = langTag.str();
        assertEquals("String formatted", formatted, f);
    }

    private static void testFormatting(String langString, String expected) {
        // Formatting.
        // Already in test1 but redoing it allows a check between the two formatters.
        LangTag langTag = LangTagRFC5646.create(langString);
        // Build formatted language tag.
        String fmt1 = langTag.str();
        assertEquals("RFC5646 parser format", expected, fmt1);
        // Formatting using the general algorithm of RFC5646.
        String fmt2 = LangTagOps.basicFormat(langString);
        assertEquals("RFC5646 basic algoithm", expected, fmt2);
    }
}
