package com.sample;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;


public class CustomerResourceApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(CustomerService.class);

        return classes;

    }
}
