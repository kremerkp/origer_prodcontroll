package lu.origer.serviceagree.backend.contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.contract.QServiceType;
import lu.origer.serviceagree.models.contract.ServiceType;

@Stateless
public class ServiceTypeFacade extends AbstractFacade<ServiceType> {
		
	
	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;
	
	public ServiceTypeFacade(){  
		super(ServiceType.class);
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	public List<SelectItem> getServiceTypeListItems(){
		List<SelectItem> serviceTypeList =  new ArrayList<>();
		for (ServiceType st : findAllActive()){
			serviceTypeList.add(new SelectItem(st, st.getName()));
		}
		return serviceTypeList;
	}
	
	public HashMap<String, String> getServiceTypeListMap(){
		HashMap<String, String> it = new HashMap<>();
		
		for (ServiceType st : findAllActive()){
			it.put(st.getName(), st.getName());
		}
		return it;
	}
	
	public Boolean serviceTypeReferenceExists(Integer typeID){
		Query q = em.createNativeQuery("select count(*) cnt from service_elements where fk_element_type = ?");
		q.setParameter(1, typeID);
		Long res = (Long) q.getSingleResult();
		if (res != null && res > 0){
			return true;
		}
		return false;
	}
	
	public ServiceType findByName(String name){
		try {
			QServiceType st = QServiceType.serviceType; 
			JPAQuery q = new JPAQuery(this.em);
			q.from(st);
			q.where(st.name.eq(name)); 
			return q.singleResult(st); 
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<ServiceType> findAllServiceTypes(){
		QServiceType st = QServiceType.serviceType;  
		JPAQuery q = new JPAQuery(this.em); 
		q.from(st); 
		return q.list(st); 
	}
	
	public List<ServiceType> findAllActive(){
		QServiceType st = QServiceType.serviceType;  
		JPAQuery q = new JPAQuery(this.em); 
		q.from(st); 
		q.where(st.active.isTrue());
		return q.list(st); 
	}
	


}
