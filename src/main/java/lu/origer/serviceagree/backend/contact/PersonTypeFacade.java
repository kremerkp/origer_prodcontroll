package lu.origer.serviceagree.backend.contact;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.contact.PersonType;

@Stateless
public class PersonTypeFacade  extends AbstractFacade<PersonType> { 
	
	public static final Integer CUSTOMER = 1; 
	public static final Integer TECHNICIAN = 2; 
	public static final Integer PARTNER = 3; 
	public static final Integer LOGIN = 4; 
	public static final Integer MONTEUR = 5; 
	
 
	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;
	
	public PersonTypeFacade(){
		super(PersonType.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
}