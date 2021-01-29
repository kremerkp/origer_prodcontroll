package lu.origer.serviceagree.models.contract;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import lu.origer.serviceagree.models.contact.Person;

/**
 *
 * @author kai.kremer
 *
 */
@Entity
@Table(name = "service_contract")
@XmlRootElement
public class ServiceContract implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1957741159340716533L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer           id;
    

	@Basic(optional = false)
	@NotNull
	@Column(name = "active")
	private boolean active;
	
	@Column(name = "shn", unique = true)
	@NotNull
	private String serviceContractNumber;
	
	@Column(name = "description")
	private String description;

	@Basic(optional = false)
	@NotNull
	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "fk_create_user")
	private Integer createUser;
	
	@Column(name = "edit_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date editDate;
	
	@Column(name = "first_intervall_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date firstIntervallDate;
	
	@Column(name = "fk_edit_user")
	private Integer editUser;
	
	
	@JoinColumn(name = "fk_building_site", referencedColumnName = "id" )
	@ManyToOne(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
	private BuildingSite buildingSite;
	
	@JoinColumn(name = "fk_technician", referencedColumnName = "id" )
	@ManyToOne(cascade = { CascadeType.ALL}, fetch = FetchType.EAGER)
	private Person technician;
	
	@JoinColumn(name = "fk_partner", referencedColumnName = "id" )
	@ManyToOne(cascade = { CascadeType.ALL}, fetch = FetchType.EAGER)
	private Person partner;
	
	@JoinColumn(name = "fk_service_type", referencedColumnName = "id" )
	@ManyToOne(cascade = { CascadeType.ALL}, fetch = FetchType.EAGER)
	private ServiceType serviceType;
	
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceContract", fetch = FetchType.LAZY)
    private List<Service> services;
    
	@NotNull
	@Column(name = "from_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fromdate;
	
	@NotNull
	@Column(name = "to_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date todate;
	
	@Column(name= "service_amount")
	private BigDecimal servicesAmount; 
	
	@Column(name = "time_planned" )
	private Integer timePlannedTotal; 
	
	@Column(name = "time_spent")
	private Integer timeSpentTotal;
	
	@Column(name = "intervall")
	private Integer intervall;
	
	@Column(name = "finalized")
	private Boolean finalized;
	
	@JoinColumn(name = "fk_previous_contract", referencedColumnName = "id" )
	@ManyToOne(fetch = FetchType.LAZY)
	private ServiceContract previousContract;
	
	@JoinColumn(name = "fk_contract_type", referencedColumnName = "id" )
	@ManyToOne(fetch = FetchType.EAGER)
	private ContractType contractType;
	


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public Integer getCreateUser() {
		return createUser;
	}

	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	public Date getEditDate() {
		return editDate;
	}

	public void setEditDate(Date editDate) {
		this.editDate = editDate;
	}

	public Integer getEditUser() {
		return editUser;
	}

	public void setEditUser(Integer editUser) {
		this.editUser = editUser;
	}

	public BuildingSite getBuildingSite() {
		return buildingSite;
	}

	public void setBuildingSite(BuildingSite buildingSite) {
		this.buildingSite = buildingSite;
	}

	public Person getTechnician() {
		return technician;
	}

	public void setTechnician(Person technician) {
		this.technician = technician;
	}

	public Person getPartner() {
		return partner;
	}

	public void setPartner(Person partner) {
		this.partner = partner;
	}

	public Date getFromdate() {
		return fromdate;
	}

	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}

	public Date getTodate() {
		return todate;
	}

	public void setTodate(Date todate) {
		this.todate = todate;
	}

	public BigDecimal getServicesAmount() {
		return servicesAmount;
	}

	public void setServicesAmount(BigDecimal servicesAmount) {
		this.servicesAmount = servicesAmount;
	}
	
	public Integer getTimePlannedTotal() {
		return timePlannedTotal;
	}

	public void setTimePlannedTotal(Integer timePlannedTotal) {
		this.timePlannedTotal = timePlannedTotal;
	}

	public Integer getTimeSpentTotal() {
		return timeSpentTotal;
	}

	public void setTimeSpentTotal(Integer timeSpentTotal) {
		this.timeSpentTotal = timeSpentTotal;
	}

	public Integer getIntervall() {
		return intervall;
	}

	public void setIntervall(Integer intervall) {
		this.intervall = intervall;
	}

	public Date getFirstIntervallDate() {
		return firstIntervallDate;
	}

	public void setFirstIntervallDate(Date firstIntervallDate) {
		this.firstIntervallDate = firstIntervallDate;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public List<Service> getServices() {
		return services;
	}

	public void setServices(List<Service> services) {
		this.services = services;
	}
	
    public Boolean getFinalized() {
		return finalized;
	}

	public void setFinalized(Boolean finalized) {
		this.finalized = finalized;
	}

	public ServiceContract getPreviousContract()
	{
		return previousContract;
	}

	public void setPreviousContract(ServiceContract previousContract)
	{
		this.previousContract = previousContract;
	}

	@Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ServiceContract)) {
            return false;
        }
        ServiceContract other = (ServiceContract) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format(id.toString());
    }
   

	public ContractType getContractType() {
		return contractType;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}
    

}

