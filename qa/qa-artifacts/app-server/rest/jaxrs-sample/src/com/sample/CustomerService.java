package com.sample;

import com.beans.Customer;
import com.db.Storage;

import javax.ws.rs.*;
import java.sql.SQLException;

@Path("/qa")
public class CustomerService {

    @GET
    @Path("/customer/{customerid}")
    @Produces("text/plain")
    public String getCustomerName(@PathParam("customerid") int customerID) {
        Storage storage = new Storage();
        Customer customer = null;
        try {
            customer = storage.getCustomerDetails(customerID);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer.getCustomerName();
    }


    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Path("/customer")
    public void addCustomer(@FormParam("customerid") int customerID, @FormParam("customername") String customerName, @FormParam("customerage") int customerAge, @FormParam("customeraddress") String customerAddress){

        Storage storage = new Storage();
        Customer customer = new Customer();
        customer.setCustomerID(customerID);
        customer.setCustomerName(customerName);
        customer.setCustomerAge(customerAge);
        customer.setCustomerAddress(customerAddress);
        storage.addCustomer(customer);


    }

    @PUT
    @Consumes("application/x-www-form-urlencoded")
    @Path("/customer")
    public void updateCustomer(@FormParam("customername") String customerName, @FormParam("customeraddress") String customerAddress){

        Storage storage = new Storage();
        storage.updateCustomer(customerName, customerAddress);


    }

    @DELETE
    @Path("/customer/{customerid}")

    public void deleteUser(@PathParam("customerid") int customerID) {

        Storage storage = new Storage();
        storage.deleteCustomer(customerID);

    }



}
