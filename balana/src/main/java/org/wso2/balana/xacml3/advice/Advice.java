/*
*  Copyright (c) WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/

package org.wso2.balana.xacml3.advice;

import org.wso2.balana.Indenter;
import org.wso2.balana.xacml3.AttributeAssignment;

import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.List;

/**
 * Represents the AdviceType XML type in XACML. Advice are introduced with XACML 3
 */
public class Advice {

    /**
     * The value of the advice identifier may be interpreted by the PEP.
     */
    private URI adviceId;

    /**
     *  Advice arguments as a <code>List</code> of <code>AttributeAssignment</code>
     *  The values of the advice arguments may be interpreted by the PEP
     */
    private List<AttributeAssignment> assignments;

    /**
     * Constructor that creates a new <code>Advice</code> based on
     * the given elements.
     *
     * @param adviceId  Identifier that uniquely identify the Advice
     * @param assignments  <code>List</code> of <code>AttributeAssignment</code>
     */
    public Advice(URI adviceId, List<AttributeAssignment> assignments) {
        this.adviceId = adviceId;
        this.assignments = assignments;
    }


    /**
     * Encodes this <code>Advice</code> into its XML form and writes this out to the provided
     * <code>OutputStream<code> with no indentation.
     *
     * @param output a stream into which the XML-encoded data is written
     */
    public void encode(OutputStream output){
        encode(output, new Indenter(0));
    }

    /**
     * Encodes this <code>Advice</code> into its XML form and writes this out to the provided
     * <code>OutputStream<code> with indentation.
     *
     * @param output a stream into which the XML-encoded data is written
     * @param indenter an object that creates indentation strings
     */
    public void encode(OutputStream output, Indenter indenter){

        PrintStream out = new PrintStream(output);
        String indent = indenter.makeString();

        out.println(indent + "<Advice AdviceId=" + adviceId + " >");

        indenter.in();

        if(assignments != null && assignments.size() > 0){
            for(AttributeAssignment assignment : assignments){
                assignment.encode(output, indenter);
            }
        }
        
        indenter.out();        
        out.println(indent + "</Advice>");
    }
}
