package lu.origer.serviceagree.backend.contract;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.checklist.ChecklistItem;
import lu.origer.serviceagree.models.checklist.QChecklistItem;
import lu.origer.serviceagree.models.synch.QServiceHistory;
import lu.origer.serviceagree.models.synch.ServiceHistory;

@Stateless
public class ChecklistItemFacade extends AbstractFacade<ChecklistItem>  {
	
	public static final Integer REGIE_TIME = -1;
	public static final Integer CUSTOMER_COMPLAINT = -2; 
	
    
    public ChecklistItemFacade() {
        super(ChecklistItem.class);
    }

    @PersistenceContext(unitName = "ORIGERPU")
    private EntityManager em;


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    

    
    public List<ChecklistItem> findAllWithoutRegie(){
    	QChecklistItem c = QChecklistItem.checklistItem; 
    	JPAQuery q = new JPAQuery(this.em); 
    	q.from(c); 
    	q.where(c.id.goe(0));
		return q.list(c);
    }
    
    public List<ChecklistItem> findByChecklist(Integer checklistId){
    	QChecklistItem c = QChecklistItem.checklistItem; 
    	JPAQuery q = new JPAQuery(this.em);
    	q.from(c); 
    	return null;
    	
    }
    
    
    public List<ChecklistItem> findAll(){
    	QChecklistItem c = QChecklistItem.checklistItem; 
    	JPAQuery q = new JPAQuery(this.em); 
    	q.from(c); 
    	q.where(c.id.ne(ChecklistItemFacade.REGIE_TIME));
		return q.list(c);
    }    
    
    public List<ServiceHistory> findRepairItems(final Integer serviceId, final Integer elementId, final Number[] excludeIds)
    {
    	QServiceHistory history = QServiceHistory.serviceHistory;
    	JPAQuery query = new JPAQuery(em);
    	return query.from(history)
    			.where(history.service.id.eq(serviceId)
    					.and(history.element.id.eq(elementId)
    					.and(history.checkedAsRepaired.isTrue()    					
    					.and(history.id.notIn(excludeIds))))).list(history);
    }
    
    public List<ServiceHistory> findUnrepairedItems(final Integer serviceId, final Integer elementId, final Number[] excludeIds)
    {
    	QServiceHistory history = QServiceHistory.serviceHistory;
    	JPAQuery query = new JPAQuery(em);
    	return query.from(history)
    			.where(history.service.id.eq(serviceId)
    					.and(history.element.id.eq(elementId)
    					.and(history.checkedAsRepaired.isTrue()
    					.and(history.endTime.isNotNull()
    					.and(history.id.notIn(excludeIds)))))).list(history);
    }
}
