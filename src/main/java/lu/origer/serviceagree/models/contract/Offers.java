package lu.origer.serviceagree.models.contract;

import java.io.Serializable;
import java.math.BigDecimal;
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

import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.main.FileArchive;

/**
 *
 * @author kai.kremer
 *
 */
@Entity
@Table(name = "offers")
@XmlRootElement
public class Offers implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -5968302475482521481L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer           id;
    
    
	@Column(name = "name")
	private String name;
	
	@Column(name = "shn")
	private String shortname;
	
	@NotNull
	@Column(name = "offerdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date offerdate;
	
	@Column(name = "amount")
	private BigDecimal amount;
	
	@Column(name = "checklistitems")
	private String checklistitems;
	
	@Column(name = "state")
	private String stateDB;
	
	@Transient
	private String state;

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
	
	@Transient
	private String elementsInOffer; 
	
	@NotNull
	@JoinColumn(name = "fk_servicecontract", referencedColumnName = "id")
	@ManyToOne
	private ServiceContract serviceContract;
	
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "offer", fetch = FetchType.EAGER)
    private List<FileArchive> files;
    
    @JoinTable(name = "assoc_offers_elements", joinColumns = {
            @JoinColumn(name = "fk_offer", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "fk_element", referencedColumnName = "id")})
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH , CascadeType.DETACH }, fetch = FetchType.EAGER)
    //@ManyToMany
    private List<ServiceElements> serviceElementList;

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

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public Date getOfferdate() {
		return offerdate;
	}

	public void setOfferdate(Date offerdate) {
		this.offerdate = offerdate;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
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

	public String getChecklistitems() {
		return checklistitems;
	}

	public void setChecklistitems(String checklistitems) {
		this.checklistitems = checklistitems;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public ServiceContract getServiceContract() {
		return serviceContract;
	}

	public void setServiceContract(ServiceContract serviceContract) {
		this.serviceContract = serviceContract;
	}

	public List<FileArchive> getFiles() {
		return files;
	}

	public void setFiles(List<FileArchive> files) {
		this.files = files;
	}

	public List<ServiceElements> getServiceElementList() {
		return serviceElementList;
	}

	public void setServiceElementList(List<ServiceElements> serviceElementList) {
		this.serviceElementList = serviceElementList;
	}

	public String getElementsInOffer() {
		return elementsInOffer;
	}

	public void setElementsInOffer(String elementsInOffer) {
		this.elementsInOffer = elementsInOffer;
	}

	public String getStateDB() {
		return stateDB;
	}

	public void setStateDB(String stateDB) {
		this.stateDB = stateDB;
	}
	
	
}



