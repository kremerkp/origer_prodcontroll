package lu.origer.serviceagree.backend.soap;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author ian.husting
 *
 * RPC-confirm SOAP object for synchronisation controls
 */
@XmlRootElement
public class SyncControlsSO {
	private Integer id;
	private ChecklistItemSO checklistItem;
	private Date startTime;
	private Date endTime;
	private Integer checkingMinutes;
	private Boolean errorHistoryFlag;
	private Integer setupMinutes;
	private Boolean active;
	private String description;
	private Boolean visualControl;
	private Boolean functionalControl;
	private Boolean isOk;
	private Boolean isDefect;
	private Boolean isFaulty;
	private Boolean createOffer;
	private ServiceSO service;
	private ServiceElementSO element;
	private Boolean isRepaired;
	private Integer vf;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public ChecklistItemSO getChecklistItem() {
		return checklistItem;
	}
	public void setChecklistItem(ChecklistItemSO checklistItem) {
		this.checklistItem = checklistItem;
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
	public Integer getCheckingMinutes() {
		return checkingMinutes;
	}
	public void setCheckingMinutes(Integer checkingMinutes) {
		this.checkingMinutes = checkingMinutes;
	}
	public Boolean getErrorHistoryFlag() {
		return errorHistoryFlag;
	}
	public void setErrorHistoryFlag(Boolean errorHistoryFlag) {
		this.errorHistoryFlag = errorHistoryFlag;
	}
	public Integer getSetupMinutes() {
		return setupMinutes;
	}
	public void setSetupMinutes(Integer setupMinutes) {
		this.setupMinutes = setupMinutes;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Boolean getIsOk() {
		return isOk;
	}
	public void setIsOk(Boolean isOk) {
		this.isOk = isOk;
	}
	public Boolean getIsDefect() {
		return isDefect;
	}
	public void setIsDefect(Boolean isDefect) {
		this.isDefect = isDefect;
	}
	public Boolean getIsFaulty() {
		return isFaulty;
	}
	public void setIsFaulty(Boolean isFaulty) {
		this.isFaulty = isFaulty;
	}
	public Boolean getCreateOffer() {
		return createOffer;
	}
	public void setCreateOffer(Boolean createOffer) {
		this.createOffer = createOffer;
	}
	public ServiceSO getService() {
		return service;
	}
	public void setService(ServiceSO service) {
		this.service = service;
	}
	public ServiceElementSO getElement() {
		return element;
	}
	public void setElement(ServiceElementSO element) {
		this.element = element;
	}
	public Boolean getIsRepaired() {
		return isRepaired;
	}
	public void setIsRepaired(Boolean isRepaired) {
		this.isRepaired = isRepaired;
	}
	public Integer getVf()
	{
		return vf;
	}
	public void setVf(Integer vf)
	{
		this.vf = vf;
	}
	
}
