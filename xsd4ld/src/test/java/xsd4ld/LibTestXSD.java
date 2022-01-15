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

import static org.junit.Assert.* ;

public class LibTestXSD {

    static void valid(String lex, XSDDatatype type) {
        test(lex, type, true);
    }

    static void test(String lex, XSDDatatype type, boolean valid) {
        if ( valid != type.isValid(lex) ) {
            if ( valid ) 
                fail("Unexpected invalid: "+type.getName()+" '"+lex+"'") ;
            else
                fail("Unexpected valid: "+type.getName()+" '"+lex+"'") ;
        }
        if ( valid && type.getRegex() != null ) {
            // Collapse
            lex = lex.replace("  ", " ") ;
            lex = lex.trim();       
            assertTrue("Lex:"+lex+" Regex: "+type.getRegex(), type.getRegex().matcher(lex).matches()) ;
        }
    }

    static void invalid(String lex, XSDDatatype type) {
        test(lex, type, false);
    }

}

