package lu.origer.serviceagree.frontend.contract.edit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import lu.origer.serviceagree.backend.contract.AssocOfferItemFacade;
import lu.origer.serviceagree.backend.contract.FileArchiveFacade;
import lu.origer.serviceagree.backend.contract.OfferFacade;
import lu.origer.serviceagree.backend.contract.ServiceContractFacade;
import lu.origer.serviceagree.backend.contract.ServiceElementFacade;
import lu.origer.serviceagree.backend.synch.ServiceHistoryFacade;
import lu.origer.serviceagree.frontend.main.ApplicationBean;
import lu.origer.serviceagree.frontend.main.BasicFormBean;
import lu.origer.serviceagree.models.assocs.Assoc_Offer_Elements;
import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contract.Offers;
import lu.origer.serviceagree.models.contract.Service;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.main.FileArchive;
import lu.origer.serviceagree.models.synch.ServiceHistory;

@ManagedBean
@ViewScoped
public class OfferAssocEditBean extends BasicFormBean<Assoc_Offer_Elements> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String LOG_REPORTER = "ChecklistEditBean";

	@Inject
	OfferFacade offerFacade;

	@Inject
	ServiceElementFacade serviceElementFacade;

	@Inject
	AssocOfferItemFacade assocFacade;

	@Inject
	ServiceHistoryFacade historyFacade;

	@Inject
	ServiceContractFacade serviceContractFacade;

	@Inject
	FileArchiveFacade fileArchiveFacade;

	private Boolean offerActiv;

	private String offerName;

	private Date offerDate;

	private BigDecimal offerValue;

	private Boolean offerExists = false;

	private ServiceContract serviceContract;

	private Service currentService;

	private StreamedContent file;

	private Assoc_Offer_Elements currentOfferElement;

	private String offerState;

	private List<FileArchive> fileList;

	private List<ServiceHistory> checkListItems;

	private List<ServiceContract> scontracts;

	private List<Assoc_Offer_Elements> assocOfferElements;

	// private List<ChecklistItem> source;
	// private List<ChecklistItem> target;

	private List<Assoc_Offer_Elements> source;
	private List<Assoc_Offer_Elements> assignedAssoc;
	private List<Assoc_Offer_Elements> sourceSelected;
	private List<Assoc_Offer_Elements> target;
	private List<Assoc_Offer_Elements> targetSelected;
	private List<Assoc_Offer_Elements> delteAssoc = new ArrayList<>();

	public Boolean langFr = false;

	private Integer delFile;

	private Offers cl;

	// private DualListModel<Assoc_Checklist_Checklistentries>
	// assocChecklistItemsPickList;

	private List<Assoc_Offer_Elements> allAssocItemsList;

	private List<ServiceElements> checklistItemsList;
	private List<ServiceElements> allItemsList;
	private List<ServiceElements> selectedItems;
	private List<Offers> allChecklist;
	private Integer id = null;

	@PostConstruct
	public void init() {

		this.fileList = new ArrayList<>();
		this.scontracts = serviceContractFacade.findAll();

		List<Integer> elements = new ArrayList<>();
		elements = getApplicationBean().getElements();
		Integer contractId = getApplicationBean().getContractId();

		this.serviceContract = serviceContractFacade.find(contractId);
		this.offerName = "Angebot";
		this.offerDate = new Date();

		id = getApplicationBean().getIdFromURL();
		this.langFr = ApplicationBean.getlangFromURLIsFrench();

		allChecklist = offerFacade.findAll();
		if (id == null || id == -1) {
			assocOfferElements = new ArrayList<>();
			cl = new Offers();
			offerExists = false; 
			// source = assocFacade.findAllItmesForChecklistId(cl);
			source = new ArrayList<>();

			target = new ArrayList<>();
			if (contractId != null) {
				this.serviceChanged(elements);
			}
			// assocChecklistItemsPickList = new DualListModel<>(source2,
			// target2);
			allItemsList = serviceElementFacade.findAll();
			allAssocItemsList = assocFacade.findAllItmesForChecklistId(null);

		} else {
			// cl = offerFacade.find(id);
			cl = offerFacade.findById(id);
			this.serviceContract = cl.getServiceContract();
			offerExists = true;
			initTarget(null);
			initSource(null);
			this.offerActiv = cl.isActive();
			this.offerName = cl.getName();
			this.offerDate = cl.getOfferdate();
			this.offerValue = cl.getAmount();
			this.serviceContract = cl.getServiceContract();
			this.fileList = cl.getFiles();
			this.offerState = cl.getState();

			// assocOfferElements = assocFacade.findByOfferId(id);
			// target = assocFacade.findByOfferId(cl.getId());
			// source = assocFacade.findTargetItemsForOfferId(cl,
			// cl.getServiceContract());
			// assignedAssoc = assocFacade.findByOfferId(cl.getId());
		}
	}

	public void setState() {
		System.out.println("setting state ");
		// if (test.contains("CANC")) {
		if (this.cl != null && this.cl.getId() != null) {
			for (Assoc_Offer_Elements aof : this.targetSelected) {
				assocFacade.edit(aof);
			}
		}
		// recalc OfferState
		this.offerState = offerFacade.getOfferState(this.target, null);

	}

	public void testit() {
		System.out.println("test ok");
	}

	public void targetChecklist(SelectEvent event) {

		this.currentOfferElement = ((Assoc_Offer_Elements) event.getObject());

		ServiceElements serEl = this.currentOfferElement.getElement();

		this.checkListItems = historyFacade.findChecklistForElementWithoutRegie(serEl, this.serviceContract);

		System.out.println("targetChecklist");
	}

	public void sourceChecklist(SelectEvent event) {
		this.currentOfferElement = ((Assoc_Offer_Elements) event.getObject());
		ServiceElements serEl = this.currentOfferElement.getElement();
		this.checkListItems = historyFacade.findChecklistForElementWithoutRegie(serEl, this.serviceContract);
		System.out.println("sourceChecklist");
	}

	public void serviceChanged(List<Integer> elList) {
		// System.out.println("test");
		// System.out.println(this.serviceContract.getServiceContractNumber());
		this.initSource(elList);
		this.initTarget(elList);
	}

	public List<ServiceContract> completeServiceContract(String query) {
		return serviceContractFacade.completeServiceContract(query);
	}

	public Boolean doesFileNameAllreadyExist(String fileName) {
		Boolean exists = false;
		for (FileArchive f : fileArchiveFacade.findFilesForOffer(this.cl)) {
			if (f.getName().equals(fileName)) {
				exists = true;
			}
		}
		return exists;
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

	public void actionDelFile() {
		System.out.println("actionFile");
		try {
			String url = fileArchiveFacade.find(this.delFile).getUrl();
			// refresh DB
			fileArchiveFacade.remove(fileArchiveFacade.find(this.delFile));
			// refresh list
			this.fileList = new ArrayList<>();
			this.fileList.addAll(fileArchiveFacade.findFilesForOffer(this.cl));
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

	public void handleFileUpload(FileUploadEvent event) throws IOException {

		UploadedFile uploadedFile = event.getFile();
		
		String fileName = FilenameUtils.removeExtension(uploadedFile.getFileName());
		String fileExtension = FilenameUtils.getExtension(uploadedFile.getFileName()); 
		String fileNameNew = fileArchiveFacade.getFileName(fileName) + "." + fileExtension;
		
		// String contentType = uploadedFile.getContentType();
		InputStream input = uploadedFile.getInputstream();
		String year = getApplicationBean().getYearFromDate(new Date());
		File dir = new File(getApplicationBean().getPathToFileArchiv() + "/" + year);
		String url = fileArchiveFacade.getUrl(dir.getPath() + "/" + fileName) + "." + fileExtension;
		getApplicationBean().createDirectory(dir);

		OutputStream output = new FileOutputStream(new File(dir.getPath(), fileNameNew));

		if (!doesFileNameAllreadyExist(fileNameNew)) {
			try {
				IOUtils.copy(input, output);
			} finally {
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
			}
			FileArchive f = new FileArchive();
			f.setCreateDate(new Date());
			f.setEditDate(new Date());
			f.setOffer(this.cl);
			f.setSignature(false);
			f.setName(fileNameNew);
			f.setDescription(null);
			f.setSubfolder(year);
			f.setUrl(url);
			//fileArchiveFacade.edit(f);
			List<FileArchive> accFileList = new ArrayList<>();
			accFileList.addAll(this.fileList);
			accFileList.add(f);
			this.fileList = new ArrayList<>();
			this.fileList.addAll(accFileList);
		}

	}

	@Override
	public void save() {
		if (Faces.getContext().getMessageList().isEmpty()) {
			// No Errors occured
			if (this.id != null) {
				update();
			} else {
				create();
			}
		}

	}

	public void saveAndClose() {
		try {
			save();
			Faces.redirect("faces/origer/serveAgree/admin/offers/offers_list.xhtml?faces-redirect=true" + "&lang="
					+ (this.langFr ? "fr" : "de"));
		} catch (IOException ex) {
			Logger.getLogger(ChecklistItemCategoryEditBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void initTarget(List<Integer> elements) {
		List<Assoc_Offer_Elements> tmpList = new ArrayList<>();
		if(elements != null && elements.size() > 0 ){
			tmpList = assocFacade.findSourceItemsForOfferId(this.cl, this.serviceContract, elements, true);
		} else {
			tmpList = assocFacade.findTargetItemsForOfferId(this.cl, this.serviceContract, elements);			
		}
		
		this.target = new ArrayList<>();
		this.target.addAll(tmpList);
	}

	public void initSource(List<Integer> elements) {
		List<Assoc_Offer_Elements> tmpList = new ArrayList<>();
		tmpList = assocFacade.findSourceItemsForOfferId(this.cl, this.serviceContract, elements, false);
		this.source = new ArrayList<>();
		this.source.addAll(tmpList);
	}

	public void setTargetList() {
		List<Assoc_Offer_Elements> tmpList = new ArrayList<>();
		tmpList.addAll(this.target);
		tmpList.addAll(this.sourceSelected);
		this.target = new ArrayList<>();
		this.target.addAll(tmpList);
		tmpList = new ArrayList<>();
		tmpList.addAll(source);
		tmpList.removeAll(this.sourceSelected);
		this.sourceSelected = null;
		this.targetSelected = null;
		this.source = new ArrayList<>();
		this.source.addAll(tmpList);
	}

	public void removeTargetSeleceted() {
		for (Assoc_Offer_Elements as : this.targetSelected) {
			assocFacade.remove(as);
		}
	}

	public void setSourceList() {
		List<Assoc_Offer_Elements> tmpList = new ArrayList<>();
		tmpList.addAll(this.target);
		tmpList.removeAll(this.targetSelected);
		// removeTargetSeleceted();
		delteAssoc.addAll(targetSelected);
		this.target = new ArrayList<>();
		this.target.addAll(tmpList);
		tmpList = new ArrayList<>();
		tmpList.addAll(source);
		tmpList.addAll(this.targetSelected);
		this.sourceSelected = null;
		this.targetSelected = null;
		this.source = new ArrayList<>();
		this.source.addAll(tmpList);
	}

	@Override
	protected void create() {
		createOfferList();
		if (target != null && !target.isEmpty()){			
			for (Assoc_Offer_Elements it : target) {
				it.getElement().setCreateOffer(false);
				serviceElementFacade.edit(it.getElement());
				assocFacade.create(it);
			}
		}

	}

	public void createOfferList() {
		this.cl.setName(this.offerName);
		this.cl.setActive(this.offerActiv);
		this.cl.setAmount(this.offerValue);
		this.cl.setOfferdate(this.offerDate);
		this.cl.setFiles(this.fileList);
		this.cl.setServiceContract(this.serviceContract);
		this.cl.setCreateDate(new Date());
		this.cl.setCreateUser(getSessionBean().getCurrentUser().getId());
		this.cl.setEditDate(new Date());
		this.cl.setEditUser(getSessionBean().getCurrentUser().getId());
		this.cl.setStateDB(offerFacade.getOfferState(this.target, Locale.GERMANY));
		
		offerFacade.persist(cl);
	}

	public void updateOfferList() {
		this.cl.setName(this.offerName);
		this.cl.setActive(this.offerActiv);
		this.cl.setAmount(this.offerValue);
		this.cl.setOfferdate(this.offerDate);
		this.cl.setFiles(this.fileList);
		this.cl.setServiceContract(this.serviceContract);
		this.cl.setEditDate(new Date());
		this.cl.setEditUser(getSessionBean().getCurrentUser().getId());
		this.cl.setStateDB(offerFacade.getOfferState(this.target, Locale.GERMANY));
		offerFacade.edit(cl);
	}

	@Override
	protected void update() {
		updateOfferList();

		List<Assoc_Offer_Elements> tmp = new ArrayList<>();

		// tmp.addAll(assignedAssoc);
		tmp.addAll(this.delteAssoc);
		// edit
		for (Assoc_Offer_Elements it : this.target) {
			if (it.getOfferState().equals(OfferFacade.OFFER_STATE_OK_DE)
					|| it.getOfferState().equals(OfferFacade.OFFER_STATE_CHANCELED_DE)) {
				it.getElement().setCreateOffer(false);
				serviceElementFacade.edit(it.getElement());
			}
			assocFacade.edit(it);
		}
		// remove
		for (Assoc_Offer_Elements it : tmp) {
			assocFacade.remove(it);
		}
	}

	@Override
	protected void checkInput() {
		// TODO Auto-generated method stub

	}

	public OfferFacade getOfferFacade() {
		return offerFacade;
	}

	public void setOfferFacade(OfferFacade offerFacade) {
		this.offerFacade = offerFacade;
	}

	public ServiceElementFacade getServiceElementFacade() {
		return serviceElementFacade;
	}

	public void setServiceElementFacade(ServiceElementFacade serviceElementFacade) {
		this.serviceElementFacade = serviceElementFacade;
	}

	public AssocOfferItemFacade getAssocFacade() {
		return assocFacade;
	}

	public void setAssocFacade(AssocOfferItemFacade assocFacade) {
		this.assocFacade = assocFacade;
	}

	public Boolean getOfferActiv() {
		return offerActiv;
	}

	public void setOfferActiv(Boolean offerActiv) {
		this.offerActiv = offerActiv;
	}

	public String getOfferName() {
		return offerName;
	}

	public void setOfferName(String offerName) {
		this.offerName = offerName;
	}

	public Date getOfferDate() {
		return offerDate;
	}

	public void setOfferDate(Date offerDate) {
		this.offerDate = offerDate;
	}

	public BigDecimal getOfferValue() {
		return offerValue;
	}

	public void setOfferValue(BigDecimal offerValue) {
		this.offerValue = offerValue;
	}

	public ServiceContract getServiceContract() {
		return serviceContract;
	}

	public void setServiceContract(ServiceContract serviceContract) {
		this.serviceContract = serviceContract;
	}

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public List<Assoc_Offer_Elements> getAssocOfferElements() {
		return assocOfferElements;
	}

	public void setAssocOfferElements(List<Assoc_Offer_Elements> assocOfferElements) {
		this.assocOfferElements = assocOfferElements;
	}

	public List<Assoc_Offer_Elements> getSource() {
		return source;
	}

	public void setSource(List<Assoc_Offer_Elements> source) {
		this.source = source;
	}

	public List<Assoc_Offer_Elements> getAssignedAssoc() {
		return assignedAssoc;
	}

	public void setAssignedAssoc(List<Assoc_Offer_Elements> assignedAssoc) {
		this.assignedAssoc = assignedAssoc;
	}

	public List<Assoc_Offer_Elements> getSourceSelected() {
		return sourceSelected;
	}

	public void setSourceSelected(List<Assoc_Offer_Elements> sourceSelected) {
		this.sourceSelected = sourceSelected;
	}

	public List<Assoc_Offer_Elements> getTarget() {
		return target;
	}

	public void setTarget(List<Assoc_Offer_Elements> target) {
		this.target = target;
	}

	public List<Assoc_Offer_Elements> getTargetSelected() {
		return targetSelected;
	}

	public void setTargetSelected(List<Assoc_Offer_Elements> targetSelected) {
		this.targetSelected = targetSelected;
	}

	public List<Assoc_Offer_Elements> getDelteAssoc() {
		return delteAssoc;
	}

	public void setDelteAssoc(List<Assoc_Offer_Elements> delteAssoc) {
		this.delteAssoc = delteAssoc;
	}

	public Boolean getLangFr() {
		return langFr;
	}

	public void setLangFr(Boolean langFr) {
		this.langFr = langFr;
	}

	public Offers getCl() {
		return cl;
	}

	public void setCl(Offers cl) {
		this.cl = cl;
	}

	public List<Assoc_Offer_Elements> getAllAssocItemsList() {
		return allAssocItemsList;
	}

	public void setAllAssocItemsList(List<Assoc_Offer_Elements> allAssocItemsList) {
		this.allAssocItemsList = allAssocItemsList;
	}

	public List<ServiceElements> getChecklistItemsList() {
		return checklistItemsList;
	}

	public void setChecklistItemsList(List<ServiceElements> checklistItemsList) {
		this.checklistItemsList = checklistItemsList;
	}

	public List<ServiceElements> getAllItemsList() {
		return allItemsList;
	}

	public void setAllItemsList(List<ServiceElements> allItemsList) {
		this.allItemsList = allItemsList;
	}

	public List<ServiceElements> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(List<ServiceElements> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public List<Offers> getAllChecklist() {
		return allChecklist;
	}

	public void setAllChecklist(List<Offers> allChecklist) {
		this.allChecklist = allChecklist;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOfferState() {
		return offerState;
	}

	public void setOfferState(String offerState) {
		this.offerState = offerState;
	}

	public List<FileArchive> getFileList() {
		return fileList;
	}

	public void setFileList(List<FileArchive> fileList) {
		this.fileList = fileList;
	}

	public Integer getDelFile() {
		return delFile;
	}

	public void setDelFile(Integer delFile) {
		this.delFile = delFile;
	}

	public List<ServiceContract> getScontracts() {
		return scontracts;
	}

	public void setScontracts(List<ServiceContract> scontracts) {
		this.scontracts = scontracts;
	}

	public Boolean getOfferExists() {
		return offerExists;
	}

	public void setOfferExists(Boolean offerExists) {
		this.offerExists = offerExists;
	}

	public List<ServiceHistory> getCheckListItems() {
		return checkListItems;
	}

	public void setCheckListItems(List<ServiceHistory> checkListItems) {
		this.checkListItems = checkListItems;
	}

	public Assoc_Offer_Elements getCurrentOfferElement() {
		return currentOfferElement;
	}

	public void setCurrentOfferElement(Assoc_Offer_Elements currentOfferElement) {
		this.currentOfferElement = currentOfferElement;
	}

	public Service getCurrentService() {
		return currentService;
	}

	public void setCurrentService(Service currentService) {
		this.currentService = currentService;
	}

}
