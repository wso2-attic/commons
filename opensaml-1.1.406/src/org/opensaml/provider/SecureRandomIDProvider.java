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

package org.opensaml.provider;

import java.security.SecureRandom;

import org.apache.commons.codec.binary.Hex;
import org.opensaml.SAMLException;
import org.opensaml.SAMLIdentifier;

/**
 *  Generation of identifiers for SAML objects using SecureRandom
 *
 * @author     Scott Cantor
 * @created    January 31, 2005
 */
public class SecureRandomIDProvider implements SAMLIdentifier
{
    private static SecureRandom random = new SecureRandom();

    public SecureRandomIDProvider() {
    }
        
    /**
     * @see org.opensaml.SAMLIdentifier#getIdentifier()
     */
    public synchronized String getIdentifier() throws SAMLException {
        byte[] buf=new byte[16];
        random.nextBytes(buf);
        return "_".concat(new String(Hex.encodeHex(buf)));
    }

    /**
     * @see org.opensaml.SAMLIdentifier#generateRandomBytes(java.security.SecureRandom, int)
     */
    public byte[] generateRandomBytes(SecureRandom random, int n) {
        byte[] bytes = new byte[n];
        random.nextBytes( bytes );
        return bytes;
    }

    /**
     * @see org.opensaml.SAMLIdentifier#generateRandomBytes(int)
     */
    public synchronized byte[] generateRandomBytes(int n) {
        return generateRandomBytes(random, n);
    }
}
