package entity;
import javax.persistence.*;


/*
 * ONLINE CUSTOMER ENTITY CLASS -> This is an example of Single table inheritance
 */
	
	@Entity(name = "ONLINECUSTOMER") //Name of the entity
	@DiscriminatorValue("ONLINE")

	public class OnlineCustomer extends CustomerSingle{
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
