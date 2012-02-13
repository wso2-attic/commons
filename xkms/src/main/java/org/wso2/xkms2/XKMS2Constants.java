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
package org.wso2.xkms2;

import javax.xml.namespace.QName;

import org.apache.xml.security.algorithms.MessageDigestAlgorithm;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.signature.XMLSignature;

/*
 * 
 */

public final class XKMS2Constants {
    public final static String XKMS2_NS = "http://www.w3.org/2002/03/xkms#";

    public final static String XKMS2_PREFIX = "xkms";

    public static final String XML_SIG_METHOD = XMLSignature.ALGO_ID_MAC_HMAC_SHA1;

    public static final String XML_CANON_METHOD = Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS;

    public static final String XML_DIGST_METHOD = MessageDigestAlgorithm.ALGO_ID_DIGEST_SHA1;

    public static final String XML_SEC_NS = "http://www.w3.org/2000/09/xmldsig#";

    public static final String XML_ENC_NS = "http://www.w3.org/2001/04/xmlenc#";

    public static final QName Q_ELEM_XML_ENC_DATA = new QName(XML_ENC_NS,
            "EncryptedData");

    public static final QName Q_ELEM_XML_SIG = new QName(XML_SEC_NS,
            "Signature");

    public static final String ATTR_ID = "Id";

    public static final String ATTR_SERVICE = "Service";

    public static final String ATTR_NONCE = "Nonce";

    public static final String ATTR_MECHANISM = "Mechanism";

    public static final String ATTR_IDENTIFIER = "Identifier";

    public static final String ATTR_ORIGINAL_REQUEST_ID = "OriginalRequestId";

    public static final String ATTR_RESPONSE_LIMIT = "ResponseLimit";

    public static final String ATTR_APPLICATION = "Application";

    public static final String ATTR_TIME = "Time";

    public static final String ATTR_RESULT_MAJOR = "ResultMajor";

    public static final String ATTR_RESULT_MINOR = "ResultMinor";

    public static final String ATTR_REQUEST_ID = "RequestId";

    public static final String ATTR_NOT_BEFORE = "NotBefore";

    public static final String ATTR_NOT_ON_OR_AFTER = "NotOnOrAfter";

    public static final String ATTR_STATUS_VALUE = "StatusValue";

    public static final QName Q_ATTR_NOT_BEFORE = new QName(ATTR_NOT_BEFORE);

    public static final QName Q_ATTR_NOT_ON_OR_AFTER = new QName(
            ATTR_NOT_ON_OR_AFTER);

    public static final String ELEM_REGISTER_REQUEST = "RegisterRequest";

    public static final String ELEM_PROTOTYPE_KEY_BINDING = "PrototypeKeyBinding";

    public static final String ELEM_REGISTER_RESULT = "RegisterResult";

    public static final String ELEM_KEY_BINDING_AUTH = "KeyBindingAuthentication";

    public static final String ELEM_PROOF_OF_POSSESSION = "ProofOfPossession";

    public static final String ELEM_AUTHENTICATION = "Authentication";

    public static final String ELEM_PRIVATE_KEY = "PrivateKey";

    public static final String ELEM_RECOVER_KEY_BINDING = "RecoverKeyBinding";

    public static final String ELEM_RECOVER_REQUEST = "RecoverRequest";

    public static final String ELEM_RECOVER_RESULT = "RecoverResult";

    public static final String ELEM_REISSUE_REQUEST = "ReissueRequest";

    public static final String ELEM_REISSUE_KEY_BINDING = "ReissueKeyBinding";

    public static final String ELEM_REISSUE_RESULT = "ReissueResult";

    public static final String ELEM_REVOKE_REQUEST = "RevokeRequest";

    public static final String ELEM_REVOKE_KEY_BINDING = "RevokeKeyBinding";

    public static final String ELEM_REVOCATION_CODE = "RevocationCode";

    public static final String ELEM_REVOKE_RESULT = "RevokeResult";

    public static final String ELEM_RSA_KEY_PAIR = "RSAKeyPair";
    
    public static final String ELEM_ENCRYPTED_DATA = "EncryptedData";

    public static final QName ELE_OPAQUE_CLIENT_DATA = new QName(XKMS2_NS,
            "OpaqueClientData", XKMS2_PREFIX);

    public static final QName ELE_OPAQUE_DATA = new QName(XKMS2_NS,
            "OpaqueData", XKMS2_PREFIX);

    public static final QName ELE_RESPONSE_MECHANISM = new QName(XKMS2_NS,
            "ResponseMechanism", XKMS2_PREFIX);

    public static final QName ELE_RESPOND_WITH = new QName(XKMS2_NS,
            "RespondWith", XKMS2_PREFIX);

    public static final QName ELE_PENDING_NOTIFICATION = new QName(XKMS2_NS,
            "PendingNotification", XKMS2_PREFIX);

    public static final QName ELE_PROTOTYPE_KEY_BINDING = new QName(XKMS2_NS,
            "PrototypeKeyBinding", XKMS2_PREFIX);

    public static final QName ELE_LOCATE_REQUEST = new QName(XKMS2_NS,
            "LocateRequest", XKMS2_PREFIX);

    public static final QName ELE_KEY_USAGE = new QName(XKMS2_NS, "KeyUsage",
            XKMS2_PREFIX);

    public static final QName ELE_USE_KEY_WITH = new QName(XKMS2_NS,
            "UseKeyWith", XKMS2_PREFIX);

    public static final QName ELE_TIME_INSTANCE = new QName(XKMS2_NS,
            "TimeInstant", XKMS2_PREFIX);

    public static final QName ELE_QUERY_KEY_BINDING = new QName(XKMS2_NS,
            "QueryKeyBinding", XKMS2_PREFIX);

    public static final QName ELE_REQUEST_SIGNATURE_VALUE = new QName(XKMS2_NS,
            "RequestSignatureValue", XKMS2_PREFIX);

    public static final QName ELE_LOCATE_RESULT = new QName(XKMS2_NS,
            "LocateResult", XKMS2_PREFIX);

    public static final QName ELE_UNVERIFIED_KEY_BINDING = new QName(XKMS2_NS,
            "UnverifiedKeyBinding", XKMS2_PREFIX);

    public static final QName ELE_VALIDITY_INTERVAL = new QName(XKMS2_NS,
            "ValidityInterval", XKMS2_PREFIX);

    public static final QName ELE_VALIDATE_REQUEST = new QName(XKMS2_NS,
            "ValidateRequest", XKMS2_PREFIX);

    public static final QName ELE_VALIDATE_RESULT = new QName(XKMS2_NS,
            "ValidateResult", XKMS2_PREFIX);

    public static final QName ELE_KEY_BINDING = new QName(XKMS2_NS,
            "KeyBinding", XKMS2_PREFIX);

    public static final QName ELE_STATUS = new QName(XKMS2_NS, "Status",
            XKMS2_PREFIX);

    public static final QName ELE_VALID_REASON = new QName(XKMS2_NS,
            "ValidReason", XKMS2_PREFIX);

    public static final QName ELE_INDETERMINATE_REASON = new QName(XKMS2_NS,
            "IndeterminateReason", XKMS2_PREFIX);

    public static final QName ELE_INVALID_REASON = new QName(XKMS2_NS,
            "InvalidReason", XKMS2_PREFIX);

    public static final QName ELEM_REVOCATION_CODE_IDENTIFIER = new QName(
            XKMS2_NS, "RevocationCodeIdentifier", XKMS2_PREFIX);

    public static final QName ELEM_VALIDITY_INTERVAL = new QName(XKMS2_NS,
            "ValidityInterval", XKMS2_PREFIX); 

    public static final QName Q_ELEM_KEY_BINDING_AUTH = new QName(XKMS2_NS,
            ELEM_KEY_BINDING_AUTH, XKMS2_PREFIX);

    public static final QName Q_ELEM_PROOF_OF_POSSESSION = new QName(XKMS2_NS,
            ELEM_PROOF_OF_POSSESSION, XKMS2_PREFIX);

    public static final QName Q_ELEM_AUTHENTICATION = new QName(XKMS2_NS,
            ELEM_AUTHENTICATION, XKMS2_PREFIX);

    public static final QName Q_ELEM_REGISTER_REQUEST = new QName(XKMS2_NS,
            ELEM_REGISTER_REQUEST, XKMS2_PREFIX);

    public static final QName Q_ELEM_PROTOTYPE_KEY_BINDING = new QName(
            XKMS2_NS, ELEM_PROTOTYPE_KEY_BINDING, XKMS2_PREFIX);

    public static final QName Q_ELEM_REGISTER_RESULT = new QName(XKMS2_NS,
            ELEM_REGISTER_RESULT, XKMS2_PREFIX);

    public static final QName Q_ELEM_PRIVATE_KEY = new QName(XKMS2_NS,
            ELEM_PRIVATE_KEY, XKMS2_PREFIX);

    public static final QName Q_ELEM_RECOVER_KEY_BINDING = new QName(XKMS2_NS,
            ELEM_RECOVER_KEY_BINDING, XKMS2_PREFIX);

    public static final QName Q_ELEM_RECOVER_REQUEST = new QName(XKMS2_NS,
            ELEM_RECOVER_REQUEST, XKMS2_PREFIX);

    public static final QName Q_ELEM_RECOVER_RESULT = new QName(XKMS2_NS,
            ELEM_RECOVER_RESULT, XKMS2_PREFIX);

    public static final QName Q_ELEM_REISSUE_REQUEST = new QName(XKMS2_NS,
            ELEM_REISSUE_REQUEST, XKMS2_PREFIX);

    public static final QName Q_ELEM_REISSUE_KEY_BINDING = new QName(XKMS2_NS,
            ELEM_REISSUE_KEY_BINDING, XKMS2_PREFIX);

    public static final QName Q_ELEM_REISSUE_RESULT = new QName(XKMS2_NS,
            ELEM_REISSUE_RESULT, XKMS2_PREFIX);

    public static final QName Q_ELEM_REVOKE_REQUEST = new QName(XKMS2_NS,
            ELEM_REVOKE_REQUEST, XKMS2_PREFIX);

    public static final QName Q_ELEM_REVOKE_KEY_BINDING = new QName(XKMS2_NS,
            ELEM_REVOKE_KEY_BINDING, XKMS2_PREFIX);

    public static final QName Q_ELEM_REVOCATION_CODE = new QName(XKMS2_NS,
            ELEM_REVOCATION_CODE, XKMS2_PREFIX);

    public static final QName Q_ELEM_REVOKE_RESULT = new QName(XKMS2_NS,
            ELEM_REVOKE_RESULT, XKMS2_PREFIX);

    public static final QName Q_ELEM_RSA_KEY_PAIR = new QName(XKMS2_NS,
            ELEM_RSA_KEY_PAIR, XKMS2_PREFIX); 
    
    public static final String TAG_RSA_KEY_PAIR = "RSAKeyPair";

    public static final String EXECUTOR_MAP = "local_EXECUTOR_MAP";
    
    public static final String XKMS_EXECUTOR_IMPL = "xkms.excutor.impls";

}
