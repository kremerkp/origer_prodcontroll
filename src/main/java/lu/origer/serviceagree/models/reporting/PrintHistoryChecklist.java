package lu.origer.serviceagree.models.reporting;

import java.io.Serializable;

public class PrintHistoryChecklist implements Serializable {
	
	public String checklistItemName; 
	
	private Boolean visualControl;

	private Boolean functionalControl;

	private Boolean checkedAsOk;

	private Boolean checkedAsDefect;

	private Boolean createOffer;

	private Boolean checkedAsLack;
	
	private Boolean checkedAsRepaired;
	
	private String description;
	
	public String urlImage;

	public String getChecklistItemName() {
		return checklistItemName;
	}

	public void setChecklistItemName(String checklistItemName) {
		this.checklistItemName = checklistItemName;
	}

	public Boolean getVisualControl() {
		return visualControl;
	}

	public void setVisualControl(Boolean visualControl) {
		this.visualControl = visualControl;
	}

	public Boolean getFunctionalControl() {
		return functionalControl;
	}

	public void setFunctionalControl(Boolean functionalControl) {
		this.functionalControl = functionalControl;
	}

	public Boolean getCheckedAsOk() {
		return checkedAsOk;
	}

	public void setCheckedAsOk(Boolean checkedAsOk) {
		this.checkedAsOk = checkedAsOk;
	}

	public Boolean getCheckedAsDefect() {
		return checkedAsDefect;
	}

	public void setCheckedAsDefect(Boolean checkedAsDefect) {
		this.checkedAsDefect = checkedAsDefect;
	}

	public Boolean getCreateOffer() {
		return createOffer;
	}

	public void setCreateOffer(Boolean createOffer) {
		this.createOffer = createOffer;
	}

	public Boolean getCheckedAsLack() {
		return checkedAsLack;
	}

	public void setCheckedAsLack(Boolean checkedAsLack) {
		this.checkedAsLack = checkedAsLack;
	}

	public Boolean getCheckedAsRepaired() {
		return checkedAsRepaired;
	}

	public void setCheckedAsRepaired(Boolean checkedAsRepaired) {
		this.checkedAsRepaired = checkedAsRepaired;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrlImage() {
		return urlImage;
	}

	public void setUrlImage(String urlImage) {
		this.urlImage = urlImage;
	} 
	
	

}
