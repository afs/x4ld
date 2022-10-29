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

package org.seaborne.xsd4ld.types;

import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.XMLGregorianCalendar;

import org.seaborne.xsd4ld.ValueClass;
import org.seaborne.xsd4ld.XSDConst;
import org.seaborne.xsd4ld.XSDDatatype;
import org.seaborne.xsd4ld.XSDTypeRegistry;
import org.seaborne.xsd4ld.lib.DateTimeStruct;

/**
 * Gregorian xsd:g*
 * for Year, Month, Day, YearMonth and MonthDay
 */
abstract class BaseGregorian extends XSDDatatype  {
    // Some overlap with BaseDateTime. Costructors.Parser, parse
    // Common "BaseTimeLine"
    @FunctionalInterface interface Parser { DateTimeStruct parse(String s); }
    protected final Parser parser;

    public BaseGregorian(String shortName, Parser parser) {
        this(shortName, XSDConst.xsd_atomic, parser);
    }

    private BaseGregorian(String shortName, String derivedFrom, Parser parser) {
        super(shortName, derivedFrom, ValueClass.DATETIME, XSDTypeRegistry.getRegex(shortName));
        this.parser = parser;
    }

    // java.time. does not have the necessary support for XSD Gregorian formats.
    // Maybe we define our own DateTimeFormatter?

    // Only makes sense for Year and YearMonth.
    // Better "absent" handling.
    //Not Temporal?

    @Override
    protected Object valueOrException(String lex) {
        XMLGregorianCalendar cal = Base.xmlDatatypeFactory.newXMLGregorianCalendar(lex);
        return cal;
    }

    @Override
    public boolean isValid(String lex) {
        DateTimeStruct dts = parse(lex);
        if ( dts == null )
            return false;
        // XML Schema 1.1
        if ( "0000".equals(dts.year) )
            return true;
        return value(lex) != null;
    }

//    // Can't be done?  Temporal is point-in-time so not "month".
//    protected static Temporal valueOf(XMLGregorianCalendar cal) {
//        if ( cal.getTimezone() == DatatypeConstants.FIELD_UNDEFINED )
//            return LocalDateTime.of(fix(cal.getYear(),0),
//                                    fix(cal.getMonth(),1),
//                                    fix(cal.getDay(),1),
//                                    fix(cal.getHour(), 0),
//                                    fix(cal.getMinute(), 0),
//                                    fix(cal.getSecond(), 0),
//                                    fix(cal.getMillisecond(),0)*1_000_000);
//        else {
//            int mins = cal.getTimezone();
//            int h = mins/60;
//            int m = mins-60*h;
//            ZoneOffset zoneOffset = ZoneOffset.ofHoursMinutes(h, m);
//            return OffsetDateTime.of(fix(cal.getYear(),0),
//                                     fix(cal.getMonth(),1),
//                                     fix(cal.getDay(),1),
//                                     fix(cal.getHour(), 0),
//                                     fix(cal.getMinute(), 0),
//                                     fix(cal.getSecond(), 0),
//                                     fix(cal.getMillisecond(),0)*1_000_000,
//                                     zoneOffset);
//        }
//    }

      private static int fix(int xmlCal, int dft) {
          if ( xmlCal == DatatypeConstants.FIELD_UNDEFINED )
              return dft;
          return xmlCal;
      }

      protected DateTimeStruct parse(String lex) {
          // We use a different parser setup for each derived type. The parsers are strict.
          try {
              return parser.parse(lex);
          } catch (Exception ex) { return null; }
      }
}
