package org.wso2.sandesha2.storage.persistent.util;

import java.util.ArrayList;

import javax.xml.namespace.QName;

import org.apache.sandesha2.storage.SandeshaStorageException;

public class PersistentUtil {

	private static final String QNAME_SEPERATOR = "::";
	private static final String NULL_VALUE = "NullValue";
	private static final String EMPTY_VALUE = "EmptyValue";
	
	/**
	 * Gives an String representation of a QName for persistence perposes.
	 * NULLs will be represented with the constant 'NullValue'
	 * 
	 * @param qName
	 * @return
	 */
	public static String getStringFromQName (QName qName) {
		String localPart = qName.getLocalPart();
		String namespaceURI = qName.getNamespaceURI();
		String prefix = qName.getPrefix();
		
		if (localPart==null)
			localPart = NULL_VALUE;
		else if ("".equals(localPart))
			localPart = EMPTY_VALUE;
		
		if (namespaceURI==null)
			namespaceURI = NULL_VALUE;
		else if ("".equals(namespaceURI))
			namespaceURI = EMPTY_VALUE;
		
		if (prefix==null)
			prefix = NULL_VALUE;
		else if ("".equals(prefix))
			prefix = EMPTY_VALUE;

		String stringQname = localPart + QNAME_SEPERATOR + namespaceURI + QNAME_SEPERATOR + prefix;
		return stringQname;
	}
	
	/**
	 * Gives back the QName of the passed String object. 
	 * 
	 * @param stringQName A String representation of a Qname. See 'getStringFromQName'
	 * @return
	 * @throws SandeshaStorageException
	 */
	public static QName getQnameFromString (String stringQName) throws SandeshaStorageException {
		String[] parts = stringQName.split(QNAME_SEPERATOR);
		if (parts==null || parts.length!=3)
			throw new SandeshaStorageException ("Invalid QName representation");
		
		String localPart = parts[0];
		String namespaceURI = parts[1];
		String prefix = parts[2];
		
		if (NULL_VALUE.equals(localPart))
			localPart = null;
		if (NULL_VALUE.equals(namespaceURI))
			namespaceURI = null;
		if (NULL_VALUE.equals(prefix))
			prefix = null;
		
		if (EMPTY_VALUE.equals(localPart))
			localPart = "";
		if (EMPTY_VALUE.equals(namespaceURI))
			namespaceURI = "";
		if (EMPTY_VALUE.equals(prefix))
			prefix = "";
		
		QName qName = new QName (namespaceURI,localPart,prefix);
		return qName;
	}
}
