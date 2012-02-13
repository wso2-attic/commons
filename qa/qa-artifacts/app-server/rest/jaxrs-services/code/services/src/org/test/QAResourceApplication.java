package org.test;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

public class QAResourceApplication extends Application {


@Override
public Set<Class<?>> getClasses() {
Set<Class<?>> classes = new HashSet<Class<?>>();
classes.add(QAResource.class);

    return classes;

}

}
