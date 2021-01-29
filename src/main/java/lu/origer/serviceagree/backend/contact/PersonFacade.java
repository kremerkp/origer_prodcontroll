package lu.origer.serviceagree.backend.contact;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.activemq.network.NetworkConnector;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.contact.Address;
import lu.origer.serviceagree.models.contact.Contact;
import lu.origer.serviceagree.models.contact.MappedPerson;
import lu.origer.serviceagree.models.contact.Person;
import lu.origer.serviceagree.models.contact.QPerson;
import lu.origer.serviceagree.models.main.Users;

@Stateless
public class PersonFacade extends AbstractFacade<Person> {

	@Inject
	PersonTypeFacade personTypeFacade;

	@Inject
	ContactTypeFacade contactTypeFacade;

	@Inject
	ContactFacade contactFacade;

	@Inject
	AddressFacade addressFacade;
	
	private Integer userId; 

	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;

	public PersonFacade() {
		super(Person.class);
	}

	public List<Person> findPersonByName(String search) {
		QPerson p = QPerson.person;
		JPAQuery q = new JPAQuery(this.em);
		q.from(p);
		q.where(p.active.isTrue());
		q.where(p.lastname.containsIgnoreCase(search));
		return q.list(p);
	}

	public Boolean checkUniqunessCustomer(String firstname, String lastname) {
		QPerson p = QPerson.person;
		JPAQuery q = new JPAQuery(this.em);
		q.from(p);
		q.where(p.firstname.equalsIgnoreCase(firstname));
		q.where(p.lastname.equalsIgnoreCase(lastname));
		q.where(p.type.id.eq(PersonTypeFacade.CUSTOMER));
		List<Person> pList = q.list(p);
		return pList == null;
	}

	public Boolean checkUniqunessTechnician(String firstname, String lastname) {
		QPerson p = QPerson.person;
		JPAQuery q = new JPAQuery(this.em);
		q.from(p);
		q.where(p.firstname.equalsIgnoreCase(firstname));
		q.where(p.lastname.equalsIgnoreCase(lastname));
		q.where(p.type.id.eq(PersonTypeFacade.TECHNICIAN));
		List<Person> pList = q.list(p);
		return pList == null;
	}

	public List<MappedPerson> setMappedPersonAttributesFromPerson(List<Person> pList) {
		List<MappedPerson> cList = new ArrayList<>();
		for (Person p : pList) {
			MappedPerson c = new MappedPerson();
			c.setId(p.getId());
			c.setCompany(p.getCompany());
			c.setLastname(p.getLastname());
			c.setFirstname(p.getFirstname());
			c.setShortname(p.getShortname());
			if (p.getAddress() != null) {
				c.setStreet(p.getAddress().getStreet());
				c.setCity(p.getAddress().getCity());
				c.setStreetNumber(p.getAddress().getStreetNumber());
				c.setCountry(p.getAddress().getCountry());
			} else {
				c.setStreet("");
				c.setCity("");
				c.setStreetNumber("");
				c.setCountryName("");
			}
			c.setTitle(p.getTitle());
			c.setType(p.getType());
			c.setMobil(ContactFacade.getMobilNumberFromContactList(p.getContactList()));
			c.setPhone(ContactFacade.getPhoneNumberFromContactList(p.getContactList()));
			c.setMail(ContactFacade.getMailFromContactList(p.getContactList()));
			c.setActive(p.isActive());
			cList.add(c);
		}
		return cList;
	}

	public MappedPerson findMappedPersonById(Integer id) {
		List<Person> pList = new ArrayList<>();
		pList.add(this.find(id));
		List<MappedPerson> cList = setMappedPersonAttributesFromPerson(pList);
		return cList.get(0);
	}

	public List<Person> findMonteure() {
		QPerson p = QPerson.person;
		JPAQuery q = new JPAQuery(this.em);
		q.from(p);
		q.where(p.type.id.eq(PersonTypeFacade.MONTEUR));
		return q.list(p);
	}

	public List<Person> findCustomers() {
		QPerson p = QPerson.person;
		JPAQuery q = new JPAQuery(this.em);
		q.from(p);
		q.where(p.type.id.eq(PersonTypeFacade.CUSTOMER));
		return q.list(p);
	}

	public List<Person> findTechnician() {
		QPerson p = QPerson.person;
		JPAQuery q = new JPAQuery(this.em);
		q.from(p);
		q.where(p.type.id.eq(PersonTypeFacade.TECHNICIAN));
		return q.list(p);
	}

	public List<MappedPerson> findAllMonteure() {
		QPerson p = QPerson.person;
		JPAQuery q = new JPAQuery(this.em);
		q.from(p);
		q.where(p.type.id.eq(PersonTypeFacade.MONTEUR));
		List<Person> pList = q.list(p);
		return setMappedPersonAttributesFromPerson(pList);

	}
	

	public List<MappedPerson> findAllCustomers() {
		QPerson p = QPerson.person;
		JPAQuery q = new JPAQuery(this.em);
		q.from(p);
		q.where(p.type.id.eq(PersonTypeFacade.CUSTOMER));
		List<Person> pList = q.list(p);
		return setMappedPersonAttributesFromPerson(pList);

	}

	public List<MappedPerson> findAllTechnicians() {
		QPerson p = QPerson.person;
		JPAQuery q = new JPAQuery(this.em);
		q.from(p);
		q.where(p.type.id.eq(PersonTypeFacade.TECHNICIAN));
		List<Person> pList = q.list(p);
		return setMappedPersonAttributesFromPerson(pList);

	}

	public List<Person> findCustomersByName(String search) {
		QPerson p = QPerson.person;
		JPAQuery q = new JPAQuery(this.em);
		q.from(p);
		q.where(p.active.isTrue());
		q.where(p.lastname.containsIgnoreCase(search));
		q.where(p.type.id.eq(PersonTypeFacade.CUSTOMER));
		return q.list(p);
	}

	public List<Person> findTechnicianByName(String search) {
		QPerson p = QPerson.person;
		JPAQuery q = new JPAQuery(this.em);
		q.from(p);
		q.where(p.active.isTrue());
		q.where(p.lastname.containsIgnoreCase(search));
		q.where(p.type.id.eq(PersonTypeFacade.TECHNICIAN));
		return q.list(p);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public Contact getContact(String contactField, Integer contactType, Users u) {
		Contact c = new Contact();
		c.setActive(true);
		c.setContactField(contactField);
		c.setContactType(contactTypeFacade.find(contactType));
		c.setEditDate(new Date());
		c.setCreateDate(new Date());
		c.setEditUser(u.getId());
		c.setCreateUser(u.getId());
		return c;
	}

	public Person setCustomerContactList(Person monteur, MappedPerson mp, Users u, Boolean isNew) {
		if (monteur.getContactList() == null) {
			if (isNew) {
				List<Contact> cList = new ArrayList<>();
				monteur.setContactList(cList);
				Contact mobil = contactFacade.getMobilContactFromString(mp.getMobil());
				Contact mail = contactFacade.getMailContactFromString(mp.getMail());
				Contact phone = contactFacade.getPhoneContactFromString(mp.getPhone());
				cList.add(getContact(mp.getMobil(), ContactTypeFacade.PHONEMOBIL, u));
				cList.add(getContact(mp.getMail(), ContactTypeFacade.MAIL, u));
				cList.add(getContact(mp.getPhone(), ContactTypeFacade.PHONEBUSINESS, u));
				monteur.setContactList(cList);
			} else {
				List<Contact> cList = monteur.getContactList();
				Contact mobil = contactFacade.getMobilContactFromString(mp.getMobil());
				Contact mail = contactFacade.getMailContactFromString(mp.getMail());
				Contact phone = contactFacade.getPhoneContactFromString(mp.getPhone());
				if (mp.getPhone() != null) {
					phone.setContactField(mp.getPhone());
					phone.setEditDate(new Date());
					phone.setEditUser(u.getId());
					if (mp.getPhone().length() > 0) {
						contactFacade.edit(phone);
					}
				}
				if (mp.getMobil() != null) {
					mobil.setContactField(mp.getMobil());
					mobil.setEditDate(new Date());
					mobil.setEditUser(u.getId());
					if (mp.getMobil().length() > 0) {
						contactFacade.edit(mobil);
					}
				}
				if (mp.getMail() != null) {
					mail.setContactField(mp.getMail());
					mail.setEditDate(new Date());
					mail.setEditUser(u.getId());
					if (mp.getMail().length() > 0) {
						contactFacade.edit(mail);
					}
				}

			}
		}
		return monteur;

	}

	public void createNewMonteuer(MappedPerson mp, Users user, Boolean isNew) {
		Person mon = new Person();
		mon.setActive(true);
		Address ad = new Address();
		ad.setActive(true);
		ad.setCity(mp.getCity());
		ad.setCountry(mp.getCountry());
		ad.setStreet(mp.getStreet());
		ad.setStreetNumber(mp.getStreetNumber());
		ad.setCreateDate(new Date());
		ad.setEditDate(new Date());
		ad.setCreateUser(user.getId());
		ad.setEditUser(user.getId());
		addressFacade.create(ad);
		mon.setAddress(ad);
		mon.setCompany(mp.getCompany());
		mon.setCreateDate(new Date());
		mon.setEditDate(new Date());
		mon.setCreateUser(user.getId());
		mon.setEditUser(user.getId());
		mon.setFirstname(mp.getFirstname());
		mon.setLastname(mp.getLastname());
		mon.setTitle(mp.getTitle());
		mon.setType(personTypeFacade.find(PersonTypeFacade.MONTEUR));

		mon = setCustomerContactList(mon, mp, user, isNew);
		this.edit(mon);

	}

	public void createNewMonteur23(MappedPerson mp, Users user) {
		Person mon = new Person();
		mon.setActive(true);
		Address ad = new Address();
		ad.setActive(true);
		ad.setCity(mp.getCity());
		ad.setCountry(mp.getCountry());
		ad.setStreet(mp.getStreet());
		ad.setStreetNumber(mp.getStreetNumber());
		ad.setCreateDate(new Date());
		ad.setEditDate(new Date());
		ad.setCreateUser(user.getId());
		ad.setEditUser(user.getId());
		addressFacade.create(ad);

		mon.setAddress(ad);
		mon.setCompany(mp.getCompany());
		List<Contact> cList = new ArrayList<>();
		Contact mobil = contactFacade.getMobilContactFromString(mp.getMobil());
		if (mobil != null) {
			mobil.setCreateUser(user.getId());
			mobil.setEditUser(user.getId());
			// contactFacade.create(mobil);
			cList.add(mobil);
		}
		Contact mail = contactFacade.getMailContactFromString(mp.getMail());
		if (mail != null) {
			mail.setCreateUser(user.getId());
			mail.setEditUser(user.getId());
			// contactFacade.create(mail);
			cList.add(mail);
		}
		Contact phone = contactFacade.getPhoneContactFromString(mp.getPhone());
		if (phone != null) {
			phone.setCreateUser(user.getId());
			phone.setEditUser(user.getId());
			// contactFacade.create(phone);
			cList.add(phone);
		}
		if (cList != null && cList.size() > 0) {
			mon.setContactList(cList);
		}

		mon.setCreateDate(new Date());
		mon.setEditDate(new Date());
		mon.setCreateUser(user.getId());
		mon.setEditUser(user.getId());
		mon.setFirstname(mp.getFirstname());
		mon.setLastname(mp.getLastname());
		mon.setTitle(mp.getTitle());
		mon.setType(personTypeFacade.find(PersonTypeFacade.MONTEUR));
		this.create(mon);

	}

	public void editPersonFromMonteur(MappedPerson monteur, Users user) {
		// createNewMonteuer(monteur, user, false);
		Person p = convertMappedPersonToPerson(monteur, user, PersonTypeFacade.MONTEUR);
		edit(p);
	}

	public void createPersonFromMenteur(MappedPerson monteur, Users user) {
		// TODO Auto-generated method stub
		// createNewMonteur(monteur, user);
		createNewMonteuer(monteur, user, true);
	}

	public void createPersonFromCustomer(MappedPerson customer, Users user) {
		// TODO Auto-generated method stub
		convertMappedPersonToPerson(customer, user, PersonTypeFacade.CUSTOMER);
	}

	public void createPersonFromTechnician(MappedPerson technician, Users user) {
		// TODO Auto-generated method stub
		convertMappedPersonToPerson(technician, user, PersonTypeFacade.CUSTOMER);
	}

	@SuppressWarnings("static-access")
	public void convertMappedPersonToContactList(MappedPerson mp, Person p) {
		List<Contact> cList = p.getContactList();
		Contact newContact; 
		// if (cList.isEmpty()) {
		if (mp.getMail() != null && mp.getMail().length() >= 0) {
			if (contactFacade.getMailContactFromContactList(p.getContactList()) != null) {
				Contact con = contactFacade.getMailContactFromContactList(p.getContactList());
				con.setContactField(mp.getMail());
			} else {
				newContact = new Contact();
				newContact = contactFacade.getMailContactFromString(mp.getMail());
				newContact.setCreateUser(this.userId);
				newContact.setEditUser(this.userId);
				contactFacade.create(newContact);
				cList.add(newContact);
			}
		}
		if (mp.getMobil() != null && mp.getMobil().length() >= 0) {
			if (contactFacade.getMobilContactFromContactList(p.getContactList()) != null) {
				Contact con = contactFacade.getMobilContactFromContactList(p.getContactList());
				con.setContactField(mp.getMobil());
			} else {
				newContact = new Contact();
				newContact = contactFacade.getMobilContactFromString(mp.getMobil());
				newContact.setCreateUser(this.userId);
				newContact.setEditUser(this.userId);
				contactFacade.create(newContact);
				cList.add(newContact);
			}
		}
		if (mp.getPhone() != null && mp.getPhone().length() >= 0) {
			if (contactFacade.getPhoneContactFromContactList(p.getContactList()) != null) {
				Contact con = contactFacade.getPhoneContactFromContactList(p.getContactList());
				con.setContactField(mp.getPhone());
			} else {
				newContact = new Contact();
				newContact = contactFacade.getMobilContactFromString(mp.getPhone());
				newContact.setCreateUser(this.userId);
				newContact.setEditUser(this.userId);
				contactFacade.create(newContact);
				cList.add(newContact);
			}
		}
		p.setContactList(cList);
	}

	public void convertMappedPersonAttributesToAddress(MappedPerson mp, Person p) {		
		if (p.getAddress() == null) {
			Address ad = new Address();
			ad.setActive(true);
			ad.setCreateDate(new Date());
			ad.setEditDate(new Date());
			ad.setCreateUser(this.userId);
			ad.setEditUser(this.userId);
			p.setAddress(ad);
		}
		p.getAddress().setCity(mp.getCity());
		p.getAddress().setCountry(mp.getCountry());
		p.getAddress().setCity(mp.getCity());
		p.getAddress().setStreet(mp.getStreet());
		p.getAddress().setStreetNumber(mp.getStreetNumber());
		// TODO: Implement zipCode if neccessary
	}

	public Person convertMappedPersonToPerson(MappedPerson c, Users u, Integer personTypeId) {
		if (u != null){
			this.userId = u.getId();			
		}
		Person p = find(c.getId());
		p.setActive(c.getActive());
		// convert address is okay!!
		convertMappedPersonAttributesToAddress(c, p);
		p.setCompany(c.getCompany());
		convertMappedPersonToContactList(c, p);
		p.setEditDate(new Date());
		p.setEditUser(u.getId());
		p.setFirstname(c.getFirstname());
		p.setLastname(c.getLastname());
		p.setName(c.getName());
		p.setShortname(c.getShortname());
		p.setTitle(c.getTitle());
		if (personTypeId.equals(PersonTypeFacade.CUSTOMER)) {
			p.setType(personTypeFacade.find(PersonTypeFacade.CUSTOMER));
		} else if (personTypeId.equals(PersonTypeFacade.TECHNICIAN)) {
			p.setType(personTypeFacade.find(PersonTypeFacade.TECHNICIAN));
		} else if (personTypeId.equals(PersonTypeFacade.MONTEUR)) {
			p.setType(personTypeFacade.find(PersonTypeFacade.MONTEUR));
		} else {
			p.setType(personTypeFacade.find(PersonTypeFacade.CUSTOMER));
		}

		return p;
	}

	public void editPersonFromCustomer(MappedPerson customer, Users user) {
		// TODO Auto-generated method stub
		Person p = convertMappedPersonToPerson(customer, user, PersonTypeFacade.CUSTOMER);
		edit(p);
	}

	public void editPersonFromTechnician(MappedPerson technician, Users user) {
		// TODO Auto-generated method stub
		Person p = convertMappedPersonToPerson(technician, user, PersonTypeFacade.TECHNICIAN);
		edit(p);
	}
	
	

}
