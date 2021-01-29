package lu.origer.serviceagree.models.synch;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

import lu.origer.serviceagree.models.checklist.ChecklistItem;
import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contract.Service;
import lu.origer.serviceagree.models.main.FileArchive;

@Entity
@Table(name = "service_history")
@XmlRootElement
public class ServiceHistory implements Serializable {

	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "starttime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;

	@Column(name = "endtime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date endTime;

	@Column(name = "checking_sec")
	private Integer checkingSeconds;

	@Column(name = "setup_sec")
	private Integer setupSeconds;

	@Column(name = "error_history_flag")
	private Boolean errorHistoryFlag;

	@Column(name = "visualcontrol")
	private Boolean visualControl;

	@Column(name = "functionalcontrol")
	private Boolean functionalControl;

	@Column(name = "isok")
	private Boolean checkedAsOk;

	@Column(name = "isdefect")
	private Boolean checkedAsDefect;

	@Column(name = "createoffer")
	private Boolean createOffer;

	@Column(name = "islacking")
	private Boolean checkedAsLack;
	
	@Column(name = "isRepaired")
	private Boolean checkedAsRepaired;
	
	@Column(name = "description")
	private String description;
	
	@Column(name = "fk_checklist")
	private Integer serviceChecklist;
	
	@Column(name = "comment")
	private String comment;
	
	@Column(name = "setBackOk")
	private Boolean wasSettedBackOk;
	
	@Column(name = "vf")
	private Integer visualAndFuntionControl;

	@NotNull
	@JoinColumn(name = "fk_service", referencedColumnName = "id")
	@ManyToOne(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER)
	private Service service;

	@NotNull
	@JoinColumn(name = "fk_element", referencedColumnName = "id")
	@ManyToOne(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER)
	private ServiceElements element;

	@NotNull
	@JoinColumn(name = "fk_checklistitem", referencedColumnName = "id")
	@ManyToOne(cascade = { CascadeType.DETACH }, fetch = FetchType.EAGER)
	private ChecklistItem checkListItem;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "serviceHistory", fetch = FetchType.EAGER)
	private List<FileArchive> files;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getCheckingSeconds() {
		return checkingSeconds;
	}

	public void setCheckingSeconds(Integer checkingSeconds) {
		this.checkingSeconds = checkingSeconds;
	}

	public Integer getSetupSeconds() {
		return setupSeconds;
	}

	public void setSetupSeconds(Integer setupSeconds) {
		this.setupSeconds = setupSeconds;
	}


	public Boolean getErrorHistoryFlag() {
		return errorHistoryFlag;
	}

	public void setErrorHistoryFlag(Boolean errorHistoryFlag) {
		this.errorHistoryFlag = errorHistoryFlag;
	}

	public Boolean getVisualControl() {
		return visualControl;
	}

	public void setVisualControl(Boolean visualControl) {
		this.visualControl = visualControl;
	}

	public Boolean getFunctionalControl() {
		return functionalControl;
	}

	public void setFunctionalControl(Boolean functionalControl) {
		this.functionalControl = functionalControl;
	}

	public Boolean getCheckedAsOk() {
		return checkedAsOk;
	}

	public void setCheckedAsOk(Boolean checkedAsOk) {
		this.checkedAsOk = checkedAsOk;
	}

	public Boolean getCheckedAsDefect() {
		return checkedAsDefect;
	}

	public void setCheckedAsDefect(Boolean checkedAsDefect) {
		this.checkedAsDefect = checkedAsDefect;
	}

	public Boolean getCreateOffer() {
		return createOffer;
	}

	public void setCreateOffer(Boolean createOffer) {
		this.createOffer = createOffer;
	}

	public Boolean getCheckedAsLack() {
		return checkedAsLack;
	}

	public void setCheckedAsLack(Boolean checkedAsLack) {
		this.checkedAsLack = checkedAsLack;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public ServiceElements getElement() {
		return element;
	}

	public void setElement(ServiceElements element) {
		this.element = element;
	}

	public ChecklistItem getCheckListItem() {
		return checkListItem;
	}

	public void setCheckListItem(ChecklistItem checkListItem) {
		this.checkListItem = checkListItem;
	}

	public List<FileArchive> getFiles() {
		return files;
	}

	public void setFiles(List<FileArchive> files) {
		this.files = files;
	}

	public Boolean getCheckedAsRepaired() {
		return checkedAsRepaired;
	}

	public void setCheckedAsRepaired(Boolean checkedAsRepaired) {
		this.checkedAsRepaired = checkedAsRepaired;
	}

	public Integer getServiceChecklist() {
		return serviceChecklist;
	}

	public void setServiceChecklist(Integer serviceChecklist) {
		this.serviceChecklist = serviceChecklist;
	}

	public Integer getVisualAndFuntionControl() {
		return visualAndFuntionControl;
	}

	public void setVisualAndFuntionControl(Integer visualAndFuntionControl) {
		this.visualAndFuntionControl = visualAndFuntionControl;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getWasSettedBackOk() {
		return wasSettedBackOk;
	}

	public void setWasSettedBackOk(Boolean wasSettedBackOk) {
		this.wasSettedBackOk = wasSettedBackOk;
	}

	

}
