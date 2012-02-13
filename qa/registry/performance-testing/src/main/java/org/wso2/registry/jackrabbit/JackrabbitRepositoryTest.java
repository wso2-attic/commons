package org.wso2.registry.jackrabbit;

import org.wso2.registry.jackrabbit.nodetype.Comment;
import org.wso2.registry.jackrabbit.nodetype.Folder;
import org.wso2.registry.jackrabbit.nodetype.Resource;

public class JackrabbitRepositoryTest {

    public static void main(String[] args) throws RegistryException {

        JackrabbitRepository repository = new JackrabbitRepository();

        JackrabbitRegistry jackrabbitRegistry = new JackrabbitRegistry(
                repository, "wso2", "wso2");

        Folder a = new Folder("/folder1");

        jackrabbitRegistry.createPath(a);

        Resource resource = jackrabbitRegistry.newResource();

        resource.setPath("/folder1/test.txt");
        resource.setDescription("this is a test");
        resource.setContent("This is file the content".getBytes());

        resource.setProperty("key01", "value01");
        resource.setProperty("key02", "value02");

        Comment comment = new Comment("This is a comment");
        resource.addComment(comment);

        jackrabbitRegistry.put(resource);

        // Folder b = new Folder("/folder1/folder2");

        // jackrabbitRegistry.createPath(b);

        Resource resource2 = jackrabbitRegistry.get("/folder1/test.txt");
        System.out.println(resource2.getProperty("key02"));
        System.out.println(resource2.getComments().get(0).getText());

        jackrabbitRegistry.removePath(a);
    }

    private static JackrabbitConfig createJackrabbitConfig() {

        JackrabbitConfig jackrabbitConfig = new JackrabbitConfig();
        jackrabbitConfig.setRepositoryXML("my-repo/repository.xml");
        jackrabbitConfig.setRepositoryHome("my-repo");
        jackrabbitConfig.setCustomNodetypesXML("my-repo/custom_nodetypes.xml");
        jackrabbitConfig.setRepositoryName("repo");
        jackrabbitConfig.setSystemUser("wso2");
        jackrabbitConfig.setSystemUserPassword("wso2");

        return jackrabbitConfig;
    }

}
