package org.wso2.registry.jackrabbit.nodetype;

import java.util.Date;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

@Node(jcrType = "registry:comment", discriminator = false)
public class Comment {

    @Field(path = true)
    private String commentPath;

    @Field(jcrName = "registry:text")
    private String text;

    @Field(jcrName = "registry:user")
    private String user;

    @Field(jcrName = "registry:time")
    private Date time;

    public Comment() {
    }

    public Comment(String commentText) {
        this.text = commentText;
        time = new Date();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getCommentPath() {
        return commentPath;
    }

    public void setCommentPath(String commentPath) {
        this.commentPath = commentPath;
    }

    public String getMediaType() {
        return "application/atom+xml";
    }

    public String getAuthorUserName() {
        return user;
    }

    public Date getLastModified() {
        return time;
    }

}
