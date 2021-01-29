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
import javax.xml.bind.annotation.XmlRootElement;

import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contact.Person;
import lu.origer.serviceagree.models.contract.BuildingSite;
import lu.origer.serviceagree.models.contract.Service;
import lu.origer.serviceagree.models.main.FileArchive;

@Entity
@Table(name = "synch_time_recordings")
@XmlRootElement
public class SynchTimeRecording implements Serializable {
	

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "time_in_seconds")
    private Integer timeInSeconds;
    
	@Column(name = "fk_service")
	private Integer service;
    
	@Column(name = "fk_element")
    private Integer serviceElement;
    
	@Column(name = "fk_mechanic")
	private Integer mechanic;
	
	@JoinColumn(name = "fk_building_site", referencedColumnName = "id")
	@ManyToOne(cascade = { CascadeType.DETACH}, fetch = FetchType.EAGER)
	private BuildingSite buildingSite;
    
	@Column(name = "record_date")
	@Temporal(TemporalType.TIMESTAMP)
    private Date recordDate;
	
	@Column(name = "type")
    private String type;
	
	@Column(name = "fk_checklist_item")
    private Integer checklistItem;
	
	@Column(name = "description")
    private String description;
	
    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "synchTimeRecording", fetch = FetchType.EAGER)
    private List<FileArchive> files;
    
	@Column(name = "fk_synch_job")
    private SynchJobs synchJob;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTimeInSeconds() {
		return timeInSeconds;
	}

	public void setTimeInSeconds(Integer timeInSeconds) {
		this.timeInSeconds = timeInSeconds;
	}


	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	public Integer getServiceElement() {
		return serviceElement;
	}

	public void setServiceElement(Integer serviceElement) {
		this.serviceElement = serviceElement;
	}

	public Integer getMechanic() {
		return mechanic;
	}

	public void setMechanic(Integer mechanic) {
		this.mechanic = mechanic;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<FileArchive> getFiles() {
		return files;
	}

	public void setFiles(List<FileArchive> files) {
		this.files = files;
	}

	public Integer getChecklistItem() {
		return checklistItem;
	}

	public void setChecklistItem(Integer checklistItem) {
		this.checklistItem = checklistItem;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SynchJobs getSynchJob() {
		return synchJob;
	}

	public void setSynchJob(SynchJobs synchJob) {
		this.synchJob = synchJob;
	}

	public BuildingSite getBuildingSite() {
		return buildingSite;
	}

	public void setBuildingSite(BuildingSite buildingSite) {
		this.buildingSite = buildingSite;
	}
	


}
