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

import lu.origer.serviceagree.backend.contract.AssocChecklistItemFacade;
import lu.origer.serviceagree.backend.contract.ChecklistFacade;
import lu.origer.serviceagree.backend.contract.ChecklistItemFacade;
import lu.origer.serviceagree.frontend.main.ApplicationBean;
import lu.origer.serviceagree.frontend.main.BasicFormBean;
import lu.origer.serviceagree.frontend.main.Constants;
import lu.origer.serviceagree.models.assocs.Assoc_Checklist_Checklistentries;
import lu.origer.serviceagree.models.checklist.Checklist;
import lu.origer.serviceagree.models.checklist.ChecklistItem;

@ManagedBean
@ViewScoped
public class ChecklistAssocEditBean extends BasicFormBean<Assoc_Checklist_Checklistentries> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String LOG_REPORTER = "ChecklistEditBean";

	@Inject
	ChecklistFacade checklistFacade;

	@Inject
	ChecklistItemFacade checklistItemFacade;
	
	@Inject
	AssocChecklistItemFacade assocChecklistItemFacade;
	
	private Boolean checklistActiv; 
	
	private String checklistName; 

	
	
	private List<Assoc_Checklist_Checklistentries> assocChecklist;

//	private List<ChecklistItem> source;
//	private List<ChecklistItem> target;
	
	private List<Assoc_Checklist_Checklistentries> source;
	private List<Assoc_Checklist_Checklistentries> assignedAssoc;
	private List<Assoc_Checklist_Checklistentries> sourceSelected;
	private List<Assoc_Checklist_Checklistentries> target;
	private List<Assoc_Checklist_Checklistentries> targetSelected;
	private List<Assoc_Checklist_Checklistentries> delteAssoc  = new ArrayList<>();
	
	
	public Boolean langFr = false; 
	
	private Checklist cl; 

	//private DualListModel<Assoc_Checklist_Checklistentries> assocChecklistItemsPickList;
	
	private List<Assoc_Checklist_Checklistentries> allAssocItemsList;

	private List<ChecklistItem> checklistItemsList;
	private List<ChecklistItem> allItemsList;
	private List<ChecklistItem> selectedItems;
	private List<Checklist> allChecklist;
	private Integer id = null;

	@PostConstruct
	public void init() {
		id = getApplicationBean().getIdFromURL();
		this.langFr = ApplicationBean.getlangFromURLIsFrench();
		
		allChecklist = checklistFacade.findAllActive();
		if (id == null || id == -1) {
			assocChecklist = new ArrayList<>();
			cl = new Checklist(); 
			source = assocChecklistItemFacade.findAllItmesForChecklistId(cl);
			target = new ArrayList<>();
			// assocChecklistItemsPickList = new DualListModel<>(source2, target2);
			allItemsList = checklistItemFacade.findAll();
			allAssocItemsList = assocChecklistItemFacade.findAllItmesForChecklistId(null);
		} else {
			cl = checklistFacade.find(id);
			this.checklistActiv = cl.isActive(); 
			this.checklistName = cl.getName(); 
			assocChecklist = assocChecklistItemFacade.findByChecklistId(id);
			target = assocChecklistItemFacade.findByChecklistId(cl.getId());
			source = assocChecklistItemFacade.findTargetItemsForChecklistId(cl);
			assignedAssoc = assocChecklistItemFacade.findByChecklistId(cl.getId());
		}
	}

	@Override
	public void save() {
		if (Faces.getContext().getMessageList().isEmpty()) {
			// No Errors occured
			if (this.id != null) {
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
	
	public void setTargetList(){
		List<Assoc_Checklist_Checklistentries> tmpList = new ArrayList<>(); 
		tmpList.addAll(this.target); 
		tmpList.addAll(this.sourceSelected); 
		this.target = new ArrayList<>();
		this.target.addAll(tmpList);
		tmpList = new ArrayList<>(); 
		tmpList.addAll(source);
		tmpList.removeAll(this.sourceSelected);
		this.sourceSelected = null; 
		this.targetSelected = null;
		this.source = new ArrayList<>(); 
		this.source.addAll(tmpList);
	}
	
	public void removeTargetSeleceted(){
		for(Assoc_Checklist_Checklistentries as: this.targetSelected){
			assocChecklistItemFacade.remove(as);
		}
	}
	
	public void setSourceList(){
		List<Assoc_Checklist_Checklistentries> tmpList = new ArrayList<>(); 
		tmpList.addAll(this.target); 
		tmpList.removeAll(this.targetSelected); 
		//removeTargetSeleceted();
		delteAssoc.addAll(targetSelected);
		this.target = new ArrayList<>();
		this.target.addAll(tmpList);
		tmpList = new ArrayList<>(); 
		tmpList.addAll(source);
		tmpList.addAll(this.targetSelected);
		this.sourceSelected = null; 
		this.targetSelected = null;
		this.source = new ArrayList<>(); 
		this.source.addAll(tmpList);		
	}

	@Override
	protected void create() { 
		createChecklist();
		for(Assoc_Checklist_Checklistentries it : target){
			assocChecklistItemFacade.create(it);
		}

	}
	
	public void createChecklist(){
		this.cl.setName(this.checklistName);
		this.cl.setActive(this.checklistActiv);
		this.cl.setCreateDate(new Date());
		this.cl.setCreateUser(getSessionBean().getCurrentUser().getId());
		this.cl.setEditDate(new Date());
		this.cl.setEditUser(getSessionBean().getCurrentUser().getId());
		checklistFacade.persist(cl);		
	}
	
	public void updateChecklist(){
		this.cl.setName(this.checklistName);
		this.cl.setActive(this.checklistActiv);
		this.cl.setEditDate(new Date());
		this.cl.setEditUser(getSessionBean().getCurrentUser().getId());
		checklistFacade.edit(cl);		
	}

	@Override
	protected void update() {
		updateChecklist();
		
		List<Assoc_Checklist_Checklistentries> tmp = new ArrayList<>(); 
		
		//tmp.addAll(assignedAssoc); 
		tmp.addAll(this.delteAssoc);
		// edit
		for(Assoc_Checklist_Checklistentries it : this.target){
			assocChecklistItemFacade.edit(it);
		}
		//remove 
		for(Assoc_Checklist_Checklistentries it : tmp){
			assocChecklistItemFacade.remove(it);
		}		
	}

	@Override
	protected void checkInput() {
		// TODO Auto-generated method stub

	}

//	public Checklist getChecklist() {
//		return checklist;
//	}
//
//	public void setChecklist(Checklist checklist) {
//		this.checklist = checklist;
//	}

	public List<ChecklistItem> getChecklistItemsList() {
		return checklistItemsList;
	}

	public void setChecklistItemsList(List<ChecklistItem> checklistItemsList) {
		this.checklistItemsList = checklistItemsList;
	}

//	public DualListModel<ChecklistItem> getChecklistItemsPickList() {
//		return checklistItemsPickList;
//	}
//
//	public void setChecklistItemsPickList(DualListModel<ChecklistItem> checklistItemsPickList) {
//		this.checklistItemsPickList = checklistItemsPickList;
//	}

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

//	public List<ChecklistItem> getSource() {
//		return source;
//	}
//
//	public void setSource(List<ChecklistItem> source) {
//		this.source = source;
//	}
//
//	public List<ChecklistItem> getTarget() {
//		return target;
//	}
//
//	public void setTarget(List<ChecklistItem> target) {
//		this.target = target;
//	}


	

	public Boolean getChecklistActiv() {
		return checklistActiv;
	}

	public List<Assoc_Checklist_Checklistentries> getSource() {
		return source;
	}

	public void setSource(List<Assoc_Checklist_Checklistentries> source) {
		this.source = source;
	}

	public List<Assoc_Checklist_Checklistentries> getTarget() {
		return target;
	}

	public void setTarget(List<Assoc_Checklist_Checklistentries> target) {
		this.target = target;
	}

	public void setChecklistActiv(Boolean checklistActiv) {
		this.checklistActiv = checklistActiv;
	}

	public String getChecklistName() {
		return checklistName;
	}

	public void setChecklistName(String checklistName) {
		this.checklistName = checklistName;
	}

	public List<Assoc_Checklist_Checklistentries> getSourceSelected() {
		return sourceSelected;
	}

	public void setSourceSelected(List<Assoc_Checklist_Checklistentries> sourceSelected) {
		this.sourceSelected = sourceSelected;
	}

	public List<Assoc_Checklist_Checklistentries> getTargetSelected() {
		return targetSelected;
	}

	public void setTargetSelected(List<Assoc_Checklist_Checklistentries> targetSelected) {
		this.targetSelected = targetSelected;
	}

	public List<Assoc_Checklist_Checklistentries> getAssignedAssoc() {
		return assignedAssoc;
	}

	public void setAssignedAssoc(List<Assoc_Checklist_Checklistentries> assignedAssoc) {
		this.assignedAssoc = assignedAssoc;
	}

	public List<Assoc_Checklist_Checklistentries> getDelteAssoc() {
		return delteAssoc;
	}

	public void setDelteAssoc(List<Assoc_Checklist_Checklistentries> delteAssoc) {
		this.delteAssoc = delteAssoc;
	}
	

	
	
}
