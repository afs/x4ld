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

package langtag;

/*package*/ class LangTagLib {

    /** ASCII A-Z */
    /*package*/ static boolean isA2Z(int ch) {
        return range(ch, 'a', 'z') || range(ch, 'A', 'Z');
    }

    /** ASCII A-Z or 0-9 */
    /*package*/ static boolean isA2ZN(int ch) {
        return range(ch, 'a', 'z') || range(ch, 'A', 'Z') || range(ch, '0', '9');
    }

    private static boolean range(int ch, char a, char b) {
        return (ch >= a && ch <= b);
    }

}
