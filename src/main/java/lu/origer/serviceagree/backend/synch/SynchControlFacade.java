package lu.origer.serviceagree.backend.synch;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.synch.QSynchControl;
import lu.origer.serviceagree.models.synch.SynchControl;

@Stateless
public class SynchControlFacade extends AbstractFacade<SynchControl> {
	

	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;
	
	public SynchControlFacade(){
		super(SynchControl.class);
	}
	
	public List<SynchControl> findSynchControlForJob(Integer jobId){
		QSynchControl sy = QSynchControl.synchControl; 
		JPAQuery q = new JPAQuery(this.em);
		q.from(sy); 
		q.where(sy.jobId.eq(jobId));
		return q.list(sy); 
		
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
