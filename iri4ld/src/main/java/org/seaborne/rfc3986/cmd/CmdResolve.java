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

package org.seaborne.rfc3986.cmd;

import org.seaborne.rfc3986.IRI3986;
import org.seaborne.rfc3986.IRIParseException;
import org.seaborne.rfc3986.RFC3986;

public class CmdResolve {
    public static void main(String... args) {
        if ( args.length != 2 ) {
            System.err.println("request two arguments - base and IRI to resolve.");
            System.exit(1);
        }

        String baseStr = fixup(args[0]);
        String relStr = fixup(args[1]);

        IRI3986 base;
        try {
            base = RFC3986.create(baseStr);
        } catch (IRIParseException ex) {
            System.err.println("Bad base IRI: " + ex.getMessage());
            System.exit(1);
            base = null;
        }

        IRI3986 rel;
        try {
            rel = RFC3986.create(relStr);
        } catch (IRIParseException ex) {
            System.err.println("Bad IRI: " + ex.getMessage());
            System.exit(1);
            rel = null;
        }

        if ( ! base.isAbsolute() ) {
            System.err.println("Base must be an absolute IRI: '" +base+"'");
            System.exit(1);
        }

        IRI3986 result = base.resolve(rel);
        System.out.println("Base:     "+base);
        System.out.println("IRI:      "+rel);
        System.out.println("Resolved: "+result);

        try {
            IRI3986 iri = result;

//                System.out.println("Absolute: "+iri.isAbsolute());
//                System.out.println("Relative: "+iri.isRelative());
//                System.out.println("Hierarchical: "+iri.isHierarchical());
//                System.out.println("Rootless: "+iri.isRootless());

            System.out.printf("%s|%s|  ", "Scheme",     iri.scheme());
            System.out.printf("%s|%s|  ", "Authority",  iri.authority());
            System.out.printf("%s|%s|  ", "Path",       iri.path());
            System.out.printf("%s|%s|  ", "Query",      iri.query());
            System.out.printf("%s|%s|", "Fragment",   iri.fragment());
            System.out.println();
            if ( iri.hasViolations() ) {
                iri.forEachViolation(v->{
                    System.out.println();
                    System.err.println("Scheme specific error:");
                    System.err.println("    "+v.message);
                });
            }
        } catch (IRIParseException ex) {
            System.err.println(ex.getMessage());
        }
    }

    static String fixup(String iriStr) {
        if ( iriStr.startsWith("<") && iriStr.endsWith(">") )
            iriStr = iriStr.substring(1, iriStr.length()-1);
        return iriStr;
    }
}
