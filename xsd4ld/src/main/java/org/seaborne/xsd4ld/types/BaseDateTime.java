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

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;

import org.seaborne.xsd4ld.ValueClass;
import org.seaborne.xsd4ld.XSDConst;
import org.seaborne.xsd4ld.XSDDatatype;
import org.seaborne.xsd4ld.XSDTypeRegistry;
import org.seaborne.xsd4ld.lib.DateTimeStruct;

/** The 7 component date/time model.
 *  year / month / day / hour / minute / second / timezone
 *  for xsd:dateTime, xsd:date, xsd:dateTimeStamp.
 *  but not the Gregorian xsd:g* which are not "temporal" (a point in time).
 */
abstract class BaseDateTime extends XSDDatatype  {
    @FunctionalInterface interface Parser { DateTimeStruct parse(String s); }
    protected final Parser parser;

    public BaseDateTime(String shortName, Parser parser) {
        this(shortName, XSDConst.xsd_atomic, parser);
    }

    public BaseDateTime(String shortName, String derivedFrom, Parser parser) {
        super(shortName, derivedFrom, ValueClass.DATETIME, XSDTypeRegistry.getRegex(shortName));
        this.parser = parser;
    }

    @Override
    protected Temporal valueOrException(String lex) {

        DateTimeStruct dts = parser.parse(lex);

        // Matches XML schema 1.1, not 1.0 (year 0 is not allowed)
        // 1BC is 0000
        // 2BC is -0001

        // ISO_DATE, ISO_DATE_TIME

        DateTimeFormatter dtFmt;
        // YUK
        if ( shortName.equals("date"))
            dtFmt = DateTimeFormatter.ISO_DATE;
        else
            dtFmt = DateTimeFormatter.ISO_DATE_TIME;

        TemporalAccessor temporalAccessor = dtFmt.parse(lex);

        // dateTime vs date (vs g*

        if ( temporalAccessor.isSupported(ChronoField.OFFSET_SECONDS) ) {
            // XSD: Timezones are +/- 14 hours.
            OffsetDateTime odt = OffsetDateTime.from(temporalAccessor);
            int x = odt.get(ChronoField.OFFSET_SECONDS);
            if ( x < -14*60*60 || x > 14*60*60 )
                return null;
            return odt;
        } else {
            return LocalDateTime.from(temporalAccessor);
        }
    }

    protected DateTimeStruct parse(String lex) {
        // We use a different parser setup for each derived type. The parsers are strict.
        try {
            return parser.parse(lex);
        } catch (Exception ex) { return null; }
    }
}
