package lu.origer.serviceagree.backend.soap;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

public class ServiceContractSO {
	private Integer id;
	private Boolean active;
	private String serviceContractNumber;
	private String description;
	private Date fromDate;
	private Date toDate;
	private BuildingSiteSO buildingSite;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getServiceContractNumber() {
		return serviceContractNumber;
	}
	public void setServiceContractNumber(String serviceContractNumber) {
		this.serviceContractNumber = serviceContractNumber;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public BuildingSiteSO getBuildingSite() {
		return buildingSite;
	}
	public void setBuildingSite(BuildingSiteSO buildingSite) {
		this.buildingSite = buildingSite;
	}
}
