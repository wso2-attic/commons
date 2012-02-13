package org.wso2.registry.jackrabbit;

import org.wso2.registry.jackrabbit.nodetype.Resource;

/**
 * CoreRegistry is the central get/put API for the Registry.  This is typically what you want
 * if you're a Java programmer wanting to simply store and manage Resources.  Since it's an
 * interface, you don't care if the results come from (say) an embedded file-based Registry or
 * a remote one - that decision (and the resulting cache dynamics, exception throwing, etc)
 * can be made by a factory or dependency injection.
 *
 * If you want programatic access to features like tags/comments/ratings/versions, please
 * have a look at the full Registry interface which extends this one.
 */
public interface CoreRegistry {

    Resource newResource() throws RegistryException;

    /**
     * Returns the resource at the given path.
     *
     * @param path Path of the resource. e.g. /project1/server/deployment.xml
     * @return Resource instance
     * @throws org.wso2.registry.RegistryException
     *          is thrown if the resource is not in the registry
     */
    Resource get(String path) throws RegistryException;

    /**
     * Check whether a resource exists at the given path
     *
     * @param path Path of the resource to be checked
     * @return true if a resource exists at the given path, false otherwise.
     * @throws org.wso2.registry.RegistryException
     *          if an error occurs
     */
    boolean resourceExists(String path) throws RegistryException;

    /**
     * Adds or updates resources in the registry. If there is no resource at the given path,
     * resource is added. If a resource already exist at the given path, it will be replaced with
     * the new resource.
     *
     * @param suggestedPath the path which we'd like to use for the new resource.
     * @param resource      Resource instance for the new resource
     * @return the actual path that the server chose to use for our Resource
     * @throws org.wso2.registry.RegistryException
     *
     */
    void put(Resource resource) throws RegistryException;

    /**
     * Deletes the resource at the given path. If the path refers to a directory, all child
     * resources of the directory will also be deleted.
     *
     * @param path Path of the resource to be deleted.
     * @throws RegistryException is thrown depending on the implementation.
     */
    void delete(String path) throws RegistryException;
}