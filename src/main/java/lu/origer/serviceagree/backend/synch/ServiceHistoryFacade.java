package lu.origer.serviceagree.backend.synch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.contract.ChecklistItemFacade;
import lu.origer.serviceagree.backend.contract.FileArchiveFacade;
import lu.origer.serviceagree.backend.contract.ServiceContractFacade;
import lu.origer.serviceagree.backend.contract.ServiceFacade;
import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.frontend.main.SessionBean;
import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contract.Service;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.main.FileArchive;
import lu.origer.serviceagree.models.main.ObjectComments;
import lu.origer.serviceagree.models.main.QObjectComments;
import lu.origer.serviceagree.models.reporting.SubZertifikat;
import lu.origer.serviceagree.models.synch.QServiceHistory;
import lu.origer.serviceagree.models.synch.ServiceElementHistory;
import lu.origer.serviceagree.models.synch.ServiceHistory;
import lu.origer.serviceagree.models.synch.TimeRecordingHistory;

@Stateless
public class ServiceHistoryFacade extends AbstractFacade<ServiceHistory> {

	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;

	@Inject
	ServiceHistoryFacade serviceHistoryFacade;

	@Inject
	FileArchiveFacade fileArchiveFacade;

	@Inject
	ServiceFacade serviceFacade;
	
	@Inject
	SessionBean sessionBean;
	
	@Inject
	ServiceContractFacade serviceContractFacade;
	
	@Inject
	TimeRecordingHistoryFacade timeRecordingHistoryFacade;

	private List<TimeRecordingHistory> findByElementAndService;

	public ServiceHistoryFacade() {
		super(ServiceHistory.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public Boolean elementSynchronisationExists(ServiceElements se) {
//		QServiceHistory h = QServiceHistory.serviceHistory;
//		JPAQuery q = new JPAQuery(this.em);
//		q.from(h);
//		q.where(h.element.eq(se));
//		q.where(h.startTime.isNotNull());
//		return q.list(h) != null && q.list(h).size() > 0;
//		
		Query q = em.createNativeQuery(
				"select count(*) as count from service_history where fk_element = ? and starttime is not null");
		q.setParameter(1, se.getId());
		Long res = (Long) q.getSingleResult();

		if (res != null) {
			return res > 0;
		} else {
			return false;
		}
		
		
	}

	/**
	 * 
	 * @param histList
	 *            List<ServiceHistory>
	 * @param serEl
	 *            ServiceElements
	 * @param force
	 */
	public void removeHistoryElements(List<ServiceHistory> histList, ServiceElements serEl, Boolean force) {
		for (ServiceHistory sh : histList) {
			// Einträge werden nur gelöscht wenn bisher keine Überprüfung
			// stattgefunden hat!
			// Wenn löschen forciert angehakt ist, werden zu dem Service alle
			// History-Element gelöscht
			if (sh.getStartTime() != null && force == false) {
				if (serEl.equals(sh.getElement())) {
					serviceHistoryFacade.remove(sh);
				}
			}
		}
	}

	/**
	 * Löscht die Einträge der Historie sofern diese bisher nicht bearbeitet
	 * wurden.
	 * 
	 * @param histList
	 *            List<ServiceHistory>
	 * @param serList
	 *            List<ServiceElements>
	 */
	public void removeHistoryElements(List<ServiceHistory> histList, List<ServiceElements> serList, Boolean force) {
		for (ServiceHistory sh : histList) {
			// Einträge werden nur gelöscht wenn bisher keine Überprüfung
			// stattgefunden hat!
			// Wenn löschen forciert angehakt ist, werden zu dem Service alle
			// History-Element gelöscht, außer Regiezeiten
			if (sh.getStartTime() == null && force) {
				for (ServiceElements selectedElements : serList) {
					if (selectedElements.equals(sh.getElement())) {
						if (!sh.getCheckListItem().getId().equals(ChecklistItemFacade.REGIE_TIME)
								&& !sh.getCheckedAsRepaired()) {
							serviceHistoryFacade.remove(sh);
						}
					}
				}
			}
		}
	}

	public List<ServiceHistory> findForServiceAndElement(ServiceElements se, Service s) {
		QServiceHistory sh = QServiceHistory.serviceHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(sh);
		q.where(sh.service.eq(s));
		q.where(sh.element.eq(se));
		return q.list(sh);
	}

	public Boolean elementHasHist(Integer serviceElement, Integer service) {
		Query q = em.createNativeQuery(
				"select count(*) as count from service_history where fk_element = ? and fk_service = ?");
		q.setParameter(1, serviceElement);
		q.setParameter(2, service);
		Long res = (Long) q.getSingleResult();

		if (res != null) {
			return res > 0;
		} else {
			return false;
		}

	}

	public Boolean elementHasHistory(ServiceElements e, Service s) {
		QServiceHistory h = QServiceHistory.serviceHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(h);
		q.where(h.element.eq(e));
		q.where(h.service.eq(s));
		return q.list(h).size() > 0;

	}

	public List<ServiceHistory> findChecklistForSelectedHistoryElementByDate(ServiceElementHistory sh,
			ServiceElements se) {
		QServiceHistory hi = QServiceHistory.serviceHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(hi);
		q.where(hi.element.eq(se));
		if (sh.getDate() != null) {
			q.where(hi.startTime.eq(sh.getDate()));
		} else {
			q.where(hi.startTime.isNull());
		}
		return q.list(hi);
	}

	public List<ServiceHistory> findChecklistForSelectedHistoryElement(ServiceElementHistory sh, ServiceElements se,
			Service s) {
		QServiceHistory hi = QServiceHistory.serviceHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(hi);
		q.where(hi.element.eq(se));
		q.where(hi.service.eq(s));
		if (sh.getDate() != null) {
			q.where(hi.startTime.eq(sh.getDate()));
		} else {
			q.where(hi.startTime.isNull());
		}
		return q.list(hi);
	}
	
	public List<ServiceHistory> getServiceCustomerServiceHistory(ServiceElements se) {
		QServiceHistory qs = QServiceHistory.serviceHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(qs).where(qs.element.eq(se).and(qs.startTime.isNotNull())).orderBy(qs.startTime.asc());		

		return q.list(qs);

	}

	public List<ServiceHistory> getServiceHistoryByElementAndDate(Date date, ServiceElements se) {
		QServiceHistory qs = QServiceHistory.serviceHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(qs);
		q.where(qs.element.eq(se));
		if (date == null) {
			q.where(qs.startTime.isNull());
		} else {
			q.where(qs.startTime.eq(date));
		}

		return q.list(qs);

	}

	public List<ServiceHistory> getServiceHistoryByElementServiceAndDate(Service s, Date date, ServiceElements se) {
		QServiceHistory qs = QServiceHistory.serviceHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(qs);
		q.where(qs.service.eq(s));

		q.where(qs.element.eq(se));
		if (date == null) {
			q.where(qs.startTime.isNull());
		} else {
			q.where(qs.startTime.eq(date));
		}		
		return q.list(qs);
	}
	
	public List<ServiceHistory> getServiceHistoryByElementService(Service s, ServiceElements se) {
		QServiceHistory qs = QServiceHistory.serviceHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(qs);
		q.where(qs.service.eq(s));
		q.where(qs.element.eq(se));	
		return q.list(qs);
	}


	public String getFileUrlFromRepairedItem(Service service, Date date, ServiceElements se) {
		List<ServiceHistory> sList = getServiceHistoryByElementServiceAndDate(service, date, se);
		String result = "";
		for (ServiceHistory s : sList) {
			// if (s.getCheckedAsRepaired() != null && s.getCheckedAsRepaired())
			// {
			FileArchive f = fileArchiveFacade.findForHistory(s);
			if (f != null && (f.getSignature() != null && !f.getSignature())) {
				result = f.getUrl();
				Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Archiv gefunden für " + se.getElementnumber());
				break;
			}
			// }
		}
		if(result == null || result.isEmpty())
		{
			Logger.getLogger(this.getClass().getName()).log(Level.ERROR, "Kein Archiv gefunden für " + se.getElementnumber());
			// No image found, skip date check.
			final List<ServiceHistory> history = this.getServiceHistoryByElementService(service, se);
			for(ServiceHistory entry : history)
			{
				FileArchive f = fileArchiveFacade.findForHistory(entry);
				if (f != null && (f.getSignature() != null && !f.getSignature())) {
					result = f.getUrl();
					break;
				}
			}
		}
		return result;
	}

	public String getAggregatedDescriptionFromServiceHistoryList(Service service, Date date, ServiceElements se) {
		List<ServiceHistory> sList = getServiceHistoryByElementServiceAndDate(service, date, se);

		String result = "";
		for (ServiceHistory s : sList) {
			if (s.getDescription() != null && s.getDescription().length() > 0) {
				result += s.getDescription() + "\n";
			}
		}
		return result;
	}

	// public String getStateFromServiceHistoryList(List<ServiceHistory> sList)
	// {
	// String result = "ungewartet";
	// Boolean repaired = false;
	// Boolean defect = false;
	// Boolean isOkay = true;
	// Boolean isLack = false;
	// Boolean isErrorHistEntry = false;
	//
	// for (ServiceHistory s : sList) {
	// if (s.getCheckedAsDefect() && !defect) {
	// defect = true;
	// }
	// if (!s.getCheckedAsOk() && isOkay) {
	// isOkay = false;
	// }
	// if (s.getCheckedAsRepaired() != null && s.getCheckedAsRepaired() &&
	// !repaired) {
	// repaired = true;
	// }
	// if (s.getCheckedAsLack() && !isLack) {
	// isLack = true;
	// }
	//
	//
	// if (defect && repaired && s.getErrorHistoryFlag() != null &&
	// s.getErrorHistoryFlag()) {
	// result = ServiceFacade.STATE_DEFEKT;
	//
	// } else if (defect && repaired && s.getErrorHistoryFlag() != null &&
	// !s.getErrorHistoryFlag()) {
	// result = ServiceFacade.STATE_REPARIERT;
	//
	// } else if (defect && repaired && s.getErrorHistoryFlag() == null) {
	// result = ServiceFacade.STATE_REPARIERT;
	//
	// } else if (defect) {
	// result = ServiceFacade.STATE_DEFEKT;
	// } else if (repaired) {
	// result = ServiceFacade.STATE_REPARIERT;
	// } else if (isOkay && repaired && !defect) {
	// result = ServiceFacade.STATE_OK_UND_REPARIERT;
	// } else if (isOkay && !repaired && !defect && !isLack) {
	// result = ServiceFacade.STATE_OK;
	// }
	// if (s.getCheckListItem().equals(ChecklistItemFacade.REGIE_TIME)) {
	// {
	// result = ServiceFacade.STATE_REGIEZEIT;
	// }
	//
	// }
	// }
	// return result;
	//
	// }

	public String getStateFromServiceHistoryList(List<ServiceHistory> sList) {
		String result;
		if(sessionBean.getUserLocale().equals(Locale.GERMANY))
		{
			result = ServiceFacade.STATE_UNGEWARTET;
		}
		else
		{
			result = ServiceFacade.STATE_UNGEWARTET_FR;
		}
		Boolean oneTimeDefect = false;
		Boolean oneTimeRepairedAndOk = false;
		Boolean oneTimeMangel = false;
		Boolean allTimeok = true;
		Boolean isRegie = false;
		Boolean isReclamation = false; 
		if(sList != null && !sList.isEmpty())
		{
			for (ServiceHistory s : sList) {
				if(s.getStartTime() != null)
				{
					if (s.getCheckListItem().getId() == -1) {
						isRegie = true;
					}
					if (s.getCheckListItem().getId() == -2 && !(s.getCheckedAsRepaired() && s.getCheckedAsOk())) {
						isReclamation = true;
					}
		
					if (!s.getCheckedAsOk()) {
						allTimeok = false;						
					}
					if (s.getCheckedAsDefect()) {
						oneTimeDefect = true;
					}
					if (s.getCheckedAsLack()) {
						oneTimeMangel = true;
					}
					if (s.getCheckedAsRepaired() && s.getCheckedAsOk()) {
						oneTimeRepairedAndOk = true;
					}
				}
	
			}
		}
		if(sessionBean.getUserLocale().equals(Locale.GERMANY))
		{
			if (oneTimeDefect && !oneTimeRepairedAndOk) {
				result = ServiceFacade.STATE_DEFEKT;
			} else if (oneTimeMangel && !oneTimeRepairedAndOk) {
				result = ServiceFacade.STATE_MANGEL;
			}
			if (allTimeok) {
				result = ServiceFacade.STATE_OK;
			}
			if (allTimeok && oneTimeRepairedAndOk) {
				result = ServiceFacade.STATE_OK_UND_REPARIERT;
			}
			if (oneTimeDefect && oneTimeRepairedAndOk) {
				result = ServiceFacade.STATE_DEFEKT_UND_REPARIERT;
			} else if (oneTimeMangel && oneTimeRepairedAndOk) {
				result = ServiceFacade.STATE_MANGEL_UND_REPARIERT;
			}
			if (isRegie) {
				result = ServiceFacade.STATE_REGIEZEIT;
				if (allTimeok) {
					result = ServiceFacade.STATE_REGIEZEIT_OK;
				} else if (oneTimeMangel) {
					result = ServiceFacade.STATE_REGIEZEIT_MANGEL;
				} else if (oneTimeDefect) {
					result = ServiceFacade.STATE_REGIEZEIT_DEFEKT;
				}
			}
			if (isReclamation){
				result = ServiceFacade.STATE_CUSTOMER_RECLAMATION;
				if (allTimeok) {
					result = ServiceFacade.STATE_CUSTOMER_RECLAMATION_OK;
				} else if (oneTimeMangel) {
					result = ServiceFacade.STATE_CUSTOMER_RECLAMATION_MANGEL;
				} else if (oneTimeDefect) {
					result = ServiceFacade.STATE_CUSTOMER_RECLAMATION_DEKEFT;
				}
			}
		}
		else
		{
			if (oneTimeDefect && !oneTimeRepairedAndOk) {
				result = ServiceFacade.STATE_DEFEKT_FR;
			} else if (oneTimeMangel && !oneTimeRepairedAndOk) {
				result = ServiceFacade.STATE_MANGEL_FR;
			}
			if (allTimeok) {
				result = ServiceFacade.STATE_OK_FR;
			}
			if (allTimeok && oneTimeRepairedAndOk) {
				result = ServiceFacade.STATE_OK_UND_REPARIERT_FR;
			}
			if (oneTimeDefect && oneTimeRepairedAndOk) {
				result = ServiceFacade.STATE_DEFEKT_UND_REPARIERT_FR;
			} else if (oneTimeMangel && oneTimeRepairedAndOk) {
				result = ServiceFacade.STATE_MANGEL_UND_REPARIERT_FR;
			}
			if (isRegie) {
				result = ServiceFacade.STATE_REGIEZEIT_FR;
				if (allTimeok) {
					result = ServiceFacade.STATE_REGIEZEIT_OK_FR;
				} else if (oneTimeMangel) {
					result = ServiceFacade.STATE_REGIEZEIT_MANGEL_FR;
				} else if (oneTimeDefect) {
					result = ServiceFacade.STATE_REGIEZEIT_DEFEKT_FR;
				}
			}
			if (isReclamation){
				result = ServiceFacade.STATE_CUSTOMER_RECLAMATION_FR;
				if (allTimeok) {
					result = ServiceFacade.STATE_CUSTOMER_RECLAMATION_OK_FR;
				} else if (oneTimeMangel) {
					result = ServiceFacade.STATE_CUSTOMER_RECLAMATION_MANGEL_FR;
				} else if (oneTimeDefect) {
					result = ServiceFacade.STATE_CUSTOMER_RECLAMATION_DEKEFT_FR;
				}
				
			}
		}

		return result;

	}
	
	public String getAggregatedStateForDate(Service s, ServiceElements se)
	{	
		List<ServiceElementHistory> entries = serviceHistoryFacade.findHistoryElementFromSelected(se,s);
		String status;
		if(entries != null && !entries.isEmpty())
		{
			status = entries.get(entries.size()-1).getState();
		}
		else
		{
			if(sessionBean.getUserLocale().equals(Locale.GERMANY))
			{
				status = ServiceFacade.STATE_UNGEWARTET;
			}
			else
			{
				status = ServiceFacade.STATE_UNGEWARTET_FR;
			}		
		}
		return status;
	}

	public String getAggregatedStateFromAllServiceHistoryList(Date date, ServiceElements se) {
		List<ServiceHistory> sList =  getServiceCustomerServiceHistory(se);
		String result = getStateFromServiceHistoryList(sList);
		return result;
	}

	public String getAggregatedStateFromServiceHistoryList(Service service, Date date, ServiceElements se) {
		List<ServiceHistory> sList = getServiceHistoryByElementServiceAndDate(service, date, se);
		String result = getStateFromServiceHistoryList(sList);
		return result;
	}

	public String getAggregatedRepairedChecklistItems(Service service, Date date, ServiceElements se) {
		List<ServiceHistory> sList = getServiceHistoryByElementServiceAndDate(service, date, se);
		String result = "";
		Boolean first = true; 
		for (ServiceHistory h : sList) {
			if (h.getCheckListItem().getId() != -1 && h.getCheckListItem().getId() != -2 ) {
				if ((h.getCheckedAsRepaired() || h.getCheckedAsLack()) && h.getCheckedAsOk()) {
					if (first){
						result += "* ";
						first = false; 
					} else {
						result += "\n* ";
					}
					
					if(sessionBean.getUserLocale().equals(Locale.GERMANY))
					{
						if(h.getCheckListItem().getCheckListItemCategory() != null && !h.getCheckListItem().getCheckListItemCategory().getName().isEmpty())
						{
							result += h.getCheckListItem().getCheckListItemCategory().getName();							
						}
						else
						{
							result += "Kategorie-Übersetzung fehlt";
						}
						if(h.getCheckListItem().getName() != null && !h.getCheckListItem().getName().isEmpty())
						{
							result +=  " - " + h.getCheckListItem().getName() + "\n";
						}
						else
						{
							result += "* Übersetzung fehlt\n";
						}						
					}
					else
					{
						if(h.getCheckListItem().getCheckListItemCategory() != null && !h.getCheckListItem().getCheckListItemCategory().getName().isEmpty())
						{
							result += h.getCheckListItem().getCheckListItemCategory().getNameFrench();							
						}
						else
						{
							result += "Traduction categorie manque";
						}
						if(h.getCheckListItem().getNameFrench() != null && !h.getCheckListItem().getNameFrench().isEmpty())
						{
							result +=  " - " + h.getCheckListItem().getNameFrench() + "\n";
						}
						else
						{
							result += "* Traduction manque\n";
						}
					}
				}
			}
		}
		return result;
	}

	public String getAggregatedChecklistItems2Repair(Service service, Date date, ServiceElements se) {
		List<ServiceHistory> sList = getServiceHistoryByElementServiceAndDate(service, date, se);
		String result = "";
		Boolean first = true; 
		for (ServiceHistory h : sList) {
			if (h.getCheckListItem().getId() != -1 && h.getCheckListItem().getId() != -2) {
				if ((h.getCheckedAsDefect() || h.getCheckedAsLack()) && !h.getCheckedAsOk()) {
					if (first){
						result += "* ";
						first = false;
					} else {
						result += "\n* ";
					}
					
					if(sessionBean.getUserLocale().equals(Locale.GERMANY))
					{						
						if(h.getCheckListItem().getCheckListItemCategory() != null && !h.getCheckListItem().getCheckListItemCategory().getName().isEmpty())
						{
							result += h.getCheckListItem().getCheckListItemCategory().getName();							
						}
						else
						{
							result += "Kategorie-Übersetzung fehlt";
						}
						if(h.getCheckListItem().getName() != null && !h.getCheckListItem().getName().isEmpty())
						{
							result +=  " - " + h.getCheckListItem().getName() + "\n";
						}
						else
						{
							result += "* Übersetzung fehlt\n";
						}
					}
					else
					{
						if(h.getCheckListItem().getCheckListItemCategory() != null && !h.getCheckListItem().getCheckListItemCategory().getName().isEmpty())
						{
							result += h.getCheckListItem().getCheckListItemCategory().getNameFrench();							
						}
						else
						{
							result += "Traduction categorie manque";
						}
						if(h.getCheckListItem().getNameFrench() != null && !h.getCheckListItem().getNameFrench().isEmpty())
						{
							result +=  " - " + h.getCheckListItem().getNameFrench() + "\n";
						}
						else
						{
							result += "* Traduction manque\n";
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Fetches object comments for the respective service element.
	 * 
	 * @param elementId
	 * @return An array of object comments if found, an empty array else.
	 */
	private List<ObjectComments> getCommentsForElement(final Integer elementId)
	{
		List<ObjectComments> comments = new ArrayList<>();
		if(elementId != null)
		{
			QObjectComments comment = QObjectComments.objectComments;
			final JPAQuery query = new JPAQuery(this.em);
			query.from(comment).where(comment.objectId.eq(elementId).and(comment.objectType.eq(ServiceElements.class.getName())));
			comments = query.list(comment);
		}
		return comments;
	}
	
	public List<ServiceHistory> getHistoryForElement(final Integer elementId)
	{
		List<ServiceHistory> result = new ArrayList<>();
		if(elementId != null)
		{
			QServiceHistory history = QServiceHistory.serviceHistory;
			JPAQuery query = new JPAQuery(this.em);
			query.from(history).where(history.element.id.eq(elementId)					
					.and(history.startTime.isNotNull()))
			.orderBy(history.startTime.desc(), history.element.id.asc());
			result = query.list(history);
		}
		return result;
	}

	public List<ServiceElementHistory> findHistoryElementFromSelected(ServiceElements e, Service s) {
		List<ServiceElementHistory> result = new ArrayList<>();
		HashMap<ServiceElementHistory, ServiceHistory> resultMap = new HashMap<>();
		QServiceHistory sh = QServiceHistory.serviceHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(sh);
		q.where(sh.element.eq(e));
		q.where(sh.startTime.isNotNull());
		for (ServiceHistory hist : q.list(sh)) {
			ServiceElementHistory seh = new ServiceElementHistory();
			seh.setName(hist.getElement().getElementnumber());
			seh.setDate(hist.getStartTime());
			seh.setServiceElementId(s.getId());
			if(hist.getCheckListItem().getId().equals(ChecklistItemFacade.CUSTOMER_COMPLAINT)){
				List<String> mech = new ArrayList<>(); 
				mech.add(hist.getComment());
				seh.setMechanics(mech);
			}
			resultMap.put(seh, hist);
		}

		Iterator<Entry<ServiceElementHistory, ServiceHistory>> it = resultMap.entrySet().iterator();
		while (it.hasNext()) {
			@SuppressWarnings("rawtypes")
			Map.Entry pair = (Map.Entry) it.next();
			ServiceElementHistory k = (ServiceElementHistory) pair.getKey();
			ServiceHistory servHist = (ServiceHistory) pair.getValue();

			k.setDescription(getAggregatedDescriptionFromServiceHistoryList(servHist.getService(), k.getDate(),
					servHist.getElement()));
			k.setState(getAggregatedStateFromServiceHistoryList(servHist.getService(), k.getDate(),
					servHist.getElement()));
			k.setChecklistitems2Repair(
					getAggregatedChecklistItems2Repair(servHist.getService(), k.getDate(), servHist.getElement()));
			k.setChecklistitemsRepaired(
					getAggregatedRepairedChecklistItems(servHist.getService(), k.getDate(), servHist.getElement()));
			k.setServiceElementId(servHist.getElement().getId());
			// Check if the history entry is a reclamation.
			Boolean isReclamation = false;
			if(sessionBean.getUserLocale().equals(Locale.GERMANY))
			{
				isReclamation = k.getState() != null 
						&& (k.getState().equals(ServiceFacade.STATE_CUSTOMER_RECLAMATION) || k.getState().equals(ServiceFacade.STATE_CUSTOMER_RECLAMATION_MANGEL));
			}
			else
			{
				isReclamation = k.getState() != null 
						&& (k.getState().equals(ServiceFacade.STATE_CUSTOMER_RECLAMATION_FR) || k.getState().equals(ServiceFacade.STATE_CUSTOMER_RECLAMATION_MANGEL_FR));
			}
			// Skip mechanic search for admin reclamations.
			if(!isReclamation)
			{
				if(k.getMechanics() == null)
				{
					k.setMechanics(getMechanicsForHistoryFromTimeRecording(servHist.getService(), (k.getState().contains("rep") || k.getState().contains("rek")), servHist.getElement()));
				}
				else
				{			
					k.getMechanics().addAll(getMechanicsForHistoryFromTimeRecording(servHist.getService(), (k.getState().contains("rep") || k.getState().contains("rek")), servHist.getElement()));
				}
			}
			result.add(k);
		}
		// Check for comments.		
		e.setComments(this.getCommentsForElement(e.getId()));		
		if (e.getComments() != null && e.getComments().size() > 0 ) {
			for (ObjectComments oc : e.getComments()) {
				ServiceElementHistory ssh = new ServiceElementHistory(); 
				ssh.setChecklistitems2Repair("-");
				ssh.setChecklistitemsRepaired("-");
				ssh.setDate(oc.getCreateDate());
				ssh.setDescription(oc.getComment());
				ssh.setFloor("-");
				ssh.setName("-");
				ssh.setRoom("-");
				ssh.setState("-");
				ssh.setCommentId(oc.getId());
				result.add(ssh);
			}
		}

		Collections.sort(result, new Comparator<ServiceElementHistory>() {
			@Override
			public int compare(ServiceElementHistory f1, ServiceElementHistory f2) {
				if (f1.getDate() == null) {
					return (f2.getDate() == null) ? 0 : -1;
				}
				if (f2.getDate() == null) {
					return 1;
				}
				return f1.getDate().compareTo(f2.getDate());
			}
		});

		return result;

	}

	private List<String> getMechanicsForHistoryFromTimeRecording(Service service, final Boolean isRepair, ServiceElements element) {
		findByElementAndService = timeRecordingHistoryFacade.findByElementAndService(element.getId(), service.getId(), isRepair);
		HashMap<String, String> m1 = new HashMap<>();
		List<String> result = new ArrayList<>();
		for (TimeRecordingHistory tr: findByElementAndService){
			m1.put(tr.getMechanic().getFirstname() + " " + tr.getMechanic().getLastname() , 
					tr.getMechanic().getFirstname() + " " + tr.getMechanic().getLastname());
		}
		Iterator it = m1.entrySet().iterator(); 
		while( it.hasNext()){
			Map.Entry pair = (Map.Entry)it.next();
			result.add((String)pair.getValue());
		}
		return result;
	}

	public List<ServiceHistory> getLastServiceHistoryFromElement(ServiceElements e) {
		QServiceHistory hi = QServiceHistory.serviceHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(hi);
		q.where(hi.element.eq(e));
		q.orderBy(hi.startTime.desc());
		Date lastDate = q.singleResult(hi.startTime);

		q.from(hi);
		q.where(hi.startTime.eq(lastDate));
		q.where(hi.element.eq(e));

		return q.list(hi);
	}

	public Integer getLackElementsFromService(Service service) {
		return 0;
	}

	public Integer getDefectElementsFromService(Service service) {
		return 0;

	}

	public Integer getControlledElementesFromService(Service service) {
		List<ServiceElements> sList = service.getServiceElementList();
		for (ServiceElements s : sList) {

		}
		return 0;
	}

	public Integer getTotalElementsFromService(Service service) {
		return service.getServiceElementList().size();
	}

	public List<SubZertifikat> findSubZertifikatForService(Service service) {
		List<ServiceElements> sList = serviceFacade.findElementsForServiceSortedProofReporting(service, service.getServiceContract());
		List<SubZertifikat> zertList = new ArrayList<>();

		for (ServiceElements se : sList) {
			SubZertifikat zert = new SubZertifikat();
			zert.setElementState(se.getLastState());
			zert.setElementComment(getAggregatedDescriptionFromServiceHistoryList(service, se.getControlDate(), se));
			zert.setElementDate(se.getControlDate());
			zert.setChecklistEntry("");
			zert.setDescription(se.getDescription());
			zert.setElementName(se.getElementnumber());
			zert.setOrientation(se.getOrientation());
			zert.setRoom(se.getRoom());
			zert.setFloor(se.getFloor());
			String url = getFileUrlFromRepairedItem(service, se.getControlDate(), se);
			zert.setUrlFoto(url.replace("/etc", ""));
			zert.setUrlFoto(url);
			zertList.add(zert);

		}

		return zertList;

	}

	public List<SubZertifikat> findSubZertifikatForServiceOld(Service service) {
		QServiceHistory hist = QServiceHistory.serviceHistory;
		String checkListEntry = "";
		String aggDesc = "";
		JPAQuery q = new JPAQuery(this.em);
		q.from(hist);
		q.where(hist.service.eq(service));

		List<ServiceHistory> sList = q.list(hist);

		List<SubZertifikat> zertList = new ArrayList<>();

		for (ServiceHistory sh : sList) {
			SubZertifikat zert = new SubZertifikat();
			if (sh.getCheckListItem() != null && sh.getCheckListItem().getId() != -1) {
				checkListEntry = sh.getCheckListItem().getCheckListItemCategory().getName() + " : "
						+ sh.getCheckListItem().getName();
			} else {
				checkListEntry = "";
			}

			zert.setElementState(getAggregatedStateFromServiceHistoryList(service, sh.getStartTime(), sh.getElement()));
			zert.setElementComment(
					getAggregatedDescriptionFromServiceHistoryList(service, sh.getStartTime(), sh.getElement()));
			zert.setElementDate(sh.getStartTime());
			zert.setChecklistEntry(checkListEntry);
			zert.setDescription(sh.getDescription());
			zert.setElementMangel(sh.getCheckedAsLack());
			zert.setElementName(sh.getElement().getElementnumber());
			zert.setElementOffer(sh.getCreateOffer());
			zert.setElementOk(sh.getCheckedAsOk());

			zert.setOrientation(sh.getElement().getOrientation());
			zert.setRoom(sh.getElement().getRoom());
			zert.setFloor(sh.getElement().getFloor());

			zert.setElementRepair(sh.getCheckedAsRepaired());
			zert.setFunctionalControl(sh.getFunctionalControl());
			zert.setViewControl(sh.getVisualControl());
			String url = getFileUrlFromRepairedItem(service, sh.getStartTime(), sh.getElement());
			zert.setUrlFoto(url.replace("/etc", ""));
			zertList.add(zert);
		}

		return zertList;
	}

	public List<ServiceHistory> findHistForServiceWithoutRegie(Service service) {
		QServiceHistory hist = QServiceHistory.serviceHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(hist);
		q.where(hist.checkListItem.id.ne(ChecklistItemFacade.REGIE_TIME));
		q.where(hist.service.eq(service));
		return q.list(hist);
	}

	public List<ServiceHistory> findHistForService(Service service) {
		QServiceHistory hist = QServiceHistory.serviceHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(hist);
		q.where(hist.service.eq(service));
		return q.list(hist);
	}
	
	public Service findActiveServiceForElement(ServiceElements se){
		Query q = em.createNativeQuery(
				"select id from service where id in (select fk_service from "
				+ "assoc_service_elements where fk_elements = ? ) and active = ? order by id asc");
		q.setParameter(1, se.getId());
		q.setParameter(2, Boolean.TRUE);
		Integer res = null;
		try {			
			res = (Integer) q.getSingleResult();
		} catch (Exception e) {
			
		}
		
		if (res != null && res.intValue() > 0 ){
			return serviceFacade.find(res.intValue());			
		} else {
			return null;
		}
		
	}
	
	public List<ServiceHistory> findChecklistForElementWithoutRegie(ServiceElements se) {
		Service s = findActiveServiceForElement(se);
		if (s != null){
			QServiceHistory hist = QServiceHistory.serviceHistory;
			JPAQuery q = new JPAQuery(this.em);
			q.from(hist);
			q.where(hist.service.eq(s));
			/* q.where(hist.checkListItem.id.ne(ChecklistItemFacade.REGIE_TIME)); */
			q.where(hist.element.eq(se));
			return q.list(hist);			
		} else {
			return null;
		}
		
		

	}
	
	
	public List<ServiceHistory> findChecklistForElementWithoutRegie(ServiceElements se, ServiceContract sc) {
		QServiceHistory hist = QServiceHistory.serviceHistory;
		ServiceContract sco = serviceContractFacade.find(sc.getId()); 
		sco.getServices(); 
		sco.getServices().size(); 
		JPAQuery q = new JPAQuery(this.em);
		q.from(hist);
		q.where(hist.service.in(sco.getServices()));
		/* q.where(hist.checkListItem.id.ne(ChecklistItemFacade.REGIE_TIME)); */
		q.where(hist.element.eq(se));
		return q.list(hist);

	}
	

	public List<ServiceHistory> findChecklistForElementWithoutRegie(ServiceElements se, Service s) {
		QServiceHistory hist = QServiceHistory.serviceHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(hist);
		q.where(hist.service.eq(s));
		/* q.where(hist.checkListItem.id.ne(ChecklistItemFacade.REGIE_TIME)); */
		q.where(hist.element.eq(se));
		return q.list(hist);

	}

	public List<ServiceHistory> findChecklistForElementNotNull(ServiceElements se, Service s) {
		QServiceHistory hist = QServiceHistory.serviceHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(hist);
		q.where(hist.service.eq(s));
		q.where(hist.element.eq(se));
		q.where(hist.startTime.isNull());
		return q.list(hist);

	}

	public List<ServiceHistory> findChecklistForElement(ServiceElements se, Service s) {
		QServiceHistory hist = QServiceHistory.serviceHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(hist);
		q.where(hist.service.eq(s));
		q.where(hist.element.eq(se));
		return q.list(hist);

	}

	public List<ServiceElements> findServiceElementsForService(Service service) {
		QServiceHistory sh = QServiceHistory.serviceHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(sh);
		q.where(sh.service.eq(service));
		List<ServiceHistory> histList = q.list(sh);

		for (ServiceHistory s : histList) {
			
		}

		return null;

	}
	
	public static void main (String[] args){
		HashMap<String, String> m1 = new HashMap<>();
		m1.put("Peter Listig", "Peter Listig" );
		m1.put("Peter Listig", "Peter Listig" );
		m1.put("Peter Listig", "Peter Listig" );
		m1.put("Peter Listig", "Peter Listig" );
		m1.put("Peter Listig", "Peter Listig" );
		m1.put("Theo Dor", "Theo Dor" );
		m1.put("Theo Dor", "Theo Dor" );
		Iterator it = m1.entrySet().iterator();
		while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        System.out.println(pair.getKey() + " = " + pair.getValue());
	        it.remove(); // avoids a ConcurrentModificationException
	    }
		
	}
}
