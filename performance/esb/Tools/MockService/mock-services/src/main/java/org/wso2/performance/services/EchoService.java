/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *   * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.wso2.performance.services;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.List;
import java.util.ArrayList;

/**
 * A simple http/s servlet that will echo the given message using the same content type of the
 * request. Internally uses a ByteBuffer to optimize on performance, and "knows" to detect the
 * size this buffer to be allocated for optimal performance
 *
 * @author Asankha Perera (asankha AT wso2 DOT com)
 *
 * 11 June 2008: Enhance to add an optional 'smart' delay before responding.. again uses the SOAPAction
 * 20 June 2008: Allow the buffer(s) to be dynamically created to support large messages
 */
public class EchoService extends HttpServlet {

    private static final int DEFAULT_BUFFER_SIZE = 4;

    public void doPost(HttpServletRequest request, HttpServletResponse response) {

        ByteBuffer bb = null;
        List<ByteBuffer> bbList = null;

        try {
            String soapAction = request.getHeader("SOAPAction");
            if (soapAction != null && soapAction.startsWith("\"")) {
                soapAction = soapAction.replaceAll("\"", "");
            }
            int dotPos = soapAction.indexOf(".");
            int secondDotPos = (dotPos == -1 ? -1 : soapAction.indexOf(".", dotPos+1));

            int bufKBytes = DEFAULT_BUFFER_SIZE;
            int delaySecs = 0;

            if (secondDotPos > 0) {
                bufKBytes = Integer.parseInt(soapAction.substring(dotPos+1, secondDotPos));
                delaySecs = Integer.parseInt(soapAction.substring(secondDotPos+1));
            } else if (dotPos > 0) {
                bufKBytes = Integer.parseInt(soapAction.substring(dotPos+1));
            }

            bb = ByteBuffer.allocate(bufKBytes * 1024);
            ReadableByteChannel rbc = Channels.newChannel(request.getInputStream());

            int len, tot = 0;
            while ((len = rbc.read(bb)) > 0) {
                tot += len;
                if (tot >= bb.capacity()) {
                    // --- auto expand logic ---
                    if (bbList == null) {
                        bbList = new ArrayList<ByteBuffer>();
                    }
                    bb.flip();
                    bbList.add(bb);
                    bufKBytes = bufKBytes * 2;
                    bb = ByteBuffer.allocate(bufKBytes * 1024);
                }
            }
            bb.flip();

            // --- auto expand logic ---
            if (bbList != null) {
                bbList.add(bb);
            }


            if (delaySecs > 0) {
                Thread.sleep(delaySecs * 1000);
            } else {
                Thread.sleep(10);
            }

            response.setContentType(request.getContentType());

            OutputStream out = response.getOutputStream();
            WritableByteChannel wbc = Channels.newChannel(out);

            if (bbList == null) {
                while ((len = wbc.write(bb)) > 0);

            } else {
                // --- auto expand logic ---
                for (ByteBuffer b : bbList) {
                    while ((len = wbc.write(b)) > 0);
                }
            }
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

