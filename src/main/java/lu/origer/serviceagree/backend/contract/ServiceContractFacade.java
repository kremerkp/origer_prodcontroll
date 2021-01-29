package lu.origer.serviceagree.backend.contract;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.omnifaces.util.Faces;
import org.primefaces.component.autocomplete.AutoComplete;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.contract.BuildingSite;
import lu.origer.serviceagree.models.contract.QBuildingSite;
import lu.origer.serviceagree.models.contract.QService;
import lu.origer.serviceagree.models.contract.QServiceContract;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.contract.ServiceType;

@Stateless
public class ServiceContractFacade extends AbstractFacade<ServiceContract> {

	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;

	public ServiceContractFacade() {
		super(ServiceContract.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public ServiceContract findByBuildingSite(BuildingSite bs) {
		QServiceContract s = QServiceContract.serviceContract;
		JPAQuery q = new JPAQuery(this.em);
		q.from(s);
		q.where(s.buildingSite.eq(bs));
		return q.singleResult(s);

	}
	
	public Boolean elementsExistsforServiceContractAndService(ServiceContract sc, ServiceType st){
		Query q = em.createNativeQuery("select count(*) from assoc_service_elements where fk_service in (select id from service where fk_service_contract = ? and fk_service_type = ? )");
		q.setParameter(1, sc.getId());
		q.setParameter(2, st.getId());
		Long res = null;
		if (q.getSingleResult() != null){
			res = ((Long) q.getSingleResult());
		}
		
		return res != null && res > 0; 
		
	}
	
	public Boolean elementsExistsforServiceContract(ServiceContract sc){
		Query q = em.createNativeQuery("select count(*) from assoc_service_elements where fk_service in (select id from service where fk_service_contract = ? )");
		q.setParameter(1, sc.getId());
		Long res = null;
		if (q.getSingleResult() != null){
			res = ((Long) q.getSingleResult());
		}
		
		return res != null && res > 0; 
		
	}

	public String findNextContractNumber(String shortname) {
		Query q = em.createNativeQuery("select max(convert(substring(shn, 9, 4), UNSIGNED INTEGER)) as test from service_contract s where substring(shn, 7, 1) = ?");
		q.setParameter(1, shortname);
		Integer res = null;
		if (q.getSingleResult() != null){
			res = ((BigInteger) q.getSingleResult()).intValue();			
		}
		
		String result = "0001";
		 
		if (res != null ){
			res += 1;
			if (res > 0 && res < 10){
				result = "000" + res ; 
			} else if (res > 10 && res < 100){
				result = "00" + res; 
			} else if (res > 100 && res < 1000){
				result = "0" + res; 
			} else if (res > 1000 && res < 10000){
				result =  res.toString();
			}	
		}
		
		return result;

	}

	public Integer getNextContractId(Integer bsId, Integer accServiceContractId) {
		QServiceContract sc = QServiceContract.serviceContract;
		JPAQuery qry = new JPAQuery(this.em);
		qry.from(sc);
		qry.where(sc.buildingSite.id.eq(bsId));
		qry.orderBy(sc.id.asc());
		List<Integer> allIds = qry.list(sc.id);
		Integer pointer = 0;
		Integer res = 0;
		Integer counter = 0;
		for (Integer id : allIds) {
			counter++;
			if (id.equals(accServiceContractId)) {
				pointer = counter;
			}
		}

		if (pointer >= allIds.size()) {
			res = allIds.get(0);
		} else {
			res = allIds.get(pointer);
		}

		return res;

	}

	public Integer countedServiceContractsForBuildingSite(BuildingSite site) {
		QServiceContract sc = QServiceContract.serviceContract;
		JPAQuery q = new JPAQuery(this.em);
		q.from(sc);
		q.where(sc.buildingSite.eq(site));
		List<ServiceContract> conList = q.list(sc);

		if (conList != null && conList.size() > 0) {
			return conList.size();
		} else {
			return 0;
		}

	}

	public List<ServiceContract> findByName(String find) {
		QServiceContract s = QServiceContract.serviceContract;
		JPAQuery q = new JPAQuery(this.em);
		q.from(s);
		q.where(s.active.isTrue());
		q.where(s.serviceContractNumber.containsIgnoreCase(find));
		return q.list(s);
	}

	public Boolean checkUniquness(String contractNumber) {
		QServiceContract s = QServiceContract.serviceContract;
		JPAQuery q = new JPAQuery(this.em);
		q.from(s);
		q.where(s.serviceContractNumber.equalsIgnoreCase(contractNumber));
		List<ServiceContract> sc = q.list(s);
		return sc == null;
	}

	public List<ServiceContract> completeServiceContract(String filter) {
		ValueExpression exp = Faces.getApplication().getExpressionFactory()
				.createValueExpression(Faces.getContext().getELContext(), "#{ser.serviceContractNumber}", String.class);
		AutoComplete autoComplete = (AutoComplete) UIComponent.getCurrentComponent(Faces.getContext());
		autoComplete.setValueExpression("itemLabel", exp);
		List<ServiceContract> serviceContractList = this.findByName(filter);
		if (serviceContractList == null) {
			List<ServiceContract> c = new ArrayList<>();
			return c;
		}
		return serviceContractList;
	}

	public List<ServiceContract> findForAutocomplete(String query) {
		QServiceContract s = QServiceContract.serviceContract;
		JPAQuery q = new JPAQuery(this.em);
		q.from(s);
		q.where(s.serviceContractNumber.startsWithIgnoreCase(query));

		List<ServiceContract> sc = q.list(s);

		return sc;
	}

	public Integer getLatestId()
	{
		QServiceContract contract = QServiceContract.serviceContract;
		JPAQuery query = new JPAQuery(this.em);
		return query.from(contract).limit(1).orderBy(contract.id.desc()).singleResult(contract.id);
	}
}
