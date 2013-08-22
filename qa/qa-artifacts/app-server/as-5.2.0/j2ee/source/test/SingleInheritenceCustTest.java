package test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entity.CustomerSingle;
import entity.OnlineCustomer;

/*
 * This is a test class for testing Single table inheritance
 */
public class SingleInheritenceCustTest {

	public static void main(String[] args) {
		EntityManagerFactory entityManagerFactory =  Persistence.createEntityManagerFactory("testjpa");
		
		EntityManager em = entityManagerFactory.createEntityManager();
		EntityTransaction userTransaction = em.getTransaction();
		
		userTransaction.begin();
		//inserting Customer
		CustomerSingle customer = new CustomerSingle();
		customer.setFirstName("Antony");
		customer.setLastName("John");
		customer.setCustType("RETAIL");
		customer.getAddress().setStreet("1 Broad street");
		customer.getAddress().setAppt("111");
		customer.getAddress().setCity("NewYork");
		customer.getAddress().setZipCode("23456");
		em.persist(customer);
//		inserting Online Customer
		OnlineCustomer onlineCust = new OnlineCustomer();
		onlineCust.setFirstName("Henry");
		onlineCust.setLastName("Ho");
		onlineCust.setCustType("ONLINE");
		onlineCust.getAddress().setStreet("1 Mission Street");
		onlineCust.getAddress().setAppt("222");
		onlineCust.getAddress().setCity("Seatle");
		onlineCust.getAddress().setZipCode("33345");
		onlineCust.setWebsite("www.amazon.com");
		em.persist(onlineCust);
		userTransaction.commit();
		
		// fetch only the online customers
		Query query = em.createQuery("SELECT customer FROM ONLINECUSTOMER customer");
		List<OnlineCustomer> list= query.getResultList();
		System.out.println("The list is: "+ list);
		for(int i=0;i<list.size();i++){
			System.out.println("ONLINE CUSTOMER ["+ i +"] " +  list.get(i));
		}
		em.close();
		entityManagerFactory.close();
		

	}
}
