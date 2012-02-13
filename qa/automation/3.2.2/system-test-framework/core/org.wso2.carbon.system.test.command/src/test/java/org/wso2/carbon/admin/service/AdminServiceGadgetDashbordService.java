package org.wso2.carbon.admin.service;

import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.dashboard.stub.DashboardServiceStub;

import java.rmi.RemoteException;

public class AdminServiceGadgetDashbordService {
    private static final Log log = LogFactory.getLog(AdminServiceGadgetDashbordService.class);

    public void isSessionValid(DashboardServiceStub dashboardServiceStub) {
        try {
            boolean isValid = dashboardServiceStub.isSessionValid();
            if (!isValid) {
                log.error("Invalid session found in isSessionValid test");
                Assert.fail("Invalid session found in isSessionValid test");
            } else {
                log.info("Successfully executed isSessionValid test");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed isSessionValid test" + e.getMessage());
        }
    }

    // Get default gadget url set
    public void getDefaultGadgetUrlSet(DashboardServiceStub dashboardServiceStub, String userID) {
        try {
            String[] defaultGadgetUrlSet = dashboardServiceStub.getDefaultGadgetUrlSet(userID);
            if (defaultGadgetUrlSet != null) {
                for (String gadget : defaultGadgetUrlSet) {
                    log.debug("DefaultGadget: " + gadget + " -available");
                }
            }
            log.info("Successfully executed getDefaultGadgetUrlSet test");
        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed getDefaultGadgetUrlSet test" + e.getMessage());
        }
    }

    // Get dashboard content for the given user as a bean
    public void getDashboardContent(DashboardServiceStub dashboardServiceStub, String userId,
                                    String dashboardName, String tDomain, String backendServerURL) {
        try {
            dashboardServiceStub.getDashboardContent(userId, dashboardName, tDomain, backendServerURL);
            log.info("Successfully executed getDashboardContent test");

        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed getDashboardContent test" + e.getMessage());
        }
    }

    // Get tab layout for the user
    public void getTabLayout(DashboardServiceStub dashboardServiceStub, String userId,
                             String dashboardName) {
        try {
            String tabLayout = dashboardServiceStub.getTabLayout(userId, dashboardName);
            if (!"0".equals(tabLayout)) {
                log.error("Failed to get tab layout");
                Assert.fail("Failed to get tab layout");
            } else {
                log.info("Successfully executed getTabLayout test");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed getTabLayout test" + e.getMessage());
        }
    }


    // Retrieves the stored layout
    public void getGadgetLayout(DashboardServiceStub dashboardServiceStub, String userId, String tabId,
                                String dashboardName) {
        try {
            String gadgetLayout = dashboardServiceStub.getGadgetLayout(userId, tabId, dashboardName);
            if (gadgetLayout == null) {
                log.error("Failed to get gadget layout");
                Assert.fail("Failed to get gadget layout");
            } else {
                log.info("Successfully executed getGadgetLayout test");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to execute getGadgetLayout test" + e.getMessage());
        }

    }

    // Dashboard read-only mode checking
    public void isReadOnlyMode(DashboardServiceStub dashboardServiceStub, String userId) {
        try {
            boolean isReadOnlyMode = dashboardServiceStub.isReadOnlyMode(userId);
            if (isReadOnlyMode) {
                log.error("Dashboard can not be read only for this user");
                Assert.fail("Dashboard can not be read only for this user");
            } else {
                log.info("Successfully executed isReadOnlyMode test");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to execute isReadOnlyMode test" + e.getMessage());
        }
    }

    // Populate Default Three Column Layout
    public void populateDefaultThreeColumnLayout(DashboardServiceStub dashboardServiceStub,
                                                 String userId, String tabID) {
        try {
            // this call caused to test setGadgetLayout service too
            String columnLayout = dashboardServiceStub.populateDefaultThreeColumnLayout(userId, tabID);
            if (columnLayout == null) {
                log.error("Failed to populate default three column layout");
                Assert.fail("Failed to populate default three column layout");
            } else {
                log.info("Successfully executed populateDefaultThreeColumnLayout test");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed populateDefaultThreeColumnLayout test" + e.getMessage());
        }
    }

    // Set a given preference value for a Gadget
    public void setGadgetPrefs(DashboardServiceStub dashboardServiceStub, String userId, String gadgetId,
                               String prefId, String value, String dashboardName) {
        try {
            boolean setGadgetPrefsStatus = dashboardServiceStub.setGadgetPrefs(userId, gadgetId, prefId, value, dashboardName);
            if (!setGadgetPrefsStatus) {
                log.error("Failed to set given preference value for the gadget");
                Assert.fail("Failed to set given preference value for the gadget");
            } else {
                log.info("Successfully executed setGadgetPrefs test");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed setGadgetPrefs test" + e.getMessage());
        }
    }

    // Retrieves a given preference value for a Gadget
    public void getGadgetPrefs(DashboardServiceStub dashboardServiceStub, String userId, String gadgetId,
                               String prefId, String dashboardName) {
        try {
            String gadgetPerfs = dashboardServiceStub.getGadgetPrefs(userId, gadgetId, prefId, dashboardName);
            if (gadgetPerfs == null) {
                log.error("Failed to retrieves a given preference value for a gadget");
                Assert.fail("Failed to retrieves a given preference value for a gadget");
            } else {
                log.info("Successfully executed getGadgetPrefs test");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed getGadgetPrefs test" + e.getMessage());
        }
    }

    // Get gadget urls to layout
    public void getGadgetUrlsToLayout(DashboardServiceStub dashboardServiceStub, String userID, String tabID,
                                      String dashboardName, String backendServerURL) {
        try {
            String[] gadgetUrlsToLayout = dashboardServiceStub.getGadgetUrlsToLayout(
                    userID, tabID, dashboardName, backendServerURL);
            if (gadgetUrlsToLayout != null) {
                for (String gadget : gadgetUrlsToLayout) {
                    log.debug("gadgetUrl: " + gadget + " -available to layout");
                }
            }
            log.info("Successfully executed getGadgetUrlsToLayout test");
        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed getGadgetUrlsToLayout test" + e.getMessage());
        }
    }

    // Add new tab
    public int addNewTab(DashboardServiceStub dashboardServiceStub, String userID,
                         String tabTitle, String dashboardName) {
        int tabID = 0;
        try {
            tabID = dashboardServiceStub.addNewTab(userID, tabTitle, dashboardName);
            log.info("Successfully executed addNewTab test");
        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed addNewTab test" + e.getMessage());
        }
        return tabID;
    }

    // Get title of the given tab
    public String getTabTitle(DashboardServiceStub dashboardServiceStub, String userId, String tabId,
                              String dashboardName, String addedTabName) {
        String tabTitle = null;
        try {
            tabTitle = dashboardServiceStub.getTabTitle(userId, tabId, dashboardName);
            if (!addedTabName.equals(tabTitle)) {
                log.error("Failed to get tab title");
                Assert.fail("Failed to get tab title");
            } else {
                log.info("Successfully executed getTabTitle test");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to execute getTabTitle test" + e.getMessage());
        }
        return tabTitle;
    }

    // Removes a given tab from the system
    public void removeTab(DashboardServiceStub dashboardServiceStub, String userId, String tabId,
                          String dashboardName) {
        try {
            // This call caused to test removeGadget service too
            boolean removeTabStatus = dashboardServiceStub.removeTab(userId, tabId, dashboardName);
            if (!removeTabStatus) {
                log.error("Failed to remove tab");
                Assert.fail("Failed to remove tab");
            } else {
                log.info("Successfully executed removeTab test");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to execute removeTab test" + e.getMessage());
        }
    }

    // Duplicate tab
    public int duplicateTab(DashboardServiceStub dashboardServiceStub, String userID,
                            String dashboardName, String sourceTabId, String newTabName) {
        int tabID = 0;
        try {
            tabID = dashboardServiceStub.duplicateTab(userID, dashboardName, sourceTabId, newTabName);
            log.info("Successfully executed duplicateTab test");
        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed duplicateTab test" + e.getMessage());
        }
        return tabID;
    }

    // Add gadget to user
    public void addGadgetToUser(DashboardServiceStub dashboardServiceStub, String userID, String tabID, String url,
                                String dashboardName, String gadgetGroup) {
        try {
            boolean addGadgetToUserStatus = dashboardServiceStub.addGadgetToUser(userID, tabID, url, dashboardName, gadgetGroup);
            if (!addGadgetToUserStatus) {
                log.error("Failed to add gadget to user");
                Assert.fail("Failed to add gadget to user");
            } else {
                log.info("Successfully executed addGadgetToUser test");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            Assert.fail("Failed to executed addGadgetToUser test" + e.getMessage());
        }

    }
}
