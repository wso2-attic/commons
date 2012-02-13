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
package org.wso2.carbon.common.test.utils.client.httpclient;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.OptionsMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;


import java.io.IOException;


/**
 * Implementation class of HTTPClientHandler interface
 */
public class HTTPClient implements HTTPClientHandler {
    private static HttpClient httpClient;

    /**
     * This method used to send HTTP Get call
     *
     * @param uri URL of HTTP Request
     * @return HTTP Get Object
     */
    public GetMethod httpGet(String uri) {
        try {
            httpClient = new HttpClient();
            GetMethod get = new GetMethod(uri);
            httpClient.executeMethod(get);
            return get;
        } catch (IOException e) {
            throw new RuntimeException("Exception occurred while invoking HTTP GET method : " + e);
        }
    }

    /**
     * This method used to send HTTP Head call
     *
     * @param uri URL of HTTP Request
     * @return HTTP Head object
     */
    public HeadMethod httpHead(String uri) {
        try {
            httpClient = new HttpClient();
            HeadMethod head = new HeadMethod(uri);
            httpClient.executeMethod(head);
            return head;
        } catch (IOException e) {
            throw new RuntimeException("Exception occurred while invoking HTTP HEAD method : " + e);
        }
    }

    /**
     * This method used to send HTTP Post call
     *
     * @param uri URL of HTTP Request
     * @return HTTP Post object
     */
    public PostMethod httpPost(String uri) {
        try {
            httpClient = new HttpClient();
            PostMethod post = new PostMethod(uri);
            httpClient.executeMethod(post);
            return post;
        } catch (IOException e) {
            throw new RuntimeException("Exception occurred while invoking HTTP POST method : " + e);
        }
    }

    /**
     * This method used to send HTTP Put call
     *
     * @param uri URL of HTTP Request
     * @return HTTP PUT object
     */
    public PutMethod httpPut(String uri) {
        try {
            httpClient = new HttpClient();
            PutMethod put = new PutMethod(uri);
            httpClient.executeMethod(put);
            return put;
        } catch (IOException e) {
            throw new RuntimeException("Exception occurred while invoking HTTP PUT method : " + e);
        }
    }

    /**
     * This method used to send HTTP Delete call
     *
     * @param uri URL of HTTP Request
     * @return HTTP Delete object
     */
    public DeleteMethod httpDelete(String uri) {
        try {
            httpClient = new HttpClient();
            DeleteMethod delete = new DeleteMethod(uri);
            httpClient.executeMethod(delete);
            return delete;
        } catch (IOException e) {
            throw new RuntimeException("Exception occurred while invoking HTTP DELETE method : " + e);
        }
    }

    /**
     * This method used to send HTTP Option call
     *
     * @param uri URL of HTTP Request
     * @return HTTP OPTIONS Object
     */
    public OptionsMethod httpOption(String uri) {
        try {
            httpClient = new HttpClient();
            OptionsMethod option = new OptionsMethod(uri);
            httpClient.executeMethod(option);
            return option;
        } catch (IOException e) {
            throw new RuntimeException("Exception occurred while invoking HTTP OPTIONS method : " + e);
        }
    }
}
