package lu.origer.serviceagree.backend.soap;

import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import lu.origer.serviceagree.models.contact.Address;
import lu.origer.serviceagree.models.contact.Contact;

/**
 * 
 * @author ian.husting
 *
 * RPC-confirm SOAP object for building sites
 */
@XmlRootElement
public class BuildingSiteSO {

	private Long id;
	
	private String code;
	
	private String name;
	
	private String shortname;
	
	private String description;
	
	private Address address;
	
	private Contact[] contactList;
	
	private boolean active;
	
	private Date createDate;
	
	private int createUser;
	
	private Date editDate;
	
	private Long editUser;
	
	private Date deleteDate;
	
	private Long deleteUser;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Contact[] getContactList() {
		return contactList;
	}

	public void setContactList(Contact[] contactList) {
		this.contactList = contactList;
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

	public Long getEditUser() {
		return editUser;
	}

	public void setEditUser(Long editUser) {
		this.editUser = editUser;
	}

	public Date getDeleteDate() {
		return deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

	public Long getDeleteUser() {
		return deleteUser;
	}

	public void setDeleteUser(Long deleteUser) {
		this.deleteUser = deleteUser;
	}
}
