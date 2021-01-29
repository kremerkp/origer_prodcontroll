package lu.origer.serviceagree.models.reporting;

import java.io.Serializable;

public class IntervalChart implements Serializable {
	
	private String name;
	private String IntervallName; 
	
	private Integer state;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getIntervallName() {
		return IntervallName;
	}

	public void setIntervallName(String intervallName) {
		IntervallName = intervallName;
	}

}
