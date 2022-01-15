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
import static org.junit.Assert.* ;

public class TestDatatype {
    @Test public void __init() {
        XSDDatatype dt = XSD.xsdAtomic ;
    }
    
    @Test public void datatype_01() {
        XSDDatatype dt = XSDTypeRegistry.getType("byte") ;
        assertNotNull(dt) ;
    }
    
    @Test public void datatype_02() {
        XSDDatatype dt = XSDTypeRegistry.getType("byte") ;
        XSDDatatype dt2 = XSDTypeRegistry.getDerivedFrom(dt) ;
        assertEquals(XSDConst.xsd_short, dt2.getName()) ;
    }
    
    @Test public void datatype_03() {
        assertEquals(XSD.xsdDecimal, XSDTypeRegistry.getType(XSD.xsdDecimal.getName())) ;
    }
    
    @Test public void datatype_04() {
        XSDTypeRegistry.registeredNames().stream().forEach( n -> {
            assertEquals(n, XSDTypeRegistry.getType(n).getName()) ;
            String z = XSD.getURI()+n ;
            assertEquals(n, XSDTypeRegistry.getTypeByURI(z).getName()) ;
        });
    }

    @Test public void datatype_05() {
        XSDTypeRegistry.registeredTypes().stream().forEach( dt -> {
            assertEquals(dt, XSDTypeRegistry.getType(dt.getName())) ; 
        });
    }
    
    @Test public void datatype_06() {
        XSDTypeRegistry.registeredTypes().stream().forEach( dt -> {
            assertNotNull(dt.getValueClass()) ;  
        });
    }

}

