package org.wso2.caching;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.description.*;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.AxisEvent;
import org.apache.axis2.engine.AxisObserver;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;

public class CachingObserver implements AxisObserver{

    private static Log log = LogFactory.getLog(CachingObserver.class);

    /** Caching module name. */
    public static final String CACHING_MODULE = "wso2caching";

    public void init(AxisConfiguration axisConfiguration) {
    }

    public void serviceUpdate(AxisEvent axisEvent, AxisService axisService) {
        log.debug("CachingObserver notified for a serviceUpdate.");

        AxisDescription axisDescription = axisEvent.getAxisDescription();
        if (axisDescription.isEngaged(axisService.getAxisConfiguration().
                getModule(CACHING_MODULE))) {
            if (axisEvent.getEventType() == AxisEvent.POLICY_ADDED) {
                try {
                    CachingEngageUtils.enguage(axisEvent.getAxisDescription());
                } catch (AxisFault axisFault) {
                    log.error("Error while re-engaging caching", axisFault);
                }
            }
        }
    }

    public void serviceGroupUpdate(AxisEvent axisEvent, AxisServiceGroup axisServiceGroup) {
    }

    public void moduleUpdate(AxisEvent axisEvent, AxisModule axisModule) {
    }

    public void addParameter(Parameter parameter) throws AxisFault {
    }

    public void removeParameter(Parameter parameter) throws AxisFault {
    }

    public void deserializeParameters(OMElement omElement) throws AxisFault {
    }

    public Parameter getParameter(String s) {
        return null;
    }

    public ArrayList<Parameter> getParameters() {
        return null;
    }

    public boolean isParameterLocked(String s) {
        return false;
    }
}
