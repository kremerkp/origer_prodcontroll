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

import lu.origer.serviceagree.backend.contract.ElementTypeFacade;
import lu.origer.serviceagree.backend.contract.FrontTypeFacade;
import lu.origer.serviceagree.frontend.main.BasicFormBean;
import lu.origer.serviceagree.frontend.main.Constants;
import lu.origer.serviceagree.models.contact.types.ElementType;
import lu.origer.serviceagree.models.contact.types.Front;

@ManagedBean
@ViewScoped
public class FrontTypeEditBean extends BasicFormBean<Front> {
	
	private static final String LOG_REPORTER = "FrontTypeEditBean";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	FrontTypeFacade frontTypeFacade; 
	
	private Front frontType;  
	
    @PostConstruct
    public void init() {
        Integer id = getApplicationBean().getIdFromURL();
        if (id == null || id == -1) {
        	frontType = new Front();
        	frontType.setActive(true);        	
        } else {
        	frontType = frontTypeFacade.find(id);
        }
    }
	

	@Override
	public void save() {
        if (Faces.getContext().getMessageList().isEmpty()) {
            // No Errors occured
            if (frontType.getId() != null) {
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
            Faces.redirect ("faces/origer/serveAgree/admin/front/front_list.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(FrontTypeEditBean.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

	@Override
	protected void create() {
		frontType.setCreateDate(new Date());
		frontType.setEditDate(new Date());
		frontType.setCreateUser(getSessionBean().getCurrentUser().getId());
		frontType.setEditUser(getSessionBean().getCurrentUser().getId());
		frontTypeFacade.create(frontType);
        String message = "Neuer Eintrag einer Front [" + frontType.getName() + "] wurde angelegt";
        getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.INFO);
		
	}

	@Override
	protected void update() {
        // Update checklistItemCategory
		frontType.setEditUser(getSessionBean().getCurrentUser().getId());
		frontType.setEditDate(new Date());		
		frontTypeFacade.edit(frontType);	
	}

	@Override
	protected void checkInput() {
		// TODO Auto-generated method stub
		
	}


	public Front getFrontType() {
		return frontType;
	}


	public void setFrontType(Front frontType) {
		this.frontType = frontType;
	}



}
