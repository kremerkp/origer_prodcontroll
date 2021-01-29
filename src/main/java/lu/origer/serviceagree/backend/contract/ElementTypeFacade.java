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
import lu.origer.serviceagree.models.contact.types.ElementType;
import lu.origer.serviceagree.models.contact.types.QElementType;

@Stateless
public class ElementTypeFacade extends AbstractFacade<ElementType> {
	
    
    public ElementTypeFacade() {
        super(ElementType.class);
    }
    
    public List<ElementType> findAllActive(){
    	QElementType e = QElementType.elementType; 
    	JPAQuery q = new JPAQuery(this.em);
    	q.from(e);
    	q.where(e.active.isTrue());
    	return q.list(e);
    	
    }

    @PersistenceContext(unitName = "ORIGERPU")
    private EntityManager em;


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    

}
