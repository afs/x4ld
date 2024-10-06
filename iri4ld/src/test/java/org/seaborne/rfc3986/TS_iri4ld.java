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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses( {
    TestRFC3986Syntax.class,
    TestIRI3986.class,

    // Old material - these will removed
    // jena-iri comparision - alignment now incorporated into the other tests.
    Test_X_RFC3986_Scheme_Full.class,
    Test_X_RFC3986_Full.class,
    // Duplicated by TestRFC3986_Syntax
    Test_X_ParseIRIComponents.class,

    TestURISchemes.class,
    TestResolve.class,
    TestNormalize.class,
    TestRelative.class,
    TestRelative2.class,
    TestRelative3.class,
    TestBuild.class,
    TestParseIPv4Address.class,
    TestParseIPv6Address.class,
    TestParseDID.class,
    TestParseDNS.class,
    TestParseURNComponents.class,
    TestParseOID.class,
    TestSystem3986.class
} )

public class TS_iri4ld { }
