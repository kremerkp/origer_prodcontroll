package lu.origer.serviceagree.backend.soap;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author ian.husting
 *
 * RPC-confirm SOAP object for services
 */
@XmlRootElement
public class ServiceSO {
	private Integer id;	
	private String name;
	private String shortName;
	private Date latestServiceDate;
	private Boolean active;
	private Date startDate;
	private Date endDate;
	private ServiceContractSO contract;
	private ServiceTypeSO serviceType;
	private Integer interval;
	private String description;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public Date getLatestServiceDate() {
		return latestServiceDate;
	}
	public void setLatestServiceDate(Date latestServiceDate) {
		this.latestServiceDate = latestServiceDate;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public ServiceContractSO getContract() {
		return contract;
	}
	public void setContract(ServiceContractSO contract) {
		this.contract = contract;
	}
	public ServiceTypeSO getServiceType() {
		return serviceType;
	}
	public void setServiceType(ServiceTypeSO serviceType) {
		this.serviceType = serviceType;
	}
	public Integer getInterval() {
		return interval;
	}
	public void setInterval(Integer interval) {
		this.interval = interval;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
