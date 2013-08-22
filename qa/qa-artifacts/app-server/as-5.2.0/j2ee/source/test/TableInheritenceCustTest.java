package test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entity.CustomerJoined;
import entity.CustomerTable;
import entity.OnlineCustomerJoined;
import entity.OnlineCustomerTable;
/*
 * This is a test class for testing Table per class inheritance
 */
public class TableInheritenceCustTest {

	public static void main(String[] args) {
		EntityManagerFactory entityManagerFactory =  Persistence.createEntityManagerFactory("testjpa");
		
		EntityManager em = entityManagerFactory.createEntityManager();
		EntityTransaction userTransaction = em.getTransaction();
		
		userTransaction.begin();
		//inserting Customer
		CustomerTable customer = new CustomerTable();
		customer.setFirstName("Amit");
		customer.setLastName("Shukla");
		customer.setCustType("RETAIL");
		customer.getAddress().setStreet("111 Geary Street");
		customer.getAddress().setAppt("333");
		customer.getAddress().setCity("San Jose");
		customer.getAddress().setZipCode("33345");
		em.persist(customer);
		//inserting Online Customer
		OnlineCustomerTable onlineCust = new OnlineCustomerTable();
		onlineCust.setFirstName("Raj");
		onlineCust.setLastName("Tandon");
		onlineCust.setCustType("ONLINE");
		onlineCust.getAddress().setStreet("777 4th Street");
		onlineCust.getAddress().setAppt("444");
		onlineCust.getAddress().setCity("NewYork");
		onlineCust.getAddress().setZipCode("23456");
		onlineCust.setWebsite("www.ebay.com");
		em.persist(onlineCust);
		userTransaction.commit();
		
//		 fetch only the online customers
		Query query = em.createQuery("SELECT customer FROM ONLINECUSTOMERTABLE customer");
		List<OnlineCustomerJoined> list= query.getResultList();
		System.out.println("The list is: "+ list);
		for(int i=0;i<list.size();i++){
			System.out.println("ONLINE CUSTOMER ["+ i +"] " +  list.get(i));
		}
		em.close();
		entityManagerFactory.close();
		

	}
}
