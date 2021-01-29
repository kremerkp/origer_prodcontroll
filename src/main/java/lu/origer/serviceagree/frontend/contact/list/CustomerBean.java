package lu.origer.serviceagree.frontend.contact.list;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.omnifaces.util.Faces;

import lu.origer.serviceagree.backend.contact.PersonFacade;
import lu.origer.serviceagree.backend.contract.InvoiceFacade;
import lu.origer.serviceagree.frontend.main.BasicListBean;
import lu.origer.serviceagree.models.contact.MappedPerson;
import lu.origer.serviceagree.models.contact.Person;
import lu.origer.serviceagree.models.contract.Invoice;

@SuppressWarnings("rawtypes")
@ManagedBean
@ViewScoped
public class CustomerBean extends BasicListBean{

	/***
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private PersonFacade personFacade;

	@Override
	public void filterList() {
		// TODO Auto-generated method stub
		
	}
	
    @PostConstruct
    public void init() {
        data = personFacade.findAllCustomers();
    }

	@Override
	public String add() {
		return "/origer/serveAgree/admin/customer/customer_edit.xhtml?faces-redirect=true";
	}

	@Override
	public String edit() {
		try {
			return "/origer/serveAgree/admin/customer/customer_edit.xhtml?faces-redirect=true&id=" + ((MappedPerson) selectedData ).getId();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("test");
			return ""; 
		}
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}
	
    public void openCustomer(){
		try {
			Faces.redirect("/origer/faces/origer/serveAgree/admin/customer/customer_edit.xhtml?faces-redirect=true&id=" + ((MappedPerson) selectedData ).getId());			
		} catch (IOException ex) {
			Logger.getLogger(CustomerBean.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
	
	

}
