package lu.origer.serviceagree.models.assocs;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity 
@Table(name = "assoc_service_elements")
public class Assoc_Service_Elements implements Serializable{
	
  
	/**
	 * 
	 */
	private static final long serialVersionUID = 1055930920494098025L;
	@EmbeddedId          
	private AssocServiceElementsId id;

	public AssocServiceElementsId getId() {
		return id;
	}

	public void setId(AssocServiceElementsId id) {
		this.id = id;
	} 

	
}

