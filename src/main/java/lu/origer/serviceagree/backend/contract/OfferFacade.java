package lu.origer.serviceagree.backend.contract;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.omnifaces.util.Faces;
import org.primefaces.component.autocomplete.AutoComplete;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.backend.main.UsersFacade;
import lu.origer.serviceagree.frontend.main.SessionBean;
import lu.origer.serviceagree.models.assocs.Assoc_Offer_Elements;
import lu.origer.serviceagree.models.assocs.QAssoc_Offer_Elements;
import lu.origer.serviceagree.models.contract.Offers;
import lu.origer.serviceagree.models.contract.QOffers;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.main.Users;

@Stateless
public class OfferFacade extends AbstractFacade<Offers>  {
	
	public static final String OFFER_STATE_NEW_DE = "neu"; 
	
	public static final String OFFER_STATE_WAITING_DE = "gesendet";
	
	public static final String OFFER_STATE_OK_DE = "ok";
	
	public static final String OFFER_STATE_CHANCELED_DE = "abgelehnt";
	
	public static final String OFFER_STATE_NEW_FR = "nouveau"; 
	
	public static final String OFFER_STATE_WAITING_FR = "expédié";
	
	public static final String OFFER_STATE_OK_FR = "bien";
	
	public static final String OFFER_STATE_CHANCELED_FR = "rejeté";
	

	
	
	@Inject 
	ServiceContractFacade serviceContractFacade;
	
	@Inject 
	AssocOfferItemFacade assocFacade;
	
	@Inject 
	UsersFacade userfacade;
	
	@Inject 
	SessionBean sessionBean; 
	
	
	
    public OfferFacade() {
        super(Offers.class);
    }

    @PersistenceContext(unitName = "ORIGERPU")
    private EntityManager em;
    
    private List<Offers> findByName(String find){
    	QOffers off = QOffers.offers; 
    	JPAQuery q = new JPAQuery(this.em);
    	q.from(off); 
    	q.where(off.name.containsIgnoreCase(find)); 
    	return addTransientElements(q.list(off)); 
    }
    
    public String getElementsFromOffer(Offers off){
    	String result = ""; 
    	QAssoc_Offer_Elements aoe = QAssoc_Offer_Elements.assoc_Offer_Elements; 
    	JPAQuery q = new JPAQuery(this.em);
    	q.from(aoe); 
    	q.where(aoe.offer.eq(off)); 
    	q.orderBy(aoe.element.elementnumber.asc());
    	List<Assoc_Offer_Elements> resOffer = q.list(aoe);
    	Boolean first = true;
    	for (Assoc_Offer_Elements o : resOffer){
    		if (first){
    			result += o.getElement().getElementnumber();  
    			first = false;
    		} else {
    			result += ", " +  o.getElement().getElementnumber();
    		}
    	}
    	return result;
    }
    
    
    public String getOfferState(List<Assoc_Offer_Elements> aoe, Locale loc){
    	
    	Boolean allSet = true;
    	Boolean oneOk = false;
    	Boolean oneDiss = false;
    	Boolean allOk = true;
    	Boolean allDiss = true;
    	Locale f_loc = loc;
    	
    	
    	if (loc == null ){
    		f_loc = sessionBean.getUserLocale();
    	}
    	
    	String state = "";
    	for(Assoc_Offer_Elements a : aoe){
    		System.out.println(a.getOffer().getState()); 
    		
    		if (a.getOfferState().equals(OfferFacade.OFFER_STATE_CHANCELED_DE)){
    			oneDiss = true;
    			allOk = false;
    		}
    		if (a.getOfferState().equals(OfferFacade.OFFER_STATE_WAITING_DE)){
    			allSet = false;
    			allDiss = false;
    			allOk = false;
    		}
    		if (a.getOfferState().equals(OfferFacade.OFFER_STATE_OK_DE)){ 
    			oneOk = true;  
    			allDiss = false;
    		}    		

    	}

		if(f_loc.equals(Locale.GERMANY)){
    		if (allOk){
    			state = "ok"; 
    		} else if (!allSet){
    			state = "nicht abgeschlossen"; 
    		} else if (oneDiss && oneOk){
    			state = "ok/abgelehnt"; 
    		} else if (allDiss){
    			state = "abgelehnt";
    		}    			
		} else {
    		if (allOk){
    			state = "ok"; 
    		} else if (!allSet){
    			state = "En traite."; 
    		} else if (oneDiss && oneOk){
    			state = "Ok/Refusé"; 
    		} else if (allDiss){
    			state = "Refusé";
    		}    			
		}
		
    	return state;
    }
    
    public Offers addTransientElements(Offers offer){
    	offer.setElementsInOffer(getElementsFromOffer(offer));
    	List<Assoc_Offer_Elements> aoe = new ArrayList<>();
    	aoe = assocFacade.findByOfferId(offer.getId());
    	offer.setState(getOfferState(aoe, null));
    	return offer; 
    }
    
    
    
    public List<Offers> addTransientElements(List<Offers> list){
    	for(Offers of : list){
    		of.setElementsInOffer(getElementsFromOffer(of));
        	List<Assoc_Offer_Elements> aoe = new ArrayList<>();
        	aoe = assocFacade.findByOfferId(of.getId());
        	of.setState(getOfferState(aoe, null));
    	}
    	return list;
    }
    
    public Offers findById(Integer offerId){
    	QOffers o = QOffers.offers; 
    	JPAQuery q = new  JPAQuery(this.em); 
    	q.from(o); 
    	q.where(o.id.eq(offerId)); 
    	Offers of = q.singleResult(o); 
    	return addTransientElements(of); 
    	
    	
    }
     
    
    public List<Offers> findAllOffers(){
    	QOffers o = QOffers.offers; 
    	JPAQuery q = new JPAQuery(this.em);
    	q.from(o); 
    	return addTransientElements(q.list(o));    	
    }
    
    public List<Offers> findAllForServiceContract(Integer serviceContractId){
    	QOffers o = QOffers.offers; 
    	JPAQuery q = new JPAQuery(this.em);
    	q.from(o); 
    	q.where(o.serviceContract.id.eq(serviceContractId));
    	return addTransientElements(q.list(o));
    	
    }
    
    public List<Offers> offerForCustomers(Integer user){
    	List<ServiceContract> con = userfacade.find(user).getServiceContractList();
    	QOffers o = QOffers.offers; 
    	JPAQuery q = new JPAQuery(this.em); 
    	q.from(o); 
    	q.where(o.serviceContract.in(con));
    	return addTransientElements(q.list(o));
    }
    
    public List<Offers> offerForCustomers(Users user){
    	List<ServiceContract> con = user.getServiceContractList();
    	QOffers o = QOffers.offers; 
    	JPAQuery q = new JPAQuery(this.em); 
    	q.from(o); 
    	q.where(o.serviceContract.in(con));
    	return addTransientElements(q.list(o));
    }
    
    public List<Offers> completeOffer(String find){
		ValueExpression exp = Faces.getApplication().getExpressionFactory().createValueExpression(
				Faces.getContext().getELContext(), "#{off.name}" , String.class);
		AutoComplete autoComplete = (AutoComplete) UIComponent.getCurrentComponent(Faces.getContext());
		autoComplete.setValueExpression("itemLabel", exp);
		List<Offers> offerList = this.findByName(find);
		if (offerList == null) {
			List<Offers> c = new ArrayList<>();
			return c;
		}
		return addTransientElements(offerList);
    }


    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    
    
}
