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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kai.kremer
 *
 */
@Entity
@Table(name = "address")
@XmlRootElement
public class Address implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -7813972860380585144L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer           id;
    

	@Basic(optional = false)
	@NotNull
	@Column(name = "active")
	private boolean active;
	
	@Size(max = 255)
	@Column(name = "city")
	private String city;
	
	@Size(max = 100)
	@Column(name = "zip")
	private String zip;
	
	@Size(max = 255)
	@Column(name = "street")
	private String street;
	
	@Size(max = 50)
	@Column(name = "street_nr")
	private String streetNumber;
	
    @ManyToOne
    @JoinColumn(name = "fk_country")
    private Country country;
	
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
	private Integer deleteUser;
	

	public Address() {
		super();
	}

	public Address(String city, String zip, String street, String streetNumber, Country country) {
		super();
		this.city = city;
		this.zip = zip;
		this.street = street;
		this.streetNumber = streetNumber;
		this.country = country;
	}

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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
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
	
}


