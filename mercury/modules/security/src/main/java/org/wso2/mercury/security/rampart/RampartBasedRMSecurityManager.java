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
package org.wso2.mercury.security.rampart;

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.Constants;
import org.apache.axis2.client.Options;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.AxisModule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.neethi.Policy;
import org.apache.rahas.RahasConstants;
import org.apache.rahas.SimpleTokenStore;
import org.apache.rahas.Token;
import org.apache.rahas.TokenStorage;
import org.apache.rahas.TrustException;
import org.apache.rahas.TrustUtil;
import org.apache.rahas.client.STSClient;
import org.apache.rampart.RampartException;
import org.apache.rampart.RampartMessageData;
import org.apache.rampart.policy.RampartPolicyBuilder;
import org.apache.rampart.policy.RampartPolicyData;
import org.apache.rampart.util.RampartUtil;
import org.apache.ws.secpolicy.WSSPolicyException;
import org.apache.ws.secpolicy.model.SecureConversationToken;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSDerivedKeyTokenPrincipal;
import org.apache.ws.security.WSSecurityEngineResult;
import org.apache.ws.security.conversation.ConversationConstants;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.apache.ws.security.handler.WSHandlerResult;
import org.apache.ws.security.message.token.Reference;
import org.apache.ws.security.message.token.SecurityTokenReference;
import org.apache.ws.security.message.token.SecurityContextToken;

import org.wso2.mercury.security.RMSecurityManager;
import org.wso2.mercury.security.SecurityToken;
import org.wso2.mercury.exception.RMSecurityException;
import org.wso2.mercury.util.MercuryConstants;

import javax.xml.namespace.QName;

import java.security.Principal;
import java.util.List;
import java.util.Vector;
import java.util.Iterator;


public class RampartBasedRMSecurityManager extends RMSecurityManager {

    private static final Log log = LogFactory.getLog(RampartBasedRMSecurityManager.class);

    TokenStorage storage = null;

    /**
     * @param context
     */
    public RampartBasedRMSecurityManager(ConfigurationContext context) {
        super(context);

        this.storage = (TokenStorage) context
                .getProperty(TokenStorage.TOKEN_STORAGE_KEY);
        if (this.storage == null) {
            this.storage = new SimpleTokenStore();
            context.setProperty(TokenStorage.TOKEN_STORAGE_KEY, this.storage);
        }
    }

    public void checkProofOfPossession(SecurityToken token, MessageContext message)
            throws RMSecurityException {

        Vector results = null;
        if ((results = (Vector) message
                .getProperty(WSHandlerConstants.RECV_RESULTS)) == null) {
            throw new RMSecurityException("No Security results");
        } else {

            RampartSecurityToken storedToken = (RampartSecurityToken) token;
            WSHandlerResult wsHandlerResult = null;
            WSSecurityEngineResult wsSecurityEngineResult = null;
            for (Iterator handlerIter = results.iterator(); handlerIter.hasNext();) {
                wsHandlerResult = (WSHandlerResult) handlerIter.next();
                for (Iterator engineResultIter = wsHandlerResult.getResults().iterator(); engineResultIter.hasNext();)
                {
                    wsSecurityEngineResult = (WSSecurityEngineResult) engineResultIter.next();
                    Integer actInt
                            = (Integer) wsSecurityEngineResult.get(WSSecurityEngineResult.TAG_ACTION);
                    if (WSConstants.SIGN == actInt.intValue()) {

                        Principal principal = (Principal) wsSecurityEngineResult.get(WSSecurityEngineResult.TAG_PRINCIPAL);
                        if (principal instanceof WSDerivedKeyTokenPrincipal) {
                            String baseTokenId = ((WSDerivedKeyTokenPrincipal) principal).getBasetokenId();
                            try {
                                Token usedToken = this.storage.getToken(baseTokenId);
                                if (!isEqual(usedToken.getSecret(), storedToken.getToken().getSecret()))
                                {
                                    throw new RMSecurityException("Stored security token is not match with the " +
                                            " security token for this message");
                                }
                            } catch (TrustException e) {
                                throw new RMSecurityException("Can not get the security token from the storage");
                            }
                        }
                    }
                }

            }
        }
    }

    private boolean isEqual(byte[] secret1, byte[] secret2) {
        boolean isEqual = false;
        if ((secret1 != null) && (secret2 != null)) {
            if (secret1.length == secret2.length) {
                isEqual = true;
                for (int i = 0; i < secret1.length; i++) {
                   if (secret1[i] != secret2[i]){
                       isEqual = false;
                       break;
                   }
                }
            }
        }
        return isEqual;
    }

    private String getUriFromSTR(OMElement str) {
        OMElement refElem = str.getFirstChildWithName(Reference.TOKEN);
        return refElem.getAttributeValue(new QName("URI")).substring(1);
    }

    /* (non-Javadoc)
      * @see org.apache.sandesha2.security.RMSecurityManager#createSecurityTokenReference(org.apache.sandesha2.security.SecurityToken, org.apache.axis2.context.MessageContext)
      */
    public OMElement createSecurityTokenReference(SecurityToken token,
                                                  MessageContext message) throws RMSecurityException {

        OMFactory fac = OMAbstractFactory.getOMFactory();

        RampartSecurityToken rampartToken = (RampartSecurityToken) token;
        OMElement element = rampartToken.getToken().getAttachedReference();
        if (element == null) {
            element = rampartToken.getToken().getUnattachedReference();
        }

        if (element == null) {
            //Now use the token id and construct the ref element
            element = fac.createOMElement(
                    SecurityTokenReference.SECURITY_TOKEN_REFERENCE,
                    WSConstants.WSSE_LN, WSConstants.WSSE_PREFIX);
            OMElement refElem = fac.createOMElement(Reference.TOKEN, element);
            refElem.addAttribute("ValueType",
                    "http://schemas.xmlsoap.org/ws/2005/02/sc/sct", null);
            refElem.addAttribute("URI", rampartToken.getToken().getId(), null);
        }

        return this.convertOMElement(fac, element);
    }

    /* (non-Javadoc)
      * @see org.apache.sandesha2.security.RMSecurityManager#getSecurityToken(org.apache.axis2.context.MessageContext)
      */
    public SecurityToken getSecurityToken(MessageContext message)
            throws RMSecurityException {
        String contextIdentifierKey = RampartUtil
                .getContextIdentifierKey(message);
        String identifier = (String) RampartUtil.getContextMap(message).get(
                contextIdentifierKey);

        if (identifier == null && !message.isServerSide()) {
            try {
                OMElement rstTmpl = RampartUtil.createRSTTempalteForSCT(
                        ConversationConstants.VERSION_05_02,
                        RahasConstants.VERSION_05_02);

                String action = TrustUtil.getActionValue(
                        RahasConstants.VERSION_05_02,
                        RahasConstants.RST_ACTION_SCT);

                Policy servicePolicy = message.getEffectivePolicy();

                if (servicePolicy == null) {
                    //Missing service policy means no security requirement
                    return null;
                }
                List it = (List) servicePolicy.getAlternatives().next();
                RampartPolicyData rpd = RampartPolicyBuilder.build(it);

                SecureConversationToken secConvTok = null;

                org.apache.ws.secpolicy.model.Token encrtok = rpd
                        .getEncryptionToken();
                secConvTok = (encrtok != null && encrtok instanceof SecureConversationToken) ? (SecureConversationToken) encrtok
                        : null;

                if (secConvTok == null) {
                    org.apache.ws.secpolicy.model.Token sigtok = rpd
                            .getSignatureToken();
                    secConvTok = (sigtok != null && sigtok instanceof SecureConversationToken) ? (SecureConversationToken) sigtok
                            : null;
                }

                if (secConvTok != null) {

                    Policy issuerPolicy = secConvTok.getBootstrapPolicy();
                    issuerPolicy.addAssertion(rpd.getRampartConfig());

                    STSClient client = new STSClient(message
                            .getConfigurationContext());
                    Options op = new Options();
                    op.setProperty(MercuryConstants.PROCESS_RM_CONTROL_MESSAGE, Constants.VALUE_TRUE);
                    client.setOptions(op);
                    client.setAction(action);
                    client.setRstTemplate(rstTmpl);
                    client.setCryptoInfo(RampartUtil.getEncryptionCrypto(rpd
                            .getRampartConfig(), message.getAxisService()
                            .getClassLoader()), RampartUtil.getPasswordCB(
                            message, rpd));
                    String address = message.getTo().getAddress();
                    Token tok = client.requestSecurityToken(servicePolicy,
                            address, issuerPolicy, null);

                    tok.setState(Token.ISSUED);
                    this.storage.add(tok);

                    contextIdentifierKey = RampartUtil
                            .getContextIdentifierKey(message);
                    RampartUtil.getContextMap(message).put(
                            contextIdentifierKey, tok.getId());
                    identifier = tok.getId();

                } else {
                    log.debug("No Security Conversation In Policy");
                    return null;
                }

            } catch (RampartException e) {
                throw new RMSecurityException(e.getMessage(), e);
            } catch (TrustException e) {
                throw new RMSecurityException(e.getMessage(), e);
            } catch (WSSPolicyException e) {
                throw new RMSecurityException(e.getMessage(), e);
            }
        }

        return this.recoverSecurityToken(identifier);

    }

    /* (non-Javadoc)
      * @see org.apache.sandesha2.security.RMSecurityManager#getSecurityToken(org.apache.axiom.om.OMElement, org.apache.axis2.context.MessageContext)
      */
    public SecurityToken getSecurityToken(OMElement theSTR,
                                          MessageContext message) throws RMSecurityException {

        OMElement refElem = theSTR.getFirstChildWithName(Reference.TOKEN);
        String id = refElem.getAttributeValue(new QName("URI"));
        String tokenId = id;
        if (!id.startsWith("urn:") && id.startsWith("#")) {
            tokenId = tokenId.substring(1);
        }
        return this.recoverSecurityToken(tokenId);
    }

    /* (non-Javadoc)
      * @see org.apache.sandesha2.security.RMSecurityManager#getTokenRecoveryData(org.apache.sandesha2.security.SecurityToken)
      */
    public String getTokenRecoveryData(SecurityToken token)
            throws RMSecurityException {
        String id = ((RampartSecurityToken) token).getToken().getId();
        if (!id.startsWith("urn:") && id.startsWith("#")) {
            id = id.substring(1);
        }
        return id;
    }

    /* (non-Javadoc)
      * @see org.apache.sandesha2.security.RMSecurityManager#initSecurity(org.apache.axis2.description.AxisModule)
      */
    public void initSecurity(AxisModule moduleDesc) {
    }

    /* (non-Javadoc)
      * @see org.apache.sandesha2.security.RMSecurityManager#recoverSecurityToken(java.lang.String)
      */
    public SecurityToken recoverSecurityToken(String tokenData)
            throws RMSecurityException {
        try {
            Token token = this.storage.getToken(tokenData);
            if (token != null) {
                return new RampartSecurityToken(token);
            } else {
                throw new RMSecurityException("Error Retrieving Security Token");
            }
        } catch (TrustException e) {
            throw new RMSecurityException("Error Retrieving Security Token");
        }
    }

    private OMElement convertOMElement(OMFactory fac, OMElement elem) {
        return new StAXOMBuilder(fac, elem.getXMLStreamReader())
                .getDocumentElement();
    }

    public void applySecurityToken(SecurityToken token,
                                   MessageContext outboundMessage) throws RMSecurityException {
        // TODO If there are any properties that should be put onto the outbound message
        // to ensure that the correct token is used to secure it, then they should be
        // added now.
    }
}
