package lu.origer.serviceagree.frontend.contract.list;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.omnifaces.util.Faces;

import lu.origer.serviceagree.backend.contract.ChecklistItemCategoryFacade;
import lu.origer.serviceagree.backend.contract.DirectionTypeFacade;
import lu.origer.serviceagree.frontend.main.BasicListBean;
import lu.origer.serviceagree.models.checklist.ChecklistItemCategory;
import lu.origer.serviceagree.models.contact.types.Direction;

@SuppressWarnings("rawtypes")
@ManagedBean
@ViewScoped
public class DirectionTypeBean extends BasicListBean{
	
	@Inject
	DirectionTypeFacade directionTypeFacade; 
	
	private Direction direction; 

	@Override
	public void filterList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String add() {
		return "/origer/serveAgree/admin/direction/direction_edit.xhtml?faces-redirect=true";
	}

	@Override
	public String edit() {
		try {
			return "/origer/serveAgree/admin/direction/direction_edit.xhtml?faces-redirect=true&id=" + ((Direction) selectedData ).getId();			
		} catch (Exception e) {
			// TODO: handle exception
			return ""; 
		}
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}
	
    @PostConstruct
    public void init() {
        data = directionTypeFacade.findAll();
        data = new ArrayList<>(data);
    }
    
    public void openDirectionType(){
		try {
			Faces.redirect("/origer/faces/origer/serveAgree/admin/direction/direction_edit.xhtml?faces-redirect=true&id=" + ((Direction) selectedData ).getId());			
		} catch (IOException ex) {
			Logger.getLogger(DirectionTypeBean.class.getName()).log(Level.SEVERE, null, ex);
		}
    	
    }
	

}
