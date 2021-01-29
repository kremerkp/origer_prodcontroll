package lu.origer.serviceagree.backend.contract;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.omnifaces.util.Faces;
import org.primefaces.component.autocomplete.AutoComplete;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.checklist.ChecklistItemCategory;
import lu.origer.serviceagree.models.checklist.QChecklistItemCategory;

@Stateless
public class ChecklistItemCategoryFacade extends AbstractFacade<ChecklistItemCategory>  {


    public ChecklistItemCategoryFacade() {
        super(ChecklistItemCategory.class);
    }

    @PersistenceContext(unitName = "ORIGERPU")
    private EntityManager em;


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    private List<ChecklistItemCategory> findByName(String query){
    	QChecklistItemCategory c = QChecklistItemCategory.checklistItemCategory; 
    	JPAQuery q = new JPAQuery(this.em); 
    	q.from(c); 
    	q.where(c.active.isTrue());
    	q.where(c.name.containsIgnoreCase(query)); 
    	return q.list(c);
    }
    
	/**
	 * Autocomplete für ChecklistItemCategory
	 * @param query
	 * @return
	 */
	public List<ChecklistItemCategory> completeChecklistItemCategory(String query){
		ValueExpression exp = Faces.getApplication().getExpressionFactory().createValueExpression(
				Faces.getContext().getELContext(), "#{it.name}" , String.class);
		AutoComplete autoComplete = (AutoComplete) UIComponent.getCurrentComponent(Faces.getContext());
		autoComplete.setValueExpression("itemLabel", exp);
		List<ChecklistItemCategory> itemCatList = this.findByName(query);
		
		if (itemCatList == null) {
			List<ChecklistItemCategory> c = new ArrayList<>();
			return c;
		}
		return itemCatList;
	}
}
