package lu.origer.serviceagree.models.contract;

import java.io.Serializable;

import lu.origer.serviceagree.models.checklist.ServiceElements;

public class OpenOfferElements implements Serializable {
	
	private String buildingSiteNr; 
	
	private String buildingSiteName; 
	
	private String serviceContractNumber;
	
	private String serviceContractDescription;
	
	private String serviceContractType; 
	
	private String serviceElement; 
	
	private String serviceElementZimmer; 
	
	private String serviceElementStockwerk; 
	
	private String serviceElementAusrichtung;
	
	private String serviceElementDIN;
	
	private String containInOffer; 
	
	private String containOfferState; 
	
	private Integer serviceContractId; 
	
	private String elementState;
	
	private Offers offer; 
	
	private ServiceElements serviceElementObject; 
	
	private ServiceContract serviceContract; 

	public String getBuildingSiteNr() {
		return buildingSiteNr;
	}

	public void setBuildingSiteNr(String buildingSiteNr) {
		this.buildingSiteNr = buildingSiteNr;
	}

	public String getBuildingSiteName() {
		return buildingSiteName;
	}

	public void setBuildingSiteName(String buildingSiteName) {
		this.buildingSiteName = buildingSiteName;
	}

	public String getServiceContractNumber() {
		return serviceContractNumber;
	}

	public void setServiceContractNumber(String serviceContractNumber) {
		this.serviceContractNumber = serviceContractNumber;
	}

	public String getServiceContractType() {
		return serviceContractType;
	}

	public void setServiceContractType(String serviceContractType) {
		this.serviceContractType = serviceContractType;
	}

	public String getServiceElement() {
		return serviceElement;
	}

	public void setServiceElement(String serviceElement) {
		this.serviceElement = serviceElement;
	}

	public String getContainInOffer() {
		return containInOffer;
	}

	public void setContainInOffer(String containInOffer) {
		this.containInOffer = containInOffer;
	}

	public String getContainOfferState() {
		return containOfferState;
	}

	public void setContainOfferState(String containOfferState) {
		this.containOfferState = containOfferState;
	}

	public String getElementState() {
		return elementState;
	}

	public void setElementState(String elementState) {
		this.elementState = elementState;
	}

	public Offers getOffer() {
		return offer;
	}

	public void setOffer(Offers offer) {
		this.offer = offer;
	}

	public ServiceElements getServiceElementObject() {
		return serviceElementObject;
	}

	public void setServiceElementObject(ServiceElements serviceElementObject) {
		this.serviceElementObject = serviceElementObject;
	}

	public ServiceContract getServiceContract() {
		return serviceContract;
	}

	public void setServiceContract(ServiceContract serviceContract) {
		this.serviceContract = serviceContract;
	}

	public String getServiceContractDescription() {
		return serviceContractDescription;
	}

	public void setServiceContractDescription(String serviceContractDescription) {
		this.serviceContractDescription = serviceContractDescription;
	}

	public String getServiceElementZimmer() {
		return serviceElementZimmer;
	}

	public void setServiceElementZimmer(String serviceElementZimmer) {
		this.serviceElementZimmer = serviceElementZimmer;
	}

	public String getServiceElementStockwerk() {
		return serviceElementStockwerk;
	}

	public void setServiceElementStockwerk(String serviceElementStockwerk) {
		this.serviceElementStockwerk = serviceElementStockwerk;
	}

	public String getServiceElementAusrichtung() {
		return serviceElementAusrichtung;
	}

	public void setServiceElementAusrichtung(String serviceElementAusrichtung) {
		this.serviceElementAusrichtung = serviceElementAusrichtung;
	}

	public String getServiceElementDIN() {
		return serviceElementDIN;
	}

	public void setServiceElementDIN(String serviceElementDIN) {
		this.serviceElementDIN = serviceElementDIN;
	}

	public Integer getServiceContractId() {
		return serviceContractId;
	}

	public void setServiceContractId(Integer serviceContractId) {
		this.serviceContractId = serviceContractId;
	}
	
	

}
