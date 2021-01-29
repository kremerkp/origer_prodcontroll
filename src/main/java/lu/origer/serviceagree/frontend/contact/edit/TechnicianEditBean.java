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
public class TechnicianEditBean extends BasicFormBean<Person> {
	
	private static final long serialVersionUID = 1L;
	
	private static final String LOG_REPORTER = "ChecklistItemCategoryEditBean";
	
	@Inject
	PersonFacade personFacade;
	@Inject
	PersonTypeFacade personTypeFacade;
	@Inject 
	CountryFacade countryFacade; 
	 
	private MappedPerson tech;
	
	private List<Country> countryList;

	@Override
	public void save() {
        if (Faces.getContext().getMessageList().isEmpty()) {
            // No Errors occured
            if (tech.getId() != null) {
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
            Faces.redirect ("faces/origer/serveAgree/admin/technician/technician_list.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(TechnicianEditBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public List<Country> findCountryList(){
    	return this.countryFacade.findAllACtive();
    }
    
    @PostConstruct
    public void init() {
        Integer id = getApplicationBean().getIdFromURL();
        if (id == null || id == -1) {
        	tech = new MappedPerson();  
        	tech.setType(personTypeFacade.find(PersonTypeFacade.TECHNICIAN));
        } else {
        	tech = personFacade.findMappedPersonById(id);
        	countryList = findCountryList(); 
        }
    }
   

	@Override
	protected void create() {
		tech.setType(personTypeFacade.find(PersonTypeFacade.TECHNICIAN));
		personFacade.createPersonFromCustomer(tech, getSessionBean().getCurrentUser());
        String message = "Neuer Eintrag eines Kunden [" + tech.getLastname() + " " +  tech.getFirstname() + "] wurde angelegt";
        getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.INFO);
	}

	@Override
	protected void update() {
        // Update checklistItemCategory		
		personFacade.editPersonFromTechnician(tech, getSessionBean().getCurrentUser());	
	}

	@Override
	protected void checkInput() {
		// TODO Auto-generated method stub
	}

	public MappedPerson getCustomer() {
		return tech;
	}

	public void setCustomer(MappedPerson customer) {
		this.tech = customer;
	}

	public List<Country> getCountryList() {
		return countryList;
	}

	public void setCountryList(List<Country> countryList) {
		this.countryList = countryList;
	}

	
}
