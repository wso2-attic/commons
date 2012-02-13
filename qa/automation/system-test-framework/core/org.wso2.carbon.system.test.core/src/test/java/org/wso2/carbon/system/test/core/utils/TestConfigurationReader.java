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

import org.apache.axiom.om.OMAttribute;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.admin.service.utils.ProductConstant;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;


import javax.xml.namespace.QName;
import java.io.FileNotFoundException;
import java.util.*;

public class TestConfigurationReader {

    private static Log log = LogFactory.getLog(TestConfigurationReader.class);


    private static TestConfigurationReader instance = new TestConfigurationReader();

    public class TestConfig {

        private String testClassName;

        private String description;

        private Map<Integer, String> productNames = new Hashtable<Integer, String>();

        private Map<String, String> artifacts = new Hashtable<String, String>();

        private String artifactType;

        MultiValueMapUtil<String, String> jarArtifactMap =
                new MultiValueMapUtil<String, String>(new ArrayList<String>());

        MultiValueMapUtil<String, String> carArtifactMap =
                new MultiValueMapUtil<String, String>(new ArrayList<String>());

        MultiValueMapUtil<String, String> marArtifactMap =
                new MultiValueMapUtil<String, String>(new ArrayList<String>());

        MultiValueMapUtil<String, String> garArtifactMap =
                new MultiValueMapUtil<String, String>(new ArrayList<String>());

        MultiValueMapUtil<String, String> xmlArtifactMap =
                new MultiValueMapUtil<String, String>(new ArrayList<String>());

        MultiValueMapUtil<String, String> aarArtifactMap =
                new MultiValueMapUtil<String, String>(new ArrayList<String>());

        MultiValueMapUtil<String, String> warArtifactMap =
                new MultiValueMapUtil<String, String>(new ArrayList<String>());

        private TestConfig(String testClassName, String description, Map<Integer,
                String> products, Map<String, String> artifacts, String artifactType, MultiValueMapUtil<String, String>
                jarArtifactMap, MultiValueMapUtil<String, String> carArtifactMap,
                           MultiValueMapUtil<String, String> marArtifactMap,
                           MultiValueMapUtil<String, String> garArtifactMap, MultiValueMapUtil<String, String> xmlArtifactMap,
                           MultiValueMapUtil<String, String> aarArtifactMap, MultiValueMapUtil<String, String> warArtifactMap) {
            this.testClassName = testClassName;
            this.description = description;
            this.productNames = products;
            this.artifacts = artifacts;
            this.artifactType = artifactType;
            this.jarArtifactMap = jarArtifactMap;
            this.carArtifactMap = carArtifactMap;
            this.marArtifactMap = marArtifactMap;
            this.garArtifactMap = garArtifactMap;
            this.xmlArtifactMap = xmlArtifactMap;
            this.aarArtifactMap = aarArtifactMap;
            this.warArtifactMap = warArtifactMap;

        }

        public String getTestClassName() {
            return testClassName;
        }

        public String getDescription() {
            return description;
        }

        public String getArtifactType() {
            return artifactType;
        }

        public Map<Integer, String> getProductName() {
            return productNames;
        }

        public Map<String, String> getArtifactList() {
            return artifacts;
        }

        public MultiValueMapUtil<String, String> getAarArtifact() {
            return aarArtifactMap;
        }

        public MultiValueMapUtil<String, String> getMarArtifact() {
            return marArtifactMap;
        }

        public MultiValueMapUtil<String, String> getJarArtifact() {
            return jarArtifactMap;
        }

        public MultiValueMapUtil<String, String> getCarArtifact() {
            return carArtifactMap;
        }

        public MultiValueMapUtil<String, String> getXmlArtifact() {
            return xmlArtifactMap;
        }

        public MultiValueMapUtil<String, String> getGarArtifact() {
            return garArtifactMap;
        }

        public MultiValueMapUtil<String, String> getWarArtifact() {
            return warArtifactMap;
        }
    }

    private Map<String, TestConfig> testScenarioConfigMap =
            new Hashtable<String, TestConfig>();

    /**
     * Returns the AuthenticatorsConfiguration singleton instance
     *
     * @return AuthenticatorsConfiguration singleton instance
     */
    public static TestConfigurationReader getInstance() {
        return instance;
    }

    private TestConfigurationReader() {
        initialize();
    }

    private void initialize() {


        if (ProductConstant.SYSTEM_TEST_RESOURCE_LOCATION == null) {
            log.error("System property: system.test.sample.location cannot be null");
        }

        String testConfigFilePath = ArtifactReader.SYSTEM_TEST_RESOURCE_LOCATION + File.separator + "conf" + File.separator + "testconfig.xml";
//        String testConfigFilePath = "/home/krishantha/svn/system-test-framework/core/org.wso2.carbon.system.test.core/src/test/resources/conf/testconfig.xml";

        //if file location =null it taking from the test data directory
        OMElement documentElement;
        FileInputStream inputStream = null;
        File file = new File(testConfigFilePath);
        if (file.exists()) {
            try {
                inputStream = new FileInputStream(testConfigFilePath);
                XMLStreamReader parser = XMLInputFactory.newInstance().createXMLStreamReader(inputStream);
                //create the builder
                StAXOMBuilder builder = new StAXOMBuilder(parser);
                //get the root element (in this case the envelope)
                documentElement = builder.getDocumentElement();

                for (Iterator itrScenario = documentElement.getChildrenWithName(new QName("scenario")); itrScenario.hasNext();) {
                    OMElement omDocument = (OMElement) itrScenario.next();
                    TestConfig testConfig = processTestConfigurationElements(omDocument);
                    if (testConfig != null) {
                        this.testScenarioConfigMap.put(testConfig.getTestClassName(), testConfig);
                    }
                }
            } catch (FileNotFoundException e) {
                log.error("testconfig.xml file is not available");
            } catch (XMLStreamException e) {
                log.error("Error reading the testconfig.xml file");
            }
            finally {
                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    log.warn("Unable to close the file input stream created for testconfig.xml");
                }
            }
        }
    }

    private TestConfig processTestConfigurationElements(OMElement OmClassDocument) {
        String className; //name of test case class
        String description = null; //Description of test scenario
        String artifactType = null; //Artifact types - eg. aar, mar, jar etc..
        Map<Integer, String> productNamesMap = new Hashtable<Integer, String>(); //Map to store product names
        Map<String, String> artifactParameterMap = new Hashtable<String, String>(); //Map to store car artifact names

        MultiValueMapUtil<String, String> jarArtifactMap =
                new MultiValueMapUtil<String, String>(new ArrayList<String>()); //Map to store jar artifacts names and dependencies

        MultiValueMapUtil<String, String> carArtifactMap =
                new MultiValueMapUtil<String, String>(new ArrayList<String>());

        MultiValueMapUtil<String, String> marArtifactMap =
                new MultiValueMapUtil<String, String>(new ArrayList<String>());

        MultiValueMapUtil<String, String> garArtifactMap =
                new MultiValueMapUtil<String, String>(new ArrayList<String>());

        MultiValueMapUtil<String, String> xmlArtifactMap =
                new MultiValueMapUtil<String, String>(new ArrayList<String>());

        MultiValueMapUtil<String, String> aarArtifactMap =
                new MultiValueMapUtil<String, String>(new ArrayList<String>());

        MultiValueMapUtil<String, String> warArtifactMap =
                new MultiValueMapUtil<String, String>(new ArrayList<String>());

        OMAttribute nameAttr = OmClassDocument.getAttribute(new QName("className"));
        if (nameAttr == null) {
            log.warn("Each Test scenario Configuration should have a unique class name attribute. +" +
                    "This scenario will not be registered for execution.");
            return null;
        }
        className = nameAttr.getAttributeValue();

        for (Iterator itrDescription = OmClassDocument.getChildrenWithName(new QName("description")); itrDescription.hasNext();) {
            OMElement omDescription = (OMElement) itrDescription.next();
            description = omDescription.toString();

            for (Iterator artifactsItr = OmClassDocument.getChildrenWithName(new QName("artifacts")); artifactsItr.hasNext();) {
                OMElement omArtifacts = (OMElement) artifactsItr.next();

                for (Iterator productsItr = omArtifacts.getChildrenWithName(new QName("products")); productsItr.hasNext();) {
                    OMElement omProducts = (OMElement) productsItr.next();

                    int productCounter = 1;
                    for (Iterator productItr = omProducts.getChildrenWithName(new QName("product")); productItr.hasNext();) {
                        OMElement omProduct = (OMElement) productItr.next();
                        OMAttribute productNameAttr = omProduct.getAttribute(new QName("productName"));
                        if (productNameAttr == null) {
                            log.warn("Each Test scenario Configuration should have a product name attribute. +" +
                                    "This scenario will not be registered for execution.");
                            return null;
                        }
                        productNamesMap.put(productCounter, productNameAttr.getAttributeValue());
                        productCounter++;


                        for (Iterator artifactItr = omProduct.getChildrenWithName(new QName("artifact")); artifactItr.hasNext();) {
                            OMElement omArtifact = (OMElement) artifactItr.next();
                            OMAttribute userIdAttr = omArtifact.getAttribute(new QName("userId"));

                            for (Iterator artifactNameItr = omArtifact.getChildrenWithName(new QName("aar")); artifactNameItr.hasNext();) {
                                OMElement omAarArtifacts = (OMElement) artifactNameItr.next();
                                for (Iterator aarArtifactNameItr = omAarArtifacts.getChildrenWithName(new QName("name")); aarArtifactNameItr.hasNext();) {
                                    OMElement omAarArtifactName = (OMElement) aarArtifactNameItr.next();
                                    aarArtifactMap.addValue(productNameAttr.getAttributeValue() + "-" + userIdAttr.getAttributeValue(), omAarArtifactName.getText());
                                }
                            }

                            for (Iterator artifactNameItr = omArtifact.getChildrenWithName(new QName("mar")); artifactNameItr.hasNext();) {
                                OMElement omAarArtifactsMar = (OMElement) artifactNameItr.next();
                                for (Iterator marArtifactNameItr = omAarArtifactsMar.getChildrenWithName(new QName("name")); marArtifactNameItr.hasNext();) {
                                    OMElement omMarArtifactName = (OMElement) marArtifactNameItr.next();
                                    marArtifactMap.addValue(productNameAttr.getAttributeValue() + "-" + userIdAttr.getAttributeValue(), omMarArtifactName.getText());
                                }

                            }

                            for (Iterator artifactNameItr = omArtifact.getChildrenWithName(new QName("car")); artifactNameItr.hasNext();) {
                                OMElement omAarArtifactsCar = (OMElement) artifactNameItr.next();
                                for (Iterator carArtifactNameItr = omAarArtifactsCar.getChildrenWithName(new QName("name")); carArtifactNameItr.hasNext();) {
                                    OMElement omCarArtifactName = (OMElement) carArtifactNameItr.next();
                                    carArtifactMap.addValue(productNameAttr.getAttributeValue() + "-" + userIdAttr.getAttributeValue(), omCarArtifactName.getText());
                                }
                            }

                            for (Iterator artifactNameItr = omArtifact.getChildrenWithName(new QName("war")); artifactNameItr.hasNext();) {
                                OMElement omWarArtifacts = (OMElement) artifactNameItr.next();
                                for (Iterator warArtifactNameItr = omWarArtifacts.getChildrenWithName(new QName("name")); warArtifactNameItr.hasNext();) {
                                    OMElement omWarArtifactName = (OMElement) warArtifactNameItr.next();
                                    warArtifactMap.addValue(productNameAttr.getAttributeValue() + "-" + userIdAttr.getAttributeValue(), omWarArtifactName.getText());
                                }
                            }

                            for (Iterator artifactNameItr = omArtifact.getChildrenWithName(new QName("jar")); artifactNameItr.hasNext();) {
                                OMElement omAarArtifactsJar = (OMElement) artifactNameItr.next();


                                for (Iterator jarArtifactItr = omAarArtifactsJar.getChildrenWithName(new QName("jarArtifact")); jarArtifactItr.hasNext();) {
                                    OMElement omJarArtifact = (OMElement) jarArtifactItr.next();
                                    OMAttribute serviceTypeAttr = omJarArtifact.getAttribute(new QName("serviceType"));

                                    for (Iterator jarNameArtifactItr = omJarArtifact.getChildrenWithName(new QName("name")); jarNameArtifactItr.hasNext();) {
                                        OMElement omjarNameArtifact = (OMElement) jarNameArtifactItr.next();
                                        jarArtifactMap.addValue(productNameAttr.getAttributeValue() + "-" + userIdAttr.getAttributeValue() + "-" + serviceTypeAttr.getAttributeValue(), omjarNameArtifact.getText());


                                        for (Iterator jarDependencyArtifactItr = omJarArtifact.getChildrenWithName(new QName("dependency")); jarDependencyArtifactItr.hasNext();) {
                                            OMElement omjarDependencyArtifact = (OMElement) jarDependencyArtifactItr.next();
                                            jarArtifactMap.addValue(productNameAttr.getAttributeValue() + "-" + userIdAttr.getAttributeValue() + "-" + serviceTypeAttr.getAttributeValue(), omjarDependencyArtifact.getText());

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return new TestConfig(className, description, productNamesMap, artifactParameterMap, artifactType, jarArtifactMap,
                carArtifactMap, marArtifactMap, garArtifactMap, xmlArtifactMap, aarArtifactMap, warArtifactMap);
    }

    public TestConfig getTestConfig(String testClassName) {
        return testScenarioConfigMap.get(testClassName);
    }
}
