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

/** Test of the machinery for numbers and derived types.
 * @see TestIntegerRange
 */
public class TestNumber {
    @Test public void integer_01() { LibTestXSD.valid("0", XSD.xsdInteger) ; }
    @Test public void integer_02() { LibTestXSD.valid("+0", XSD.xsdInteger) ; }
    @Test public void integer_03() { LibTestXSD.valid("+0", XSD.xsdInteger) ; }
    
    @Test public void integer_04() { LibTestXSD.valid("9999999999999999999999999999999999999999999999999999999", XSD.xsdInteger) ; }
    @Test public void integer_05() { LibTestXSD.valid("+9999999999999999999999999999999999999999999999999999999", XSD.xsdInteger) ; }
    @Test public void integer_06() { LibTestXSD.valid("-9999999999999999999999999999999999999999999999999999999", XSD.xsdInteger) ; }
    
    @Test public void integer_10() { LibTestXSD.invalid("1.1", XSD.xsdInteger) ; }
    @Test public void integer_11() { LibTestXSD.invalid("1e0", XSD.xsdInteger) ; }
    @Test public void integer_12() { LibTestXSD.invalid("", XSD.xsdInteger) ; }
    @Test public void integer_13() { LibTestXSD.invalid(" 1 ", XSD.xsdInteger) ; }

    @Test public void double_01() { LibTestXSD.valid("0", XSD.xsdDouble) ; }
    @Test public void double_02() { LibTestXSD.valid("1e0", XSD.xsdDouble) ; }
    @Test public void double_03() { LibTestXSD.valid("1.1e1", XSD.xsdDouble) ; }
    @Test public void double_04() { LibTestXSD.valid("-1.1e1", XSD.xsdDouble) ; }
    @Test public void double_05() { LibTestXSD.valid("-1e+2", XSD.xsdDouble) ; }
    @Test public void double_06() { LibTestXSD.valid("-2e-1", XSD.xsdDouble) ; }
    @Test public void double_07() { LibTestXSD.valid("NaN", XSD.xsdDouble) ; }
    @Test public void double_08() { LibTestXSD.valid("INF", XSD.xsdDouble) ; }
    @Test public void double_09() { LibTestXSD.valid("-INF", XSD.xsdDouble) ; }
    @Test public void double_10() { LibTestXSD.valid("+INF", XSD.xsdDouble) ; }
    
    @Test public void float_01() { LibTestXSD.valid("0", XSD.xsdFloat) ; }
    @Test public void float_02() { LibTestXSD.valid("1e0", XSD.xsdFloat) ; }
    @Test public void float_03() { LibTestXSD.valid("1.1e1", XSD.xsdFloat) ; }
    @Test public void float_04() { LibTestXSD.valid("-1.1e1", XSD.xsdFloat) ; }
    @Test public void float_05() { LibTestXSD.valid("-1e+2", XSD.xsdFloat) ; }
    @Test public void float_06() { LibTestXSD.valid("-2e-1", XSD.xsdFloat) ; }
    @Test public void float_07() { LibTestXSD.valid("NaN", XSD.xsdFloat) ; }
    @Test public void float_08() { LibTestXSD.valid("INF", XSD.xsdFloat) ; }
    @Test public void float_09() { LibTestXSD.valid("-INF", XSD.xsdFloat) ; }
    @Test public void float_10() { LibTestXSD.valid("+INF", XSD.xsdFloat) ; }

    @Test public void decimal_01() { LibTestXSD.valid("0", XSD.xsdDecimal) ; }
    @Test public void decimal_02() { LibTestXSD.valid("-0", XSD.xsdDecimal) ; }
    @Test public void decimal_03() { LibTestXSD.valid("+0", XSD.xsdDecimal) ; }

    @Test public void decimal_04() { LibTestXSD.valid("0.0", XSD.xsdDecimal) ; }
    @Test public void decimal_05() { LibTestXSD.valid("-0.0", XSD.xsdDecimal) ; }
    @Test public void decimal_06() { LibTestXSD.valid("+0.0", XSD.xsdDecimal) ; }

    @Test public void decimal_07() { LibTestXSD.valid("1.1", XSD.xsdDecimal) ; }
    @Test public void decimal_08() { LibTestXSD.valid("-1.1", XSD.xsdDecimal) ; }
    @Test public void decimal_09() { LibTestXSD.valid("+1.1", XSD.xsdDecimal) ; }
    
    @Test public void decimal_10() { LibTestXSD.valid("1.", XSD.xsdDecimal) ; }
    @Test public void decimal_11() { LibTestXSD.invalid("1e0", XSD.xsdDecimal) ; }

    @Test public void byte_01() { LibTestXSD.valid("0", XSD.xsdByte) ; }
    @Test public void byte_02() { LibTestXSD.invalid("999", XSD.xsdByte) ; }
    @Test public void byte_03() { LibTestXSD.valid("127", XSD.xsdByte) ; }
    @Test public void byte_04() { LibTestXSD.invalid("128", XSD.xsdByte) ; }
    @Test public void byte_05() { LibTestXSD.valid("-128", XSD.xsdByte) ; }
    @Test public void byte_06() { LibTestXSD.invalid("-129", XSD.xsdByte) ; }

    @Test public void ubyte_01() { LibTestXSD.valid("0", XSD.xsdUnsignedByte) ; }
    @Test public void ubyte_02() { LibTestXSD.invalid("999", XSD.xsdByte) ; }
    @Test public void ubyte_03() { LibTestXSD.valid("127", XSD.xsdByte) ; }
    @Test public void ubyte_04() { LibTestXSD.invalid("128", XSD.xsdByte) ; }
    @Test public void ubyte_05() { LibTestXSD.valid("-128", XSD.xsdByte) ; }
    @Test public void ubyte_06() { LibTestXSD.invalid("-129", XSD.xsdByte) ; }
    
    @Test public void precisionDecimal_01()     { LibTestXSD.valid("0.0", XSD.xsdPrecisionDecimal) ; }
    @Test public void precisionDecimal_02()     { LibTestXSD.invalid("", XSD.xsdPrecisionDecimal) ; }
    @Test public void precisionDecimal_03()     
    { LibTestXSD.valid("9999999999999999999999999999999999999999999999999999999.9999999999999999999999999", XSD.xsdPrecisionDecimal) ; }
    
    @Test public void precisionDecimal_04()     { LibTestXSD.valid("NaN", XSD.xsdPrecisionDecimal) ; }
    @Test public void precisionDecimal_05()     { LibTestXSD.valid("-INF", XSD.xsdPrecisionDecimal) ; }
    @Test public void precisionDecimal_06()     { LibTestXSD.valid("+INF", XSD.xsdPrecisionDecimal) ; }
    @Test public void precisionDecimal_07()     { LibTestXSD.valid("INF", XSD.xsdPrecisionDecimal) ; }

    @Test public void precisionDecimal_08()     { LibTestXSD.invalid("Infinity", XSD.xsdPrecisionDecimal) ; }

}

