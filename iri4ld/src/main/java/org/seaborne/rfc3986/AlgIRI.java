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

/**
 * Algorithms on IRIs.
 *
 * @see AlgResolveIRI
 * @see AlgRelativizeIRI
 */
public class AlgIRI {
    // Misc algorithms

    /**
     * Ensure an IRI to one suitable for a base else return null.
     */
    public static IRI3986 toBase(IRI3986 iri) {
        if ( ! validBase(iri) )
            return null;
        return iri;
    }


    /** Validate for use as a base IRI. */
    /*package*/ static boolean validBase(IRI base) {
        return base.hasScheme() && ! base.hasQuery();
    }
}
