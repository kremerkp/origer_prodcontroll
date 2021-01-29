package lu.origer.serviceagree.frontend.contract.edit;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.swing.plaf.synth.SynthSeparatorUI;

import org.omnifaces.util.Faces;

import lu.origer.serviceagree.backend.contract.ChecklistItemCategoryFacade;
import lu.origer.serviceagree.backend.contract.ChecklistItemFacade;
import lu.origer.serviceagree.frontend.main.BasicFormBean;
import lu.origer.serviceagree.frontend.main.Constants;
import lu.origer.serviceagree.models.checklist.ChecklistItem;
import lu.origer.serviceagree.models.checklist.ChecklistItemCategory;

@ManagedBean
@ViewScoped
public class ChecklistItemEditBean extends BasicFormBean<ChecklistItem> {

	private static final long serialVersionUID = 1L;

	private static final String LOG_REPORTER = "ChecklistItemEditBean";

	@Inject
	ChecklistItemFacade checklistItemFacade;
	@Inject
	ChecklistItemCategoryFacade checklistItemCategoryFacade;

	private ChecklistItemCategory checklistItemCategory;

	private ChecklistItem checklistItem;

	@Override
	public void save() {
		if (Faces.getContext().getMessageList().isEmpty()) {
			// No Errors occured
			if (checklistItem.getId() != null) {
				update();
			} else {
				create();
			}
		}

	}

	public void saveAndClose() {
		try {
			save();
			Faces.redirect("faces/origer/serveAgree/admin/checklistItems/checklistItems_list.xhtml");
		} catch (IOException ex) {
			Logger.getLogger(ChecklistItemEditBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@PostConstruct
	public void init() {
		Integer id = getApplicationBean().getIdFromURL();
		if (id == null || id == -1) {
			checklistItem = new ChecklistItem();
			checklistItem.setActive(true);
		} else {
			checklistItem = checklistItemFacade.find(id);
			checklistItemCategory = checklistItem.getCheckListItemCategory();
		}
	}

	@Override
	protected void create() {
		checklistItem.setCreateDate(new Date());
		checklistItem.setEditDate(new Date());
		checklistItem.setCreateUser(getSessionBean().getCurrentUser().getId());
		checklistItem.setEditUser(getSessionBean().getCurrentUser().getId());
		if (this.checklistItemCategory != null ){
			this.checklistItem.setCheckListItemCategory(this.checklistItemCategory);			
			checklistItemFacade.create(checklistItem);
			String message = "Neuer Eintrag eines checklistItems:  [" + checklistItem.getName() + "] wurde angelegt";
			getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.INFO);
		} else{
			String message = "Checklistitem wurde nicht angelegt, Kategorie darf nicht null sein: ";
			getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.ERROR);			
		}
	}

	@Override
	protected void update() {
		checklistItem.setEditUser(getSessionBean().getCurrentUser().getId());
		checklistItem.setEditDate(new Date());
		if (this.checklistItemCategory != null ){
			this.checklistItem.setCheckListItemCategory(this.checklistItemCategory);			
			checklistItemFacade.edit(checklistItem);
			String message = "Neuer Eintrag eines checklistItems:  [" + checklistItem.getName() + "] wurde angelegt";
			getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.INFO);
		} else{
			String message = "Checklistitem wurde nicht angelegt, Kategorie darf nicht null sein: ";
			getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.ERROR);			
		}
		

	}

	@Override
	protected void checkInput() {
		// TODO Auto-generated method stub

	}

	public List<ChecklistItemCategory> completeItemCategory(String query) {
		return checklistItemCategoryFacade.completeChecklistItemCategory(query);
	}

	public ChecklistItem getChecklistItem() {
		return checklistItem;
	}

	public void setChecklistItem(ChecklistItem checklistItem) {
		this.checklistItem = checklistItem;
	}

	public ChecklistItemCategory getChecklistItemCategory() {
		return checklistItemCategory;
	}

	public void setChecklistItemCategory(ChecklistItemCategory checklistItemCategory) {
		this.checklistItemCategory = checklistItemCategory;
	}

}
