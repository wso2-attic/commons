For this handler to take effect, it should be dropped into GREG_HOME/repository/components/lib folder (since this is not packed as an osgi bundle) and the server should be restarted.

Once restarted, add the following as a new Handler through the Management console.

<handler class="org.wso2.carbon.app.test.handler.JPGHandler">
    <filter class="org.wso2.carbon.registry.core.jdbc.handlers.filters.MediaTypeMatcher">
        <property name="mediaType">image/jpeg</property>
    </filter>
</handler>

Now upload a file of type .JPG to the registry and the following should happen automatically.

- A description should be added to the resource
- A property should be added to the resource
- An association should be added to a file named '/documentation/jpegdocs/jpegDescription.txt'
- The life cyle 'ServiceLifeCycle' should be attached to the 
- The resource should be tagged as 'picture'
- The resource should be rated as 4
- Three comments should be added to the resource


This handler will be extended to hold more funtionalities in future
