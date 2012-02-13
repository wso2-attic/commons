package org.test;

import org.test.beans.User;
import org.test.db.Storage;

import javax.ws.rs.*;
import java.io.IOException;
import java.sql.SQLException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



@Path("/qa")
public class QAResource {

    @GET
    @Path("/user/{name}")
    @Produces("application/json")
    //@Provider("")
    public Response getUser(@PathParam("name") String name) throws SQLException {
        Storage storage = new Storage();

        User user = storage.getUserDetails(name);
        //return Response.ok(user.getName(), MediaType.TEXT_XML_TYPE).build();
         return Response.ok(user.getName(), MediaType.APPLICATION_JSON).build();

    }

//    @POST
//    @Consumes("text/xml")
//    @Produces("text/xml")
//    @Path("/user")
//    public void addUser(String incomingXML) throws IOException, SAXException, ParserConfigurationException {
//
//        System.out.println("XML message " + incomingXML);
//
//        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
//        Document doc = dBuilder.parse(incomingXML);
//        doc.getDocumentElement().normalize();
//
//        System.out.println(doc.getFirstChild().getTextContent());
//
//        User user = new User();
////        user.setName();
////        user.setAddress();
////        user.setAge();
//
//        Storage storage = new Storage();
//        storage.addUser(user);
//
//    }
//
    @POST
    @Consumes("text/plain")
    @Produces("text/plain")
    @Path("/user")
    public void addUser(String username) {

        System.out.println("incoming message " + username);


        Storage storage = new Storage();
        storage.addUsername(username);

    }

    @PUT
    @Consumes("text/plain")
    @Produces("text/plain")
    @Path("/user/update")
    public void updateUser(String username) {

        System.out.println("incoming message " + username);


        Storage storage = new Storage();
        storage.updateUsername(username);

    }

    @DELETE
    @Path("/user/{name}")

    public void deleteUser(@PathParam("name") String username) {

        Storage storage = new Storage();
        storage.deleteUser(username);

    }
}
