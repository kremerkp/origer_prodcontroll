package lu.origer.serviceagree.models.synch;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "synch_jobs")
@XmlRootElement
public class SynchJobs implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name = "target_elements")
	private Integer targetElementsCount;
	
	@Column(name = "actual_elements")
	private Integer actualCountedElements; 
	
	@Column(name = "state_android")
	private String androidState;
	
	@Column(name = "state_webapp")
	private String webappState; 
	
	@Basic(optional = false)
	@NotNull
	@Column(name = "create_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createDate;
	
	@Column(name = "create_user")
	private Integer createUserID;
	
	@Column(name = "description")
	private String description;
	
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTargetElementsCount() {
		return targetElementsCount;
	}

	public void setTargetElementsCount(Integer targetElementsCount) {
		this.targetElementsCount = targetElementsCount;
	}

	public Integer getActualCountedElements() {
		return actualCountedElements;
	}

	public void setActualCountedElements(Integer actualCountedElements) {
		this.actualCountedElements = actualCountedElements;
	}

	public String getAndroidState() {
		return androidState;
	}

	public void setAndroidState(String androidState) {
		this.androidState = androidState;
	}

	public String getWebappState() {
		return webappState;
	}

	public void setWebappState(String webappState) {
		this.webappState = webappState;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getCreateUserID() {
		return createUserID;
	}

	public void setCreateUserID(Integer createUserID) {
		this.createUserID = createUserID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	


}
