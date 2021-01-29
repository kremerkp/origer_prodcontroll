package lu.origer.serviceagree.backend.contract;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mysema.query.jpa.impl.JPAQuery;
import com.sun.jersey.core.spi.scanning.ScannerListener;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.assocs.Assoc_Checklist_Checklistentries;
import lu.origer.serviceagree.models.assocs.QAssoc_Checklist_Checklistentries;
import lu.origer.serviceagree.models.checklist.Checklist;
import lu.origer.serviceagree.models.checklist.ChecklistItem;
import lu.origer.serviceagree.models.contract.BuildingSite;
import lu.origer.serviceagree.models.contract.QBuildingSite;

@Stateless
public class AssocChecklistItemFacade extends AbstractFacade<Assoc_Checklist_Checklistentries> {
	
	
	@Inject
	ChecklistItemFacade itemFacade; 
    
    public AssocChecklistItemFacade() {
        super(Assoc_Checklist_Checklistentries.class);
    }

    @PersistenceContext(unitName = "ORIGERPU")
    private EntityManager em;


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
	public List<Assoc_Checklist_Checklistentries> findByChecklistId(Integer checklistId){
		QAssoc_Checklist_Checklistentries as = QAssoc_Checklist_Checklistentries.assoc_Checklist_Checklistentries;
		JPAQuery q = new JPAQuery(this.em); 
		q.from(as); 
		q.where(as.checkList.id.eq(checklistId));
		return q.list(as); 
	}
	
	public List<Assoc_Checklist_Checklistentries> findTargetItemsForChecklistId(Checklist checklist){
		QAssoc_Checklist_Checklistentries as = QAssoc_Checklist_Checklistentries.assoc_Checklist_Checklistentries;
		JPAQuery q = new JPAQuery(this.em); 
		q.from(as); 
		q.where(as.checkList.id.eq(checklist.getId()));
		
		List<Assoc_Checklist_Checklistentries> srcList = q.list(as);
		
		List<ChecklistItem> itemList = itemFacade.findAllWithoutRegie();
		List<Assoc_Checklist_Checklistentries> resList = new ArrayList<>();
		
		for(ChecklistItem it : itemList ){
			Assoc_Checklist_Checklistentries a = new Assoc_Checklist_Checklistentries(); 
			a.setCheckList(checklist);
			a.setChecklistItem(it);
			a.setFc(true);
			a.setVc(true);
			Boolean entryAllreadyExists = false;
			for(Assoc_Checklist_Checklistentries s: srcList){
				if (s.getChecklistItem().getId().equals(it.getId())){
					entryAllreadyExists = true;
				}
			}
			if (!entryAllreadyExists){				
				resList.add(a); 
			}
		}
		
		
		List<Assoc_Checklist_Checklistentries> sourceCopy = new ArrayList<>(); 
		sourceCopy.addAll(resList); 
		sourceCopy.removeAll(srcList); 
		srcList =sourceCopy; 
		
		return srcList;		
	}
	
	public List<Assoc_Checklist_Checklistentries> findAllItmesForChecklistId(Checklist checklist){
		List<ChecklistItem> itemList = itemFacade.findAllWithoutRegie();
		List<Assoc_Checklist_Checklistentries> resList = new ArrayList<>(); 
		for(ChecklistItem it : itemList ){
			Assoc_Checklist_Checklistentries a = new Assoc_Checklist_Checklistentries(); 
			a.setCheckList(checklist);
			a.setChecklistItem(it);
			a.setFc(true);
			a.setVc(true);
			resList.add(a);
		}

		return resList; 
	}
	
	
	
}
