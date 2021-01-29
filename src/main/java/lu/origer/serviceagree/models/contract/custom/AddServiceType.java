package lu.origer.serviceagree.models.contract.custom;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import lu.origer.serviceagree.models.contact.PersonType;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.contract.ServiceType;

@Entity
@Table(name = "add_service_types")
@XmlRootElement
public class AddServiceType  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id; 
	
	@Column(name = "intervall")
	private Integer intervall;
	
	@Column(name = "latest_service_date")
	private Date latestServiceDate; 
	 
	@JoinColumn(name = "fk_service_contract", referencedColumnName = "id")
	@ManyToOne
	private ServiceContract  serviceContract;
	
	@JoinColumn(name = "fk_service_type", referencedColumnName = "id")
	@ManyToOne
	private ServiceType serviceType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIntervall() {
		return intervall;
	}

	public void setIntervall(Integer intervall) {
		this.intervall = intervall;
	}

	public Date getLatestServiceDate() {
		return latestServiceDate;
	}

	public void setLatestServiceDate(Date latestServiceDate) {
		this.latestServiceDate = latestServiceDate;
	}

	public ServiceContract getServiceContract() {
		return serviceContract;
	}

	public void setServiceContract(ServiceContract serviceContract) {
		this.serviceContract = serviceContract;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}	


}
