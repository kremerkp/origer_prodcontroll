package lu.origer.serviceagree.models.reporting;

import java.util.List;

public class SubRepairElementReport {
	private String element;
	private List<SubRepairDateReport> dates;
	public String getElement() {
		return element;
	}
	public void setElement(String element) {
		this.element = element;
	}
	public List<SubRepairDateReport> getDates() {
		return dates;
	}
	public void setDates(List<SubRepairDateReport> dates) {
		this.dates = dates;
	}	
}
