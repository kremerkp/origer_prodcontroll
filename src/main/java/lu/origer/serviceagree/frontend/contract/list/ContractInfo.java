package lu.origer.serviceagree.frontend.contract.list;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.io.IOUtils;
import org.omnifaces.util.Faces;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import lu.origer.serviceagree.backend.assoc.AssocServiceElementFacade;
import lu.origer.serviceagree.backend.contact.AddressFacade;
import lu.origer.serviceagree.backend.contact.ContactFacade;
import lu.origer.serviceagree.backend.contact.ContactTypeFacade;
import lu.origer.serviceagree.backend.contact.PersonFacade;
import lu.origer.serviceagree.backend.contact.PersonTypeFacade;
import lu.origer.serviceagree.backend.contract.BuildingSiteFacade;
import lu.origer.serviceagree.backend.contract.ChecklistFacade;
import lu.origer.serviceagree.backend.contract.ChecklistItemFacade;
import lu.origer.serviceagree.backend.contract.ContractTypeFacade;
import lu.origer.serviceagree.backend.contract.DinTypeFacade;
import lu.origer.serviceagree.backend.contract.DirectionTypeFacade;
import lu.origer.serviceagree.backend.contract.ElementTypeFacade;
import lu.origer.serviceagree.backend.contract.FileArchiveFacade;
import lu.origer.serviceagree.backend.contract.FrontTypeFacade;
import lu.origer.serviceagree.backend.contract.ServiceContractFacade;
import lu.origer.serviceagree.backend.contract.ServiceElementFacade;
import lu.origer.serviceagree.backend.contract.ServiceFacade;
import lu.origer.serviceagree.backend.contract.ServiceTypeFacade;
import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.backend.synch.ServiceHistoryFacade;
import lu.origer.serviceagree.frontend.main.ApplicationBean;
import lu.origer.serviceagree.frontend.main.BasicBean;
import lu.origer.serviceagree.frontend.report.ReportGeneratorBean;
import lu.origer.serviceagree.models.assocs.AssocServiceElementsId;
import lu.origer.serviceagree.models.assocs.Assoc_Checklist_Checklistentries;
import lu.origer.serviceagree.models.assocs.Assoc_Service_Elements;
import lu.origer.serviceagree.models.checklist.Checklist;
import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contact.Address;
import lu.origer.serviceagree.models.contact.Contact;
import lu.origer.serviceagree.models.contact.Country;
import lu.origer.serviceagree.models.contact.Person;
import lu.origer.serviceagree.models.contact.types.DinType;
import lu.origer.serviceagree.models.contact.types.Direction;
import lu.origer.serviceagree.models.contact.types.ElementType;
import lu.origer.serviceagree.models.contact.types.Front;
import lu.origer.serviceagree.models.contract.BuildingSite;
import lu.origer.serviceagree.models.contract.Invoice;
import lu.origer.serviceagree.models.contract.Offers;
import lu.origer.serviceagree.models.contract.Service;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.contract.ServiceType;
import lu.origer.serviceagree.models.contract.custom.AddServiceType;
import lu.origer.serviceagree.models.main.FileArchive;
import lu.origer.serviceagree.models.synch.ServiceHistory;

@ManagedBean
@ViewScoped
public class ContractInfo extends BasicBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Integer NUMERIC_POSITIONS = 5;
	private static Integer NO_FLOOR_NUMBER_LENGTH = 7;

	@Inject
	private ContactTypeFacade contactTypeFacade;
	@Inject
	private ServiceTypeFacade serviceTypeFacade;
	@Inject
	private ContractTypeFacade contractTypeFacade;
	@Inject
	private BuildingSiteFacade buildingSiteFacade;
	@Inject
	private PersonTypeFacade personTypeFacade;
	@Inject
	private ServiceFacade serviceFacade;
	@Inject
	private PersonFacade personFacade;
	@Inject
	private ServiceContractFacade serviceContractFacade;
	@Inject
	private ChecklistFacade checklistFacade;
	@Inject
	private ChecklistItemFacade checklistItemFacade;
	@Inject
	private ServiceElementFacade serviceElementFacade;
	@Inject
	private AddressFacade addressFacade;
	@Inject
	private DinTypeFacade dinTypFacade;
	@Inject
	private ContactFacade contactFacade;
	@Inject
	private ElementTypeFacade elementTypeFacade;
	@Inject
	private FrontTypeFacade frontTypeFacade;
	@Inject
	private DirectionTypeFacade directionTypeFacade;
	@Inject
	private AssocServiceElementFacade assocServiceElementFacade;
	@Inject
	private ServiceHistoryFacade serviceHistoryFacade;
	@Inject
	FileArchiveFacade fileArchiveFacade;
	@Inject
	DinTypeFacade dinTypeFacade;

	private Person customerContact;
	private Person technicalContact;
	private ServiceContract serviceContract;
	private String object;
	private ServiceType serviceTypeObject;
	private Boolean disableMode = true;
	// Disable new buttons
	private Boolean disableNewBtnBS = true;
	private Boolean disableNewBtnCustomer = true;
	private Boolean disableNewBtnTechnician = true;
	private Boolean disableNewBtnService = true;
	private Boolean disableNewBtnContract = true;
	// Disable edit buttons
	private Boolean disableEditBtnBS = true;
	private Boolean disableEditBtnCustomer = true;
	private Boolean disableEditBtnTechnician = true;
	private Boolean disableEditBtnService = true;
	private Boolean disableEditBtnContract = true;
	// Disable input fields
	private Boolean disableEditModeBS = true;
	private Boolean disableEditModeCustomer = true;
	private Boolean disableEditModeTechnician = true;
	private Boolean disableEditModeService = true;
	private Boolean disableEditModeContract = true;
	// Disable save buttons
	private Boolean disableSaveBtnBS = true;
	private Boolean disableSaveBtnCustomer = true;
	private Boolean disableSaveBtnTechnician = true;
	private Boolean disableSaveBtnService = true;
	private Boolean disableSaveBtnContract = true;
	private Boolean newBSAdded = false;
	private Boolean newCustAdded = false;
	private Boolean newTechAdded = false;
	private Boolean editContract = false;
	private Boolean elementsAssigned = false;
	private Integer countContractsBuildingSite;

	private Boolean bsSelected = false;
	private Boolean cusSelected = false;
	private Boolean techSelected = false;
	private Boolean contractValid = false;
	private Boolean createServiceOK = false;
	private Boolean newBuildingSite = false;
	private Boolean newCustomer = false;
	private Boolean newTechnician = false;

	private Boolean disableContractNrAC = true;

	private Boolean transactionValid;

	private Map<String, String> serviceTypeList = new HashMap<String, String>();
	private Map<String, String> contractTypeList = new HashMap<String, String>();

	public List<Service> serviceList;
	private List<ElementType> elementTypeList;
	private List<Front> frontList;
	private List<DinType> DINList;
	private List<Direction> directionList;
	private List<DinType> dinTypeList;
	public List<Service> filteredServiceList;
	private Service selectedService;
	private ServiceElements selectedServiceElement;
	private List<ServiceElements> selectedServiceElements;
	private List<Invoice> invoiceList;
	private List<Offers> offerList;
	private List<ServiceElements> serviceElementList;
	private List<AddServiceType> serviceTypeTable;
	private List<Person> custlist;
	private List<Person> techlist;

	private AddServiceType selectedServiceType;

	// Panel BuildingSite
	private String buildingSiteCode;
	private String buildingSiteName;
	private String buildingSiteStreet;
	private String buildingSiteStreetNumber;
	private String buildingSiteZip;
	private String buildingSiteTown;
	private String buildingSitePhoneNumber;
	private String countedContracts;
	private Country buildingCountry;
	private BuildingSite buildingSiteObject;

	private String addComment;
	private String addFloor;
	private String addRoom;

	// Popup renew contract
	private String renewDescription;
	private Date renewStartDate;
	private Date renewEndDate;
	private BigDecimal renewValue;
	private Integer renewNumberIntervals;

	private ElementType elementTypeForAll;

	private Direction directionForAll;

	private Front frontForAll;

	private DinType DINForAll;

	private Checklist elementChecklist;
	/* Add element popup values */
	private List<AddElements> addElements;
	private AddElements footerLine;
	private ArrayList<FloorEntry> floorList;

	private Integer numberOfElements;

	// Kontakt Customer
	@Size(min = 2, message = "Vorname muß mindest 2 Buchstaben lang sein")
	private String customerFirstname;
	@Size(min = 2, message = "Name muß mindest 2 Buchstaben lang sein")
	private String customerLastname;
	private String customerTown;

	private String customerTitel;
	@Size(min = 2, message = "Name muß mindest 2 Buchstaben lang sein")
	private String customerCompany;

	@Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "Keine gültige E-Mail Adresse")
	private String customerMailAdress;
	private String customerPhoneNumber;
	private String customerMobilNumber;

	private String customerStreet;
	private String customerZip;
	private Country customerCountry;
	private String customerStreetnumber;

	// Kontakt Technician
	@Size(min = 2, message = "Name muß mindest 2 Buchstaben lang sein")
	private String techFirstname;
	@Size(min = 2, message = "Name muß mindest 2 Buchstaben lang sein")
	private String techShortname;
	private String techLastename;
	@Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "Keine gültige E-Mail Adresse")
	private String techMailAdress;
	private String techPhoneNumber;
	@Size(min = 6, message = "Name muß mindest 6 Zeichen lang sein")
	private String techMobilNumber;

	// Service Basedata
	private Date serviceStartdate;

	private Date serviceEnddate;
	private Integer serviceIntervall;
	private BigDecimal serviceSumPerInterval;
	private Date firstIntervallDate;
	private String serviceName;
	private String serviceDesc;

	// Services Generator DefaultValues
	private Date genServiceLatestServicedate;
	private Integer genServiceActualTimeSpend;
	private Integer genServiceTargetTimeSpend;
	private String genServiceInvoice;
	private String genServiceOffer;
	private String genServiceTyp;
	// private String genServiceTypContract;
	private String genContractTypeContract;
	
	private ArrayList<Integer> resetElementIds = new ArrayList<>();

	/**
	 * Used to get an overview on existing floors for the selected building.
	 * 
	 * @author ian.husting
	 *
	 */
	private class FloorEntry {
		private String floor;
		private Integer startNumber;

		public String getFloor() {
			return floor;
		}

		public void setFloor(String floor) {
			this.floor = floor;
		}

		public Integer getStartNumber() {
			return startNumber;
		}

		public void setStartNumber(Integer startNumber) {
			this.startNumber = startNumber;
		}

		@Override
		public boolean equals(Object obj) {
			Boolean result = false;
			if (obj != null && obj.getClass().equals(this.getClass())) {
				FloorEntry item = ((FloorEntry) obj);
				if (item.getFloor().equalsIgnoreCase(this.getFloor())) {
					result = true;
				}
			}
			return result;
		}
	}

	private void setButtonStatus(final Boolean newStatus, final Boolean editStatus, final Boolean saveStatus) {
		this.disableNewBtnBS = newStatus;
		this.disableNewBtnCustomer = newStatus;
		this.disableNewBtnTechnician = newStatus;
		this.disableNewBtnContract = newStatus;
		// Disable edit buttons
		this.disableEditBtnBS = editStatus;
		this.disableEditBtnCustomer = editStatus;
		this.disableEditBtnTechnician = editStatus;
		this.disableEditBtnContract = editStatus;
		// Disable save buttons
		this.disableSaveBtnBS = saveStatus;
		this.disableSaveBtnCustomer = saveStatus;
		this.disableSaveBtnTechnician = saveStatus;
		this.disableSaveBtnContract = saveStatus;
	}

	public void saveService() {
		this.disableMode = true;
		persistServicesToDb(false);
		this.transactionValid = true;
		this.disableEditModeService = true;
		this.disableEditModeCustomer = true;
		this.disableEditModeTechnician = true;
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", getFacesMessage()));
	}

	public void save() {
		this.disableMode = true;
		persistServicesToDb(true);
		this.transactionValid = true;
		this.disableEditModeService = true;
		this.disableEditModeCustomer = true;
		this.disableEditModeTechnician = true;
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Info!", getFacesMessage()));
	}

	/**
	 * Wartungsvertrag speichern
	 */
	public void persistServicesToDb(Boolean withElements) {
		// List<Services> ser = this.serviceList;

		for (Service s : this.serviceList) {
			for (ServiceElements e : s.getServiceElementList()) {
				e.setEditDate(new Date());
				e.setEditUser(getSessionBean().getCurrentUser().getId());
				if (withElements) {
					// Reset checklist for elements with individual checklists to prevent overwriting the value. 
					if(resetElementIds.contains(e.getId()))
					{
						e.setChecklist(this.checklistFacade.getInitialChecklist(e.getId()));
						//System.out.println("RESET CHECKLIST: " + this.checklistFacade.getInitialChecklist(e.getId()).getId());
					}
					serviceElementFacade.edit(e);
				}
			}
			s.setEditDate(new Date());
			s.setEditUser(getSessionBean().getCurrentUser().getId());
			serviceFacade.edit(s);
		}
		this.resetElementIds = new ArrayList<>();
	}

	public String getFacesMessage() {
		return "Es wurden alle relevanten Daten festgelegt. Die Intervalle / Wartungen können jetzt erstellt werden.";

	}

	public List<AddServiceType> initServiceTypesTest() {
		List<AddServiceType> ast = new ArrayList<>();
		for (int i = 0; i <= 4; i++) {
			AddServiceType a = new AddServiceType();
			a.setIntervall((int) ((Math.random()) * 4 + 1));
			a.setLatestServiceDate(new Date());
			a.setServiceType(serviceTypeFacade.find((int) ((Math.random()) * 4 + 1)));
			ast.add(a);
		}
		return ast;
	}

	public Address getBuildingSiteAdress() {
		Address ad = new Address();
		ad.setActive(true);
		ad.setCity(this.buildingSiteTown);
		// ad.setCountry(buildingCountry);
		// ad.setCountry(countryFacade.findById(CountryFacade.LUXEMBOURG));
		ad.setCreateDate(new Date());
		return ad;

	}

	public ContractInfo() {
		super();
	}

	public void initEditMode() {
		this.disableMode = false;
		this.cusSelected = false;
		this.techSelected = false;

	}

	@PostConstruct
	public void init() {

		Integer id = getApplicationBean().getIdFromURL();
		this.custlist = personFacade.findCustomers();
		this.techlist = personFacade.findTechnician();
		this.elementTypeList = elementTypeFacade.findAllActive();
		this.frontList = frontTypeFacade.findAllActiveFront();
		this.DINList = dinTypFacade.findAllActive();
		this.directionList = directionTypeFacade.findAll();
		this.dinTypeList = dinTypeFacade.findAll();
		if (id != null) {
			initilizeContractEdit(id);
		} else {
			Integer newService = getApplicationBean().getEditModeFromURL();
			this.newBuildingSite = true;
			if (newService != null && newService == 1) {
				initEditMode();
			} else {
				// Activate building site panel
				this.setButtonStatus(false, false, false);
				this.disableEditModeBS = false;
				this.disableSaveBtnBS = true;
			}
			this.buildingSiteObject = new BuildingSite();
			this.customerContact = new Person();
			this.technicalContact = new Person();
			this.serviceTypeTable = new ArrayList<>();
			this.serviceElementList = new ArrayList<>();
			this.serviceList = new ArrayList<>();
		}
		this.serviceTypeList = serviceTypeFacade.getServiceTypeListMap();
		this.contractTypeList = contractTypeFacade.getContractTypeListMap();
		this.setServiceTypeList(serviceTypeList);
		this.setContractTypeList(contractTypeList);
		// Init renew contract fields
		if (this.getServiceContract() != null) {
			this.renewStartDate = this.getServiceContract().getTodate();
		} else {
			this.renewStartDate = null;
		}
		this.renewEndDate = null;
		this.renewValue = new BigDecimal(0);
		this.renewDescription = "";
		this.initAddElements();
		this.floorList = new ArrayList<>();
	}

	/**
	 * Clears the element list for the add-elements-popup or creates it if it
	 * doesn't exist yet and adds the generic footer line.
	 */
	public void initAddElements() {
		// Clear element list or create it if it does not exists.
		if (this.addElements == null) {
			this.addElements = new ArrayList<>();
		} else {
			this.addElements.clear();
		}
		// Generate or reset footer entry.
		if (this.footerLine == null) {
			this.footerLine = new AddElements();
			this.footerLine.setShowDeleteButton(false);
		}
		this.footerLine.setAmount(0);
		this.addFloor = "";
		try {
			this.footerLine.setChecklist(this.checklistFacade.findAllActive().get(0));
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.addElements.add(this.footerLine);
	}

	/**
	 * Adds a new line to the add-elements-dialog.
	 */
	public void addElementLine(final Integer index) {
		final AddElements newElementLine = new AddElements();
		newElementLine.setAmount(1);
		try {
			newElementLine.setChecklist(this.footerLine.getChecklist());
		} catch (Exception e) {
			e.printStackTrace();
		}
		newElementLine.setFloor("");
		newElementLine.setShowDeleteButton(true);
		this.addElements.add(index, newElementLine);
		this.updateElementTotal();
	}
	
	/**
	 * Sets the checklist of all new elements to the one selected in the footer line.
	 */
	public void updateChecklists()
	{
		Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Calling " + this.getClass().getName() + ".updateChecklist()");
		for(AddElements element :  this.addElements)
		{
			try
			{
			element.setChecklist(this.footerLine.getChecklist());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * Calculates the total amount of added elements.
	 */
	public void updateElementTotal()
	{
		Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Calling " + this.getClass().getName() + ".updateElementTotal()");
		this.footerLine.setAmount(0);
		for(int c = 0; c < this.addElements.size()-1; c++)
		{
			this.footerLine.setAmount(this.footerLine.getAmount() + this.addElements.get(c).getAmount());
		}
	}

	/**
	 * Removes the line with the corresponding index from the add-elements-dialog.
	 * 
	 * @param rowIndex
	 */
	public void removeElementLine(final Integer rowIndex) {
		if (rowIndex != null && rowIndex > -1) {

			this.addElements.remove(this.addElements.get(rowIndex));
		}
		this.updateElementTotal();
	}

	public void setBuildingSiteInputFields(BuildingSite bs) {
		if (bs.getAddress() != null) {
			Address ad = bs.getAddress();
			this.buildingCountry = ad.getCountry();
			this.buildingSiteStreet = ad.getStreet();
			this.buildingSiteStreetNumber = ad.getStreetNumber();
			this.buildingSiteTown = ad.getCity();
			this.buildingSiteZip = ad.getZip();
		}
		this.buildingSiteCode = bs.getCode();
		this.buildingSiteName = bs.getName();
		this.buildingSitePhoneNumber = ContactFacade.getPhoneNumberFromContactList(bs.getContactList());
	}

	public void generateBarcodeReport() throws IOException, InterruptedException, SQLException {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ReportGeneratorBean reportGeneratorBean = (ReportGeneratorBean) FacesContext.getCurrentInstance()
				.getApplication().getELResolver().getValue(elContext, null, "reportGeneratorBean");
		if (selectedServiceElements != null && selectedServiceElements.size() > 0) {
			reportGeneratorBean.generateBarcodeReport(selectedServiceElements);
		}
	}

	public void setServiceContractFields(BuildingSite bs) {
		ServiceContract sc = serviceContractFacade.findByBuildingSite(bs);
		try {
			this.serviceContract = sc;
			setServiceStartdate(sc.getFromdate());
			setServiceName(sc.getServiceContractNumber());
			setServiceDesc(sc.getDescription());
			setServiceEnddate(sc.getTodate());
			setServiceSumPerInterval(sc.getServicesAmount());
		} catch (Exception e) {
			this.serviceContract = null;
			setServiceStartdate(null);
			setServiceName(null);
			setServiceDesc(null);
			setServiceEnddate(null);
			setServiceSumPerInterval(null);
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
					"NPE: Es wurde keine ServiceContract zu der BulidingSite gefunden! " + e.getMessage());
		}
	}

	public void setTechnicianFields(BuildingSite bs) {
		ServiceContract sc = serviceContractFacade.findByBuildingSite(bs);
		try {
			this.technicalContact = sc.getTechnician();
			setTechFirstname(sc.getTechnician().getFirstname());
			setTechShortname(sc.getTechnician().getShortname());
			setTechLastename(sc.getTechnician().getLastname());
			setTechMailAdress(ContactFacade.getMailFromContactList(sc.getTechnician().getContactList()));
			setTechMobilNumber(ContactFacade.getMobilNumberFromContactList(sc.getTechnician().getContactList()));
			setTechPhoneNumber(ContactFacade.getPhoneNumberFromContactList(sc.getTechnician().getContactList()));

		} catch (Exception e) {
			this.technicalContact = null;
			setTechFirstname(null);
			setTechShortname(null);
			setTechLastename(null);
			setTechMailAdress(null);
			setTechMobilNumber(null);
			setTechPhoneNumber(null);
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
					"NPE: Es wurde keine ServiceContract zu der BulidingSite gefunden! " + e.getMessage());
		}
	}

	public void setCustomerFields(BuildingSite bs) {
		try {
			ServiceContract sc = serviceContractFacade.findByBuildingSite(bs);
			this.customerContact = sc.getPartner();

			if (this.customerContact.getAddress() != null) {
				Address ad = this.customerContact.getAddress();
				this.customerCountry = ad.getCountry();
				this.customerStreet = ad.getStreet();
				this.customerStreetnumber = ad.getStreetNumber();
				this.customerTown = ad.getCity();
				this.customerZip = ad.getZip();
			}
			setCustomerLastname(sc.getPartner().getLastname());
			setCustomerFirstname(sc.getPartner().getFirstname());
			setCustomerTitel(sc.getPartner().getTitle());
			setCustomerCompany(sc.getPartner().getCompany());
			setCustomerMailAdress(ContactFacade.getMailFromContactList(sc.getPartner().getContactList()));
			this.customerPhoneNumber = ContactFacade.getPhoneNumberFromContactList(sc.getPartner().getContactList());
			this.customerMobilNumber = ContactFacade.getMobilNumberFromContactList(sc.getPartner().getContactList());

		} catch (Exception e) {
			this.customerContact = null;
			setCustomerLastname(null);
			setCustomerFirstname(null);
			setCustomerTitel(null);
			setCustomerCompany(null);
			setCustomerMailAdress(null);
			setCustomerStreet(null);
			setCustomerStreetnumber(null);
			setCustomerCountry(null);
			setCustomerTown(null);
			setCustomerZip(null);
			this.customerPhoneNumber = null;
			this.customerMobilNumber = null;
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE,
					"NPE: Es wurde keine ServiceContract zu der BulidingSite gefunden! " + e.getMessage());
		}
	}

	public void updateElementChecklist() {
		AutoComplete autoComplete = (AutoComplete) UIComponent.getCurrentComponent(Faces.getContext());
		ValueExpression exp = Faces.getApplication().getExpressionFactory()
				.createValueExpression(Faces.getContext().getELContext(), "#{c.name}", String.class);
		autoComplete.setValueExpression("itemLabel", exp);
		Checklist c = (Checklist) autoComplete.getValue();
		setElementChecklist(c);
	}

	public void updateCountries() {
		AutoComplete autoComplete = (AutoComplete) UIComponent.getCurrentComponent(Faces.getContext());
		ValueExpression exp = Faces.getApplication().getExpressionFactory()
				.createValueExpression(Faces.getContext().getELContext(), "#{c.name} - #{c.alpha2}", String.class);
		autoComplete.setValueExpression("itemLabel", exp);
		Country c = (Country) autoComplete.getValue();
		setBuildingCountry(c);
	}

	public void updateCountriesCustomer() {
		AutoComplete autoComplete = (AutoComplete) UIComponent.getCurrentComponent(Faces.getContext());
		ValueExpression exp = Faces.getApplication().getExpressionFactory()
				.createValueExpression(Faces.getContext().getELContext(), "#{c.name} - #{c.alpha2}", String.class);
		autoComplete.setValueExpression("itemLabel", exp);
		Country c = (Country) autoComplete.getValue();
		setCustomerCountry(c);
	}

	public String getElementNamesFromList(List<ServiceElements> eList) {
		String res = "";
		Integer counter = 0;
		if (eList != null && eList.size() > 0) {
			for (ServiceElements se : eList) {
				counter++;
				if (eList.size() == counter) {
					res += se.getElementnumber();
				} else {
					res += se.getElementnumber() + ", ";
				}
			}

		}
		return res;
	}

	public void removeElementsWithoutAssoc(ServiceElements se) {
		if (!serviceElementFacade.elementHasAssoc(se)) {
			serviceElementFacade.remove(se);
		}

	}

	public void removeHistory(ServiceElements se, Service s) {
		List<ServiceHistory> sHistList = new ArrayList<>();
		sHistList = serviceHistoryFacade.findChecklistForElementNotNull(se, s);
		for (ServiceHistory sh : sHistList) {
			serviceHistoryFacade.remove(sh);
		}
	}

	public void removeAssocElements(List<ServiceElements> elList, Service s) {
		for (ServiceElements se : elList) {
			AssocServiceElementsId sId = new AssocServiceElementsId();
			sId.setElements(se.getId());
			sId.setService(s.getId());
			Assoc_Service_Elements ase = new Assoc_Service_Elements();
			ase.setId(sId);
			assocServiceElementFacade.remove(ase);
		}

	}

	/**
	 * ausgewählte elemente löschen
	 */
	public void delteSericeElementsFromService() {

		if (selectedService != null && selectedServiceElements != null) {
			for (ServiceElements se : selectedServiceElements) {
				if (serviceFacade.historyAllreadySynchronized(selectedService, se)) {
					FacesContext.getCurrentInstance().addMessage("Element: nicht aus dem Service gelöscht!",
							new FacesMessage(FacesMessage.SEVERITY_WARN, " ",
									"Das Element wurde bereits synchronisiert und kann nicht mehr gelöscht werden"));
				} else {
					List<ServiceElements> source = this.selectedService.getServiceElementList();
					List<ServiceElements> delete = this.selectedServiceElements;
					List<ServiceElements> newList = new ArrayList<>();
					newList.addAll(source);
					newList.removeAll(delete);
					// Löscht das ServiceElement als ganzes!
					// deleteElementListforAllServices(this.selectedServiceElements);

					// löschen der Assoziation aus der selektierten Tabelle
					removeAssocElements(this.selectedServiceElements, this.selectedService);
					removeHistory(se, this.selectedService);

					// wenn keine assoziation mehr besteht kann das Element
					// komplett aus der DB gelöscht werden

					// Aktualisierung der Intervallliste
					if (this.serviceContract != null) {
						this.serviceList = serviceFacade.findServiceFromServiceContract(this.serviceContract);
					}

					this.selectedService.setServiceElementList(newList);
					this.serviceElementList = new ArrayList<>();
					this.serviceElementList = newList;

					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Elemente gelöscht",
							"Die Elemente: " + "" + " wurden aus dem ausgewählten Intervallen der Historie gelöscht!"));

				}
				removeElementsWithoutAssoc(se);
			}

		}

	}

	public void updateServiceContract() {
		AutoComplete autoComplete = (AutoComplete) UIComponent.getCurrentComponent(Faces.getContext());
		ValueExpression exp = Faces.getApplication().getExpressionFactory()
				.createValueExpression(Faces.getContext().getELContext(), "#{c.serviceContractNumber}", String.class);

		autoComplete.setValueExpression("itemLabel", exp);
		ServiceContract sc = (ServiceContract) autoComplete.getValue();
		this.serviceContract = sc;

		if (sc.getBuildingSite() != null) {
			System.out.println("es gibt bereits eine Baustelle zu dem ausgwählten Wartungsvertrag. ");
			System.out.println("wollen sie den Wartungsvertrags der Baustelle: " + this.buildingSiteObject.getName()
					+ " zuordnen? ");
			System.out.println(" Die Zuordnung des Wartungsvertrags. ");

		}
		setServiceContractFields(sc.getBuildingSite());
		this.serviceList = serviceFacade.findServiceFromServiceContract(sc);
	}

	public void updateBuildingSite() {
		AutoComplete autoComplete = (AutoComplete) UIComponent.getCurrentComponent(Faces.getContext());
		ValueExpression exp = Faces.getApplication().getExpressionFactory()
				.createValueExpression(Faces.getContext().getELContext(), "#{c.code}", String.class);
		autoComplete.setValueExpression("itemLabel", exp);
		BuildingSite bs = (BuildingSite) autoComplete.getValue();
		this.buildingSiteObject = bs;
		this.countedContracts = getCountedContractForBuildingSite(this.buildingSiteObject);
		setBuildingSiteInputFields(bs);
		setCustomerFields(bs);
		setTechnicianFields(bs);
		setServiceContractFields(bs);
		ServiceContract sc = serviceContractFacade.findByBuildingSite(bs);
		if (sc == null) {
			this.serviceList = new ArrayList<>();
		} else {
			this.serviceList = serviceFacade.findServiceFromServiceContract(sc);
		}
		this.disableEditModeBS = true;
		this.bsSelected = true;
	}

	public void nextContract() {
		if (this.buildingSiteObject != null) {
			if (serviceContractFacade.countedServiceContractsForBuildingSite(this.buildingSiteObject) > 1) {
				Integer nextId = serviceContractFacade.getNextContractId(this.buildingSiteObject.getId(),
						this.serviceContract.getId());
				try {
					Faces.redirect(
							"faces/origer/serveAgree/contract/contract_edit.xhtml?faces-redirect=true&id=" + nextId);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getCountedContractForBuildingSite(BuildingSite bs) {
		Integer size = serviceContractFacade.countedServiceContractsForBuildingSite(bs);
		String res = "";
		if (size == null || size == 0) {
			res = "";
		} else if (size == 1) {
			res = size.toString() + " Vertrag";
		} else if (size > 1) {
			res = size.toString() + " Verträge";
		}
		return res;
	}

	public void initilizeContractEdit(Integer contractID) {
		ServiceContract sc = serviceContractFacade.find(contractID);
		this.buildingSiteObject = sc.getBuildingSite();
		this.countedContracts = getCountedContractForBuildingSite(this.buildingSiteObject);
		this.buildingSiteCode = sc.getBuildingSite().getCode();
		// this.genServiceTypContract = sc.getServiceType().getName();
		this.genContractTypeContract = sc.getContractType().getName();
		setBuildingSiteInputFields(sc.getBuildingSite());
		setCustomerFields(sc.getBuildingSite());
		setTechnicianFields(sc.getBuildingSite());
		setCountContractsBuildingSite(
				serviceContractFacade.countedServiceContractsForBuildingSite(sc.getBuildingSite()));
		// setServiceContractFields(sc.getBuildingSite());

		this.serviceContract = sc;
		try {
			setServiceStartdate(sc.getFromdate());
			setServiceName(sc.getServiceContractNumber());
			setServiceDesc(sc.getDescription());
			setServiceEnddate(sc.getTodate());
			setServiceSumPerInterval(sc.getServicesAmount());
			setGenServiceTyp(sc.getServiceType().getShortname());
		} catch (Exception e) {
			System.out.println("Fehler/Error: Service wurde nicht korrekt initialisiert");
		}

		this.serviceList = serviceFacade.findServiceFromServiceContract(sc);
		this.serviceTypeTable = new ArrayList<>();
		this.serviceElementList = new ArrayList<>();
		this.disableEditModeBS = true;
		this.bsSelected = true;
		this.setButtonStatus(true, true, false);
		this.newBuildingSite = false;
	}

	public void updateCustomer() {
		AutoComplete autoComplete = (AutoComplete) UIComponent.getCurrentComponent(Faces.getContext());
		ValueExpression exp = Faces.getApplication().getExpressionFactory()
				.createValueExpression(Faces.getContext().getELContext(), "#{c.lastname}", String.class);
		autoComplete.setValueExpression("itemLabel", exp);
		Person p = (Person) autoComplete.getValue();
		this.customerContact = p;
		this.customerCompany = p.getCompany();
		this.customerTitel = p.getTitle();
		this.customerLastname = p.getLastname();
		this.customerFirstname = p.getFirstname();
		this.customerMailAdress = ContactFacade.getMailFromContactList(p.getContactList());
		this.customerMobilNumber = ContactFacade.getMobilNumberFromContactList(p.getContactList());
		this.customerPhoneNumber = ContactFacade.getPhoneNumberFromContactList(p.getContactList());
		this.cusSelected = true;
		this.disableEditModeCustomer = true;
		if (p.getAddress() != null) {
			Address ad = p.getAddress();
			this.customerCountry = ad.getCountry();
			this.customerStreet = ad.getStreet();
			this.customerStreetnumber = ad.getStreetNumber();
			this.customerTown = ad.getCity();
			this.customerZip = ad.getZip();
		}
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Kunde geändert",
				"Damit der Kunde dem Vertrag zugeordnet wird muß der Wartungsvertrag gespeichert werden. Bitte klicken Sie auf 'Wartungsvertrag speichern'"));

	}

	public void updateTechnician() {
		AutoComplete autoComplete = (AutoComplete) UIComponent.getCurrentComponent(Faces.getContext());
		ValueExpression exp = Faces.getApplication().getExpressionFactory()
				.createValueExpression(Faces.getContext().getELContext(), "#{c.lastname}", String.class);
		autoComplete.setValueExpression("itemLabel", exp);
		Person p = (Person) autoComplete.getValue();
		this.technicalContact = p;
		this.techFirstname = p.getFirstname();
		this.techLastename = p.getLastname();
		this.techShortname = p.getShortname();
		this.techMailAdress = ContactFacade.getMailFromContactList(p.getContactList());
		this.techMobilNumber = ContactFacade.getMobilNumberFromContactList(p.getContactList());
		this.techPhoneNumber = ContactFacade.getPhoneNumberFromContactList(p.getContactList());
		this.techSelected = true;
		this.disableEditModeTechnician = true;
		if ((this.buildingSiteObject != null && this.buildingSiteObject.getId() != null)
				|| (this.customerContact != null && this.customerContact.getId() != null)
				|| (this.serviceContract != null && this.serviceContract.getId() != null)) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Techniker geändert",
					"Damit der Techniker dem Vertrag zugeordnet wird muß der Wartungsvertrag gespeichert werden. Bitte klicken Sie auf 'Wartungsvertrag speichern'"));
		}
	}

	public List<Checklist> completeChecklist(String find) {
		return checklistFacade.completeChecklist(find);

	}

	// public void createNewBuildingSite() {
	// HttpServletRequest request = (HttpServletRequest)
	// FacesContext.getCurrentInstance().getExternalContext()
	// .getRequest();
	// String buildingSiteName = request.getParameter("contractForm:bsNr");
	// String countryName = request.getParameter("contractForm:bsCountry");
	// }

	public Contact getContact(String contactField, Integer contactType) {
		Contact c = new Contact();
		c.setActive(true);
		c.setContactField(contactField);
		c.setContactType(contactTypeFacade.find(contactType));
		c.setEditDate(new Date());
		c.setCreateDate(new Date());
		c.setEditUser(getSessionBean().getCurrentUser().getId());
		c.setCreateUser(getSessionBean().getCurrentUser().getId());
		return c;
	}

	// public Person getServiceContractCustomer() {
	// if (!cusSelected) {
	// return setCustomerObjectIfNotSelected();
	// } else {
	// return this.customerContact;
	// }
	//
	// }

	public void setServiceContractIfNotSelected(Boolean isNew) {
		if (this.serviceContract == null) {
			this.serviceContract = new ServiceContract();
		}
		this.serviceContract.setActive(true);
		this.serviceContract.setCreateDate(new Date());
		this.serviceContract.setEditDate(new Date());
		this.serviceContract.setCreateUser(getSessionBean().getCurrentUser().getId());
		this.serviceContract.setEditUser(getSessionBean().getCurrentUser().getId());
		if (isNew) {
			this.serviceContract.setBuildingSite(this.buildingSiteObject);
		}

		this.serviceContract.setServiceContractNumber(this.serviceName);
		this.serviceContract.setDescription(getServiceDesc());
		this.serviceContract.setServiceContractNumber(getServiceName());
		this.serviceContract.setFirstIntervallDate(getFirstIntervallDate());
		this.serviceContract.setFromdate(getServiceStartdate());
		this.serviceContract.setTodate(getServiceEnddate());
		this.serviceContract.setIntervall(getServiceIntervall());
		this.serviceContract.setPartner(getCustomerContact());
		this.serviceContract.setServicesAmount(getServiceSumPerInterval());
		// this.serviceContract.setServiceType(serviceTypeFacade.findByName(genServiceTypContract));
		this.serviceContract.setContractType(contractTypeFacade.findByName(genContractTypeContract));
		this.serviceContract.setTechnician(getTechnicalContact());
		this.serviceContract.setTimePlannedTotal(getGenServiceTargetTimeSpend());
		this.serviceContract.setTimeSpentTotal(getGenServiceActualTimeSpend());

	}

	public void setCustomerObjectIfNotSelected(Boolean isNew) {
		if (this.customerContact == null) {
			this.customerContact = new Person();
		}
		this.customerContact.setActive(true);
		this.customerContact.setCreateDate(new Date());
		this.customerContact.setEditDate(new Date());
		this.customerContact.setCreateUser(getSessionBean().getCurrentUser().getId());
		this.customerContact.setEditUser(getSessionBean().getCurrentUser().getId());
		this.customerContact.setFirstname(this.customerFirstname);
		this.customerContact.setLastname(this.customerLastname);
		this.customerContact.setCompany(this.customerCompany);

		this.customerContact.setType(personTypeFacade.find(PersonTypeFacade.CUSTOMER));
		setCustomerContactList(isNew);
		setCustomerAddress(isNew);
		// this.customerContact.setAddress(null);
		this.customerContact.setTitle(this.customerTitel);
	}

	public void setCustomerContactList(Boolean isNew) {
		if (this.customerContact.getContactList() == null) {
			List<Contact> cList = new ArrayList<>();
			this.customerContact.setContactList(cList);
			cList.add(getContact(this.customerMobilNumber, ContactTypeFacade.PHONEMOBIL));
			cList.add(getContact(this.customerMailAdress, ContactTypeFacade.MAIL));
			cList.add(getContact(this.customerPhoneNumber, ContactTypeFacade.PHONEBUSINESS));
			this.customerContact.setContactList(cList);
		}
		if (!isNew) {
			List<Contact> cList = this.customerContact.getContactList();
			Contact phone = ContactFacade.getPhoneContactFromContactList(cList);
			Contact mobil = ContactFacade.getMobilContactFromContactList(cList);
			Contact mail = ContactFacade.getMailContactFromContactList(cList);
			if (this.customerPhoneNumber != null) {
				phone.setContactField(this.customerPhoneNumber);
				phone.setEditDate(new Date());
				phone.setEditUser((getSessionBean().getCurrentUser().getId()));
				if (this.customerPhoneNumber.length() > 0) {
					contactFacade.edit(phone);
				}
			}
			if (this.customerMobilNumber != null) {
				mobil.setContactField(this.customerMobilNumber);
				mobil.setEditDate(new Date());
				mobil.setEditUser((getSessionBean().getCurrentUser().getId()));
				if (this.customerMobilNumber.length() > 0) {
					contactFacade.edit(mobil);
				}
			}
			if (this.customerMailAdress != null) {
				mail.setContactField(this.customerMailAdress);
				mail.setEditDate(new Date());
				mail.setEditUser((getSessionBean().getCurrentUser().getId()));
				if (this.customerMailAdress.length() > 0) {
					contactFacade.edit(mail);
				}
			}

		}
	}

	public void setTechnicianObjectIfNotSelected(Boolean isNew) {
		if (this.technicalContact == null) {
			this.technicalContact = new Person();
		}
		this.technicalContact.setActive(true);
		this.technicalContact.setCreateDate(new Date());
		this.technicalContact.setEditDate(new Date());
		this.technicalContact.setCreateUser(getSessionBean().getCurrentUser().getId());
		this.technicalContact.setEditUser(getSessionBean().getCurrentUser().getId());
		this.technicalContact.setFirstname(this.techFirstname);
		this.technicalContact.setShortname(this.techShortname);
		this.technicalContact.setLastname(this.techLastename);
		this.technicalContact.setType(personTypeFacade.find(PersonTypeFacade.TECHNICIAN));
		setTechnicalContactList(isNew);
		this.technicalContact.setAddress(null);
	}

	public void setTechnicalContactList(Boolean isNew) {
		if (this.technicalContact.getContactList() == null) {
			List<Contact> cList = new ArrayList<>();
			this.technicalContact.setContactList(cList);
			cList.add(getContact(this.techMobilNumber, ContactTypeFacade.PHONEMOBIL));
			cList.add(getContact(this.techMailAdress, ContactTypeFacade.MAIL));
			cList.add(getContact(this.techPhoneNumber, ContactTypeFacade.PHONEBUSINESS));
			this.technicalContact.setContactList(cList);
		}
		if (!isNew) {
			List<Contact> cList = this.technicalContact.getContactList();
			Contact phone = ContactFacade.getPhoneContactFromContactList(cList);
			Contact mobil = ContactFacade.getMobilContactFromContactList(cList);
			Contact mail = ContactFacade.getMailContactFromContactList(cList);
			if (this.techPhoneNumber != null) {
				phone.setContactField(this.techPhoneNumber);
				phone.setEditDate(new Date());
				phone.setEditUser(getSessionBean().getCurrentUser().getId());
				if (this.techPhoneNumber.length() > 0) {
					contactFacade.edit(phone);
				}
			}
			if (this.techMobilNumber != null) {
				mobil.setContactField(this.techMobilNumber);
				mobil.setEditDate(new Date());
				mobil.setEditUser(getSessionBean().getCurrentUser().getId());
				if (this.techMobilNumber.length() > 0) {
					contactFacade.edit(mobil);
				}
			}
			if (this.techMailAdress != null) {
				mail.setContactField(this.techMailAdress);
				mail.setEditDate(new Date());
				mail.setEditUser(getSessionBean().getCurrentUser().getId());
				if (this.techMailAdress.length() > 0) {
					contactFacade.edit(mail);
				}
			}

		}
	}

	public void setBuildingSiteObjectIfNotSelected(Boolean isNew) {
		if (isNew && this.buildingSiteObject == null) {
			this.buildingSiteObject = new BuildingSite();
		}
		this.buildingSiteObject.setCode(getBuildingSiteCode());
		this.buildingSiteObject.setName(getBuildingSiteName());
		this.buildingSiteObject.setActive(true);
		this.buildingSiteObject.setCreateDate(new Date());
		this.buildingSiteObject.setEditDate(new Date());
		this.buildingSiteObject.setCreateUser(getSessionBean().getCurrentUser().getId());
		this.buildingSiteObject.setEditUser(getSessionBean().getCurrentUser().getId());
		setBuildingSiteAddress(isNew);
		setBuildingSiteContactList(isNew);

	}

	public void setBuildingSiteContactList(Boolean isNew) {
		if (this.buildingSiteObject.getContactList() == null) {
			List<Contact> cList = new ArrayList<>();
			this.buildingSiteObject.setContactList(cList);
			cList.add(getContact(buildingSitePhoneNumber, ContactTypeFacade.PHONEBUSINESS));
			this.buildingSiteObject.setContactList(cList);
		}
		if (!isNew) {
			List<Contact> cList = this.buildingSiteObject.getContactList();
			Contact phone = ContactFacade.getPhoneContactFromContactList(cList);
			phone.setContactField(buildingSitePhoneNumber);
			phone.setEditDate(new Date());
			phone.setEditUser(getSessionBean().getCurrentUser().getId());
			contactFacade.edit(phone);
		}
	}

	// public Person getServiceContractTechnician() {
	// if (!techSelected) {
	// return setTechnicianObjectIfNotSelected();
	// } else {
	// return this.technicalContact;
	// }
	// }

	public void setCustomerAddress(Boolean isNew) {
		if (this.customerContact.getAddress() == null) {
			this.customerContact.setAddress(new Address());
			this.customerContact.getAddress().setCreateDate(new Date());
			this.customerContact.getAddress().setCreateUser(getSessionBean().getCurrentUser().getId());
		}

		this.customerContact.getAddress().setStreet(getCustomerStreet());
		this.customerContact.getAddress().setStreetNumber(getCustomerStreetnumber());
		this.customerContact.getAddress().setZip(getCustomerZip());
		this.customerContact.getAddress().setCity(getCustomerTown());
		this.customerContact.getAddress().setCountry(getCustomerCountry());
		this.customerContact.getAddress().setActive(true);
		this.customerContact.getAddress().setEditDate(new Date());
		this.customerContact.getAddress().setEditUser(getSessionBean().getCurrentUser().getId());
		if (!isNew) {
			Address a = this.customerContact.getAddress();
			a.setEditDate(new Date());
			a.setEditUser(getSessionBean().getCurrentUser().getId());
			addressFacade.edit(a);
		}
	}

	public void setBuildingSiteAddress(Boolean isNew) {
		if (this.buildingSiteObject.getAddress() == null) {
			this.buildingSiteObject.setAddress(new Address());
			this.buildingSiteObject.getAddress().setCreateDate(new Date());
			this.buildingSiteObject.getAddress().setCreateUser(getSessionBean().getCurrentUser().getId());
		}

		this.buildingSiteObject.getAddress().setStreet(getBuildingSiteStreet());
		this.buildingSiteObject.getAddress().setStreetNumber(getBuildingSiteStreetNumber());
		this.buildingSiteObject.getAddress().setZip(getBuildingSiteZip());
		this.buildingSiteObject.getAddress().setCity(getBuildingSiteTown());
		this.buildingSiteObject.getAddress().setCountry(getBuildingCountry());
		this.buildingSiteObject.getAddress().setActive(true);
		this.buildingSiteObject.getAddress().setEditDate(new Date());
		this.buildingSiteObject.getAddress().setEditUser(getSessionBean().getCurrentUser().getId());
		if (!isNew) {
			Address a = this.buildingSiteObject.getAddress();
			a.setEditDate(new Date());
			a.setEditUser(getSessionBean().getCurrentUser().getId());
			addressFacade.edit(a);
		}
	}

	public void setElementHasHistory(List<ServiceElements> sList) {
		for (ServiceElements s : sList) {
			// s.setChecklistHistoryExists(serviceHistoryFacade.elementHasHistory(s,
			// this.selectedService)); // 40 Sekunden
			s.setChecklistHistoryExists(serviceHistoryFacade.elementHasHist(s.getId(), this.selectedService.getId())); // 14
																														// Sekunden

		}
	}

	public void setElementsAllreadyAssigned(Service s) {
		List<ServiceElements> se = s.getServiceElementList();
		Boolean showCreateElements = false;
		// erste Bedingung -> noch keien Elemente zugeordnet
		if (se == null || se.size() < 1) {
			// zweite Prüfung, gibt es Elemente innerhalb des Contracts
			if (serviceContractFacade.elementsExistsforServiceContractAndService(s.getServiceContract(),
					s.getServiceType())) {
				showCreateElements = false;
			} else {
				showCreateElements = true;
			}
		} else {
			showCreateElements = true;
		}
		this.elementsAssigned = showCreateElements;
	}
		

	/**
	 * ServiceTable - rowSelect - SelectItem
	 */
	public void updateElementList() {
		this.serviceElementList = new ArrayList<>();
		this.resetElementIds = new ArrayList<>();
		Service serv = null;
		// TODO: bei folgeintervall müssen erst element zugeornet werden bevor neue
		// erstellt werden können.

		try {
			if (this.selectedService != null) {
				serv = this.selectedService;
				setElementsAllreadyAssigned(serv);
			}
			if (serv != null && serv.getServiceElementList().size() > 0) {

				List<ServiceElements> sList = serv.getServiceElementList();
				setElementHasHistory(sList);
				// Sorting

				Collections.sort(sList, new Comparator<ServiceElements>() {
					@Override
					public int compare(ServiceElements s2, ServiceElements s1) {
						return s2.getElementnumber().compareTo(s1.getElementnumber());
					}
				});

				for (ServiceElements e : serv.getServiceElementList()) {
					// TODO: Wenn kein Hist-Checklistitem dann elementChecklist
					e.setServiceChecklist(e.getChecklist());
				}
				for(int c = 0; c < serv.getServiceElementList().size(); c++)
				{					
					ServiceElements element = serv.getServiceElementList().get(c);
					if(element.getChecklistHistoryExists())
					{												
						element.setChecklist(this.checklistFacade.getChecklistForService(element, this.selectedService.getId()));
					}
					this.serviceElementList.add(serv.getServiceElementList().get(c));					
				}
				//this.serviceElementList.addAll(serv.getServiceElementList());
			}
			this.generateFloorList();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Sets the floor list, containing all element prefixes of the selected service and their current largest number.
	 */
	private void generateFloorList() {
		// Generate floor list.
		this.floorList = new ArrayList<>();
		if (this.serviceElementList != null && !this.serviceElementList.isEmpty()) {
			for (ServiceElements element : this.serviceElementList) {
				final FloorEntry entry = new FloorEntry();
				// Fetch barcode prefix.
				Integer digitNumber = NUMERIC_POSITIONS;
				if(element.getElementnumber().length() > NO_FLOOR_NUMBER_LENGTH)
				{	
					// Barcodes with floor numbers have 1 less digit.
					digitNumber--;
				}
				entry.setFloor(element.getElementnumber().substring(0,
						element.getElementnumber().length() - digitNumber));
				// Remove non-digits with regex.
				try {
					final String fuString = element.getElementnumber().substring(element.getElementnumber().length() - digitNumber, element.getElementnumber().length());
					final Integer fu = Integer.parseInt(fuString);
					entry.setStartNumber(fu);
				} catch (Exception e) {
					e.printStackTrace();
					entry.setStartNumber(1);
				}
				// Check if floor is already in list.
				if (floorList.contains(entry)) {
					// Is in list, check if current number is greater than stored number.
					final FloorEntry oldEntry = floorList.get(floorList.indexOf(entry));
					if (oldEntry.getStartNumber() < entry.getStartNumber()) {
						// Is bigger, updating...
						oldEntry.setStartNumber(entry.getStartNumber());
					}
				} else {
					// Not in list, adding...
					floorList.add(entry);
				}
			}
		}
	}

	public String genBarcode(Integer id, String suffix, final Boolean fiveDigits) {
		// return String.format("*" + suffix + "%07d*", id);
		if(fiveDigits)
		{
			return String.format(suffix + "%05d", id);
		}
		else
		{
			return String.format(suffix + "%04d", id);
		}
	}

	public Integer getMaxNumberFromElementList(List<ServiceElements> sList) {
		Integer max = 0;
		Integer com = 0;
		String onlyNumber = null;
		for (ServiceElements el : sList) {
			if (el != null) {
				onlyNumber = el.getElementnumber().replaceAll("[^\\d.]", "");
				com = Integer.valueOf(onlyNumber);
			}

			if (com > max) {
				max = com;
			}
		}
		return max;
	}

	public void delteSelectedService() {
		if (serviceFacade.historyAllreadySynchronized(this.selectedService)) {
			FacesContext.getCurrentInstance().addMessage("Service wird nicht gelöscht!",
					new FacesMessage(FacesMessage.SEVERITY_WARN, " ",
							"Service wird nicht gelöscht, da bereits eine Synchronisierung eroflgt ist!"));
		} else {
			List<Service> newList = new ArrayList<>();
			newList.addAll(this.serviceList);
			newList.remove(this.selectedService);
			this.selectedService.setCustomer(null);
			this.selectedService.setPartner(null);
			this.selectedService.setServiceContract(null);
			this.selectedService.setServiceElementList(null);
			this.selectedService.setServiceType(null);
			this.selectedService.setTechnician(null);
			this.selectedService.setEditDate(new Date());
			this.selectedService.setEditUser(getSessionBean().getCurrentUser().getId());
			serviceFacade.edit(this.selectedService);
			serviceFacade.remove(this.selectedService);
			this.serviceList = new ArrayList<>();
			this.serviceList.addAll(newList);
		}
	}

	/**
	 * Elemente werden nur für die Services zugeordnet, bei denen noch Elemente
	 * Elemente zuordnen #{contractInfo.serviceList}
	 */
	public void assignElementsForSelectedIntervall() {
		// List<Service> sList =
		// serviceFacade.findServiceFromServiceContract(this.serviceContract);
		for (Service s : this.serviceList) {
			if (s.getServiceType().getId().equals(selectedService.getServiceType().getId())) {
				if (s.getId().equals(this.selectedService.getId())) {
					System.out.println("Quellservice");
				} else {
					// Servives haben beiden gleichen Typ
					// Es werden nur Elemente neu zugeordnet, wenn keine
					// Änderungen
					// in der Historie gemacht wurden.
					if (serviceFacade.historyAllreadySynchronized(s)) {
						String m = "Für bereits bearbetete Intervalle wurden die Elemente nicht erstellt, ";
						System.out.println(m);
						FacesContext.getCurrentInstance().addMessage("Keine Zurodnungen geändert!",
								new FacesMessage(FacesMessage.SEVERITY_WARN, " ", m));

					} else {
						// hier müssten die Elemente bereits drin sein.
						for (ServiceElements se : this.selectedService.getServiceElementList()) {

							AssocServiceElementsId t = new AssocServiceElementsId();
							t.setElements(se.getId());
							t.setService(s.getId());
							Assoc_Service_Elements as = new Assoc_Service_Elements();
							as.setId(t);
							assocServiceElementFacade.edit(as);
						}
						s.setServiceElementList(this.selectedService.getServiceElementList());
					}
					if (this.selectedService.getComment() != null && this.selectedService.getComment().length() > 0) {
						s.setComment(this.selectedService.getComment());
					}

				}
			}
		}
	}

	/**
	 * Funktion wird von der Maske Vertragsdetails aufgerufen. Der markierte Service
	 * wird gelöscht service löschen
	 */
	public void delteElementsForSelectedIntervall() {
		delteSelectedService();
	}

	public void generateElementsForInterval() {
		final List<ServiceElements> elementList = new ArrayList<>();
		final List<ServiceElements> elementsForAssoc = new ArrayList<>();
		final ServiceType st = this.selectedService.getServiceType();
		Integer generatedCount = 0;
		for (AddElements newElement : this.addElements) {
			// Not the footer line, generate element normally.
			if (!newElement.equals(this.footerLine) && newElement.getAmount() > 0) {
				// Check if there's already elements with the generated prefix.
				FloorEntry floor = new FloorEntry();
				Integer generateAmount = 0;
				
					// Is not footer line, prefix is name of floor + short name of service, amount
					// is entered value.
					floor.setFloor(newElement.getFloor().trim() + st.getShortname());
					generateAmount = newElement.getAmount();
				
				if (this.floorList.contains(floor)) {
					// Prefix already exists, fetch current number.
					floor = this.floorList.get(this.floorList.indexOf(floor));
				} else {
					// Prefix doesn't exist yet, set counter to 0 and add to floor list.
					floor.setStartNumber(0);
					this.floorList.add(floor);
				}
				// Generate elements.
				if (generateAmount > 0) {
					for (int c = 0; c < generateAmount; c++) {
						// Increment start number and generate element.
						floor.setStartNumber(floor.getStartNumber() + 1);
						ServiceElements e = new ServiceElements();
						e.setActive(true);
						e.setCreateDate(new Date());
						e.setEditDate(new Date());
						e.setCreateUser(getSessionBean().getCurrentUser().getId());
						e.setEditUser(getSessionBean().getCurrentUser().getId());
						if(newElement.getFloor() != null && !newElement.getFloor().isEmpty())
						{
							e.setElementnumber(genBarcode(floor.getStartNumber(), floor.getFloor().toUpperCase(), false));
						}
						else
						{
							e.setElementnumber(genBarcode(floor.getStartNumber(), floor.getFloor().toUpperCase(), true));
						}
						e.setChecklist(newElement.getChecklist());
						e.setFloor(newElement.getFloor());
						e.setFront("");
						e.setRoom("");
						e.setOrientation("");
						e.setName(st.getShortname());
						e.setElementType(null);
						elementList.add(e);
						elementsForAssoc.add(e);
						generatedCount++;
						// add elements detached
						serviceElementFacade.create(e);
					}
				}
			}
		}
		persistElementsListtoAssocElementService(elementsForAssoc);		
		// Anstatt service persist, persist elements
		FacesContext.getCurrentInstance().addMessage("Zuordnung erfolgreich",
				new FacesMessage(FacesMessage.SEVERITY_INFO, " ", "Die Elemente wurden dem Service zugeordnet"));
		
		this.serviceElementList.addAll(elementList);
		this.selectedService.getServiceElementList().addAll(elementList);
		this.initAddElements();
		this.generateFloorList();
	}

	/**
	 * elemente erstellen
	 */
	public void generateElementsForSelectedIntervall() {
		List<ServiceElements> elementsList = new ArrayList<>();
		List<ServiceElements> elementsForAssoc = new ArrayList<>();
		if (numberOfElements != null && numberOfElements > 0 && elementChecklist != null) {
			Integer addCount = 0;
			ServiceType st = this.selectedService.getServiceType();
			if (this.selectedService.getServiceElementList() != null
					&& this.selectedService.getServiceElementList().size() > 0) {
				addCount = getMaxNumberFromElementList(selectedService.getServiceElementList());
				// addCount = selectedService.getServiceElementList().size();
				this.serviceElementList = new ArrayList<>();
				elementsList.addAll(selectedService.getServiceElementList());
			}
			for (int i = 1; i <= numberOfElements; i++) {
				ServiceElements e = new ServiceElements();
				e.setActive(true);
				e.setCreateDate(new Date());
				e.setEditDate(new Date());
				e.setCreateUser(getSessionBean().getCurrentUser().getId());
				e.setEditUser(getSessionBean().getCurrentUser().getId());
				Integer erg = i + addCount;
				/*if(e.getFloor() != null && !e.getFloor().isEmpty())
				{
					e.setElementnumber(genBarcode(erg, st.getShortname(), false));
				}
				else
				{
					e.setElementnumber(genBarcode(erg, st.getShortname(), true));
				}*/
				e.setElementnumber(genBarcode(erg, st.getShortname(), true));
				e.setChecklist(this.elementChecklist);
				e.setFloor("");
				e.setFront("");
				e.setRoom("");
				e.setOrientation("");
				e.setName(st.getShortname());
				e.setElementType(null);
				elementsList.add(e);
				elementsForAssoc.add(e);
				// add elements detached
				serviceElementFacade.create(e);
			}
			persistElementsListtoAssocElementService(elementsForAssoc);
			this.serviceElementList.addAll(elementsList);
			this.selectedService.setServiceElementList(elementsList);
			// Anstatt service persist, persist elements
			FacesContext.getCurrentInstance().addMessage("Zuordnung erfolgreich",
					new FacesMessage(FacesMessage.SEVERITY_INFO, " ", "Die Elemente wurden dem Service zugeordnet"));
		} else {
			FacesContext.getCurrentInstance().addMessage("Zuordnung Fehlgeschlagen", new FacesMessage(
					FacesMessage.SEVERITY_ERROR, " ", "Anzahl der Elemente und Checkliste müssen ausgefüllt werden!"));

		}
	}

	/**
	 * 
	 * @param sc:
	 *            context ServiceContract
	 * @param eList:
	 *            List of ServiceElements
	 */
	public void persistElementsListtoAssocElementService(List<ServiceElements> eList) {
		for (ServiceElements se : eList) {
			AssocServiceElementsId t = new AssocServiceElementsId();
			t.setElements(se.getId());
			t.setService(this.selectedService.getId());
			Assoc_Service_Elements as = new Assoc_Service_Elements();
			as.setId(t);
			assocServiceElementFacade.create(as);
		}
	}

	/**
	 * Delete Assoc of Element
	 */
	public void deleteElementListforSelectedService(List<ServiceElements> selectedElementList) {
		for (ServiceElements se : selectedElementList) {
			AssocServiceElementsId t = new AssocServiceElementsId();
			t.setElements(se.getId());
			t.setService(this.selectedService.getId());
			Assoc_Service_Elements as = new Assoc_Service_Elements();
			as.setId(t);
			assocServiceElementFacade.remove(as);
			this.selectedService.getServiceElementList().remove(se);
		}
	}

	/**
	 * Delete Elemente with all Assocs
	 */
	public void deleteElementListforAllServices(List<ServiceElements> selectedElementList) {
		for (ServiceElements s : selectedElementList) {
			serviceElementFacade.remove(s);
		}
	}

	public Boolean serviceTypeActiveIntervallAllreadyExist(AddServiceType a, ServiceContract sc) {
		return serviceFacade.serviceTypeActiveIntervallAllreadyExist(a, sc);

	}

	public Integer getNextServiceIntervall(AddServiceType a, ServiceContract sc) {
		return serviceFacade.getNextServiceIntervall(a.getServiceType().getId(), sc.getId());
	}

	public void saveServices() {
		if (this.serviceStartdate.after(this.serviceEnddate)) {
			FacesContext.getCurrentInstance().addMessage("lpcDlgMessage",
					new FacesMessage(FacesMessage.SEVERITY_ERROR, " ", "Startdatum muss kleiner sein als Endedatum!"));
		} else {
			String sInt = "";
			Boolean firstRun = true;
			for (AddServiceType a : serviceTypeTable) {
				if (a.getServiceType() != null) {
					Logger.getLogger(this.getClass().getName()).log(Level.INFO,
							"Service-Type: " + a.getServiceType().getName());
				}
				Logger.getLogger(this.getClass().getName()).log(Level.INFO,
						"Service-Date: " + a.getLatestServiceDate());
				Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Service-Intervall: " + a.getIntervall());
				List<Service> tmpList = new ArrayList<>();
				for (int i = 0; i < a.getIntervall(); i++) {
					if (serviceList != null && serviceList.size() > 0) {
						tmpList.addAll(this.serviceList);
						this.serviceList = new ArrayList<>();
					}
					Service s = new Service();
					if (firstRun) {
						if (!serviceTypeActiveIntervallAllreadyExist(a, this.serviceContract)) {
							s.setActive(true);
						}

					} else {
						s.setActive(false);
					}

					s.setCreateDate(new Date());
					s.setEditDate(new Date());
					s.setEditUser(getSessionBean().getCurrentUser().getId());
					s.setCreateUser(getSessionBean().getCurrentUser().getId());

					s.setIntervall(getNextServiceIntervall(a, this.serviceContract));
					if (firstRun) {
						s.setLatestServiceDate(a.getLatestServiceDate());
					} else {
						s.setLatestServiceDate(null);
					}

					s.setServiceType(a.getServiceType());
					s.setDescription("");
					s.setTechnician(getTechnicalContact());
					s.setCustomer(getCustomerContact());
					s.setPartner(getCustomerContact());
					s.setStartdate(getServiceStartdate());
					s.setEnddate(getServiceEnddate());
					s.setServiceContract(this.serviceContract);
					this.serviceFacade.edit(s);
					sInt += s.getServiceType().getName() + ", Intervall: " + s.getIntervall() + " ";
					firstRun = false;
				}
				if (tmpList.size() > 0) {
					this.serviceList.addAll(tmpList);
				}
			}
			this.serviceList = new ArrayList<>();
			this.serviceList.addAll(serviceFacade.findServiceFromServiceContract(this.serviceContract));

			if (sInt.length() <= 0) {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "keine Neuzuordnung",
						"keine Neuzuordnung, da keine Intervalle über den Button '(+) Ser-Int. ' ausgewählt wurden "));
			} else {
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "neue Zuordnung",
						"Die Intervalle: " + sInt + " wurden dem Vertrag neu zugewiesen!"));
			}
		}
	}

	// public Boolean customerExist() {
	// // if(this.personFacade.findCustomersByName(this.customerLastname)){
	// //
	// // }
	// return null;
	// }

	public Boolean buildingSiteExists() {
		if (this.buildingSiteFacade.findByCode(getBuildingSiteCode()) != null) {
			return true;
		} else
			return false;
	}

	/**
	 * 
	 * @param elementList
	 * @return
	 */
	public String checkIfChecklistIsAlwaysSet(List<ServiceElements> elementList) {
		if (elementList == null || elementList.size() == 0) {
			return "Dem Service sind keine Elemente zugeordnet";
		}
		for (ServiceElements s : elementList) {
			if (s.getChecklist() == null) {
				return "Das Element: " + s.getElementnumber() + " hat keine zugeordnete Checkliste";
			}
		}

		return null;
	}

	public void onCellEditElement(CellEditEvent event) {

		// Object oldValue = (Object) event.getOldValue();
		if (event.getNewValue() != null
				&& event.getNewValue().getClass().getSimpleName().toLowerCase().contains("checklist")) {
			System.out.println("Checkliste geändert");
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Checkliste wurde geändert",
					"Damit die Checkliste neu zugeordnet wird muss der Button Checkliste für alle Intervalle erstellen betätigt werden "));

		} else {
			for (ServiceElements s : selectedServiceElements) {
				this.serviceElementFacade.edit(s);

			}

		}

	}

	/**
	 * Checkliste für ausgewählte Elemente erstellen
	 */
	public void updateServiceHistoryElements() {
		String listElements = "";
		Integer counter = 0;
		List<ServiceHistory> histList = serviceHistoryFacade.findHistForService(this.selectedService);
		// löschen der Elemente denen eine neue Checkliste zugeordnet wurde
		Boolean isAdmin = getSessionBean().getIsAdmin();
		// Wenn Admin, wird geprüft ob bereits Synchronisiert wurde. Ist das der
		// Fall wird diese
		// gelöscht (ausser Regiezeiten, Reparaturen)
		// Wenn kein Admin, dann wird die Historie nur neu erstellt, wenn bisher
		// nicht synchronisert wurde

		if (isAdmin) {
			serviceHistoryFacade.removeHistoryElements(histList, this.selectedServiceElements, true);
		} else {
			serviceHistoryFacade.removeHistoryElements(histList, this.selectedServiceElements, false);
		}

		// Erstellen der Elemente mit neuer Checklist
		for (ServiceElements element : this.selectedServiceElements) {
			counter++;
			if (counter == this.selectedServiceElements.size()) {
				listElements += element.getElementnumber() + " ";
			} else {
				listElements += element.getElementnumber() + ", ";
			}
			if(!this.resetElementIds.contains(element.getId()))
			{
				this.resetElementIds.add(element.getId());
			}

			// for (ChecklistItem ci : element.getChecklist().getChecklistItems()) {
			// element.setChecklistHistoryExists(true);
			// // checklistItemFacade.findByChecklist();
			// addServiceHistoryElement(ci, element, this.selectedService);
			// }
			// element.getChecklist().getAssocChecklistEntries();
			Checklist cl = checklistFacade.findDetached(element.getChecklist().getId());
			for (Assoc_Checklist_Checklistentries ci : cl.getAssocChecklistEntries()) {
				element.setChecklistHistoryExists(true);				
				// checklistItemFacade.findByChecklist();
				addServiceHistoryElement(ci, element, this.selectedService);
			}
		}
		if (listElements.length() > 150) {
			listElements = listElements.substring(0, 150) + "...";
		}
		System.out.println("Checklistenitems für die Elemente: " + listElements + " wurden neu zugeordnet");
		FacesContext context = FacesContext.getCurrentInstance();
		context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "neue Zuordnung",
				"Checklistenitems für die Elemente: " + listElements + " wurden neu zugeordnet"));
	}

	public void addServiceHistoryElement(Assoc_Checklist_Checklistentries ci, ServiceElements element,
			Service serivce) {
		ServiceHistory s = new ServiceHistory();
		s.setStartTime(null);
		s.setEndTime(null);
		s.setCheckingSeconds(0);
		s.setDescription(null);
		s.setVisualControl(false);
		s.setFunctionalControl(false);
		s.setCheckedAsOk(false);
		s.setCheckedAsDefect(false);
		s.setCheckedAsLack(false);
		s.setCheckedAsRepaired(false);
		s.setCreateOffer(false);
		s.setService(serivce);
		s.setCheckListItem(ci.getChecklistItem());
		Integer vf = 0;
		vf = (ci.getFc() && ci.getVc()) ? 0 : ci.getFc() ? 2 : 1;
		s.setVisualAndFuntionControl(vf);
		s.setElement(element);
		s.setSetupSeconds(0);
		s.setErrorHistoryFlag(false);
		s.setServiceChecklist(ci.getCheckList().getId());
		System.out.println("SETTING INDIVIDUAL CHECKLIST: " + ci.getCheckList().getName());
		serviceHistoryFacade.create(s);

	}

	// Checkliste für alle Intervalle erstellen
	public void generateHistEntriesAllServices() {
		for (Service s : this.serviceList) {
			generateHistEntries(s);
		}

	}

	public void generateHistEntriesSelectedServices() {
		generateHistEntries(this.selectedService);

	}

	/**
	 * Beim ersten Aufruf werden allen Elemententen innerhalb einens Services
	 * 
	 * 
	 * ServiceHistory GenerateServiceHistory Checkliste für Service erstellen
	 */
	public void generateHistEntries(Service s) {
		String msg = checkIfChecklistIsAlwaysSet(s.getServiceElementList());

		if (msg != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehlgeschlagen!", msg));
		} else {
			try {
				// Liste aller Elemente
				// if (serviceFacade.historyAllreadyGenerated(s)) {
				if (s.getId() == null) {
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Keine Zuordnung!",
							"Checklisteinträge müssen über die Tabelle der Elemente zugordnet werden, da die globale Zuordnung bereits erfolgt ist! "));
				} else {
					for (ServiceElements element : s.getServiceElementList()) {
						if (!serviceFacade.historyForElementAllreadyGenerated(s.getId(), element.getId())) {

							// Historie wird erstellt
							// Wenn bereits Historie besteht!
							element.getChecklist().getAssocChecklistEntries();
							Checklist cl = checklistFacade.findDetached(element.getChecklist().getId());
							if (cl.getAssocChecklistEntries() != null) {
								for (Assoc_Checklist_Checklistentries ci : cl.getAssocChecklistEntries()) {
									addServiceHistoryElement(ci, element, s);
								}
								element.setChecklistHistoryExists(true);
							}
							element.setState("ungewartet");
							element.setEditDate(new Date());
							element.setCreateUser(getSessionBean().getCurrentUser().getId());
							serviceElementFacade.edit(element);

						} else {
							FacesContext context = FacesContext.getCurrentInstance();
							context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Keine Zuordnung!",
									"Checklisteinträge müssen über die Tabelle der Elemente zugordnet werden, da die globale Zuordnung bereits erfolgt ist! "));
						}
					}
					FacesContext context = FacesContext.getCurrentInstance();
					context.addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolgreich!",
									"Checklistenitems wurden für die Serviceart " + s.getServiceType().getName()
											+ " Intervall: " + s.getIntervall() + " erstellt"));
				}

				// this.serviceContract.setFinalized(true);
				// this.selectedService.setChecklistItemsCreated(true);
				// serviceContractFacade.edit(this.serviceContract);
				// serviceFacade.edit(this.selectedService);

			} catch (Exception e) {
				System.err.println("Service, Elemente oder Checkliste nicht zugeordnet! ");
				FacesContext context = FacesContext.getCurrentInstance();
				context.addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehlgeschlagen!", e.getMessage()));
			}
		}
	}

	public void setServiceActive() {
		for (Service service : serviceList) {
			if (service.getServiceType().getId().equals(this.selectedService.getServiceType().getId())) {
				service.setActive(false);
				service.setEditDate(new Date());
				service.setEditUser(getSessionBean().getCurrentUser().getId());
				serviceFacade.edit(service);
			}
		}
		this.selectedService.setActive(true);
		this.selectedService.setEditDate(new Date());
		this.selectedService.setEditUser(getSessionBean().getCurrentUser().getId());
		serviceFacade.edit(this.selectedService);
	}

	public void saveBuildingSiteObjectWithReferences() {
		if (!bsSelected) {
			setBuildingSiteObjectIfNotSelected(false);
		}
		this.buildingSiteObject.setEditDate(new Date());
		this.buildingSiteObject.setEditUser(getSessionBean().getCurrentUser().getId());
		buildingSiteFacade.edit(this.buildingSiteObject);
	}

	public void showPara() {
		System.out.println("buildingSiteCode:  " + getBuildingSiteCode());
	}

	public void disableEditMode(ActionEvent actionEvent) {
		this.disableMode = true;
	}

	public void enableEditMode(ActionEvent actionEvent) {
		this.disableMode = false;
	}

	public void createServices(ActionEvent actionEvent) {
		this.disableMode = false;

	}

	public void ac_editBuildingSite() {
		this.object = AbstractFacade.OBJECT_BUILDING_SITE;
		this.disableEditModeBS = false;
		this.setButtonStatus(false, false, false);
		this.disableSaveBtnBS = true;
		this.countedContracts = getCountedContractForBuildingSite(this.buildingSiteObject);
		createNewObject(false);
	}

	public void ac_createNewBuildingSite() {
		this.object = AbstractFacade.OBJECT_BUILDING_SITE;
		this.buildingSiteObject = new BuildingSite();
		this.disableEditModeBS = false;
		this.setButtonStatus(false, false, false);
		this.disableSaveBtnBS = true;
		this.newBuildingSite = true;
		createNewObject(true);
	}

	public void ac_editCustomer() {
		this.object = AbstractFacade.OBJECT_CUSTOMER;
		this.disableEditModeCustomer = false;
		this.setButtonStatus(false, false, false);
		this.disableSaveBtnCustomer = true;
		createNewObject(false);
	}

	public void ac_createNewCustomer() {
		this.object = AbstractFacade.OBJECT_CUSTOMER;
		this.customerContact = new Person();
		this.disableEditModeCustomer = false;
		this.setButtonStatus(false, false, false);
		this.disableSaveBtnCustomer = true;
		createNewObject(true);
	}

	public void ac_editContract() {
		this.object = AbstractFacade.OBJECT_CONTRACT;
		this.disableEditModeContract = false;
		this.setButtonStatus(false, false, false);
		this.disableSaveBtnContract = true;
		this.editContract = true;
		createNewObject(false);

	}

	public void createNewContract() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('addSubContract').show();");
	}

	public void ac_createNewContract() {
		this.editContract = false;
		this.object = AbstractFacade.OBJECT_CONTRACT;
		this.serviceContract = new ServiceContract();
		this.serviceElementList = new ArrayList<>();
		this.disableEditModeContract = false;
		this.setButtonStatus(false, false, false);
		this.disableSaveBtnContract = true;
		this.disableContractNrAC = false;
		createNewObject(true);

	}

	public void ac_editTechnician() {
		this.newTechnician = false;
		this.object = AbstractFacade.OBJECT_TECHNICIAN;
		this.disableEditModeTechnician = false;
		this.setButtonStatus(false, false, false);
		this.disableSaveBtnTechnician = true;
		createNewObject(false);
	}

	public void ac_createNewTechnician() {
		this.newTechnician = true;
		this.object = AbstractFacade.OBJECT_TECHNICIAN;
		this.technicalContact = new Person();
		this.disableEditModeTechnician = false;
		this.setButtonStatus(false, false, false);
		this.disableSaveBtnTechnician = true;
		createNewObject(true);
	}

	/**
	 * 
	 * @param isNew
	 *            True creates new Object, false for editing object
	 */
	public void createNewObject(Boolean isNew) {
		if (this.object.equals(AbstractFacade.OBJECT_BUILDING_SITE)) {
			clearBuildingSiteForm(isNew);
			this.countedContracts = getCountedContractForBuildingSite(this.buildingSiteObject);
		} else if (this.object.equals(AbstractFacade.OBJECT_CUSTOMER)) {
			clearCustomerForm(isNew);
		} else if (this.object.equals(AbstractFacade.OBJECT_TECHNICIAN)) {
			clearTechnicianForm(isNew);
		} else if (this.object.equals(AbstractFacade.OBJECT_CONTRACT)) {
			// Service
			clearContractForm(isNew);
			this.disableEditModeContract = false;
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Bitte Services zuordnen!", "Bitte Services zuordnen!"));
		}
	}

	public void cancel_new_contract() {
		this.disableEditModeContract = true;
		this.setButtonStatus(true, true, false);
		this.disableContractNrAC = true;
	}

	public void ac_createNewContract_save() {
		if (serviceEnddate.before(serviceStartdate)) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Datum prüfen", "Vertragsende muß nach Vertragsbeginn sein!"));
		} else {

			if (this.serviceContract == null || this.serviceContract.getServiceContractNumber() == null
					|| buildingSiteFacade.checkIfUnique(this.serviceContract.getServiceContractNumber())) {
				setServiceContractIfNotSelected(true);
				serviceContractFacade.create(this.serviceContract);
			} else {
				System.out.println("Allready Exists, just update ServiceContract");
				setServiceContractIfNotSelected(false);
				this.serviceContract.setEditDate(new Date());
				this.serviceContract.setEditUser(getSessionBean().getCurrentUser().getId());
				serviceContractFacade.edit(this.serviceContract);
			}
			this.disableEditModeContract = true;
			this.setButtonStatus(true, true, false);
			this.disableContractNrAC = true;
		}
	}

	public void ac_createNewBuildingSite_save() {
		if (this.buildingSiteObject == null || this.buildingSiteObject.getCode() == null
				|| buildingSiteFacade.checkIfUnique(this.buildingSiteObject.getCode())) {
			setBuildingSiteObjectIfNotSelected(true);
			buildingSiteFacade.create(this.buildingSiteObject);
		} else {
			System.out.println("Allready Exists, just update BuildingSite");
			setBuildingSiteObjectIfNotSelected(false);
			this.buildingSiteObject.setEditDate(new Date());
			this.buildingSiteObject.setEditUser(getSessionBean().getCurrentUser().getId());
			buildingSiteFacade.edit(this.buildingSiteObject);
		}
		this.disableEditModeBS = true;
		this.newBuildingSite = false;
		this.setButtonStatus(true, true, false);
	}

	public void ac_createNewCustomer_save() {
		if (this.customerContact == null
				|| personFacade.checkUniqunessCustomer(customerContact.getFirstname(), customerContact.getLastname())) {
			setCustomerObjectIfNotSelected(true);
			personFacade.create(this.customerContact);
		} else {
			System.out.println("Allready Exists, just update Customer");
			setCustomerObjectIfNotSelected(false);
			this.customerContact.setEditDate(new Date());
			this.customerContact.setEditUser(getSessionBean().getCurrentUser().getId());
			if (this.serviceContract != null) {
				this.serviceContract.setPartner(this.customerContact);
				this.serviceContractFacade.edit(this.serviceContract);
				// Update customer for all Services
				// this.serviceContract.getServices().size();
				for (Service s : this.serviceList) {
					s.setCustomer(this.customerContact);
					serviceFacade.edit(s);
				}
			}
			personFacade.edit(this.customerContact);
		}
		this.disableEditModeCustomer = true;
		this.setButtonStatus(true, true, false);
	}

	public void ac_createNewTechnician_save() {
		if (technicalContact == null || personFacade.checkUniqunessTechnician(technicalContact.getFirstname(),
				technicalContact.getLastname())) {
			setTechnicianObjectIfNotSelected(true);
			personFacade.create(this.technicalContact);
		} else {
			System.out.println("Allready Exists, update Technician");
			setTechnicianObjectIfNotSelected(false);
			this.technicalContact.setEditDate(new Date());
			this.technicalContact.setEditUser(getSessionBean().getCurrentUser().getId());
			// edit only on existing serviceContract
			if (this.serviceContract != null) {
				this.serviceContract.setTechnician(this.technicalContact);
				this.serviceContractFacade.edit(this.serviceContract);
				// this.serviceContract.getServices().size();
				for (Service s : this.serviceList) {
					s.setTechnician(this.technicalContact);
					serviceFacade.edit(s);
				}
			}
			personFacade.edit(this.technicalContact);
		}
		this.disableEditModeTechnician = true;
		this.setButtonStatus(true, true, false);
	}

	public void clearContractForm(Boolean isNew) {
		if (isNew) {
			// Service werden zurück gesetzt
			this.serviceList = new ArrayList<>();
			this.selectedServiceElements = new ArrayList<>();
			this.serviceDesc = null;
			this.serviceName = null;
			this.serviceStartdate = null;
			this.serviceEnddate = null;
			this.serviceSumPerInterval = null;
		}
		this.disableEditModeContract = false;
	}

	public void clearTechnicianForm(Boolean isNew) {
		if (isNew) {
			this.techLastename = null;
			this.techFirstname = null;
			this.techShortname = null;
			this.techMailAdress = null;
			this.techPhoneNumber = null;
			this.techMobilNumber = null;
			this.technicalContact = new Person();
		}
		this.disableEditModeTechnician = false;
	}

	public void clearCustomerForm(Boolean isNew) {
		if (isNew) {
			this.customerLastname = null;
			this.customerFirstname = null;
			this.customerCompany = null;
			this.customerMailAdress = null;
			this.customerPhoneNumber = null;
			this.customerMobilNumber = null;
			this.customerStreet = null;
			this.customerStreetnumber = null;
			this.customerZip = null;
			this.customerTown = null;
			this.customerCountry = null;
			this.customerContact = new Person();
		}
		this.disableEditModeCustomer = false;
	}

	public void clearBuildingSiteForm(Boolean isNew) {
		if (isNew) {
			this.buildingCountry = null;
			this.buildingSiteCode = null;
			this.buildingSiteName = null;
			this.buildingSitePhoneNumber = null;
			this.buildingSiteStreet = null;
			this.buildingSiteStreetNumber = null;
			this.buildingSiteTown = null;
			this.buildingSiteZip = null;
			this.buildingSiteObject = new BuildingSite();
		}
		this.disableEditModeBS = false;
	}

	public void temp() {
		this.buildingSiteObject = new BuildingSite();
		this.buildingSiteObject.setCode(getBuildingSiteCode());
	}

	public Integer checkAutoCompleteNamesSet() {
		int count = 0;

		if (this.buildingSiteObject == null && this.buildingSiteCode.length() > 0) {
			this.buildingSiteObject = new BuildingSite();
			this.buildingSiteObject.setCode(getBuildingSiteCode());
			count++;
		}
		if (this.customerContact == null && this.customerLastname.length() > 0) {
			this.customerContact = new Person();
			this.customerContact.setLastname(this.customerLastname);
			count++;
		}
		if (this.technicalContact == null && this.techLastename.length() > 0) {
			this.technicalContact = new Person();
			this.technicalContact.setLastname(this.techLastename);
			count++;
		}

		return count;
	}

	public Boolean serviceContractEditable() {
		if (this.customerContact != null && this.customerContact.getLastname().length() > 0
				&& this.buildingSiteObject != null && this.buildingSiteObject.getCode().length() > 0
				&& this.technicalContact != null && this.technicalContact.getLastname().length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public void validateAutocomplete() {

		if (checkAutoCompleteNamesSet() >= 3 || serviceContractEditable()) {
			disableEditModeService = false;
			System.out.println("count:: " + disableEditModeService);
		}

	}

	public void validateContractInformations() {
		if (this.buildingSiteObject != null && this.buildingSiteObject.getCode() != null && this.customerContact != null
				&& this.customerContact.getFirstname() != null && this.customerContact.getLastname() != null
				&& this.technicalContact != null && this.technicalContact.getFirstname() != null
				&& this.technicalContact.getLastname() != null && this.serviceContract != null
				&& this.serviceContract.getServiceContractNumber() != null) {
			this.contractValid = true;
		} else {
			this.contractValid = false;

		}

	}

	public void addTypesToContract() {
		// erstellen
		saveServices();
		serviceTypeTable = new ArrayList<>();
	}

	public void addServiceTypeEntry() {
		AddServiceType st = new AddServiceType();
		st.setIntervall(getServiceIntervall());
		if (getGenServiceLatestServicedate() == null) {
			setGenServiceLatestServicedate(this.serviceStartdate);
		}
		st.setLatestServiceDate(getGenServiceLatestServicedate());
		st.setServiceType(serviceTypeFacade.findByName(genServiceTyp));
		st.setServiceContract(this.serviceContract);
		this.serviceTypeTable.add(st);
	}

	public void addNewInvoice() {
		try {
			Faces.redirect(
					"faces/origer/serveAgree/admin/invoices/invoice_edit.xhtml?faces-redirect=true&serviceContractID="
							+ this.serviceContract.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void showInvoicesForContract() {
		try {
			Faces.redirect(
					"faces/origer/serveAgree/admin/invoices/invoice_list.xhtml?faces-redirect=true&serviceContractID="
							+ this.serviceContract.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void addNewOffer() {
		try {
			Faces.redirect(
					"faces/origer/serveAgree/admin/offers/offers_edit.xhtml?faces-redirect=true&serviceContractID="
							+ this.serviceContract.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showOfferList() {
		try {
			Faces.redirect(
					"faces/origer/serveAgree/admin/offers/offers_list.xhtml?faces-redirect=true&serviceContractID="
							+ this.serviceContract.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteItem() {
		this.serviceTypeTable.remove(selectedServiceType);
	}

	public void assignTypeToAllSelectedElements() {
		for (ServiceElements e : this.selectedServiceElements) {
			e.setElementType(this.elementTypeForAll);
			serviceElementFacade.edit(e);
		}
	}

	public void assignFloorToAllSelectedElements() {
		for (ServiceElements e : this.selectedServiceElements) {
			e.setFloor(this.addFloor);
			serviceElementFacade.edit(e);
		}
	}

	public void assignRoomToAllSelectedElements() {
		for (ServiceElements e : this.selectedServiceElements) {
			e.setRoom(this.addRoom);
			serviceElementFacade.edit(e);
		}
	}

	public void assignCommentToAllSelectedElements() {
		for (ServiceElements e : this.selectedServiceElements) {
			e.setDescription(this.addComment);
			serviceElementFacade.edit(e);
		}
	}

	public void assignDirectionToAllSelectedElements() {
		for (ServiceElements e : this.selectedServiceElements) {
			e.setDirectionType(this.directionForAll);
			serviceElementFacade.edit(e);
		}
	}

	public void assignFrontToAllSelectedElements() {
		for (ServiceElements e : this.selectedServiceElements) {
			e.setFrontType(this.frontForAll);
			serviceElementFacade.edit(e);
		}
	}

	public void assignDINToAllSelectedElements() {
		for (ServiceElements e : this.selectedServiceElements) {
			e.setDinType(this.DINForAll);
			serviceElementFacade.edit(e);
		}
	}

	public void createServiceOKFunction() {
		System.out.println("date: " + getGenServiceLatestServicedate());
		System.out.println("type: " + this.genServiceTyp);
		if (this.genServiceTyp != null && !this.genServiceTyp.isEmpty()) {
			this.createServiceOK = true;
		} else {
			this.createServiceOK = false;
		}

	}

	public void onCellEdit(CellEditEvent event) {
		serviceFacade.edit(this.selectedService);
	}

	public Boolean doesUrlAllreadyExists(String url) {
		Boolean exists = false;
		for (FileArchive f : fileArchiveFacade.findfilesForBuildingSite(this.buildingSiteObject)) {
			if (f.getUrl().equals(url)) {
				exists = true;
			}
		}
		return exists;
	}

	public Boolean doesFileNameAllreadyExist(String fileName) {
		Boolean exists = false;
		for (FileArchive f : fileArchiveFacade.findfilesForBuildingSite(this.buildingSiteObject)) {
			if (f.getName().equals(fileName)) {
				exists = true;
			}
		}
		return exists;
	}

	public void addNewContractFilePlan(FileUploadEvent event) throws IOException {
		UploadedFile uploadedFile = event.getFile();
		String fileName = uploadedFile.getFileName();
		// String contentType = uploadedFile.getContentType();
		InputStream input = uploadedFile.getInputstream();
		String year = getApplicationBean().getYearFromDate(new Date());
		File dir = new File(getApplicationBean().getPathToFileArchiv() + "/" + year);
		getApplicationBean().createDirectory(dir);

		OutputStream output = new FileOutputStream(new File(dir.getPath(), fileName));
		try {
			IOUtils.copy(input, output);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}

		if (!doesFileNameAllreadyExist(fileName)) {
			FileArchive f = new FileArchive();
			f.setCreateDate(new Date());
			f.setEditDate(new Date());
			f.setBuildingSite(this.buildingSiteObject);
			f.setName(fileName);
			f.setDescription("Plan");
			f.setSignature(false);
			f.setSubfolder(year);
			f.setUrl(dir.getPath() + "/" + fileName);
			f.setSynchronizeAndroid(true);
			fileArchiveFacade.create(f);
		}
	}

	public void addNewContractFile(FileUploadEvent event) throws IOException {
		UploadedFile uploadedFile = event.getFile();
		String fileName = uploadedFile.getFileName();
		// String contentType = uploadedFile.getContentType();
		InputStream input = uploadedFile.getInputstream();
		String year = getApplicationBean().getYearFromDate(new Date());
		File dir = new File(getApplicationBean().getPathToFileArchiv() + "/" + year);
		getApplicationBean().createDirectory(dir);

		OutputStream output = new FileOutputStream(new File(dir.getPath(), fileName));
		try {
			IOUtils.copy(input, output);
		} finally {
			IOUtils.closeQuietly(input);
			IOUtils.closeQuietly(output);
		}
		if (!doesFileNameAllreadyExist(fileName)) {
			FileArchive f = new FileArchive();
			f.setCreateDate(new Date());
			f.setEditDate(new Date());
			f.setBuildingSite(this.buildingSiteObject);
			f.setName(fileName);
			f.setSignature(false);
			f.setDescription("Vertragsdatei");
			f.setSubfolder(year);
			f.setUrl(dir.getPath() + "/" + fileName);
			f.setSynchronizeAndroid(false);
			fileArchiveFacade.create(f);
		}
	}

	public void goToBuildingSiteFiles() {
		try {
			Faces.redirect(
					"faces/origer/serveAgree/customer/serviceFiles_list.xhtml?faces-redirect=true&buildingSiteId="
							+ this.buildingSiteObject.getId());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static Calendar getCalendar(Date date) {
		Calendar cal = Calendar.getInstance(Locale.GERMANY);
		cal.setTime(date);
		return cal;
	}
	
	public String lastRow(Integer index) {
		String styleClass = "";
		
		if (addElements != null) {
			if (index + 1 == addElements.size()) {
				styleClass = "lastRow";
			}
			if (index + 1 == addElements.size() - 1) {
				styleClass = "prelastRow";
			}			
		}
		

		return styleClass;
		
	}

	public static void main(String args[]) {
		Date from = new Date();
		Date to = new Date();
		from = ApplicationBean.setDateToBeginOfYear(from);
		to = ApplicationBean.addYears(from, 1);
		to = ApplicationBean.addMonths(to, 1);
		System.out.println(yearBetween(from, to).toString());

	}

	/**
	 * 
	 * @param first
	 * @param last
	 * @return returns 1 for 1-365,35 days
	 */
	public static Integer yearBetween(Date first, Date last) {

		first = ApplicationBean.setDateToBeginOfDay(first);
		last = ApplicationBean.setDateToEndOfDay(last);

		Calendar present = Calendar.getInstance();
		Calendar past = Calendar.getInstance();
		past.setTime(first);
		present.setTime(last);

		int days = 0;

		System.out.println("from: " + past.getTime());
		System.out.println("to : " + present.getTime());
		while (past.before(present)) {
			past.add(Calendar.DATE, 1);
			// past.add(Calendar.HOUR, -1);
			if (past.before(present)) {
				days++;
			}
		}
		Float oneYear = 365.25F;
		Float year = (float) days / oneYear;
		Float mod = days % oneYear;
		if (mod > 1) {
			year += 1;
		}
		return year.intValue();
	}

	public void generateContractNumber() {

		// if (this.serviceEnddate != null && this.serviceStartdate != null &&
		// !this.editContract
		// && this.genServiceTypContract != null &&
		// this.genServiceTypContract.length() > 0)
		// {
		//
		// Integer years = yearBetween(this.serviceStartdate,
		// this.serviceEnddate);
		// String yy = ApplicationBean.getDateYearYY(this.serviceEnddate);
		// String mm = ApplicationBean.getDateMonth(this.serviceEnddate);
		// String stName =
		// serviceTypeFacade.findByName(this.genServiceTypContract).getShortname();
		// String nextCNr =
		// serviceContractFacade.findNextContractNumber(stName);
		// this.serviceName = yy + mm + "/" + years.toString() + stName + "/" +
		// nextCNr;
		// setServiceName(this.serviceName);
		// }
	}

	public void generateContractNumberForNewContract(final ServiceContract contract) {

		if (contract.getTodate() != null && contract.getFromdate() != null && contract.getServiceType() != null
				&& !contract.getServiceType().getShortname().isEmpty()) {

			Integer years = yearBetween(contract.getFromdate(), contract.getTodate());
			String yy = ApplicationBean.getDateYearYY(contract.getTodate());
			String mm = ApplicationBean.getDateMonth(contract.getTodate());
			String stName = contract.getServiceType().getShortname();
			String nextCNr = serviceContractFacade.findNextContractNumber(stName);
			contract.setServiceContractNumber(yy + mm + "/" + years.toString() + stName + "/" + nextCNr);

		}
	}

	public Boolean elementsSelected() {
		return !(this.selectedServiceElements != null && this.selectedServiceElements.size() > 0);
	}

	/*
	 * ******************** GETTER / SETTER **************************************
	 */

	public Person getCustomerContact() {
		return customerContact;
	}

	public void setCustomerContact(Person customerContact) {
		this.customerContact = customerContact;
	}

	public Person getTechnicalContact() {
		return technicalContact;
	}

	public void setTechnicalContact(Person technicalContact) {
		this.technicalContact = technicalContact;
	}

	public ServiceContract getServiceContract() {
		return serviceContract;
	}

	public void setServiceContract(ServiceContract serviceContract) {
		this.serviceContract = serviceContract;
	}

	public List<Service> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<Service> serviceList) {
		this.serviceList = serviceList;
	}

	public List<Invoice> getInvoiceList() {
		return invoiceList;
	}

	public void setInvoiceList(List<Invoice> invoiceList) {
		this.invoiceList = invoiceList;
	}

	public List<Offers> getOfferList() {
		return offerList;
	}

	public void setOfferList(List<Offers> offerList) {
		this.offerList = offerList;
	}

	public String getBuildingSiteCode() {
		return buildingSiteCode;
	}

	public void setBuildingSiteCode(String buildingSiteCode) {
		this.buildingSiteCode = buildingSiteCode;
	}

	public String getBuildingSiteStreet() {
		return buildingSiteStreet;
	}

	public void setBuildingSiteStreet(String buildingSiteStreet) {
		this.buildingSiteStreet = buildingSiteStreet;
	}

	public String getBuildingSiteStreetNumber() {
		return buildingSiteStreetNumber;
	}

	public void setBuildingSiteStreetNumber(String buildingSiteStreetNumber) {
		this.buildingSiteStreetNumber = buildingSiteStreetNumber;
	}

	public String getBuildingSiteZip() {
		return buildingSiteZip;
	}

	public void setBuildingSiteZip(String buildingSiteZip) {
		this.buildingSiteZip = buildingSiteZip;
	}

	public String getBuildingSiteTown() {
		return buildingSiteTown;
	}

	public void setBuildingSiteTown(String buildingSiteTown) {
		this.buildingSiteTown = buildingSiteTown;
	}

	public String getBuildingSitePhoneNumber() {
		return buildingSitePhoneNumber;
	}

	public void setBuildingSitePhoneNumber(String buildingSitePhoneNumber) {
		this.buildingSitePhoneNumber = buildingSitePhoneNumber;
	}

	public String getCustomerFirstname() {
		return customerFirstname;
	}

	public void setCustomerFirstname(String customerFirstname) {
		this.customerFirstname = customerFirstname;
	}

	public String getCustomerLastname() {
		return customerLastname;
	}

	public void setCustomerLastname(String customerLastname) {
		this.customerLastname = customerLastname;
	}

	public String getCustomerTitel() {
		return customerTitel;
	}

	public void setCustomerTitel(String customerTitel) {
		this.customerTitel = customerTitel;
	}

	public String getCustomerCompany() {
		return customerCompany;
	}

	public void setCustomerCompany(String customerCompany) {
		this.customerCompany = customerCompany;
	}

	public String getCustomerMailAdress() {
		return customerMailAdress;
	}

	public void setCustomerMailAdress(String customerMailAdress) {
		this.customerMailAdress = customerMailAdress;
	}

	public String getCustomerPhoneNumber() {
		return customerPhoneNumber;
	}

	public void setCustomerPhoneNumber(String customerPhoneNumber) {
		this.customerPhoneNumber = customerPhoneNumber;
	}

	public String getCustomerMobilNumber() {
		return customerMobilNumber;
	}

	public void setCustomerMobilNumber(String customerMobilNumber) {
		this.customerMobilNumber = customerMobilNumber;
	}

	public String getTechFirstname() {
		return techFirstname;
	}

	public void setTechFirstname(String techFirstname) {
		this.techFirstname = techFirstname;
	}

	public String getTechLastename() {
		return techLastename;
	}

	public void setTechLastename(String techLastename) {
		this.techLastename = techLastename;
	}

	public String getTechMailAdress() {
		return techMailAdress;
	}

	public void setTechMailAdress(String techMailAdress) {
		this.techMailAdress = techMailAdress;
	}

	public String getTechPhoneNumber() {
		return techPhoneNumber;
	}

	public void setTechPhoneNumber(String techPhoneNumber) {
		this.techPhoneNumber = techPhoneNumber;
	}

	public String getTechMobilNumber() {
		return techMobilNumber;
	}

	public void setTechMobilNumber(String techMobilNumber) {
		this.techMobilNumber = techMobilNumber;
	}

	public Date getServiceStartdate() {
		return serviceStartdate;
	}

	public void setServiceStartdate(Date serviceStartdate) {
		this.serviceStartdate = serviceStartdate;
	}

	public Date getServiceEnddate() {
		return serviceEnddate;
	}

	public void setServiceEnddate(Date serviceEnddate) {
		this.serviceEnddate = serviceEnddate;
	}

	public Integer getServiceIntervall() {
		return serviceIntervall;
	}

	public void setServiceIntervall(Integer serviceIntervall) {
		this.serviceIntervall = serviceIntervall;
	}

	public BigDecimal getServiceSumPerInterval() {
		return serviceSumPerInterval;
	}

	public void setServiceSumPerInterval(BigDecimal serviceSumPerInterval) {
		this.serviceSumPerInterval = serviceSumPerInterval;
	}

	public Date getGenServiceLatestServicedate() {
		return genServiceLatestServicedate;
	}

	public void setGenServiceLatestServicedate(Date genServiceLatestServicedate) {
		this.genServiceLatestServicedate = genServiceLatestServicedate;
	}

	public Integer getGenServiceActualTimeSpend() {
		return genServiceActualTimeSpend;
	}

	public void setGenServiceActualTimeSpend(Integer genServiceActualTimeSpend) {
		this.genServiceActualTimeSpend = genServiceActualTimeSpend;
	}

	public Integer getGenServiceTargetTimeSpend() {
		return genServiceTargetTimeSpend;
	}

	public void setGenServiceTargetTimeSpend(Integer genServiceTargetTimeSpend) {
		this.genServiceTargetTimeSpend = genServiceTargetTimeSpend;
	}

	public String getGenServiceInvoice() {
		return genServiceInvoice;
	}

	public void setGenServiceInvoice(String genServiceInvoice) {
		this.genServiceInvoice = genServiceInvoice;
	}

	public String getGenServiceOffer() {
		return genServiceOffer;
	}

	public void setGenServiceOffer(String genServiceOffer) {
		this.genServiceOffer = genServiceOffer;
	}

	public String getGenServiceTyp() {
		return genServiceTyp;
	}

	public void setGenServiceTyp(String genServiceTyp) {
		this.genServiceTyp = genServiceTyp;
	}

	public Date getFirstIntervallDate() {
		return firstIntervallDate;
	}

	public void setFirstIntervallDate(Date firstIntervallDate) {
		this.firstIntervallDate = firstIntervallDate;
	}

	public Country getBuildingCountry() {
		return buildingCountry;
	}

	public void setBuildingCountry(Country buildingCountry) {
		this.buildingCountry = buildingCountry;
	}

	public void setBuildingSiteObject(BuildingSite buildingSiteObject) {
		this.buildingSiteObject = buildingSiteObject;
	}

	public ServiceType getServiceTypeObject() {
		return serviceTypeObject;
	}

	public void setServiceTypeObject(ServiceType serviceTypeObject) {
		this.serviceTypeObject = serviceTypeObject;
	}

	public Map<String, String> getServiceTypeList() {
		return serviceTypeList;
	}

	public void setServiceTypeList(Map<String, String> serviceTypeList) {
		this.serviceTypeList = serviceTypeList;
	}

	public BuildingSite getBuildingSiteObject() {
		return buildingSiteObject;
	}

	public Boolean getDisableMode() {
		return disableMode;
	}

	public void setDisableMode(Boolean disableMode) {
		this.disableMode = disableMode;
	}

	public Boolean getTransactionValid() {
		return transactionValid;
	}

	public void setTransactionValid(Boolean transactionValid) {
		this.transactionValid = transactionValid;
	}

	public String getBuildingSiteName() {
		return buildingSiteName;
	}

	public void setBuildingSiteName(String buildingSiteName) {
		this.buildingSiteName = buildingSiteName;
	}

	public Boolean getDisableEditModeBS() {
		return disableEditModeBS;
	}

	public void setDisableEditModeBS(Boolean disableEditModeBS) {
		this.disableEditModeBS = disableEditModeBS;
	}

	public Boolean getDisableEditModeCustomer() {
		return disableEditModeCustomer;
	}

	public void setDisableEditModeCustomer(Boolean disableEditModeCustomer) {
		this.disableEditModeCustomer = disableEditModeCustomer;
	}

	public Boolean getDisableEditModeTechnician() {
		return disableEditModeTechnician;
	}

	public void setDisableEditModeTechnician(Boolean disableEditModeTechnician) {
		this.disableEditModeTechnician = disableEditModeTechnician;
	}

	public Boolean getDisableEditModeService() {
		return disableEditModeService;
	}

	public void setDisableEditModeService(Boolean disableEditModeService) {
		this.disableEditModeService = disableEditModeService;
	}

	public Boolean getNewBSAdded() {
		return newBSAdded;
	}

	public void setNewBSAdded(Boolean newBSAdded) {
		this.newBSAdded = newBSAdded;
	}

	public Boolean getNewCustAdded() {
		return newCustAdded;
	}

	public void setNewCustAdded(Boolean newCustAdded) {
		this.newCustAdded = newCustAdded;
	}

	public Boolean getNewTechAdded() {
		return newTechAdded;
	}

	public void setNewTechAdded(Boolean newTechAdded) {
		this.newTechAdded = newTechAdded;
	}

	public Boolean getBsSelected() {
		return bsSelected;
	}

	public void setBsSelected(Boolean bsSelected) {
		this.bsSelected = bsSelected;
	}

	public Boolean getCusSelected() {
		return cusSelected;
	}

	public void setCusSelected(Boolean cusSelected) {
		this.cusSelected = cusSelected;
	}

	public Boolean getTechSelected() {
		return techSelected;
	}

	public void setTechSelected(Boolean techSelected) {
		this.techSelected = techSelected;
	}

	public List<AddServiceType> getServiceTypeTable() {
		return serviceTypeTable;
	}

	public void setServiceTypeTable(List<AddServiceType> serviceTypeTable) {
		this.serviceTypeTable = serviceTypeTable;
	}

	public AddServiceType getSelectedServiceType() {
		return selectedServiceType;
	}

	public void setSelectedServiceType(AddServiceType selectedServiceType) {
		this.selectedServiceType = selectedServiceType;
	}

	public Service getSelectedService() {
		return selectedService;
	}

	public void setSelectedService(Service selectedService) {
		this.selectedService = selectedService;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	public Boolean getDisableEditModeContract() {
		return disableEditModeContract;
	}

	public void setDisableEditModeContract(Boolean disableEditModeContract) {
		this.disableEditModeContract = disableEditModeContract;
	}

	public Integer getNumberOfElements() {
		return numberOfElements;
	}

	public void setNumberOfElements(Integer numberOfElements) {
		this.numberOfElements = numberOfElements;
	}

	public Checklist getElementChecklist() {
		return elementChecklist;
	}

	public void setElementChecklist(Checklist elementChecklist) {
		this.elementChecklist = elementChecklist;
	}

	public List<ServiceElements> getServiceElementList() {
		return serviceElementList;
	}

	public void setServiceElementList(List<ServiceElements> serviceElementList) {
		this.serviceElementList = serviceElementList;
	}

	public ServiceElements getSelectedServiceElement() {
		return selectedServiceElement;
	}

	public void setSelectedServiceElement(ServiceElements selectedServiceElement) {
		this.selectedServiceElement = selectedServiceElement;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public Boolean getContractValid() {
		return contractValid;
	}

	public void setContractValid(Boolean contractValid) {
		this.contractValid = contractValid;
	}

	public List<ServiceElements> getSelectedServiceElements() {
		return selectedServiceElements;
	}

	public void setSelectedServiceElements(List<ServiceElements> selectedServiceElements) {
		this.selectedServiceElements = selectedServiceElements;
	}

	public List<Person> getCustlist() {
		return custlist;
	}

	public void setCustlist(List<Person> custlist) {
		this.custlist = custlist;
	}

	public List<Person> getTechlist() {
		return techlist;
	}

	public void setTechlist(List<Person> techlist) {
		this.techlist = techlist;
	}

	public List<Service> getFilteredServiceList() {
		return filteredServiceList;
	}

	public void setFilteredServiceList(List<Service> filteredServiceList) {
		this.filteredServiceList = filteredServiceList;
	}

	public List<ElementType> getElementTypeList() {
		return elementTypeList;
	}

	public void setElementTypeList(List<ElementType> elementTypeList) {
		this.elementTypeList = elementTypeList;
	}

	public List<Front> getFrontList() {
		return frontList;
	}

	public void setFrontList(List<Front> frontList) {
		this.frontList = frontList;
	}

	public List<Direction> getDirectionList() {
		return directionList;
	}

	public void setDirectionList(List<Direction> directionList) {
		this.directionList = directionList;
	}

	public ElementType getElementTypeForAll() {
		return elementTypeForAll;
	}

	public void setElementTypeForAll(ElementType elementTypeForAll) {
		this.elementTypeForAll = elementTypeForAll;
	}

	public Direction getDirectionForAll() {
		return directionForAll;
	}

	public void setDirectionForAll(Direction directionForAll) {
		this.directionForAll = directionForAll;
	}

	public Front getFrontForAll() {
		return frontForAll;
	}

	public void setFrontForAll(Front frontForAll) {
		this.frontForAll = frontForAll;
	}

	public Integer getCountContractsBuildingSite() {
		return countContractsBuildingSite;
	}

	public void setCountContractsBuildingSite(Integer countContractsBuildingSite) {
		this.countContractsBuildingSite = countContractsBuildingSite;
	}

	public Boolean getCreateServiceOK() {
		return createServiceOK;
	}

	public void setCreateServiceOK(Boolean createServiceOK) {
		this.createServiceOK = createServiceOK;
	}

	public String getCountedContracts() {
		return countedContracts;
	}

	public void setCountedContracts(String countedContracts) {
		this.countedContracts = countedContracts;
	}

	public Boolean getNewBuildingSite() {
		return newBuildingSite;
	}

	public void setNewBuildingSite(Boolean newBuildingSite) {
		this.newBuildingSite = newBuildingSite;
	}

	public Boolean getDisableContractNrAC() {
		return disableContractNrAC;
	}

	public void setDisableContractNrAC(Boolean disableContractNrAC) {
		this.disableContractNrAC = disableContractNrAC;
	}

	// public String getGenServiceTypContract() {
	// return genServiceTypContract;
	// }
	//
	// public void setGenServiceTypContract(String genServiceTypContract) {
	// this.genServiceTypContract = genServiceTypContract;
	// }

	public String getTechShortname() {
		return techShortname;
	}

	public void setTechShortname(String techShortname) {
		this.techShortname = techShortname;
	}

	public String getRenewDescription() {
		return renewDescription;
	}

	public void setRenewDescription(String renewDescription) {
		this.renewDescription = renewDescription;
	}

	public Date getRenewStartDate() {
		return renewStartDate;
	}

	public void setRenewStartDate(Date renewStartDate) {
		this.renewStartDate = renewStartDate;
	}

	public Date getRenewEndDate() {
		return renewEndDate;
	}

	public void setRenewEndDate(Date renewEndDate) {
		this.renewEndDate = renewEndDate;
	}

	public BigDecimal getRenewValue() {
		return renewValue;
	}

	public void setRenewValue(BigDecimal renewValue) {
		this.renewValue = renewValue;
	}

	public Integer getRenewNumberIntervals() {
		return renewNumberIntervals;
	}

	public void setRenewNumberIntervals(Integer renewNumberIntervals) {
		this.renewNumberIntervals = renewNumberIntervals;
	}

	public Boolean getDisableNewBtnBS() {
		return disableNewBtnBS;
	}

	public void setDisableNewBtnBS(Boolean disableNewBtnBS) {
		this.disableNewBtnBS = disableNewBtnBS;
	}

	public Boolean getDisableNewBtnCustomer() {
		return disableNewBtnCustomer;
	}

	public void setDisableNewBtnCustomer(Boolean disableNewBtnCustomer) {
		this.disableNewBtnCustomer = disableNewBtnCustomer;
	}

	public Boolean getDisableNewBtnTechnician() {
		return disableNewBtnTechnician;
	}

	public void setDisableNewBtnTechnician(Boolean disableNewBtnTechnician) {
		this.disableNewBtnTechnician = disableNewBtnTechnician;
	}

	public Boolean getDisableNewBtnService() {
		return disableNewBtnService;
	}

	public void setDisableNewBtnService(Boolean disableNewBtnService) {
		this.disableNewBtnService = disableNewBtnService;
	}

	public Boolean getDisableNewBtnContract() {
		return disableNewBtnContract;
	}

	public void setDisableNewBtnContract(Boolean disableNewBtnContract) {
		this.disableNewBtnContract = disableNewBtnContract;
	}

	public Boolean getDisableEditBtnBS() {
		return disableEditBtnBS;
	}

	public void setDisableEditBtnBS(Boolean disableEditBtnBS) {
		this.disableEditBtnBS = disableEditBtnBS;
	}

	public Boolean getDisableEditBtnCustomer() {
		return disableEditBtnCustomer;
	}

	public void setDisableEditBtnCustomer(Boolean disableEditBtnCustomer) {
		this.disableEditBtnCustomer = disableEditBtnCustomer;
	}

	public Boolean getDisableEditBtnTechnician() {
		return disableEditBtnTechnician;
	}

	public void setDisableEditBtnTechnician(Boolean disableEditBtnTechnician) {
		this.disableEditBtnTechnician = disableEditBtnTechnician;
	}

	public Boolean getDisableEditBtnService() {
		return disableEditBtnService;
	}

	public void setDisableEditBtnService(Boolean disableEditBtnService) {
		this.disableEditBtnService = disableEditBtnService;
	}

	public Boolean getDisableEditBtnContract() {
		return disableEditBtnContract;
	}

	public void setDisableEditBtnContract(Boolean disableEditBtnContract) {
		this.disableEditBtnContract = disableEditBtnContract;
	}

	public Boolean getDisableSaveBtnBS() {
		return disableSaveBtnBS;
	}

	public void setDisableSaveBtnBS(Boolean disableSaveBtnBS) {
		this.disableSaveBtnBS = disableSaveBtnBS;
	}

	public Boolean getDisableSaveBtnCustomer() {
		return disableSaveBtnCustomer;
	}

	public void setDisableSaveBtnCustomer(Boolean disableSaveBtnCustomer) {
		this.disableSaveBtnCustomer = disableSaveBtnCustomer;
	}

	public Boolean getDisableSaveBtnTechnician() {
		return disableSaveBtnTechnician;
	}

	public void setDisableSaveBtnTechnician(Boolean disableSaveBtnTechnician) {
		this.disableSaveBtnTechnician = disableSaveBtnTechnician;
	}

	public Boolean getDisableSaveBtnService() {
		return disableSaveBtnService;
	}

	public void setDisableSaveBtnService(Boolean disableSaveBtnService) {
		this.disableSaveBtnService = disableSaveBtnService;
	}

	public Boolean getDisableSaveBtnContract() {
		return disableSaveBtnContract;
	}

	public void setDisableSaveBtnContract(Boolean disableSaveBtnContract) {
		this.disableSaveBtnContract = disableSaveBtnContract;
	}

	/**
	 * Renews the current contract, by creating a new contract that's linked to the
	 * currently selected contract.
	 * 
	 */
	public void renewContract() {
		// Generate a new contract
		try {
			final ServiceContract newContract = new ServiceContract();
			newContract.setPreviousContract(this.serviceContract);
			newContract.setDescription(this.renewDescription);
			newContract.setCreateDate(new Date());
			newContract.setActive(true);
			newContract.setBuildingSite(this.serviceContract.getBuildingSite());
			newContract.setCreateUser(this.getSessionBean().getCurrentUser().getId());
			newContract.setEditDate(new Date());
			newContract.setEditUser(this.getSessionBean().getCurrentUser().getId());
			newContract.setFromdate(this.renewStartDate);
			newContract.setTodate(this.renewEndDate);
			newContract.setPartner(this.serviceContract.getPartner());
			newContract.setServicesAmount(this.renewValue);
			newContract.setServiceType(this.serviceContract.getServiceType());
			newContract.setContractType(this.serviceContract.getContractType());
			newContract.setTechnician(this.serviceContract.getTechnician());
			// this.generateContractNumberForNewContract(newContract);
			newContract.setServiceContractNumber(this.serviceName);
			if (newContract.getFromdate().before(newContract.getTodate())) {
				if (newContract.getServiceContractNumber().equals(this.serviceContract.getServiceContractNumber())) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Vertragsnummer prüfen", "Vertragsnummer muss eindeutig sein!"));
				} else {

					this.ac_reNewContract(newContract);
					Faces.redirect("faces/origer/serveAgree/contract/contract_edit.xhtml?faces-redirect=true&id="
							+ newContract.getId());
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Datum prüfen", "Vertragsende muß nach Vertragsbeginn sein!"));

			}

		} catch (Exception e) {
			try {
				Faces.redirect("faces/origer/serveAgree/contract/contract_edit.xhtml?faces-redirect=true&id="
						+ this.serviceContract.getId());
			} catch (Exception f) {
				f.printStackTrace();
			}
		}
	}

	public void onCellEditAddElements(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
		System.out.println("old-Value: " + oldValue);
		System.out.println("new-Value: " + newValue);

		if (newValue != null) {
			if (oldValue != null) {
				if (!newValue.equals(oldValue)) {
					// Example for update form
					RequestContext.getCurrentInstance().update("billForm:delivernote");
				}
			} else {
				// Example for update form
				RequestContext.getCurrentInstance().update("billForm:delivernote");
			}

		}
	}

	/**
	 * Saves the new contract into the DB, generates the entered number of intervals
	 * and attaches the previous contract's elements
	 * 
	 * @param contract
	 */
	public void ac_reNewContract(final ServiceContract contract) {
		if (contract.getTodate().before(contract.getFromdate())) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Datum prüfen", "Vertragsende muß nach Vertragsbeginn sein!"));
		} else {
			// Save contract
			serviceContractFacade.create(contract);
			contract.setId(this.serviceContractFacade.getLatestId());
			// Fetch services from previous contract for data
			final List<Service> firstIntervallServices = this.serviceFacade.findFirstServicesFromServiceContract(this.serviceContract);
			if (firstIntervallServices != null && !firstIntervallServices.isEmpty()) {
				for(Service previousService : firstIntervallServices)
				{	
					for (int c = 0; c < this.getRenewNumberIntervals(); c++) {
						// Generate interval
						final Service newInterval = new Service();
						newInterval.setIntervall(c + 1);
						// Set the first interval as active, the others inactive
						if (newInterval.getIntervall() == 1) {
							newInterval.setActive(true);
						} else {
							newInterval.setActive(false);
						}
						newInterval.setCreateDate(new Date());
						newInterval.setCreateUser(this.getSessionBean().getCurrentUser().getId());
						newInterval.setCustomer(previousService.getCustomer());
						newInterval.setPartner(previousService.getPartner());
						newInterval.setServiceContract(contract);
						newInterval.setServiceType(previousService.getServiceType());
						newInterval.setTechnician(previousService.getTechnician());
						newInterval.setActualAmount(previousService.getActualAmount());
						newInterval.setShortname(previousService.getShortname());
						newInterval.setTotalElements(previousService.getTotalElements());
						serviceFacade.create(newInterval);
						newInterval.setId(serviceFacade.getLatestId());
						// Link elements to new interval
						for (ServiceElements element : previousService.getServiceElementList()) {
							AssocServiceElementsId id = new AssocServiceElementsId();
							id.setElements(element.getId());
							id.setService(newInterval.getId());
							final Assoc_Service_Elements assoc = new Assoc_Service_Elements();
							assoc.setId(id);
	
							assocServiceElementFacade.create(assoc);
						}
					}
				}
			}

			this.disableEditModeContract = true;
			this.disableContractNrAC = true;
		}
	}

	public String getCustomerStreet() {
		return customerStreet;
	}

	public void setCustomerStreet(String customerStreet) {
		this.customerStreet = customerStreet;
	}

	public String getCustomerZip() {
		return customerZip;
	}

	public void setCustomerZip(String customerZip) {
		this.customerZip = customerZip;
	}

	public Country getCustomerCountry() {
		return customerCountry;
	}

	public void setCustomerCountry(Country customerCountry) {
		this.customerCountry = customerCountry;
	}

	public String getCustomerStreetnumber() {
		return customerStreetnumber;
	}

	public void setCustomerStreetnumber(String customerStreetnumber) {
		this.customerStreetnumber = customerStreetnumber;
	}

	public Boolean getNewCustomer() {
		return newCustomer;
	}

	public void setNewCustomer(Boolean newCustomer) {
		this.newCustomer = newCustomer;
	}

	public Boolean getNewTechnician() {
		return newTechnician;
	}

	public void setNewTechnician(Boolean newTechnician) {
		this.newTechnician = newTechnician;
	}

	public String getCustomerTown() {
		return customerTown;
	}

	public void setCustomerTown(String customerTown) {
		this.customerTown = customerTown;
	}

	public String getGenContractTypeContract() {
		return genContractTypeContract;
	}

	public void setGenContractTypeContract(String genContractTypeContract) {
		this.genContractTypeContract = genContractTypeContract;
	}

	public Map<String, String> getContractTypeList() {
		return contractTypeList;
	}

	public void setContractTypeList(Map<String, String> contractTypeList) {
		this.contractTypeList = contractTypeList;
	}

	public List<DinType> getDinTypeList() {
		return dinTypeList;
	}

	public void setDinTypeList(List<DinType> dinTypeList) {
		this.dinTypeList = dinTypeList;
	}

	public DinType getDINForAll() {
		return DINForAll;
	}

	public void setDINForAll(DinType dINForAll) {
		DINForAll = dINForAll;
	}

	public List<DinType> getDINList() {
		return DINList;
	}

	public void setDINList(List<DinType> dINList) {
		DINList = dINList;
	}

	public String getAddComment() {
		return addComment;
	}

	public void setAddComment(String addComment) {
		this.addComment = addComment;
	}

	public String getAddFloor() {
		return addFloor;
	}

	public void setAddFloor(String addFloor) {
		this.addFloor = addFloor;
	}

	public String getAddRoom() {
		return addRoom;
	}

	public void setAddRoom(String addRoom) {
		this.addRoom = addRoom;
	}

	public Boolean getElementsAssigned() {
		return elementsAssigned;
	}

	public void setElementsAssigned(Boolean elementsAssigned) {
		this.elementsAssigned = elementsAssigned;
	}

	public List<AddElements> getAddElements() {
		return addElements;
	}

	public void setAddElements(List<AddElements> addElements) {
		this.addElements = addElements;
	}

}
