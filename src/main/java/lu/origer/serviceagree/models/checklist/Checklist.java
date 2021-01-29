package lu.origer.serviceagree.models.checklist;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import lu.origer.serviceagree.models.assocs.Assoc_Checklist_Checklistentries;
import lu.origer.serviceagree.models.contract.ServiceType;
import lu.origer.serviceagree.models.main.Roles;

/**
 *
 * @author kai.kremer
 *
 */
@Entity
@Table(name = "checklist")
@XmlRootElement
public class Checklist implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7357948942871694684L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer           id;
    

	@Basic(optional = false)
	@NotNull
	@Column(name = "active")
	private boolean active;
	
	@Column(name = "name")
	private String name;
	
	@JoinColumn(name = "fk_service_type", referencedColumnName = "id")
	@ManyToOne
	private ServiceType serviceType;
	
//    @JoinTable(name = "assoc_checklist_checklistitems", joinColumns = {
//            @JoinColumn(name = "fk_checklist", referencedColumnName = "id")}, inverseJoinColumns = {
//            @JoinColumn(name = "fk_checklist_entry", referencedColumnName = "id")})
//        @ManyToMany(fetch = FetchType.EAGER)
//    private List<ChecklistItem> checklistItems;

	@Basic(optional = false)
	@NotNull
	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "fk_create_user")
	private int createUser;
	
	@OneToMany(fetch = FetchType.LAZY,  cascade = CascadeType.DETACH, mappedBy = "checkList", orphanRemoval = true)
	private List<Assoc_Checklist_Checklistentries> assocChecklistEntries;
	
	@Column(name = "edit_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date editDate;
	
	@Column(name = "fk_edit_user")
	private Integer editUser;
	

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
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

//	public List<ChecklistItem> getChecklistItems() {
//		return checklistItems;
//	}
//
//	public void setChecklistItems(List<ChecklistItem> checklistItems) {
//		this.checklistItems = checklistItems;
//	}
	
    @Override
    public String toString() {
        return String.format(name);
    }

	public List<Assoc_Checklist_Checklistentries> getAssocChecklistEntries() {
		return assocChecklistEntries;
	}

	public void setAssocChecklistEntries(List<Assoc_Checklist_Checklistentries> assocChecklistEntries) {
		this.assocChecklistEntries = assocChecklistEntries;
	}

	
	
}
