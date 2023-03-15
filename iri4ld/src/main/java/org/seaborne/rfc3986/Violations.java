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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Violations {

    /** System default settings for violation levels */
    public static final Map<Issue, Severity> systemLevels = systemSettings();
    /** System default settings for violation levels */
    public static final Map<Issue, Severity> levels = new HashMap<>(systemLevels);

    public static Severity getSeverity(Issue issue) {
        return levels.getOrDefault(issue, Severity.INVALID);
    }

    public static void setSeverity(Issue issue, Severity severity) {
        Objects.requireNonNull(issue);
        Objects.requireNonNull(severity);
        if ( severity == Severity.INVALID )
            throw new IllegalArgumentException("Can't not set Severity.INVALID");
        levels.put(issue, severity);
    }

    public static void reset() {
        levels.clear();
        levels.putAll(systemLevels);
    }

    private static Map<Issue, Severity> systemSettings() {
        Map<Issue, Severity> systemLevels = new HashMap<>();
        systemLevels.put(Issue.ParseError,                        Severity.INVALID);
        // General
        systemLevels.put(Issue.iri_percent_not_uppercase,         Severity.WARNING);
        systemLevels.put(Issue.iri_host_not_lowercase,            Severity.WARNING);
        // Scheme
        systemLevels.put(Issue.iri_scheme_name_is_not_lowercase,  Severity.WARNING);
        systemLevels.put(Issue.iri_scheme_expected,               Severity.ERROR);
        systemLevels.put(Issue.iri_scheme_unexpected,             Severity.ERROR);
        // http/https
        systemLevels.put(Issue.http_no_host,                      Severity.ERROR);
        systemLevels.put(Issue.http_empty_host,                   Severity.ERROR);
        systemLevels.put(Issue.http_empty_port,                   Severity.ERROR);
        systemLevels.put(Issue.http_port_not_advised,             Severity.WARNING);

        systemLevels.put(Issue.http_userinfo,                     Severity.ERROR);
        systemLevels.put(Issue.http_password,                     Severity.ERROR);
        systemLevels.put(Issue.http_omit_well_known_port,         Severity.ERROR);
        // urn:uuid and uuid
        systemLevels.put(Issue.urn_uuid_bad_pattern,              Severity.ERROR);
        systemLevels.put(Issue.uuid_bad_pattern,                  Severity.ERROR);
        systemLevels.put(Issue.uuid_has_query,                    Severity.ERROR);
        systemLevels.put(Issue.uuid_has_fragment,                 Severity.ERROR);
        systemLevels.put(Issue.uuid_not_lowercase,                Severity.ERROR);
        // urn
        systemLevels.put(Issue.urn_bad_pattern,                   Severity.ERROR);
        systemLevels.put(Issue.urn_nid,                           Severity.ERROR);
        systemLevels.put(Issue.urn_nss,                           Severity.ERROR);
        systemLevels.put(Issue.urn_bad_query,                     Severity.ERROR);
        systemLevels.put(Issue.urn_bad_fragment,                  Severity.ERROR);
        systemLevels.put(Issue.urn_non_ascii_character,           Severity.ERROR);
        // file
        systemLevels.put(Issue.file_bad_form,                     Severity.WARNING);
        systemLevels.put(Issue.file_relative_path,                Severity.WARNING);
        // did
        systemLevels.put(Issue.did_bad_syntax,                    Severity.ERROR);

        checkComplete(systemLevels);

        return Map.copyOf(systemLevels);
    }

    private static void checkComplete() {
        checkComplete(systemLevels);
    }

    public static void checkComplete(Map<Issue, Severity> levels) {
        Set<Issue> keys = levels.keySet();
        for ( Issue issue : Issue.values()) {
            if ( ! keys.contains(issue) ) {
                System.err.println("Missing entry for "+issue);
            }
        }
    }

}
