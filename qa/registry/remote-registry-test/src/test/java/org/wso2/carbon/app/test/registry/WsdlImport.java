/*
 * Copyright 2004,2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.carbon.app.test.registry;

import org.wso2.carbon.registry.app.RemoteRegistry;
import org.wso2.carbon.registry.core.Association;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;

import java.util.List;

public class WsdlImport extends TestSetup {

    public WsdlImport(String text) {
        super(text);
    }

    public void testWsdlimport() throws RegistryException {

        String url = "http://131.107.72.15/SoapWsdl_ComplexDataTypes_XmlFormatter_Service_Indigo/ComplexDataTypesRpcLit.svc?wsdl";
        Resource r1 = registry.newResource();
        r1.setDescription("WSDL imported from url");
        r1.setMediaType("application/wsdl+xml");
        String path = "/MyTestWSDL/ComplexDataTypesRpcLit.svc.wsdl";

        String endpointPath = "/_system/governance/endpoints/http/131/107/72/15/SoapWsdl_ComplexDataTypes_XmlFormatter_" +
                "Service_Indigo/ep-ComplexDataTypesRpcLit-svc";

        registry.importResource(path, url, r1);

        String wsdlPath = "/_system/governance/wsdls/http/tempuri/org/";

        assertTrue("ComplexDataTypesRpcLit.svc.wsdl", resourceExists(registry, wsdlPath + "ComplexDataTypesRpcLit.svc.wsdl"));
        assertTrue("ComplexDataTypesRpcLitService is not available", resourceExists(registry, "/_system/governance/servi" +
                "ces/http/tempuri/org/ComplexDataTypesRpcLitService"));


        assertTrue("ComplexDataTypesRpcLit.xsd not found", resourceExists(registry, "/_system/governance/schemas/http/" +
                "schemas/microsoft/com/2003/10/Serialization/Arrays/ComplexDataTypesRpcLit.xsd"));
        assertTrue("ComplexDataTypesRpcLit1.xsd not found", resourceExists(registry, "/_system/governance/schemas/http" +
                "/schemas/microsoft/com/2003/10/Serialization/ComplexDataTypesRpcLit1.xsd"));
        assertTrue("ComplexDataTypesRpcLit2.xsd not found", resourceExists(registry, "/_system/governance/schemas/http" +
                "/schemas/datacontract/org/2004/07/System/ComplexDataTypesRpcLit2.xsd"));
        assertTrue("ComplexDataTypesRpcLit3.xsd not found", resourceExists(registry, "/_system/governance/schemas/http" +
                "/schemas/datacontract/org/2004/07/XwsInterop/SoapWsdl/ComplexDataTypes/XmlFormatter/Service/Indigo/" +
                "ComplexDataTypesRpcLit3.xsd"));
        assertTrue("ep-ComplexDataTypesRpcLit-svc endpoint not found", resourceExists(registry, "/_system/governance/" +
                "endpoints/http/131/107/72/15/SoapWsdl_ComplexDataTypes_XmlFormatter_Service_Indigo" +
                "/ep-ComplexDataTypesRpcLit-svc"));

        assertTrue("association Destination path not exist", associationPathExists(wsdlPath +
                "ComplexDataTypesRpcLit.svc.wsdl", "/_system/governance/services/http/tempuri/org/ComplexDataTypesRpcLit" +
                "Service"));

        assertTrue("association Destination path not exist", associationPathExists(wsdlPath +
                "ComplexDataTypesRpcLit.svc.wsdl", "/_system/governance/schemas/http/schemas/datacontract/org/2004/07" +
                "/XwsInterop/SoapWsdl/ComplexDataTypes/XmlFormatter/Service/Indigo/ComplexDataTypesRpcLit3.xsd"));

        assertTrue("association Destination path not exist", associationPathExists(wsdlPath +
                "ComplexDataTypesRpcLit.svc.wsdl", "/_system/governance/endpoints/http/131/107/72/15" +
                "/SoapWsdl_ComplexDataTypes_XmlFormatter_Service_Indigo/ep-ComplexDataTypesRpcLit-svc"));

        assertTrue("association Destination path not exist", associationPathExists(wsdlPath +
                "ComplexDataTypesRpcLit.svc.wsdl", "/_system/governance/schemas/http/schemas/microsoft" +
                "/com/2003/10/Serialization/Arrays/ComplexDataTypesRpcLit.xsd"));

        assertTrue("association Destination path not exist", associationPathExists(wsdlPath +
                "ComplexDataTypesRpcLit.svc.wsdl", "/_system/governance/schemas/http/schemas/microsoft" +
                "/com/2003/10/Serialization/ComplexDataTypesRpcLit1.xsd"));

        assertTrue("association Destination path not exist", associationPathExists(wsdlPath +
                "ComplexDataTypesRpcLit.svc.wsdl", "/_system/governance/schemas/http/schemas/datacontract" +
                "/org/2004/07/System/ComplexDataTypesRpcLit2.xsd"));

        assertTrue("association Destination path not exist", associationPathExists(wsdlPath +
                "ComplexDataTypesRpcLit.svc.wsdl", "/_system/governance/schemas/http/schemas/datacontract" +
                "/org/2004/07/XwsInterop/SoapWsdl/ComplexDataTypes/XmlFormatter/Service/Indigo/ComplexDataTypesRpcLit3.xsd"));

        /*check wsdl properties*/

        Resource r2b = registry.get(wsdlPath + "ComplexDataTypesRpcLit.svc.wsdl");

        assertEquals("WSDL validation status", r2b.getProperty("WSDL Validation"), "Valid");
        assertEquals("WS-I validation status", r2b.getProperty("WSI Validation"), "Invalid");

        /*check for enpoint dependencies*/
        assertTrue("association Destination path not exist", associationPathExists(endpointPath,
                "/_system/governance/wsdls/http/tempuri/org/ComplexDataTypesRpcLit.svc.wsdl"));
        assertTrue("association Destination path not exist", associationPathExists(endpointPath,
                "/_system/governance/services/http/tempuri/org/ComplexDataTypesRpcLitService"));

        /*check for xsd dependencies*/

        assertTrue("association Destination path not exist", associationPathExists("/_system/governance/schemas/http/" +
                "schemas/microsoft/com/2003/10/Serialization/Arrays/ComplexDataTypesRpcLit.xsd", "/_system/governance/" +
                "schemas/http/schemas/datacontract/org/2004/07/XwsInterop/SoapWsdl/ComplexDataTypes/XmlFormatter/Service" +
                "/Indigo/ComplexDataTypesRpcLit3.xsd"));

        assertTrue("association Destination path not exist", associationPathExists("/_system/governance/schemas/http/" +
                "schemas/microsoft/com/2003/10/Serialization/Arrays/ComplexDataTypesRpcLit.xsd", "/_system/governance/" +
                "wsdls/http/tempuri/org/ComplexDataTypesRpcLit.svc.wsdl"));

        assertTrue("association Destination path not exist", associationPathExists("/_system/governance/schemas/http/schemas" +
                "/datacontract/org/2004/07/XwsInterop/SoapWsdl/ComplexDataTypes/XmlFormatter/Service/Indigo/" +
                "ComplexDataTypesRpcLit3.xsd", "/_system/governance/schemas/http/schemas/microsoft/com/2003/10/" +
                "Serialization/Arrays/ComplexDataTypesRpcLit.xsd"));

        assertTrue("association Destination path not exist", associationPathExists("/_system/governance/schemas/http/schemas" +
                "/datacontract/org/2004/07/XwsInterop/SoapWsdl/ComplexDataTypes/XmlFormatter/Service/Indigo/" +
                "ComplexDataTypesRpcLit3.xsd", "/_system/governance/wsdls/http/tempuri/org" +
                "/ComplexDataTypesRpcLit.svc.wsdl"));
    }

    public static boolean resourceExists(RemoteRegistry registry, String fileName) throws RegistryException {
        boolean value = registry.resourceExists(fileName);
        return value;
    }

    public boolean associationPathExists(String path, String assoPath)
            throws RegistryException {
        Association association[] = registry.getAllAssociations(path);
        boolean value = false;

        for (int i = 0; i < association.length; i++) {
            //System.out.println(association[i].getDestinationPath());
            if (assoPath.equals(association[i].getDestinationPath()))
                value = true;
        }


        return value;
    }

    public boolean associationExists(String path, String pathValue)
            throws RegistryException {
        Association association[] = registry.getAllAssociations(path);
        boolean value = false;
        for (int i = 0; i < association.length; i++)
            if (pathValue.equals(association[i].getDestinationPath()))
                value = true;

        return value;
    }

    public boolean associationNotExists(String path) throws RegistryException {
        Association association[] = registry.getAllAssociations(path);
        boolean value = true;
        if (association.length > 0)
            value = false;
        return value;
    }

    public boolean getProperty(String path, String key, String value) throws RegistryException {
        Resource r3 = registry.newResource();
        try {
            r3 = registry.get(path);
        }
        catch (RegistryException e) {
            fail((new StringBuilder()).append("Couldn't get file from the path :").append(path).toString());
        }
        List propertyValues = r3.getPropertyValues(key);
        Object valueName[] = propertyValues.toArray();
        boolean propertystatus = containsString(valueName, value);
        return propertystatus;
    }

    private boolean containsString(Object[] array, String value) {
        boolean found = false;
        for (Object anArray : array) {
            String s = anArray.toString();
            if (s.startsWith(value)) {
                found = true;
                break;
            }
        }

        return found;
    }
}
