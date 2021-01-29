package lu.origer.serviceagree.models.reporting;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class PrintHistory  implements Serializable{
	
	public String name; 
	
	public Date date; 
	
	public String state; 
	
	public String remark; 
	
	public String isRepaired; 
	
	public String toRepair;
	
	public List<PrintHistoryChecklist> histChecklist; 
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsRepaired() {
		return isRepaired;
	}

	public void setIsRepaired(String isRepaired) {
		this.isRepaired = isRepaired;
	}

	public String getToRepair() {
		return toRepair;
	}

	public void setToRepair(String toRepair) {
		this.toRepair = toRepair;
	}

	public List<PrintHistoryChecklist> getHistChecklist() {
		return histChecklist;
	}

	public void setHistChecklist(List<PrintHistoryChecklist> histChecklist) {
		this.histChecklist = histChecklist;
	}
	
	

}
