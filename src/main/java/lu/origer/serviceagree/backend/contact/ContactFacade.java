package lu.origer.serviceagree.backend.contact;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.contact.Contact;

@Stateless
public class ContactFacade extends AbstractFacade<Contact> {

	@Inject
	ContactTypeFacade contactTypeFacade;

	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;

	public ContactFacade() {
		super(Contact.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	public static String getPhoneNumberFromContactList(List<Contact> contactList) {
		if (contactList.size() > 0) {
			for (Contact contact : contactList) {
				if (contact.getContactType().getId().equals(ContactTypeFacade.PHONEBUSINESS)) {
					return contact.getContactField();
				}
			}
		}
		return "";
	}

	public static String getMobilNumberFromContactList(List<Contact> contactList) {
		if (contactList.size() > 0) {
			for (Contact contact : contactList) {
				if (contact.getContactType().getId().equals(ContactTypeFacade.PHONEMOBIL)) {
					return contact.getContactField();
				}
			}
		}
		return "";
	}

	public static String getMailFromContactList(List<Contact> contactList) {
		if (contactList.size() > 0) {
			for (Contact contact : contactList) {
				if (contact.getContactType().getId().equals(ContactTypeFacade.MAIL)) {
					return contact.getContactField();
				}
			}
		}
		return "";

	}

	public static String getFaxFromContactList(List<Contact> contactList) {
		if (contactList.size() > 0) {
			for (Contact contact : contactList) {
				if (contact.getContactType().getId().equals(ContactTypeFacade.FAX)) {
					return contact.getContactField();
				}
			}
		}
		return "";
	}

	public static String getPhonePrivatFromContactList(List<Contact> contactList) {
		if (contactList.size() > 0) {
			for (Contact contact : contactList) {
				if (contact.getContactType().getId().equals(ContactTypeFacade.PHONEPRIVAT)) {
					return contact.getContactField();
				}
			}
		}
		return "";
	}

	public static String getFacebookFromContactList(List<Contact> contactList) {
		if (contactList.size() > 0) {
			for (Contact contact : contactList) {
				if (contact.getContactType().getId().equals(ContactTypeFacade.FACEBOOK)) {
					return contact.getContactField();
				}
			}
		}
		return "";
	}

	public static String getTwitterFromContactList(List<Contact> contactList) {
		if (contactList.size() > 0) {
			for (Contact contact : contactList) {
				if (contact.getContactType().getId().equals(ContactTypeFacade.TWITTER)) {
					return contact.getContactField();
				}
			}
		}
		return "";
	}

	public static String getWebsiteFromContactList(List<Contact> contactList) {
		if (contactList.size() > 0) {
			for (Contact contact : contactList) {
				if (contact.getContactType().getId().equals(ContactTypeFacade.WEBSITE)) {
					return contact.getContactField();
				}
			}
		}
		return "";
	}

	public static Contact getPhoneContactFromContactList(List<Contact> contactList) {
		if (contactList.size() > 0) {
			for (Contact contact : contactList) {
				if (contact.getContactType().getId().equals(ContactTypeFacade.PHONEBUSINESS)) {
					return contact;
				}
			}
		}
		return null;
	}

	public static Contact getMobilContactFromContactList(List<Contact> contactList) {
		if (contactList.size() > 0) {
			for (Contact contact : contactList) {
				if (contact.getContactType().getId().equals(ContactTypeFacade.PHONEMOBIL)) {
					return contact;
				}
			}
		}
		return null;
	}

	public static Contact getMailContactFromContactList(List<Contact> contactList) {
		if (contactList.size() > 0) {
			for (Contact contact : contactList) {
				if (contact.getContactType().getId().equals(ContactTypeFacade.MAIL)) {
					return contact;
				}
			}
		}
		return null;

	}

	public static Contact getFaxContactFromContactList(List<Contact> contactList) {
		if (contactList.size() > 0) {
			for (Contact contact : contactList) {
				if (contact.getContactType().getId().equals(ContactTypeFacade.FAX)) {
					return contact;
				}
			}
		}
		return null;
	}

	public static Contact getPhoneContactPrivateFromContactList(List<Contact> contactList) {
		if (contactList.size() > 0) {
			for (Contact contact : contactList) {
				if (contact.getContactType().getId().equals(ContactTypeFacade.PHONEPRIVAT)) {
					return contact;
				}
			}
		}
		return null;
	}

	public static Contact getFacebookContactFromContactList(List<Contact> contactList) {
		if (contactList.size() > 0) {
			for (Contact contact : contactList) {
				if (contact.getContactType().getId().equals(ContactTypeFacade.FACEBOOK)) {
					return contact;
				}
			}
		}
		return null;
	}

	public static Contact getTwitterContactFromContactList(List<Contact> contactList) {
		if (contactList.size() > 0) {
			for (Contact contact : contactList) {
				if (contact.getContactType().getId().equals(ContactTypeFacade.TWITTER)) {
					return contact;
				}
			}
		}
		return null;
	}

	public static Contact getWebsiteContactFromContactList(List<Contact> contactList) {
		if (contactList.size() > 0) {
			for (Contact contact : contactList) {
				if (contact.getContactType().getId().equals(ContactTypeFacade.WEBSITE)) {
					return contact;
				}
			}
		}
		return null;
	}

	public Contact getMailContactFromString(String contact) {
		if (contact != null && contact.length() > 0) {
			try {
				Contact cc = new Contact();
				cc.setContactType(contactTypeFacade.find(ContactTypeFacade.MAIL));
				cc.setActive(true);
				cc.setContactField(contact);
				cc.setCreateDate(new Date());
				cc.setEditDate(new Date());
				return cc;
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
		}
		return null;
	}

	public Contact getMobilContactFromString(String contact) {

		if (contact != null && contact.length() > 0) {
			try {
				Contact cc = new Contact();
				cc.setContactType(contactTypeFacade.find(ContactTypeFacade.PHONEMOBIL));
				cc.setActive(true);
				cc.setContactField(contact);
				cc.setCreateDate(new Date());
				cc.setEditDate(new Date());
				return cc;
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
		}
		return null;

	}

	public Contact getPhoneContactFromString(String contact) {
		if (contact != null && contact.length() > 0) {

			try {
				Contact cc = new Contact();
				cc.setContactType(contactTypeFacade.find(ContactTypeFacade.PHONEBUSINESS));
				cc.setActive(true);
				cc.setContactField(contact);
				cc.setCreateDate(new Date());
				cc.setEditDate(new Date());

				return cc;
			} catch (Exception e) {
				// TODO: handle exception
				return null;
			}
		}
		return null;
	}

}
