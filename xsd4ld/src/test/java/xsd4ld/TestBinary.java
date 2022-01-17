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

import static xsd4ld.LibTestXSD.* ;
import org.junit.Test ;
import org.seaborne.xsd4ld.XSD;

public class TestBinary {
    // No whitespace allowed
    @Test public void hexBinary_00() { valid("", XSD.xsdHexBinary) ; }
    @Test public void hexBinary_01() { valid("ABCD", XSD.xsdHexBinary) ; }
    @Test public void hexBinary_02() { invalid("X01", XSD.xsdHexBinary) ; }
    @Test public void hexBinary_03() { invalid("FG", XSD.xsdHexBinary) ; }
    @Test public void hexBinary_04() { invalid("ABC", XSD.xsdHexBinary) ; }
    @Test public void hexBinary_05() { invalid(" AB ", XSD.xsdHexBinary) ; }

    @Test public void base64Binary_00() { valid("", XSD.xsdBase64Binary) ; }
    @Test public void base64Binary_01() { valid("0F+40A==", XSD.xsdBase64Binary) ; }
    @Test public void base64Binary_02() { valid("0 F + 4 0 A = =", XSD.xsdBase64Binary) ; }
    @Test public void base64Binary_03() { valid(" 0F+40A==", XSD.xsdBase64Binary) ; }
    @Test public void base64Binary_04() { valid("0F+40A== ", XSD.xsdBase64Binary) ; }

}

