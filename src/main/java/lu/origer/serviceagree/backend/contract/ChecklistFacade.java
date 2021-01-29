package lu.origer.serviceagree.backend.contract;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.omnifaces.util.Faces;
import org.primefaces.component.autocomplete.AutoComplete;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.checklist.Checklist;
import lu.origer.serviceagree.models.checklist.QChecklist;
import lu.origer.serviceagree.models.checklist.QServiceElements;
import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.synch.QServiceHistory;

@Stateless
public class ChecklistFacade extends AbstractFacade<Checklist> {
	
    
    public ChecklistFacade() {
        super(Checklist.class);
    }

    @PersistenceContext(unitName = "ORIGERPU")
    private EntityManager em;


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public Checklist getInitialChecklist(final Integer elementId)
    {
    	Checklist result = null;
    	try
    	{
    		QServiceElements elements = QServiceElements.serviceElements;
    		JPAQuery q = new JPAQuery(this.em);
    		result = q.from(elements).where(elements.id.eq(elementId)).singleResult(elements.checklist);
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    	}    	
    	return result;
    }
    
    public Checklist getChecklistForService(final ServiceElements element, final Integer serviceId)
    {    	
    	Checklist result = null;
    	try
    	{
    		QServiceHistory history = QServiceHistory.serviceHistory;
    		JPAQuery q = new JPAQuery(em);
    		Integer checklistId = q.from(history).where(history.element.id.eq(element.getId()).and(history.service.id.eq(serviceId))).singleResult(history.serviceChecklist);    	    		
    		JPAQuery checklistQuery = new JPAQuery(em);
    		if(checklistId != null)
    		{     
    			QChecklist checklist = QChecklist.checklist;    			
    			    			
    			Checklist checklistResult = checklistQuery.from(checklist).where(checklist.id.eq(checklistId)).singleResult(checklist);
    			if(checklistResult != null)
    			{    			    				
    				result = checklistResult;    				
    			}
    		}
    		else
    		{    			
    			final QServiceElements serviceElement =  QServiceElements.serviceElements;    			
    			result = checklistQuery.from(serviceElement).where(serviceElement.id.eq(element.getId())).singleResult(serviceElement.checklist); 			
    		}
    	}
    	catch(Exception e)
    	{    		    		
    		e.printStackTrace();
    	}
		finally
		{
			if (result == null)
			{					
				result = element.getChecklist();
			}
		}    	
    	return result;
    }
    
    public Checklist findDetached(Integer checklistId){
    	QChecklist c = QChecklist.checklist; 
    	JPAQuery q = new JPAQuery(this.em); 
    	q.from(c);
    	q.where(c.id.eq(checklistId));
    	
    	Checklist e = q.singleResult(c);
    	e.getAssocChecklistEntries();
    	return e;    	
//    	for(Checklist d : q.list(c)){
//    		d.getAssocChecklistEntries();
//    	}
//    	return q.singleResult(c);
    }
    
    public List<Checklist> findAllDetached(){
    	QChecklist c = QChecklist.checklist; 
    	JPAQuery q = new JPAQuery(this.em); 
    	q.from(c);
    	//q.where(c.active.isTrue());
    	
    	for(Checklist d : q.list(c)){
    		d.getAssocChecklistEntries();
    	}
    	return q.list(c);
    }
    
    public List<Checklist> findAllActive(){
    	QChecklist c = QChecklist.checklist; 
    	JPAQuery q = new JPAQuery(this.em); 
    	q.from(c);
    	q.where(c.active.isTrue());
    	return q.list(c);
    }
    
    public Boolean getFKFromItem(Integer fk_checklist_entry, Integer fk_checklist){
		Query q = em.createNativeQuery("select has_fc from assoc_checklist_checklistitems "
				+ "where fk_checklist_entry = ? and fk_checklist = ?  ");
		q.setParameter(1, fk_checklist_entry);
		q.setParameter(2, fk_checklist);
		Boolean res = (Boolean) q.getSingleResult();
		return res;
    }
    
    public Boolean getVKFromItem(Integer fk_checklist_entry, Integer fk_checklist){
		Query q = em.createNativeQuery("select has_vc from assoc_checklist_checklistitems "
				+ "where fk_checklist_entry = ? and fk_checklist = ? ");
		q.setParameter(1, fk_checklist_entry);
		q.setParameter(2, fk_checklist);
		Boolean res = (Boolean) q.getSingleResult();
		return res;
    }
    
    
    public List<Checklist> findAllWithControl(){
    	QChecklist c = QChecklist.checklist; 
    	JPAQuery q = new JPAQuery(this.em); 
    	q.from(c);
    	
    	List<Checklist> list = q.list(c); 
    	
    	
    	for(Checklist cl : list){
//    		for (ChecklistItem item : cl.getChecklistItems()){
//    			item.setViewControl(getVKFromItem(item.getId(), cl.getId() ));
//    			item.setFunctionalControl(getFKFromItem(item.getId(), cl.getId() ));
//    		}
    		
    	}
    	
    	return list;
    	
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
