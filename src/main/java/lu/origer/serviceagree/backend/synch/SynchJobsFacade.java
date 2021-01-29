package lu.origer.serviceagree.backend.synch;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.synch.QSynchJobs;
import lu.origer.serviceagree.models.synch.QTimeRecordingHistory;
import lu.origer.serviceagree.models.synch.SynchJobs;
import lu.origer.serviceagree.models.synch.TimeRecordingHistory;

@Stateless
public class SynchJobsFacade extends AbstractFacade<SynchJobs> {
	
	public static final String JOB_COMPLETE = "Complete";
	public static final String JOB_INCOMPLETE = "Incomplete";
	public static final String JOB_ERROR = "Error";
	

	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;
	
	public SynchJobsFacade(){
		super(SynchJobs.class);
	}
	
	public Boolean openSynchJobs(){
		QSynchJobs sy = QSynchJobs.synchJobs; 
		JPAQuery q = new JPAQuery(this.em); 
		q.from(sy);
		q.where(sy.webappState.isNull());
		
		List<SynchJobs> res = q.list(sy);
		return res != null && res.size() > 0 ;
		
	}
	
	public List<SynchJobs> findAllOrderByDateDesc(){
		QSynchJobs sy = QSynchJobs.synchJobs; 
		JPAQuery q = new JPAQuery(this.em); 
		q.from(sy); 
		q.orderBy(sy.createDate.desc()); 
		return q.list(sy); 
		
	}
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public List<SynchJobs> findAllNotImported() {
		// TODO Auto-generated method stub
		QSynchJobs sy = QSynchJobs.synchJobs; 
		JPAQuery q = new JPAQuery(this.em); 
		q.from(sy);
		q.where(sy.webappState.notEqualsIgnoreCase("complete").or(sy.webappState.isNull()));
		q.orderBy(sy.createDate.desc()); 
		return q.list(sy); 
	}
}
