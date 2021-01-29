package lu.origer.serviceagree.frontend.contract.edit;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.omnifaces.util.Faces;

import lu.origer.serviceagree.backend.contract.DinTypeFacade;
import lu.origer.serviceagree.backend.contract.DirectionTypeFacade;
import lu.origer.serviceagree.backend.contract.ElementTypeFacade;
import lu.origer.serviceagree.backend.contract.FrontTypeFacade;
import lu.origer.serviceagree.frontend.main.BasicFormBean;
import lu.origer.serviceagree.frontend.main.Constants;
import lu.origer.serviceagree.models.contact.types.DinType;
import lu.origer.serviceagree.models.contact.types.Direction;
import lu.origer.serviceagree.models.contact.types.ElementType;
import lu.origer.serviceagree.models.contact.types.Front;

@ManagedBean
@ViewScoped
public class DinTypeEditBean extends BasicFormBean<Front> {
	
	private static final String LOG_REPORTER = "FrontTypeEditBean";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	DinTypeFacade dinTypeFacade; 
	
	private DinType dinType;  
	
    @PostConstruct
    public void init() {
        Integer id = getApplicationBean().getIdFromURL();
        if (id == null || id == -1) {
        	dinType = new DinType();
        	dinType.setActive(true);        	
        } else {
        	dinType = dinTypeFacade.find(id);
        }
    }
	

	@Override
	public void save() {
        if (Faces.getContext().getMessageList().isEmpty()) {
            // No Errors occured
            if (dinType.getId() != null) {
                update();
            } else {
                create();
            }
        }
		
	}
	
	public void saveAndClose(){
    	//save();
        try {
            save();
            Faces.redirect ("faces/origer/serveAgree/admin/dintype/dintype_list.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(DinTypeEditBean.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

	@Override
	protected void create() {
		dinType.setCreateDate(new Date());
		dinType.setEditDate(new Date());
		dinType.setCreateUser(getSessionBean().getCurrentUser().getId());
		dinType.setEditUser(getSessionBean().getCurrentUser().getId());
		dinTypeFacade.create(dinType);
        String message = "Neuer Eintrag einer dirction [" + dinType.getName() + "] wurde angelegt";
        getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.INFO);
		
	}

	@Override
	protected void update() {
        // Update checklistItemCategory
		dinType.setEditUser(getSessionBean().getCurrentUser().getId());
		dinType.setEditDate(new Date());		
		dinTypeFacade.edit(dinType);	
	}

	@Override
	protected void checkInput() {
		// TODO Auto-generated method stub
		
	}


	public DinType getDinType() {
		return dinType;
	}


	public void setDinType(DinType dinType) {
		this.dinType = dinType;
	}



}
