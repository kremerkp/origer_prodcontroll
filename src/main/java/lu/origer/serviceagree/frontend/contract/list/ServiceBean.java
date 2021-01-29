package lu.origer.serviceagree.frontend.contract.list;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.omnifaces.util.Faces;

import lu.origer.serviceagree.backend.contract.FileArchiveFacade;
import lu.origer.serviceagree.backend.contract.ServiceContractFacade;
import lu.origer.serviceagree.backend.contract.ServiceElementFacade;
import lu.origer.serviceagree.backend.contract.ServiceFacade;
import lu.origer.serviceagree.backend.synch.ServiceHistoryFacade;
import lu.origer.serviceagree.frontend.main.BasicBean;
import lu.origer.serviceagree.frontend.main.SessionBean;
import lu.origer.serviceagree.frontend.report.ReportGeneratorBean;
import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contract.OpenOfferElements;
import lu.origer.serviceagree.models.contract.Service;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.reporting.AbnahmeBericht;
import lu.origer.serviceagree.models.reporting.Zertifikat;
import lu.origer.serviceagree.models.synch.ServiceElementHistory;

@SuppressWarnings("rawtypes")
@ManagedBean
@ViewScoped
public class ServiceBean extends BasicBean {

	private static final long serialVersionUID = 1L;

	@Inject
	private ServiceFacade serviceFacade;

	@Inject
	private ServiceHistoryFacade serviceHistoryFacade;

	@Inject
	private ServiceElementFacade serviceElementFacade;

	@Inject
	private FileArchiveFacade fileArchivFacade;

	@Inject
	private ServiceContractFacade contractFacade;

	@Inject
	private SessionBean sessionBean;

	private List<OpenOfferElements> openOfferElements;

	private List<OpenOfferElements> openOfferElementsSelected;

	private List<OpenOfferElements> openOfferElementsFiltered;

	private List<Service> serviceList;

	private List<Service> serviceListOpenOffers;

	private List<Service> serviceListfiltered;
	private String comment;

	private String commentService;

	private Service selectedService;

	private ServiceContract serviceContract;

	@PostConstruct
	public void init() {
		Integer custId = getApplicationBean().getCustomerIdFromURL();

		if (getSessionBean().getIsAdmin()) {

			if (custId != null) {
				serviceList = serviceFacade.findAllServicesForCustomer(custId);
				serviceList = new ArrayList<>(serviceList);
			} else {
				serviceListOpenOffers = serviceFacade.findAllServicesOpenOffer();
				serviceListOpenOffers = new ArrayList<>(serviceListOpenOffers);
				openOfferElements = serviceElementFacade.findOpenOffers();
				openOfferElements = new ArrayList<>(openOfferElements);
				serviceList = serviceFacade.findAllServices();
				serviceList = new ArrayList<>(serviceList);
			}

		} else {
			serviceList = serviceFacade.findAllServicesForCustomer(getSessionBean().getCurrentUser());
			serviceList = new ArrayList<>(serviceList);
		}
	}

	public void setComment() {

		// if (this.selectedService == null){
		// this.comment = "";
		// } else {
		// this.comment = this.selectedService.getDescription();
		// RequestContext context = RequestContext.getCurrentInstance();
		// context.execute("PF('addDescServiceReady').show();");
		// }
	}

	public void resetSelected() {
		this.openOfferElementsSelected = new ArrayList<>();
	}

	public void setCommentForSelectedService() {
		this.commentService = this.selectedService.getComment();
	}
	
	public void cancelOfferForElements()
	{
		System.out.println ("Cancel offer");
		try
		{
			for(OpenOfferElements tableEntry : this.openOfferElementsSelected)
			{
				final ServiceElements element = tableEntry.getServiceElementObject();
				element.setCreateOffer(false);
				element.setOfferState(null);
				element.setEditDate(new Date());
				element.setEditUser(getSessionBean().getCurrentUser().getId());				
				
				this.serviceElementFacade.edit(element);
			}
		}
		catch(Exception e)
		{
			System.out.println("Kein Element ausgewählt.");
		}
	}

	public void setServiceActive() {
		System.out.println("Set Service Active");
		try {
			this.selectedService.setActive(true);
			this.selectedService.setEditDate(new Date());
			this.selectedService.setEditUser(getSessionBean().getCurrentUser().getId());

			serviceFacade.edit(this.selectedService);
		} catch (Exception e) {
			System.out.println("Kein Service ausgewählt");
		}
	}

	public void createApprovalReport(Service service) throws IOException, InterruptedException, SQLException {
		List<AbnahmeBericht> aList = new ArrayList<>();
		AbnahmeBericht a = new AbnahmeBericht();
		a.setElementeGesamt(service.getServiceElementList().size());
		String urlSign = fileArchivFacade.findApprovalSignForService(service);
		String signName = fileArchivFacade.findApprovalSignNameForService(service);

		Integer defekt = 0;
		Integer ok = 0;
		Integer mangel = 0;
		Integer offen = 0;
		Integer pruef = 0;
		Integer repair = 0;
		List<ServiceElementHistory> sListHist = new ArrayList<>();
		for (ServiceElements se : service.getServiceElementList()) {
			Date lastDate = serviceFacade.getLastControlDateForElement(service, se);
			String state = "";
			if(service == null)
			{
				state = serviceHistoryFacade.getAggregatedStateFromAllServiceHistoryList(lastDate, se);
			}
			else
			{
				state = serviceHistoryFacade.getAggregatedStateForDate(service,se);
			}
			String desc = serviceHistoryFacade.getAggregatedDescriptionFromServiceHistoryList(service, lastDate, se);
			String tooRepair = serviceHistoryFacade.getAggregatedChecklistItems2Repair(service, lastDate, se);
			String repaired = serviceHistoryFacade.getAggregatedRepairedChecklistItems(service, lastDate, se);
			ServiceElementHistory seh = new ServiceElementHistory();
			if (state.contains("ungew")) {
				offen++;
			} else {
				pruef++;
				if (state.contains("ok")) {
					ok++;
				}
				if (state.contains("mangel")) {
					mangel++;
				}
				if (state.contains("defekt")) {
					defekt++;
				}
				if (state.contains("repariert")) {
					repair++;
				}
			}
			seh.setDate(lastDate);
			seh.setName(se.getElementnumber());
			seh.setDescription(desc);
			seh.setChecklistitems2Repair(tooRepair);
			seh.setChecklistitemsRepaired(repaired);
			seh.setState(state);
			seh.setRoom(se.getRoom());
			seh.setFloor(se.getFloor());
			sListHist.add(seh);

		}
		a.setElementeDefekt(defekt);
		a.setElementeGeprüft(pruef);
		a.setElementeMangelhaft(mangel);
		a.setElementeRepariert(repair);
		a.setListElements(sListHist);
		a.setUrlUnterschrift(urlSign);
		a.setUnterschriftName(signName);

		aList.add(a);

		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ReportGeneratorBean reportGeneratorBean = (ReportGeneratorBean) FacesContext.getCurrentInstance()
				.getApplication().getELResolver().getValue(elContext, null, "reportGeneratorBean");

		reportGeneratorBean.generateApprovalReport(aList, sessionBean.getUserLocale());

	}

	public void createCertification(Service service) throws IOException, InterruptedException, SQLException {
		List<Zertifikat> cList = new ArrayList<>();
		Zertifikat z = new Zertifikat();
		z.setContractNumber(service.getServiceContract().getServiceContractNumber());
		z.setEndDate(serviceFacade.findLastDateFromService(service));
		z.setIntervall(service.getIntervall().toString());
		z.setResponsiblePerson(service.getTechnician().getFirstname() + ", " + service.getTechnician().getLastname());
		z.setServiceObject(service.getServiceContract().getBuildingSite().getCode());
		String elementList;
		if(service.getElememtsChecked() != null)
		{
			elementList = service.getElememtsChecked().toString();
		}
		else
		{
			elementList = "0";
		}
		if(this.sessionBean.getUserLocale().equals(Locale.FRANCE))
		{
			elementList += " de ";
		}
		else
		{
			elementList += " von ";
		}				
		if(service.getTotalElements() != null)
		{
			elementList += service.getTotalElements().toString();
		}
		else
		{
			elementList += "0";
		}
		/*for (ServiceElements se : service.getServiceElementList()) {
			elementList += se.getElementnumber() + ", ";
		}
		StringBuilder b = new StringBuilder(elementList);
		b.replace(elementList.lastIndexOf(","), elementList.lastIndexOf(",") + 1, "");
		String r = "";
		if (b.length() > 240) {
			r = b.substring(0, 240) + " ...";
		} else {
			r = b.toString();
		}
		z.setElementList(r);*/
		z.setElementList(elementList);		
		z.setHistElements(serviceHistoryFacade.findHistForServiceWithoutRegie(service));
		z.setSubZertifkat(serviceHistoryFacade.findSubZertifikatForService(service));

		cList.add(z);

		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ReportGeneratorBean reportGeneratorBean = (ReportGeneratorBean) FacesContext.getCurrentInstance()
				.getApplication().getELResolver().getValue(elContext, null, "reportGeneratorBean");

		reportGeneratorBean.generateCertification(cList, sessionBean.getUserLocale());

		System.out.println("service-Intervall: " + service.getIntervall());
	}

	public void createNewOffer() {
		HashMap<String, String> mp = new HashMap<>();
		String elements = "";
		String contractId = "";
		Boolean firstrun = true;

		for (OpenOfferElements ooe : this.openOfferElementsSelected) {
			contractId = ooe.getServiceContractId().toString();
			this.serviceContract = ooe.getServiceContract();
			System.out.println("contractID: " + ooe.getServiceContractId());
			if (firstrun) {
				elements += ooe.getServiceElementObject().getId();
				firstrun = false;
			} else {
				elements += "," + ooe.getServiceElementObject().getId();
			}

			mp.put(contractId, contractId);
		}
		System.out.println(mp.size());
		if (mp.size() > 1) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info",
					"Es dürfen nur Elemente innerhalb eines Vertrags ausgewählt werden."));
		} else {

			System.out.println("To Implement NEW OFFER");
			try {
				Faces.redirect("faces/origer/serveAgree/admin/offers/offers_edit.xhtml?faces-redirect&elements="
						+ elements + "&contractId=" + contractId);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void setServiceComment() {
		System.out.println("Set Service Comment");
		try {
			this.selectedService.setComment(this.commentService);
			;
			this.selectedService.setEditUser(getSessionBean().getCurrentUser().getId());
			serviceFacade.edit(this.selectedService);
			this.commentService = "";
		} catch (Exception e) {
			System.out.println("Kein Service ausgewählt");
		}

	}

	public void setServiceInactive() {
		System.out.println("Set Service Inactive");
		try {
			this.selectedService.setActive(false);
			this.selectedService.setEditDate(new Date());
			this.selectedService.setEditUser(getSessionBean().getCurrentUser().getId());

			serviceFacade.edit(this.selectedService);
		} catch (Exception e) {
			System.out.println("Kein Service ausgewählt");
		}
	}

	public void setServiceReady() {
		System.out.println("Set Service Ready");
		try {
			this.selectedService.setReady(true);
			this.selectedService.setEditDate(new Date());
			this.selectedService.setDescription(this.comment);
			this.selectedService.setEditUser(getSessionBean().getCurrentUser().getId());
			serviceFacade.edit(this.selectedService);
			this.comment = "";
		} catch (Exception e) {
			System.out.println("Kein Service ausgewählt");
		}
	}

	public void setServiceOnWork() {
		System.out.println("Set Service on Work");
		try {
			this.selectedService.setReady(false);
			this.selectedService.setEditDate(new Date());
			this.selectedService.setEditUser(getSessionBean().getCurrentUser().getId());
			serviceFacade.edit(this.selectedService);
		} catch (Exception e) {
			System.out.println("Kein Service ausgewählt");
		}

	}

	public void filterList() {

	}

	public void test() {

	}

	public String serviceProtocol() {
		return "/origer/serveAgree/customer/serviceProofReporting_list.xhtml?faces-redirect=true&serviceID="
				+ this.selectedService.getId();
	}

	public List<Service> getServiceList() {
		return serviceList;
	}

	public void setServiceList(List<Service> serviceList) {
		this.serviceList = serviceList;
	}

	public void openService() {
		try {
			Faces.redirect("/origer/faces/origer/serveAgree/contract/contract_edit.xhtml?faces-redirect=true&id="
					+ (selectedService.getServiceContract().getId()));
		} catch (IOException ex) {
			Logger.getLogger(ChecklistItemCategoryBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void createNewService() {
		try {
			Faces.redirect("faces/origer/serveAgree/admin/checklist/checklist_list.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Service getSelectedService() {
		return selectedService;
	}

	public void setSelectedService(Service selectedService) {
		this.selectedService = selectedService;
	}

	public List<Service> getServiceListfiltered() {
		return serviceListfiltered;
	}

	public void setServiceListfiltered(List<Service> serviceListfiltered) {
		this.serviceListfiltered = serviceListfiltered;
	}

	public List<Service> getServiceListOpenOffers() {
		return serviceListOpenOffers;
	}

	public void setServiceListOpenOffers(List<Service> serviceListOpenOffers) {
		this.serviceListOpenOffers = serviceListOpenOffers;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<OpenOfferElements> getOpenOfferElements() {
		return openOfferElements;
	}

	public void setOpenOfferElements(List<OpenOfferElements> openOfferElements) {
		this.openOfferElements = openOfferElements;
	}

	public List<OpenOfferElements> getOpenOfferElementsSelected() {
		return openOfferElementsSelected;
	}

	public void setOpenOfferElementsSelected(List<OpenOfferElements> openOfferElementsSelected) {
		this.openOfferElementsSelected = openOfferElementsSelected;
	}

	public List<OpenOfferElements> getOpenOfferElementsFiltered() {
		return openOfferElementsFiltered;
	}

	public void setOpenOfferElementsFiltered(List<OpenOfferElements> openOfferElementsFiltered) {
		this.openOfferElementsFiltered = openOfferElementsFiltered;
	}

	public String getCommentService() {
		return commentService;
	}

	public void setCommentService(String commentService) {
		this.commentService = commentService;
	}

	public ServiceContract getServiceContract() {
		return serviceContract;
	}

	public void setServiceContract(ServiceContract serviceContract) {
		this.serviceContract = serviceContract;
	}

}
