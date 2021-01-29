package lu.origer.serviceagree.frontend.report;

import java.util.ArrayList;
import java.util.Date;

import lu.origer.serviceagree.models.synch.ServiceHistory;
import lu.origer.serviceagree.models.synch.TimeRecordingHistory;

public class RepairReportChecklistEntry {
	private String entryDate;
	private Date date;
	private Integer elementId;
	private ArrayList<TimeRecordingHistory> times;
	private ArrayList<ServiceHistory> entries;
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public ArrayList<TimeRecordingHistory> getTimes() {
		return times;
	}
	public void setTimes(ArrayList<TimeRecordingHistory> times) {
		this.times = times;
	}	
	
	public Integer getElementId() {
		return elementId;
	}
	public void setElementId(Integer elementId) {
		this.elementId = elementId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	
	public ArrayList<ServiceHistory> getEntries() {
		return entries;
	}
	public void setEntries(ArrayList<ServiceHistory> entries) {
		this.entries = entries;
	}
	@Override
	public String toString()
	{
		return entryDate;
	}
}
