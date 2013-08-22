package test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.apache.openjpa.persistence.OpenJPAEntityManager;
import org.apache.openjpa.persistence.OpenJPAPersistence;

import entity.Customer;

/*
 * This is the test class for Inserting/fetching CUSTOMER information
 */
public class CustomerTest {

	public static void main(String[] args) {
		EntityManagerFactory entityManagerFactory =  Persistence.createEntityManagerFactory("testjpa");
		
		EntityManager em = entityManagerFactory.createEntityManager();
		EntityTransaction userTransaction = em.getTransaction();
		
		userTransaction.begin();
		//inserting Customer
		Customer customer = new Customer();
		customer.setFirstName("Charles");
		customer.setLastName("Dickens");
		customer.setCustType("RETAIL");
		customer.getAddress().setStreet("10 Downing Street");
		customer.getAddress().setAppt("1");
		customer.getAddress().setCity("NewYork");
		customer.getAddress().setZipCode("12345");
		em.persist(customer);
		userTransaction.commit();
		
		OpenJPAEntityManager oem = OpenJPAPersistence.cast(em);
		Object objId = oem.getObjectId(customer);
		Customer cust = em.find(Customer.class, objId);
		
		em.close();
		entityManagerFactory.close();
		System.out.println("Customer info: " + cust);

	}
}
