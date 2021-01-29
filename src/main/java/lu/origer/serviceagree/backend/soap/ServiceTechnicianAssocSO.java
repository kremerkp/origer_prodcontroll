package lu.origer.serviceagree.backend.soap;

public class ServiceTechnicianAssocSO {
	private Long technicianId;
	private Long serviceId;
	private String description;
	public Long getTechnicianId() {
		return technicianId;
	}
	public void setTechnicianId(Long technicianId) {
		this.technicianId = technicianId;
	}
	public Long getServiceId() {
		return serviceId;
	}
	public void setServiceId(Long serviceId) {
		this.serviceId = serviceId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
