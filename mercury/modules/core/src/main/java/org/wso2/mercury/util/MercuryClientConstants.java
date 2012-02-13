/*
 * Copyright 2008,2009 WSO2, Inc. http://www.wso2.org.
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
package org.wso2.mercury.util;

/**
 * this interface defines contants that can be used by the client
 * to set various options for the WSO2RM
 */
public interface MercuryClientConstants {
    // clients can use this key to indicate that set of messags
    // belongs to the same sequence.
    public static final String INTERNAL_KEY = "MercuryInternalKey";
    public static final String LAST_MESSAGE = "MercuryLastMessage";

    public static final String TERMINATE_MESSAGE = "MercuryTerminateMessage";
    // this is used to resume a session at client side which has dided.
    // it is users responsibility to examine the RMS_SEQUENCE table and start the
    // sequences that has not finished due to an client crash.
    public static final String RESUME_SEQUENCE ="MercuryResumeSequence";

    // this is used to ask RMS to offer an sequence for response chanel
    public static final String SEQUENCE_OFFER = "MercurySequenceOffer";

    // this is used to get any notifications for sequence problems.
    public static final String ERROR_CALLBACK = "MercuryErrorCallback";
    public static final String PERSISTANCE_CALLBACK = "MercuryPersistanceCallback";

    public static final String USE_SECURE_RM = "MercuryUseSecureRM";

    public static final String TERMINATE_CALLBACK = "MercuryTerminateCallback";

}


