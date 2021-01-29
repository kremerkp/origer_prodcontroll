package lu.origer.serviceagree.frontend.contract.list;

import java.io.Serializable;

import lu.origer.serviceagree.models.checklist.Checklist;

public class AddElements implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer amount;
	private String floor;
	private Checklist checklist;
	private Boolean showDeleteButton;
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public Checklist getChecklist() {
		return checklist;
	}
	public void setChecklist(Checklist checklist) {
		this.checklist = checklist;
	}
	public Boolean getShowDeleteButton() {
		return showDeleteButton;
	}
	public void setShowDeleteButton(Boolean showDeleteButton) {
		this.showDeleteButton = showDeleteButton;
	} 	
}
