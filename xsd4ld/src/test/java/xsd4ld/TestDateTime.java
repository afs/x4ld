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

import static xsd4ld.LibTestXSD.*;
import static xsd4ld.XSD.*;
import org.junit.Test;

/** Test for xsd:dateTime etc
 */
public class TestDateTime {

    @Test public void dateTimeStamp_01() {
        valid("2015-02-23T15:21:18Z", xsdDateTime);
        valid("2015-02-23T15:21:18Z", xsdDateTimeStamp);
    }

    @Test public void dateTimeStamp_02() {
        valid("2015-02-23T15:21:18", xsdDateTime);
        invalid("2015-02-23T15:21:18", xsdDateTimeStamp);
    }

    @Test public void dateTimeStamp_03() {
        invalid("2015-02-23Z", xsdDateTime);
        invalid("2015-02-23Z", xsdDateTimeStamp);
    }

    @Test public void dateTimeStamp_04() {
        valid("2015-02-23T15:21:18.665Z", xsdDateTime);
        valid("2015-02-23T15:21:18.665Z", xsdDateTimeStamp);
    }

    @Test public void dateTimeStamp_05() {
        valid("2015-02-23T15:21:18.665+00:00", xsdDateTime);
        valid("2015-02-23T15:21:18.665+00:00", xsdDateTimeStamp);
    }

    @Test public void dateTimeStamp_06() {
        invalid("2015-02-23T15:21:18.665+15:00", xsdDateTime);
        invalid("2015-02-23T15:21:18.665+15:00", xsdDateTimeStamp);
    }

    @Test public void dateTimeStamp_07() {
        // Valid in XSD schema 1.1, not XSD Schema 1.0.

        //valid("0000-02-23T15:21:18.665+15:00", xsdDateTime);
        //valid("0000-02-23T15:21:18.665+15:00", xsdDateTimeStamp);
    }

    // Timezone

    @Test public void gregorian_year_01() {
        valid("2015", xsdGYear);
        valid("-0001", xsdGYear);
        // invalid in XML Schema 1.0
        // valid in XML Schema 1.1
        //   See https://www.w3.org/TR/xmlschema11-2/#vp-dt-year
        // Java XMLGregorian calendar supports XML Schema 1.0
        valid("0000", xsdGYear);

        // Timezones are legal!
        valid("2015Z", xsdGYear);
        valid("0001-05:00", xsdGYear);
    }

    @Test public void gregorian_year_02() {
        invalid("2015-02-23",   xsdGYear);
        invalid("2015-02",      xsdGYear);
        invalid("-1",           xsdGYear);
    }

    @Test public void gregorian_month_01() {
        valid("--02", xsdGMonth);
        valid("--12", xsdGMonth);
    }

    @Test public void gregorian_month_02() {
        invalid("-00", xsdGMonth);
        invalid("-13", xsdGMonth);
        invalid("12", xsdGMonth);
        invalid("-12", xsdGMonth);
        invalid("---12", xsdGMonth);
    }

    @Test public void gregorian_day_01() {
        valid("---02", xsdGDay);
        valid("---31", xsdGDay);
    }

    @Test public void gregorian_day_02() {
        invalid("-02", xsdGDay);
        invalid("--02", xsdGDay);
        invalid("--32", xsdGDay);
    }

    @Test public void gregorian_yearMonth_01() {
        valid("2015-09", xsdGYearMonth);
        valid("-2015-09", xsdGYearMonth);
        valid("0000-02", xsdGYearMonth);
        valid("2015-09Z", xsdGYearMonth);
        valid("2015-09-08:00", xsdGYearMonth);
    }

    @Test public void gregorian_yearMonth_02() {
        invalid("2015-13", xsdGYearMonth);
        invalid("2015-13", xsdGYearMonth);
        invalid("2015-02-23", xsdGYearMonth);
    }

    @Test public void gregorian_monthDay_01() {
        valid("--12-09", xsdGMonthDay);
        valid("--12-31", xsdGMonthDay);
        valid("--12-31Z", xsdGMonthDay);
        valid("--12-31-05:00", xsdGMonthDay);
    }

    @Test public void gregorian_monthDay_02() {
        invalid("-12-09", xsdGMonthDay);
        invalid("---12-32", xsdGMonthDay);
    }


}

