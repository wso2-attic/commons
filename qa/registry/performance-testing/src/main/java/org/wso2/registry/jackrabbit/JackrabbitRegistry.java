package org.wso2.registry.jackrabbit;

import javax.jcr.Session;
import javax.jcr.version.VersionException;

import org.apache.jackrabbit.ocm.manager.ObjectContentManager;
import org.wso2.registry.jackrabbit.nodetype.Folder;
import org.wso2.registry.jackrabbit.nodetype.Resource;

public class JackrabbitRegistry implements CoreRegistry {

    private ObjectContentManager objectContentManager;

    private Session jcrSession;

    public JackrabbitRegistry(JackrabbitRepository repository, String username,
            String password) throws RegistryException {

        jcrSession = repository.getJCRSession(username, password);
        objectContentManager = repository.getObjectContentManager(jcrSession);

    }

    public void delete(String path) throws RegistryException {

        objectContentManager.remove(path);
        objectContentManager.save();

    }

    public Resource get(String path) throws RegistryException {

        Resource resource = (Resource) objectContentManager.getObject(path);
        return resource;
    }

    public Resource newResource() {
        return new Resource();
    }

    public void put(Resource resource) throws RegistryException {

        objectContentManager.insert(resource);
        objectContentManager.save();

    }
    
    public void update(Resource resource) throws RegistryException {
        try {
            objectContentManager.checkout(resource.getPath()); 
            objectContentManager.update(resource);
            objectContentManager.save();
            objectContentManager.checkin(resource.getPath());
        } catch (VersionException e) {
            throw new RegistryException("Something bad happened",e);
        }
        
        
    }

    public boolean resourceExists(String path) throws RegistryException {

        boolean exists = objectContentManager.objectExists(path);
        return exists;
    }

    public void createPath(Folder folder) throws RegistryException {

        objectContentManager.insert(folder);
        objectContentManager.save();
    }

    public void removePath(Folder folder) throws RegistryException {

        objectContentManager.remove(folder);
        objectContentManager.save();

    }

    public void logout() {
        if (jcrSession != null) {
            jcrSession.logout();
        }
    }

}
