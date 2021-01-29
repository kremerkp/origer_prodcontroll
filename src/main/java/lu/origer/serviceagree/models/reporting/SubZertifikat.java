package lu.origer.serviceagree.models.reporting;

import java.io.Serializable;
import java.util.Date;

public class SubZertifikat implements Serializable {
	
	private String elementName; 
	
	private Date elementDate;
	
	private String elementState; 
	
	private String elementComment;
	
	private String floor; 
	
	private String room; 
	
	private String orientation; 
	
	private String checklistEntry;
	
	private Boolean functionalControl;
	
	private Boolean viewControl;

	private Boolean elementOk;
	
	private Boolean elementMangel;
	
	private Boolean elementOffer;
	
	private Boolean elementRepair;
	
	private String description; 
	
	private String urlFoto;
	
	private String urlFoto2;
	

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public Date getElementDate() {
		return elementDate;
	}

	public void setElementDate(Date elementDate) {
		this.elementDate = elementDate;
	}

	public String getElementState() {
		return elementState;
	}

	public void setElementState(String elementState) {
		this.elementState = elementState;
	}

	public String getElementComment() {
		return elementComment;
	}

	public void setElementComment(String elementComment) {
		this.elementComment = elementComment;
	}

	public String getChecklistEntry() {
		return checklistEntry;
	}

	public void setChecklistEntry(String checklistEntry) {
		this.checklistEntry = checklistEntry;
	}

	public Boolean getFunctionalControl() {
		return functionalControl;
	}

	public void setFunctionalControl(Boolean functionalControl) {
		this.functionalControl = functionalControl;
	}

	public Boolean getViewControl() {
		return viewControl;
	}

	public void setViewControl(Boolean viewControl) {
		this.viewControl = viewControl;
	}

	public Boolean getElementOk() {
		return elementOk;
	}

	public void setElementOk(Boolean elementOk) {
		this.elementOk = elementOk;
	}

	public Boolean getElementMangel() {
		return elementMangel;
	}

	public void setElementMangel(Boolean elementMangel) {
		this.elementMangel = elementMangel;
	}

	public Boolean getElementOffer() {
		return elementOffer;
	}

	public void setElementOffer(Boolean elementOffer) {
		this.elementOffer = elementOffer;
	}

	public Boolean getElementRepair() {
		return elementRepair;
	}

	public void setElementRepair(Boolean elementRepair) {
		this.elementRepair = elementRepair;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrlFoto() {
		return urlFoto;
	}

	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
	}

	public String getUrlFoto2() {
		return urlFoto2;
	}

	public void setUrlFoto2(String urlFoto2) {
		this.urlFoto2 = urlFoto2;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
	
	

}
