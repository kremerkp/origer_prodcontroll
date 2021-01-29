package lu.origer.serviceagree.models.main;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import lu.origer.serviceagree.models.contract.BuildingSite;
import lu.origer.serviceagree.models.contract.Invoice;
import lu.origer.serviceagree.models.contract.Offers;
import lu.origer.serviceagree.models.contract.Service;
import lu.origer.serviceagree.models.synch.ServiceHistory;
import lu.origer.serviceagree.models.synch.SynchTimeRecording;
import lu.origer.serviceagree.models.synch.TimeRecordingHistory;

@Entity
@Table(name = "filearchive")
@XmlRootElement
public class FileArchive implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "url")
	private String url;
	
	@Column(name = "description")
	private String description;
	
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
	
	@Column(name = "subfolder")
	private String subfolder;

	
	@JoinColumn(name = "fk_offers", referencedColumnName = "id")
	@ManyToOne(cascade = CascadeType.MERGE,  fetch = FetchType.EAGER)
	private Offers offer;
	
	@JoinColumn(name = "fk_invoice", referencedColumnName = "id")
	@ManyToOne(cascade = CascadeType.MERGE,  fetch = FetchType.EAGER)
	private Invoice invoice;
	
	@JoinColumn(name = "fk_service", referencedColumnName = "id")
	@ManyToOne(cascade = CascadeType.MERGE,  fetch = FetchType.EAGER)
	private Service service;
	
	@JoinColumn(name = "fk_time_recording", referencedColumnName = "id")
	@ManyToOne(cascade = CascadeType.MERGE,  fetch = FetchType.EAGER)
	private TimeRecordingHistory timeRecordingHistory;
	
	@JoinColumn(name = "fk_synch_time_recording", referencedColumnName = "id")
	@ManyToOne(cascade = CascadeType.MERGE,  fetch = FetchType.EAGER)
	private SynchTimeRecording synchTimeRecording;
	
	@JoinColumn(name = "fk_service_history", referencedColumnName = "id")
	@ManyToOne(cascade = CascadeType.MERGE,  fetch = FetchType.EAGER)
	private ServiceHistory serviceHistory;
	
	@JoinColumn(name = "fk_building_site", referencedColumnName = "id")
	@ManyToOne(cascade = CascadeType.MERGE,  fetch = FetchType.EAGER)
	private BuildingSite buildingSite;

	@Column(name = "sync")
	private Boolean synchronizeAndroid;
	
	@Column(name = "is_signature")
	private Boolean signature;

	
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public Offers getOffer() {
		return offer;
	}

	public void setOffer(Offers offer) {
		this.offer = offer;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public ServiceHistory getServiceHistory() {
		return serviceHistory;
	}

	public void setServiceHistory(ServiceHistory serviceHistory) {
		this.serviceHistory = serviceHistory;
	}

	public String getSubfolder() {
		return subfolder;
	}

	public void setSubfolder(String subfolder) {
		this.subfolder = subfolder;
	}

	public TimeRecordingHistory getTimeRecordingHistory() {
		return timeRecordingHistory;
	}

	public void setTimeRecordingHistory(TimeRecordingHistory timeRecordingHistory) {
		this.timeRecordingHistory = timeRecordingHistory;
	}

	public BuildingSite getBuildingSite() {
		return buildingSite;
	}

	public void setBuildingSite(BuildingSite buildingSite) {
		this.buildingSite = buildingSite;
	}

	public Boolean getSynchronizeAndroid() {
		return synchronizeAndroid;
	}

	public void setSynchronizeAndroid(Boolean synchronizeAndroid) {
		this.synchronizeAndroid = synchronizeAndroid;
	}

	public SynchTimeRecording getSynchTimeRecording() {
		return synchTimeRecording;
	}

	public void setSynchTimeRecording(SynchTimeRecording synchTimeRecording) {
		this.synchTimeRecording = synchTimeRecording;
	}

	public Boolean getSignature() {
		return signature;
	}

	public void setSignature(Boolean signature) {
		this.signature = signature;
	}

	

}
