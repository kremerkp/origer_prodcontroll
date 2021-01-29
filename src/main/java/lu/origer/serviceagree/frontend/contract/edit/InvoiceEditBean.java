package lu.origer.serviceagree.frontend.contract.edit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
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

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.omnifaces.util.Faces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import lu.origer.serviceagree.backend.contract.FileArchiveFacade;
import lu.origer.serviceagree.backend.contract.InvoiceFacade;
import lu.origer.serviceagree.backend.contract.OfferFacade;
import lu.origer.serviceagree.backend.contract.ServiceContractFacade;
import lu.origer.serviceagree.backend.contract.ServiceElementFacade;
import lu.origer.serviceagree.backend.contract.ServiceFacade;
import lu.origer.serviceagree.frontend.main.ApplicationBean;
import lu.origer.serviceagree.frontend.main.BasicFormBean;
import lu.origer.serviceagree.frontend.main.Constants;
import lu.origer.serviceagree.models.contract.Invoice;
import lu.origer.serviceagree.models.contract.Offers;
import lu.origer.serviceagree.models.contract.Service;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.main.FileArchive;

@ManagedBean
@ViewScoped
public class InvoiceEditBean extends BasicFormBean<Invoice> {
	
	private static final long serialVersionUID = 1L;
	
	private static final String LOG_REPORTER = "ChecklistItemCategoryEditBean";
	
	@Inject
	InvoiceFacade invoiceFacade; 
	
	@Inject 
	ServiceFacade serviceFacade; 
	
	@Inject 
	OfferFacade offerFacade; 
	
	@Inject 
	FileArchiveFacade fileArchiveFacade;
	
	@Inject 
	ServiceElementFacade serviceElementFacade;
	
	@Inject 
	ServiceContractFacade serviceContractFacade;
	
	private Invoice invoice;
	
	private ServiceContract serviceContract; 
	

	private List<FileArchive> fileList; 
	
	private List<FileArchive> fileListNew;
	
	
	private StreamedContent file;
	
	private Integer openFile; 
	
	private Integer delFile; 
	
	private Offers offer; 
	
	private List<FileArchive> files; 

	@Override
	public void save() {
        if (Faces.getContext().getMessageList().isEmpty()) {
            // No Errors occured
            if (invoice.getId() != null) {
                update();
            } else {
                create();
            }
        }
		
	}
	
    public void saveAndClose() {
    	//save();
        try {
            save();
            Faces.redirect ("faces/origer/serveAgree/admin/invoices/invoice_list.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(InvoiceEditBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
	public List<Service> completeService(String query) {
		return serviceFacade.completeService(query);
	}
	
	public List<ServiceContract> completeServiceContract(String query) {
		return serviceContractFacade.completeServiceContract(query);
	}
	
	public List<Offers> completeOffer(String query) {
		return offerFacade.completeOffer(query);
	}
    
    @PostConstruct
    public void init() {
        Integer id = getApplicationBean().getIdFromURL();
        Integer contractId = getApplicationBean().getContractIdFromUrl(); 
        
        if(contractId != null && contractId != -1){
        	this.serviceContract = serviceContractFacade.find(contractId);
        }
        
        if (id == null || id == -1) {
        	invoice = new Invoice();
        	invoice.setActive(true);        	
        	fileList = new ArrayList<>();
        } else {
        	invoice = invoiceFacade.find(id);
        	this.serviceContract = invoice.getServiceContract();
        	fileList = fileArchiveFacade.findFilesForInvoice(invoice);
        }
    }
   

	@Override
	protected void create() {
		invoice.setCreateDate(new Date());
		invoice.setEditDate(new Date());
		invoice.setCreateUser(getSessionBean().getCurrentUser().getId());
		invoice.setEditUser(getSessionBean().getCurrentUser().getId());
		invoice.setServiceContract(serviceContract);
		invoice.setFiles(this.fileList);
		invoiceFacade.create(invoice);
        String message = "Neuer Eintrag einer Rechnung [" + invoice.getName() + "] wurde angelegt";
        getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.INFO);
	}

	@Override
	protected void update() {
        // Update checklistItemCategory
		invoice.setEditUser(getSessionBean().getCurrentUser().getId());
		invoice.setEditDate(new Date());	
		invoice.setFiles(this.fileList);
		if (this.serviceContract != null){
			invoice.setServiceContract(serviceContract);
		}
		invoiceFacade.edit(invoice);	
	}
	

	public Boolean doesFileNameAllreadyExist(String fileName){
		Boolean exists = false;
		for (FileArchive f : fileArchiveFacade.findFilesForInvoice(this.invoice)){
			if(f.getName().equals(fileName)){
				exists = true;
			}
		}
		return exists;
	}
	

	public void actionDelFile(){
		try {
			String url = fileArchiveFacade.find(this.delFile).getUrl();
			// refresh DB 
			fileArchiveFacade.remove(fileArchiveFacade.find(this.delFile));
			// refresh list 
        	this.fileList = new ArrayList<>(); 
        	this.fileList.addAll(fileArchiveFacade.findFilesForInvoice(this.invoice));
			// refresh FileSystem
			File tmpFile = new File(url);
	 		if(tmpFile.delete()){
    			System.out.println(tmpFile.getName() + " is deleted!");
    			
    		}else{
    			System.out.println("Delete operation is failed.");
    		}
		} catch (Exception e) {
			System.out.println("File does not exists: " +  e.getMessage());
		}
		
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
	
	public void actionOpenFile() throws FileNotFoundException{
		FileArchive ff = fileArchiveFacade.find(this.openFile);
		String url = getApplicationBean().getPathToFileArchiv() + "/" + ff.getSubfolder() + "/" + ff.getName();
		
	    File f = new File(url);
	    InputStream input = new FileInputStream(f);
	    ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
	    setFile(new DefaultStreamedContent(input, externalContext.getMimeType(f.getName()), f.getName()));
	    System.out.println("PREP = " + this.file.getName());
				
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
			f.setInvoice(this.invoice);
			f.setName(fileNameNew);
			f.setSignature(false);
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
	protected void checkInput() {
		// TODO Auto-generated method stub
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public ServiceContract getServiceContract() {
		return serviceContract;
	}

	public void setServiceContract(ServiceContract serviceContract) {
		this.serviceContract = serviceContract;
	}

	public Offers getOffer() {
		return offer;
	}

	public void setOffer(Offers offer) {
		this.offer = offer;
	}

	public List<FileArchive> getFiles() {
		return files;
	}

	public void setFiles(List<FileArchive> files) {
		this.files = files;
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

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
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

	
	public ServiceFacade getServiceFacade()
	{
		return this.serviceFacade;
	}
}
