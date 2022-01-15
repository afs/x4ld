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

import xsd4ld.types.*;

/**
 * The atomic datatypes, except those specific to XML.
 *
 * @see <a href="http://www.w3.org/TR/xmlschema11-2/">W3C XML Schema Definition Language (XSD) 1.1 Part 2: Datatypes</a>
 * @see <a href="http://www.w3.org/TR/xsd-precisionDecimal/">An XSD datatype for IEEE floating-point decimal</a>
 */

// Not: Name, NCName, ENTITY, ID IDREF, NMTOKEN, NOTATION, QName

public class XSD {
    public static void init() {}

    /** The URI friendly for the of the namepace URI (this one has a # on it) */
    static public String getURI() {
        return "http://www.w3.org/2001/XMLSchema#";
    }

    /** The URI friendly for the of the namepace URI (this one has a # on it) */
    static public String getNamespaceURI() {
        return "http://www.w3.org/2001/XMLSchema";
    }

    static public XSDDatatype xsdAnyType            = new XSD_AnyType();
    static public XSDDatatype xsdSimple             = new XSD_Simple();
    static public XSDDatatype xsdAtomic             = new XSD_Atomic();

    static public XSDDatatype xsdBoolean            = new XSD_Boolean();

    static public XSDDatatype xsdDecimal            = new XSD_Decimal();
    static public XSDDatatype xsdInteger            = new XSD_Integer();

    static public XSDDatatype xsdDouble             = new XSD_Double();
    static public XSDDatatype xsdFloat              = new XSD_Float();
    static public XSDDatatype xsdPrecisionDecimal   = new XSD_PrecisionDecimal();

    static public XSDDatatype xsdLong               = new XSD_Long();
    static public XSDDatatype xsdInt                = new XSD_Int();
    static public XSDDatatype xsdShort              = new XSD_Short();
    static public XSDDatatype xsdByte               = new XSD_Byte();

    static public XSDDatatype xsdNegativeInteger    = new XSD_NegativeInteger();
    static public XSDDatatype xsdNonNegativeInteger = new XSD_NonNegativeInteger();
    static public XSDDatatype xsdNonPositiveInteger = new XSD_NonPositiveInteger();
    static public XSDDatatype xsdPositiveInteger    = new XSD_PositiveInteger();

    static public XSDDatatype xsdUnsignedLong       = new XSD_UnsignedLong();
    static public XSDDatatype xsdUnsignedInt        = new XSD_UnsignedInt();
    static public XSDDatatype xsdUnsignedShort      = new XSD_UnsignedShort();
    static public XSDDatatype xsdUnsignedByte       = new XSD_UnsignedByte();

    static public XSDDatatype xsdDuration           = new XSD_Duration();
    static public XSDDatatype xsdYearMonthDuration  = new XSD_YearMonthDuration();
    static public XSDDatatype xsdDayTimeDuration    = new XSD_DayTimeDuration();

    static public XSDDatatype xsdString             = new XSD_String();
    static public XSDDatatype xsdNormalizedString   = new XSD_NormalizedString();
    static public XSDDatatype xsdToken              = new XSD_Token();
    static public XSDDatatype xsdLanguage           = new XSD_Language();
    static public XSDDatatype xsdAnyURI             = new XSD_AnyURI();

    static public XSDDatatype xsdDateTimeStamp      = new XSD_DateTimeStamp();
    static public XSDDatatype xsdDateTime           = new XSD_DateTime();
    static public XSDDatatype xsdDate               = new XSD_Date();
    static public XSDDatatype xsdTime               = new XSD_Time();

    static public XSDDatatype xsdGYear              = new XSD_GYear();
    static public XSDDatatype xsdGYearMonth         = new XSD_GYearMonth();
    static public XSDDatatype xsdGMonthDay          = new XSD_GMonthDay();
    static public XSDDatatype xsdGDay               = new XSD_GDay();
    static public XSDDatatype xsdGMonth             = new XSD_GMonth();

    static public XSDDatatype xsdHexBinary          = new XSD_HexBinary();
    static public XSDDatatype xsdBase64Binary       = new XSD_Base64Binary();

    static {
        register(xsdAnyType.getName(), xsdAnyType);
        register(xsdSimple.getName(), xsdSimple);
        register(xsdAtomic.getName(), xsdAtomic);

        register(xsdBoolean.getName(), xsdBoolean);


        register(xsdDecimal.getName(), xsdDecimal);
        register(xsdInteger.getName(), xsdInteger);

        register(xsdDouble.getName(), xsdDouble);
        register(xsdFloat.getName(), xsdFloat);
        register(xsdPrecisionDecimal.getName(), xsdPrecisionDecimal);

        register(xsdLong.getName(), xsdLong);
        register(xsdInt.getName(), xsdInt);
        register(xsdShort.getName(), xsdShort);
        register(xsdByte.getName(), xsdByte);

        register(xsdNegativeInteger.getName(), xsdNegativeInteger);
        register(xsdNonNegativeInteger.getName(), xsdNonNegativeInteger);
        register(xsdNonPositiveInteger.getName(), xsdNonPositiveInteger);
        register(xsdPositiveInteger.getName(), xsdPositiveInteger);

        register(xsdUnsignedLong.getName(), xsdUnsignedLong);
        register(xsdUnsignedInt.getName(), xsdUnsignedInt);
        register(xsdUnsignedShort.getName(), xsdUnsignedShort);
        register(xsdUnsignedByte.getName(), xsdUnsignedByte);

        register(xsdDuration.getName(), xsdDuration);
        register(xsdYearMonthDuration.getName(), xsdYearMonthDuration);
        register(xsdDayTimeDuration.getName(), xsdDayTimeDuration);

        register(xsdString.getName(), xsdString);
        register(xsdNormalizedString.getName(), xsdNormalizedString);
        register(xsdToken.getName(), xsdToken);
        register(xsdLanguage.getName(), xsdLanguage);
        register(xsdAnyURI.getName(), xsdAnyURI);

        register(xsdDateTimeStamp.getName(), xsdDateTimeStamp);
        register(xsdDateTime.getName(), xsdDateTime);
        register(xsdDate.getName(), xsdDate);
        register(xsdTime.getName(), xsdTime);

        register(xsdGYear.getName(), xsdGYear);
        register(xsdGYearMonth.getName(), xsdGYearMonth);
        register(xsdGMonthDay.getName(), xsdGMonthDay);
        register(xsdGDay.getName(), xsdGDay);
        register(xsdGMonth.getName(), xsdGMonth);

        register(xsdHexBinary.getName(), xsdHexBinary);
        register(xsdBase64Binary.getName(), xsdBase64Binary);
    }

    private static void register(String name, XSDDatatype xsdType) {
        XSDTypeRegistry.register(name, xsdType);
    }
}
