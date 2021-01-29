package lu.origer.serviceagree.backend.contact;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.contact.Address;

@Stateless
public class AddressFacade extends AbstractFacade<Address> {

	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;

	public AddressFacade() {
		super(Address.class);
	}

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	/**
	 * Returns the fist Element of the Address-List 
	 * @param addressList
	 * @return
	 */
	public static Address getAddressFromAddressList(List<Address> addressList){
		//TODO: get Default-Address 
		return addressList.get(0);
		
	}
}
