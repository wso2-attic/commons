/*
 * Copyright 2005,2006 WSO2, Inc. http://www.wso2.org
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
package org.wso2.utils.transport.http;

import org.mortbay.util.IO;
import org.mortbay.log.Log;
import org.mortbay.jetty.HttpMethods;
import org.mortbay.jetty.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.io.IOException;

/**
 * This is a Jetty related Favicon providing class
 */
public class FaviconProvider {

    private long faviconModified = (System.currentTimeMillis() / 1000) * 1000;
    private byte[] favicon;
    private boolean serveIcon = true;

    /**
     * Ex: org/wso2/wsas/transport/jetty/favicon.ico
     *
     * @param relativePath
     */
    public void setFavIconFromResource(String relativePath) throws IOException {
        try {
            URL fav = this.getClass().getClassLoader()
                    .getResource(relativePath);
            if (fav != null) {
                favicon = IO.readBytes(fav.openStream());
            }
        } catch (IOException e) {
            Log.warn(e);
            throw e;
        }
    }

    public boolean provideFavIcon(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String method = request.getMethod();
        if (serveIcon && favicon != null && method.equals(HttpMethods.GET) &&
            request.getRequestURI().equals("/favicon.ico")) {
            if (request.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE) == faviconModified) {
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("image/x-icon");
                response.setContentLength(favicon.length);
                response.setDateHeader(HttpHeaders.LAST_MODIFIED, faviconModified);
                response.getOutputStream().write(favicon);
            }
            return true;
        } else {
            return false;
        }

    }
}
