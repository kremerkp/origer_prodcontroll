package lu.origer.serviceagree.backend.soap;

public class SoapParcel {
	private Integer serviceSize;
	private Integer controlSize;
	private Integer fileArchiveSize;
	private Integer offerSize;	
	private Integer recordingSize;
	private Integer elementsSize;
	private Integer techSize;
	
	private ServiceSO[] services;
	private SyncControlsSO[] controls;
	private OfferSO[] offers;
	private FileArchiveSO[] fileArchive;
	private TimeRecordingSO[] recordings;
	private Integer[] printRequestElements;
	private ServiceElementSO[] elements;
	private ServiceTechnicianAssocSO[] technicians;
	
	public Integer getServiceSize() {
		return serviceSize;
	}
	public void setServiceSize(Integer serviceSize) {
		this.serviceSize = serviceSize;
	}
	public Integer getControlSize() {
		return controlSize;
	}
	public void setControlSize(Integer controlSize) {
		this.controlSize = controlSize;
	}
	public Integer getFileArchiveSize() {
		return fileArchiveSize;
	}
	public void setFileArchiveSize(Integer fileArchiveSize) {
		this.fileArchiveSize = fileArchiveSize;
	}
	public ServiceSO[] getServices() {
		return services;
	}
	public void setServices(ServiceSO[] services) {
		this.services = services;
	}
	public SyncControlsSO[] getControls() {
		return controls;
	}
	public void setControls(SyncControlsSO[] controls) {
		this.controls = controls;
	}
	public Integer getOfferSize() {
		return offerSize;
	}
	public void setOfferSize(Integer offerSize) {
		this.offerSize = offerSize;
	}
	public OfferSO[] getOffers() {
		return offers;
	}
	public void setOffers(OfferSO[] offers) {
		this.offers = offers;
	}
	public FileArchiveSO[] getFileArchive() {
		return fileArchive;
	}
	public void setFileArchive(FileArchiveSO[] fileArchive) {
		this.fileArchive = fileArchive;
	}
	public Integer getRecordingSize() {
		return recordingSize;
	}
	public void setRecordingSize(Integer recordingSize) {
		this.recordingSize = recordingSize;
	}
	public TimeRecordingSO[] getRecordings() {
		return recordings;
	}
	public void setRecordings(TimeRecordingSO[] recordings) {
		this.recordings = recordings;
	}
	public Integer[] getPrintRequestElements() {
		return printRequestElements;
	}
	public void setPrintRequestElements(Integer[] printRequestElements) {
		this.printRequestElements = printRequestElements;
	}
	public Integer getElementsSize() {
		return elementsSize;
	}
	public void setElementsSize(Integer elementsSize) {
		this.elementsSize = elementsSize;
	}
	public ServiceElementSO[] getElements() {
		return elements;
	}
	public void setElements(ServiceElementSO[] elements) {
		this.elements = elements;
	}
	public Integer getTechSize() {
		return techSize;
	}
	public void setTechSize(Integer techSize) {
		this.techSize = techSize;
	}
	public ServiceTechnicianAssocSO[] getTechnicians() {
		return technicians;
	}
	public void setTechnicians(ServiceTechnicianAssocSO[] technicians) {
		this.technicians = technicians;
	}
	
}
