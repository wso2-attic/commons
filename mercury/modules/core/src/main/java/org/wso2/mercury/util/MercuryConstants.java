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
 * this interface is used to keep the WSO2RM specific internal
 * constants
 */
public interface MercuryConstants {

    public static final String RMS_CONTEXT = "MercuryRMSContext";
    public static final String RMD_CONTEXT = "MercuryRMDContext";
    public static final String RM_DISPATCH_INFO = "RMDispatchInfo";

    public static final String DEFAULT_INTERNAL_KEY = "DefaultInternalKey";

    //mercury message element names and namespaces
    public static final String RM_1_0_NAMESPACE = "http://schemas.xmlsoap.org/ws/2005/02/rm";
    public static final String RM_1_0_NAMESPACE_PREFIX = "wsrm";

    //create sequence message element names
    public static final String CREATE_SEQUENCE = "CreateSequence";
    public static final String CREATE_SEQUENCE_RESPONSE = "CreateSequenceResponse";
    public static final String ACKS_TO = "AcksTo";
    public static final String ACCEPT = "Accept";
    public static final String ADDRESS = "Address";
    public static final String OFFER = "Offer";
    public static final String IDENTIFIER = "Identifier";
    public static final String SEQUENCE = "Sequence";
    public static final String MESSAGE_NUMBER = "MessageNumber";
    public static final String LAST_MESSAGE = "LastMessage";
    public static final String ACKKNOWLEDGEMENT_RANGE = "AcknowledgementRange";
    public static final String UPPER = "Upper";
    public static final String LOWER = "Lower";
    public static final String SEQUENCE_ACKNOWLEDGMENT = "SequenceAcknowledgement";
    public static final String TERMINATE_SEQUENCE = "TerminateSequence";
    public static final String ACKREQUESTED = "AckRequested";
    public static final String SECURITY_TOKEN_REFERENCE = "SecurityTokenReference";

    //mercury actions
    public static final String CREATE_SEQUENCE_ACTION = "http://schemas.xmlsoap.org/ws/2005/02/rm/CreateSequence";
    public static final String CREATE_SEQUENCE_RESPONSE_ACTION = "http://schemas.xmlsoap.org/ws/2005/02/rm/CreateSequenceResponse";
    public static final String SEQUENCE_ACKNOWLEDGMENT_ACTION = "http://schemas.xmlsoap.org/ws/2005/02/rm/SequenceAcknowledgement";
    public static final String TERMINATE_SEQUENCE_ACTION = "http://schemas.xmlsoap.org/ws/2005/02/rm/TerminateSequence";
    public static final String LAST_MESSAGE_ACTION = "http://schemas.xmlsoap.org/ws/2005/02/rm/LastMessage";

    // this constant used to skip the RM controll messages from invoking mercury control messages.
    public static final String PROCESS_RM_CONTROL_MESSAGE = "processRMControlMessage";

    // this is used to set the internal key of the returned sequence
    public static final String SESSION_ID = "SessionID";
    public static final String IS_ANNONYMOUS = "IsAnnonymous";

    // thread sleep time constants.
    public static final String RMS_SEQUENCE_TIMEOUT = "rmsSequenceTimeout";
    public static final String RMS_SEQUENCE_RETRANSMIT_TIME = "rmsSequenceRetransmitTime";
    public static final String RMS_SEQUENCE_WORKER_SLEEP_TIME= "rmsSequenceWorkerSleepTime";
    public static final String RMS_MAXIMUM_RETRANSMIT_COUNT= "rmsMaximumRetransmitCount";

    // exponential backoff
    public static final String RMS_EXPONENTIAL_BACKOFF = "rmsExponentialBackoff";

    
    public static final String RMD_SEQUENCE_TIMEOUT = "rmdSequenceTimeout";
    public static final String RMD_SEQUENCE_RETRANSMIT_TIME = "rmdSequenceRetransmitTime";
    public static final String RMD_SEQUENCE_WORKER_SLEEP_TIME = "rmdSequenceWorkerSleepTime";

    public static final String INVOKER_TIMEOUT = "invokerTimeout";
    public static final String INVOKER_WORKER_SLEEP_TIME = "invokerWorkerSleepTime";

    public static final String NOTIFY_THREADS = "notifyThreads";
    public static final String BUILD_MESSAGE_WITHOUT_WAITING = "buildMessageWithoutWaiting";

    public static final String SEQUENCE_REMOVAL_WORKER_SLEEP_TIME = "sequenceRemovalWorkerSleepTime";

    public static final String RM_PERSISTANCE_MANAGER = "rmPersistanceManager";
    public static final String RM_SECURITY_MANAGER = "rmSecurityManager";

    // Client side operations
    public static final String MERCURY_OPERATION_OUT_IN = "MercuryOperationOutIn";
    public static final String MERCURY_OPERATION_OUT_ONLY = "MercuryOperationOutOnly";

    public static final String ENFORCE_RM = "enforceRM";

    public static final String RM_ERROR = "RMError";
    public static final String RM_FAULT_ENVElOPE = "RMFaultEnvelope";
    public static final String RM_AXIS_FAULT = "RMAxisFault";


}
