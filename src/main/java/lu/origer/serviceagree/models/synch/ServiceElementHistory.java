package lu.origer.serviceagree.models.synch;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ServiceElementHistory implements Serializable{
	   
	private static final long serialVersionUID = 1L;

	private String name; 
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    
    private String state; 
    
    private String description; 
    
    private String checklistitems2Repair; 
    
    private String checklistitemsRepaired; 
    
    private String floor; 
    
    private String room; 
    
    private List<String> mechanics;
    
    private Integer commentId; 
    
    private Integer serviceElementId; 

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ServiceElementHistory other = (ServiceElementHistory) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getChecklistitems2Repair() {
		return checklistitems2Repair;
	}

	public void setChecklistitems2Repair(String checklistitems2Repair) {
		this.checklistitems2Repair = checklistitems2Repair;
	}

	public String getChecklistitemsRepaired() {
		return checklistitemsRepaired;
	}

	public void setChecklistitemsRepaired(String checklistitemsRepaired) {
		this.checklistitemsRepaired = checklistitemsRepaired;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public List<String> getMechanics() {
		return mechanics;
	}

	public void setMechanics(List<String> mechanics) {
		this.mechanics = mechanics;
	}

	public Integer getCommentId() {
		return commentId;
	}

	public void setCommentId(Integer commentId) {
		this.commentId = commentId;
	}

	public Integer getServiceElementId() {
		return serviceElementId;
	}

	public void setServiceElementId(Integer serviceElementId) {
		this.serviceElementId = serviceElementId;
	}
	
	
}
