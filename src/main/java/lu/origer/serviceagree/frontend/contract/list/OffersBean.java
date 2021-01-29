package lu.origer.serviceagree.frontend.contract.list;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Local;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.omnifaces.util.Faces;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lu.origer.serviceagree.backend.contract.AssocOfferItemFacade;
import lu.origer.serviceagree.backend.contract.OfferFacade;
import lu.origer.serviceagree.backend.contract.ServiceContractFacade;
import lu.origer.serviceagree.frontend.main.BasicListBean;
import lu.origer.serviceagree.models.assocs.Assoc_Offer_Elements;
import lu.origer.serviceagree.models.contract.Offers;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.main.FileArchive;
import lu.origer.serviceagree.models.main.Roles;

@SuppressWarnings("rawtypes")
@ManagedBean
@ViewScoped
public class OffersBean extends BasicListBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private StreamedContent file;
	
	private Offers selectedOffer; 
	
	private ServiceContract serviceContract;
	
	private List<Assoc_Offer_Elements> offerElements; 
	
	
	@Inject
	private OfferFacade offerFacade;
	
	@Inject 
	private ServiceContractFacade serviceContractFacade;
	
	@Inject 
	private AssocOfferItemFacade offerItemFacade;

	@Override
	public void filterList() {
		// TODO Auto-generated method stub
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
	

	public Boolean renderStateOk(String state){
//		FacesContext context = FacesContext.getCurrentInstance();
//		String studentId = context.getExternalContext().getRequestParameterMap().get("state");
		if (state.equalsIgnoreCase("ok")){
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean renderStateSend(String state){
//		FacesContext context = FacesContext.getCurrentInstance();
//		String studentId = context.getExternalContext().getRequestParameterMap().get("state");
		if (state.equalsIgnoreCase("gesendet")){
			return true;
		} else {
			return false;
		}
	}
	
	public Boolean renderStateDismissed(String state){
//		FacesContext context = FacesContext.getCurrentInstance();
//		String studentId = context.getExternalContext().getRequestParameterMap().get("state");
		if (state.equalsIgnoreCase("abgelehnt")){
			return true;
		} else {
			return false;
		}
	}
	
	public List<Assoc_Offer_Elements> translateOffer(List<Assoc_Offer_Elements> list){
		for(Assoc_Offer_Elements of : list){
			if(getSessionBean().getUserLocale().equals(Locale.GERMANY)){

			} else {
				if (of.getOfferState().contains("abgel")){
					of.setOfferState("Refusé");
				}							
				if (of.getOfferState().contains("ges")){
					of.setOfferState("En traite.");
				}
			}
		}
		return list;
	}
	
	public void setContract(){
		if(this.selectedOffer != null){
			this.serviceContract = selectedOffer.getServiceContract();
			this.offerElements = offerItemFacade.findTargetItemsForOfferId(this.selectedOffer, this.serviceContract, null);
			this.offerElements = translateOffer(offerElements);
		}
	}

	
	
    @SuppressWarnings("unchecked")
	@PostConstruct
    public void init() {        
        Integer id = getApplicationBean().getIdFromURL();
        Integer scId = getApplicationBean().getContractIdFromUrl();
        Integer custId = getApplicationBean().getCustomerIdFromURL();
        // Alle Angebote
        if (id == null || id == -1) {
        	if (getSessionBean().getIsAdmin()){
        		if(scId == null ){
        			data = offerFacade.findAllOffers();     
        			data = new ArrayList<>(data);
        		} else {
        			data = offerFacade.findAllForServiceContract(scId);
        			this.serviceContract = serviceContractFacade.find(scId);
        			data = new ArrayList<>(data);
        		}
        		if (custId != null){
        			data = offerFacade.offerForCustomers(custId);
        			data = new ArrayList<>(data);        			
        		}
        	} else{
        		data = offerFacade.offerForCustomers(getSessionBean().getCurrentUser());
        		data = new ArrayList<>(data);
        	}
        } else {
        	List<Offers> oList = new ArrayList<>(); 
        	oList.add(offerFacade.find(id));
        	data = oList;
        	data = new ArrayList<>(data);
        }
    }

	@Override
	public String add() {
		return "/origer/serveAgree/admin/offers/offers_edit.xhtml?faces-redirect=true";
	}
	
	public void goToContract(){
		//System.out.println("goToContract" + ((Offers) selectedData ).getServiceContract().getId());
		
		try {
			Faces.redirect("/origer/faces/origer/serveAgree/contract/contract_edit.xhtml?faces-redirect=true&id=" + this.serviceContract.getId());
			//return "origer/serveAgree/contract/contract_edit.xhtml?faces-redirect=true&id=" + ((Offers) selectedData ).getServiceContract().getId();			
		} catch (Exception e) {
			// TODO: handle exception
			//return ""; 
		}
	}
	
	
	public String refreshSite() {
		return "/origer/serveAgree/admin/offers/offers_list.xhtml";
	}
	

	@Override
	public String edit() {
		try {
			return "/origer/serveAgree/admin/offers/offers_edit.xhtml?faces-redirect=true&id=" + selectedOffer.getId();			
		} catch (Exception e) {
			// TODO: handle exception
			return ""; 
		}
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		this.offerFacade.remove(this.selectedOffer);
		data = new ArrayList<>();
		data = offerFacade.findAllOffers();     
		data = new ArrayList<>(data);
	}
	
    public void openOffer(){
		try {
			Faces.redirect("/origer/faces/origer/serveAgree/admin/offers/offers_edit.xhtml?faces-redirect=true&id=" + selectedOffer.getId());			
		} catch (IOException ex) {
			Logger.getLogger(ChecklistItemCategoryBean.class.getName()).log(Level.SEVERE, null, ex);
		}
    }

	public StreamedContent getFile() {
		return file;
	}

	public void setFile(StreamedContent file) {
		this.file = file;
	}

	public Offers getSelectedOffer() {
		return selectedOffer;
	}

	public void setSelectedOffer(Offers selectedOffer) {
		this.selectedOffer = selectedOffer;
	}

	public ServiceContract getServiceContract() {
		return serviceContract;
	}

	public void setServiceContract(ServiceContract serviceContract) {
		this.serviceContract = serviceContract;
	}

	public List<Assoc_Offer_Elements> getOfferElements() {
		return offerElements;
	}

	public void setOfferElements(List<Assoc_Offer_Elements> offerElements) {
		this.offerElements = offerElements;
	}
	


}
