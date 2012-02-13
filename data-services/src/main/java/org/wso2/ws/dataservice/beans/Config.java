package org.wso2.ws.dataservice.beans;

import java.util.ArrayList;
import java.util.Iterator;

public class Config {
	ArrayList properties = new ArrayList();

	public ArrayList getProperties() {
		return properties;
	}

	public void setProperties(ArrayList properties) {
		this.properties = properties;
	}

	public void addProperty(String name,String value){
		Property property = new Property(name,value);
		properties.add(property);
	}
	
	public String getPropertyValue(String propertyName){
		Iterator propertyItr = properties.iterator();
		for (; propertyItr.hasNext();) {
			Property property = (Property) propertyItr.next();
			if(property.getName().equals(propertyName)){
				return property.getValue();
			}
		}
		return null;
	}

}
