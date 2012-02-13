/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.wso2.xkms2.client;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.wso2.xkms2.Authentication;
import org.wso2.xkms2.LocateRequest;
import org.wso2.xkms2.PrototypeKeyBinding;
import org.wso2.xkms2.QueryKeyBinding;
import org.wso2.xkms2.RecoverRequest;
import org.wso2.xkms2.RegisterRequest;
import org.wso2.xkms2.ReissueKeyBinding;
import org.wso2.xkms2.ReissueRequest;
import org.wso2.xkms2.ValidateRequest;
import org.wso2.xkms2.util.XKMSUtil;

public class XKMSClientUtil {
    
    
    public static RegisterRequest createRegisterRequest() {
        RegisterRequest request = new RegisterRequest();
        request.setId(XKMSUtil.getRamdomId());
        return request;
    }
    
    public static Authentication createAuthenticate() {
        Authentication authentication = new Authentication();
        return authentication;
    }
    
    public static PrototypeKeyBinding createPrototypeKeyBinding() {
        PrototypeKeyBinding keyBinding = new PrototypeKeyBinding();
        keyBinding.setId(XKMSUtil.getRamdomId());
        return keyBinding;
    }
    
    public static QueryKeyBinding createQueryKeyBinding() {
        QueryKeyBinding binding = new QueryKeyBinding();
        binding.setId(XKMSUtil.getRamdomId());
        return binding;
    }
    
    public static ReissueRequest createReissueRequest() {
        ReissueRequest reissueRequest  = new ReissueRequest();
        reissueRequest.setId(XKMSUtil.getRamdomId());
        return reissueRequest;
    }
    
    public static ReissueKeyBinding createReissueKeyBinding() {
        ReissueKeyBinding reissueKeyBinding = new ReissueKeyBinding();
        reissueKeyBinding.setId(XKMSUtil.getRamdomId());
        return reissueKeyBinding;
    }
    
    public static RecoverRequest createRecoverRequest() {
        RecoverRequest recoverRequest = new RecoverRequest();
        recoverRequest.setId(XKMSUtil.getRamdomId());
        return recoverRequest;
    }
    
    public static ValidateRequest createValidateRequest() {
        ValidateRequest validate = new ValidateRequest();
        validate.setId(XKMSUtil.getRamdomId());
        return validate;
    }
    
    public static LocateRequest createLocateRequest() {
        LocateRequest locate = new LocateRequest();
        locate.setId(XKMSUtil.getRamdomId());
        return locate;
    }
    
    
}
