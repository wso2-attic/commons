package org.wso2.ws.dataservice.beans;

import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;

public class Result {    
    private String resultWrapper = ""; //represents element attribute of result element
    private String rowName = ""; //represents rowName attribute of result element
    private String resultSetColumnNames[];
    private String displayColumnNames[];
    private String elementLocalNames[];
    
    public String getResultWrapper() {
        return resultWrapper;
    }
    public void setResultWrapper(String resultWrapper) {
        this.resultWrapper = resultWrapper;
    }
    public String getRowName() {
        return rowName;
    }
    public void setRowName(String rowName) {
        this.rowName = rowName;
    }
    public String[] getResultSetColumnNames() {
        return resultSetColumnNames;
    }
    public void setResultSetColumnNames(String[] resultSetColumnNames) {
        this.resultSetColumnNames = resultSetColumnNames;
    }
    public String[] getDisplayColumnNames() {
        return displayColumnNames;
    }
    public void setDisplayColumnNames(String[] displayColumnNames) {
        this.displayColumnNames = displayColumnNames;
    }
    
    
    public String[] getElementLocalNames() {
        return elementLocalNames;
    }
    public void setElementLocalNames(String[] elementLocalNames) {
        this.elementLocalNames = elementLocalNames;
    }
    public Result(OMElement result){
        String wrapperElementName = result.getAttributeValue(new QName("element"));
        String rowElementName = result.getAttributeValue(new QName("rowName"));
        
        //if wrapper element || row element is not set, set default values to them
        if(wrapperElementName == null || wrapperElementName.trim().length() == 0){
            //default value
            wrapperElementName = "results";             
        }
        if(rowElementName == null || rowElementName.trim().length() == 0){
            //default value
            rowElementName = "row";             
        }
        this.resultWrapper = wrapperElementName;
        this.rowName = rowElementName;
        
        Iterator elements = result.getChildElements();
        ArrayList displayColumns = new ArrayList();
        ArrayList resultSetColumns = new ArrayList();
        ArrayList elementLclNames = new ArrayList();
        
        while (elements.hasNext()) {
            OMElement element = (OMElement) elements.next();
            String displayTagName = element
                    .getAttributeValue(new QName("name"));
            String resultSetFieldName = element.getAttributeValue(new QName(
                    "column"));
            displayColumns.add(displayTagName);
            resultSetColumns.add(resultSetFieldName);
            elementLclNames.add(element.getLocalName());
        }

        Object[] objDisplayColumns = displayColumns.toArray();
        this.displayColumnNames = new String[objDisplayColumns.length];
        for (int a = 0; a < objDisplayColumns.length; a++) {
            this.displayColumnNames[a] = (String)objDisplayColumns[a];
        }
        
        Object[] objResultSetColumns = resultSetColumns.toArray();
        this.resultSetColumnNames = new String[objResultSetColumns.length];
        for (int a = 0; a < objResultSetColumns.length; a++) {
            this.resultSetColumnNames[a] = (String)objResultSetColumns[a];
        }
        
        Object[] objElementLocalNames = elementLclNames.toArray();
        this.elementLocalNames = new String[objElementLocalNames.length];
        for (int a = 0; a < objElementLocalNames.length; a++) {
            this.elementLocalNames[a] = (String)objElementLocalNames[a];
        }
        
        
    }

}
