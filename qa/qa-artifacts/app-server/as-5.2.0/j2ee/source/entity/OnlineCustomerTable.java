package entity;
import javax.persistence.*;


/*
 * ONLINE CUSTOMER ENTITY CLASS-> This is an example of Table per class inheritance
 */
	@Table(name="ONLINECUSTOMER")
	@Entity(name = "ONLINECUSTOMERTABLE") //Name of the entity
	public class OnlineCustomerTable extends CustomerTable{

				
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
