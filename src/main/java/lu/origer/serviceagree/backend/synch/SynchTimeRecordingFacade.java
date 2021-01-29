package lu.origer.serviceagree.backend.synch;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.synch.QSynchTimeRecording;
import lu.origer.serviceagree.models.synch.SynchTimeRecording;

@Stateless
public class SynchTimeRecordingFacade extends AbstractFacade<SynchTimeRecording> {
	

	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;
	
	public SynchTimeRecordingFacade(){
		super(SynchTimeRecording.class);
	}
	
	public List<SynchTimeRecording> findAllTimeRec(){
		QSynchTimeRecording tr = QSynchTimeRecording.synchTimeRecording; 
		JPAQuery q = new JPAQuery(this.em); 
		q.from(tr); 
		List<SynchTimeRecording> res = q.list(tr); 
		return res;
				
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

}
