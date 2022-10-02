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

public class Violations {
    Violations(String iriStr, URIScheme schema, Issue violation, String message) {}

    public enum Issue {
        ParseError,
        // General
        iri_scheme_name_is_not_lowercase,
        iri_percent_not_uppercase,
        iri_host_not_lowercase,

        // http/https
        http_no_host,
        http_empty_host,
        http_empty_port,
        http_not_advised,
        http_userinfo,
        http_well_known_port,
        // urn:uuid and uuid
        urn_uuid_bad_pattern,
        uuid_bad_pattern,
        uuid_has_query,
        uuid_has_fragment,
        uuid_not_lowercase,
        // urn
        urn_bad_pattern,
        urn_nid,
        urn_nss,
        urn_bad_query,
        urn_bad_fragment,
        // file
        file_bad_form,
        // did
        did_bad_syntax
    }
}
