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

import static xsd4ld.XSD.* ;
import static xsd4ld.LibTestXSD.* ;
import org.junit.Test ;

public class TestDuration {

    @Test public void duration_01() {
        valid("P1Y", xsdDuration) ;
    }

    @Test public void duration_02() {
        valid("-P1Y", xsdDuration) ;
    }

    @Test public void duration_03() {
        invalid("1Y", xsdDuration) ;
    }
    
    @Test public void duration_04() {
        invalid("-1Y", xsdDuration) ;
    }
    
    @Test public void duration_05() {
        invalid("P", xsdDuration) ;
    }
    
    @Test public void duration_06() {
        invalid("", xsdDuration) ;
    }
    
    @Test public void duration_07() {
        invalid("P1Y ", xsdDuration) ;
    }

    @Test public void duration_08() {
        invalid(" P1Y", xsdDuration) ;
    }

    @Test public void duration_09() {
        invalid("P 1Y", xsdDuration) ;
    }
    // xsd:yearMonthDuration
    @Test public void yearMonthDuration_01() {
        valid("P1Y", xsdDuration) ;
        valid("P1Y", xsdYearMonthDuration) ;
    }

    @Test public void yearMonthDuration_02() {
        valid("-P1M", xsdDuration) ;
        valid("-P1M", xsdYearMonthDuration) ;
    }

    @Test public void yearMonthDuration_03() {
        valid("P9Y10M", xsdYearMonthDuration) ;
        valid("P9Y10M", xsdYearMonthDuration) ;
    }

    @Test public void yearMonthDuration_04() {
        valid("P1Y1D", xsdDuration) ;
        invalid("P1Y1D", xsdYearMonthDuration) ;
    }

    @Test public void yearMonthDuration_05() {
        valid("P1YT1M", xsdDuration) ;
        invalid("P1YT1M", xsdYearMonthDuration) ;
    }

    @Test public void yearMonthDuration_06() {
        valid("P1D", xsdDuration) ;
        invalid("P1D", xsdYearMonthDuration) ;
    }

    // xsd:dayTimeDuration
    @Test public void dayTimeDuration_01() {
        valid("PT0S", xsdDuration) ;
        valid("PT0S", xsdDayTimeDuration) ;
    }

    @Test public void dayTimeDuration_02() {
        invalid("PT", xsdDuration) ;
        invalid("PT", xsdDayTimeDuration) ;
    }

    @Test public void dayTimeDuration_03() {
        valid("P1D", xsdDuration) ;
        valid("P1D", xsdDayTimeDuration) ;
    }

    @Test public void dayTimeDuration_04() {
        valid("PT1M", xsdDuration) ;
        valid("PT1M", xsdDayTimeDuration) ;
    }

    @Test public void dayTimeDuration_05() {
        valid("PT1S", xsdDuration) ;
        valid("PT1S", xsdDayTimeDuration) ;
    }

    @Test public void dayTimeDuration_06() {
        valid("PT1M", xsdDuration) ;
        invalid("P1M", xsdDayTimeDuration) ;
    }

    @Test public void dayTimeDuration_07() {
        invalid("P1DT", xsdDuration) ;
        invalid("P1DT", xsdDayTimeDuration) ;
    }
}

