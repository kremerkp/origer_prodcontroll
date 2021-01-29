package lu.origer.serviceagree.backend.contract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hsqldb.result.Result;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.assocs.QAssoc_Service_Elements;
import lu.origer.serviceagree.models.checklist.QServiceElements;
import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contract.Offers;
import lu.origer.serviceagree.models.contract.OpenOfferElements;
import lu.origer.serviceagree.models.contract.QService;
import lu.origer.serviceagree.models.contract.Service;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.synch.ServiceHistory;

@Stateless
public class ServiceElementFacade extends AbstractFacade<ServiceElements> {

	@Inject
	ServiceContractFacade serviceContractFacade;

	@Inject
	OfferFacade offerFacade;

	public ServiceElementFacade() {
		super(ServiceElements.class);
	}

	public List<OpenOfferElements> findOpenOffers() {
		List<OpenOfferElements> resultList = new ArrayList<>();
		QServiceElements se = QServiceElements.serviceElements;
		JPAQuery q = new JPAQuery(this.em);
		q.from(se);
		q.where(se.createOffer.isTrue());
		//TODO: Elemente die Bereits in einem noch nicht abgeschlossenen Angeobt 
		
		if (q.list(se) != null && q.list(se).size() > 0) {
			for (ServiceElements e : q.list(se)) {
				/**/
				// List<Integer> offers;
				// Query qry = em
				// .createNativeQuery("select fk_offer from " +
				// "assoc_offers_elements where fk_element = ?");
				// qry.setParameter(1, e.getId());
				// offers = qry.getResultList();

				List<Integer> contracts;
				Query qry = em.createNativeQuery(
						"select distinct(fk_service_contract) from service where id in (select fk_service from assoc_service_elements where fk_elements = ?)");
				qry.setParameter(1, e.getId());
				contracts = qry.getResultList();

				if (contracts.size() != 0) {
					for (Integer con : contracts) {
						// Offers offerObject = offerFacade.find(off);
						ServiceContract sc = serviceContractFacade.find(con);
						OpenOfferElements ooe = new OpenOfferElements();
						ooe.setBuildingSiteName(getBuildingSiteNameForElement(e));
						ooe.setBuildingSiteNr(getBuildingSiteCodeForElement(e));
						ooe.setServiceContractNumber(getServiceContractForElement(e));
						ooe.setServiceContractType(getServiceContractTypeForElement(e));
						ooe.setServiceContract(sc);
						ooe.setServiceContractId(sc.getId());
						ooe.setServiceContractDescription(sc.getDescription());
						ooe.setServiceContractNumber(sc.getServiceContractNumber());
						String dirType= e.getDirectionType() == null ? "": e.getDirectionType().getName();
						ooe.setServiceElementAusrichtung(dirType);
						String dinType= e.getDinType() == null ? "": e.getDinType().getName();
						ooe.setServiceElementDIN(dinType);
						ooe.setServiceElementZimmer(e.getRoom());
						ooe.setServiceElementStockwerk(e.getFloor());
						ooe.setServiceElementObject(e);
						ooe.setServiceElement(e.getElementnumber());
						ooe.setServiceElementObject(e);
						ooe.setServiceContractId(sc.getId());
						resultList.add(ooe);
					}
				}

			}
		}

		return resultList;
	}

	public List<ServiceElements> findAllServiceElementsForService(Service s) {
		QService se = QService.service;
		JPAQuery q = new JPAQuery(this.em);
		q.from(se);
		q.where(se.eq(se));
		Service w = q.singleResult(se);
		return w.getServiceElementList();
	}

	public void setResetElementBarcode(List<ServiceElements> seList) {
		for (ServiceElements se : seList) {
			se.setPrintNewBarcode(false);
			this.edit(se);
		}
	}

	public Boolean printNewBarcodes() {
		QServiceElements se = QServiceElements.serviceElements;
		JPAQuery q = new JPAQuery(this.em);
		q.from(se);
		q.where(se.printNewBarcode.isTrue());
		if (q.list(se) != null && q.list(se).size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public String getBuildingSiteNameForElement(ServiceElements se) {
		String res = "";
		List<String> results;

		Query q = em.createNativeQuery("select name from building_site where id in"
				+ " (select fk_building_site from service_contract where id in "
				+ "	(select fk_service_contract from service where id in "
				+ "	( select fk_service from assoc_service_elements " + "where fk_elements = ?  )))");
		q.setParameter(1, se.getId());
		// res = (String) q.getSingleResult();
		results = q.getResultList();

		if (results == null || results.size() < 0) {
			res = "";
		} else {
			res = results.get(0);
		}

		if (res != null) {
			return res;
		}
		return "";
	}

	public String getBuildingSiteCodeForElement(ServiceElements se) {
		String res = "";
		List<String> results;

		Query q = em.createNativeQuery("select code from building_site where id in"
				+ " (select fk_building_site from service_contract where id in "
				+ "	(select fk_service_contract from service where id in "
				+ "	( select fk_service from assoc_service_elements " + "where fk_elements = ?  )))");
		q.setParameter(1, se.getId());
		// res = (String) q.getSingleResult();
		results = q.getResultList();

		if (results == null || results.size() < 0) {
			res = "";
		} else {
			res = results.get(0);
		}

		if (res != null) {
			return res;
		}
		return "";
	}

	@SuppressWarnings("unchecked")
	public String getBuildingSiteForElement(ServiceElements se) {
		String res = "";
		List<String> results;

		Query q = em.createNativeQuery("select name from building_site where id ="
				+ " (select fk_building_site from service_contract where id = "
				+ "	(select fk_service_contract from service where id = "
				+ "	( select fk_service from assoc_service_elements " + "where fk_elements = ? limit 1 )))");
		q.setParameter(1, se.getId());
		// res = (String) q.getSingleResult();
		results = q.getResultList();

		if (results == null || results.size() < 0) {
			res = "";
		} else {
			res = results.get(0);
		}

		if (res != null) {
			return res;
		}
		return "";
	}

	public String getServiceContractTypeForElement(ServiceElements se) {
		String res = "";
		List<String> results;

		Query q = em.createNativeQuery("select name from service_type where id = "
				+ "(select fk_service_type from service_contract 	" + "where id = (select fk_service_contract 	"
				+ "from service 	" + "where id = (select fk_service 	" + "from service_history 	"
				+ "where fk_element = ? " + "order by 	" + "starttime desc limit 1 )))");
		q.setParameter(1, se.getId());
		// res = (String) q.getSingleResult();
		results = q.getResultList();

		if (results == null || results.size() <= 0) {
			res = "";
		} else {
			res = results.get(0);
		}

		if (res != null) {
			return res;
		}
		return "";

	}

	public ServiceContract getServiceContractObjectForElement(ServiceElements se) {
		Integer results;

		Query q = em.createNativeQuery("select id from service_contract where id = "
				+ "(select fk_service_contract from service where id = " + "(select fk_service from service_history "
				+ "where fk_element = ? order by starttime desc limit 1 )) ");
		q.setParameter(1, se.getId());
		// res = (String) q.getSingleResult();
		results = (Integer) q.getSingleResult();

		if (results != null) {
			return serviceContractFacade.find(results);
		} else {
			return null;
		}

	}

	public String getServiceContractForElement(ServiceElements se) {
		String res = "";
		List<String> results;

		Query q = em.createNativeQuery("select shn from service_contract where id = "
				+ "(select fk_service_contract from service where id = " + "(select fk_service from service_history "
				+ "where fk_element = ? order by starttime desc limit 1 )) ");
		q.setParameter(1, se.getId());
		// res = (String) q.getSingleResult();
		results = q.getResultList();

		if (results == null || results.size() <= 0) {
			res = "";
		} else {
			res = results.get(0);
		}

		if (res != null) {
			return res;
		}
		return "";
	}

	/**
	 * Returns the active service for the respective element.
	 * 
	 * @param element
	 * @return The active service if found, null else.
	 */
	public Service getActiveServiceForElement(final ServiceElements element) {
		QService service = QService.service;
		JPAQuery q = new JPAQuery(em);
		q.from(service).where(service.active.eq(true).and(service.serviceElementList.contains(element)));
		List<Service> result = q.list(service);
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	/**
	 * Returns the latest service for the respective element.
	 * 
	 * @param element
	 * @return The latest service if found, null else.
	 */
	public Service getLatestServiceForElement(final ServiceElements element) {
		QService service = QService.service;
		JPAQuery q = new JPAQuery(em);
		q.from(service).where(service.serviceElementList.contains(element)).orderBy(service.startdate.desc());
		List<Service> result = q.list(service);
		if (result != null && result.size() > 0) {
			return result.get(0);
		} else {
			return null;
		}
	}

	public List<ServiceElements> findAllServiceElementsForPrintingBarcode() {
		QServiceElements se = QServiceElements.serviceElements;
		JPAQuery q = new JPAQuery(this.em);
		q.from(se);
		q.where(se.printNewBarcode.isTrue());

		List<ServiceElements> elementList = q.list(se);

		for (ServiceElements e : elementList) {
			e.setBaustelle(getBuildingSiteForElement(e));
			e.setWartungsvertrag(getServiceContractForElement(e));
		}

		return elementList;
	}

	public List<ServiceElements> findServiceElementsForOffer(Offers offer) {
		return offer.getServiceElementList();
		// QServiceElements elList = QServiceElements.serviceElements;
		// JPAQuery q = new JPAQuery(this.em);
		// q.from(elList);
		// q.where(elList.offer.eq(offer));
		// return q.list(elList);
	}

	public ServiceContract initilizeServiceContract(Integer scId) {
		ServiceContract sc = serviceContractFacade.find(scId);
		sc.getServices().size();
		return sc;

	}

	public List<ServiceElements> findOfferServiceElementsForServiceContract(ServiceContract sc) {
		sc = initilizeServiceContract(sc.getId());
		List<Service> sList = sc.getServices();
		List<ServiceElements> allElements = new ArrayList<>();
		for (Service s : sList) {
			List<ServiceElements> eList = s.getServiceElementList();
			for (ServiceElements se : eList) {
				for (ServiceHistory sh : se.getHistoryItems()) {

				}

				allElements.add(se);
			}
		}

		Set<ServiceElements> hs = new HashSet<>();
		hs.addAll(allElements);
		allElements.clear();
		allElements.addAll(hs);

		return allElements;
	}

	public List<ServiceElements> findServiceElementsForServiceContractOffer(ServiceContract sc) {
		sc = initilizeServiceContract(sc.getId());
		List<Service> sList = sc.getServices();
		List<ServiceElements> allElements = new ArrayList<>();
		for (Service s : sList) {
			List<ServiceElements> eList = s.getServiceElementList();
			for (ServiceElements se : eList) {
				if (se.isCreateOffer()) {
					allElements.add(se);
				}
			}
		}

		Set<ServiceElements> hs = new HashSet<>();
		hs.addAll(allElements);
		allElements.clear();
		allElements.addAll(hs);

		return allElements;
	}

	public List<ServiceElements> findServiceElementsForServiceContract(ServiceContract sc) {

		sc = initilizeServiceContract(sc.getId());
		List<Service> sList = sc.getServices();
		List<ServiceElements> allElements = new ArrayList<>();
		for (Service s : sList) {
			List<ServiceElements> eList = s.getServiceElementList();
			for (ServiceElements se : eList) {
				allElements.add(se);
			}
		}

		Set<ServiceElements> hs = new HashSet<>();
		hs.addAll(allElements);
		allElements.clear();
		allElements.addAll(hs);

		return allElements;
	}

	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public boolean elementHasAssoc(ServiceElements se) {
		// TODO Auto-generated method stub

		Query q = em.createNativeQuery("select count(*) from assoc_service_elements " + "where fk_elements = ?");
		q.setParameter(1, se.getId());
		// res = (String) q.getSingleResult();
		Long res = (Long) q.getSingleResult();
		if (res != null) {
			return res > 0;
		}
		return false;
	}

}
