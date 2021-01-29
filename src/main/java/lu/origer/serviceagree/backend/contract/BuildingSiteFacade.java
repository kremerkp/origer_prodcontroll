package lu.origer.serviceagree.backend.contract;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.contact.Person;
import lu.origer.serviceagree.models.contract.BuildingSite;
import lu.origer.serviceagree.models.contract.QBuildingSite;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.main.Users;

@Stateless
public class BuildingSiteFacade extends AbstractFacade<BuildingSite> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7978156737040087172L;
	
	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;
	
	public BuildingSiteFacade(){
		super(BuildingSite.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	public BuildingSite findByCode(String code){
		QBuildingSite bs = QBuildingSite.buildingSite; 
		JPAQuery q = new JPAQuery(this.em); 
		q.from(bs); 
		q.where(bs.code.containsIgnoreCase(code));
		return q.singleResult(bs); 
	}
	
	public Boolean checkIfUnique(String code){
		QBuildingSite bs = QBuildingSite.buildingSite;
		JPAQuery q = new JPAQuery(this.em);
		q.from(bs);
		q.where(bs.code.equalsIgnoreCase(code));
		List<BuildingSite> bsList = q.list(bs);
		return bsList == null; 
	}
	
	
	public List<Integer> findBuildingSiteForCustomer(Users u){
		List<Integer> bsList = new ArrayList<>();
		for(ServiceContract sc : u.getServiceContractList()){
			if (sc.getBuildingSite() != null){
				bsList.add(sc.getBuildingSite().getId());
			}
		}
		return bsList;
	}
	public List<BuildingSite> findForAutocomplete(String code){
		QBuildingSite bs = QBuildingSite.buildingSite; 
		JPAQuery q = new JPAQuery(this.em); 
		q.from(bs); 
		q.where(bs.code.containsIgnoreCase(code).or(bs.name.containsIgnoreCase(code)));
		q.where(bs.active.isTrue());
		List<BuildingSite> list = q.list(bs);
		
		return list; 
	}
	
	public List<BuildingSite> findAllActive(){
		QBuildingSite bs = QBuildingSite.buildingSite; 
		JPAQuery q = new JPAQuery(this.em); 
		q.from(bs); 
		q.where(bs.active.isTrue()); 
		return q.list(bs); 
	}
	
	

}
