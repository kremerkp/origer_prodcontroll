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
public class ServiceListView implements Serializable {

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
	
	
	@Column(name = "fk_service_contract")
	private Integer serviceContractId;

	@Column(name = "fk_service_type")
	private Integer serviceTypeId;
	
	@Column(name = "fk_customer")
	private Integer customerId;
	
	@Column(name = "fk_technician")
	private Integer technicianId;
	
	@Column(name = "fk_partner")
	private Integer partnerId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "shn")
	private String shortname;
	
	@Column(name = "target_amount")
	private Float targetAmount;
	
	@Column(name = "actual_amount")
	private String actualAmount;

	@Column(name = "latest_service_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date latestServiceDate;
	
    @Transient
    public Integer countPerType;

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

	public Integer getDeleteUser() {
		return deleteUser;
	}

	public void setDeleteUser(Integer deleteUser) {
		this.deleteUser = deleteUser;
	}

	public Boolean getChecklistItemsCreated() {
		return checklistItemsCreated;
	}

	public void setChecklistItemsCreated(Boolean checklistItemsCreated) {
		this.checklistItemsCreated = checklistItemsCreated;
	}

	public Integer getServiceContractId() {
		return serviceContractId;
	}

	public void setServiceContractId(Integer serviceContractId) {
		this.serviceContractId = serviceContractId;
	}

	public Integer getServiceTypeId() {
		return serviceTypeId;
	}

	public void setServiceTypeId(Integer serviceTypeId) {
		this.serviceTypeId = serviceTypeId;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getTechnicianId() {
		return technicianId;
	}

	public void setTechnicianId(Integer technicianId) {
		this.technicianId = technicianId;
	}

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
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

	public Date getLatestServiceDate() {
		return latestServiceDate;
	}

	public void setLatestServiceDate(Date latestServiceDate) {
		this.latestServiceDate = latestServiceDate;
	}

	public Integer getCountPerType() {
		return countPerType;
	}

	public void setCountPerType(Integer countPerType) {
		this.countPerType = countPerType;
	} 

    

}
