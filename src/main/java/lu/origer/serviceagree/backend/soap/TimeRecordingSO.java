package lu.origer.serviceagree.backend.soap;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author ian.husting
 *
 * RPC-confirm SOAP object for time recordings
 */
@XmlRootElement
public class TimeRecordingSO {
	private Long id;
	private String type;
    private Integer seconds;
    private ServiceElementSO element;
    private ServiceSO service;
    private FileArchiveSO signature;
    private Date recordingDate;    
    private Long mechanic;
    private Long site;
    private Long historyEntry;
    private String comment;
    
	public Date getRecordingDate() {
		return recordingDate;
	}
	public void setRecordingDate(Date recordingDate) {
		this.recordingDate = recordingDate;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getSeconds() {
		return seconds;
	}
	public void setSeconds(Integer seconds) {
		this.seconds = seconds;
	}
	public ServiceElementSO getElement() {
		return element;
	}
	public void setElement(ServiceElementSO element) {
		this.element = element;
	}
	public ServiceSO getService() {
		return service;
	}
	public void setService(ServiceSO service) {
		this.service = service;
	}
	public Long getMechanic() {
		return mechanic;
	}
	public void setMechanic(Long mechanic) {
		this.mechanic = mechanic;
	}
	public FileArchiveSO getSignature() {
		return signature;
	}
	public void setSignature(FileArchiveSO signature) {
		this.signature = signature;
	}
	public Long getSite() {
		return site;
	}
	public void setSite(Long site) {
		this.site = site;
	}
	public Long getHistoryEntry() {
		return historyEntry;
	}
	public void setHistoryEntry(Long historyEntry) {
		this.historyEntry = historyEntry;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}	
}
