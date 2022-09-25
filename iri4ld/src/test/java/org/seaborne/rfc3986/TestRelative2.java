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

import static org.apache.jena.iri.IRIRelativize.ABSOLUTE;
import static org.apache.jena.iri.IRIRelativize.CHILD;
import static org.apache.jena.iri.IRIRelativize.PARENT;
import static org.apache.jena.iri.IRIRelativize.SAMEDOCUMENT;
import static org.junit.Assert.assertEquals;

import java.util.Objects;

import org.apache.jena.iri.IRIFactory;
import org.junit.Test;

public class TestRelative2 {
    @Test public void relative_path_101() { test_relative(asIRI("/a"), asIRI("/a/b"),  "a/b"); }
    @Test public void relative_path_102() { test_relative(asIRI("/a"), asIRI("/a/b/"), "a/b/"); }
    @Test public void relative_path_103() {  test_relative(asIRI("/a"), asIRI("/a"), ""); }
    @Test public void relative_path_104() { test_relative(asIRI("/a"), asIRI("/a/"), "a/"); }
    @Test public void relative_path_105() { test_relative(asIRI("/a"), asIRI("/z"), "z"); }
    @Test public void relative_path_106() { test_relative(asIRI("/a"), asIRI("/z/"), "z/"); }
    @Test public void relative_path_107() { test_relative(asIRI("/a"), asIRI("/z/y"), "z/y"); }
    @Test public void relative_path_108() { test_relative(asIRI("/a"), asIRI("/a/z"), "a/z"); }
    @Test public void relative_path_109() { test_relative(asIRI("/a"), asIRI("/a/z/"), "a/z/"); }

    @Test public void relative_path_200() { test_relative(asIRI("/a/"), asIRI("/a/b"),  "b"); }
    @Test public void relative_path_201() { test_relative(asIRI("/a/"), asIRI("/a/b/"), "b/"); }

    @Test public void relative_path_202() { test_relative(asIRI("/a/"), asIRI("/a"), "/a"); }

    @Test public void relative_path_203() { test_relative(asIRI("/a/"), asIRI("/a/"), ""); }
    @Test public void relative_path_204() { test_relative(asIRI("/a/"), asIRI("/z"), "/z"); }
    @Test public void relative_path_205() { test_relative(asIRI("/a/"), asIRI("/z/"), "/z/"); }
    @Test public void relative_path_206() { test_relative(asIRI("/a/"), asIRI("/z/y"), "/z/y"); }
    @Test public void relative_path_207() { test_relative(asIRI("/a/"), asIRI("/a/z"), "z"); }
    @Test public void relative_path_208() { test_relative(asIRI("/a/"), asIRI("/a/z/"), "z/"); }

    @Test public void relative_path_300() { test_relative(asIRI("/a/b"), asIRI("/a/b"),  ""); }
    @Test public void relative_path_301() { test_relative(asIRI("/a/b"), asIRI("/a/b/"), "b/"); }
    @Test public void relative_path_302() { test_relative(asIRI("/a/b"), asIRI("/a"), "/a"); }
    @Test public void relative_path_303() { test_relative(asIRI("/a/b"), asIRI("/a/"), "."); }
    @Test public void relative_path_304() { test_relative(asIRI("/a/b"), asIRI("/a/z"), "z"); }
    @Test public void relative_path_305() { test_relative(asIRI("/a/b"), asIRI("/a/z/"), "z/"); }
    @Test public void relative_path_306() { test_relative(asIRI("/a/b"), asIRI("/z"), "/z"); }
    @Test public void relative_path_307() { test_relative(asIRI("/a/b"), asIRI("/z/"), "/z/"); }
    @Test public void relative_path_308() { test_relative(asIRI("/a/b/"), asIRI("/a/z/e"), "../z/e"); }

    // Special handling case 1
    @Test public void relative_path_400() { test_relative(asIRI("/a/b/"), asIRI("/a/b"), "../b");}
    @Test public void relative_path_401() { test_relative(asIRI("/a/b/"), asIRI("/a/b/"), ""); }
    @Test public void relative_path_402() { test_relative(asIRI("/a/b/"), asIRI("/a"), "/a"); }
    @Test public void relative_path_403() { test_relative(asIRI("/a/b/"), asIRI("/a/"), ".."); }
    @Test public void relative_path_404() { test_relative(asIRI("/a/b/"), asIRI("/z"), "/z"); }
    @Test public void relative_path_405() { test_relative(asIRI("/a/b/"), asIRI("/z/"), "/z/"); }

    @Test public void relative_path_500() { test_relative(asIRI("/"), asIRI("/a"),   "a"); }
    @Test public void relative_path_501() { test_relative(asIRI("/"), asIRI("/a/"),  "a/"); }
    @Test public void relative_path_502() { test_relative(asIRI("/"), asIRI("/a/b"), "a/b"); }
    @Test public void relative_path_503() { test_relative(asIRI("/"), asIRI("/a/b/"), "a/b/"); }
    @Test public void relative_path_504() { test_relative(asIRI("/a/b/"), asIRI("/a/z/e"), "../z/e"); }

    @Test public void relative_path_600() { test_relative(asIRI("/a/b/c"), asIRI("/a/z"), "../z"); }
    // Grandparent
    @Test public void relative_path_601() { test_relative(asIRI("/a/b/c/"), asIRI("/a/z"), "/a/z");}
    @Test public void relative_path_602() { test_relative(asIRI("/a/b/c"), asIRI("/a"), "/a"); }
    @Test public void relative_path_603() { test_relative(asIRI("/a/b/c"), asIRI("/a/"), ".."); }

    // #45 jena-iri ../../c
    @Test public void relative_path_700() { test_relative(asIRI("/a/b/c/d/"), asIRI("/a/b/c"), "/a/b/c"); }
    // Special handling case 2
    @Test public void relative_path_701() { test_relative(asIRI("/a/b/c/d/"), asIRI("/a/b/c/"), ".."); }
    // jena-iri does grandparent.
    @Test public void relative_path_702() { test_relative(asIRI("/a/b/c/d/"), asIRI("/a/b/e"), "/a/b/e"); }
    @Test public void relative_path_703() { test_relative(asIRI("/a/b/c/d/"), asIRI("/a/b"), "/a/b"); }
    // jena-iri does grandparent.
    @Test public void relative_path_704() { test_relative(asIRI("/a/b/c/d/"), asIRI("/a/b/"), "/a/b/");}
    @Test public void relative_path_705() { test_relative(asIRI("/a/b/c/d/"), asIRI("/a/"), "/a/"); }
    @Test public void relative_path_706() { test_relative(asIRI("/a/b/c/d/"), asIRI("/a"), "/a"); }

    // Special handling case 1
    @Test public void relative_path_800() { test_relative(asIRI("/a/b/c/d"), asIRI("/a/b/c"), "../c");}
    @Test public void relative_path_801() { test_relative(asIRI("/a/b/c/d"), asIRI("/a/b/c/"), "."); }
    @Test public void relative_path_802() { test_relative(asIRI("/a/b/c/d"),  asIRI("/a/b/e"), "../e"); }
    // jena-iri does grandparent.
    @Test public void relative_path_803() { test_relative(asIRI("/a/b/c/d"), asIRI("/a/b"), "/a/b"); }
    // Special handling case 2
    @Test public void relative_path_804() { test_relative(asIRI("/a/b/c/d"), asIRI("/a/b/"), ".."); }
    // jena-iri does grandparent.
    @Test public void relative_path_805() { test_relative(asIRI("/a/b/c/d"), asIRI("/a/"), "/a/");}
    @Test public void relative_path_806() { test_relative(asIRI("/a/b/c/d"), asIRI("/a"), "/a"); }
    @Test public void relative_path_807() { test_relative(asIRI("/a/b/c/d"), asIRI("/a/b/x"), "../x"); }
    @Test public void relative_path_808() { test_relative(asIRI("/a/b/c/d"), asIRI("/a/b/x/"), "../x/"); }
    // jena-iri does grandparent.
    @Test public void relative_path_809() { test_relative(asIRI("/a/b/c/d"), asIRI("/a/x/"), "/a/x/"); }
    // jena-iri does grandparent.
    @Test public void relative_path_810() { test_relative(asIRI("/a/b/c/d"), asIRI("/a/x"), "/a/x"); }

    @Test public void relative_path_900() { test_relative(asIRI("/a/b/c/d/e"), asIRI("/a/b"), "/a/b"); }
    // jena-iri does grandparent.
    @Test public void relative_path_901() { test_relative(asIRI("/a/b/c/d/e"), asIRI("/a/b/"), "/a/b/");}
    @Test public void relative_path_902() { test_relative(asIRI("/a/b/c/d/e"), asIRI("/a/"), "/a/"); }
    @Test public void relative_path_903() { test_relative(asIRI("/a/b/c/d/e"), asIRI("/a"), "/a"); }

    private static void test_relative(String baseIRI, String targetIRI, String expected) {
        IRI3986 base = IRI3986.create(baseIRI);
        IRI3986 target = IRI3986.create(targetIRI);
        IRI3986 rel = base.relativize(target);
        String relStr = (rel != null ) ? rel.str() : null;

        String jenairiRelative = calcJenaIRI(baseIRI, targetIRI);

        if ( ! Objects.equals(relStr, expected) ) {

            System.out.printf("Fail: base=%-20s : iri=%-20s => got: %s, expected: %s, alt:%s \n", base, target,
                              enclose(relStr), enclose(expected), enclose(jenairiRelative));
            assertEquals(relStr, expected);
            return;
        }

        // null to default.
        String relStr2 = (relStr != null ) ? relStr : targetIRI;

        // Check with jena-iri -- skip grandparent
        //if ( ! jenairiRelative.startsWith("../..") )
        {
            if ( ! Objects.equals(relStr2, jenairiRelative) ) {
                assertEquals(relStr2, jenairiRelative);
                System.out.printf("Diff: base=%-20s : iri=%-20s => got: %s, jena-iri: %s\n", base, target, enclose(relStr2), enclose(jenairiRelative));
            }
        }

        IRI3986 iri = base.resolve(rel);
        assertEquals(iri, target);
    }

    private static int RelativizeFlags = ABSOLUTE | SAMEDOCUMENT | CHILD | PARENT; //| GRANDPARENT | NETWORK
    /** Calculate the relative URI using using jena-iri. */
    private static String calcJenaIRI(String basePath, String path) {
        // jena-iri does not return null. It returns the target IRI if there is no appropriate relative IRI.
        // Calculating using jena-iri. That needs the absolute "http://example" part.
        // IRI.GRANDPARENT : jena-iri includes relativizing with leading "../..".
        org.apache.jena.iri.IRI iriBase = IRIFactory.iriImplementation().create(basePath);
        org.apache.jena.iri.IRI jenaIriRelativize = iriBase.relativize(path, RelativizeFlags);
        String jenaIriRelativizeStr = jenaIriRelativize.toString();
        return jenaIriRelativizeStr;
    }

    private static String asIRI(String path) {
        return "http://example"+path;
    }


    private static String enclose(String x) {
        if ( x == null )
            return "<null>";
        return "|"+x+"|";
    }


}
