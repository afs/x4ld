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

import org.junit.Test ;
import static org.junit.Assert.* ;

/** Test of the machinery for strings and derived types.
 * @see TestIntegerRange
 */
public class TestString {
    @Test public void string_01() { LibTestXSD.valid("abc", XSD.xsdString); }  
    @Test public void string_02() { LibTestXSD.valid("\t", XSD.xsdString); }
    
    @Test public void normalizedString_01() { LibTestXSD.valid("abc", XSD.xsdNormalizedString) ; }
    @Test public void normalizedString_02() { LibTestXSD.invalid("\t", XSD.xsdNormalizedString) ; }
    @Test public void normalizedString_03() { LibTestXSD.invalid("abc\n", XSD.xsdNormalizedString) ; }
    @Test public void normalizedString_04() { LibTestXSD.invalid("\rdef", XSD.xsdNormalizedString) ; }

    @Test public void normalizedString_05() { LibTestXSD.valid(" def", XSD.xsdNormalizedString) ; }
    @Test public void normalizedString_06() { LibTestXSD.valid("def ", XSD.xsdNormalizedString) ; }
    @Test public void normalizedString_07() { LibTestXSD.valid("abc def", XSD.xsdNormalizedString) ; }
    @Test public void normalizedString_08() { LibTestXSD.valid("abc  def", XSD.xsdNormalizedString) ; }
    
    @Test public void token_01() { LibTestXSD.valid("abc", XSD.xsdToken) ; }
    @Test public void token_02() { LibTestXSD.invalid("\t", XSD.xsdToken) ; }
    @Test public void token_03() { LibTestXSD.invalid("abc\n", XSD.xsdToken) ; }
    @Test public void token_04() { LibTestXSD.invalid("\rdef", XSD.xsdToken) ; }

    @Test public void token_05() { LibTestXSD.invalid(" def", XSD.xsdToken) ; }
    @Test public void token_06() { LibTestXSD.invalid("def ", XSD.xsdToken) ; }
    @Test public void token_07() { LibTestXSD.valid("abc def", XSD.xsdToken) ; }
    @Test public void token_08() { LibTestXSD.invalid("abc  def", XSD.xsdToken) ; }
    
    // The language tag parser is tested separately.
    @Test public void language_01() { LibTestXSD.valid("en-uk", XSD.xsdLanguage) ; }
    @Test public void language_02() { LibTestXSD.valid("en-UK", XSD.xsdLanguage) ; }
    @Test public void language_10() { LibTestXSD.invalid(" en-UK", XSD.xsdLanguage) ; }
    @Test public void language_11() { LibTestXSD.invalid("en-UK ", XSD.xsdLanguage) ; }
    @Test public void language_12() { LibTestXSD.invalid("en UK", XSD.xsdLanguage) ; }
    @Test public void language_13() { LibTestXSD.invalid("abcdefghijkl-xyz", XSD.xsdLanguage) ; }


    // Using spec regex
    @Test public void language_01a() { 
        assertTrue(XSD.xsdLanguage.getRegex().matcher("en-UK").matches()) ;
    }
    @Test public void language_02a() {
        assertTrue(XSD.xsdLanguage.getRegex().matcher("en").matches()) ;
    }

    
    @Test public void language_10a() { 
        assertFalse(XSD.xsdLanguage.getRegex().matcher(" en-UK").matches()) ;
    }
        
    @Test public void language_11a() { 
        assertFalse(XSD.xsdLanguage.getRegex().matcher("en-UK ").matches()) ;
    }
    @Test public void language_12a() {
        assertFalse(XSD.xsdLanguage.getRegex().matcher("en UK").matches()) ;
    }
    
    @Test public void language_13a() {
        assertFalse(XSD.xsdLanguage.getRegex().matcher("abcdefghijkl-xyz").matches()) ;
    }


}

