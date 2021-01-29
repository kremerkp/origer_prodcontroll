/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lu.origer.serviceagree.models.main;

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
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import lu.origer.serviceagree.models.contract.ServiceContract;

/**
 *
 * @author fred.freres
 */
@Entity
@Table(name = "users")
@XmlRootElement
public class Users implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "username")
    private String username;
    @Basic
    @Size(min = 0, max = 32)
    @Column(name = "password")
    private String password;
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 255)
    @Column(name = "prename")
    private String prename;
    @Size(max = 255)
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
    @Column(name = "created_by")
    private int createdBy;
    
    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;
    
    @Column(name = "updated_by")
    private Integer updatedBy;
    
    @Column(name = "language")
    private String lang;
    
    @JoinTable(name = "assoc_users_servicecontracts", joinColumns = {
            @JoinColumn(name = "fk_user", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "fk_servicecontract", referencedColumnName = "id")})
        @ManyToMany( fetch = FetchType.EAGER)
        private List<ServiceContract> serviceContractList;
    
    @JoinTable(name = "assoc_users_roles", joinColumns = {
            @JoinColumn(name = "fk_users", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "fk_roles", referencedColumnName = "id")})
        @ManyToMany(cascade = { CascadeType.MERGE}, fetch = FetchType.EAGER)
        private List<Roles> rolesList;
    
    @Transient
    private String fullName;
    

    public Users() {
    }

    public Users(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return name + " " + prename;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Users(Integer id, String username, String password, boolean active, Date createDate, int createdBy) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.active = active;
        this.createDate = createDate;
        this.createdBy = createdBy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrename() {
        return prename;
    }

    public void setPrename(String prename) {
        this.prename = prename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getActive() {
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

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Integer updatedBy) {
        this.updatedBy = updatedBy;
    }

    @XmlTransient
    public List<Roles> getRolesList() {
        return rolesList;
    }

    public void setRolesList(List<Roles> rolesList) {
        this.rolesList = rolesList;
    }
    
    @XmlTransient
	public List<ServiceContract> getServiceContractList() {
		return serviceContractList;
	}

	public void setServiceContractList(List<ServiceContract> serviceContractList) {
		this.serviceContractList = serviceContractList;
	}

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "lu.cimw.models.Users[ id=" + id + " ]";
    }

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}
    
    

}
