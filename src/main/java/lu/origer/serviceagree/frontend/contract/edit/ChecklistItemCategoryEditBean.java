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

import lu.origer.serviceagree.backend.contract.ChecklistItemCategoryFacade;
import lu.origer.serviceagree.frontend.main.BasicFormBean;
import lu.origer.serviceagree.frontend.main.Constants;
import lu.origer.serviceagree.models.checklist.ChecklistItemCategory;

@ManagedBean
@ViewScoped
public class ChecklistItemCategoryEditBean extends BasicFormBean<ChecklistItemCategory> {
	
	private static final long serialVersionUID = 1L;
	
	private static final String LOG_REPORTER = "ChecklistItemCategoryEditBean";
	
	@Inject
	ChecklistItemCategoryFacade checklistItemCategoryFacade; 
	
	private ChecklistItemCategory checklistItemCategory;  

	@Override
	public void save() {
        if (Faces.getContext().getMessageList().isEmpty()) {
            // No Errors occured
            if (checklistItemCategory.getId() != null) {
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
            Faces.redirect ("faces/origer/serveAgree/admin/checklistCategory/checklistCategory_list.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(ChecklistItemCategoryEditBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @PostConstruct
    public void init() {
        Integer id = getApplicationBean().getIdFromURL();
        if (id == null || id == -1) {
        	checklistItemCategory = new ChecklistItemCategory();
        	checklistItemCategory.setActive(true);        	
        } else {
        	checklistItemCategory = checklistItemCategoryFacade.find(id);
        }
    }

	@Override
	protected void create() {
		checklistItemCategory.setCreateDate(new Date());
		checklistItemCategory.setEditDate(new Date());
		checklistItemCategory.setCreateUser(getSessionBean().getCurrentUser().getId());
		checklistItemCategory.setEditUser(getSessionBean().getCurrentUser().getId());
		checklistItemCategoryFacade.create(checklistItemCategory);
        String message = "Neuer Eintrag einer checklistItemCategory [" + checklistItemCategory.getName() + "] wurde angelegt";
        getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.INFO);
	}

	@Override
	protected void update() {
        // Update checklistItemCategory
		checklistItemCategory.setEditUser(getSessionBean().getCurrentUser().getId());
		checklistItemCategory.setEditDate(new Date());		
		checklistItemCategoryFacade.edit(checklistItemCategory);	
	}

	@Override
	protected void checkInput() {
		// TODO Auto-generated method stub
	}

	public ChecklistItemCategory getChecklistItemCategory() {
		return checklistItemCategory;
	}

	public void setChecklistItemCategory(ChecklistItemCategory checklistItemCategory) {
		this.checklistItemCategory = checklistItemCategory;
	}
	

}
