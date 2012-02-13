/*
 *  Copyright 2001-2005 Internet2
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.artifact;

import java.io.UnsupportedEncodingException;


/**
 * A simple class to represent URIs.
 * 
 * @author Chad La Joie
 */
/*
 *  This class is here only because we need to support JDK 1.3,
 *  using the JDK 1.4 java.net.URI class would be much nicer.
 */
public class URI {

    private String uri;
    
    /**
     * Constructor.
     */
    public URI() {
        
    }
    
    /**
     * Constructor.
     * 
     * @param uri the URI
     */
    public URI(String uri) {
        setURI(uri);
    }
    
    /**
     * Constructor.
     * 
     * @param uri the URI in byte array format
     * @param encoding the encoding to use when converting to a string
     * 
     * @throws UnsupportedEncodingException thrown if the encoding is not supported by the VM
     */
    public URI(byte[] uri, String encoding) throws UnsupportedEncodingException {
        setURI(uri, encoding);
    }
    
    /**
     * Gets the URI as a string.
     * 
     * @return the URI
     */
    public String getURI() {
        return toString();
    }
    
    /**
     * Sets the URI.
     * 
     * @param uri the URI
     */
    public void setURI(String uri) {
        this.uri = uri;
    }
    
    /**
     * Sets the URI.
     * 
     * @param uri the URI in byte array format
     * @param encoding the encoding to use when converting to a string
     * 
     * @throws UnsupportedEncodingException thrown if the encoding is not supported by the VM
     */
    public void setURI(byte[] uri, String encoding) throws UnsupportedEncodingException {
        this.uri = new String(uri, encoding);
    }
    
    /**
     * Gets the URI as a string.
     * 
     * @return the URI
     */
    public String toString() {
        return uri;
    }
    
    /**
     * Gets the URI as an array of bytes.
     * 
     * @return the URI
     */
    public byte[] toBytes() {
        return uri.getBytes();
    }
    
    /**
     * Checks to see if this URI is the same as the given URI
     * 
     * @param otherUri the other URI
     * 
     * @return true if thier equal, false if not
     */
    public boolean equals(URI otherUri) {
        return uri.equals(otherUri.getURI());
    }
}
