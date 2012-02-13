/*
 * Copyright (c) 2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.startos.system.test.hectorClient;

import junit.framework.Assert;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Simple sample using hector API to connect to Cassandra
 */
public class HectorExample {

    private static Cluster cluster;

    public static void main(String arg[]) {
        //Read User Inputs
        Scanner scanner = new Scanner(System.in);
        System.out.print("Tenant Id: ");
        String tenantId = "admin123@krishantha.hhh";
        System.out.print("Tenant Password: ");
        String tenantPasswd = "admin123";
        System.out.print("Keyspace Name: ");
        String keyspaceName = "TestKeySpace";
        System.out.print("Column Family: ");
        String ColumnFamilyName = "TestColumnFamily";
        System.out.print("Column Name List ( separated by colon) : ");
        String columnNameList = "AAA:BBB:CCC:DDD";
        System.out.print("Number of Row you need: ");
        String rowCount = "1";

        cluster = ExampleHelper.createCluster(tenantId, tenantPasswd);
        createKeyspace(keyspaceName, ColumnFamilyName, columnNameList, rowCount);
    }

    public static Boolean executeKeySpaceSample() {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Tenant Id: ");
        String tenantId = "admin123@krishantha.hhh";
        System.out.print("Tenant Password: ");
        String tenantPasswd = "admin123";
        System.out.print("Keyspace Name: ");
        String keyspaceName = "TestKeySpace";
        System.out.print("Column Family: ");
        String ColumnFamilyName = "TestColumnFamily";
        System.out.print("Column Name List ( separated by colon) : ");
        String columnNameList = "AAA:BBB:CCC:DDD";
        System.out.print("Number of Row you need: ");
        String rowCount = "1";

        cluster = ExampleHelper.createCluster(tenantId, tenantPasswd);
        return (createKeyspace(keyspaceName, ColumnFamilyName, columnNameList, rowCount));

    }


    /**
     * Create a keyspace, add a column family and read a column's value
     *
     * @param keyspaceName
     * @param columnFamily
     * @param columnList
     * @param rowCount
     */
    private static Boolean createKeyspace(String keyspaceName, String columnFamily, String columnList,
                                          String rowCount) {
        Boolean cassendraSampleStatus = false;
        boolean status = false;
        String addKeySpace = null;
        //Create Keyspace
        KeyspaceDefinition definition = new ThriftKsDef(keyspaceName);
        cluster.dropKeyspace(keyspaceName);

        addKeySpace = cluster.addKeyspace(definition);


        //add columnt family
        ColumnFamilyDefinition familyDefinition = new ThriftCfDef(keyspaceName, columnFamily);
        cluster.addColumnFamily(familyDefinition);
        //Add data to a column
        Keyspace keyspace = null;
        try {
            keyspace = HFactory.createKeyspace(keyspaceName, cluster);


        } catch (Exception e) {
            Assert.fail("Error in adding Keyspace" + e.getMessage());
            System.out.println("Error in adding Keyspace" + e);
        }
        Mutator<String> mutator = HFactory.createMutator(keyspace, new StringSerializer());
        String rowKey = null;
        List<String> keyList = new ArrayList<String>();
        for (int i = 0; i < Integer.parseInt(rowCount); i++) {
            rowKey = UUID.randomUUID().toString();
            keyList.add(rowKey);
            System.out.println("\nInserting Key " + rowKey + "To Column Family " + columnFamily + "\n");
            for (String columnName : columnList.split(":")) {
                String columnValue = "testvalue";
                mutator.insert(rowKey, columnFamily, HFactory.createStringColumn(columnName, columnValue));
                System.out.println("Column Name: " + columnName + " Value: " + columnValue + "\n");
            }
        }
        //Read Data
        ColumnQuery<String, String, String> columnQuery =
                HFactory.createStringColumnQuery(keyspace);
        for (String key : keyList) {
            System.out.println("\nretrieving Key " + rowKey + "From Column Family " + columnFamily + "\n");
            for (String columnName : columnList.split(":")) {
                columnQuery.setColumnFamily(columnFamily).setKey(key).setName(columnName);
                QueryResult<HColumn<String, String>> result = columnQuery.execute();
                HColumn<String, String> hColumn = result.get();
                //sout data
                System.out.println("Column: " + hColumn.getName() + " Value : " + hColumn.getValue() + "\n");
                if (hColumn.getValue().contains("testvalue")) {
                    System.out.println("Pass");
                    cassendraSampleStatus = true;
                    break;
                }
            }
        }
        return cassendraSampleStatus;
    }
}
