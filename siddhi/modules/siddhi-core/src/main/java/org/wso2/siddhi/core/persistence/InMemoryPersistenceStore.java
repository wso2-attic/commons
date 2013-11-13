/*
*  Copyright (c) 2005-2012, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing,
* software distributed under the License is distributed on an
* "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
* KIND, either express or implied.  See the License for the
* specific language governing permissions and limitations
* under the License.
*/
package org.wso2.siddhi.core.persistence;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.apache.log4j.Logger;
import org.wso2.siddhi.core.event.management.PersistenceManagementEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InMemoryPersistenceStore implements PersistenceStore {

    private static final Logger log = Logger.getLogger(InMemoryPersistenceStore.class);
    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(new Config().setInstanceName(UUID.randomUUID().toString()));
    IMap<String, Map<String, Map<String, byte[]>>> persistenceMap = hazelcastInstance.getMap("persistenceMap");
    IMap<String, List<String>> revisionMap = hazelcastInstance.getMap("revisionMap");


    @Override
    public void save(PersistenceManagementEvent persistenceManagementEvent, String elementId,
                     PersistenceObject data) {
        Map<String, Map<String, byte[]>> executionPersistenceMap = persistenceMap.get(persistenceManagementEvent.getExecutionPlanIdentifier());
        if (executionPersistenceMap == null) {
            executionPersistenceMap = new HashMap<String, Map<String, byte[]>>();
        }
        Map<String, byte[]> executionRevisionMap = executionPersistenceMap.get(persistenceManagementEvent.getRevision());

        if (executionRevisionMap == null) {
            executionRevisionMap = new HashMap<String, byte[]>();
            executionPersistenceMap.put(persistenceManagementEvent.getRevision(), executionRevisionMap);
        }
        data.setElementId(elementId);
        executionRevisionMap.put(elementId, ByteSerializer.OToB(data));
        if (log.isDebugEnabled()) {
            log.debug(elementId + " serialized");
        }

        List<String> revisionList = revisionMap.get(persistenceManagementEvent.getExecutionPlanIdentifier());
        if (revisionList == null) {
            revisionList = new ArrayList<String>();
            revisionMap.put(persistenceManagementEvent.getExecutionPlanIdentifier(), revisionList);
        }
        if (revisionList.size() == 0 || (revisionList.size() > 0 && !persistenceManagementEvent.getRevision().equals(revisionList.get(revisionList.size() - 1)))) {
            revisionList.add(persistenceManagementEvent.getRevision());
            revisionMap.put(persistenceManagementEvent.getExecutionPlanIdentifier(), revisionList);
        }
        persistenceMap.put(persistenceManagementEvent.getExecutionPlanIdentifier(), executionPersistenceMap);


    }

    @Override
    public PersistenceObject load(PersistenceManagementEvent persistenceManagementEvent,
                                  String elementId) {


        Map<String, Map<String, byte[]>> executionPersistenceMap = persistenceMap.get(persistenceManagementEvent.getExecutionPlanIdentifier());
        if (executionPersistenceMap == null) {
            log.warn("Data not found for the execution plan " + persistenceManagementEvent.getExecutionPlanIdentifier());
            return null;
        }
        Map<String, byte[]> executionRevisionMap = executionPersistenceMap.get(persistenceManagementEvent.getRevision());

        if (executionRevisionMap == null) {
            log.warn("Data not found for the revision  " + persistenceManagementEvent.getRevision() + " of the execution plan " + persistenceManagementEvent.getExecutionPlanIdentifier());
            return null;
        }
        if (log.isDebugEnabled()) {
            log.debug("Deserializing " + elementId);
        }
        return (PersistenceObject) ByteSerializer.BToO(executionRevisionMap.get(elementId));
    }

    @Override
    public String getLastRevision(String executionPlanIdentifier) {
        List<String> revisionList = revisionMap.get(executionPlanIdentifier);
        if (revisionList == null) {
            return null;
        }
        if (revisionList.size() > 0) {
            return revisionList.get(revisionList.size() - 1);
        }
        return null;
    }

    public void shutdown() {
        hazelcastInstance.getLifecycleService().shutdown();
    }
}
