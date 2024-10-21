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

package org.seaborne.rfc3986.compat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.apache.jena.iri.IRI;
import org.apache.jena.iri.IRIFactory;
import org.apache.jena.iri.IRIRelativize;
import org.seaborne.rfc3986.AlgRelativizeIRI;
import org.seaborne.rfc3986.AlgResolveIRI;
import org.seaborne.rfc3986.IRI3986;
import org.seaborne.rfc3986.TestRelativeVariants;


/**
 * Generate the source for {@link TestRelativeVariants}
 */
@TestMethodOrder(MethodOrderer.MethodName.class)    // Important!
public class Generate_TestRelativeVariants {

    @Test public void test_rel_100() { executeTests("test_rel_100", "http://example/dir", "http://example/path"); }
    @Test public void test_rel_101() { executeTests("test_rel_101", "http://example/dir", "http://example/dir/path"); }
    @Test public void test_rel_102() { executeTests("test_rel_102", "http://example/dir/", "http://example/dir/path"); }
    @Test public void test_rel_103() { executeTests("test_rel_103", "http://example/dir/file", "http://example/path"); }

    @Test public void test_rel_200() { executeTests("test_rel_200", "http://example/z/dir/", "http://example/z/alt/"); }
    @Test public void test_rel_201() { executeTests("test_rel_201", "http://example/z/dir/", "http://example/z/alt/path"); }
    @Test public void test_rel_202() { executeTests("test_rel_202", "http://example/z/dir/def", "http://example/z/alt/path"); }
    @Test public void test_rel_203() { executeTests("test_rel_203", "http://example/a/z/dir/", "http://example/a/z/alt/"); }
    @Test public void test_rel_204() { executeTests("test_rel_204", "http://example/a/z/dir/", "http://example/a/z/alt/path"); }
    @Test public void test_rel_205() { executeTests("test_rel_205", "http://example/a/z/dir/def", "http://example/a/z/alt/path"); }

    @Test public void test_rel_300() { executeTests("test_rel_300", "http://example/abc", "http://example/abc#frag"); }
    @Test public void test_rel_301() { executeTests("test_rel_301", "http://example/abc", "http://example/abc/file"); }
    @Test public void test_rel_302() { executeTests("test_rel_302", "http://example/abc", "http://example/abc?query"); }
    @Test public void test_rel_303() { executeTests("test_rel_303", "http://example/abc", "http://example/abc?query#frag"); }
    @Test public void test_rel_304() { executeTests("test_rel_304", "http://example/abc", "http://example/abc/#frag"); }
    @Test public void test_rel_305() { executeTests("test_rel_305", "http://example/abc", "http://example/abc/?query"); }
    @Test public void test_rel_306() { executeTests("test_rel_306", "http://example/abc", "http://example/abc/?query#frag"); }

    @Test public void test_rel_400() { executeTests("test_rel_400", "http://example/abc/", "http://example/abc"); }
    @Test public void test_rel_401() { executeTests("test_rel_401", "http://example/abc/", "http://example/abc/"); }
    @Test public void test_rel_402() { executeTests("test_rel_402", "http://example/abc/", "http://example/abc/file"); }
    @Test public void test_rel_403() { executeTests("test_rel_403", "http://example/abc/", "http://example/abc#frag"); }
    @Test public void test_rel_404() { executeTests("test_rel_404", "http://example/abc/", "http://example/abc?query"); }
    @Test public void test_rel_405() { executeTests("test_rel_405", "http://example/abc/", "http://example/abc?query#frag"); }
    @Test public void test_rel_406() { executeTests("test_rel_406", "http://example/abc/", "http://example/xyz#frag"); }
    @Test public void test_rel_407() { executeTests("test_rel_407", "http://example/abc/", "http://example/xyz?query"); }
    @Test public void test_rel_408() { executeTests("test_rel_408", "http://example/abc/", "http://example/xyz?query#frag"); }

    @Test public void test_rel_501() { executeTests("test_rel_501", "http://example/abc#", "http://example/abc"); }
    @Test public void test_rel_502() { executeTests("test_rel_502", "http://example/abc#", "http://example/abc#"); }
    @Test public void test_rel_503() { executeTests("test_rel_503", "http://example/abc#", "http://example/abc#frag"); }
    @Test public void test_rel_504() { executeTests("test_rel_504", "http://example/abc#", "http://example/abc?query"); }
    @Test public void test_rel_505() { executeTests("test_rel_505", "http://example/abc#", "http://example/abc?query#frag"); }
    @Test public void test_rel_506() { executeTests("test_rel_506", "http://example/abc#", "http://example/abc/"); }
    @Test public void test_rel_507() { executeTests("test_rel_507", "http://example/abc#frag", "http://example/abc"); }
    @Test public void test_rel_508() { executeTests("test_rel_508", "http://example/abc#frag", "http://example/abc#"); }
    @Test public void test_rel_509() { executeTests("test_rel_509", "http://example/abc#frag", "http://example/abc#frag"); }
    @Test public void test_rel_510() { executeTests("test_rel_510", "http://example/abc#frag", "http://example/abc#frag2"); }
    @Test public void test_rel_511() { executeTests("test_rel_511", "http://example/abc#frag", "http://example/abc?query"); }
    @Test public void test_rel_512() { executeTests("test_rel_512", "http://example/abc#frag", "http://example/abc?query#frag"); }
    @Test public void test_rel_513() { executeTests("test_rel_513", "http://example/abc#frag", "http://example/abc?query#frag2"); }
    @Test public void test_rel_514() { executeTests("test_rel_514", "http://example/abc#frag", "http://example/abc/"); }

    @Test public void test_rel_600() { executeTests("test_rel_600", "http://example/", "http://example/"); }
    @Test public void test_rel_601() { executeTests("test_rel_601", "http://example/", "http://example/#frag"); }
    @Test public void test_rel_602() { executeTests("test_rel_602", "http://example/", "http://example/?query"); }
    @Test public void test_rel_603() { executeTests("test_rel_603", "http://example/", "http://example/?query#frag"); }

    @Test public void test_rel_700() { executeTests("test_rel_700", "http://example/#", "http://example/"); }
    @Test public void test_rel_701() { executeTests("test_rel_701", "http://example/#", "http://example/#"); }
    @Test public void test_rel_702() { executeTests("test_rel_702", "http://example/#", "http://example/#frag"); }
    @Test public void test_rel_703() { executeTests("test_rel_703", "http://example/#", "http://example/?query"); }
    @Test public void test_rel_704() { executeTests("test_rel_704", "http://example/#", "http://example/?query#frag"); }
    @Test public void test_rel_705() { executeTests("test_rel_705", "http://example/#", "http://example/c/"); }
    @Test public void test_rel_706() { executeTests("test_rel_706", "http://example/#", "http://example/abc"); }
    @Test public void test_rel_707() { executeTests("test_rel_707", "http://example/#", "http://example/abc#"); }

    @Test public void test_rel_800() { executeTests("test_rel_800", "http://example/dir", "http://example/dir/path"); }
    @Test public void test_rel_801() { executeTests("test_rel_801", "http://example/dir1/file2", "http://example/dir1/dir2/path"); }
    @Test public void test_rel_802() { executeTests("test_rel_802", "http://example/dir/", "http://example/dir/path"); }
    @Test public void test_rel_803() { executeTests("test_rel_803", "http://example/dir1", "http://example/dir1/dir2/"); }
    @Test public void test_rel_804() { executeTests("test_rel_804", "http://example/dir1/", "http://example/dir1/dir2/"); }
    @Test public void test_rel_805() { executeTests("test_rel_805", "http://example/abc/def", "http://example/abc#frag"); }
    @Test public void test_rel_806() { executeTests("test_rel_806", "http://example/abc/def", "http://example/abc?query"); }
    @Test public void test_rel_807() { executeTests("test_rel_807", "http://example/abc/def", "http://example/abc?query#frag"); }
    @Test public void test_rel_808() { executeTests("test_rel_808", "https://example/dir", "http://example/dir"); }
    @Test public void test_rel_809() { executeTests("test_rel_809", "http://example/a", "http://other/a"); }

    @Test public void test_rel_900() { executeTests("test_rel_900", "http:", "http://other/a"); }
    @Test public void test_rel_901() { executeTests("test_rel_901", "http://", "http://other/a"); }
    @Test public void test_rel_902() { executeTests("test_rel_902", "http:", "https://other/a"); }
    @Test public void test_rel_903() { executeTests("test_rel_903", "http://", "https://other/a"); }
    @Test public void test_rel_904() { executeTests("test_rel_904", "http:", "//other/a"); }
    @Test public void test_rel_905() { executeTests("test_rel_905", "http://", "//other/a"); }


    private static int dftFlags = IRIRelativize.ABSOLUTE | IRIRelativize.SAMEDOCUMENT | IRIRelativize.CHILD | IRIRelativize.PARENT;

    private static void executeTests(String testName, String iriStr1, String iriStr2) {
        // Generate tests.


        IRI3986 base = IRI3986.create(iriStr1);
        IRI3986 target = IRI3986.create(iriStr2);
        {
            IRI3986 r = AlgResolveIRI.relativize(base, target);
            executeTest("all", dftFlags, base, target, r);
            generateTest(testName, "AlgResolveIRI::relativize", iriStr1, iriStr2, r);
        }{
            int flag = IRIRelativize.NETWORK;
            IRI3986 r = AlgRelativizeIRI.relativeScheme(base, target);
            executeTest("relativeScheme", flag, base, target, r);
            generateTest(testName, "AlgRelativizeIRI::relativeScheme", iriStr1, iriStr2, r);
        }{
            int flag = IRIRelativize.ABSOLUTE;
            IRI3986 r = AlgRelativizeIRI.relativeResource(base, target);
            executeTest("relativeResource", flag, base, target, r);
            generateTest(testName, "AlgRelativizeIRI::relativeResource", iriStr1, iriStr2, r);
        }{
            int flag = IRIRelativize.CHILD;
            IRI3986 r = AlgRelativizeIRI.relativePath(base, target);
            executeTest("relativePath", flag, base, target, r);
            generateTest(testName, "AlgRelativizeIRI::relativePath", iriStr1, iriStr2, r);
        }{
            int flag = IRIRelativize.PARENT;
            IRI3986 r = AlgRelativizeIRI.relativeParentPath(base, target);
            executeTest("relativeParentPath", flag, base, target, r);
            generateTest(testName, "AlgRelativizeIRI::relativeParentPath", iriStr1, iriStr2, r);
        }{
            int flag = IRIRelativize.SAMEDOCUMENT;;
            IRI3986 r = AlgRelativizeIRI.relativeSameDocument(base, target);
            executeTest("relativeSameDocument", flag, base, target, r);
            generateTest(testName, "AlgRelativizeIRI::relativeSameDocument", iriStr1, iriStr2, r);
        }
    }

    private static void generateTest(String testName, String function, String iriStr1, String iriStr2, IRI3986 expected) {
        String expectedStr =  (expected==null)? "null" : "\""+expected.str()+"\"";
        String functionMethod = function.replaceAll(".*::", "");
        String template = """
                @Test public void %s_%s() {
                    executeTest("%s", "%s", %s, %s);
                }

                """.formatted(testName, functionMethod, iriStr1, iriStr2, function, expectedStr);
        System.out.print(template);
    }

    private static void executeTest(String function, int flag, IRI3986 base, IRI3986 target, IRI3986 relative) {

        if ( relative != null ) {
            IRI3986 iri = base.resolve(relative);
            if ( ! iri.equals(target) )
                System.out.printf("Failed roundtrip : (<%s> <%s>) => <%s> => <%s>\n", base,target, out(relative), iri);
            assertEquals(iri, target);
        }

        IRI iri1 = IRIFactory.iriImplementation().create(base.str());
        IRI iri2 = IRIFactory.iriImplementation().create(target.str());
        String s2 = iri1.relativize(iri2, flag).toString();

        if ( s2.equals(iri2.toString()) )
            s2 = "--";
        else
            s2 = "<"+s2+">";
        String s1 = (relative==null)? "--" : "<"+relative.str()+">";

        // Known variance between iri4ld and jena-iri
        boolean variance = false;

        //if ( ! AlgRelativizeIRI.legacyCompatibility ) {
        if ( true ) {
            // Legacy jena-iri does not generate <> for the path when base path == target path.
            // e.g. jena-iri does not generate <#frag> for a CHILD when paths match and there is no query string
            // Covered by compatibility mode
            if ( flag == IRIRelativize.CHILD && base.path().equals(target.path())) {
                if ( ! target.hasQuery() ) {
                    variance = true;
                    return;
                }
            }
        }

        if ( true ) {
            // Legacy jena-iri does not handle "http:" (no "//").
            if ( flag == IRIRelativize.NETWORK && ! base.hasAuthority() && ( ! base.hasPath() || base.path().isEmpty() ) ) {
                variance = true;
                return;
            }

        }

        if ( ! s1.equals(s2) ) {
            System.out.print("** "+function+" ** ");
            System.out.printf("%-20s %-20s\n", s1, s2);
        }
        assertEquals(s1,s2);
    }

    private static String out(IRI3986 r) {
        if ( r == null )
            return "";
        return "<"+r.str()+">";
    }

    private static void compareRel(String base, String x) {
        IRI3986 iri1 = IRI3986.create(base);
        IRI3986 iri2 = IRI3986.create(x);
        String rel = iri1.relativize(iri2).str();

        String z = calcJenaIRI(base, x);

        System.out.println(rel);
        System.out.println(z);
        System.exit(0);
    }

    // Consider if relativeFlags is not -1, one routine per case for backwards compatibility.
    private static int RelativizeFlags = IRIRelativize.ABSOLUTE | IRIRelativize.SAMEDOCUMENT
            | IRIRelativize.CHILD | IRIRelativize.PARENT | IRIRelativize.GRANDPARENT;

    /** Calculate the relative URI using using jena-iri. */
    private static String calcJenaIRI(String basePath, String targetPath) {
        return TestRelativePaths_JenaIRI.calcJenaIRI(basePath, targetPath);
    }
}
