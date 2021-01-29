package lu.origer.serviceagree.frontend.contact.list;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

import lu.origer.serviceagree.backend.contact.CountryFacade;
import lu.origer.serviceagree.frontend.main.BasicBean;
import lu.origer.serviceagree.models.contact.Country;

@ManagedBean
@ViewScoped
public class CountryBean extends BasicBean {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<Country> countryList;
	
	private List<Country> filteredCountryList;
		
	private Country selectedCountry;
	
	@Inject
	CountryFacade countryFacade; 
	
    @PostConstruct
    public void init() {
        countryList = countryFacade.findAll();
    }
    


	public List<Country> getCountryList() {
		return countryList;
	}

	public void setCountryList(List<Country> countryList) {
		this.countryList = countryList;
	}

	public Country getSelectedCountry() {
		return selectedCountry;
	}

	public void setSelectedCountry(Country selectedCountry) {
		this.selectedCountry = selectedCountry;
	}

	public List<Country> getFilteredCountryList() {
		return filteredCountryList;
	}

	public void setFilteredCountryList(List<Country> filteredCountryList) {
		this.filteredCountryList = filteredCountryList;
	}
	
	
    

}
