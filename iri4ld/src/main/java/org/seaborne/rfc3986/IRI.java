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

public interface IRI {
    // Operations like relativize and resolve left to external functions.

    /** The IRI in string form. This is guaranteed to parse to an equals IRI. */
    public String str();

//    /** Human-readable appearance. Use {@link #str()} to a string to use in code. */
//    @Override public String toString();

    /** Java hashCode. */
    @Override
    public int hashCode();

    /** Java equals. IRI implementations must provide value-based equaliaty and hashCode.
     * That is, two IRI objects with the same components are ".equals"and have the same ".hashCode".
     * (Like java.lang.String where "same chars" means "equals".)
     * Specifically, if two IRI objects are parsed from the same string or return the same "str()"
     * they are .equals ahd hance also have the same .hashCode
     */
    @Override
    public boolean equals(Object obj);

    public default boolean hasScheme() { return getScheme() == null; }
    public String getScheme();

    public default boolean hasAuthority() { return getAuthority() != null; }
    public String getAuthority();

    public default boolean hasUserInfo() { return getUserInfo() != null; }
    public String getUserInfo();

    public default boolean hasHost() { return getHost() != null; }
    public String getHost();

    public default boolean hasPort() { return getPort() != null; }
    public String getPort();

    public default boolean hasPath() { return getPath() != null; }
    public String getPath();

    public String[] getPathSegments();

    public default boolean hasQuery() { return getQuery() != null; }
    public String getQuery();

    public default boolean hasFragment() { return getFragment() != null; }
    public String getFragment();

    /** <a href="https://tools.ietf.org/html/rfc3986#section-4.3">RFC 3986, Section 4.3</a> */
    public default boolean isAbsolute() {
        // With scheme, without fragment
        return hasScheme() && ! hasFragment();
    }

    /**
     * <a href="https://tools.ietf.org/html/rfc3986#section-4.2">RFC 3986, Section 4.2</a>.
     * This is not "! isAbsolute()".
     *  */
    public default boolean isRelative() {
        return ! hasScheme();
    }

    /**
     * <a href="https://tools.ietf.org/html/rfc3986#section-3">RFC 3986, Section 3</a>.
     * IRI has a scheme, no authority (no //) and is path-rootless (does not start with /)
     * e.g. URN's.
     */
    public default boolean isRootless() {
        return hasScheme() && !hasAuthority() && rootlessPath();
    }

    private boolean rootlessPath() {
        if ( ! hasPath() )
            return false;
        String path = getPath();
        return firstChar(path) != '/';
    }

    private char firstChar(String str) {
        if ( str.isEmpty() )
            return 0xFFFF;
        return str.charAt(0);
    }

    /**
     * <a href="https://tools.ietf.org/html/rfc3986#section-1.2.3">RFC 3986, Section 1.2.3 : Hierarchical Identifiers</a>.
     */
    public default boolean isHierarchical() {
        return hasScheme() && hasAuthority() && hierarchicalPath();
    }

    private boolean hierarchicalPath() {
        if ( ! hasPath() )
            return false;
        String path = getPath();
        return firstChar(path) == '/';
    }

    /**
     * <a href="https://tools.ietf.org/html/rfc3986#section-6.2.2">RFC 3986, Section 6.2.2 : Syntax-Based Normalization.</a>.
     */
    public IRI normalize();

}
