/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.seaborne.rfc3986.cmd;

import static org.seaborne.rfc3986.cmd.CmdBase.createOrExit;
import static org.seaborne.rfc3986.cmd.CmdBase.fixup;
import static org.seaborne.rfc3986.cmd.CmdBase.print;

import org.seaborne.rfc3986.IRI3986;

public class CmdRelative {
    public static void main(String... args) {
        if ( args.length != 2 ) {
            System.err.println("Requires two arguments - base IRI and IRI to make relative.");
            System.exit(1);
        }

        String baseStr = fixup(args[0]);
        String relStr = fixup(args[1]);

        IRI3986 base = createOrExit(baseStr, "Bad base");
        IRI3986 target = createOrExit(relStr, "Bad IRI");

        if ( ! base.isAbsolute() ) {
            System.err.println("Base must be an absolute IRI: '" +base+"'");
            System.exit(1);
        }

        IRI3986 result = base.relativize(target);
        System.out.println("Base:     "+base);
        System.out.println("IRI:      "+target);
        System.out.println();
        System.out.println("Relative: "+result);
        System.out.println();

        print(result);
    }
}
