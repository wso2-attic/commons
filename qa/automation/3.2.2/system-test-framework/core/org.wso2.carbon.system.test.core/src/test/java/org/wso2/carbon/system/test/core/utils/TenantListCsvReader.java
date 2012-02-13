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

package org.wso2.carbon.system.test.core.utils;

import au.com.bytecode.opencsv.CSVReader;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.ProductConstant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TenantListCsvReader {

    private static final Log log = LogFactory.getLog(TenantListCsvReader.class);

    /*
        create open csv reader - reads tenantList.csv
     */
    private static CSVReader createReader() {
        CSVReader reader = null;
        try {
            reader = new CSVReader(new FileReader(new File(ProductConstant.SYSTEM_TEST_RESOURCE_LOCATION + File.separator + "tenantList.csv")));
        }
        catch (FileNotFoundException e) {
            log.error("Tenant list not found" + e.getMessage());
        }
        return reader;
    }


    private static ArrayList<TenantDetails> csvTenantReader() {

        List tenantDetailsList = new ArrayList<TenantDetails>();
        try {
            tenantDetailsList = createReader().readAll();
        } catch (IOException e) {
            log.error("unable to read all tenant details: " + e.getMessage());
            Assert.fail("unable to read all tenant details: " + e.getMessage());
        }

        //array list to store tenant details
        ArrayList<TenantDetails> tenantList = new ArrayList<TenantDetails>();

        //Store tenantDetails instances to tenant list array
        for (int listCounter = 0; listCounter < tenantDetailsList.size(); listCounter++) {
            String[] tempTenantDetailsStr = (String[]) tenantDetailsList.get(listCounter);
            for (int elementCounter = 0; elementCounter < tempTenantDetailsStr.length; elementCounter++) {
                if (!tempTenantDetailsStr[elementCounter].equals(null) && !tempTenantDetailsStr[elementCounter + 1].equals(null) && !tempTenantDetailsStr[elementCounter + 2].equals(null)) {
                    tenantList.add(new TenantDetails(tempTenantDetailsStr[elementCounter], tempTenantDetailsStr[elementCounter + 1], tempTenantDetailsStr[elementCounter + 2], domainNameExtractor(tempTenantDetailsStr[elementCounter + 1])));
                }
                break;
            }
        }
        return tenantList;
    }

    public static TenantDetails getTenantDetails(int tenantId) {
        TenantDetails tenantDetails = csvTenantReader().get(tenantId);
        return tenantDetails;
    }

    public static int getTenantId(String key) {
        String inputInt;
        Pattern intsOnly = Pattern.compile("\\d+");
        Matcher makeMatch = intsOnly.matcher(key);
        makeMatch.find();
        inputInt = makeMatch.group();

        return (Integer.parseInt(inputInt));
    }

    private static String domainNameExtractor(String userName) {
        int index = userName.indexOf("@");
        return userName.substring(index + 1);
    }


}


