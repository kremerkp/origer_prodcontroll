package lu.origer.serviceagree.models.contract;

import java.io.Serializable;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import org.primefaces.model.chart.HorizontalBarChartModel;

import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contact.Person;

/**
 *
 * @author kai.kremer
 *
 */
@Entity
@Table(name = "service")
@XmlRootElement
public class Service implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 758802689629790005L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer           id;
    
	@Basic(optional = false)
	@NotNull
	@Column(name = "active")
	private boolean active;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "ready")
	private boolean ready;
	
	@Column(name = "intervall")
	private Integer intervall;
	
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
	private int createUser;
	
	@Column(name = "edit_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date editDate;
	
	@Column(name = "fk_edit_user")
	private Integer editUser;
	
	@Column(name = "delete_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date deleteDate;
	
	@Column(name = "startdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startdate;
	
	@Column(name = "enddate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date enddate;
	
	@Column(name = "fk_delete_user")
	private Integer deleteUser;
	
	@Column(name = "checklistItemsCreated")
	private Boolean checklistItemsCreated;
	
	
	@JoinColumn(name = "fk_service_contract", referencedColumnName = "id")
	@ManyToOne(cascade = { CascadeType.MERGE, 
			CascadeType.PERSIST,
			CascadeType.MERGE,
			CascadeType.REFRESH}, fetch = FetchType.LAZY)
	private ServiceContract serviceContract;

	@JoinColumn(name = "fk_service_type", referencedColumnName = "id")
	@ManyToOne(cascade = { CascadeType.MERGE, 
			CascadeType.PERSIST,
			CascadeType.MERGE,
			CascadeType.REFRESH}, fetch = FetchType.EAGER)
	private ServiceType serviceType;
	
	@JoinColumn(name = "fk_customer", referencedColumnName = "id")
	@ManyToOne(cascade = { CascadeType.MERGE, 
			CascadeType.PERSIST,
			CascadeType.MERGE,
			CascadeType.REFRESH})
	private Person customer;
	
	@JoinColumn(name = "fk_technician", referencedColumnName = "id")
	@ManyToOne(cascade = { CascadeType.MERGE, 
		CascadeType.PERSIST,
		CascadeType.MERGE,
		CascadeType.REFRESH})
	private Person technician;
	
	@JoinColumn(name = "fk_partner", referencedColumnName = "id")
	@ManyToOne(cascade = { CascadeType.MERGE, 
			CascadeType.PERSIST,
			CascadeType.MERGE,
			CascadeType.REFRESH})
	private Person partner;
	
	@Column(name = "name")
	private String comment;
	
	@Column(name = "shn")
	private String shortname;
	
	@Column(name = "target_amount")
	private Float targetAmount;
	
	@Column(name = "actual_amount")
	private String actualAmount;
	
	@Column(name = "service_value")
	private Double serviceAmount;

	@Column(name = "latest_service_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date latestServiceDate;
	
    @JoinTable(name = "assoc_service_elements", joinColumns = {
            @JoinColumn(name = "fk_service", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "fk_elements", referencedColumnName = "id")})
    @ManyToMany(cascade = {CascadeType.REFRESH }, fetch = FetchType.LAZY)
    //@ManyToMany
    private List<ServiceElements> serviceElementList;
    
    @Transient
    public Integer countPerType; 
    
    @Transient
    public Integer totalElements;
    
    @Transient
    public Integer elememtsChecked;
    
    @Transient
    public String acctualTime;
    
    
    @Transient
    public Float percentReady;
     
    @Transient
    private HorizontalBarChartModel horizontalBarModel;
   

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

	public int getCreateUser() {
		return createUser;
	}

	public void setCreateUser(int createUser) {
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

	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	public Integer getDeleteUser() {
		return deleteUser;
	}

	public void setDeleteUser(Integer deleteUser) {
		this.deleteUser = deleteUser;
	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public Person getCustomer() {
		return customer;
	}

	public void setCustomer(Person customer) {
		this.customer = customer;
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

	

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public Date getLatestServiceDate() {
		return latestServiceDate;
	}

	public void setLatestServiceDate(Date latestServiceDate) {
		this.latestServiceDate = latestServiceDate;
	}

	public Date getStartdate() {
		return startdate;
	}

	public void setStartdate(Date startdate) {
		this.startdate = startdate;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Integer getIntervall() {
		return intervall;
	}

	public void setIntervall(Integer intervall) {
		this.intervall = intervall;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ServiceContract getServiceContract() {
		return serviceContract;
	}

	public void setServiceContract(ServiceContract serviceContract) {
		this.serviceContract = serviceContract;
	}

	public List<ServiceElements> getServiceElementList() {
		return serviceElementList;
	}

	public void setServiceElementList(List<ServiceElements> serviceElementList) {
		this.serviceElementList = serviceElementList;
	}

	public Float getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(Float targetAmount) {
		this.targetAmount = targetAmount;
	}

	public String getActualAmount() {
		return actualAmount;
	}

	public void setActualAmount(String actualAmount) {
		this.actualAmount = actualAmount;
	}

	public Integer getCountPerType() {
		return countPerType;
	}

	public void setCountPerType(Integer countPerType) {
		this.countPerType = countPerType;
	}

	public Boolean getChecklistItemsCreated() {
		return checklistItemsCreated;
	}

	public void setChecklistItemsCreated(Boolean checklistItemsCreated) {
		this.checklistItemsCreated = checklistItemsCreated;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Service other = (Service) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (comment == null) {
			if (other.comment != null)
				return false;
		} else if (!comment.equals(other.comment))
			return false;
		return true;
	}

	public Integer getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(Integer totalElements) {
		this.totalElements = totalElements;
	}

	public Integer getElememtsChecked() {
		return elememtsChecked;
	}

	public void setElememtsChecked(Integer elememtsChecked) {
		this.elememtsChecked = elememtsChecked;
	}

	public Float getPercentReady() {
		return percentReady;
	}

	public void setPercentReady(Float percentReady) {
		this.percentReady = percentReady;
	}

	public HorizontalBarChartModel getHorizontalBarModel() {
		return horizontalBarModel;
	}

	public void setHorizontalBarModel(HorizontalBarChartModel horizontalBarModel) {
		this.horizontalBarModel = horizontalBarModel;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public Double getServiceAmount() {
		return serviceAmount;
	}

	public void setServiceAmount(Double serviceAmount) {
		this.serviceAmount = serviceAmount;
	}

	public String getAcctualTime() {
		return acctualTime;
	}

	public void setAcctualTime(String acctualTime) {
		this.acctualTime = acctualTime;
	}
	
	
	
	


}
