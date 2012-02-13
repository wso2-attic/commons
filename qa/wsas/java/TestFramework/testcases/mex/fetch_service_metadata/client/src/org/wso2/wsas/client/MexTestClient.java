package org.wso2.wsas.client;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNode;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.dataretrieval.DRConstants;
import org.apache.axis2.dataretrieval.client.MexClient;
import org.apache.axis2.mex.MexConstants;
import org.apache.axis2.mex.om.Location;
import org.apache.axis2.mex.om.Metadata;
import org.apache.axis2.mex.om.MetadataReference;
import org.apache.axis2.mex.om.MetadataSection;


public class MexTestClient {
	
	public static void main(String[] args) throws AxisFault{
	 String SERVICE_ENDPOINT = args[0];
		try {
			System.out.println("*************Schema of service**********************");
			getServiceSchema(SERVICE_ENDPOINT);
			System.out.println("*************WSDL1.1 of service**********************");
			getServiceWSDL(SERVICE_ENDPOINT);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	public static MexClient getMexClient(String epr)throws AxisFault{
		MexClient mc = new MexClient();
		Options options = mc.getOptions();
        options.setTo(new EndpointReference(epr));
        options.setAction(DRConstants.SPEC.Actions.GET_METADATA_REQUEST);
        options.setExceptionToBeThrownOnSOAPFault(true);
        return mc;
	}
     public static void getServiceSchema(String epr) throws Exception{
        MexClient mc = getMexClient(epr);
        OMElement request = mc.setupGetMetadataRequest(MexConstants.SPEC.DIALECT_TYPE_SCHEMA, null);
        OMElement result = mc.sendReceive(request);
        Metadata metadata = new Metadata();
        metadata.fromOM(result);
        printResults(result);

	}
     
     public static void getServiceWSDL(String epr) throws Exception{
         
     	 MexClient mc = getMexClient(epr);
         OMElement request = mc.setupGetMetadataRequest(MexConstants.SPEC.DIALECT_TYPE_WSDL, null);
         OMElement result = mc.sendReceive(request);
         Metadata metadata = new Metadata();
         metadata.fromOM(result);
         printResults(result);

 	}
        
    public static void printResults(OMElement result)throws org.apache.axis2.mex.MexException{
        
    	Metadata metadata = new Metadata();
        metadata.fromOM(result);
        MetadataSection[] metadatSections = metadata.getMetadatSections();
        
        if (metadatSections == null || metadatSections.length == 0) {
            System.out.println("MetadataSection is not available");
            
        } else  {
            MetadataSection metadataSection;
            for (int i = 0; i < metadatSections.length; i++) {
                metadataSection = metadatSections[i];
                System.out.println("Metadata Section:");
                System.out.println("****************");
                
                String dialect = metadataSection.getDialect();
                if (dialect != null) {
                    System.out.println("Dialect : " + dialect);
                }
                               
                OMNode inlineData = metadataSection.getInlineData();
                if (inlineData != null) {
                    System.out.println("InlineData : \n" + inlineData.toString());
                    continue;
                }
                
                Location location = metadataSection.getLocation();
                if (location != null) {
                    System.out.println("Location : \n" + location.getURI());
                     continue;
                }
                
                MetadataReference metadataReference = metadataSection.getMetadataReference();
                if (metadataReference != null) {
                    System.out.println("MetadataSection : \n" + metadataReference.getEPRElement());
                   
                }     

            }
        }
	}


}
