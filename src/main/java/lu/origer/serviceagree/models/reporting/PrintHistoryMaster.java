package lu.origer.serviceagree.models.reporting;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PrintHistoryMaster implements Serializable{

	List<PrintHistory> printHistory;
	
	private String barcode; 
	
	private Date lastControlDate; 
	
	private String actualState; 
	
	private String room; 
	
	private String floor; 
	
	private String orientation; 
	
	private String contractNumber;

	public List<PrintHistory> getPrintHistory() {
		return printHistory;
	}

	public void setPrintHistory(List<PrintHistory> printHistory) {
		this.printHistory = printHistory;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public Date getLastControlDate() {
		return lastControlDate;
	}

	public void setLastControlDate(Date lastControlDate) {
		this.lastControlDate = lastControlDate;
	}

	public String getActualState() {
		return actualState;
	}

	public void setActualState(String actualState) {
		this.actualState = actualState;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getContractNumber()
	{
		return contractNumber;
	}

	public void setContractNumber(String contractNumber)
	{
		this.contractNumber = contractNumber;
	} 	
}
