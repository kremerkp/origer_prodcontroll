package lu.origer.serviceagree.models.assocs;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import lu.origer.serviceagree.models.main.Roles;
import lu.origer.serviceagree.models.main.Users;

@Entity
@Table(name = "assoc_users_roles")
@XmlRootElement
public class Assoc_Users_Roles implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3753831141523506595L;
	@Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer           id;
	
	@Column(name="fk_users")
	private Integer user;
	
	@Column(name="fk_roles")
	private Integer roles;

	public Integer getId() {
		return id;
	}

	public Integer getUser() {
		return user;
	}

	public void setUser(Integer user) {
		this.user = user;
	}

	public Integer getRoles() {
		return roles;
	}

	public void setRoles(Integer roles) {
		this.roles = roles;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	


}
