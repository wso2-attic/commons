package test;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import entity.CustomerJoined;
import entity.OnlineCustomerJoined;

/*
 * This is a test class for testing Joined table inheritance 
 */
public class JoinedInheritenceCustTest {

	public static void main(String[] args) {
		EntityManagerFactory entityManagerFactory =  Persistence.createEntityManagerFactory("testjpa");
		
		EntityManager em = entityManagerFactory.createEntityManager();
		EntityTransaction userTransaction = em.getTransaction();
		
		userTransaction.begin();
		//inserting Customer
		CustomerJoined customer = new CustomerJoined();
		customer.setFirstName("Stefan");
		customer.setLastName("Flemming");
		customer.setCustType("RETAIL");
		customer.getAddress().setStreet("10 Downing Street");
		customer.getAddress().setAppt("1");
		customer.getAddress().setCity("NewYork");
		customer.getAddress().setZipCode("12345");
		em.persist(customer);
		//inserting Online Customer
		OnlineCustomerJoined onlineCust = new OnlineCustomerJoined();
		onlineCust.setFirstName("Liews");
		onlineCust.setLastName("Hofkins");
		onlineCust.setCustType("ONLINE");
		onlineCust.getAddress().setStreet("1 Mission Street");
		onlineCust.getAddress().setAppt("111");
		onlineCust.getAddress().setCity("NewYork");
		onlineCust.getAddress().setZipCode("23456");
		onlineCust.setWebsite("www.buy.com");
		em.persist(onlineCust);
		userTransaction.commit();
		
//		 fetch only the online customers
		Query query = em.createQuery("SELECT customer FROM ONLINECUSTOMERJOINED customer");
		List<OnlineCustomerJoined> list= query.getResultList();
		System.out.println("The list is: "+ list);
		for(int i=0;i<list.size();i++){
			System.out.println("ONLINE CUSTOMER ["+ i +"] " +  list.get(i));
		}
		em.close();
		entityManagerFactory.close();
		

	}
}
