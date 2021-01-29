package lu.origer.serviceagree.frontend.contract.edit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.varia.StringMatchFilter;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.DualListModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import lu.origer.serviceagree.backend.contract.FileArchiveFacade;
import lu.origer.serviceagree.backend.contract.OfferFacade;
import lu.origer.serviceagree.backend.contract.ServiceContractFacade;
import lu.origer.serviceagree.backend.contract.ServiceElementFacade;
import lu.origer.serviceagree.backend.contract.ServiceFacade;
import lu.origer.serviceagree.frontend.main.BasicFormBean;
import lu.origer.serviceagree.frontend.main.Constants;
import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contract.Offers;
import lu.origer.serviceagree.models.contract.Service;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.main.FileArchive;

@ManagedBean
@ViewScoped
public class OffersEditBean extends BasicFormBean<Offers> {

	private static final long serialVersionUID = 1L;

	private static final String LOG_REPORTER = "ChecklistItemCategoryEditBean";

	@Inject
	OfferFacade offerFacade;

	@Inject
	ServiceFacade serviceFacade;

	@Inject
	ServiceContractFacade serviceContractFacade;

	@Inject
	ServiceElementFacade serviceElementFacade;

	@Inject
	FileArchiveFacade fileArchiveFacade;

	private Offers offer;

	private Service service;

	private ServiceContract serviceContract;

	private List<FileArchive> fileList;

	private List<FileArchive> fileListNew;

	List<ServiceElements> source;
	List<ServiceElements> target;

	private List<ServiceElements> allItemsList;

	private DualListModel<ServiceElements> serviceElementPickList;

	private StreamedContent file;

	private Integer openFile;

	private Integer delFile;

	@Override
	public void save() {
		if (Faces.getContext().getMessageList().isEmpty()) {
			// No Errors occured
			if (offer.getId() != null) {
				update();
			} else {
				create();
			}
			persistFileList();
		}

	}

	public List<Service> completeService(String query) {
		return serviceFacade.completeService(query);
	}

	public List<ServiceContract> completeServiceContract(String query) {
		return serviceContractFacade.completeServiceContract(query);
	}

	public void saveAndClose() {
		// save();
		try {
			save();
			Faces.redirect(
					"faces/origer/serveAgree/admin/offers/offers_list.xhtml?faces-redirect=true&id=" + offer.getId());
		} catch (IOException ex) {
			Logger.getLogger(OffersEditBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void initializeItems(){
		if (this.serviceContract != null){
			source = orderElementList(serviceElementFacade.findServiceElementsForServiceContract(this.serviceContract));
			target = new ArrayList<>(); 
			serviceElementPickList = new DualListModel<>(source, target);
			
		}

	}
	
	public void showAllElements(){
		setContractFromUrl();
		if (this.offer != null){
			this.serviceContract = offer.getServiceContract();
			fileList = fileArchiveFacade.findFilesForOffer(offer);
			source = orderElementList(serviceElementFacade.findServiceElementsForServiceContract(this.serviceContract));
			// target =
			// serviceElementFacade.findServiceElementsForOffer(this.offer);
			target = orderElementList(offer.getServiceElementList());
			source.removeAll(target);
			serviceElementPickList = new DualListModel<>(source, target);
			allItemsList = serviceElementFacade.findServiceElementsForServiceContract(this.serviceContract);
			
		}
	}
	
	public void showOnlyActiveElements(){
		setContractFromUrl();
		
		if (this.offer != null){
			source = new ArrayList<>();
			source = orderElementList(serviceElementFacade.findServiceElementsForServiceContract(this.serviceContract));
			// target =
			// serviceElementFacade.findServiceElementsForOffer(this.offer);
			if (offer.getServiceElementList() != null){
				target = orderElementList(offer.getServiceElementList());				
			} else {
				target = new ArrayList<>();
			}
			
			source.removeAll(target);
			
			source = orderElementList(
					serviceElementFacade.findServiceElementsForServiceContractOffer(this.serviceContract));
			serviceElementPickList = new DualListModel<>(source, target);
			allItemsList = serviceElementFacade.findAll();
			
		}
				
	}
	
	public void setOfferDefaultValues(){
		this.offer.setCreateDate(new Date());
		this.offer.setState("gesendet");
		this.offer.setActive(true);
	}
	
	public void setContractFromUrl(){
		Integer contractId = getApplicationBean().getContractIdFromUrl();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String url = request.getRequestURL().toString();
		String uri = request.getRequestURI();
		System.out.println(url);
		System.out.println(uri);

		if (contractId != null && contractId != -1) {
			this.serviceContract = serviceContractFacade.find(contractId);
		}
	}

	@PostConstruct
	public void init() {
		Integer id = getApplicationBean().getIdFromURL();
		
		
		source = new ArrayList<>();
		target = new ArrayList<>();

		setContractFromUrl();

		this.fileListNew = new ArrayList<>();
		if (id == null || id == -1) {
			offer = new Offers();
			setOfferDefaultValues();
			fileList = new ArrayList<>();
			if (serviceContract != null) {
				source = orderElementList(
						serviceElementFacade.findServiceElementsForServiceContract(this.serviceContract));
				// target =
				// serviceElementFacade.findServiceElementsForOffer(this.offer);
				target = new ArrayList<>();
			} else {
				// sollte erst initialisisert werden wenn ein Vertrag ausgewählt wurde, sonst 
				// werden zu viele Objekte geladen
				source = new ArrayList<>();// orderElementList(serviceElementFacade.findAll());
				target = new ArrayList<>();
			}
			serviceElementPickList = new DualListModel<>(source, target);
			allItemsList = serviceElementFacade.findAll();
		} else {
			offer = offerFacade.find(id);
			this.serviceContract = offer.getServiceContract();
			fileList = fileArchiveFacade.findFilesForOffer(offer);
			source = orderElementList(serviceElementFacade.findServiceElementsForServiceContract(this.serviceContract));
			// target =
			// serviceElementFacade.findServiceElementsForOffer(this.offer);
			target = orderElementList(offer.getServiceElementList());
			source.removeAll(target);
			serviceElementPickList = new DualListModel<>(source, target);
			allItemsList = serviceElementFacade.findServiceElementsForServiceContract(this.serviceContract);
		}
	}

	public void setElementCheckListFromServiceElements(Offers offer) {

		List<ServiceElements> sList = orderElementList(offer.getServiceElementList());

		String serviceElements = "";
		if (sList != null && sList.size() > 0) {
			int i = 0;
			for (ServiceElements e : sList) {
				i++;
				String end = "\n";
				if (i == getServiceElementPickList().getTarget().size()) {
					end = "";
				}
				serviceElements = serviceElements + e.getElementnumber().replace("*", "") + end;
			}
		}
		offer.setChecklistitems(serviceElements);
		offerFacade.edit(offer);
	}

	@Override
	protected void create() {
		offer.setCreateDate(new Date());
		offer.setEditDate(new Date());
		offer.setCreateUser(getSessionBean().getCurrentUser().getId());
		offer.setEditUser(getSessionBean().getCurrentUser().getId());
		offer.setServiceContract(serviceContract);
		offerFacade.create(offer);
		this.offer.setServiceElementList(serviceElementPickList.getTarget());
		setElementCheckListFromServiceElements(offer);
		updateElementOfferCreateState(offer.getServiceElementList());
		String message = "Neuer Eintrag eines Angebots [" + offer.getName() + "] wurde angelegt";
		getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.INFO);
		
	}
	// private void persitElementPickListOffer(List<ServiceElements>
	// target,List<ServiceElements> source ){
	// for(ServiceElements e : source){
	// e.setOffer(null);
	// serviceElementFacade.edit(e);
	// }
	// for (ServiceElements s : target){
	// s.setOffer(this.offer);
	// serviceElementFacade.edit(s);
	// }
	// }

	public List<ServiceElements> orderElementList(List<ServiceElements> sList) {
		Collections.sort(sList, new Comparator<ServiceElements>() {
			@Override
			public int compare(ServiceElements s1, ServiceElements s2) {
				return s1.getElementnumber().compareTo(s2.getElementnumber());
			}
		});
		return sList;
	}

	public void persistFileList() {
		if (this.fileList != null && this.fileList.size() > 0) {
			for (FileArchive f : this.fileList) {
				fileArchiveFacade.edit(f);
			}
		}
	}
	
	public void updateElementOfferCreateState(List<ServiceElements> sList){
		
		if(this.offer.getState().equalsIgnoreCase("ok")){
			if ( sList != null && sList.size() > 0){				
				for(ServiceElements se : sList){
					se.setCreateOffer(false);
					serviceElementFacade.edit(se);
					
				}			
			}
		} else if (this.offer.getState().equalsIgnoreCase("abgelehnt")){ 
			for(ServiceElements se: this.serviceElementPickList.getSource()){
				se.setCreateOffer(false);
				serviceElementFacade.edit(se);				
			}
		}
	}

	@Override
	protected void update() {
		// Update checklistItemCategory
		offer.setEditUser(getSessionBean().getCurrentUser().getId());
		offer.setEditDate(new Date());
		this.offer.setServiceElementList(serviceElementPickList.getTarget());
		if (serviceElementPickList.getTarget() != null && serviceElementPickList.getTarget().size() > 0) {
			this.offer.setServiceElementList(serviceElementPickList.getTarget());
			// persitElementPickListOffer(serviceElementPickList.getTarget(),
			// serviceElementPickList.getSource());
		}
		if (this.serviceContract != null) {
			offer.setServiceContract(serviceContract);
		}

		offerFacade.edit(offer);
		setElementCheckListFromServiceElements(offer);
		updateElementOfferCreateState(offer.getServiceElementList());
	}

	public Boolean doesFileNameAllreadyExist(String fileName) {
		Boolean exists = false;
		for (FileArchive f : fileArchiveFacade.findFilesForOffer(this.offer)) {
			if (f.getName().equals(fileName)) {
				exists = true;
			}
		}
		return exists;
	}

	public void actionDelFile() {
		System.out.println("actionFile");
		try {
			String url = fileArchiveFacade.find(this.delFile).getUrl();
			// refresh DB
			fileArchiveFacade.remove(fileArchiveFacade.find(this.delFile));
			// refresh list
			this.fileList = new ArrayList<>();
			this.fileList.addAll(fileArchiveFacade.findFilesForOffer(this.offer));
			// refresh FileSystem
			File tmpFile = new File(url);
			if (tmpFile.delete()) {
				System.out.println(tmpFile.getName() + " is deleted!");

			} else {
				System.out.println("Delete operation is failed.");
			}
		} catch (Exception e) {
			System.out.println("File does not exists: " + e.getMessage());
		}

	}

	public void openFileTest(String fileUrl) throws FileNotFoundException {
		System.out.println(fileUrl);
		System.out.println("geht");
		//String url = fileArchiveFacade.find(this.openFile).getUrl();

		File f = new File(fileUrl);
		InputStream input = new FileInputStream(f);
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		setFile(new DefaultStreamedContent(input, externalContext.getMimeType(f.getName()), f.getName()));
		System.out.println("PREP = " + this.file.getName());

		FacesContext facesContext = FacesContext.getCurrentInstance();

	}

	public void actionOpenFile(FileArchive file) throws FileNotFoundException {
		String url = getApplicationBean().getPathToFileArchiv() + "/" + file.getSubfolder() + "/" + file.getName(); 

		File f = new File(url);
		InputStream input = new FileInputStream(f);
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		setFile(new DefaultStreamedContent(input, externalContext.getMimeType(f.getName()), f.getName()));
		System.out.println("PREP = " + this.file.getName());

		FacesContext facesContext = FacesContext.getCurrentInstance();

	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		
		UploadedFile uploadedFile = event.getFile();
		// TODO: fill FileArchive
		String fileName = uploadedFile.getFileName();
		// String contentType = uploadedFile.getContentType();
		InputStream input = uploadedFile.getInputstream();
		String year = getApplicationBean().getYearFromDate(new Date());
		File dir = new File(getApplicationBean().getPathToFileArchiv() + "/" + year);
		getApplicationBean().createDirectory(dir);

		OutputStream output = new FileOutputStream(new File(dir.getPath(), fileName));

		if (!doesFileNameAllreadyExist(fileName)) {
			try {
				IOUtils.copy(input, output);
			} finally {
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
			}
			FileArchive f = new FileArchive();
			f.setCreateDate(new Date());
			f.setEditDate(new Date());
			f.setOffer(this.offer);
			f.setSignature(false);
			f.setName(fileName);
			f.setDescription(null);
			f.setSubfolder(year);
			f.setUrl(dir.getPath() + "/" + fileName);
			// fileArchiveFacade.edit(f);
			List<FileArchive> accFileList = new ArrayList<>();
			accFileList.addAll(this.fileList);
			accFileList.add(f);
			this.fileList = new ArrayList<>();
			this.fileList.addAll(accFileList);
		}

	}

	@Override
	protected void checkInput() {
		// TODO Auto-generated method stub
	}

	public Offers getOffer() {
		return offer;
	}

	public void setOffer(Offers offer) {
		this.offer = offer;
	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public ServiceContract getServiceContract() {
		return serviceContract;
	}

	public void setServiceContract(ServiceContract serviceContract) {
		this.serviceContract = serviceContract;
	}

	public List<FileArchive> getFileList() {
		return fileList;
	}

	public void setFileList(List<FileArchive> fileList) {
		this.fileList = fileList;
	}

	public List<FileArchive> getFileListNew() {
		return fileListNew;
	}

	public void setFileListNew(List<FileArchive> fileListNew) {
		this.fileListNew = fileListNew;
	}

	public Integer getOpenFile() {
		return openFile;
	}

	public void setOpenFile(Integer openFile) {
		this.openFile = openFile;
	}

	public Integer getDelFile() {
		return delFile;
	}

	public void setDelFile(Integer delFile) {
		this.delFile = delFile;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public List<ServiceElements> getSource() {
		return source;
	}

	public void setSource(List<ServiceElements> source) {
		this.source = source;
	}

	public List<ServiceElements> getTarget() {
		return target;
	}

	public void setTarget(List<ServiceElements> target) {
		this.target = target;
	}

	public DualListModel<ServiceElements> getServiceElementPickList() {
		return serviceElementPickList;
	}

	public void setServiceElementPickList(DualListModel<ServiceElements> serviceElementPickList) {
		this.serviceElementPickList = serviceElementPickList;
	}

	public List<ServiceElements> getAllItemsList() {
		return allItemsList;
	}

	public void setAllItemsList(List<ServiceElements> allItemsList) {
		this.allItemsList = allItemsList;
	}

}
