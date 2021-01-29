package lu.origer.serviceagree.backend.soap;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author ian.husting
 *
 * RPC-confirm SOAP object for service elements
 */
@XmlRootElement
public class ServiceElementSO {
	private Integer id;
	private String name;
	private ServiceElementTypeSO elementType;
	private String floor;
	private String orientation;
	private String room;
	private String front;
	private SyncControlsSO controls;
	private Boolean active;
	private String elementNumber;
	private Integer[] offerIds;
	private Boolean printLabel;
	private Date lastServiceDate;
	private String status;
	private Integer dinId;
	private String description;
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
	public ServiceElementTypeSO getElementType() {
		return elementType;
	}
	public void setElementType(ServiceElementTypeSO elementType) {
		this.elementType = elementType;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	public String getOrientation() {
		return orientation;
	}
	public void setOrientation(String orientation) {
		this.orientation = orientation;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getFront() {
		return front;
	}
	public void setFront(String front) {
		this.front = front;
	}
	public SyncControlsSO getControls() {
		return controls;
	}
	public void setControls(SyncControlsSO controls) {
		this.controls = controls;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public String getElementNumber() {
		return elementNumber;
	}
	public void setElementNumber(String elementNumber) {
		this.elementNumber = elementNumber;
	}
	public Integer[] getOfferIds() {
		return offerIds;
	}
	public void setOfferIds(Integer[] offerIds) {
		this.offerIds = offerIds;
	}
	public Date getLastServiceDate() {
		return lastServiceDate;
	}
	public void setLastServiceDate(Date lastServiceDate) {
		this.lastServiceDate = lastServiceDate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Boolean getPrintLabel() {
		return printLabel;
	}
	public void setPrintLabel(Boolean printLabel) {
		this.printLabel = printLabel;
	}
	public Integer getDinId()
	{
		return dinId;
	}
	public void setDinId(Integer dinId)
	{
		this.dinId = dinId;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
}
