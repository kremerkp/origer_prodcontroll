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

import lu.origer.serviceagree.models.contact.types.DinType;
import lu.origer.serviceagree.models.contact.types.Direction;
import lu.origer.serviceagree.models.contact.types.ElementType;
import lu.origer.serviceagree.models.contact.types.Front;
import lu.origer.serviceagree.models.contract.Offers;
import lu.origer.serviceagree.models.main.ObjectComments;
import lu.origer.serviceagree.models.synch.ServiceHistory;

/**
 *
 * @author kai.kremer
 *
 */
/**
 * @author kai.kremer
 *
 */
@Entity
@Table(name = "service_elements")
@XmlRootElement
public class ServiceElements implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2278196845905701181L;
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer           id;
    
	@Basic(optional = false)
	@NotNull
	@Column(name = "active")
	private boolean active;
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "createOffer")
	private boolean createOffer;
	
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
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "floor")
	private String floor;
	
	@Column(name = "orientation")
	private String orientation;
	
	@Column(name = "room")
	private String room;

	@Column(name = "front")
	private String front;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "elementnumber")
	private String elementnumber;
	
	@Column(name = "print_new_barcode")
	private Boolean printNewBarcode;
	
	@JoinColumn(name = "fk_element_type", referencedColumnName = "id")
	@ManyToOne(cascade = { CascadeType.MERGE}, fetch = FetchType.EAGER)
	private ElementType elementType;
	
	@JoinColumn(name = "fk_front_type", referencedColumnName = "id")
	@ManyToOne(cascade = { CascadeType.MERGE}, fetch = FetchType.EAGER)
	private Front frontType;
	
	@JoinColumn(name = "fk_orientation_type", referencedColumnName = "id")
	@ManyToOne(cascade = { CascadeType.MERGE}, fetch = FetchType.EAGER)
	private Direction directionType;
	
	@JoinColumn(name = "fk_checklist", referencedColumnName = "id")
	@ManyToOne
	private Checklist checklist;
	
	@JoinColumn(name = "fk_din_type", referencedColumnName = "id")
	@ManyToOne
	private DinType dinType;
	
	@Column(name = "last_service_date")
	private Date controlDate;
	
	@Transient
	private Date lastControlDate;
	
	@Transient
	private String lastState;
	
	@Transient
	private String baustelle;
	
	@Transient
	private String wartungsvertrag;
	
	@Transient
	private Checklist serviceChecklist;
	
	@Transient
	private List<ObjectComments> comments;
	
	@Column(name = "element_state")
	private String state;
	
	@Column(name = "offer_state")
	private String offerState;
	
    @JoinTable(name = "assoc_offers_elements", joinColumns = {
            @JoinColumn(name = "fk_element", referencedColumnName = "id")}, inverseJoinColumns = {
            @JoinColumn(name = "fk_offer", referencedColumnName = "id")})
        @ManyToMany( fetch = FetchType.EAGER)
        private List<Offers> offerList;
	
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "element", fetch = FetchType.LAZY)
    private List<ServiceHistory> historyItems;
    
    @Transient
	private Boolean checklistHistoryExists; 
	
	
//    @JoinTable(name = "assoc_service_elements", joinColumns = {
//            @JoinColumn(name = "fk_elements", referencedColumnName = "id")}, inverseJoinColumns = {
//            @JoinColumn(name = "fk_service", referencedColumnName = "id")})
//    @ManyToMany(cascade = { CascadeType.ALL}, fetch = FetchType.EAGER)
//    private List<Service> serviceList;
    

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getOrientation() {
		return orientation;
	}

	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getFront() {
		return front;
	}

	public void setFront(String front) {
		this.front = front;
	}
	
	public String getElementnumber() {
		return elementnumber.replace("*", "");
	}

	public void setElementnumber(String elementnumber) {
		this.elementnumber = elementnumber;
	}

	public ElementType getElementType() {
		return elementType;
	}

	public void setElementType(ElementType elementType) {
		this.elementType = elementType;
	}

	public Checklist getChecklist() {
		return checklist;
	}

	public void setChecklist(Checklist checklist) {
		this.checklist = checklist;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    @Override
	public String toString() {
    	return String.format(elementnumber);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((elementnumber == null) ? 0 : elementnumber.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceElements other = (ServiceElements) obj;
		if (elementnumber == null) {
			if (other.elementnumber != null)
				return false;
		} else if (!elementnumber.equals(other.elementnumber))
			return false;
		return true;
	}

	public Front getFrontType() {
		return frontType;
	}

	public void setFrontType(Front frontType) {
		this.frontType = frontType;
	}

	public Direction getDirectionType() {
		return directionType;
	}

	public void setDirectionType(Direction directionType) {
		this.directionType = directionType;
	}

	public Boolean getPrintNewBarcode() {
		return printNewBarcode;
	}

	public void setPrintNewBarcode(Boolean printNewBarcode) {
		this.printNewBarcode = printNewBarcode;
	}

	public Date getControlDate() {
		return controlDate;
	}

	public void setControlDate(Date controlDate) {
		this.controlDate = controlDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<ServiceHistory> getHistoryItems() {
		return historyItems;
	}

	public void setHistoryItems(List<ServiceHistory> historyItems) {
		this.historyItems = historyItems;
	}

	public Boolean getChecklistHistoryExists() {
		return checklistHistoryExists;
	}

	public void setChecklistHistoryExists(Boolean checklistHistoryExists) {
		this.checklistHistoryExists = checklistHistoryExists;
	}

	public Date getLastControlDate() {
		return lastControlDate;
	}

	public void setLastControlDate(Date lastControlDate) {
		this.lastControlDate = lastControlDate;
	}

	public String getLastState() {
		return lastState;
	}

	public void setLastState(String lastState) {
		this.lastState = lastState;
	}

	public String getBaustelle() {
		return baustelle;
	}

	public void setBaustelle(String baustelle) {
		this.baustelle = baustelle;
	}

	public String getWartungsvertrag() {
		return wartungsvertrag;
	}

	public void setWartungsvertrag(String wartungsvertrag) {
		this.wartungsvertrag = wartungsvertrag;
	}

	public boolean isCreateOffer() {
		return createOffer;
	}

	public void setCreateOffer(boolean createOffer) {
		this.createOffer = createOffer;
	}

	public Checklist getServiceChecklist() {
		return serviceChecklist;
	}

	public void setServiceChecklist(Checklist serviceChecklist) {
		this.serviceChecklist = serviceChecklist;
	}

	public DinType getDinType() {
		return dinType;
	}

	public void setDinType(DinType dinType) {
		this.dinType = dinType;
	}

	public String getOfferState() {
		return offerState;
	}

	public void setOfferState(String offerState) {
		this.offerState = offerState;
	}

	public List<Offers> getOfferList() {
		return offerList;
	}

	public void setOfferList(List<Offers> offerList) {
		this.offerList = offerList;
	}

	public List<ObjectComments> getComments() {
		return comments;
	}

	public void setComments(List<ObjectComments> comments) {
		this.comments = comments;
	}
	
	
	
	
	
}
