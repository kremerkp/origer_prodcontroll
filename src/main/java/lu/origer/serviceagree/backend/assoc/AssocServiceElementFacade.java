package lu.origer.serviceagree.backend.assoc;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.assocs.Assoc_Service_Elements;
import lu.origer.serviceagree.models.assocs.QAssoc_Service_Elements;
import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contract.Service;
import lu.origer.serviceagree.models.contract.ServiceContract;
@Stateless
public class AssocServiceElementFacade extends AbstractFacade<Assoc_Service_Elements>  {
	
    public AssocServiceElementFacade() {
        super(Assoc_Service_Elements.class);
    }
    
    public List<Assoc_Service_Elements> getAllForElements(List<ServiceElements> sList){
    	
    	List<Integer> sListNumber = new ArrayList<>();
    	
    	for(ServiceElements se : sList){
    		sListNumber.add(se.getId());
    	}
    	
    	QAssoc_Service_Elements ase = QAssoc_Service_Elements.assoc_Service_Elements; 
    	JPAQuery q = new JPAQuery(this.em);
    	q.from(ase); 
    	q.where(ase.id.service.in(sListNumber));
    	
    	return q.list(ase);
    	
    }
    
    /**
     * Prüft ob das ServiceElement nochmal in einer Assocziation vorkomment
     * @param sc
     * @return
     */
    public Boolean elementExistsInAssoc(ServiceContract sc, ServiceElements serviceElement ){
    	
    	Boolean exists = false;
    	// Liste allser Service innerhalb des Vertrags
    	List<Service> sList = sc.getServices();
    	List<Integer> idList = new ArrayList<>();
    	// Liste aller Elemente 
    	QAssoc_Service_Elements seList = QAssoc_Service_Elements.assoc_Service_Elements; 
    	JPAQuery q = new JPAQuery(this.em);
    	q.from(seList);
    	
    	if(sList != null && sList.size() > 0 ){
    		for(Service s : sList){
    			idList.add(s.getId());
    		}    		
    		q.where(seList.id.service.in(idList));
    	}
    	// alle Elemente enthalten
    	List<Assoc_Service_Elements> all = q.list(seList);
    	// exisitert ElementID in Liste
    	for(Assoc_Service_Elements ase : all){
    		if (ase.getId().getElements().equals(serviceElement.getId())){
    			exists = true;
    		}
    	}
    	return exists;
    }

    @PersistenceContext(unitName = "ORIGERPU")
    private EntityManager em;
    

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

}
