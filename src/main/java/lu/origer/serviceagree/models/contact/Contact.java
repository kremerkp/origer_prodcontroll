package lu.origer.serviceagree.models.contact;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kai.kremer
 *
 */
@Entity
@Table(name = "contact")
@XmlRootElement
public class Contact implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4316499811882046281L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer           id;

	@Column(name = "contact_field")
	private String contactField;
	
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
	
	@JoinColumn (name="fk_contact_typ", referencedColumnName = "id")
	@ManyToOne
	private ContactType contactType; 
	
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

	public String getContactField() {
		return contactField;
	}

	public void setContactField(String contactField) {
		this.contactField = contactField;
	}

	public ContactType getContactType() {
		return contactType;
	}

	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}
	
}
