package lu.origer.serviceagree.models.synch;

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

import lu.origer.serviceagree.models.checklist.ChecklistItem;
import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contract.Service;

@Entity
@Table(name = "synch_controls")
@XmlRootElement
public class SynchControl implements Serializable {
	
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long           id;
    
	@Basic(optional = false)
	@NotNull
	@Column(name = "starttime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date startTime;
	
	@Basic(optional = false)
	@NotNull
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

	@Column(name = "fk_service")
	private Integer service;
	
	@Column(name = "fk_element")
	private Integer element;
	
	@Column(name = "fk_checklistitem")
	private Integer checkListItem;
	
	@Column(name = "fk_synch_jobs")
	private Integer jobId;
	
	@Column(name = "comment")
	private String comment;
	
	@Column(name = "vf")
	private Integer vf;
	

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

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	public Integer getElement() {
		return element;
	}

	public void setElement(Integer element) {
		this.element = element;
	}

	public Integer getCheckListItem() {
		return checkListItem;
	}

	public void setCheckListItem(Integer checkListItem) {
		this.checkListItem = checkListItem;
	}

	public Integer getJobId() {
		return jobId;
	}

	public void setJobId(Integer jobId) {
		this.jobId = jobId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Integer getVf() {
		return vf;
	}

	public void setVf(Integer vf) {
		this.vf = vf;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		SynchControl other = (SynchControl) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Boolean getCheckedAsRepaired() {
		return checkedAsRepaired;
	}

	public void setCheckedAsRepaired(Boolean checkedAsRepaired) {
		this.checkedAsRepaired = checkedAsRepaired;
	}
	
	
	
	
}
