package lu.origer.serviceagree.backend.contract;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.contract.custom.AddServiceType;
import lu.origer.serviceagree.models.contract.custom.QAddServiceType;

@Stateless
public class AddServiceTypeFacade extends AbstractFacade<AddServiceType> {
	

	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;
	
	public AddServiceTypeFacade(){
		super(AddServiceType.class);
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	public List<AddServiceType> findServiceTypeByServiceContract(ServiceContract sc){
		QAddServiceType st = QAddServiceType.addServiceType; 
		JPAQuery q = new JPAQuery(this.em);
		q.from(st); 
		q.where(st.serviceContract.eq(sc));
		return q.list(st);
		
		
	}
	

}
