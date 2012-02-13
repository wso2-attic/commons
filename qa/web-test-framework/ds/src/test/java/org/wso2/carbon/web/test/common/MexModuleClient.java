/*
 *  Copyright (c) 2005-2009, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.wso2.carbon.web.test.common;

import org.apache.axis2.dataretrieval.client.MexClient;
import org.apache.axis2.dataretrieval.DRConstants;
import org.apache.axis2.AxisFault;
import org.apache.axis2.mex.MexConstants;
import org.apache.axis2.mex.om.Metadata;
import org.apache.axis2.mex.om.MetadataSection;
import org.apache.axis2.mex.om.Location;
import org.apache.axis2.mex.om.MetadataReference;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;

public class MexModuleClient {

    public static MexClient getMexClient(String epr) throws AxisFault {
        MexClient mc = new MexClient();
        Options options = mc.getOptions();
        options.setTo(new EndpointReference(epr));
        options.setAction(DRConstants.SPEC.Actions.GET_METADATA_REQUEST);
        options.setExceptionToBeThrownOnSOAPFault(true);
        return mc;
    }

    public void getServiceSchema(String epr) throws Exception {
        MexClient mc = getMexClient(epr);
        OMElement request = mc.setupGetMetadataRequest(MexConstants.SPEC.DIALECT_TYPE_SCHEMA, null);
        OMElement result = mc.sendReceive(request);
        Metadata metadata = new Metadata();
        metadata.fromOM(result);
        printResults(result);

    }

    public static void getServiceWSDL(String epr) throws Exception {

        MexClient mc = getMexClient(epr);
        OMElement request = mc.setupGetMetadataRequest(MexConstants.SPEC.DIALECT_TYPE_WSDL, null);
        OMElement result = mc.sendReceive(request);
        Metadata metadata = new Metadata();
        metadata.fromOM(result);
        printResults(result);

    }

    public static void printResults(OMElement result) throws org.apache.axis2.mex.MexException {

        Metadata metadata = new Metadata();
        metadata.fromOM(result);
        MetadataSection[] metadatSections = metadata.getMetadatSections();

        if (metadatSections == null || metadatSections.length == 0) {
            System.out.println("MetadataSection is not available");

        } else {
            MetadataSection metadataSection;
            for (int i = 0; i < metadatSections.length; i++) {
                metadataSection = metadatSections[i];
                System.out.println("Metadata Section:");
                System.out.println("****************");

                String dialect = metadataSection.getDialect();
                if (dialect != null) {
                    System.out.println("Dialect : " + dialect);
                }

                OMNode inlineData = metadataSection.getInlineData();
                if (inlineData != null) {
                    System.out.println("InlineData : \n" + inlineData.toString());
                    continue;
                }

                Location location = metadataSection.getLocation();
                if (location != null) {
                    System.out.println("Location : \n" + location.getURI());
                    continue;
                }

                MetadataReference metadataReference = metadataSection.getMetadataReference();
                if (metadataReference != null) {
                    System.out.println("MetadataSection : \n" + metadataReference.getEPRElement());

                }

            }
        }
    }
}
