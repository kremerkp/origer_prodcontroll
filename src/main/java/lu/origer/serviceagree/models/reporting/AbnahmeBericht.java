package lu.origer.serviceagree.models.reporting;

import java.io.Serializable;
import java.util.List;

import lu.origer.serviceagree.models.synch.ServiceElementHistory;

public class AbnahmeBericht implements Serializable{
	
	private Integer elementeGesamt; 
	
	private Integer elementeGeprüft; 
	
	private Integer elementeDefekt; 
	
	private Integer elementeMangelhaft; 
	
	private Integer elementeRepariert; 
	
	private List<ServiceElementHistory> listElements;
	
	private String urlUnterschrift;
	
	private String unterschriftName; 

	public Integer getElementeGesamt() {
		return elementeGesamt;
	}

	public void setElementeGesamt(Integer elementeGesamt) {
		this.elementeGesamt = elementeGesamt;
	}

	public Integer getElementeGeprüft() {
		return elementeGeprüft;
	}

	public void setElementeGeprüft(Integer elementeGeprüft) {
		this.elementeGeprüft = elementeGeprüft;
	}

	public Integer getElementeDefekt() {
		return elementeDefekt;
	}

	public void setElementeDefekt(Integer elementeDefekt) {
		this.elementeDefekt = elementeDefekt;
	}

	public Integer getElementeMangelhaft() {
		return elementeMangelhaft;
	}

	public void setElementeMangelhaft(Integer elementeMangelhaft) {
		this.elementeMangelhaft = elementeMangelhaft;
	}

	public Integer getElementeRepariert() {
		return elementeRepariert;
	}

	public void setElementeRepariert(Integer elementeRepariert) {
		this.elementeRepariert = elementeRepariert;
	}

	public List<ServiceElementHistory> getListElements() {
		return listElements;
	}

	public void setListElements(List<ServiceElementHistory> listElements) {
		this.listElements = listElements;
	}

	public String getUrlUnterschrift() {
		return urlUnterschrift;
	}

	public void setUrlUnterschrift(String urlUnterschrift) {
		this.urlUnterschrift = urlUnterschrift;
	}

	public String getUnterschriftName() {
		return unterschriftName;
	}

	public void setUnterschriftName(String unterschriftName) {
		this.unterschriftName = unterschriftName;
	}
	

}
