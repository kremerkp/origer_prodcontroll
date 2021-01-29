package lu.origer.serviceagree.models.contact;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "country")
@XmlRootElement
public class Country implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 2081317238542156013L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer           id;
    
	@Size(max = 255)
	@Column(name = "name")
	private String name;
	
	@Size(max = 255)
	@Column(name = "name_de")
	private String nameDE;
	
	@Size(max = 255)
	@Column(name = "name_fr")
	private String nameFR;
	
	@Size(max = 128)
	@Column(name = "shn")
	private String shn;
	
	@Size(max = 4000)
	@Column(name = "description")
	private String description;
	
	@Size(max = 2)
	@Column(name = "alpha2")
	@NotNull
	private String alpha2;
	
	@Size(max = 3)
	@Column(name = "alpha3")
	@NotNull
	private String alpha3;
    
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

	public String getNameDE() {
		return nameDE;
	}

	public void setNameDE(String nameDE) {
		this.nameDE = nameDE;
	}

	public String getNameFR() {
		return nameFR;
	}

	public void setNameFR(String nameFR) {
		this.nameFR = nameFR;
	}

	public String getShn() {
		return shn;
	}

	public void setShn(String shn) {
		this.shn = shn;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getAlpha2() {
		return alpha2;
	}

	public void setAlpha2(String alpha2) {
		this.alpha2 = alpha2;
	}

	public String getAlpha3() {
		return alpha3;
	}

	public void setAlpha3(String alpha3) {
		this.alpha3 = alpha3;
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
	
	
	
    @Override
    public String toString() {
        return String.format(alpha2 + " - " +  name);
    }
}

