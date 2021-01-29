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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import lu.origer.serviceagree.models.contact.Address;
import lu.origer.serviceagree.models.contact.Contact;

/**
 *
 * @author kai.kremer
 *
 */
@Entity
@Table(name = "building_site")
@XmlRootElement
public class BuildingSite implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3910340454057733729L;

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Size(max = 255)
	@Column(name = "code")
	private String code;

	@Size(max = 255)
	@Column(name = "name")
	private String name;

	@Size(max = 32)
	@Column(name = "shn")
	private String shortname;

	@Size(max = 4000)
	@Column(name = "description")
	private String description;

	@JoinColumn(name = "fk_address", referencedColumnName = "id")
	@ManyToOne(cascade = { CascadeType.ALL}, fetch = FetchType.EAGER)
	private Address address;

	@JoinTable(name = "assoc_building_site_contact", joinColumns = {
			@JoinColumn(name = "fk_building_site", referencedColumnName = "id") }, inverseJoinColumns = {
					@JoinColumn(name = "fk_contact", referencedColumnName = "id") })
	@ManyToMany(cascade = { CascadeType.ALL}, fetch = FetchType.EAGER)
	private List<Contact> contactList;

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

	@Column(name = "fk_edit_user")
	private Integer editUser;

	@Column(name = "delete_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date deleteDate;

	@Column(name = "fk_delete_user")
	private Integer delteUser;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Integer getDelteUser() {
		return delteUser;
	}

	public void setDelteUser(Integer delteUser) {
		this.delteUser = delteUser;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<Contact> getContactList() {
		return contactList;
	}

	public void setContactList(List<Contact> contactList) {
		this.contactList = contactList;
	}

	@Override
	public String toString() {
		return "BuildingSite [id=" + id + "]";
	}

	
}

