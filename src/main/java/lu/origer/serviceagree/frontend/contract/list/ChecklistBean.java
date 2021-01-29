package lu.origer.serviceagree.frontend.contract.list;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.omnifaces.util.Faces;

import lu.origer.serviceagree.backend.contract.AssocChecklistItemFacade;
import lu.origer.serviceagree.backend.contract.ChecklistFacade;
import lu.origer.serviceagree.frontend.main.ApplicationBean;
import lu.origer.serviceagree.frontend.main.BasicListBean;
import lu.origer.serviceagree.models.checklist.Checklist;

@SuppressWarnings("rawtypes")
@ManagedBean
@ViewScoped
public class ChecklistBean  extends BasicListBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	ChecklistFacade checklistFacade; 
	
	@Inject
	AssocChecklistItemFacade assFacade; 
	
	private List<Checklist> checklistData; 
	
	private Checklist selectedChecklist; 
	
	private List<Checklist> filteredChecklist; 
	
	public Boolean langFr = false; 
	
	public Boolean changeSKFK = true; 
	

	@Override
	public void filterList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String add() {
		return "/origer/serveAgree/admin/checklist/checklist_edit.xhtml?faces-redirect=true" + "&lang=" + (this.langFr ? "fr" : "de");
	}
	
	public String addAs() {
		return "/origer/serveAgree/admin/checklist/assoc_edit.xhtml?faces-redirect=true" + "&lang=" + (this.langFr ? "fr" : "de");
	}
	

	@Override
	public String edit() {
		try {
			return "/origer/serveAgree/admin/checklist/checklist_edit.xhtml?faces-redirect=true&id=" + selectedChecklist.getId() + "&lang=" + (this.langFr ? "fr" : "de");			
		} catch (Exception e) {
			// TODO: handle exception
			return ""; 
		}
	}
	
	public String editAs() {
		try {
			return "/origer/serveAgree/admin/checklist/assoc_edit.xhtml?faces-redirect=true&id=" + selectedChecklist.getId() + "&lang=" + (this.langFr ? "fr" : "de");			
		} catch (Exception e) {
			// TODO: handle exception
			return ""; 
		}
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}
	
	public void updateChecklist(){
		checklistFacade.edit(this.selectedChecklist);
	}
	
	public void setLangFR(){
		this.langFr = true; 
	}
	
	public void setLangDE(){
		this.langFr = false;
	}
	
	public String getButtonLabel(){
		if (this.langFr){
			if (changeSKFK){
				return "FK/SK modifier";
			} else {
				return "FK/SK enregistrer";
			}
		} else {
			if (changeSKFK){
				return "FK/SK bearbeiten";
			} else {
				return "FK/SK speichern";
			}			
		}
	}
	
    @PostConstruct
    public void init() {
    	langFr = getApplicationBean().getlangFromURLIsFrench();
        checklistData = checklistFacade.findAllDetached();
        checklistData = new ArrayList<>(checklistData);
    }
    
    public void openChecklist(){
		try {
			//Faces.redirect("/origer/faces/origer/serveAgree/admin/checklist/checklist_edit.xhtml?faces-redirect=true&id=" + selectedChecklist.getId() + "&lang=" + (this.langFr ? "fr" : "de"));
			Faces.redirect("/origer/faces/origer/serveAgree/admin/checklist/assoc_edit.xhtml?faces-redirect=true&id=" + selectedChecklist.getId() + "&lang=" + (this.langFr ? "fr" : "de"));
		} catch (IOException ex) {
			Logger.getLogger(ChecklistItemCategoryBean.class.getName()).log(Level.SEVERE, null, ex);
		}
    }
    
    public void switchSK(){
    	this.changeSKFK = !changeSKFK;
    	checklistFacade.edit(this.selectedChecklist);
    }

	public Boolean getLangFr() {
		return langFr;
	}

	public void setLangFr(Boolean langFr) {
		this.langFr = langFr;
	}

	public Boolean getChangeSKFK() {
		return changeSKFK;
	}

	public void setChangeSKFK(Boolean changeSKFK) {
		this.changeSKFK = changeSKFK;
	}


	public Checklist getSelectedChecklist() {
		return selectedChecklist;
	} 

	public void setSelectedChecklist(Checklist selectedChecklist) {
		this.selectedChecklist = selectedChecklist;
	}

	public List<Checklist> getFilteredChecklist() {
		return filteredChecklist;
	}

	public void setFilteredChecklist(List<Checklist> filteredChecklist) {
		this.filteredChecklist = filteredChecklist;
	}

	public void setChecklistData(List<Checklist> checklistData) {
		this.checklistData = checklistData;
	}

	public List<Checklist> getChecklistData() {
		return checklistData;
	}
	
	
	

}
