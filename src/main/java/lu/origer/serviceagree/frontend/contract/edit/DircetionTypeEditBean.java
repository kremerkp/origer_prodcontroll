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

import lu.origer.serviceagree.backend.contract.DirectionTypeFacade;
import lu.origer.serviceagree.backend.contract.ElementTypeFacade;
import lu.origer.serviceagree.backend.contract.FrontTypeFacade;
import lu.origer.serviceagree.frontend.main.BasicFormBean;
import lu.origer.serviceagree.frontend.main.Constants;
import lu.origer.serviceagree.models.contact.types.Direction;
import lu.origer.serviceagree.models.contact.types.ElementType;
import lu.origer.serviceagree.models.contact.types.Front;

@ManagedBean
@ViewScoped
public class DircetionTypeEditBean extends BasicFormBean<Front> {
	
	private static final String LOG_REPORTER = "FrontTypeEditBean";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	DirectionTypeFacade directionTypeFacade; 
	
	private Direction directionType;  
	
    @PostConstruct
    public void init() {
        Integer id = getApplicationBean().getIdFromURL();
        if (id == null || id == -1) {
        	directionType = new Direction();
        	directionType.setActive(true);        	
        } else {
        	directionType = directionTypeFacade.find(id);
        }
    }
	

	@Override
	public void save() {
        if (Faces.getContext().getMessageList().isEmpty()) {
            // No Errors occured
            if (directionType.getId() != null) {
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
            Faces.redirect ("faces/origer/serveAgree/admin/direction/direction_list.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(DircetionTypeEditBean.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

	@Override
	protected void create() {
		directionType.setCreateDate(new Date());
		directionType.setEditDate(new Date());
		directionType.setCreateUser(getSessionBean().getCurrentUser().getId());
		directionType.setEditUser(getSessionBean().getCurrentUser().getId());
		directionTypeFacade.create(directionType);
        String message = "Neuer Eintrag einer dirction [" + directionType.getName() + "] wurde angelegt";
        getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.INFO);
		
	}

	@Override
	protected void update() {
        // Update checklistItemCategory
		directionType.setEditUser(getSessionBean().getCurrentUser().getId());
		directionType.setEditDate(new Date());		
		directionTypeFacade.edit(directionType);	
	}

	@Override
	protected void checkInput() {
		// TODO Auto-generated method stub
		
	}


	public Direction getDirectionType() {
		return directionType;
	}


	public void setDirectionType(Direction directionType) {
		this.directionType = directionType;
	}


}
