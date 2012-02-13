package org.wso2.registry.jackrabbit;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Hashtable;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Workspace;
import javax.jcr.nodetype.NoSuchNodeTypeException;
import javax.jcr.nodetype.NodeTypeManager;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.jackrabbit.core.jndi.RegistryHelper;
import org.apache.jackrabbit.core.nodetype.NodeTypeDef;
import org.apache.jackrabbit.core.nodetype.NodeTypeManagerImpl;
import org.apache.jackrabbit.core.nodetype.NodeTypeRegistry;
import org.apache.jackrabbit.core.nodetype.xml.NodeTypeReader;
import org.apache.jackrabbit.ocm.exception.RepositoryException;
import org.apache.jackrabbit.util.ISO9075;
import org.apache.jackrabbit.util.Text;

public class RepositoryUtil {

    /** namespace prefix for OCM */
    public static final String OCM_NAMESPACE_PREFIX = "ocm";

    /** namespace for OCM */
    public static final String OCM_NAMESPACE = "http://jackrabbit.apache.org/ocm";

    /** namespace prefix for WSO2 registry node types */
    public static final String REGISTRY_NAMESPACE_PREFIX = "registry";

    /** namespace for for WSO2 registry node types */
    public static final String REGISTRY_NAMESPACE = "http://www.wso2.org/registry";

    /** Item path separator */
    public static final String PATH_SEPARATOR = "/";

    private final static Log log = LogFactory.getLog(RepositoryUtil.class);

    /**
     * Register a new repository
     * 
     * @param repositoryName
     *                The repository unique name
     * @param configFile
     *                The JCR config file
     * @param homeDir
     *                The directory containing the complete repository settings
     *                (workspace, node types, ...)
     * 
     * @throws RepositoryException
     *                 when it is not possible to register the repository
     */
    public static void registerRepository(String repositoryName,
            String configFile, String homeDir) throws RepositoryException {
        try {
            Hashtable env = new Hashtable();
            env
                    .put(Context.INITIAL_CONTEXT_FACTORY,
                            "org.apache.jackrabbit.core.jndi.provider.DummyInitialContextFactory");
            env.put(Context.PROVIDER_URL, "localhost");
            InitialContext ctx = new InitialContext(env);

            RegistryHelper.registerRepository(ctx, repositoryName, configFile,
                    homeDir, true);
        } catch (Exception e) {
            throw new RepositoryException(
                    "Impossible to register the respository : "
                            + repositoryName + " - config file : " + configFile,
                    e);
        }

    }

    /**
     * Unregister a repository
     * 
     * @param repositoryName
     *                The repository unique name
     * 
     * @throws RepositoryException
     *                 when it is not possible to unregister the repository
     */
    public static void unRegisterRepository(String repositoryName)
            throws RepositoryException {
        try {
            Hashtable env = new Hashtable();
            env
                    .put(Context.INITIAL_CONTEXT_FACTORY,
                            "org.apache.jackrabbit.core.jndi.provider.DummyInitialContextFactory");
            env.put(Context.PROVIDER_URL, "localhost");
            InitialContext ctx = new InitialContext(env);

            RegistryHelper.unregisterRepository(ctx, repositoryName);
        } catch (Exception e) {
            throw new RepositoryException(
                    "Impossible to unregister the respository : "
                            + repositoryName, e);
        }

    }

    /**
     * Get a repository
     * 
     * @param repositoryName
     *                The repository name
     * @return a JCR repository reference
     * 
     * @throws RepositoryException
     *                 when it is not possible to get the repository. Before
     *                 calling this method, the repository has to be registered
     *                 (@see RepositoryUtil#registerRepository(String, String,
     *                 String)
     */
    public static Repository getRepository(String repositoryName)
            throws RepositoryException {
        try {
            Hashtable env = new Hashtable();
            env
                    .put(Context.INITIAL_CONTEXT_FACTORY,
                            "org.apache.jackrabbit.core.jndi.provider.DummyInitialContextFactory");
            env.put(Context.PROVIDER_URL, "localhost");
            InitialContext ctx = new InitialContext(env);

            Repository repository = (Repository) ctx.lookup(repositoryName);
            return repository;
        } catch (Exception e) {
            throw new RepositoryException("Impossible to get the repository : "
                    + repositoryName, e);
        }
    }

    /**
     * Connect to a JCR repository
     * 
     * @param repository
     *                The JCR repository
     * @param user
     *                The user name
     * @param password
     *                The password
     * @return a valid JCR session
     * 
     * @throws RepositoryException
     *                 when it is not possible to connect to the JCR repository
     */
    public static Session login(Repository repository, String user,
            String password) throws RepositoryException {
        try {
            Session session = repository.login(new SimpleCredentials(user,
                    password.toCharArray()), null);

            return session;
        } catch (Exception e) {
            throw new RepositoryException("Impossible to login ", e);

        }

    }

    public static Session login(Repository repository, String user,
            String password, String workspace) throws RepositoryException {
        try {
            Session session = repository.login(new SimpleCredentials(user,
                    password.toCharArray()), workspace);
            return session;
        } catch (Exception e) {
            throw new RepositoryException("Impossible to login ", e);

        }

    }

    /**
     * Create the ocm namespace
     * 
     */
    public static void createNamespace(Session session)
            throws RepositoryException {
        try {
            log.info("Setup Jcr session setup ...");

            String[] jcrNamespaces = session.getWorkspace()
                    .getNamespaceRegistry().getPrefixes();
            boolean ocmNamespaceExists = false;
            boolean registryNamespaceExists = false;

            for (int i = 0; i < jcrNamespaces.length; i++) {
                if (jcrNamespaces[i].equals(OCM_NAMESPACE_PREFIX)) {
                    ocmNamespaceExists = true;
                    log.debug("Jackrabbit OCM namespace exists.");
                } else if (jcrNamespaces[i].equals(REGISTRY_NAMESPACE_PREFIX)) {
                    registryNamespaceExists = true;
                    log.debug("WSO2 Registry namespace exists.");
                }
            }

            if (!ocmNamespaceExists) {
                session.getWorkspace().getNamespaceRegistry()
                        .registerNamespace(OCM_NAMESPACE_PREFIX, OCM_NAMESPACE);
                log.info("Successfully created Jackrabbit OCM namespace.");
            }

            if (!registryNamespaceExists) {
                session.getWorkspace().getNamespaceRegistry()
                        .registerNamespace(REGISTRY_NAMESPACE_PREFIX,
                                REGISTRY_NAMESPACE);
                log.info("Successfully created WSO2 Registry namespace.");
            }

            if (session.getRootNode() != null) {
                log.info("Jcr session setup successfull.");
            }

        } catch (Exception e) {
            log.error("Error while setting up the jcr session.", e);
            throw new RepositoryException(e.getMessage());
        }
    }

    public static void registerNodeTypes(Session session, String nodeTypeFile) {
        try {
            InputStream xml = new FileInputStream(nodeTypeFile);

            NodeTypeDef[] types = NodeTypeReader.read(xml);

            Workspace workspace = session.getWorkspace();
            NodeTypeManager ntMgr = workspace.getNodeTypeManager();
            NodeTypeRegistry ntReg = ((NodeTypeManagerImpl) ntMgr)
                    .getNodeTypeRegistry();

            for (int j = 0; j < types.length; j++) {
                NodeTypeDef def = types[j];

                try {
                    ntReg.getNodeTypeDef(def.getName());
                } catch (NoSuchNodeTypeException nsne) {
                    // HINT: if not already registered than register custom node
                    // type
                    ntReg.registerNodeType(def);
                }

            }
        } catch (Exception e) {
            throw new RepositoryException("Impossible to register node types",
                    e);
        }
    }

    /**
     * Encode a path
     * 
     * @param path
     *                the path to encode
     * @return the encoded path
     * 
     */
    public static String encodePath(String path) {
        String[] pathElements = Text.explode(path, '/');
        for (int i = 0; i < pathElements.length; i++) {
            pathElements[i] = ISO9075.encode(pathElements[i]);
        }
        return "/" + Text.implode(pathElements, "/");
    }

}
