/*
 * Copyright 2005-2007 WSO2, Inc. (http://wso2.com)
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

import java.util.Date;
import java.util.Calendar;

/**
 * A Subscription represents a given subscription to an event source.  Any addition to the values
 * descibed in the Subscrion can store using the SubscriptionData.
 */
public class Subscription {
    private SubscriptionData subData;
    private String endpointUrl;
	private String deliveryMode;
	private String id = null;
	private Calendar expires = null;
    private String addressUrl = null;
    private String filterDialect = null;
    private String filterValue = null;
    private String subManUrl = null;
    private boolean staticEntry;

    /**
     * Set subscription metadata
     * @param subData
     */
    public void setSubscriptionData(SubscriptionData subData){
        this.subData = subData;
    }
    /**
     * Get the subscription data
     * @return Subscription Data
     */
    public SubscriptionData getSubscriptionData(){
        return subData;
    }

    public String getEndpointUrl() {
        return endpointUrl;
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public String getDeliveryMode() {
        return deliveryMode;
    }

    public String getFilterDialect() {
        return filterDialect;
    }

    public void setFilterDialect(String filterDialect) {
        this.filterDialect = filterDialect;
    }

    public String getFilterValue() {
        return filterValue;
    }

    public void setFilterValue(String filterValue) {
        this.filterValue = filterValue;
    }

    public String getSubManUrl() {
        return subManUrl;
    }

    public void setSubManUrl(String subManUrl) {
        this.subManUrl = subManUrl;
    }

    public void setDeliveryMode(String deliveryMode) {
        this.deliveryMode = deliveryMode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Calendar getExpires() {
        return expires;
    }

    public void setExpires(Calendar expires) {
        this.expires = expires;
    }

        public String getAddressUrl() {
        return addressUrl;
    }

    public void setAddressUrl(String addressUrl) {
        this.addressUrl = addressUrl;
    }

    public boolean isStaticEntry() {
        return staticEntry;
    }

    public void setStaticEntry(boolean staticEntry) {
        this.staticEntry = staticEntry;
    }
}
