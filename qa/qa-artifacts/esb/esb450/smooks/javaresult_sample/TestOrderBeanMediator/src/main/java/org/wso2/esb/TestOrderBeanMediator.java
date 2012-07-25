package org.wso2.esb;

import org.apache.synapse.ManagedLifecycle;
import org.apache.synapse.MessageContext;
import org.apache.synapse.SynapseLog;
import org.apache.synapse.core.SynapseEnvironment;
import org.apache.synapse.mediators.AbstractMediator;
import org.milyn.payload.JavaResult;

import example.model.Header;
import example.model.Order;

public class TestOrderBeanMediator extends AbstractMediator implements ManagedLifecycle{

	public boolean mediate(MessageContext synCtx) {
		SynapseLog synLog = getLog(synCtx);

        if (synLog.isTraceOrDebugEnabled()) {
            synLog.traceOrDebug("Start : Smooks mediator");

            if (synLog.isTraceTraceEnabled()) {
                synLog.traceTrace("Message : " + synCtx.getEnvelope());
            }
        }
        JavaResult result = (JavaResult)synCtx.getProperty("order");
        Order order = (Order)result.getBean("order");
        Header header = order.getHeader();
        synLog.auditLog(header.getCustomerName());
	synLog.auditLog(header.getcCustomerNumber());
		return true;
	}

	public void init(SynapseEnvironment se) {
		// TODO Auto-generated method stub
		
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
