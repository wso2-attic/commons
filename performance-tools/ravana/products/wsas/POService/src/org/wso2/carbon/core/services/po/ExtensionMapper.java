
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6-wso2v1  Built on : Mar 29, 2010 (11:46:19 UTC)
 */

        
            package org.wso2.carbon.core.services.po;
        
            /**
            *  ExtensionMapper class
            */
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://po.services.core.carbon.wso2.org".equals(namespaceURI) &&
                  "OrderType".equals(typeName)){
                   
                            return  org.wso2.carbon.core.services.po.OrderType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://po.services.core.carbon.wso2.org".equals(namespaceURI) &&
                  "BuyStocksType".equals(typeName)){
                   
                            return  org.wso2.carbon.core.services.po.BuyStocksType.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    