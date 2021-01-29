package lu.origer.serviceagree.backend.soap;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author ian.husting
 *
 * RPC-confirm SOAP object for offers
 */
@XmlRootElement
public class FileArchiveSO 
{
	private Long id;
	private String url;
	private byte[] data;
	private SyncControlsSO controls;
	private String name;
	private String description;
	private Date createDate;
	private TimeRecordingSO recording;
	private PersonSO createUser;
	private ServiceSO service;
	private Integer site;
	private Boolean isSignature;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	public SyncControlsSO getControls() {
		return controls;
	}
	public void setControls(SyncControlsSO controls) {
		this.controls = controls;
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
	public TimeRecordingSO getRecording() {
		return recording;
	}
	public void setRecording(TimeRecordingSO recording) {
		this.recording = recording;
	}
	public PersonSO getCreateUser() {
		return createUser;
	}
	public void setCreateUser(PersonSO createUser) {
		this.createUser = createUser;
	}
	public ServiceSO getService() {
		return service;
	}
	public void setService(ServiceSO service) {
		this.service = service;
	}
	public Integer getSite() {
		return site;
	}
	public void setSite(Integer site) {
		this.site = site;
	}
	public Boolean getIsSignature() {
		return isSignature;
	}
	public void setIsSignature(Boolean isSignature) {
		this.isSignature = isSignature;
	}	
}
