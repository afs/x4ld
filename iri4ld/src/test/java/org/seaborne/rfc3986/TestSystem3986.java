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

import static org.junit.Assert.assertTrue;

import java.util.function.Consumer;

import org.junit.Test;

/** Test of machinery */
public class TestSystem3986 {

    private static class MarkerException extends RuntimeException {}

    @Test(expected = MarkerException.class)
    public void violations_01() {
        // Warning as exception
        IRI3986 iri = IRI3986.createAny("HTTP://host/path");
        assertTrue(iri.hasViolations());
        Consumer<String> x = (msg)-> { throw new MarkerException(); };
        ErrorHandler eh = ErrorHandler.create(x, x);
        iri.toHandler(eh);
    }

    @Test
    public void violations_02() {
        // Warning as information
        IRI3986 iri = IRI3986.createAny("HTTP://host/path");
        assertTrue(iri.hasViolations());
        Consumer<String> x = (msg)-> { throw new MarkerException(); };
        ErrorHandler eh = ErrorHandler.create(x, null);
        iri.toHandler(eh);
    }
}
