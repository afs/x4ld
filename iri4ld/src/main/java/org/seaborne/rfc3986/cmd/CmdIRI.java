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

import org.seaborne.rfc3986.*;

public class CmdIRI {
    public static void main(String... args) {
        if ( args.length == 0 ) {
            System.err.println("No iri string");
            System.exit(1);
        }

        ErrorHandler errorHandler = new ErrorHandler() {
            @Override
            public void warning(String message) {
                System.err.println("Scheme specific warning:");
                System.err.println("    "+message);
            }
            @Override
            public void error(String message) { throw new IRIParseException(message); }
        };
        SystemIRI3986.setErrorHandler(errorHandler);

        for (String iriStr : args ) {
            if ( iriStr.startsWith("<") && iriStr.endsWith(">") )
                iriStr = iriStr.substring(1, iriStr.length()-1);
            try {
                IRI3986 iri = RFC3986.create(iriStr);
                IRI3986 iri1 = iri.normalize();

                System.out.println(iriStr);
                System.out.println("      ==> "+iri) ;
//                System.out.println("Absolute: "+iri.isAbsolute());
//                System.out.println("Relative: "+iri.isRelative());
//                System.out.println("Hierarchical: "+iri.isHierarchical());
//                System.out.println("Rootless: "+iri.isRootless());
                if ( ! iri.equals(iri1) )
                    System.out.println("      ==> "+iri1) ;

                System.out.printf("%s|%s|  ", "Scheme",     iri.scheme());
                System.out.printf("%s|%s|  ", "Authority",  iri.authority());
                System.out.printf("%s|%s|  ", "Path",       iri.path());
                System.out.printf("%s|%s|  ", "Query",      iri.query());
                System.out.printf("%s|%s|", "Fragment",   iri.fragment());
                System.out.println();
                try {
                    iri.schemeSpecificRules();
                } catch (IRIParseException ex) {
                    System.out.println();
                    System.err.println("Scheme specific error:");
                    System.err.println("    "+ex.getMessage());
                }
            } catch (IRIParseException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
