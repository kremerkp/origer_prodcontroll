package lu.origer.serviceagree.models.reporting;

import java.io.Serializable;
import java.util.List;

import lu.origer.serviceagree.models.contract.Invoice;
import lu.origer.serviceagree.models.synch.TimeRecordingHistory;

public class UebersichtVertragGesamt implements Serializable {
	
	private List<RegiezeitenReport> regieZeitReportList; 
	
	private List<IntervalOverview> intervalOverviewList;
	
	private List<Invoice> invoiceList;
	
	public String creator; 
	
	public String contractName; 
	
	public String contractCode; 
	
	public String buldingSite; 
	
	public String technician; 
	
	public String monteuer; 
	
	public String serviceType; 

	public List<RegiezeitenReport> getRegieZeitReportList() {
		return regieZeitReportList;
	}

	public void setRegieZeitReportList(List<RegiezeitenReport> regieZeitReportList) {
		this.regieZeitReportList = regieZeitReportList;
	}

	public List<IntervalOverview> getIntervalOverviewList() {
		return intervalOverviewList;
	}

	public void setIntervalOverviewList(List<IntervalOverview> intervalOverviewList) {
		this.intervalOverviewList = intervalOverviewList;
	}

	public List<Invoice> getInvoiceList() {
		return invoiceList;
	}

	public void setInvoiceList(List<Invoice> invoiceList) {
		this.invoiceList = invoiceList;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getBuldingSite() {
		return buldingSite;
	}

	public void setBuldingSite(String buldingSite) {
		this.buldingSite = buldingSite;
	}

	public String getTechnician() {
		return technician;
	}

	public void setTechnician(String technician) {
		this.technician = technician;
	}

	public String getMonteuer() {
		return monteuer;
	}

	public void setMonteuer(String monteuer) {
		this.monteuer = monteuer;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	

}
