package lu.origer.serviceagree.backend.contract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.backend.synch.ServiceHistoryFacade;
import lu.origer.serviceagree.frontend.main.SessionBean;
import lu.origer.serviceagree.models.assocs.Assoc_Offer_Elements;
import lu.origer.serviceagree.models.assocs.QAssoc_Offer_Elements;
import lu.origer.serviceagree.models.checklist.QServiceElements;
import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contract.Offers;
import lu.origer.serviceagree.models.contract.QService;
import lu.origer.serviceagree.models.contract.ServiceContract;

@Stateless
public class AssocOfferItemFacade extends AbstractFacade<Assoc_Offer_Elements> {

	@Inject
	ServiceElementFacade serviceElementFacade;

	@Inject
	SessionBean sessionBean;

	@Inject
	ServiceFacade serviceFacade;

	@Inject
	ServiceHistoryFacade serviceHistoryFacade;

	public AssocOfferItemFacade() {
		super(Assoc_Offer_Elements.class);
	}

	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public List<Assoc_Offer_Elements> findByOfferId(Integer offerId) {
		QAssoc_Offer_Elements as = QAssoc_Offer_Elements.assoc_Offer_Elements;
		JPAQuery q = new JPAQuery(this.em);
		q.from(as);
		q.where(as.offer.id.eq(offerId));
		return q.list(as);
	}

	public String getDefaultOfferState() {
		if (sessionBean.getUserLocale().equals(Locale.GERMANY)) {
			return OfferFacade.OFFER_STATE_WAITING_DE;
		} else {
			return OfferFacade.OFFER_STATE_WAITING_FR;
		}
	}

	public List<Integer> findAvailableElementsForOffer(Offers offer, ServiceContract sc) {
		List<Integer> results;

		Query q = em.createNativeQuery("select fk_elements from assoc_service_elements "
				+ "where fk_service in (select id from service " + "where fk_service_contract = ?) "
				+ "and fk_elements not in (select fk_element from assoc_offers_elements where offer_state not in ('ok', 'abgelehnt' )) "
				+ "group by fk_elements ");
		q.setParameter(1, sc.getId());
		// res = (String) q.getSingleResult();
		results = q.getResultList();
		return results;
	}

	public List<Integer> findSelectedElementsForOffer(Offers offer, ServiceContract sc) {
		List<Integer> results;

		Query q = em.createNativeQuery("select fk_element from assoc_offers_elements a where a.fk_offer = ? ");
		q.setParameter(1, offer.getId());
		// res = (String) q.getSingleResult();
		results = q.getResultList();
		return results;
	}

	public List<Assoc_Offer_Elements> findSourceItemsForOfferId(Offers offer, ServiceContract sc,
			List<Integer> elements, Boolean isTarget) {
		List<Integer> elList = findAvailableElementsForOffer(offer, sc);
		List<Assoc_Offer_Elements> result = new ArrayList<>();
		List<ServiceElements> sList = new ArrayList();
		QServiceElements se = QServiceElements.serviceElements;
		JPAQuery q = new JPAQuery(this.em);
		q.from(se);
		q.where(se.id.in(elList));
		sList = q.list(se);

		for (ServiceElements s : sList) {
			Assoc_Offer_Elements aof = new Assoc_Offer_Elements();

			// System.out.println("Element: " + se.getElementnumber());
			Date lastControlDate = serviceFacade.getLastControlDateForElementByService(null, s, sc);
			// System.out.println("Datum: " + lastControlDate);
			// System.out.println("Service-Intervall: " + s.getIntervall());
			aof.setLastControlDate(lastControlDate);
			aof.setLastState(serviceHistoryFacade.getAggregatedStateFromAllServiceHistoryList(lastControlDate, s));
			// se.setLastState(findState(se,s));

			aof.setCreateOffer(s.isCreateOffer());
			aof.setElement(s);
			aof.setOffer(offer);
			// aof.setOfferState("zu erstellen");
			aof.setOfferState(getDefaultOfferState());
			aof.setServiceContract(sc.getId());
			Boolean addElement = false;
			if (elements != null && elements.size() > 0) {
				addElement = false;
				for (Integer e : elements) {
					if (aof.getElement().getId().equals(e)) {
						addElement = true;
					}
				}
				if (isTarget != null && isTarget) {
					if (addElement) {
						result.add(aof);
					}
				} else {
					if (!addElement) {
						result.add(aof);
					}
				}

			} else {
				result.add(aof);
			}

		}
		return setTransientAttributes(result, sc);

	}

	public List<Assoc_Offer_Elements> setTransientAttributes(List<Assoc_Offer_Elements> list, ServiceContract sc) {
		for (Assoc_Offer_Elements a : list) {
			Date last = serviceFacade.getLastControlDateForElementByService(null, a.getElement(), sc);
			a.setLastControlDate(last);
			a.setLastState(serviceHistoryFacade.getAggregatedStateFromAllServiceHistoryList(last, a.getElement()));
		}
		return list;
	}

	public List<Assoc_Offer_Elements> findTargetItemsForOfferId(Offers offer, ServiceContract sc,
			List<Integer> elements) {
		List<Integer> elList = findSelectedElementsForOffer(offer, sc);
		List<Assoc_Offer_Elements> result = new ArrayList<>();

		QAssoc_Offer_Elements qe = QAssoc_Offer_Elements.assoc_Offer_Elements;
		JPAQuery q = new JPAQuery(this.em);
		q.from(qe);
		q.where(qe.element.id.in(elList));
		q.where(qe.offer.eq(offer));
		q.orderBy(qe.element.elementnumber.asc());
		result = q.list(qe);
		return setTransientAttributes(result, sc);
	}

	public List<Assoc_Offer_Elements> findTargetItemsForOfferId_old(Offers offer, ServiceContract sc) {

		// List<ServiceElements> seList = new ArrayList<>();
		// QService s = QService.service;
		// JPAQuery q1 = new JPAQuery(this.em);
		// q1.from(s.serviceContract.eq(sc));
		//
		// QAssoc_Offer_Elements as =
		// QAssoc_Offer_Elements.assoc_Offer_Elements;
		// JPAQuery q = new JPAQuery(this.em);
		// q.from(as);
		// if (offer.getId() == null){
		// q.where(as.offer.id.eq(-1));
		// } else {
		// q.where(as.offer.id.eq(offer.getId()));
		// }
		//
		List<Integer> elList = findSelectedElementsForOffer(offer, sc);
		List<Assoc_Offer_Elements> result = new ArrayList<>();

		List<ServiceElements> sList = new ArrayList();
		QServiceElements se = QServiceElements.serviceElements;
		JPAQuery q = new JPAQuery(this.em);
		q.from(se);
		q.where(se.id.in(elList));
		sList = q.list(se);

		List<Assoc_Offer_Elements> srcList = findSourceItemsForOfferId(offer, sc, null, null);

		// List<ServiceElements> itemList = serviceElementFacade.findAll();

		List<Assoc_Offer_Elements> resList = new ArrayList<>();

		for (ServiceElements it : sList) {
			Assoc_Offer_Elements a = new Assoc_Offer_Elements();
			a.setElement(it);
			a.setOffer(offer);
			a.setOfferState(getDefaultOfferState());

			Boolean entryAllreadyExists = false;
			for (Assoc_Offer_Elements s : srcList) {
				if (s.getElement().getId().equals(it.getId())) {
					entryAllreadyExists = true;
				}
			}
			if (!entryAllreadyExists) {
				resList.add(a);
			}
		}

		List<Assoc_Offer_Elements> sourceCopy = new ArrayList<>();
		sourceCopy.addAll(resList);
		sourceCopy.removeAll(srcList);
		srcList = sourceCopy;
		return srcList;
	}

	public List<Assoc_Offer_Elements> findAllItmesForChecklistId(Offers offer) {
		List<ServiceElements> itemList = serviceElementFacade.findAll();
		List<Assoc_Offer_Elements> resList = new ArrayList<>();
		for (ServiceElements it : itemList) {
			Assoc_Offer_Elements a = new Assoc_Offer_Elements();
			a.setOffer(offer);
			a.setElement(it);
			a.setOfferState(getDefaultOfferState());
			resList.add(a);
		}

		return resList;
	}

}
