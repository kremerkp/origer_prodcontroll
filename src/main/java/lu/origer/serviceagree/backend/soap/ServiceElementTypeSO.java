package lu.origer.serviceagree.backend.soap;

/**
 * 
 * @author ian.husting
 *
 * RPC-confirm SOAP object for service element types
 */
public class ServiceElementTypeSO {
	private Integer id;
	private String name;
	private Boolean active;
	
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
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
}
