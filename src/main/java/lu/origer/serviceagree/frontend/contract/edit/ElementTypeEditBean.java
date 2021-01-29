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
import lu.origer.serviceagree.frontend.main.BasicFormBean;
import lu.origer.serviceagree.frontend.main.Constants;
import lu.origer.serviceagree.models.contact.types.ElementType;

@ManagedBean
@ViewScoped
public class ElementTypeEditBean extends BasicFormBean<ElementType> {
	
	private static final String LOG_REPORTER = "ElementTypeEditBean";
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	ElementTypeFacade elementTypeFacade; 
	
	private ElementType elementType;  
	
    @PostConstruct
    public void init() {
        Integer id = getApplicationBean().getIdFromURL();
        if (id == null || id == -1) {
        	elementType = new ElementType();
        	elementType.setActive(true);        	
        } else {
        	elementType = elementTypeFacade.find(id);
        }
    }
	

	@Override
	public void save() {
        if (Faces.getContext().getMessageList().isEmpty()) {
            // No Errors occured
            if (elementType.getId() != null) {
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
            Faces.redirect ("faces/origer/serveAgree/admin/elementtype/elementtype_list.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ElementTypeEditBean.class.getName()).log(Level.SEVERE, null, ex);
        }
	}

	@Override
	protected void create() {
		elementType.setCreateDate(new Date());
		elementType.setEditDate(new Date());
		elementType.setCreateUser(getSessionBean().getCurrentUser().getId());
		elementType.setEditUser(getSessionBean().getCurrentUser().getId());
		elementTypeFacade.create(elementType);
        String message = "Neuer Eintrag einer checklistItemCategory [" + elementType.getName() + "] wurde angelegt";
        getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.INFO);
		
	}

	@Override
	protected void update() {
        // Update checklistItemCategory
		elementType.setEditUser(getSessionBean().getCurrentUser().getId());
		elementType.setEditDate(new Date());		
		elementTypeFacade.edit(elementType);	
	}

	@Override
	protected void checkInput() {
		// TODO Auto-generated method stub
		
	}

	public ElementType getElementType() {
		return elementType;
	}

	public void setElementType(ElementType elementType) {
		this.elementType = elementType;
	}
	

}
