/*******************************************************************************
 * Copyright (c) 2016 Eye-T S.àr.l. .
 *******************************************************************************/

package lu.origer.serviceagree.frontend.main;

import javax.annotation.PostConstruct;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;
import org.primefaces.component.autocomplete.AutoComplete;

import lu.origer.serviceagree.backend.contact.ContactFacade;
import lu.origer.serviceagree.backend.contract.ServiceElementFacade;
import lu.origer.serviceagree.backend.contract.ServiceFacade;
import lu.origer.serviceagree.backend.synch.SynchJobsFacade;
import lu.origer.serviceagree.backend.synch.TimeRecordingHistoryFacade;
import lu.origer.serviceagree.models.contact.Person;
import lu.origer.serviceagree.models.main.Users;

/**
 * @author Kai Kremer
 * @Version 1.0
 */
@Named
@ViewScoped
public class TileNavigationBean {
	@Inject
	ServiceElementFacade serviceElementFacade;
	
	@Inject
	ServiceFacade serviceFacade;
	
	@Inject
	TimeRecordingHistoryFacade timeRecordingHistoryFacade;
	
	@Inject
	SynchJobsFacade synchJobFacade;
	
	private Users customer; 
	
	public TileNavigationBean() {
		super();
	}

	public String homeTile() {
		return "/origer/serveAgree/navigation/tile-start.xhtml";
	}

	public String homeSearch() {
		return "/origer/serveAgree/contract/search/search_edit.xhtml?faces-redirect=true";
	}
	
	public String homeMaterial(){
		return "/origer/serveAgree/customer/offerReporting_list.xhtml?faces-redirect=true";
	}

	public String homeServiceContract() {
		return "/origer/serveAgree/contract/contract_edit.xhtml?faces-redirect=true";
	}
	
	public String homeServiceContractAdmin() {
		return "/origer/serveAgree/admin/service_contract/contract_list.xhtml?faces-redirect=true";
	}
	
	public String homeServiceAdmin() {
		return "/origer/serveAgree/admin/service/services_list.xhtml?faces-redirect=true";
	}
	
	public String homeOfferCustomerReporting() {
		// WORKS!
		if(customer != null){
			return "/origer/serveAgree/customer/offerReporting_list.xhtml?faces-redirect=true&customerID=" +customer.getId();
		} else {
			return "/origer/serveAgree/customer/offerReporting_list.xhtml?faces-redirect=true";			
		}
		
	}
	
	public String homeServiceFilesCustomerReporting() {
		if (customer != null){
			return "/origer/serveAgree/customer/serviceFiles_list.xhtml?faces-redirect=true&customerID=" +customer.getId();
		} else {
			return "/origer/serveAgree/customer/serviceFiles_list.xhtml?faces-redirect=true";			
		}
		
	}
	public String homeServiceFilesCustomerHistory() {
		if (customer != null){
			return "/origer/serveAgree/customer/history_list.xhtml?faces-redirect=true&customerID=" +customer.getId();
		} else {
			return "/origer/serveAgree/customer/history_list.xhtml?faces-redirect=true";			
		}
		
	}
	
	public String homeServiceStateCustomerReporting() {
		if(customer != null){
			return "/origer/serveAgree/customer/serviceStateReporting_list.xhtml?faces-redirect=true&customerID=" +customer.getId();
		} else {
			return "/origer/serveAgree/customer/serviceStateReporting_list.xhtml?faces-redirect=true";			
		}

		
	}
	
	
	public String homeServiceProofCustomerReporting() {
		return "/origer/serveAgree/customer/serviceProofReporting_list.xhtml?faces-redirect=true";
	}
	
	
	public String homeServiceTypeAdmin() {
		return "/origer/serveAgree/admin/service_types/service_types_list.xhtml?faces-redirect=true";
	}
	
	public String homeBuildingSiteAdmin() {
		return "/origer/serveAgree/admin/building_site/building_site_list.xhtml?faces-redirect=true";
	}
	
	public String homeServiceContractEdit() {
		return "/origer/serveAgree/contract/contract_edit.xhtml?faces-redirect=true&editMode=1";
	}

	public String homeAdmin() {
		return "/origer/serveAgree/admin/admin.xhtml?faces-redirect=true";
	}
	
	public String homeCustomer() {
		return "/origer/serveAgree/customer/customer.xhtml?faces-redirect=true";
	}

	public String homeCustomerAdmin() {
		return "/origer/serveAgree/admin/customer/customer_list.xhtml?faces-redirect=true";
	}

	public String homeTechnicianAdmin() {
		return "/origer/serveAgree/admin/technician/technician_list.xhtml?faces-redirect=true";
	}
	
	public String homeCountryAdmin() {
		return "/origer/serveAgree/admin/country/country_list.xhtml?faces-redirect=true";
	}

	public String homeAdminChecklist() {
		return "/origer/serveAgree/admin/checklist/checklist_list.xhtml?faces-redirect=true";
	}

	public String homeInvoices() {
		return "/origer/serveAgree/admin/invoices/invoice_list.xhtml?faces-redirect=true";
	}

	public String homeOffers() {
		return "/origer/serveAgree/admin/offers/offers_list.xhtml?faces-redirect=true";
	}

	public String homeAdminChecklistItems() {
		return "/origer/serveAgree/admin/checklistItems/checklistItems_list.xhtml?faces-redirect=true";
	}

	public String homeAdminChecklistCategory() {
		return "/origer/serveAgree/admin/checklistCategory/checklistCategory_list.xhtml?faces-redirect=true";
	}

	public String homeAdminElements() {
		return "/origer/serveAgree/admin/elements/elements_list.xhtml?faces-redirect=true";
	}

	public String homeAdminUser() {
		return "/origer/serveAgree/admin/user/user_list.xhtml?faces-redirect=true";
	}

	public String redirectServeAgreeStart() {
		return "/origer/serveAgree/navigation/tile-start.xhtml";
	}

	public String homeAdminContractAdmin() {
		return "/origer/serveAgree/admin/adminContract.xhtml?faces-redirect=true";
	}

	public String homeAdminChecklistAdmin() {
		return "/origer/serveAgree/admin/adminChecklist.xhtml?faces-redirect=true";
	}

	public String homeAdminContacts() {
		return "/origer/serveAgree/admin/adminContacts.xhtml?faces-redirect=true";
	}
	
	public String openOffersExists(){
		Boolean exists = serviceFacade.openOffersExists();
		if (exists){
			return "newBarocdeStyle";
		}else {
			return "noNewBarocdeStyle";
		} 
	}
	
	public String openSynchExists(){
		Boolean exists = false;
		try
		{
			exists =  synchJobFacade.openSynchJobs();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		if (exists){
			return "red";
		}else {
			return "blue";
		}				
	}
	
	public String openBillableTimesExists(){
		Boolean exists = timeRecordingHistoryFacade.billableTimeExists();
		if (exists){
			return "newBarocdeStyle";
		}else {
			return "noNewBarocdeStyle";
		}		
	}
	
	public String newBarcodesExists(){
		Boolean exists = serviceElementFacade.printNewBarcodes();
		if (exists){
			return "newBarocdeStyle";
		}else {
			return "noNewBarocdeStyle";
		}
		
		
	}

	@PostConstruct
	private void init() {
	}

	public Users getCustomer() {
		return customer;
	}

	public void setCustomer(Users customer) {
		this.customer = customer;
	}




}
