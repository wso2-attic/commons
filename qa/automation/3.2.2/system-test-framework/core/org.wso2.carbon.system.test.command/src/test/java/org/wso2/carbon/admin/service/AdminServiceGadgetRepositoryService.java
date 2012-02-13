package org.wso2.carbon.admin.service;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.dashboard.mgt.gadgetrepo.stub.GadgetRepoServiceStub;
import org.wso2.carbon.dashboard.mgt.gadgetrepo.stub.types.carbon.Comment;
import org.wso2.carbon.dashboard.mgt.gadgetrepo.stub.types.carbon.Gadget;


import javax.activation.DataHandler;
import java.rmi.RemoteException;

public class AdminServiceGadgetRepositoryService {

    private static final Log log = LogFactory.getLog(AdminServiceGadgetRepositoryService.class);

    public void addGadgetToRepo(GadgetRepoServiceStub gadgetRepoServiceStub, DataHandler dataHandler, String gadgetName, String gadgetDescription) {

        try {
            boolean addGadgetStatus = gadgetRepoServiceStub.addGadgetEntryToRepo(
                    gadgetName, null, gadgetDescription, null, null, dataHandler);
            if (!addGadgetStatus) {
                log.error("Failed to add gadget to repository");
                Assert.fail("Failed to add gadget to repository");
            } else {
                log.info("Successfully executed addGadgetToUser test");
                log.debug("Gadget " + gadgetName + "added successfully");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed addGadgetToRepo test " + e);
        }
    }

    // Check the existence of added gadget and Get the path of added gadget
    public Gadget getGadgetFromGadgetList(GadgetRepoServiceStub gadgetRepoServiceStub, String gadgetName) {

        try {
            Gadget[] gadgets = gadgetRepoServiceStub.getGadgetData();
            Gadget returnGadget = null;
            if (gadgets != null) {
                for (Gadget gadget : gadgets) {
                    if (gadget != null) {
                        if (gadgetName.equals(gadget.getGadgetName())) {
                            log.debug("Added Gadget path is :" + gadget.getGadgetPath());
                            log.debug("Added Gadget Url is :" + gadget.getGadgetUrl());
                            log.info("Successfully executed getGadgetFromGadgetList test");
                            returnGadget = gadget;
                            break;
                        }
                    }
                }
            }
            return returnGadget;
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to get gadget Data" + e.getMessage());
            return null;
        }

    }

    // Check the existence of the added gadget using gadget path
    public void getGadgetFromPath(GadgetRepoServiceStub gadgetRepoServiceStub, String gadgetPath) {

        try {
            // If resource not exists in the path returns null
            Gadget gadget = gadgetRepoServiceStub.getGadget(gadgetPath);
            if (gadget == null) {
                log.error("Failed to get gadget using gadget path");
                Assert.fail("Failed to get gadget using gadget path");
            } else {
                log.info("Successfully executed getGadgetFromPath test");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed getGadgetFromPath test" + e.getMessage());
        }
    }

    // Add gadget to user's portal
    public void addGadgetToPortal(GadgetRepoServiceStub gadgetRepoServiceStub,
                                  String userID, String tabId, String gadgetUrl, String dashboardName,
                                  String gadgetGroup, String gadgetPath) {
        try {

            boolean addGadgetToPortalStatus = gadgetRepoServiceStub.addGadget(
                    userID, tabId, gadgetUrl, dashboardName, gadgetGroup, gadgetPath);
            if (!addGadgetToPortalStatus) {
                log.error("Failed to add gadget to the user's portal");
                Assert.fail("Failed to add gadget to the user's portal");
            } else {
                log.info("Successfully executed addGadgetToPortal test");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed addGadgetToPortal test" + e.getMessage());
        }
    }

    // Checking if the user has the added gadget
    public void userHasGadget(GadgetRepoServiceStub gadgetRepoServiceStub, String gadgetPath) {
        try {
            boolean userHasGadgetStatus = gadgetRepoServiceStub.userHasGadget(gadgetPath);
            if (!userHasGadgetStatus) {
                log.error("User has gadget failed");
                Assert.fail("User has gadget failed");
            } else {
                log.info("Successfully executed userHasGadget test");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed userHasGadget test" + e.getMessage());
        }

    }

    // Add comment on a gadget
    public void addCommentForGadget(GadgetRepoServiceStub gadgetRepoServiceStub, String gadgetPath, Comment comment) {
        try {
            boolean addCommentStatus = gadgetRepoServiceStub.addCommentForGadget(gadgetPath, comment);
            if (!addCommentStatus) {
                log.error("Failed to add comment for the gadget");
                Assert.fail("Failed to add comment for the gadget");
            } else {
                log.info("Successfully executed addCommentForGadget test");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed addCommentForGadget test" + e.getMessage());
        }
    }

    // Get comment count on a gadget
    public void getCommentCountForGadget(GadgetRepoServiceStub gadgetRepoServiceStub, String gadgetPath) {
        try {
            System.out.println(gadgetPath);
            int commentCount = gadgetRepoServiceStub.getCommentsCount(gadgetPath);
            if (commentCount < 1) {
                log.error("Failed to get comment count for the gadget");
                Assert.fail("Failed to get comment count for the gadget");
            } else {
                log.info("Successfully executed getCommentCountForGadget test");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed getCommentCountForGadget test" + e.getMessage());
        }
    }

    // Get comment set on a gadget
    public String getCommentSetForGadget(GadgetRepoServiceStub gadgetRepoServiceStub,
                                         String gadgetPath, int start, int size) {
        String gadgetxpath = null;
        try {
            Comment commentSet[] = gadgetRepoServiceStub.getCommentSet(gadgetPath, start, size);
            gadgetxpath = commentSet[0].getCommentPath();
            if (commentSet[0].getCommentText() == null) {

                log.error("Failed to get comment set for the gadget");
                Assert.fail("Failed to get comment set for the gadget");
            } else {
                log.info("Successfully executed getCommentSetForGadget test");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed getCommentSetForGadget test" + e.getMessage());
        }
        return gadgetxpath;
    }

    // Delete comment on a gadget
    public void deleteCommentOnGadget(GadgetRepoServiceStub gadgetRepoServiceStub, String gadgetPath) {
        try {
            String commentPath = gadgetPath;
            System.out.println(commentPath);
            boolean deleteCommentStatus = gadgetRepoServiceStub.deleteComment(commentPath);
            if (!deleteCommentStatus) {
                log.error("Failed to delete comment on gadget");
                Assert.fail("Failed to delete comment on gadget");
            } else {
                log.info("Successfully executed deleteCommentOnGadget test");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed deleteCommentOnGadget test" + e.getMessage());
        }
    }

    // Add Rating on a Gadget
    public void addRatingForGadget(GadgetRepoServiceStub gadgetRepoServiceStub, String gadgetPath, int rating,
                                   String tabId, String gadgetGroup) {
        try {
            boolean addRatingStatus = gadgetRepoServiceStub.addRatingForGadget(gadgetPath, rating, tabId, gadgetGroup);
            if (!addRatingStatus) {
                log.error("Failed to add rating for the gadget");
                Assert.fail("Failed to add rating for the gadget");
            } else {
                log.info("Successfully executed addRatingForGadget test");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed addRatingForGadget test" + e.getMessage());
        }
    }

    // Get user Rating on a Gadget
    public void getRatingOnGadget(GadgetRepoServiceStub gadgetRepoServiceStub, String gadgetPath, String userId) {
        try {
            String userRating = gadgetRepoServiceStub.getUserRating(gadgetPath, userId);
            if (!"3".equals(userRating)) {
                log.error("Failed to add rating for the gadget");
                Assert.fail("Failed to add rating for the gadget");
            } else {
                log.info("Successfully executed getRatingOnGadget test");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed getRatingOnGadget test" + e.getMessage());
        }
    }

    // Delete the added gadget from repository
    public void deleteGadgetFromRepo(GadgetRepoServiceStub gadgetRepoServiceStub, String gadgetPath) {
        try {
            boolean deleteStatus = gadgetRepoServiceStub.deleteGadget(gadgetPath);
            if (!deleteStatus) {
                log.error("Failed to Delete the gadget from repository");
                Assert.fail("Failed to Delete the gadget from repository");
            } else {
                log.info("Successfully executed deleteGadgetFromRepo test");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed deleteGadgetFromRepo test" + e.getMessage());
        }
    }


}
