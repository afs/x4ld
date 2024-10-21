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

import org.junit.jupiter.api.Test;

import org.apache.jena.iri.IRI;
import org.apache.jena.iri.IRIFactory;
import org.apache.jena.iri.IRIRelativize;
import org.seaborne.rfc3986.AlgRelativizeIRI;
import org.seaborne.rfc3986.AlgResolveIRI;
import org.seaborne.rfc3986.IRI3986;

public class TestRelativeVariants_JenaIRI {

    @Test public void test_rel_100() { executeTests("http://example/dir", "http://example/path"); }
    @Test public void test_rel_101() { executeTests("http://example/dir", "http://example/dir/path"); }
    @Test public void test_rel_102() { executeTests("http://example/dir/", "http://example/dir/path"); }
    @Test public void test_rel_103() { executeTests("http://example/dir/file", "http://example/path"); }

    @Test public void test_rel_200() { executeTests("http://example/z/dir/", "http://example/z/alt/"); }
    @Test public void test_rel_201() { executeTests("http://example/z/dir/", "http://example/z/alt/path"); }
    @Test public void test_rel_202() { executeTests("http://example/z/dir/def", "http://example/z/alt/path"); }
    @Test public void test_rel_203() { executeTests("http://example/a/z/dir/", "http://example/a/z/alt/"); }
    @Test public void test_rel_204() { executeTests("http://example/a/z/dir/", "http://example/a/z/alt/path"); }
    @Test public void test_rel_205() { executeTests("http://example/a/z/dir/def", "http://example/a/z/alt/path"); }

    @Test public void test_rel_300() { executeTests("http://example/abc", "http://example/abc#frag"); }
    @Test public void test_rel_301() { executeTests("http://example/abc", "http://example/abc/file"); }
    @Test public void test_rel_302() { executeTests("http://example/abc", "http://example/abc?query"); }
    @Test public void test_rel_303() { executeTests("http://example/abc", "http://example/abc?query#frag"); }
    @Test public void test_rel_304() { executeTests("http://example/abc", "http://example/abc/#frag"); }
    @Test public void test_rel_305() { executeTests("http://example/abc", "http://example/abc/?query"); }
    @Test public void test_rel_306() { executeTests("http://example/abc", "http://example/abc/?query#frag"); }

    @Test public void test_rel_400() { executeTests("http://example/abc/", "http://example/abc"); }
    @Test public void test_rel_401() { executeTests("http://example/abc/", "http://example/abc/"); }
    @Test public void test_rel_402() { executeTests("http://example/abc/", "http://example/abc/file"); }
    @Test public void test_rel_403() { executeTests("http://example/abc/", "http://example/abc#frag"); }
    @Test public void test_rel_404() { executeTests("http://example/abc/", "http://example/abc?query"); }
    @Test public void test_rel_405() { executeTests("http://example/abc/", "http://example/abc?query#frag"); }
    @Test public void test_rel_406() { executeTests("http://example/abc/", "http://example/xyz#frag"); }
    @Test public void test_rel_407() { executeTests("http://example/abc/", "http://example/xyz?query"); }
    @Test public void test_rel_408() { executeTests("http://example/abc/", "http://example/xyz?query#frag"); }

    @Test public void test_rel_501() { executeTests("http://example/abc#", "http://example/abc"); }
    @Test public void test_rel_502() { executeTests("http://example/abc#", "http://example/abc#"); }
    @Test public void test_rel_503() { executeTests("http://example/abc#", "http://example/abc#frag"); }
    @Test public void test_rel_504() { executeTests("http://example/abc#", "http://example/abc?query"); }
    @Test public void test_rel_505() { executeTests("http://example/abc#", "http://example/abc?query#frag"); }
    @Test public void test_rel_506() { executeTests("http://example/abc#", "http://example/abc/"); }
    @Test public void test_rel_507() { executeTests("http://example/abc#frag", "http://example/abc"); }
    @Test public void test_rel_508() { executeTests("http://example/abc#frag", "http://example/abc#"); }
    @Test public void test_rel_509() { executeTests("http://example/abc#frag", "http://example/abc#frag"); }
    @Test public void test_rel_510() { executeTests("http://example/abc#frag", "http://example/abc#frag2"); }
    @Test public void test_rel_511() { executeTests("http://example/abc#frag", "http://example/abc?query"); }
    @Test public void test_rel_512() { executeTests("http://example/abc#frag", "http://example/abc?query#frag"); }
    @Test public void test_rel_513() { executeTests("http://example/abc#frag", "http://example/abc?query#frag2"); }
    @Test public void test_rel_514() { executeTests("http://example/abc#frag", "http://example/abc/"); }

    @Test public void test_rel_600() { executeTests("http://example/", "http://example/"); }
    @Test public void test_rel_601() { executeTests("http://example/", "http://example/#frag"); }
    @Test public void test_rel_602() { executeTests("http://example/", "http://example/?query"); }
    @Test public void test_rel_603() { executeTests("http://example/", "http://example/?query#frag"); }

    @Test public void test_rel_700() { executeTests("http://example/#", "http://example/"); }
    @Test public void test_rel_701() { executeTests("http://example/#", "http://example/#"); }
    @Test public void test_rel_702() { executeTests("http://example/#", "http://example/#frag"); }
    @Test public void test_rel_703() { executeTests("http://example/#", "http://example/?query"); }
    @Test public void test_rel_704() { executeTests("http://example/#", "http://example/?query#frag"); }
    @Test public void test_rel_705() { executeTests("http://example/#", "http://example/c/"); }
    @Test public void test_rel_706() { executeTests("http://example/#", "http://example/abc"); }
    @Test public void test_rel_707() { executeTests("http://example/#", "http://example/abc#"); }

    @Test public void test_rel_800() { executeTests("http://example/dir", "http://example/dir/path"); }
    @Test public void test_rel_801() { executeTests("http://example/dir1/file2", "http://example/dir1/dir2/path"); }
    @Test public void test_rel_802() { executeTests("http://example/dir/", "http://example/dir/path"); }
    @Test public void test_rel_803() { executeTests("http://example/dir1", "http://example/dir1/dir2/"); }
    @Test public void test_rel_804() { executeTests("http://example/dir1/", "http://example/dir1/dir2/"); }
    @Test public void test_rel_805() { executeTests("http://example/abc/def", "http://example/abc#frag"); }
    @Test public void test_rel_806() { executeTests("http://example/abc/def", "http://example/abc?query"); }
    @Test public void test_rel_807() { executeTests("http://example/abc/def", "http://example/abc?query#frag"); }
    @Test public void test_rel_808() { executeTests("https://example/dir", "http://example/dir"); }
    @Test public void test_rel_809() { executeTests("http://example/a", "http://other/a"); }

    @Test public void test_rel_900() { executeTests("http:", "http://other/a"); }
    @Test public void test_rel_901() { executeTests("http://", "http://other/a"); }
    @Test public void test_rel_902() { executeTests("http:", "https://other/a"); }
    @Test public void test_rel_903() { executeTests("http://", "https://other/a"); }
    @Test public void test_rel_904() { executeTests("http:", "//other/a"); }
    @Test public void test_rel_905() { executeTests("http://", "//other/a"); }

    private static int dftFlags = IRIRelativize.ABSOLUTE | IRIRelativize.SAMEDOCUMENT | IRIRelativize.CHILD | IRIRelativize.PARENT;

    private static void executeTests(String iriStr1, String iriStr2) {
        IRI3986 base = IRI3986.create(iriStr1);
        IRI3986 target = IRI3986.create(iriStr2);
        {
            IRI3986 r = AlgResolveIRI.relativize(base, target);
            executeTest("all", dftFlags, base, target, r);
        }{
            int flag = IRIRelativize.NETWORK;
            IRI3986 r = AlgRelativizeIRI.relativeScheme(base, target);
            executeTest("relativeScheme", flag, base, target, r);
        }{
            int flag = IRIRelativize.ABSOLUTE;
            IRI3986 r = AlgRelativizeIRI.relativeResource(base, target);
            executeTest("relativeResource", flag, base, target, r);
        }{
            int flag = IRIRelativize.CHILD;
            IRI3986 r = AlgRelativizeIRI.relativePath(base, target);
            executeTest("relativePath", flag, base, target, r);
        }{
            int flag = IRIRelativize.PARENT;
            IRI3986 r = AlgRelativizeIRI.relativeParentPath(base, target);
            executeTest("relativeParentPath", flag, base, target, r);
        }{
            int flag = IRIRelativize.SAMEDOCUMENT;;
            IRI3986 r = AlgRelativizeIRI.relativeSameDocument(base, target);
            executeTest("relativeSameDocument", flag, base, target, r);
        }
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

        //if ( ! AlgRelativizeIRI.legacyCompatibility ) { Now always false.
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
    private static int RelativizeFlags = IRIRelativize.ABSOLUTE | IRIRelativize.SAMEDOCUMENT |
                                         IRIRelativize.CHILD    | IRIRelativize.PARENT |
                                         IRIRelativize.GRANDPARENT;

    /** Calculate the relative URI using using jena-iri. */
    private static String calcJenaIRI(String basePath, String targetPath) {
        return TestRelativePaths_JenaIRI.calcJenaIRI(basePath, targetPath);
    }
}
