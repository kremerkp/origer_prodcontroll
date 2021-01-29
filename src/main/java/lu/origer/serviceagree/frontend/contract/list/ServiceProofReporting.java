package lu.origer.serviceagree.frontend.contract.list;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

import lu.origer.serviceagree.backend.contract.ServiceFacade;
import lu.origer.serviceagree.backend.synch.ServiceHistoryFacade;
import lu.origer.serviceagree.frontend.main.BasicBean;
import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contract.Service;
import lu.origer.serviceagree.models.synch.ServiceHistory;

@ManagedBean
@ViewScoped
public class ServiceProofReporting extends BasicBean {

	@Inject
	ServiceFacade serviceFacade;

	private Service service;

	private ServiceElements selectedElement;

	private LazyDataModel<ServiceElements> serviceElementsLazy;

	private List<ServiceElements> serviceElements;

	private List<ServiceHistory> historyElements;

	private List<ServiceHistory> checklistitems;

	private ServiceHistory selectedChecklistitem;

	@Inject
	ServiceHistoryFacade serviceHistoryFacade;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void init() {
		Integer id = getApplicationBean().getserviceIDFromURL();
		if (id != null) {
			this.service = serviceFacade.find(id);
			// serviceHistoryFacade.findServiceElementsForService(this.service);
			// serviceElementsLazy = new
			// LazyServiceElements(serviceFacade.findServiceElementsMappedWithHist(this.service));
			// this.serviceElements =
			// serviceFacade.findServiceElementsMappedWithHist(this.service);

			this.serviceElements = serviceFacade.findElementsForServiceSortedProofReporting(this.service, this.service.getServiceContract());
			this.serviceElements = new ArrayList<>(this.serviceElements);
		}

	}
	
	public void onRowSelect(SelectEvent e) {
		ServiceElements serEl = (ServiceElements) e.getObject();
		this.checklistitems = serviceHistoryFacade.findChecklistForElementWithoutRegie(serEl, this.service);
		System.out.println(this.checklistitems.size());

	}

	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}

	public List<ServiceElements> getServiceElements() {
		return serviceElements;
	}

	public void setServiceElement(List<ServiceElements> serviceElements) {
		this.serviceElements = serviceElements;
	}

	public ServiceElements getSelectedElement() {
		return selectedElement;
	}

	public void setSelectedElement(ServiceElements selectedElement) {
		this.selectedElement = selectedElement;
	}

	public void setServiceElements(List<ServiceElements> serviceElements) {
		this.serviceElements = serviceElements;
	}

	public ServiceFacade getServiceFacade() {
		return serviceFacade;
	}

	public void setServiceFacade(ServiceFacade serviceFacade) {
		this.serviceFacade = serviceFacade;
	}

	public LazyDataModel<ServiceElements> getServiceElementsLazy() {
		return serviceElementsLazy;
	}

	public void setServiceElementsLazy(LazyDataModel<ServiceElements> serviceElementsLazy) {
		this.serviceElementsLazy = serviceElementsLazy;
	}

	public List<ServiceHistory> getChecklistitems() {
		return checklistitems;
	}

	public void setChecklistitems(List<ServiceHistory> checklistitems) {
		this.checklistitems = checklistitems;
	}

	public ServiceHistory getSelectedChecklistitem() {
		return selectedChecklistitem;
	}

	public void setSelectedChecklistitem(ServiceHistory selectedChecklistitem) {
		this.selectedChecklistitem = selectedChecklistitem;
	}

}
