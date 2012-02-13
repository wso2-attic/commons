/* Sample service to test MTOM file download functionality */
package org.wso2.wsas.service;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;


public class MtomDownloadService {
	
	
	public DataHandler getFile(String fileName) {

		 FileDataSource dataSource = new FileDataSource(fileName);

		 DataHandler fileDataHandler = new DataHandler(dataSource);

		 return fileDataHandler;

		}
}