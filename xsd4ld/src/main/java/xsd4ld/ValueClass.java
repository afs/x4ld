/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *  See the NOTICE file distributed with this work for additional
 *  information regarding copyright ownership.
 */

package xsd4ld;

/** The space in which arithmetic and other operations are done.
 *  For example, xsd:byte + xsd:byte is an xsd:inetger so the space is INTEGER
 *  This is not directly related to derived types.
 *  Code will need to know that INTEGER + DOUBLE is DOUBLE.
 */
public enum ValueClass {
    DECIMAL, INTEGER, DOUBLE, FLOAT, 
    STRING, BOOLEAN,
    DATETIME, DURATION,
    BINARY,
    // anyType, anySimpleType, anyAtomicType 
    ANY
}
