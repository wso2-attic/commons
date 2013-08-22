package entity;

import javax.persistence.*;

/*
 * This is the callback class for CUSTOMER entity
 */
public class CustListner {
	@PostLoad
	public void postLoad(Customer cust) {
        System.out.println("In post load");
    }
	@PrePersist
	public void prePersist(Customer cust) {
        System.out.println("In pre persist");
    }
	@PostPersist
	public void postPersist(Customer cust) {
        System.out.println("In post persist");
    }
	@PreUpdate
	public void preUpdate(Customer cust) {
        System.out.println("In pre update");
        
    }
	@PostUpdate
	public void postUpdate(Customer cust) {
        System.out.println("In post update");
    }
	@PreRemove
	public void preRemove(Customer cust) {
        System.out.println("In pre remove");
    }
	@PostRemove
	public void postRemove(Customer cust) {
        System.out.println("In post remove");
    }
}
