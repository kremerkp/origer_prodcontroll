package lu.origer.serviceagree.models.reporting;

import java.util.Date;
import java.util.List;

public class SubRepairDateReport {
	private Date startTime;
	private String time; 
	private List<SubRepairReport> repairs;
	private String usedTime;
	private String status;
	
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<SubRepairReport> getRepairs() {
		return repairs;
	}
	public void setRepairs(List<SubRepairReport> repairs) {
		this.repairs = repairs;
	}
	public String getUsedTime() {
		return usedTime;
	}
	public void setUsedTime(String usedTime) {
		this.usedTime = usedTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}		
		
}
