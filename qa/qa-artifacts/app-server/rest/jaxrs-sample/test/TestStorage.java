import com.beans.Customer;
import com.db.Storage;
import junit.framework.TestCase;

import java.sql.SQLException;


public class TestStorage extends TestCase {

    public void testAddCustomer() {

        Storage storage = new Storage();

        Customer customer = new Customer();
        customer.setCustomerID(1);
        customer.setCustomerName("charitha");
        customer.setCustomerAge(33);
        customer.setCustomerAddress("piliyandala");

        storage.addCustomer(customer);


    }

     public void testUpdateCustomer() {

        Storage storage = new Storage();

        storage.updateCustomer("charitha", "Colombo");



    }

     public void testGetCustomerDetails() {

        Storage storage = new Storage();

         try {
             Customer customer = storage.getCustomerDetails(1);
             assertEquals("charitha", customer.getCustomerName());
         } catch (SQLException e) {
             e.printStackTrace(); 
         }


     }


    public void testDeleteCustomer(){
        Storage storage = new Storage();
        storage.deleteCustomer(1);
    }
}
