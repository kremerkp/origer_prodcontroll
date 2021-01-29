package lu.origer.serviceagree.models.assocs;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contract.Offers;

@Entity 
@Table(name = "assoc_offers_elements")
public class Assoc_Offer_Elements implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1055930920494098025L;
	
	
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "fk_offer", referencedColumnName = "id")
	private Offers offer;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "fk_element", referencedColumnName = "id")	
	private ServiceElements element;
	
	
	@Column(name="offer_state")
	private String offerState;
	
	@Transient 
	private Boolean createOffer; 
	
	@Transient 
	private Integer serviceContract; 
	
	@Transient 
	private String lastState; 
	
	@Transient 
	private Date lastControlDate; 
	
	
	

	
	public Offers getOffer() {
		return offer;
	}

	public void setOffer(Offers offer) {
		this.offer = offer;
	}

	public ServiceElements getElement() {
		return element;
	}

	public void setElement(ServiceElements element) {
		this.element = element;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOfferState() {
		return offerState;
	}

	public void setOfferState(String offerState) {
		this.offerState = offerState;
	}

	public Boolean getCreateOffer() {
		return createOffer;
	}

	public void setCreateOffer(Boolean createOffer) {
		this.createOffer = createOffer;
	}

	public Integer getServiceContract() {
		return serviceContract;
	}

	public void setServiceContract(Integer serviceContract) {
		this.serviceContract = serviceContract;
	}

	public String getLastState() {
		return lastState;
	}

	public void setLastState(String lastState) {
		this.lastState = lastState;
	}

	public Date getLastControlDate() {
		return lastControlDate;
	}

	public void setLastControlDate(Date lastControlDate) {
		this.lastControlDate = lastControlDate;
	}
	
}

