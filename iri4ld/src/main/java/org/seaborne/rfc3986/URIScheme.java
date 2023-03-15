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

import java.util.Locale;

/**
 * URI scheme
 * <ul>
 * <li><a href="https://www.iana.org/assignments/uri-schemes/uri-schemes.xhtml">URI Registrations</a>
 * <li><a href="https://www.iana.org/assignments/urn-namespaces/urn-namespaces.xhtml">URN Registrations</a>
 * </ul>
 */
public enum URIScheme {
    // "scheme" is resolved for URI general parse conditions.
    HTTP("http"),
    HTTPS("https"),
    URN("urn"),
    // Pseudo scheme
    URN_UUID("urn:uuid"),
    // It's not officially registered but may be found in the wild.
    UUID("uuid"),
    DID("did"),
    FILE("file"),
    // RFC 7595 and registered.
    EXAMPLE("example")
    ;

    private final String name;
    private final String prefix;

    public static URIScheme get(String name) {
        if ( name == null )
            return null;
        String nameLC = name.toLowerCase(Locale.ROOT);
        if ( nameLC.endsWith(":") )
            nameLC = nameLC.substring(0, nameLC.length()-1);
        return URIScheme.valueOf(URIScheme.class, nameLC);
    }

    private URIScheme(String name) {
        this(name, name+":");
    }

    private URIScheme(String name, String prefix) {
        this.name = name;
        this.prefix = prefix;
    }

    /** Match case insensitively */
    public static boolean matches(String iriStr, URIScheme scheme) {
        return scheme.fromScheme(iriStr);
    }

    /** Match case sensitively */
    public static boolean matchesExact(String iriStr, URIScheme scheme) {
        return iriStr.startsWith(scheme.getPrefix());
    }

    // Test the IRI string, this does not touch the scheme name slot.
    public final boolean fromScheme(String iriStr) {
        return iriStr.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    /** Scheme name */
    public String getName() {
        return name;
    }

    /**
     * Initial part of the URI scheme that identifies this scheme.
     * In practice this is the schema name in lower case followed by ':'.
     */
    public String getPrefix() {
        return prefix;
    }
}
