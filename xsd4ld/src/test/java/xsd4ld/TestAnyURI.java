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

import static xsd4ld.LibTestXSD.invalid ;
import static xsd4ld.LibTestXSD.valid ;

import org.junit.Test ;
import org.seaborne.xsd4ld.XSD;

public class TestAnyURI {
    
    @Test public void anyURI_01() { valid("", XSD.xsdAnyURI) ; }
    @Test public void anyURI_02() { valid("abc def", XSD.xsdAnyURI) ; }
    
    // Includes surrogate
    @Test public void anyURI_03() { valid("abc\uDE00def", XSD.xsdAnyURI) ; }
    
    @Test public void anyURI_04() { invalid("\u0000", XSD.xsdAnyURI) ; }
    @Test public void anyURI_05() { invalid("\uFFFF", XSD.xsdAnyURI) ; }
    @Test public void anyURI_06() { invalid("\uFFFE", XSD.xsdAnyURI) ; }
}

