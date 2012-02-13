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
package org.wso2.xkms2.client;

import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Stub;
import org.apache.axis2.client.Options;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.xml.security.keys.KeyInfo;
import org.wso2.xkms2.*;

import java.util.List;

/**
 * This is the stub class that will be configured to use with XKMS
 */

public class XKMSStub extends Stub {

    /**
     * If configurationContext is null, default will be selected.
     * endpointReference is mandatory
     * @param configurationContext
     * @param endpointReference
     * @throws XKMSException
     */
    public XKMSStub(ConfigurationContext configurationContext, EndpointReference endpointReference)
            throws XKMSException {
        try {
            this._serviceClient = new ServiceClient(configurationContext, null);
        } catch (AxisFault e) {
            throw new XKMSException(e);

        }
        if (endpointReference == null) {
            throw new IllegalArgumentException("EndpointReference cannot be NULL");
        }

        Options options = new Options();
        options.setTo(endpointReference);
        //Set empty action explicitly
        options.setAction("\"\"");
        this._serviceClient.setOptions(options);
    }

    /**
     * Invoke Locate service that will be based on UseKeyWith
     * @param useKeyWiths
     * @param respondWiths
     * @param keyUsages
     * @return List of UnverifiedKeyBinding
     * @throws XKMSException
     */
    public List locate(UseKeyWith[] useKeyWiths,
                       RespondWith[] respondWiths,
                       KeyUsage[] keyUsages) throws XKMSException {
        return XKMSStubHelper.elementHelper(null,
                                            useKeyWiths,
                                            respondWiths,
                                            keyUsages,
                                            _serviceClient,
                                            new LocateRequest());
    }

    /**
     * Invoke Locate service that will be based on KeyInfo
     * @param keyInfo
     * @param respondWiths
     * @param keyUsages
     * @return UnverifiedKeyBinding
     * @throws XKMSException
     */
    public List locate(KeyInfo keyInfo,
                       RespondWith[] respondWiths,
                       KeyUsage[] keyUsages) throws XKMSException {
        return XKMSStubHelper.elementHelper(keyInfo,
                                            null,
                                            respondWiths,
                                            keyUsages,
                                            _serviceClient,
                                            new LocateRequest());
    }

    /**
     * Call for validate Service
     * @param keyInfo
     * @param useKeyWiths
     * @param respondWiths
     * @param keyUsages
     * @return List of KeyBinding;
     * @throws XKMSException
     */
    public List validate(KeyInfo keyInfo,
                         UseKeyWith[] useKeyWiths,
                         RespondWith[] respondWiths,
                         KeyUsage[] keyUsages) throws XKMSException {
        return XKMSStubHelper.elementHelper(keyInfo,
                                            useKeyWiths,
                                            respondWiths,
                                            keyUsages,
                                            _serviceClient,
                                            new ValidateRequest());

    }

}
