package entity;
import javax.persistence.*;


/*
 * ONLINE CUSTOMER ENTITY CLASS-> This is an example of Joined table inheritance
 */
	@Table(name="ONLINECUSTOMER")
	@Entity(name = "ONLINECUSTOMERJOINED") //Name of the entity
	@DiscriminatorValue("ONLINE")
	@PrimaryKeyJoinColumn(name="CUST_ID",referencedColumnName="CUST_ID")
	public class OnlineCustomerJoined extends CustomerJoined{

				
		@Column(name = "WEBSITE", length = 100)
		private String website;

		public String getWebsite() {
			return website;
		}

		public void setWebsite(String website) {
			this.website = website;
		}
		
		public String toString() {
			 StringBuffer sb = new StringBuffer();
			 sb.append(super.toString());
			 sb.append(" website: "+website);
			 return sb.toString();
		}


}
