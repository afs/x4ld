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
 * An Error handler captures the policy for dealing with warnings, errors and
 * fatal errors. Fatal errors mean termination of processing and must throw
 * an exception. Errors and warnings may throw an exception to terminate
 * processing or may return after, for example, logging a message. The exact
 * policy is determined the error handler itself.
 */
@FunctionalInterface
public interface ErrorHandler
{
    /** Report a warning. This method may return. */
    public default void warning(String message) { }

    /** Report an error : Must not return (depends on error handler policy). */
    public void error(String message) ;
}
