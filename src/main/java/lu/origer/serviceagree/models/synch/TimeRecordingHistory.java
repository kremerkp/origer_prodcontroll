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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contact.Person;
import lu.origer.serviceagree.models.contract.BuildingSite;
import lu.origer.serviceagree.models.contract.Service;
import lu.origer.serviceagree.models.main.FileArchive;

@Entity
@Table(name = "time_recording_history")
@XmlRootElement
public class TimeRecordingHistory implements Serializable{
	

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "time_in_seconds")
    private Integer timeInSeconds;
    
	@JoinColumn(name = "fk_service", referencedColumnName = "id")
	@ManyToOne(cascade = { CascadeType.DETACH}, fetch = FetchType.EAGER)
    private Service service;
    
	@JoinColumn(name = "fk_element", referencedColumnName = "id")
	@ManyToOne(cascade = { CascadeType.DETACH}, fetch = FetchType.EAGER)
    private ServiceElements serviceElement;
    
	@JoinColumn(name = "fk_mechanic", referencedColumnName = "id")
	@ManyToOne(cascade = { CascadeType.DETACH}, fetch = FetchType.EAGER)
    private Person mechanic;
    
	@Column(name = "record_date")
	@Temporal(TemporalType.TIMESTAMP)
    private Date recordDate;
	
	@Column(name = "type")
    private String type;
	
	@Column(name = "billable")
    private Boolean billable;
	
	@Column(name = "billed")
    private Boolean payed;
	
    @OneToMany(cascade = CascadeType.DETACH, mappedBy = "timeRecordingHistory", fetch = FetchType.EAGER)
    private List<FileArchive> files;
    
	@Column(name = "fk_checklist_item")
    private Integer checklistItem;
	
	@JoinColumn(name = "fk_building_site", referencedColumnName = "id")
	@ManyToOne(cascade = { CascadeType.DETACH}, fetch = FetchType.EAGER)
	private BuildingSite buildingSite;
	
	
	@Column(name = "description")
    private String description;
    
    @Transient
    private Integer timeInMinutes; 
    
    @Transient
    private String time; 
    

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

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public ServiceElements getServiceElement() {
		return serviceElement;
	}

	public void setServiceElement(ServiceElements serviceElement) {
		this.serviceElement = serviceElement;
	}

	public Person getMechanic() {
		return mechanic;
	}

	public void setMechanic(Person mechanic) {
		this.mechanic = mechanic;
	}

	public Date getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	public List<FileArchive> getFiles() {
		return files;
	}

	public void setFiles(List<FileArchive> files) {
		this.files = files;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getTimeInMinutes() {
		return timeInMinutes;
	}

	public void setTimeInMinutes(Integer timeInMinutes) {
		this.timeInMinutes = timeInMinutes;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Boolean getBillable() {
		return billable;
	}

	public void setBillable(Boolean billable) {
		this.billable = billable;
	}

	public Boolean getPayed() {
		return payed;
	}

	public void setPayed(Boolean payed) {
		this.payed = payed;
	}

	public BuildingSite getBuildingSite() {
		return buildingSite;
	}

	public void setBuildingSite(BuildingSite buildingSite) {
		this.buildingSite = buildingSite;
	}
	

}
