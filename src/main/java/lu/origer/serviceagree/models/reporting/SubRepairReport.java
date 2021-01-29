package lu.origer.serviceagree.models.reporting;

public class SubRepairReport {	
	private String item;
	private String comment;		
	private String signature;
	private String customer;

	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String urlSignature) {
		this.signature = urlSignature;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}		
}
