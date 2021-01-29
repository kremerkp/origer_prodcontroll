package lu.origer.serviceagree.backend.synch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.openejb.core.cmp.jpa.JpaCmpEngine;

import com.mysema.query.jpa.impl.JPAQuery;
import com.sun.research.ws.wadl.Application;

import lu.origer.serviceagree.backend.contract.ServiceContractFacade;
import lu.origer.serviceagree.backend.contract.ServiceFacade;
import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.frontend.main.ApplicationBean;
import lu.origer.serviceagree.models.contract.QService;
import lu.origer.serviceagree.models.contract.QServiceContract;
import lu.origer.serviceagree.models.synch.QServiceHistory;
import lu.origer.serviceagree.models.synch.QTimeRecordingHistory;
import lu.origer.serviceagree.models.synch.ServiceHistory;
import lu.origer.serviceagree.models.synch.TimeRecordingHistory;

@Stateless
public class TimeRecordingHistoryFacade extends AbstractFacade<TimeRecordingHistory> {

	public static final String RUESTZEIT_TR = "RZ";
	public static final String REGIEZEIT_TR = "RGZ";
	public static final String REPARATURZEIT_TR = "RPZ";
	public static final String FAHRTZEIT_TR = "FZ";
	public static final String ISTZEIT_TR = "IZ";
	public static final String ISTZEIT_PZ = "PZ";

	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;

	@Inject
	ServiceFacade serviceFacade;
	
	@Inject
	ServiceContractFacade serviceContractFacade;	

	public TimeRecordingHistoryFacade() {
		super(TimeRecordingHistory.class);
	}

	
	public Boolean billableTimeExists(){
		QTimeRecordingHistory th = QTimeRecordingHistory.timeRecordingHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(th);
		q.where(th.type.equalsIgnoreCase("RGZ"));
		q.where(th.payed.isFalse());
		List<TimeRecordingHistory> res = q.list(th);
		return res != null && res.size() > 0 ;
	}

	
	public List<TimeRecordingHistory> findByElementAndService(Integer elementId, Integer serviceId, final Boolean isRepair){
		QTimeRecordingHistory th = QTimeRecordingHistory.timeRecordingHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(th);
		q.where(th.serviceElement.id.eq(elementId));
		q.where(th.service.id.eq(serviceId));
		if(isRepair)
		{
			q.where(th.type.eq("RPZ"));
		}
		else
		{
			q.where(th.type.ne("RPZ"));
		}
		
		List<TimeRecordingHistory> res = q.list(th);
		
		
		for (TimeRecordingHistory t : res) {
			if(t != null && t.getTimeInSeconds() != null)
			{
				t.setTime(ApplicationBean.splitToComponentTimeString(t.getTimeInSeconds()));
				if (t.getService() != null) {
					t.setService(serviceFacade.find(t.getService().getId()));
					t.getService().getServiceContract();
				}
			}
			else
			{
				t.setTime("");
			}
		}
		return res;
		
	}
	
	public List<TimeRecordingHistory> findAllTransient() {
		QTimeRecordingHistory th = QTimeRecordingHistory.timeRecordingHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(th);
		q.where(th.timeInSeconds.gt(0));
		List<TimeRecordingHistory> res = q.list(th);
		for (TimeRecordingHistory t : res) {
			t.setTime(ApplicationBean.splitToComponentTimeString(t.getTimeInSeconds()));
			if (t.getService() != null) {
				t.setService(serviceFacade.find(t.getService().getId()));
				t.getService().getServiceContract();

			}
		}
		return res;
	}
	
	public List<TimeRecordingHistory> findAllByDate(Date startdate, Date enddate) {
		QTimeRecordingHistory th = QTimeRecordingHistory.timeRecordingHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(th);
		if (startdate != null){
			startdate = ApplicationBean.setDateToBeginOfDay(startdate);
			q.where(th.recordDate.after(startdate)); 
		}
		if (enddate != null){
			enddate = ApplicationBean.setDateToEndOfDay(enddate);
			q.where(th.recordDate.before(enddate));
		}
		q.where(th.type.eq(TimeRecordingHistoryFacade.REGIEZEIT_TR));

		List<TimeRecordingHistory> res = q.list(th);
		
		for (TimeRecordingHistory t : res) {
			t.setTime(ApplicationBean.splitToComponentTimeString(t.getTimeInSeconds()));
			if (t.getService() != null) {
				t.setService(serviceFacade.find(t.getService().getId()));
				t.getService().getServiceContract();

			}
		}
		
		return res;
		
	}
	
	public List<TimeRecordingHistory> findAllTransientBillable() {
		QTimeRecordingHistory th = QTimeRecordingHistory.timeRecordingHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(th);
		q.where(th.type.equalsIgnoreCase("RGZ"));
		q.where(th.timeInSeconds.gt(0));
		List<TimeRecordingHistory> res = q.list(th);
		for (TimeRecordingHistory t : res) {
			t.setTime(ApplicationBean.splitToComponentTimeString(t.getTimeInSeconds()));
			if (t.getService() != null) {
				t.setService(serviceFacade.find(t.getService().getId()));
				if(t.getBuildingSite() == null){
					t.setBuildingSite(t.getService().getServiceContract().getBuildingSite());
				}
				t.getService().getServiceContract();
			}
		}
		return res;
	}



	public List<TimeRecordingHistory> findAllEager() {
		QTimeRecordingHistory th = QTimeRecordingHistory.timeRecordingHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(th);
		List<TimeRecordingHistory> res = q.list(th);
		for (TimeRecordingHistory t : res) {
			t.getService().getId();
		}
		return res;
	}

	// public List<TimeRecordingHistory> findAllWithMinutes(){
	// QTimeRecordingHistory th = QTimeRecordingHistory.timeRecordingHistory;
	// JPAQuery q = new JPAQuery(this.em);
	// q.from(th);
	// List<TimeRecordingHistory> resultList = q.list(th);
	// for (TimeRecordingHistory t: resultList){
	// t.setTimeInMinutes(t.getTimeInMinutes() / 60);
	// }
	// }
	
	/**
	 * Fetches the repair time recording entry for the service history entry, using its element and service.
	 * 
	 * @param elementId The element id attached to the respective service history.
	 * @param serviceId The service id attached to the respective service history.
	 * @return An array list of repair time recordings corresponding to the entered element and service. 
	 */
	public List<TimeRecordingHistory> findRepairTimesForServiceHistory(final Integer elementId, final Integer serviceId, final String description)
	{
		final List<TimeRecordingHistory> times = new ArrayList<>();
		if(elementId != null && serviceId != null)
		{
			try
			{
				final QTimeRecordingHistory time = QTimeRecordingHistory.timeRecordingHistory;
				final JPAQuery query = new JPAQuery(em);
				times.addAll(query.from(time)
						.where(time.type.eq(REPARATURZEIT_TR)
								.and(time.serviceElement.id.eq(elementId)
										//.and(time.service.id.eq(serviceId)
												.and(time.description.like('%' + description + '%')))).list(time));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return times;
	}
	
	/**
	 * Fetches all repair entries for the respective building site.
	 * 
	 * @param siteId The ID of the respective building site.
	 * @return An array list of ServiceHistory elements representing conducted repairs.
	 */
	public List<ServiceHistory> findRepairEntriesForSite(final Integer siteId)
	{
		final List<ServiceHistory> entries = new ArrayList<>();
		if(siteId != null)
		{
			try
			{
				final QServiceHistory history = QServiceHistory.serviceHistory;
				final QService service = QService.service;
				final QServiceContract contract = QServiceContract.serviceContract;
				final JPAQuery query = new JPAQuery(this.em);
				entries.addAll(
						query.from(history)
						.innerJoin(history.service, service)
						.innerJoin(service.serviceContract, contract)
						.where(contract.buildingSite.id.eq(siteId)
								.and(history.checkedAsRepaired.isTrue()
										.and(history.endTime.isNull()
												.and(history.comment.isNull()))))
						.orderBy(history.element.id.asc()).list(history));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return entries;
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	/**
	 * Checks if a time record history with the entered values already exists within the DB.
	 * 
	 * @param seconds
	 * @param serviceId
	 * @param elementId
	 * @param mechanicId
	 * @return True if at least one entry with corresponding values was found, false else.
	 */
	public Boolean isDuplicate(final Integer seconds, final Integer serviceId, final Integer elementId, final Integer mechanicId)
	{
		Boolean isDuplicate = false;
		try
		{
			final List<TimeRecordingHistory> entries = new ArrayList<>();
			final QTimeRecordingHistory time = QTimeRecordingHistory.timeRecordingHistory;
			final JPAQuery query = new JPAQuery(this.em);
			query.from(time);
			if(seconds != null)
			{
				query.where(time.timeInSeconds.eq(seconds));
			}
			if(serviceId != null)
			{
				query.where(time.service.id.eq(serviceId));
			}
			if(elementId != null)
			{
				query.where(time.serviceElement.id.eq(elementId));
			}
			if(mechanicId != null)
			{
				query.where(time.mechanic.id.eq(mechanicId));
			}
			if(entries != null && !entries.isEmpty())
			{
				isDuplicate = true;
			}
			entries.addAll(query.list(time));
			isDuplicate = !entries.isEmpty();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return isDuplicate;
	}
}
