package org.apache.wink.example.helloworld;

import javax.ws.rs.GET;
import javax.ws.rs.Path;




@Path("/helloworld")
public class HelloWorldResource {

    @GET
    public String getMessage() {
        return "Hello Charitha! Welcome to Apache Wink....!!!";
    }


}
