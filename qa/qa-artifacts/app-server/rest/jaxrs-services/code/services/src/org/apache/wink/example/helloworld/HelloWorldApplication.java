package org.apache.wink.example.helloworld;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;


public class HelloWorldApplication extends Application {

@Override
public Set<Class<?>> getClasses() {
Set<Class<?>> classes = new HashSet<Class<?>>();
classes.add(HelloWorldResource.class);

    return classes;

}
}
