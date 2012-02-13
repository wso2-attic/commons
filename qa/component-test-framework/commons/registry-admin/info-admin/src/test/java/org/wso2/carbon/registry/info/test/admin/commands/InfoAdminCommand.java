/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.carbon.registry.info.test.admin.commands;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.registry.info.ui.InfoAdminServiceStub;
import org.wso2.carbon.registry.info.ui.beans.xsd.CommentBean;
import org.wso2.carbon.registry.info.ui.beans.xsd.EventTypeBean;
import org.wso2.carbon.registry.info.ui.beans.xsd.RatingBean;
import org.wso2.carbon.registry.info.ui.beans.xsd.SubscriptionBean;
import org.wso2.carbon.registry.info.ui.beans.xsd.TagBean;

public class InfoAdminCommand extends TestCase {
    private static final Log log = LogFactory.getLog(InfoAdminCommand.class);
    InfoAdminServiceStub infoAdminServiceStub;

    public InfoAdminCommand(
            InfoAdminServiceStub infoAdminServiceStub) {
        this.infoAdminServiceStub = infoAdminServiceStub;
        log.debug("infoAdminServiceStub added");
    }

    public void addCommentSuccessCase(String comment, String path, String sessionId)
            throws Exception {
        infoAdminServiceStub.addComment(comment, path, sessionId);
    }

    public void addCommentFailureCase(String comment, String path, String sessionId) {
        try {
            infoAdminServiceStub.addComment(comment, path, sessionId);
            log.error("Comment added without session cookie");
            Assert.fail("Comment added without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public void addTagSuccessCase(String tag, String path, String sessionId) throws Exception {
        infoAdminServiceStub.addTag(tag, path, sessionId);
    }

    public void addTagFailureCase(String tag, String path, String sessionId) {
        try {
            infoAdminServiceStub.addTag(tag, path, sessionId);
            log.error("Tag added without session cookie");
            Assert.fail("Tag added without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public CommentBean getCommentsSuccessCase(String path, String sessionId) throws Exception {
        CommentBean commentsBean = infoAdminServiceStub.getComments(path, sessionId);
        return commentsBean;
    }

    public void getCommentsFailureCase(String path, String sessionId) {
        try {
            infoAdminServiceStub.getComments(path, sessionId);
            log.error("Getting Comment Bean without session cookie");
            Assert.fail("Getting Comment Bean without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public EventTypeBean getEventTypesSuccessCase(String path, String sessionId) throws Exception {
        EventTypeBean eventTypeBean = infoAdminServiceStub.getEventTypes(path, sessionId);
        return eventTypeBean;
    }

    public void getEventTypesFailureCase(String path, String sessionId) {
        try {
            infoAdminServiceStub.getEventTypes(path, sessionId);
            log.error("Getting EventTypes Bean without session cookie");
            Assert.fail("Getting EventTypes Bean without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public RatingBean getRatingsSuccessCase(String path, String sessionId) throws Exception {
        RatingBean ratingBean = infoAdminServiceStub.getRatings(path, sessionId);
        return ratingBean;
    }

    public void getRatingsFailureCase(String path, String sessionId) {
        try {
            infoAdminServiceStub.getRatings(path, sessionId);
            log.error("Getting Rating Bean without session cookie");
            Assert.fail("Getting Rating Bean without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public String getRemoteURLSuccessCase(String path, String sessionId) throws Exception {
        String remoteURL = infoAdminServiceStub.getRemoteURL(path, sessionId);
        return remoteURL;
    }

    public void getRemoteURLFailureCase(String path, String sessionId) {
        try {
            infoAdminServiceStub.getRemoteURL(path, sessionId);
            log.error("Getting remote URL without session cookie");
            Assert.fail("Getting remote URL without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public SubscriptionBean getSubscriptionsSuccessCase(String path, String sessionId)
            throws Exception {
        SubscriptionBean subscriptionBean = infoAdminServiceStub.getSubscriptions(path, sessionId);
        return subscriptionBean;
    }

    public void getSubscriptionsFailureCase(String path, String sessionId) {
        try {
            infoAdminServiceStub.getSubscriptions(path, sessionId);
            log.error("Getting Subscription Bean without session cookie");
            Assert.fail("Getting Subscription Bean without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public TagBean getTagsSuccessCase(String path, String sessionId)
            throws Exception {
        TagBean tagBean = infoAdminServiceStub.getTags(path, sessionId);
        return tagBean;
    }

    public void getTagsFailureCase(String path, String sessionId) {
        try {
            infoAdminServiceStub.getTags(path, sessionId);
            log.error("Getting Tag Bean without session cookie");
            Assert.fail("Getting Tag Bean without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public SubscriptionBean subscribeSuccessCase(String path, String endpoint, String eventName,
                                                 String sessionId)
            throws Exception {
        SubscriptionBean subscriptionBean = infoAdminServiceStub.subscribe(path, endpoint, eventName, sessionId);
        return subscriptionBean;
    }

    public void subscribeFailureCase(String path, String endpoint, String eventName,
                                     String sessionId) {
        try {
            infoAdminServiceStub.subscribe(path, endpoint, eventName, sessionId);
            log.error("subscribe failed");
            Assert.fail("subscribe failed");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public void removeCommentSuccessCase(String commentPath, String sessionId)
            throws Exception {
        infoAdminServiceStub.removeComment(commentPath, sessionId);
    }

    public void removeCommentFailureCase(String path, String sessionId) {
        try {
            infoAdminServiceStub.removeComment(path, sessionId);
            log.error("Comment Removed without session cookie");
            Assert.fail("Comment Removed without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public void removeTagSuccessCase(String tag, String path, String sessionId)
            throws Exception {
        infoAdminServiceStub.removeTag(tag, path, sessionId);
    }

    public void removeTagFailureCase(String tag, String path, String sessionId) {
        try {
            infoAdminServiceStub.removeTag(tag, path, sessionId);
            log.error("Tag Removed without session cookie");
            Assert.fail("Tag Removed without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public boolean isProfileExistingSuccessCase(String userName, String sessionId)
            throws Exception {
        boolean isProfileExisting = infoAdminServiceStub.isProfileExisting(userName, sessionId);
        return isProfileExisting;
    }

    public void isProfileExistingFailureCase(String userName, String sessionId) {
        try {
            infoAdminServiceStub.isProfileExisting(userName, sessionId);
            log.error("Getting profile exist infomations without session cookie");
            Assert.fail("Getting profile exist infomations without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public boolean isResourceSuccessCase(String path, String sessionId)
            throws Exception {
        boolean isResource = infoAdminServiceStub.isResource(path, sessionId);
        return isResource;
    }

    public void isResourceFailureCase(String path, String sessionId) {
        try {
            infoAdminServiceStub.isResource(path, sessionId);
            log.error("Checking resource without session cookie");
            Assert.fail("Checking resource without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public boolean isUserValidSuccessCase(String userName, String sessionId)
            throws Exception {
        boolean isUserValid = infoAdminServiceStub.isUserValid(userName, sessionId);
        return isUserValid;
    }

    public void isUserValidFailureCase(String userName, String sessionId) {
        try {
            infoAdminServiceStub.isUserValid(userName, sessionId);
            log.error("Checking user validity without session cookie");
            Assert.fail("Checking user validity without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public void rateResourceSuccessCase(String rating, String path, String sessionId)
            throws Exception {
        infoAdminServiceStub.rateResource(rating, path, sessionId);
    }

    public void rateResourceFailureCase(String rating, String path, String sessionId) {
        try {
            infoAdminServiceStub.rateResource(rating, path, sessionId);
            log.error("Resource Rated without session cookie");
            Assert.fail("Resource Rated without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public void unSubscribeSuccessCase(String path, String id, String sessionId)
            throws Exception {
        infoAdminServiceStub.unsubscribe(path, id, sessionId);
    }

    public void unSubscribeFailureCase(String path, String id, String sessionId) {
        try {
            infoAdminServiceStub.unsubscribe(path, id, sessionId);
            log.error("Unsubscribed without session cookie");
            Assert.fail("Unsubscribed without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }

    public String verifyEmailSuccessCase(String data, String sessionId)
            throws Exception {
        String verifyInfo = infoAdminServiceStub.verifyEmail(data, sessionId);
        return verifyInfo;
    }

    public void verifyEmailFailureCase(String data, String sessionId) {
        try {
            infoAdminServiceStub.verifyEmail(data, sessionId);
            log.error("Email verified without session cookie");
            Assert.fail("Email verified without session cookie");
        }
        catch (Exception e) {
            if (!e.toString().contains("AxisEngine Access Denied. Please login first")) {
                Assert.fail("Expected exception not found");
                log.error("Expected exception not found");
            }
        }
    }
}
