package lu.origer.serviceagree.backend.synch;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.synch.SynchService;

@Stateless
public class SynchServiceFacade extends AbstractFacade<SynchService> {
	

	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;
	
	public SynchServiceFacade(){
		super(SynchService.class);
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
