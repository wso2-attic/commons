package org.wso2.balana.attr;

import org.w3c.dom.Node;
import org.wso2.balana.ParsingException;
import org.wso2.balana.PolicyMetaData;
import org.wso2.balana.XACMLConstants;
import org.wso2.balana.attr.xacml3.AttributeDesignator;

/**
 *
 */
public class AttributeDesignatorFactory {

    private static AttributeDesignatorFactory factoryInstance;

    public AbstractDesignator getAbstractDesignator(Node root, PolicyMetaData metaData)
                                                                        throws ParsingException {

        if(metaData.getXACMLVersion() == XACMLConstants.XACML_VERSION_3_0){
            return AttributeDesignator.getInstance(root);
        } else {
            return org.wso2.balana.attr.AttributeDesignator.getInstance(root);
        }
    }

    /**
     * Returns an instance of this factory. This method enforces a singleton model, meaning that
     * this always returns the same instance, creating the factory if it hasn't been requested
     * before.
    *
     * @return the factory instance
     */
    public static AttributeDesignatorFactory getFactory() {
        if (factoryInstance == null) {
            synchronized (AttributeDesignatorFactory.class) {
                if (factoryInstance == null) {
                    factoryInstance = new AttributeDesignatorFactory();
                }
            }
        }

        return factoryInstance;
    }

}
