package lu.origer.serviceagree.frontend.contract.list;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

import lu.origer.serviceagree.backend.contract.ServiceElementFacade;
import lu.origer.serviceagree.backend.contract.ServiceFacade;
import lu.origer.serviceagree.backend.main.ObjectCommentFacade;
import lu.origer.serviceagree.backend.synch.ServiceHistoryFacade;
import lu.origer.serviceagree.frontend.main.BasicBean;
import lu.origer.serviceagree.frontend.report.ReportGeneratorBean;
import lu.origer.serviceagree.models.checklist.ChecklistItem;
import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contract.Service;
import lu.origer.serviceagree.models.main.ObjectComments;
import lu.origer.serviceagree.models.main.Users;
import lu.origer.serviceagree.models.reporting.PrintHistory;
import lu.origer.serviceagree.models.reporting.PrintHistoryChecklist;
import lu.origer.serviceagree.models.reporting.PrintHistoryMaster;
import lu.origer.serviceagree.models.synch.ServiceElementHistory;
import lu.origer.serviceagree.models.synch.ServiceHistory;

@ManagedBean
@ViewScoped
public class ServiceCustomerHistoryBean extends BasicBean {

	@Inject
	ServiceFacade serviceFacade;

	private Service service;

	private ServiceElements selectedElement;
	
	private ServiceElements selectedElement23;

	private LazyDataModel<ServiceElements> serviceElementsLazy;

	private List<Service> serviceListCustomer;

	private List<Service> filteredServiceListCustomer;

	// 1. Tabelle (ol)
	private Service selectedServiceListCustomer;

	private List<ServiceElements> serviceElements;

	private List<ServiceElementHistory> historyElements;

	private ServiceElementHistory historyElementsSelected;

	private List<ServiceElementHistory> historyElementsFiltered;

	private List<ServiceHistory> checklistitems;

	private ServiceHistory selectedChecklistitem;

	private String addDescriptionOk;
	
	private String notice; 
	
	private String style; 


	// Popup repair
	private String repairCustomerName;
	private String repairComment;

	@Inject
	ServiceHistoryFacade serviceHistoryFacade;

	@Inject
	ServiceElementFacade serviceElementFacade;
	
	@Inject
	ObjectCommentFacade objectCommnentFacade;

	private Boolean rowSelected;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@PostConstruct
	public void init() {

		Integer custId = getApplicationBean().getCustomerIdFromURL();
		this.rowSelected = false;

		if (getSessionBean().getIsAdmin()) {
			if (custId == null) {
				serviceListCustomer = serviceFacade.findAllServicesGrouped();
				serviceListCustomer = new ArrayList<>(serviceListCustomer);
			} else {
				serviceListCustomer = serviceFacade.findAllServicesForCustomer(custId);
				serviceListCustomer = new ArrayList<>(serviceListCustomer);
			}

		} else {
			serviceListCustomer = serviceFacade.findAllServicesForCustomer(getSessionBean().getCurrentUser());
			serviceListCustomer = new ArrayList<>(serviceListCustomer);
		}

	}

	public String styleState(String s) {
		if (s.contains("state")) {
			if (selectedElement != null) {
				s = selectedElement.getLastState();
			}
		}
		String style = "";
		if (s.toLowerCase().contains("ok")) {
			style = "ok";
		} else if (s.toLowerCase().contains("defekt") || s.toLowerCase().contains("panne")
				|| s.toLowerCase().contains("éfectueux")) {
			style = "defekt";
		} else if (s.toLowerCase().contains("mange") || s.toLowerCase().contains("défaut")
				|| s.toLowerCase().contains("conforme")) {
			style = "mangel";
		}
		return style;
	}

	public void onRowSelectShowChecklist(SelectEvent e) {
		this.checklistitems = serviceHistoryFacade
				.findChecklistForSelectedHistoryElementByDate(this.historyElementsSelected, this.selectedElement);
	}

	public void onRowSelectService(SelectEvent e) {
		System.out.println(selectedServiceListCustomer.getId());
		this.serviceElements = this.serviceFacade.fetchElementHistory(this.selectedServiceListCustomer);
		//this.serviceElements = serviceFacade.findElementsForServiceSorted(this.selectedServiceListCustomer);
		this.serviceElements = new ArrayList<>(this.serviceElements);
		if (this.serviceElements != null && this.serviceElements.size() > 0) {
			this.selectedElement = this.serviceElements.get(0);
		}
		onRowSelect(e);

	}

	public void onRowSelect123() {
		System.out.println("test");	
	}
	
	public void onRowSelect(SelectEvent e) {
		if (this.selectedElement != null) {
			if (serviceHistoryFacade.elementSynchronisationExists(this.selectedElement)) {
				this.historyElements = serviceHistoryFacade.findHistoryElementFromSelected(this.selectedElement,
						this.selectedServiceListCustomer);
				this.historyElementsSelected = this.historyElements.get(0);
				onRowSelectShowChecklist(e);
			} else {
				this.historyElements = new ArrayList<>();
				this.checklistitems = new ArrayList<>();
				//onRowSelectShowChecklist(e);
			}
			if (e != null && e.getObject() == null) {
				this.rowSelected = false;
			} else {
				this.rowSelected = true;
			}
			// this.checklistitems =
			// serviceHistoryFacade.findChecklistForElement(this.selectedElement,
			// this.selectedServiceListCustomer);
		}		
	}

	/**
	 * Generates repair entries for the selected elements
	 */
	public void generateRepairEntries() {
		String repairMessage = "Einträge erfolgreich erstellt!";

		// * servicesElementListHistory.xhtml --
		// #{serviceCustomerHistoryBean.serviceElements}
		// * serviceHistoryList.xhtml --

		if (this.selectedElement != null) {
			// Generate repair entry
			final ServiceHistory defectEntry = new ServiceHistory();
			defectEntry.setStartTime(new Date());
			defectEntry.setCheckedAsRepaired(false);
			defectEntry.setErrorHistoryFlag(false);
			defectEntry.setCreateOffer(false);
			defectEntry.setCheckingSeconds(0);
			defectEntry.setCheckedAsDefect(true);
			defectEntry.setCheckedAsLack(false);
			defectEntry.setCheckedAsOk(false);
			Users user = getSessionBean().getCurrentUser();
			defectEntry.setComment(user.getFullName().toUpperCase());
			defectEntry.setVisualControl(false);
			defectEntry.setFunctionalControl(false);
			// Get active service for element
			Service currentService = this.serviceElementFacade.getActiveServiceForElement(this.selectedElement);
			if (currentService == null) {
				// No active service found, set latest service
				currentService = this.serviceElementFacade.getLatestServiceForElement(this.selectedElement);
			}
			if (currentService == null) {
				// No latest service found, use selected service...
				currentService = this.selectedServiceListCustomer;
			}
			defectEntry.setService(currentService);
			defectEntry.setElement(this.selectedElement);
			defectEntry.setDescription(this.repairCustomerName + ": " + this.repairComment);
			defectEntry.setCheckListItem(new ChecklistItem(-2));

			if (!ac_save_defect_entry(defectEntry)) {
				repairMessage = "Fehler beim Erstellen der Einträge!";
			}
		}
		// Display message
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage("Fehlererfassung", repairMessage));
		// Clear entered values
		this.repairComment = "";
		this.repairCustomerName = "";
		onRowSelectService(null);
		onRowSelect(null);
	}

	private Boolean ac_save_defect_entry(final ServiceHistory entry) {
		try {
			this.serviceHistoryFacade.create(entry);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<PrintHistoryChecklist> generateHistoryChecklist(ServiceElementHistory eh) {
		List<ServiceHistory> histList = serviceHistoryFacade.findChecklistForSelectedHistoryElementByDate(eh,
				this.selectedElement);
		List<PrintHistoryChecklist> historyChecklist = new ArrayList<>();
		for (ServiceHistory h : histList) {
			PrintHistoryChecklist p = new PrintHistoryChecklist();
			p.setCheckedAsDefect(h.getCheckedAsDefect());
			p.setCheckedAsLack(h.getCheckedAsLack());
			p.setCheckedAsOk(h.getCheckedAsOk());
			p.setCheckedAsRepaired(h.getCheckedAsRepaired());
			String toCheck = "";

			if (getSessionBean().getUserLocale() == Locale.FRANCE
					|| getSessionBean().getUserLocale() == Locale.FRENCH) {
				if (h.getCheckListItem().getCheckListItemCategory() != null) {
					toCheck = h.getCheckListItem().getCheckListItemCategory().getNameFrench();
					toCheck += " - ";
				}
				toCheck += h.getCheckListItem().getNameFrench();

			} else {
				if (h.getCheckListItem().getCheckListItemCategory() != null) {
					toCheck = h.getCheckListItem().getCheckListItemCategory().getName();
					toCheck += " - ";
				}
				toCheck += h.getCheckListItem().getName();

			}

			p.setChecklistItemName(toCheck);
			p.setCreateOffer(h.getCreateOffer());
			p.setDescription(h.getDescription());
			p.setFunctionalControl(h.getFunctionalControl());
			p.setVisualControl(h.getVisualControl());
			p.setUrlImage("test");
			historyChecklist.add(p);
		}

		return historyChecklist;
	}

	public List<PrintHistory> generatePrintHistList() {
		List<PrintHistory> printListHist = new ArrayList<>();
		if (this.historyElements != null && this.historyElements.size() > 0) {
			for (ServiceElementHistory s : historyElements) {
				PrintHistory p = new PrintHistory();
				p.setDate(s.getDate());
				p.setName(s.getName());
				p.setState(s.getState());
				p.setRemark(s.getDescription());
				p.setIsRepaired(s.getChecklistitemsRepaired());
				p.setToRepair(s.getChecklistitems2Repair());
				p.setHistChecklist(generateHistoryChecklist(s));
				printListHist.add(p);
			}
		}

		return printListHist;
	}

	public void setMasterElementAttributes(PrintHistoryMaster mr) {
		if (this.selectedElement != null) {
			mr.setActualState(this.selectedElement.getLastState());
			mr.setBarcode(this.selectedElement.getElementnumber());
			mr.setFloor(this.selectedElement.getFloor());
			mr.setLastControlDate(this.selectedElement.getLastControlDate());
			mr.setOrientation(this.selectedElement.getOrientation());
		}
	}

	public void generateHistReport() throws SQLException, IOException {
		List<PrintHistoryMaster> masterReport = new ArrayList<>();
		PrintHistoryMaster mr = new PrintHistoryMaster();
		mr.setPrintHistory(generatePrintHistList());
		masterReport.add(mr);
		setMasterElementAttributes(mr);
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ReportGeneratorBean reportGeneratorBean = (ReportGeneratorBean) FacesContext.getCurrentInstance()
				.getApplication().getELResolver().getValue(elContext, null, "reportGeneratorBean");
		reportGeneratorBean.generateHistReport(masterReport, getSessionBean().getUserLocale());
	}

	public void generateHistReportDetail() throws SQLException, IOException {
		List<PrintHistoryMaster> masterReport = new ArrayList<>();
		PrintHistoryMaster mr = new PrintHistoryMaster();
		mr.setPrintHistory(generatePrintHistList());
		masterReport.add(mr);
		setMasterElementAttributes(mr);
		String documentName = null;
		if (this.selectedServiceListCustomer != null && this.selectedServiceListCustomer.getServiceContract() != null) {
			final String contractNumber = this.selectedServiceListCustomer.getServiceContract()
					.getServiceContractNumber().replace('/', '_');
			documentName = this.selectedElement.getElementnumber() + "_" + contractNumber + "_detail.pdf";
		}
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ReportGeneratorBean reportGeneratorBean = (ReportGeneratorBean) FacesContext.getCurrentInstance()
				.getApplication().getELResolver().getValue(elContext, null, "reportGeneratorBean");
		reportGeneratorBean.generateHistReportDetail(masterReport, getSessionBean().getUserLocale(), documentName);
	}

	public void resetChecklistEntryOk() {
		this.selectedChecklistitem.setWasSettedBackOk(false);
		this.serviceHistoryFacade.edit(this.selectedChecklistitem);
	}

	public void setElementOfferState() {
		// Get the latest checklist for the selected element.
		final ArrayList<ServiceHistory> entries = new ArrayList<>();
		entries.addAll(this.serviceHistoryFacade.findChecklistForSelectedHistoryElementByDate(
				this.historyElements.get(this.historyElements.size() - 1), this.selectedElement));
		Boolean offerStatus = false;
		if (!entries.isEmpty()) {
			// Entry found checking offer status
			for (ServiceHistory entry : entries) {
				if (entry.getCreateOffer()) {
					// Offer status true found, update offerStatus & break.
					offerStatus = true;
					break;
				} else {
					// Update element offer status.
					this.selectedElement.setCreateOffer(offerStatus);
					this.serviceElementFacade.edit(this.selectedElement);
				}
			}
		}
	}

	public void setSelectedHistoryElementByDate() {
		Date itemDate = null;
		if (this.historyElementsSelected != null) {
			itemDate = this.historyElementsSelected.getDate();
		}

		this.historyElements = serviceHistoryFacade.findHistoryElementFromSelected(this.selectedElement,
				this.selectedServiceListCustomer);

		for (ServiceElementHistory seh : historyElements) {
			if (itemDate == null) {
				this.historyElementsSelected = historyElements.get(0);
			} else {
				if (seh.getDate() == itemDate) {
					this.historyElementsSelected = seh;
				}
			}
		}

	}

	public void setSelectedElementByBarcode() {
		Integer idEl = 0;
		if (this.selectedElement != null) {
			idEl = this.selectedElement.getId();
		}

		this.serviceElements = serviceFacade.findElementsForServiceSorted(this.selectedServiceListCustomer);
		this.serviceElements = new ArrayList<>(this.serviceElements);		
		for (ServiceElements se : serviceElements) {
			if (se.getId() == idEl) {
				this.selectedElement = se;
			}
		}
	}

	/*
	 * zurücksetzen auf ok
	 */
	public void setChecklistEntryOk() {
		// this.selectedChecklistitem.setComment(this.addCommentOk);
		if (this.selectedChecklistitem != null) {
			this.selectedChecklistitem.setDescription(this.addDescriptionOk);
			this.selectedChecklistitem.setCheckedAsOk(true);
			this.selectedChecklistitem.setCheckedAsLack(false);
			this.selectedChecklistitem.setCheckedAsRepaired(false);
			this.selectedChecklistitem.setFunctionalControl(true);
			this.selectedChecklistitem.setVisualControl(true);
			this.selectedChecklistitem.setCheckedAsDefect(false);
			this.selectedChecklistitem.setWasSettedBackOk(true);
			this.selectedChecklistitem.setCreateOffer(false);
			// TODO:: check selectedElement => delete offerFlag
			setElementOfferState();
			this.serviceHistoryFacade.edit(this.selectedChecklistitem);
			System.out.println("test");
			// historyElementsSelected change state
			// selectedElement
			setSelectedHistoryElementByDate();
			setSelectedElementByBarcode();

			// this.historyElementsSelected = this.historyElements.get(0);

			this.selectedElement = this.selectedElement;

			this.serviceHistoryFacade.edit(this.selectedChecklistitem);
		}

	}

	public String setBackOkFormat(Boolean wasSetBack) {
		if (wasSetBack) {
			return "setBack";
		} else {
			return "";
		}
	}
	
	/**
	 * Delete ObjectComment (Notice) from Customer / Historie View
	 */
	public void delNotice() {
		System.out.println(this.historyElementsSelected.getName());
		if (this.historyElementsSelected.getCommentId() != null) {
			ObjectComments o = objectCommnentFacade.find(this.historyElementsSelected.getCommentId());
			objectCommnentFacade.remove(o);			
			historyElements.remove(this.historyElementsSelected);
		}
		
	}
	/**
	 * Adds ObjectComment to ServiceElement from Customer / History View 
	 */
	public void aNotice() {
		System.out.println("aNotice");
		if (this.notice != null && this.notice.length() > 0 ) {			
			ObjectComments o = new ObjectComments();
			o.setComment(this.notice);
			o.setCreateDate(new Date());
			o.setObjectType(ServiceElements.class.getName());
			o.setObjectId(selectedElement.getId());
			objectCommnentFacade.create(o);
			List<ObjectComments> oList =  this.selectedElement.getComments(); 
			oList.add(o); 
			this.selectedElement.setComments(oList);
			this.historyElements = serviceHistoryFacade.findHistoryElementFromSelected(this.selectedElement,
					this.selectedServiceListCustomer);
			this.notice = null;
		}
		
	}
	

	public void resetValues() {
		this.repairComment = null;
		this.repairCustomerName = null;
		if (this.selectedChecklistitem != null) {
			this.addDescriptionOk = this.selectedChecklistitem.getDescription();
		} else {
			this.addDescriptionOk = null;
		}

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

	public List<Service> getServiceListCustomer() {
		return serviceListCustomer;
	}

	public void setServiceListCustomer(List<Service> serviceListCustomer) {
		this.serviceListCustomer = serviceListCustomer;
	}

	public List<Service> getFilteredServiceListCustomer() {
		return filteredServiceListCustomer;
	}

	public void setFilteredServiceListCustomer(List<Service> filteredServiceListCustomer) {
		this.filteredServiceListCustomer = filteredServiceListCustomer;
	}

	public Service getSelectedServiceListCustomer() {
		return selectedServiceListCustomer;
	}

	public void setSelectedServiceListCustomer(Service selectedServiceListCustomer) {
		this.selectedServiceListCustomer = selectedServiceListCustomer;
	}

	public List<ServiceElementHistory> getHistoryElements() {
		return historyElements;
	}

	public void setHistoryElements(List<ServiceElementHistory> historyElements) {
		this.historyElements = historyElements;
	}

	public ServiceElementHistory getHistoryElementsSelected() {
		return historyElementsSelected;
	}

	public void setHistoryElementsSelected(ServiceElementHistory historyElementsSelected) {
		this.historyElementsSelected = historyElementsSelected;
	}

	public List<ServiceElementHistory> getHistoryElementsFiltered() {
		return historyElementsFiltered;
	}

	public void setHistoryElementsFiltered(List<ServiceElementHistory> historyElementsFiltered) {
		this.historyElementsFiltered = historyElementsFiltered;
	}

	public Boolean getRowSelected() {
		return rowSelected;
	}

	public void setRowSelected(Boolean rowSelected) {
		this.rowSelected = rowSelected;
	}

	public String getRepairCustomerName() {
		return repairCustomerName;
	}

	public void setRepairCustomerName(String repairCustomerName) {
		this.repairCustomerName = repairCustomerName;
	}

	public String getRepairComment() {
		return repairComment;
	}

	public void setRepairComment(String repairComment) {
		this.repairComment = repairComment;
	}

	public String getAddCommentOk() {
		return addDescriptionOk;
	}

	public void setAddCommentOk(String addCommentOk) {
		this.addDescriptionOk = addCommentOk;
	}

	public String getNotice() {
		return notice;
	}

	public void setNotice(String notice) {
		this.notice = notice;
	}

	public ServiceElements getSelectedElement23() {
		return selectedElement23;
	}

	public void setSelectedElement23(ServiceElements selectedElement23) {
		this.selectedElement23 = selectedElement23;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}


}
