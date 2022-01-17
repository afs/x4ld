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

import java.util.Arrays ;
import java.util.Collection ;

import org.junit.Test ;
import org.junit.runner.RunWith ;
import org.junit.runners.Parameterized ;
import org.seaborne.xsd4ld.XSD;
import org.seaborne.xsd4ld.XSDDatatype;

/** test of integers and all derived types */
@RunWith(Parameterized.class)

public class TestIntegerRange {
    @Parameterized.Parameters(name="{0}")
    public static Collection<Object[]> parameters() {
       return Arrays.asList(new Object[][] {
           { XSD.xsdInteger.getName(), XSD.xsdInteger, null, null, null, null }
           , { XSD.xsdLong.getName(), XSD.xsdLong, "-9223372036854775809", "-9223372036854775808", "9223372036854775807", "9223372036854775808" }
           , { XSD.xsdInt.getName(), XSD.xsdInt, "-2147483649", "-2147483648", "2147483647", "2147483648" }
           , { XSD.xsdShort.getName(), XSD.xsdShort, "-32769", "-32768", "32767", "32768" }
           , { XSD.xsdByte.getName(), XSD.xsdByte, "129", "-128", "127", "128" }
           
           , { XSD.xsdNonNegativeInteger.getName(), XSD.xsdNonNegativeInteger, "-1", "0", null, null }
           , { XSD.xsdNonPositiveInteger.getName(), XSD.xsdNonPositiveInteger, null, null, "0", "1" }
           
           , { XSD.xsdPositiveInteger.getName(), XSD.xsdPositiveInteger, "0", "1", null, null }
           , { XSD.xsdNegativeInteger.getName(), XSD.xsdNegativeInteger, null, null, "-1", "0" }
           
           , { XSD.xsdUnsignedLong.getName(), XSD.xsdUnsignedLong, "-1", "0", "18446744073709551615", "18446744073709551616" }
           , { XSD.xsdUnsignedInt.getName(), XSD.xsdUnsignedInt, "-1", "0", "4294967295", "4294967296" }
           , { XSD.xsdUnsignedShort.getName(), XSD.xsdUnsignedShort, "-1", "0", "65535", "65536" }
           , { XSD.xsdUnsignedByte.getName(), XSD.xsdUnsignedByte, "-1", "0", "255", "256" }
       });
    }
    
    final XSDDatatype type;
    final String minNo; 
    final String minYes; 
    final String maxYes; 
    final String maxNo;

    public TestIntegerRange(String name, XSDDatatype type, String minNo, String minYes, String maxYes, String maxNo) {
        super() ;
        this.type = type ;
        this.minNo = minNo ;
        this.minYes = minYes ;
        this.maxYes = maxYes ;
        this.maxNo = maxNo ;
    }

    
    @Test public void test() {
        if ( minNo != null)
            LibTestXSD.invalid(minNo, type) ;
        if ( minYes != null)
            LibTestXSD.valid(minYes, type) ;
        if ( maxYes != null)
            LibTestXSD.valid(maxYes, type) ;
        if ( maxNo != null)
            LibTestXSD.invalid(maxNo, type) ;
        
    }
//    
//    
//    
//    @Test public void integer_01() { valid("0", XSD.xsdInteger) ; }
//    @Test public void integer_02() { valid("+0", XSD.xsdInteger) ; }
//    @Test public void integer_03() { valid("+0", XSD.xsdInteger) ; }
//    
//    @Test public void integer_04() { valid("9999999999999999999999999999999999999999999999999999999", XSD.xsdInteger) ; }
//    @Test public void integer_05() { valid("+9999999999999999999999999999999999999999999999999999999", XSD.xsdInteger) ; }
//    @Test public void integer_06() { valid("-9999999999999999999999999999999999999999999999999999999", XSD.xsdInteger) ; }
//
//    
//    // Parameterized:
//    // string, type, minNo, minYes, maxYes, maxNo
//    
//    
//    @Test public void byte_01() { valid("0", XSD.xsdByte) ; }
//    @Test public void byte_02() { invalid("999", XSD.xsdByte) ; }
//    @Test public void byte_03() { valid("127", XSD.xsdByte) ; }
//    @Test public void byte_04() { invalid("128", XSD.xsdByte) ; }
//    @Test public void byte_05() { valid("-128", XSD.xsdByte) ; }
//    @Test public void byte_06() { invalid("-129", XSD.xsdByte) ; }
//
//    @Test public void ubyte_01() { valid("0", XSD.xsdUnsignedByte) ; }
//    @Test public void ubyte_02() { invalid("999", XSD.xsdByte) ; }
//    @Test public void ubyte_03() { valid("127", XSD.xsdByte) ; }
//    @Test public void ubyte_04() { invalid("128", XSD.xsdByte) ; }
//    @Test public void ubyte_05() { valid("-128", XSD.xsdByte) ; }
//    @Test public void ubyte_06() { invalid("-129", XSD.xsdByte) ; }
    
}

