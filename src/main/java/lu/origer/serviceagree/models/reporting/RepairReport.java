package lu.origer.serviceagree.models.reporting;

import java.util.List;

public class RepairReport {
	private String buildingSite;
	private String buildingSiteCode;		
	private List<SubRepairElementReport> subRepair;
	
	public String getBuildingSite() {
		return buildingSite;
	}
	public void setBuildingSite(String buildingSite) {
		this.buildingSite = buildingSite;
	}
	public String getBuildingSiteCode() {
		return buildingSiteCode;
	}
	public void setBuildingSiteCode(String buildingSiteCode) {
		this.buildingSiteCode = buildingSiteCode;
	}
	public List<SubRepairElementReport> getSubRepair() {
		return subRepair;
	}
	public void setSubRepair(List<SubRepairElementReport> subRepairReport) {
		this.subRepair = subRepairReport;
	}
}
