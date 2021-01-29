package lu.origer.serviceagree.models.reporting;

import java.util.Date;
import java.util.List;

import lu.origer.serviceagree.models.synch.ServiceHistory;

public class Zertifikat {
	
	private String serviceObject; 
	
	private String contractNumber; 
	
	private String intervall; 
	
	private String responsiblePerson; 
	
	private String elementList; 
	
	private List<ServiceHistory> histElements;
	
	private List<SubZertifikat> subZertifkat; 
	
	private Date endDate;


	public String getServiceObject() {
		return serviceObject;
	}

	public void setServiceObject(String serviceObject) {
		this.serviceObject = serviceObject;
	}

	public String getContractNumber() {
		return contractNumber;
	}

	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}

	public String getIntervall() {
		return intervall;
	}

	public void setIntervall(String intervall) {
		this.intervall = intervall;
	}

	public String getResponsiblePerson() {
		return responsiblePerson;
	}

	public void setResponsiblePerson(String responsiblePerson) {
		this.responsiblePerson = responsiblePerson;
	}

	public String getElementList() {
		return elementList;
	}

	public void setElementList(String elementList) {
		this.elementList = elementList;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<ServiceHistory> getHistElements() {
		return histElements;
	}

	public void setHistElements(List<ServiceHistory> histElements) {
		this.histElements = histElements;
	}

	public List<SubZertifikat> getSubZertifkat() {
		return subZertifkat;
	}

	public void setSubZertifkat(List<SubZertifikat> subZertifkat) {
		this.subZertifkat = subZertifkat;
	}
	

}
