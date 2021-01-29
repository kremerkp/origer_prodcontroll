package lu.origer.serviceagree.backend.soap;

import lu.origer.serviceagree.models.contact.types.DinType;

/**
 * 
 * @author ian.husting
 *
 * RPC-confirm SOAP parcel for config data
 */
public class ConfigParcel {
	private PersonSO[] persons;
	private ServiceElementTypeSO[] elementTypes;
	private DirectionSO[] directions;
	private DinType[] dins;
	
	public PersonSO[] getPersons() {
		return persons;
	}
	public void setPersons(PersonSO[] persons) {
		this.persons = persons;
	}
	public ServiceElementTypeSO[] getElementTypes() {
		return elementTypes;
	}
	public void setElementTypes(ServiceElementTypeSO[] elementTypes) {
		this.elementTypes = elementTypes;
	}
	public DirectionSO[] getDirections() {
		return directions;
	}
	public void setDirections(DirectionSO[] directions) {
		this.directions = directions;
	}
	public DinType[] getDins()
	{
		return dins;
	}
	public void setDins(DinType[] dins)
	{
		this.dins = dins;
	}
}
