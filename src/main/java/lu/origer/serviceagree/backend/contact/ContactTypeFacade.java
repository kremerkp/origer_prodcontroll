package lu.origer.serviceagree.backend.contact;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.contact.ContactType;

@Stateless
public class ContactTypeFacade extends AbstractFacade<ContactType>{
	
	public static final Integer FAX = 1;
	public static final Integer PHONEPRIVAT = 2;
	public static final Integer MAIL = 3;
	public static final Integer WEBSITE = 4;
	public static final Integer FACEBOOK = 5;
	public static final Integer PHONEBUSINESS = 6;
	public static final Integer PHONEMOBIL = 7;
	public static final Integer TWITTER = 8;
	

	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;

	public ContactTypeFacade() {
		super(ContactType.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	


}
