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

public class TestBoolean {
    @Test public void boolean_01() { LibTestXSD.valid("true", XSD.xsdBoolean) ; }
    @Test public void boolean_02() { LibTestXSD.valid("false", XSD.xsdBoolean) ; }
    @Test public void boolean_03() { LibTestXSD.valid("1", XSD.xsdBoolean) ; }
    @Test public void boolean_04() { LibTestXSD.valid("0", XSD.xsdBoolean) ; }
    @Test public void boolean_05() { LibTestXSD.invalid("T", XSD.xsdBoolean) ; }
    @Test public void boolean_06() { LibTestXSD.invalid("F", XSD.xsdBoolean) ; }
    @Test public void boolean_07() { LibTestXSD.invalid("TRUE", XSD.xsdBoolean) ; }
    @Test public void boolean_08() { LibTestXSD.invalid("FLASE", XSD.xsdBoolean) ; }
}

