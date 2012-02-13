package org.wso2.carbon.app.test.handler;
/*  Copyright (c) WSO2 Inc. (http://wso2.com) All Rights Reserved.

  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*
*/
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.core.Comment;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.registry.core.jdbc.handlers.RequestContext;
import org.wso2.carbon.registry.core.jdbc.handlers.Handler;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.event.core.subscription.Subscription;


    public class JPGHandler extends Handler {
    private static final Log log = LogFactory.getLog(JPGHandler.class);

    public void put(RequestContext requestContext) throws RegistryException {
        String path = requestContext.getResourcePath().getPath();
        Resource resource = requestContext.getResource();

        //Adding properties to JPEG file uploaded
        resource.addProperty("JPEGProp","JPegVal");

        //Adding description to JPEG file uploaded
        resource.setDescription("This is a custom handler for JPEG images");

        //Put resource for association reference
        String associateResPath = "/documentation/jpegdocs/jpegDescription.txt";
        Resource associateResource = requestContext.getResource();
        associateResource.setContent(new String("This file explains how JPEG resources are stored in registry").getBytes());
        associateResource.setDescription("Description for association file of main JPEG file");
        associateResource.setMediaType("plain/text");
        requestContext.getRepository().put(associateResPath, associateResource);

        //Adding an association to JPEG file types
        requestContext.getRegistry().addAssociation(path,"/documentation/jpegdocs/jpegDescription.txt","usedBy");


        String comment1 = "These images should be of .JPEG extension";
        String comment2 = "A tag and an association should be added for the resource along with a property and a description";

        requestContext.getRepository().put(path, resource);

        //Adding a tag
        requestContext.getRegistry().applyTag(path, "picture");


        //Adding a comment
        Comment c1 = new Comment();
        c1.setResourcePath(path);
        c1.setText("This is the comment added when uploading the JPEG file");
        c1.setUser("admin1");

        requestContext.getRegistry().addComment(path, c1);
        requestContext.getRegistry().addComment(path, new Comment(comment1));
        requestContext.getRegistry().addComment(path, new Comment(comment2));

        //Rating the resource
        requestContext.getRegistry().rateResource(path,4);

        //Adding a life cycle
        requestContext.getRegistry().associateAspect(path,"ServiceLifeCycle");

        //Adding a subscription
        //ToDos

        

        log.info("JPG image is added to the path " + path);
//        log.info("Rating of the resource is " +requestContext.getRating());
//        log.info("Comments of resources are" + requestContext.getComment());
//        log.info("Properties of the resource are "+ requestContext.getProperty(path));
//        log.info("The description of the resource is" + requestContext.getTag());
//        log.info("Associations of the resource are " + requestContext.getRegistry().getAssociations(path, "usedBy"));
        requestContext.setProcessingComplete(true);


    }
}
