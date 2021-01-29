package lu.origer.serviceagree.backend.contact;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.contact.Country;
import lu.origer.serviceagree.models.contact.QCountry;

@Stateless
public class CountryFacade extends AbstractFacade<Country> {
	
	public static final Integer BELGIUM = 25;
	public static final Integer FRANCE = 79;
	public static final Integer GERMANY = 86;
	public static final Integer LUXEMBOURG = 133;
	
	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;
	
	public CountryFacade(){
		super(Country.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	public Country findById(Integer id){
		QCountry c = QCountry.country; 
		JPAQuery q = new JPAQuery(this.em); 
		q.from(c);  
		q.where(c.id.eq(id)); 
		return q.singleResult(c); 
		
	}
	
	public List<Country> findByName(String name){
		QCountry c = QCountry.country; 
		JPAQuery q = new JPAQuery(this.em); 
		q.from(c); 
		q.where(c.active.isTrue()); 
		q.where(c.name.containsIgnoreCase(name)
				.or(c.nameDE.containsIgnoreCase(name))
				.or(c.nameFR.containsIgnoreCase(name))); 
		return q.list(c); 
	}
	
	public List<Country> findAllACtive(){
		QCountry c = QCountry.country; 
		JPAQuery q = new JPAQuery(this.em); 
		q.from(c); 
		q.where(c.active.isTrue());  
		return q.list(c); 
	}


}
