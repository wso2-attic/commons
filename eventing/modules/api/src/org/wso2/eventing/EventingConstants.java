/*
 * Copyright 2005-2008 WSO2, Inc. (http://wso2.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.eventing;

public class EventingConstants {
    public static final String WSE_EVENTING_NS = "http://schemas.xmlsoap.org/ws/2004/08/eventing";
    public static final String WSE_EXTENDED_EVENTING_NS = "http://ws.apache.org/ws/2007/05/eventing-extended";
    public static final String WSE_EVENTING_PREFIX = "wse";
    public static final String WSE_DEFAULT_DELIVERY_MODE = "http://schemas.xmlsoap.org/ws/2004/08/eventing/DeliveryModes/Push";    

    //Actions
    public static final String WSE_SUBSCRIBE = "http://schemas.xmlsoap.org/ws/2004/08/eventing/Subscribe";
    public static final String WSE_SUBSCRIBE_RESPONSE = "http://schemas.xmlsoap.org/ws/2004/08/eventing/SubscribeResponse";

    /**
     * @deprecated A typo in this field name has been corrected. Use {@link #WSE_SUBSCRIBE_RESPONSE} instead.
     */
    public static final String WSE_SUbSCRIBE_RESPONSE = "http://schemas.xmlsoap.org/ws/2004/08/eventing/SubscribeResponse";
    public static final String WSE_RENEW = "http://schemas.xmlsoap.org/ws/2004/08/eventing/Renew";
    public static final String WSE_RENEW_RESPONSE = "http://schemas.xmlsoap.org/ws/2004/08/eventing/RenewResponse";
    public static final String WSE_UNSUBSCRIBE = "http://schemas.xmlsoap.org/ws/2004/08/eventing/Unsubscribe";
    public static final String WSE_UNSUBSCRIBE_RESPONSE = "http://schemas.xmlsoap.org/ws/2004/08/eventing/UnsubscribeResponse";
    public static final String WSE_GET_STATUS = "http://schemas.xmlsoap.org/ws/2004/08/eventing/GetStatus";
    public static final String WSE_GET_STATUS_RESPONSE ="http://schemas.xmlsoap.org/ws/2004/08/eventing/GetStatusResponse";
    public static final String WSE_SUBSCRIPTIONEND = "http://schemas.xmlsoap.org/ws/2004/08/eventing/SubscriptionEnd";
    public static final String WSE_PUBLISH = "http://ws.apache.org/ws/2007/05/eventing-extended/Publish";


    //Elements
    public static final String WSE_EN_SUBSCRIBE = "Subscribe";
    public static final String WSE_EN_END_TO = "EndTo";
    public static final String WSE_EN_DELIVERY = "Delivery";
    public static final String WSE_EN_MODE = "Mode";
    public static final String WSE_EN_NOTIFY_TO = "NotifyTo";
    public static final String WSE_EN_EXPIRES = "Expires";
    public static final String WSE_EN_FILTER = "Filter";
    public static final String WSE_EN_DIALECT = "Dialect";
    public static final String WSE_EN_SUBSCRIBE_RESPONSE = "SubscribeResponse";
    public static final String WSE_EN_SUBSCRIPTION_MANAGER = "SubscriptionManager";
    public static final String WSE_EN_RENEW = "Renew";
    public static final String WSE_EN_RENEW_RESPONSE = "RenewResponse";
    public static final String WSE_EN_IDENTIFIER = "Identifier";
    public static final String WSE_EN_UNSUBSCRIBE = "Unsubscribe";
    public static final String WSE_EN_GET_STATUS = "GetStatus";
    public static final String WSE_EN_GET_STATUS_RESPONSE = "GetStatusResponse";
    public static final String WSE_EN_TOPIC = "topic";
    public static final String WSE_EN_XPATH = "XPath";
    public static final String WSE_EN_SUBSCRIPTIONEND = "SubscriptionEnd";

    //Faults
    public static final String WSA_FAULT = "http://schemas.xmlsoap.org/ws/2004/08/addressing/fault";
    public static final String WSE_FAULT_CODE_SENDER = "Sender";
    public static final String WSE_FAULT_CODE_RECEIVER = "Receiver";
    public static final String WSE_FAULT_EN_FAULT = "Fault";
    public static final String WSE_FAULT_EN_CODE = "Code";
    public static final String WSE_FAULT_EN_SUB_CODE = "Subcode";
    public static final String WSE_FAULT_EN_REASON = "Reason";
    public static final String WSE_FAULT_EN_DETAIL = "Detail";
    public static final String WSE_FAULT_EN_VALUE = "Value";
    public static final String WSE_FAULT_EN_TEXT = "Text";
    public static final String WSE_FAULT_EN_TEXT_ATTR = "lang";
    
    public static final String SUBSCRIPTION_MANAGER = "subscriptionManager";

    //Operations
    public static final String WSE_SUBSCRIBE_OP = "SubscribeOp";
    public static final String WSE_RENEW_OP = "RenewOp";
    public static final String WSE_UNSUBSCRIBE_OP = "UnsubscribeOp";
    public static final String WSE_GET_STATUS_OP = "GetStatusOp";
    public static final String WSE_SUBSCRIPTIONEND_OP = "SubscriptionEnd";

}
