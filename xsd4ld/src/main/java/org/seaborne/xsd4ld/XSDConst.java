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

package org.seaborne.xsd4ld;

import java.math.BigInteger;

/** names and other constants */
public class XSDConst {
    public static final String xsd_atomic             = "atomic";
    public static final String xsd_simple             = "simple";
    public static final String xsd_anyType            = "anyType";
    public static final String xsd_anyURI             = "anyURI";
    public static final String xsd_string             = "string";
    public static final String xsd_boolean            = "boolean";

    public static final String xsd_decimal            = "decimal";
    public static final String xsd_float              = "float";
    public static final String xsd_double             = "double";
    public static final String xsd_precisionDecimal   = "precisionDecimal";

    public static final String xsd_integer            = "integer";
    public static final String xsd_nonPositiveInteger = "nonPositiveInteger";
    public static final String xsd_negativeInteger    = "negativeInteger";
    public static final String xsd_long               = "long";
    public static final String xsd_int                = "int";
    public static final String xsd_short              = "short";
    public static final String xsd_byte               = "byte";
    public static final String xsd_nonNegativeInteger = "nonNegativeInteger";
    public static final String xsd_unsignedLong       = "unsignedLong";
    public static final String xsd_unsignedInt        = "unsignedInt";
    public static final String xsd_unsignedShort      = "unsignedShort";
    public static final String xsd_unsignedByte       = "unsignedByte";
    public static final String xsd_positiveInteger    = "positiveInteger";

    public static final String xsd_dateTime           = "dateTime";
    public static final String xsd_dateTimeStamp      = "dateTimeStamp";
    public static final String xsd_time               = "time";
    public static final String xsd_date               = "date";
    public static final String xsd_gYearMonth         = "gYearMonth";
    public static final String xsd_gYear              = "gYear";
    public static final String xsd_gMonthDay          = "gMonthDay";
    public static final String xsd_gDay               = "gDay";
    public static final String xsd_gMonth             = "gMonth";
    public static final String xsd_hexBinary          = "hexBinary";
    public static final String xsd_base64Binary       = "base64Binary";

    public static final String xsd_normalizedString   = "normalizedString";
    public static final String xsd_token              = "token";
    public static final String xsd_language           = "language";

    public static final String xsd_duration           = "duration";
    public static final String xsd_yearMonthDuration  = "yearMonthDuration";
    public static final String xsd_dayTimeDuration    = "dayTimeDuration";

    // XML specfic
    // public static final String xsd_QName              = "QName";
    // public static final String xsd_Name               = "Name";
    // public static final String xsd_NCName             = "NCName";
    // public static final String xsd_ENTITY             = "ENTITY";
    // public static final String xsd_ID                 = "ID";
    // public static final String xsd_IDREF              = "IDREF";
    // public static final String xsd_NMTOKEN            = "NMTOKEN";
    // public static final String xsd_NOTATION           = "NOTATION";

    // Shorthand
    private static final BigInteger i(long x) {
        return BigInteger.valueOf(x);
    }

    private static final BigInteger i(String x) {
        return new BigInteger(x);
    }

    public static final BigInteger  ZERO       = i(0);
    public static final BigInteger  ONE        = i(1);
    public static final BigInteger  MINUS_ONE  = i(-1);

    public static final BigInteger  LONG_MIN   = i(Long.MIN_VALUE);
    public static final BigInteger  LONG_MAX   = i(Long.MAX_VALUE);
    public static final BigInteger  LONG_UMAX  = i("18446744073709551615");

    public static final BigInteger  INT_MIN    = i(Integer.MIN_VALUE);
    public static final BigInteger  INT_MAX    = i(Integer.MAX_VALUE);
    public static final BigInteger  INT_UMAX   = i("4294967295");

    public static final BigInteger  SHORT_MIN  = i(-32768);
    public static final BigInteger  SHORT_MAX  = i(32767);
    public static final BigInteger  SHORT_UMAX = i(65535);

    public static final BigInteger  BYTE_MIN   = i(-128);
    public static final BigInteger  BYTE_MAX   = i(127);
    public static final BigInteger  BYTE_UMAX  = i(255);
}

