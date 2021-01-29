package lu.origer.serviceagree.frontend.contact.edit;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.omnifaces.util.Faces;

import lu.origer.serviceagree.backend.contact.CountryFacade;
import lu.origer.serviceagree.backend.contact.PersonFacade;
import lu.origer.serviceagree.backend.contact.PersonTypeFacade;
import lu.origer.serviceagree.frontend.main.BasicFormBean;
import lu.origer.serviceagree.frontend.main.Constants;
import lu.origer.serviceagree.models.contact.Country;
import lu.origer.serviceagree.models.contact.MappedPerson;
import lu.origer.serviceagree.models.contact.Person;

@ManagedBean
@ViewScoped
public class CustomerEditBean extends BasicFormBean<Person> {
	
	private static final long serialVersionUID = 1L;
	
	private static final String LOG_REPORTER = "ChecklistItemCategoryEditBean";
	
	@Inject
	PersonFacade personFacade;
	@Inject
	PersonTypeFacade personTypeFacade;
	@Inject 
	CountryFacade countryFacade; 
	 
	private MappedPerson customer;
	
	private List<Country> countryList;

	@Override
	public void save() {
        if (Faces.getContext().getMessageList().isEmpty()) {
            // No Errors occured
            if (customer.getId() != null) {
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
            Faces.redirect ("faces/origer/serveAgree/admin/customer/customer_list.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(CustomerEditBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public List<Country> findCountryList(){
    	return this.countryFacade.findAllACtive();
    }
    
    @PostConstruct
    public void init() {
        Integer id = getApplicationBean().getIdFromURL();
        if (id == null || id == -1) {
        	customer = new MappedPerson();  
        	customer.setType(personTypeFacade.find(PersonTypeFacade.CUSTOMER));
        } else {
        	customer = personFacade.findMappedPersonById(id);
        }
        countryList = findCountryList();
    }
   

	@Override
	protected void create() {
		customer.setType(personTypeFacade.find(PersonTypeFacade.CUSTOMER));
		personFacade.createPersonFromCustomer(customer, getSessionBean().getCurrentUser());
        String message = "Neuer Eintrag eines Kunden [" + customer.getLastname() + " " +  customer.getFirstname() + "] wurde angelegt";
        getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.INFO);
	}

	@Override
	protected void update() {
        // Update checklistItemCategory		
		personFacade.editPersonFromCustomer(customer, getSessionBean().getCurrentUser());	
	}

	@Override
	protected void checkInput() {
		// TODO Auto-generated method stub
	}

	public MappedPerson getCustomer() {
		return customer;
	}

	public void setCustomer(MappedPerson customer) {
		this.customer = customer;
	}

	public List<Country> getCountryList() {
		return countryList;
	}

	public void setCountryList(List<Country> countryList) {
		this.countryList = countryList;
	}

	
}
