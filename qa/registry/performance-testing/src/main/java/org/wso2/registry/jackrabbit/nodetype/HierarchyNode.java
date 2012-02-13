package org.wso2.registry.jackrabbit.nodetype;

import java.util.Calendar;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

@Node(jcrType = "nt:hierarchyNode", discriminator = false)
public class HierarchyNode {
    @Field(path = true)
    private String path;

    @Field(jcrName = "jcr:created")
    private Calendar creationDate;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

}
