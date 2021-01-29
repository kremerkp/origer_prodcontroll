package lu.origer.serviceagree.models.assocs;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AssocServiceElementsId implements Serializable {
	
	@Column(name="fk_service")
	private Integer service;
	
	@Column(name="fk_elements")
	private Integer elements;
	
	public AssocServiceElementsId() {

	}

	public Integer getService() {
		return service;
	}

	public void setService(Integer service) {
		this.service = service;
	}

	public Integer getElements() {
		return elements;
	}

	public void setElements(Integer elements) {
		this.elements = elements;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + elements;
		result = prime * result + service;
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
		AssocServiceElementsId other = (AssocServiceElementsId) obj;
		if (elements != other.elements)
			return false;
		if (service != other.service)
			return false;
		return true;
	}
	
	
}

