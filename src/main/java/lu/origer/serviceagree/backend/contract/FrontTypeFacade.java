package lu.origer.serviceagree.backend.contract;

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
import lu.origer.serviceagree.models.checklist.Checklist;
import lu.origer.serviceagree.models.checklist.QChecklist;
import lu.origer.serviceagree.models.contact.Person;
import lu.origer.serviceagree.models.contact.types.Front;
import lu.origer.serviceagree.models.contact.types.QFront;

@Stateless
public class FrontTypeFacade extends AbstractFacade<Front> {
	
    
    public FrontTypeFacade() {
        super(Front.class);
    }

    @PersistenceContext(unitName = "ORIGERPU")
    private EntityManager em;


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public List<Front> findAllActiveFront(){
    	QFront f = QFront.front; 
    	JPAQuery q = new JPAQuery(this.em);
    	q.from(f); 
    	q.where(f.active.isTrue());
    	return q.list(f);
    	
    }
    
    public List<Checklist> findAllActive(){
    	QChecklist c = QChecklist.checklist; 
    	JPAQuery q = new JPAQuery(this.em); 
    	q.from(c);
    	q.where(c.active.isTrue());
    	return q.list(c);
    }
    
    public List<Checklist> findByName(String find){
    	QChecklist c = QChecklist.checklist; 
    	JPAQuery q = new JPAQuery(this.em); 
    	q.from(c);
    	q.where(c.name.containsIgnoreCase(find));
    	return q.list(c);
    }
    
    
	/** 
	 * @param query
	 * @return
	 */
	public List<Checklist> completeChecklist(String query) {
		ValueExpression exp = Faces.getApplication().getExpressionFactory().createValueExpression(
				Faces.getContext().getELContext(), "#{c.name}", String.class);
		AutoComplete autoComplete = (AutoComplete) UIComponent.getCurrentComponent(Faces.getContext());
		autoComplete.setValueExpression("itemLabel", exp);
		List<Checklist> c = this.findByName(query);
		return c;
	}


}
