package lu.origer.serviceagree.models.contract;

import java.io.Serializable;

public class OfferElements implements Serializable{
	
	private String name; 
	
	private String state; 
	
	private Boolean createOffer;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Boolean getCreateOffer() {
		return createOffer;
	}

	public void setCreateOffer(Boolean createOffer) {
		this.createOffer = createOffer;
	} 
	

}
