package lu.origer.serviceagree.frontend.contract.list;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.omnifaces.util.Faces;

import lu.origer.serviceagree.backend.contract.InvoiceFacade;
import lu.origer.serviceagree.backend.contract.ServiceContractFacade;
import lu.origer.serviceagree.frontend.main.BasicListBean;
import lu.origer.serviceagree.models.contract.Invoice;
import lu.origer.serviceagree.models.contract.ServiceContract;

@SuppressWarnings("rawtypes")
@ManagedBean
@ViewScoped
public class InvoicesBean extends BasicListBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private InvoiceFacade invoiceFacade;
	@Inject 
	ServiceContractFacade serviceContractFacade;
	
	private Invoice selectedInvoice;
	
	private ServiceContract serviceContract;

	@Override
	public void filterList() {
		// TODO Auto-generated method stub
		
	}
	
	public Boolean renderStatePayed(String state){
//		FacesContext context = FacesContext.getCurrentInstance();
//		String studentId = context.getExternalContext().getRequestParameterMap().get("state");
		if (state.equalsIgnoreCase("bezahlt")){
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
	
	public void updateSelectedData(){
		if(this.selectedInvoice != null){
			this.serviceContract = selectedInvoice.getServiceContract();
		}
	}
	
    @PostConstruct
    public void init() {
    	Integer scId = getApplicationBean().getContractIdFromUrl();
    	
    	
    	if(scId != null){
    		data = invoiceFacade.findAllForServiceContract(scId);
    		this.serviceContract = serviceContractFacade.find(scId);  
    		data = new ArrayList<>(data);
    	} else {
    		data = invoiceFacade.findAll();
    		data = new ArrayList<>(data);
    	}

    }
    
	public String refreshSite() {
		return "/origer/serveAgree/admin/invoices/invoice_list.xhtml";
	}

	@Override
	public String add() {
		return "/origer/serveAgree/admin/invoices/invoice_edit.xhtml?faces-redirect=true";
	}
	
	public void goToContract(){
		try {
			Faces.redirect("/origer/faces/origer/serveAgree/contract/contract_edit.xhtml?faces-redirect=true&id=" + this.serviceContract.getId());
			//return "origer/serveAgree/contract/contract_edit.xhtml?faces-redirect=true&id=" + ((Offers) selectedData ).getServiceContract().getId();			
		} catch (Exception e) {
			// TODO: handle exception
			//return ""; 
		}
	}

	@Override
	public String edit() {
		try {
			return "/origer/serveAgree/admin/invoices/invoice_edit.xhtml?faces-redirect=true&id=" + this.selectedInvoice.getId();			
		} catch (Exception e) {
			// TODO: handle exception
			return ""; 
		}
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}
	
    public void openInvoice(){
		try {
			Faces.redirect("/origer/faces/origer/serveAgree/admin/invoices/invoice_edit.xhtml?faces-redirect=true&id=" +selectedInvoice.getId());			
		} catch (IOException ex) {
			Logger.getLogger(ChecklistItemCategoryBean.class.getName()).log(Level.SEVERE, null, ex);
		}
    }

	public Invoice getSelectedInvoice() {
		return selectedInvoice;
	}

	public void setSelectedInvoice(Invoice selectedInvoice) {
		this.selectedInvoice = selectedInvoice;
	}

	public ServiceContract getServiceContract() {
		return serviceContract;
	}

	public void setServiceContract(ServiceContract serviceContract) {
		this.serviceContract = serviceContract;
	}
	
	

}
