package entity;
import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;
/*
 * CUSTOMER ENTITY CLASS
 */
	@EntityListeners({CustListner.class})
	@Entity(name = "CUSTOMER") //Name of the entity
	public class Customer implements Serializable{
		@Id //signifies the primary key
		@Column(name = "CUST_ID", nullable = false)
		@GeneratedValue(strategy = GenerationType.AUTO)
		private long custId;
		
		@Column(name = "FIRST_NAME", nullable = false,length = 50)
		private String firstName;
		
		@Column(name = "LAST_NAME", length = 50)
		private String lastName;
		
		@Embedded
		private Address address = new Address();
		
		@Column(name = "CUST_TYPE", length = 10)
		private String custType;
		
		@Version
		@Column(name = "LAST_UPDATED_TIME")
		private Date updatedTime;

		//Getters and setters
		public long getCustId() {
			return custId;
		}

		public void setCustId(long custId) {
			this.custId = custId;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public Date getUpdatedTime() {
			return updatedTime;
		}

		public void setUpdatedTime(Date updatedTime) {
			this.updatedTime = updatedTime;
		}

		public Address getAddress() {
			return address;
		}

		public void setAddress(Address address) {
			this.address = address;
		}

		public String getCustType() {
			return custType;
		}

		public void setCustType(String custType) {
			this.custType = custType;
		}
		// ToString()
		public String toString() {
	       StringBuffer sb = new StringBuffer();
	       sb.append("custId : " + custId);
	       sb.append("   First Name : " + firstName);
	       sb.append("   Last Name : " + lastName);
	       sb.append("   customer type : " + custType);

	       return sb.toString();
	    }
}
