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
import lu.origer.serviceagree.models.contract.Offers;

/**
 *
 * @author kai.kremer
 *
 */
@Entity
@Table(name = "checklist_items")
@XmlRootElement
public class ChecklistItem implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Basic(optional = false)
	@NotNull
	@Column(name = "active")
	private boolean active;

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

	@Transient
	@GeneratedValue
	private Boolean functionalControl;

	@Transient
	@GeneratedValue
	private Boolean viewControl;

	@Column(name = "fk_edit_user")
	private Integer editUser;

	@JoinColumn(name = "fk_category", referencedColumnName = "id")
	@ManyToOne
	private ChecklistItemCategory checkListItemCategory;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "checklistItem", fetch = FetchType.EAGER, orphanRemoval = true)
	private List<Assoc_Checklist_Checklistentries> assocChecklistEntries;

	@JoinTable(name = "assoc_checklist_checklistitems", joinColumns = {
			@JoinColumn(name = "fk_checklist", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "fk_checklist_entry", referencedColumnName = "id") })
	@ManyToMany(fetch = FetchType.EAGER)
	private List<Checklist> checkLists;

	@Column(name = "name_fr")
	private String nameFrench;

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

	public ChecklistItemCategory getCheckListItemCategory() {
		return checkListItemCategory;
	}

	public void setCheckListItemCategory(ChecklistItemCategory checkListItemCategory) {
		this.checkListItemCategory = checkListItemCategory;
	}

	public String getNameFrench() {
		return nameFrench;
	}

	public void setNameFrench(String nameFrench) {
		this.nameFrench = nameFrench;
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof ChecklistItem) && (id != null) ? id.equals(((ChecklistItem) other).id)
				: (other == this);
	}

	@Override
	public int hashCode() {
		return (id != null) ? (this.getClass().hashCode() + id.hashCode()) : super.hashCode();
	}

	@Override
	public String toString() {
		return String.format("ExampleEntity[%d, %s]", id, name);
	}

	public ChecklistItem(final Integer id) {
		this.id = id;
	}

	public ChecklistItem() {
	}

	public List<Checklist> getCheckLists() {
		return checkLists;
	}

	public void setCheckLists(List<Checklist> checkLists) {
		this.checkLists = checkLists;
	}

	public Boolean getFunctionalControl() {
		return functionalControl;
	}

	public void setFunctionalControl(Boolean functionalControl) {
		this.functionalControl = functionalControl;
	}

	public Boolean getViewControl() {
		return viewControl;
	}

	public void setViewControl(Boolean viewControl) {
		this.viewControl = viewControl;
	}

	public List<Assoc_Checklist_Checklistentries> getAssocChecklistEntries() {
		return assocChecklistEntries;
	}

	public void setAssocChecklistEntries(List<Assoc_Checklist_Checklistentries> assocChecklistEntries) {
		this.assocChecklistEntries = assocChecklistEntries;
	}
	

}
