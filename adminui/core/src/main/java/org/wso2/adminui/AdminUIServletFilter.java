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
package org.wso2.adminui;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 *
 */
public class AdminUIServletFilter implements Filter {
    protected static final Log log = LogFactory.getLog(AdminUIServletFilter.class);
    private ServletContext servletContext;
    private FilterConfig filterConfig;
    //context root could be "" or some other value
    private String contextRoot;
    private final static String GLOBAL_PARAMS_JS = "global_params.js";

    public void init() throws ServletException {
        if (filterConfig != null) {
            init(filterConfig);
        }
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();
        this.servletContext.setAttribute(this.getClass().getName(), this);
    }

    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        try {

            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            if (contextRoot == null) {
                //Context root could be "" or some othervalue
                this.contextRoot = httpServletRequest.getContextPath();
            }

            String requestURI = httpServletRequest.getRequestURI();
            int indexOfDot = requestURI.lastIndexOf(".");
            boolean isFile = false;
            if (indexOfDot != -1) {
                isFile = requestURI.substring(indexOfDot).matches("\\.(.)*");
            }
            if (!isFile &&
                requestURI.lastIndexOf("/") != requestURI.length() - 1) {
                requestURI += "/";
            }
            Map generatedPages =
                    (Map) servletContext.getAttribute(AdminUIConstants.GENERATED_PAGES);
            if (requestURI.equals(contextRoot) || requestURI.equals(contextRoot + "/")) {
                response.setContentType("text/html");
                boolean enableConsole =
                        ((Boolean) servletContext.getAttribute(AdminUIConstants.ENABLE_CONSOLE)).booleanValue();
                if (!enableConsole) {
                    ServletOutputStream out = response.getOutputStream();
                    out.write(("<b>Management Console has been disabled.</b> " +
                               "Enable it in the server.xml and try again.").getBytes());
                    ((HttpServletResponse) response).setStatus(HttpServletResponse.SC_FORBIDDEN);
                    out.flush();
                    out.close();
                    return;
                }
                String fileContents = (String) generatedPages.get("index.html");
                if (fileContents != null) {
                    ServletOutputStream op = response.getOutputStream();
                    response.setContentLength(fileContents.getBytes().length);
                    op.write(fileContents.getBytes());
                    return;
                }
            } else {
                String urlKey;
                if (contextRoot.equals("/")) {
                    urlKey = requestURI.substring(contextRoot.length(),
                                                  requestURI.length());
                } else {
                    urlKey = requestURI.substring(1 + contextRoot.length(),
                                                  requestURI.length());
                }

                if (generatedPages != null) {
                    String fileContents = (String) generatedPages.get(urlKey);
                    if (fileContents != null) {
                        ServletOutputStream op = response.getOutputStream();
                        response.setContentType("text/html");
                        response.setContentLength(fileContents.getBytes().length);
                        op.write(fileContents.getBytes());
                        return;
                    }
                }

                /*
                 || has been used to support any client who wants to access the "global_params.js"
                 regardless of where they want to access.
                */
                if (urlKey.equals(GLOBAL_PARAMS_JS) || urlKey.indexOf(GLOBAL_PARAMS_JS) > -1) {
                    initGlobalParams((HttpServletResponse) response);
                    return;
                }
            }
        } catch (Exception e) {
            String msg = "Exception occurred while processing Request";
            log.error(msg, e);
            throw new ServletException(msg, e);
        }
        filterChain.doFilter(request, response);
    }

    private void initGlobalParams(HttpServletResponse response) throws IOException {
        String localContext = AdminUIServletContextListener.contextPath;
        
        String servletCtxPath =
                (String) servletContext.getAttribute(AdminUIConstants.SERVICE_CONTEXT_PATH);
        String httpPort =
                (String) servletContext.getAttribute(AdminUIConstants.HTTP_PORT);
        String httpsPort =
                (String) servletContext.getAttribute(AdminUIConstants.HTTPS_PORT);

        String globalParamString = "SERVICE_PATH = \"" + servletCtxPath + "\";\n" +
                                   "ROOT_CONTEXT = \"" + localContext + "\";\n" +
                                   "HTTP_PORT = " + httpPort + ";\n" +
                                   "HTTPS_PORT = " + httpsPort + ";\n";
        ServletOutputStream op = response.getOutputStream();
        response.setContentType("text/html");
        response.setContentLength(globalParamString.getBytes().length);
        op.write(globalParamString.getBytes());

    }

    public void destroy() {
    }
}
