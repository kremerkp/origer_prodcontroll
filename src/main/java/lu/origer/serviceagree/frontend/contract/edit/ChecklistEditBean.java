package lu.origer.serviceagree.frontend.contract.edit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.omnifaces.util.Faces;
import org.primefaces.model.DualListModel;

import lu.origer.serviceagree.backend.contract.ChecklistFacade;
import lu.origer.serviceagree.backend.contract.ChecklistItemFacade;
import lu.origer.serviceagree.frontend.main.BasicFormBean;
import lu.origer.serviceagree.frontend.main.Constants;
import lu.origer.serviceagree.models.assocs.Assoc_Checklist_Checklistentries;
import lu.origer.serviceagree.models.checklist.Checklist;
import lu.origer.serviceagree.models.checklist.ChecklistItem;

@ManagedBean
@ViewScoped
public class ChecklistEditBean extends BasicFormBean<Checklist> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String LOG_REPORTER = "ChecklistEditBean";

	@Inject
	ChecklistFacade checklistFacade;

	@Inject
	ChecklistItemFacade checklistItemFacade;

	private Checklist checklist;

	private List<ChecklistItem> source;
	private List<ChecklistItem> target;
	
	private List<Assoc_Checklist_Checklistentries> source2;
	private List<Assoc_Checklist_Checklistentries> target2;
	
	
	public Boolean langFr = false; 

	private DualListModel<ChecklistItem> checklistItemsPickList;

	private List<ChecklistItem> checklistItemsList;
	private List<ChecklistItem> allItemsList;
	private List<ChecklistItem> selectedItems;
	private List<Checklist> allChecklist;

	@PostConstruct
	public void init() {
		Integer id = getApplicationBean().getIdFromURL();
		this.langFr = getApplicationBean().getlangFromURLIsFrench();
		
		allChecklist = checklistFacade.findAllActive();
		if (id == null || id == -1) {
			checklist = new Checklist();
			checklist.setActive(true);
			source = checklistItemFacade.findAll();
			target = new ArrayList<>();
			checklistItemsPickList = new DualListModel<>(source, target);
			allItemsList = checklistItemFacade.findAll();
		} else {
			checklist = checklistFacade.find(id);
			target = new ArrayList<>();
			target2 = checklist.getAssocChecklistEntries();
			
			for(Assoc_Checklist_Checklistentries e : target2){
				target.add(e.getChecklistItem());
			}
			
			//target = checklist.getChecklistItems();
			
			
			source = checklistItemFacade.findAll();
			List<ChecklistItem> sourceCopy = new ArrayList<>(); 
			sourceCopy.addAll(source); 
			sourceCopy.removeAll(target); 
			source =sourceCopy; 
			checklistItemsPickList = new DualListModel<>(source, target);
			allItemsList = checklistItemFacade.findAll();
		}
	}

	@Override
	public void save() {
		if (Faces.getContext().getMessageList().isEmpty()) {
			// No Errors occured
			if (checklist.getId() != null) {
				update();
			} else {
				create();
			}
		}

	}

	public void saveAndClose() {
		try {
			save();
			Faces.redirect("faces/origer/serveAgree/admin/checklist/checklist_list.xhtml?faces-redirect=true" + "&lang=" + (this.langFr ? "fr" : "de"));
		} catch (IOException ex) {
			Logger.getLogger(ChecklistItemCategoryEditBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	protected void create() {
		
		if (checklistItemsPickList.getTarget()!= null && checklistItemsPickList.getTarget().size() > 0) {
			//checklist.setChecklistItems(checklistItemsPickList.getTarget());
			checklist.setCreateDate(new Date());
			checklist.setEditDate(new Date());
			checklist.setCreateUser(getSessionBean().getCurrentUser().getId());
			checklist.setEditUser(getSessionBean().getCurrentUser().getId());
			checklistFacade.create(checklist);
			String message = "Neuer Eintrag einer checklistItemCategory [" + checklist.getName() + "] wurde angelegt";
			getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.INFO);
		} else {
			String message = "Keine Neuer Eintrag angelegt. Keine Items ausgewählt";
			getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.ERROR);
		}

	}

	@Override
	protected void update() {
		// Update checklistItemCategory
		checklist.setEditUser(getSessionBean().getCurrentUser().getId());
		checklist.setEditDate(new Date());
		if (checklistItemsPickList.getTarget() != null && checklistItemsPickList.getTarget().size() > 0) {
			//checklist.setChecklistItems(checklistItemsPickList.getTarget());
		}
		checklistFacade.edit(checklist);
	}

	@Override
	protected void checkInput() {
		// TODO Auto-generated method stub

	}

	public Checklist getChecklist() {
		return checklist;
	}

	public void setChecklist(Checklist checklist) {
		this.checklist = checklist;
	}

	public List<ChecklistItem> getChecklistItemsList() {
		return checklistItemsList;
	}

	public void setChecklistItemsList(List<ChecklistItem> checklistItemsList) {
		this.checklistItemsList = checklistItemsList;
	}

	public DualListModel<ChecklistItem> getChecklistItemsPickList() {
		return checklistItemsPickList;
	}

	public void setChecklistItemsPickList(DualListModel<ChecklistItem> checklistItemsPickList) {
		this.checklistItemsPickList = checklistItemsPickList;
	}

	public List<ChecklistItem> getAllItemsList() {
		return allItemsList;
	}

	public void setAllItemsList(List<ChecklistItem> allItemsList) {
		this.allItemsList = allItemsList;
	}

	public List<Checklist> getAllChecklist() {
		return allChecklist;
	}

	public void setAllChecklist(List<Checklist> allChecklist) {
		this.allChecklist = allChecklist;
	}

	public Boolean getLangFr() {
		return langFr;
	}

	public void setLangFr(Boolean langFr) {
		this.langFr = langFr;
	}

	public List<ChecklistItem> getSelectedItems() {
		return selectedItems;
	}

	public void setSelectedItems(List<ChecklistItem> selectedItems) {
		this.selectedItems = selectedItems;
	}

	public List<ChecklistItem> getSource() {
		return source;
	}

	public void setSource(List<ChecklistItem> source) {
		this.source = source;
	}

	public List<ChecklistItem> getTarget() {
		return target;
	}

	public void setTarget(List<ChecklistItem> target) {
		this.target = target;
	}

	public List<Assoc_Checklist_Checklistentries> getSource2() {
		return source2;
	}

	public void setSource2(List<Assoc_Checklist_Checklistentries> source2) {
		this.source2 = source2;
	}

	public List<Assoc_Checklist_Checklistentries> getTarget2() {
		return target2;
	}

	public void setTarget2(List<Assoc_Checklist_Checklistentries> target2) {
		this.target2 = target2;
	}

	
	
	
	
}
