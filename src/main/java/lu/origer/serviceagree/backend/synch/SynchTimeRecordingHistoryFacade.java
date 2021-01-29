package lu.origer.serviceagree.backend.synch;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lu.origer.serviceagree.backend.main.AbstractFacade;

@Stateless
public class SynchTimeRecordingHistoryFacade extends AbstractFacade<SynchTimeRecordingHistoryFacade> {
	

	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;
	
	public SynchTimeRecordingHistoryFacade(){
		super(SynchTimeRecordingHistoryFacade.class);
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
