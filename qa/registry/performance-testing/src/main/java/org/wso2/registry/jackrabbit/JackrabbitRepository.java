package org.wso2.registry.jackrabbit;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.jcr.LoginException;
import javax.jcr.NoSuchWorkspaceException;
import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.jackrabbit.core.jndi.RegistryHelper;
import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.apache.jackrabbit.ocm.manager.impl.ObjectContentManagerImpl;
import org.apache.jackrabbit.ocm.mapper.Mapper;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.AnnotationMapperImpl;
import org.wso2.registry.jackrabbit.nodetype.Comment;
import org.wso2.registry.jackrabbit.nodetype.Folder;
import org.wso2.registry.jackrabbit.nodetype.HierarchyNode;
import org.wso2.registry.jackrabbit.nodetype.Resource;

public class JackrabbitRepository {

    private static Repository repository;

    private JackrabbitConfig jackrabbitConfig;

    public JackrabbitRepository(JackrabbitConfig jackrabbit)
            throws RegistryException {
        init(jackrabbit);
    }

    public JackrabbitRepository() {
        try {
            init(getDefaultConfig());
        } catch (RegistryException e) {
            // TODO Swallowing the exception for the moment
            e.printStackTrace();
        }
    }

    /**
     * method that initialize the jackrabbit repository
     */
    public void init(JackrabbitConfig jackrabbitConfig)
            throws RegistryException {

        if (jackrabbitConfig == null) {
            throw new RegistryException("Jackrabbit config cannot be null");
        } else {
            this.jackrabbitConfig = jackrabbitConfig;
        }

        try {

            // TODO check whether JNDI is really necessary, Spec suggests to use
            // JNDI
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY,
                    JackrabbitConfig.INITIAL_CONTEXT_FACTORY);
            env.put(Context.PROVIDER_URL, JackrabbitConfig.PROVIDER_URL);
            InitialContext ctx = new InitialContext(env);

            RegistryHelper.registerRepository(ctx, jackrabbitConfig
                    .getRepositoryName(), jackrabbitConfig.getRepositoryXML(),
                    jackrabbitConfig.getRepositoryHome(), true);

            // Obtain the repository through a JNDI lookup
            repository = (Repository) ctx.lookup(jackrabbitConfig
                    .getRepositoryName());

            // Create a new repository session, after authenticating
            Session session = repository.login(new SimpleCredentials(
                    jackrabbitConfig.getSystemUser(), jackrabbitConfig
                            .getSystemUserPassword().toCharArray()), null);

            // Register node types and namespaces used by OCM
            RepositoryUtil.createNamespace(session);
            RepositoryUtil.registerNodeTypes(session, jackrabbitConfig
                    .getCustomNodetypesXML());

            session.logout();

        } catch (NamingException e) {
            throw new RegistryException("Somthing bad happened", e);
        } catch (RepositoryException e) {
            throw new RegistryException("Somthing bad happened", e);
        }

    }

    public static ObjectContentManager getObjectContentManager(Session session) {

        List<Class> classes = new ArrayList<Class>();
        // We must all the classes we want to persist using OCM here
        classes.add(HierarchyNode.class);
        classes.add(Folder.class);
        classes.add(Resource.class);
        classes.add(Comment.class);

        Mapper mapper = new AnnotationMapperImpl(classes);

        // return a object content manager instance to use
        return new ObjectContentManagerImpl(session, mapper);
    }

    private Session getJCRSession() throws RegistryException {

        try {
            Session session = repository.login(new SimpleCredentials(
                    jackrabbitConfig.getSystemUser(), jackrabbitConfig
                            .getSystemUserPassword().toCharArray()), null);

            return session;

        } catch (LoginException e) {
            throw new RegistryException("Something bad happend", e);
        } catch (NoSuchWorkspaceException e) {
            throw new RegistryException("Something bad happend", e);
        } catch (RepositoryException e) {
            throw new RegistryException("Something bad happend", e);
        }
    }

    public Session getJCRSession(String username, String password)
            throws RegistryException {

        try {
            Session session = repository.login(new SimpleCredentials(username,
                    password.toCharArray()), null);

            return session;

        } catch (LoginException e) {
            throw new RegistryException("Something bad happend", e);
        } catch (NoSuchWorkspaceException e) {
            throw new RegistryException("Something bad happend", e);
        } catch (RepositoryException e) {
            throw new RegistryException("Something bad happend", e);
        }
    }

    private JackrabbitConfig getDefaultConfig() {

        JackrabbitConfig jackrabbitConfig = new JackrabbitConfig();
        jackrabbitConfig.setRepositoryXML("my-repo/repository.xml");
        jackrabbitConfig.setRepositoryHome("my-repo");
        jackrabbitConfig.setCustomNodetypesXML("my-repo/custom_nodetypes.xml");
        jackrabbitConfig.setRepositoryName("repo");
        jackrabbitConfig.setSystemUser("wso2");
        jackrabbitConfig.setSystemUserPassword("wso2");

        return jackrabbitConfig;
    }

    public void shutdown() throws RegistryException {

        try {

            // TODO check whether JNDI is really necessary, Spec suggests to use
            // JNDI
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY,
                    JackrabbitConfig.INITIAL_CONTEXT_FACTORY);
            env.put(Context.PROVIDER_URL, JackrabbitConfig.PROVIDER_URL);
            InitialContext ctx = new InitialContext(env);

            RegistryHelper.unregisterRepository(ctx, jackrabbitConfig
                    .getRepositoryName());

        } catch (NamingException e) {
            throw new RegistryException("Something bad happened", e);
        }
    }

}
