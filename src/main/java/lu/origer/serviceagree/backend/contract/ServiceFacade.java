package lu.origer.serviceagree.backend.contract;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.omnifaces.util.Faces;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.backend.main.UsersFacade;
import lu.origer.serviceagree.backend.synch.ServiceHistoryFacade;
import lu.origer.serviceagree.backend.synch.TimeRecordingHistoryFacade;
import lu.origer.serviceagree.frontend.main.SessionBean;
import lu.origer.serviceagree.models.checklist.ChecklistItem;
import lu.origer.serviceagree.models.checklist.QServiceElements;
import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contact.Person;
import lu.origer.serviceagree.models.contract.QService;
import lu.origer.serviceagree.models.contract.QServiceContract;
import lu.origer.serviceagree.models.contract.QServiceListView;
import lu.origer.serviceagree.models.contract.Service;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.contract.ServiceListView;
import lu.origer.serviceagree.models.contract.custom.AddServiceType;
import lu.origer.serviceagree.models.main.ObjectComments;
import lu.origer.serviceagree.models.main.QObjectComments;
import lu.origer.serviceagree.models.main.Users;
import lu.origer.serviceagree.models.reporting.IntervalChart;
import lu.origer.serviceagree.models.reporting.IntervalOverview;
import lu.origer.serviceagree.models.synch.QServiceHistory;
import lu.origer.serviceagree.models.synch.QTimeRecordingHistory;
import lu.origer.serviceagree.models.synch.ServiceElementHistory;
import lu.origer.serviceagree.models.synch.ServiceHistory;

@Stateless
@SuppressWarnings("unchecked")
public class ServiceFacade extends AbstractFacade<Service> {

	@Inject
	SessionBean sessionBean;

	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;

	@Inject
	ServiceHistoryFacade serviceHistoryFacade;

	@Inject
	ServiceFacade serviceFacade;

	@Inject
	ServiceElementFacade serviceElementFacade;

	@Inject
	UsersFacade userfacade;

	public static final String STATE_UNGEWARTET = "ungewartet";
	public static final String STATE_REPARIERT = "repariert";
	public static final String STATE_DEFEKT = "defekt";
	public static final String STATE_MANGEL = "mangelhaft";
	public static final String STATE_OK = "ok";
	public static final String STATE_OK_UND_REPARIERT = "repariert / ok";
	public static final String STATE_UNDEFINIERT = "undefiniert";
	public static final String STATE_ANGEBOTEN = "angeboten";
	public static final String STATE_DEFEKT_UND_REPARIERT = "repariert / defekt";
	public static final String STATE_MANGEL_UND_REPARIERT = "repariert / mangelhaft";
	public static final String STATE_REGIEZEIT = "Regiezeit";
	public static final String STATE_REGIEZEIT_OK = "Regiezeit / ok";
	public static final String STATE_REGIEZEIT_DEFEKT = "Regiezeit / defekt";
	public static final String STATE_REGIEZEIT_MANGEL = "Regiezeit / mangelhaft ";
	public static final String STATE_CUSTOMER_RECLAMATION = "reklamiert / defekt ";
	public static final String STATE_CUSTOMER_RECLAMATION_OK = "reklamiert / ok ";
	public static final String STATE_CUSTOMER_RECLAMATION_MANGEL = "reklamiert / mangelhaft ";
	public static final String STATE_CUSTOMER_RECLAMATION_DEKEFT = "reklamiert / defekt ";

	public static final String STATE_UNGEWARTET_FR = "Sans entretien";
	public static final String STATE_REPARIERT_FR = "Réparé";
	public static final String STATE_DEFEKT_FR = "Défectueux";
	public static final String STATE_MANGEL_FR = "Non conforme";
	public static final String STATE_OK_FR = "Ok";
	public static final String STATE_OK_UND_REPARIERT_FR = "Réparé / ok";
	public static final String STATE_UNDEFINIERT_FR = "Non défini";
	public static final String STATE_ANGEBOTEN_FR = "Devis";
	public static final String STATE_DEFEKT_UND_REPARIERT_FR = "Réparé / en panne";
	public static final String STATE_MANGEL_UND_REPARIERT_FR = "Réparé / défaut";
	public static final String STATE_REGIEZEIT_FR = "Régie";
	public static final String STATE_REGIEZEIT_OK_FR = "Régie / ok";
	public static final String STATE_REGIEZEIT_DEFEKT_FR = "Régie / en panne";
	public static final String STATE_REGIEZEIT_MANGEL_FR = "Régie / défaut";
	public static final String STATE_CUSTOMER_RECLAMATION_FR = "réclamation / en panne ";
	public static final String STATE_CUSTOMER_RECLAMATION_OK_FR = "réclamation / ok ";
	public static final String STATE_CUSTOMER_RECLAMATION_MANGEL_FR = "réclamation / défaut";
	public static final String STATE_CUSTOMER_RECLAMATION_DEKEFT_FR = "réclamation / en panne ";

	public ServiceFacade() {
		super(Service.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public Integer getServiceContractIdFromService(Integer serviceId) {
		Query q = em.createNativeQuery("select fk_service_contract from service where id = ? ");
		q.setParameter(1, serviceId);
		Integer res = (Integer) q.getSingleResult();
		return res;
	}

	public Integer getNextServiceIntervall(Integer serviceTypeId, Integer serviceContractId) {
		Query q = em.createNativeQuery("select max(intervall) as count from service "
				+ "where fk_service_contract = ? and fk_service_type = ? ");
		q.setParameter(1, serviceContractId);
		q.setParameter(2, serviceTypeId);
		Integer res = (Integer) q.getSingleResult();
		if (res != null) {
			return res + 1;
		}
		return 1;
	}

	public Boolean serviceTypeActiveIntervallAllreadyExist(AddServiceType a, ServiceContract sc) {
		Query q = em.createNativeQuery("select count(*) as count  from service where fk_service_contract = ? "
				+ "and fk_service_type = ? and active = 1 ");
		q.setParameter(1, sc.getId());
		q.setParameter(2, a.getServiceType().getId());

		Long res = (Long) q.getSingleResult();
		if (res != null) {
			return res > 0;
		}
		return false;
	}

	public Boolean historyForElementAllreadyGenerated(Integer serviceId, Integer elementId) {
		Query q = em.createNativeQuery(
				"select count(*) as count from service_history where fk_service = ? and fk_element = ?");
		q.setParameter(1, serviceId);
		q.setParameter(2, elementId);
		Long res = (Long) q.getSingleResult();
		if (res != null) {
			return res > 0;
		}
		return false;
	}

	public Boolean historyAllreadyGenerated(Service service) {
		Query q = em.createNativeQuery("select count(*) as count from service_history where fk_service = ?");
		q.setParameter(1, service.getId());
		Long res = (Long) q.getSingleResult();
		if (res != null) {
			return res > 0;
		}
		return false;
	}

	public Boolean historyAllreadySynchronized(Service service, ServiceElements se) {
		Query q = em.createNativeQuery(
				"select count(*) as count from service_history where fk_service = ? and fk_element = ? and starttime is not null");
		q.setParameter(1, service.getId());
		q.setParameter(2, se.getId());
		Long res = (Long) q.getSingleResult();
		if (res != null) {
			return res > 0;
		}
		return false;
	}

	public Boolean historyAllreadySynchronized(Service service) {
		Query q = em.createNativeQuery(
				"select count(*) as count from service_history where fk_service = ? and starttime is not null");
		q.setParameter(1, service.getId());
		Long res = (Long) q.getSingleResult();
		if (res != null) {
			return res > 0;
		}
		return false;
	}

	public void setNextServiceActive(Service s, Integer sc) {
		QService ser = QService.service;
		JPAQuery q = new JPAQuery(this.em);
		Integer accActive = s.getIntervall();
		System.out.println("Aktueller Intervall: " + accActive);
		q.from(ser);
		if (sc != null) {
			q.where(ser.serviceContract.id.eq(sc));
		} else {
			q.where(ser.serviceContract.eq(s.getServiceContract()));
		}
		q.where(ser.serviceType.eq(s.getServiceType()));
		q.orderBy(ser.intervall.asc());

		List<Service> sList = q.list(ser);
		if (sList != null && sList.size() > 1) {
			for (Service se : sList) {
				if (se.getIntervall() > accActive) {
					System.out.println("Nächster Intervall: " + se.getIntervall());
					se.setActive(true);
					edit(se);
					break;
				}
			}
		}
		// aktueller Intervall ist abgeschlossen
		s.setActive(false);
		edit(s);
	}

	public Date getLastControlDateForElement(Service s, ServiceElements e) {
		Query q = em.createNativeQuery(
				"select max(starttime) maxDate from service_history " + "where fk_element = ?  and fk_service = ?");
		/* + "where fk_element = ? " + "and fk_checklistitem != -1 "); */
		q.setParameter(1, e.getId());
		q.setParameter(2, s.getId());
		Date d = (Date) q.getSingleResult();
		return d;
	}

	public Date getLastControlDateForElementByService(Service s, ServiceElements e, ServiceContract sc) {
		Query q;
		if (s == null) {
			q = em.createNativeQuery("select max(starttime) maxDate from service_history where "
					+ "fk_element = ? and fk_service in (select id from service where fk_service_contract = ? ) ");
			q.setParameter(1, e.getId());
			q.setParameter(2, sc.getId());
		} else {
			q = em.createNativeQuery(
					"select max(starttime) maxDate from service_history where " + "fk_element = ? and fk_service = ? ");
			q.setParameter(1, e.getId());
			q.setParameter(2, s.getId());
		}
		/*
		 * + "fk_element = ? and fk_service = ? and fk_checklistitem != -1 ");
		 */

		Date d = (Date) q.getSingleResult();
		return d;
	}

	public List<ServiceElements> findElementsForServiceSortedProofReporting(Service s, ServiceContract sc) {
		s = eagerLoadingServiceContract(s);

		List<ServiceElements> el = s.getServiceElementList();

		for (ServiceElements se : el) {
			// System.out.println("Element: " + se.getElementnumber());
			Date lastControlDate = getLastControlDateForElementByService(s, se, sc);
			// System.out.println("Datum: " + lastControlDate);
			// System.out.println("Service-Intervall: " + s.getIntervall());
			se.setLastControlDate(lastControlDate);
			se.setLastState(serviceHistoryFacade.getAggregatedStateFromAllServiceHistoryList(lastControlDate, se));
			// se.setLastState(findState(se,s));
		}
		// Sorting
		Collections.sort(el, new Comparator<ServiceElements>() {
			@Override
			public int compare(ServiceElements s1, ServiceElements s2) {

				return s1.getElementnumber().compareTo(s2.getElementnumber());
			}
		});

		return el;

	}

	public List<ServiceElements> findAllElementsForServiceContract(Integer serviceContractID, Integer serviceTypeId) {
		Query q = em.createNativeQuery(
				"select fk_elements from assoc_service_elements " + "where fk_service in (select id from service where "
						+ "fk_service_contract = ? and fk_service_type = ?  ) group by fk_elements ");

		q.setParameter(1, serviceContractID);
		q.setParameter(2, serviceTypeId);

		List<Integer> res = (List<Integer>) q.getResultList();

		QServiceElements se = QServiceElements.serviceElements;
		JPAQuery qu = new JPAQuery(this.em);
		qu.from(se);
		qu.where(se.id.in(res));

		return qu.list(se);
	}

	public List<ServiceHistory> getServiceHistoryForContract(final Integer contractId, final Integer serviceType) {
		Query query = em.createNativeQuery("select se.id, se.elementnumber, se.room, se.`floor`, se.orientation, sh.starttime, sh.isok, sh.isdefect, sh.islacking, sh.isRepaired, sh.fk_checklistitem from service_contract sc "
				+"inner join service s on sc.id = s.fk_service_contract "
				+"inner join assoc_service_elements ase on s.id = ase.fk_service " 
				+"inner join service_elements se on se.id = ase.fk_elements " 
				+"left join service_history sh on sh.fk_element = ase.fk_elements "
				+ "where sc.id = ?1 and s.fk_service_type = ?2 " + "order by sh.fk_element asc, sh.starttime desc");
		query.setParameter(1, contractId);
		query.setParameter(2, serviceType);
		// query.from(history).where(history.service.serviceContract.id.eq(contractId).and(history.startTime.isNotNull())).orderBy(history.element.id.asc(),
		// history.startTime.desc());
		final List<ServiceHistory> history = new ArrayList<>();
		List<Object[]> results = query.getResultList();
		for(Object[] record : results)
		{
			ServiceElements element = new ServiceElements();
			element.setId((Integer) record[0]);
			element.setElementnumber((String)record[1]);
			element.setRoom((String)record[2]);
			element.setFloor((String) record[3]);
			element.setOrientation((String)record[4]);
			ServiceHistory entry = new ServiceHistory();
			entry.setStartTime((Date) record[5]);
			entry.setCheckedAsOk((Boolean) record[6]);
			entry.setCheckedAsDefect((Boolean) record[7]);
			entry.setCheckedAsLack((Boolean) record[8]);
			entry.setCheckedAsRepaired((Boolean) record[9]);
			entry.setElement(element);
			entry.setCheckListItem(new ChecklistItem((Integer)record[10]));
			history.add(entry);
		};
		return history;
	}

	/**
	 * Fetches the element history for the selected service.
	 * 
	 * @param service
	 * @return
	 */
	public List<ServiceElements> fetchElementHistory(final Service service) {		
		// Fetch all service history entries & elements for the contract of the selected service.
		final List<ServiceHistory> history = this.getServiceHistoryForContract(service.getServiceContract().getId(),
				service.getServiceType().getId());
		final List<ServiceElements> result = new ArrayList<>();
		if (history != null && !history.isEmpty()) {
			Integer lastElementId = null;
			Date lastElementDate = null;
			final ArrayList<ServiceHistory> currentHistory = new ArrayList<>();
			// Iterate through complete history and fetch the latest entries for the individual entries.
			for (ServiceHistory entry : history) {
				if (lastElementId == null && lastElementDate == null) {
					// Is first entry, set values.
					currentHistory.add(entry);
					lastElementId = entry.getElement().getId();
					lastElementDate = entry.getStartTime();
				} else {
					if (!lastElementId.equals(entry.getElement().getId())) 
					{
						// Reached different element, create entry.
						if(!currentHistory.isEmpty())
						{
							final ServiceElements element = currentHistory.get(0).getElement();
							element.setLastControlDate(currentHistory.get(0).getStartTime());	
							if(element.getLastControlDate() != null)
							{
								// Element has been checked, calculate status.
								element.setLastState(this.serviceHistoryFacade.getStateFromServiceHistoryList(currentHistory));
							}
							else
							{
								// Element has not yet been checked, set status to unchecked.
								if (sessionBean.getUserLocale().equals(Locale.GERMANY)) {
									element.setLastState(ServiceFacade.STATE_UNGEWARTET);
								} else {
									element.setLastState(ServiceFacade.STATE_UNGEWARTET_FR);
								}
							}
							result.add(element);
						}					
						// Reset value and continue for new element
						currentHistory.clear();
						lastElementId = entry.getElement().getId();
						lastElementDate = entry.getStartTime();
						currentHistory.add(entry);
					} else {
						// Add entry if date equals latest date.
						if((entry.getStartTime() != null && lastElementDate != null) && (lastElementDate.equals(entry.getStartTime())) || (lastElementDate == null))
						{
							currentHistory.add(entry);
						}
					}
				}
			}
			// Add final element.
			if(!currentHistory.isEmpty())
			{
				final ServiceElements element = currentHistory.get(0).getElement();
				element.setLastControlDate(currentHistory.get(0).getStartTime());	
				if(element.getLastControlDate() != null)
				{
					// Element has been checked, calculate status.
					element.setLastState(this.serviceHistoryFacade.getStateFromServiceHistoryList(currentHistory));
				}
				else
				{
					// Element has not yet been checked, set status to unchecked.
					if (sessionBean.getUserLocale().equals(Locale.GERMANY)) {
						element.setLastState(ServiceFacade.STATE_UNGEWARTET);
					} else {
						element.setLastState(ServiceFacade.STATE_UNGEWARTET_FR);
					}
				}
				result.add(element);
			}							
		}
		return result;
	}	

	public List<ServiceElements> findElementsForServiceSorted(Service s) {
		s = eagerLoadingServiceContract(s);

		// List<ServiceElements> el = s.getServiceElementList();

		List<ServiceElements> el = new ArrayList<>(
				findAllElementsForServiceContract(s.getServiceContract().getId(), s.getServiceType().getId()));

		List<ObjectComments> allComments = this.serviceElementFacade.getObjectComments(ServiceElements.class);

		for (ServiceElements se : el) {
			// System.out.println("Element: " + se.getElementnumber());
			Date lastControlDate = getLastControlDateForElement(s, se);
			// System.out.println("Datum: " + lastControlDate);
			List<ObjectComments> addComments = new ArrayList<>();
			addComments = allComments.stream().filter(p -> p.getObjectId().equals(se.getId()))
					.collect(Collectors.toList());
			se.setComments(addComments);
			// System.out.println("Service-Intervall: " + s.getIntervall());
			se.setLastControlDate(lastControlDate);
			final List<ServiceElementHistory> entries = serviceHistoryFacade.findHistoryElementFromSelected(se, s);

			final List<ServiceElementHistory> entriesWithoutComments = entries.stream()
					.filter(q -> !q.getState().equals("-")).collect(Collectors.toList());

			if (entriesWithoutComments != null && !entriesWithoutComments.isEmpty()) {
				se.setLastState(entriesWithoutComments.get(entriesWithoutComments.size() - 1).getState());
			} else {
				if (sessionBean.getUserLocale().equals(Locale.GERMANY)) {
					se.setLastState(ServiceFacade.STATE_UNGEWARTET);
				} else {
					se.setLastState(ServiceFacade.STATE_UNGEWARTET_FR);
				}
			}
		}
		// Sorting
		Collections.sort(el, new Comparator<ServiceElements>() {
			@Override
			public int compare(ServiceElements s1, ServiceElements s2) {

				return s1.getElementnumber().compareTo(s2.getElementnumber());
			}
		});

		return el;

	}

	public Boolean isNotControlled(List<ServiceHistory> histList) {
		for (ServiceHistory s : histList) {
			if (s.getVisualControl().equals(false) || s.getFunctionalControl().equals(false)) {
				return true;
			}
		}
		return false;

	}

	public Boolean isRepaired(List<ServiceHistory> histList) {
		for (ServiceHistory s : histList) {
			if (s.getCheckListItem() == null) {
				return true;
			}
		}
		return false;
	}

	public Boolean isLacking(List<ServiceHistory> histList) {
		for (ServiceHistory s : histList) {
			if (s.getCheckedAsLack()) {
				return true;
			}
		}
		return false;
	}

	public Boolean isDefect(List<ServiceHistory> histList) {
		for (ServiceHistory s : histList) {
			if (s.getCheckedAsDefect()) {
				return true;
			}
		}
		return false;
	}

	public Boolean isOK(List<ServiceHistory> histList) {
		Boolean erg = false;
		for (ServiceHistory s : histList) {
			if (s.getCheckedAsOk()) {
				erg = true;
			}
		}
		return erg;
	}

	public String findState(ServiceElements se, Service ser) {
		List<ServiceHistory> list = historyListForElement(se, ser);
		if (isNotControlled(list)) {
			return ServiceFacade.STATE_UNGEWARTET;
		} else if (isRepaired(list)) {
			return ServiceFacade.STATE_REPARIERT;
		} else if (isDefect(list)) {
			return ServiceFacade.STATE_DEFEKT;
		} else if (isLacking(list)) {
			return ServiceFacade.STATE_MANGEL;
		} else if (isOK(list)) {
			return ServiceFacade.STATE_OK;
		} else {
			return ServiceFacade.STATE_UNDEFINIERT;
		}
	}

	public Date findControlDate(ServiceElements se, Service ser) {
		List<ServiceHistory> list = historyListForElement(se, ser);
		Date controlDate = null;
		for (ServiceHistory s : list) {
			if (s.getStartTime() != null) {
				controlDate = s.getStartTime();

			}
		}
		return controlDate;
	}

	public List<ServiceHistory> historyListForElement(ServiceElements se, Service ser) {
		QServiceHistory sh = QServiceHistory.serviceHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(sh);
		q.where(sh.element.eq(se));
		q.where(sh.service.eq(ser));
		List<ServiceHistory> list = q.list(sh);
		return list;
	}

	public List<ServiceElements> findServiceElementsMappedWithHist(Service s) {

		List<ServiceElements> se = s.getServiceElementList();
		for (ServiceElements e : se) {
			e.setControlDate(findControlDate(e, s));
			e.setState(findState(e, s));
		}
		return se;
	}

	public Integer countedElements(Service s) {

		Query q = em.createNativeQuery("select id from service_history where fk_service = ? group by fk_element ");
		q.setParameter(1, s.getId());

		List<Integer> res = (List<Integer>) q.getResultList();

		if (res != null) {
			return res.size();
		}
		return 0;
		////
		//
		//
		// QServiceHistory sh = QServiceHistory.serviceHistory;
		// JPAQuery q = new JPAQuery(this.em);
		// q.from(sh);
		// q.where(sh.service.eq(s));
		// q.groupBy(sh.element);
		// return q.list(sh.element).size();
	}

	public Integer elementsChecked(Service s) {

		Query q = em.createNativeQuery(
				"select id from service_history d where fk_service = ? and starttime is not null and endtime is not null and (visualcontrol is true or functionalcontrol is true) group by fk_element");
		q.setParameter(1, s.getId());

		List<Integer> res = (List<Integer>) q.getResultList();

		if (res != null) {
			return res.size();
		}
		return 0;

		// QServiceHistory sh = QServiceHistory.serviceHistory;
		// JPAQuery q = new JPAQuery(this.em);
		// q.from(sh);
		// q.where(sh.service.eq(s));
		// q.where(sh.startTime.isNotNull());
		// // FK und SK werden pro Checkliste frei definiert, daher
		// // kontrolle ob vc order fc!
		// q.where(sh.visualControl.isTrue().or(sh.functionalControl.isTrue()));
		// q.groupBy(sh.element);
		// return q.list(sh.element).size();
	}

	private Integer countPerType(Service s) {		
		if(s != null && s.getServiceContract() != null && s.getServiceContract().getId() != null)
		{
			Query q = em.createNativeQuery(
					"select count(*) from service where fk_service_contract = ? and fk_service_type = ?");
			q.setParameter(1, s.getServiceContract().getId());
			q.setParameter(2, s.getServiceType().getId());
			Long res = (Long) q.getSingleResult();
			if (res != null) {
				return Math.toIntExact(res);
			}
		}
		return 0;
		//
		// Integer result = 0;
		// QService ss = QService.service;
		// JPAQuery q = new JPAQuery(this.em);
		// q.from(ss);
		// q.where(ss.serviceContract.eq(s.getServiceContract()));
		// q.where(ss.serviceType.eq(s.getServiceType()));
		// List<Service> sList = q.list(ss);
		// if (sList != null && sList.size() > 0) {
		// result = sList.size();
		// }
		//
		// return result;
	}

	private Integer countPerType(ServiceListView s) {
		Integer result = 0;
		QService ss = QService.service;
		JPAQuery q = new JPAQuery(this.em);
		q.from(ss);
		q.where(ss.serviceContract.id.eq(s.getServiceContractId()));
		q.where(ss.serviceType.id.eq(s.getServiceTypeId()));
		List<Service> sList = q.list(ss);
		if (sList != null && sList.size() > 0) {
			result = sList.size();
		}

		return result;
	}

	public Boolean openOffersExists() {

		Query q = em.createNativeQuery("select t1.fk_service from assoc_service_elements t1, "
				+ "service_elements t2 where t1.fk_elements = t2.id "
				+ "and t2.createOffer = true group by t1.fk_service");

		List<Integer> res = (List<Integer>) q.getResultList();

		return res != null && res.size() > 0;
	}

	public List<Service> findAllServicesOpenOffer() {

		Query q = em.createNativeQuery("select t1.fk_service from assoc_service_elements t1, "
				+ "service_elements t2 where t1.fk_elements = t2.id "
				+ "and t2.createOffer = true group by t1.fk_service");

		List<Integer> res = (List<Integer>) q.getResultList();

		QService s = QService.service;
		JPAQuery qu = new JPAQuery(this.em);
		qu.from(s);
		qu.where(s.active.isTrue());
		qu.where(s.id.in(res));

		List<Service> sList = qu.list(s);
		for (Service x : sList) {
			x.getServiceContract();
			x.getServiceContract().getBuildingSite();
		}

		return getServiceListView(sList);

		//
		// return res;
		//
		// QService s = QService.service;
		// JPAQuery q = new JPAQuery(this.em);
		// Boolean isOffer = false;
		// q.from(s);
		// List<Service> result = new ArrayList<>();
		//
		// List<Service> sList = q.list(s);
		// for (Service ser : sList) {
		// for (ServiceElements se : ser.getServiceElementList()) {
		// if (se.isCreateOffer()) {
		// isOffer = true;
		// }
		//
		// }
		// if (isOffer) {
		// Service service = ser;
		// result.add(service);
		// isOffer = false;
		// }
		// }
		//
		// return getServiceListView(result);
	}

	public List<Service> findAllServicesForServiceContract(ServiceContract sc) {

		QService s = QService.service;
		JPAQuery q = new JPAQuery(this.em);
		q.from(s);
		q.where(s.serviceContract.in(sc));
		List<Service> sList = q.list(s);
		return getServiceListView(sList);
	}

	public List<Service> findAllServicesForCustomer(Users user) {
		List<ServiceContract> con = user.getServiceContractList();

		QService s = QService.service;
		JPAQuery q = new JPAQuery(this.em);
		q.from(s);
		q.where(s.serviceContract.in(con));
		List<Service> sList = q.list(s);
		return getServiceListView(sList);
	}

	public List<ServiceListView> findAllServicesForCustomerList(Users user) {
		List<ServiceContract> con = user.getServiceContractList();
		List<Integer> conInt = new ArrayList<>();
		for (ServiceContract c : con) {
			conInt.add(c.getId());
		}

		QServiceListView s = QServiceListView.serviceListView;
		JPAQuery q = new JPAQuery(this.em);
		q.from(s);
		q.where(s.serviceContractId.in(conInt));
		List<ServiceListView> sList = q.list(s);
		for (ServiceListView se : sList) {
			se.setCountPerType(countPerType(se));
		}
		return q.list(s);
	}

	public Integer getTotalSecondsFromHist(Service s) {
		QTimeRecordingHistory th = QTimeRecordingHistory.timeRecordingHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(th);
		q.where(th.type.eq(TimeRecordingHistoryFacade.ISTZEIT_TR));
		q.where(th.service.eq(s));
		List<Integer> timeList = q.list(th.timeInSeconds);
		Integer sumTime = 0;
		for (Integer i : timeList) {
			sumTime += i;
		}
		return sumTime;
	}

	// public String gethhmmssFromSeconds(Integer timeInSec){
	// DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	// Duration duration = Duration.ofSeconds(timeInSec);
	// Date reference = dateFormat.parse("00:00:00");
	// Date date = dateFormat.parse(string);
	// long seconds = (date.getTime() - reference.getTime()) / 1000L;
	// }

	public static String getWithLeadingZero(Integer val) {
		String res = "";
		if (val < 10) {
			if (val == 0) {
				res = "00";
			} else {
				res = "0" + val.toString();
			}
		} else {
			res = val.toString();
		}
		return res;
	}

	public static void main(String[] args) {
		System.out.println("stest");
		System.out.println(splitToComponentTimeString(1239));
		BigDecimal test = new BigDecimal("3601");
		BigDecimal test2 = new BigDecimal("200");
		System.out.println(test.divide(BigDecimal.valueOf(3600L), BigDecimal.ROUND_FLOOR));
		System.out.println(test2.divide(BigDecimal.valueOf(3600L), BigDecimal.ROUND_FLOOR));

		System.out.println(test.divide(BigDecimal.valueOf(3600L), BigDecimal.ROUND_UP).intValue());
		System.out.println(test2.divide(BigDecimal.valueOf(3600L), BigDecimal.ROUND_UP).intValue());

		// ist =
		// getActualTimeFromService(s).divide(BigDecimal.valueOf(3600L),BigDecimal.ROUND_FLOOR);
	}

	public static String splitToComponentTimeString(Integer sec) {
		Integer hours = sec / 3600;
		Integer remainder = (Integer) sec - hours * 3600;
		Integer mins = remainder / 60;
		String res = "";

		String h = getWithLeadingZero(hours);
		String m = getWithLeadingZero(mins);

		return h + ":" + m;

	}

	//
	public List<Service> getServiceListView(List<Service> sList) {
		for (Service se : sList) {
			se.setCountPerType(countPerType(se));
			se.setTotalElements(countedElements(se));
			se.setElememtsChecked(elementsChecked(se));
			Integer time = getTotalSecondsFromHist(se);
			se.setAcctualTime(splitToComponentTimeString(time));
			// Float f = 0F;
			// if (time > 0){
			// f = time.floatValue() / 60 / 60 ;
			// }
			// se.setActualAmount(f);

			Integer val = 0;
			Float valF = 0F;
			if (se.getElememtsChecked() != null && se.getTotalElements() != null && se.getElememtsChecked() > 0
					&& se.getTotalElements() > 0) {
				val = (se.getElememtsChecked() / se.getTotalElements()) * 100;
				valF = (float) ((se.getElememtsChecked() / se.getTotalElements()) * 100);
			}
			se.setPercentReady(valF);
			se.setHorizontalBarModel(createHorizontalBarModel(se.getElememtsChecked(), se.getTotalElements(), se));
			if (se.getServiceElementList() != null) {
				se.getServiceElementList().size();
			}

		}

		return sList;
	}

	public List<ServiceListView> findAllServicesView() {
		QServiceListView s = QServiceListView.serviceListView;
		JPAQuery q = new JPAQuery(this.em);
		q.from(s);
		List<ServiceListView> sList = q.list(s);
		for (ServiceListView se : sList) {
			se.setCountPerType(countPerType(se));
		}
		return q.list(s);
	}

	public Service eagerLoadingServiceContract(Service service) {

		Service s = this.find(service.getId());
		try {
			s.getServiceElementList().size();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return s;

	}

	private HorizontalBarChartModel createHorizontalBarModel(Integer checked, Integer total, Service s) {
		Float result = 0F;
		Float resultBg = 0F;
		System.out.println("total:" + total);
		if (checked != null && checked != 0 && total != null && total != 0) {
			result = Float.valueOf(checked) / total * 100;
			// if (s.isReady()){
			// result = 100F;
			// }
		}
		HorizontalBarChartModel horizontalBarModel = new HorizontalBarChartModel();
		ChartSeries completionState = new ChartSeries();
		ChartSeries backgroundState = new ChartSeries();
		completionState.setLabel("");
		completionState.set("", result);
		backgroundState.setLabel("");
		if (result != null && result > 0) {
			resultBg = 100 - result;
		} else {
			resultBg = 100F;
		}
		backgroundState.set("", resultBg);

		horizontalBarModel.addSeries(completionState);
		horizontalBarModel.addSeries(backgroundState);
		horizontalBarModel.setSeriesColors("00983f, d6d6d6");
		horizontalBarModel.setShowPointLabels(true);
		horizontalBarModel.setLegendRows(0);
		horizontalBarModel.setStacked(true);
		horizontalBarModel.setShadow(false);
		horizontalBarModel.setAnimate(true);
		Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
		xAxis.setMin(0);
		xAxis.setMax(125);
		Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);

		return horizontalBarModel;
	}

	public List<Service> findAllServicesForCustomer(Integer user) {
		List<ServiceContract> con = userfacade.find(user).getServiceContractList();
		QService s = QService.service;
		JPAQuery q = new JPAQuery(this.em);
		q.from(s);
		q.where(s.serviceContract.in(con));
		List<Service> sList = q.list(s);
		return getServiceListView(sList);
	}

	public List<Service> findAllServicesForCustomerGrouped(Integer user) {

		Query qry = em.createNativeQuery("select max(s.id) from service s where (s.active = 1 or s.ready = 1) "
				+ "group by  s.fk_service_type, s.fk_service_contract ");

		List<Integer> res = (List<Integer>) qry.getResultList();

		List<ServiceContract> con = userfacade.find(user).getServiceContractList();
		QService s = QService.service;
		JPAQuery q = new JPAQuery(this.em);
		q.from(s);
		// q.where(s.serviceContract.in(con));
		q.where(s.id.in(res));
		List<Service> sList = q.list(s);
		return getServiceListView(sList);
	}

	public List<Service> findAllServicesGrouped() {

		Query qry = em.createNativeQuery("select max(s.id) from service s where (s.active = 1 or s.ready = 1) "
				+ "group by  s.fk_service_type, s.fk_service_contract ");
		List<Integer> res = (List<Integer>) qry.getResultList();
		QService s = QService.service;
		JPAQuery q = new JPAQuery(this.em);
		q.from(s);
		q.where(s.id.in(res));
		q.orderBy(s.active.desc(), s.intervall.asc());
		List<Service> sList = q.list(s);
		return getServiceListView(sList);
		// return sList;
	}

	public List<Service> findAllServices() {
		QService s = QService.service;
		JPAQuery q = new JPAQuery(this.em);
		q.from(s);
		q.orderBy(s.active.desc(), s.intervall.asc());
		List<Service> sList = q.list(s);
		return getServiceListView(sList);
	}

	public List<Service> findServiceFromServiceContract(ServiceContract sc) {
		QService s = QService.service;
		JPAQuery q = new JPAQuery(this.em);
		q.from(s);
		q.where(s.serviceContract.eq(sc));
		q.orderBy(s.active.desc(), s.intervall.asc());
		List<Service> sList = q.list(s);
		return getServiceListView(sList);

	}
	
	/**
	 * Fetches all services from the first intervall of a service contract.
	 * Required for contract renewal to prevent duplicate generation.
	 * 
	 * @param sc The service contract 
	 * @return
	 */
	public List<Service> findFirstServicesFromServiceContract(ServiceContract sc) {
		QService s = QService.service;
		JPAQuery q = new JPAQuery(this.em);
		q.from(s);
		q.where(s.serviceContract.eq(sc).and(s.intervall.eq(1)));
		q.orderBy(s.active.desc(), s.intervall.asc());
		List<Service> sList = q.list(s);
		return getServiceListView(sList);

	}

	public List<Service> findByName(String find) {
		QService s = QService.service;
		JPAQuery q = new JPAQuery(this.em);
		q.from(s);
		q.where(s.active.isTrue());
		q.where(s.comment.containsIgnoreCase(find));
		return q.list(s);
	}

	public List<Service> completeService(String filter) {
		ValueExpression exp = Faces.getApplication().getExpressionFactory()
				.createValueExpression(Faces.getContext().getELContext(), "#{ser.name}", String.class);
		AutoComplete autoComplete = (AutoComplete) UIComponent.getCurrentComponent(Faces.getContext());
		autoComplete.setValueExpression("itemLabel", exp);
		List<Service> serviceList = this.findByName(filter);
		if (serviceList == null) {
			List<Service> c = new ArrayList<>();
			return c;
		}
		return serviceList;
	}

	public BigDecimal getActualTimeFromService(Service s) {
		QTimeRecordingHistory trh = QTimeRecordingHistory.timeRecordingHistory;

		JPAQuery q = new JPAQuery(this.em);
		q.from(trh);
		q.where(trh.service.eq(s));
		q.where(trh.type.eq(TimeRecordingHistoryFacade.ISTZEIT_TR));
		Integer sumSeconds = q.singleResult(trh.timeInSeconds.sum());

		if (sumSeconds == null) {
			sumSeconds = 0;
		}
		return BigDecimal.valueOf(sumSeconds);

	}

	public String getMonteureFromService(Service s) {
		QTimeRecordingHistory trh = QTimeRecordingHistory.timeRecordingHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(trh);
		q.where(trh.service.eq(s));
		q.groupBy(trh.mechanic);
		List<Person> mechList = q.list(trh.mechanic);
		String result = "";
		if (mechList != null && mechList.size() > 0) {
			Boolean firstRun = true;
			for (Person p : mechList) {
				if (firstRun) {
					result = p.getFirstname() + ", " + p.getLastname();
				} else {
					result += "\n" + p.getFirstname() + ", " + p.getLastname();
				}
			}
		}

		return result;
	}

	public List<IntervalOverview> findIntervallOverview(ServiceContract sc) {
		// TODO Auto-generated method stub
		QService ser = QService.service;
		JPAQuery q = new JPAQuery(this.em);
		q.from(ser);
		q.where(ser.serviceContract.eq(sc));
		List<Service> sList = q.list(ser);

		List<IntervalOverview> ovList = new ArrayList<>();

		for (Service s : sList) {
			IntervalOverview io = new IntervalOverview();
			BigDecimal ist = BigDecimal.ZERO;
			List<IntervalChart> chartList = new ArrayList<>();

			if (getActualTimeFromService(s) != null) {
				ist = getActualTimeFromService(s).divide(BigDecimal.valueOf(3600L), BigDecimal.ROUND_UP);
			}

			BigDecimal soll = BigDecimal.ZERO;
			if (s.getTargetAmount() != null) {
				soll = new BigDecimal(s.getTargetAmount().toString());
			}

			BigDecimal rest = soll.subtract(ist);

			io.setEstimatedTime(rest);

			BigDecimal serviceContrat = BigDecimal.ZERO;
			if (s.getServiceAmount() != null) {
				serviceContrat = new BigDecimal(s.getServiceAmount().toString());
			}
			io.setContractValue(serviceContrat);

			io.setMonteure(getMonteureFromService(s));
			if (s.getServiceContract() != null) {
				io.setServiceContract(s.getServiceContract().getServiceContractNumber());
			}
			io.setServiceInterval(s.getIntervall());
			io.setActualTime(ist);
			io.setTargetTime(soll);

			io.setIntervallName("Int.: " + s.getIntervall() + " - " + s.getServiceType().getName());

			IntervalChart ic = new IntervalChart();
			ic.setState(ist.intValue());
			ic.setName("Ist");
			ic.setIntervallName("Int.: " + s.getIntervall() + " - " + s.getServiceType().getName());
			chartList.add(ic);
			ic = new IntervalChart();
			ic.setName("Rest");
			ic.setState(rest.intValue());
			ic.setIntervallName("Int.: " + s.getIntervall() + " - " + s.getServiceType().getName());
			chartList.add(ic);

			io.setChartData(chartList);
			if (io.getActualTime() != null && io.getServiceInterval() != null) {
				ovList.add(io);
			}

		}

		return ovList;
	}

	public List<Service> findByTimeIntervalAndActive(final Date startDate, final Date endDate) {
		if (startDate != null && endDate != null) {
			QService service = QService.service;
			JPAQuery query = new JPAQuery(this.em);

			query.from(service)
					.where(service.latestServiceDate.before(endDate).and(service.latestServiceDate.after(startDate)));

			return query.list(service);
		} else {
			return null;
		}
	}

	public Integer getLatestId() {
		final QService service = QService.service;
		JPAQuery query = new JPAQuery(this.em);
		return query.from(service).limit(1).orderBy(service.id.desc()).singleResult(service.id);
	}

	public void setEntityManager(final EntityManager em) {
		this.em = em;
	}

	public Date findLastDateFromService(Service service) {
		// TODO Auto-generated method stub
		QServiceHistory h = QServiceHistory.serviceHistory;
		JPAQuery q = new JPAQuery(this.em);
		q.from(h);
		q.where(h.service.eq(service));
		return q.singleResult(h.endTime.max());

	}

}
