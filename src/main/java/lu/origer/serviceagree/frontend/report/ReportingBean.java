package lu.origer.serviceagree.frontend.report;

import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import lu.origer.serviceagree.backend.contact.PersonFacade;
import lu.origer.serviceagree.backend.contract.BuildingSiteFacade;
import lu.origer.serviceagree.backend.contract.InvoiceFacade;
import lu.origer.serviceagree.backend.contract.ServiceContractFacade;
import lu.origer.serviceagree.backend.contract.ServiceFacade;
import lu.origer.serviceagree.backend.contract.ServiceTypeFacade;
import lu.origer.serviceagree.backend.main.UsersFacade;
import lu.origer.serviceagree.backend.reporting.ReportFacade;
import lu.origer.serviceagree.backend.synch.ServiceHistoryFacade;
import lu.origer.serviceagree.backend.synch.TimeRecordingHistoryFacade;
import lu.origer.serviceagree.models.contact.MappedPerson;
import lu.origer.serviceagree.models.contact.Person;
import lu.origer.serviceagree.models.contract.BuildingSite;
import lu.origer.serviceagree.models.contract.Invoice;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.contract.ServiceType;
import lu.origer.serviceagree.models.reporting.IntervalOverview;
import lu.origer.serviceagree.models.reporting.RegiezeitenReport;
import lu.origer.serviceagree.models.reporting.UebersichtVertragGesamt;
import lu.origer.serviceagree.models.synch.ServiceHistory;
import lu.origer.serviceagree.models.synch.TimeRecordingHistory;

@ManagedBean
@ViewScoped
public class ReportingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	BuildingSiteFacade buildingSiteFacade;
	@Inject
	ServiceContractFacade serviceContractFacade;
	@Inject
	ServiceTypeFacade serviceTypeFacade;
	@Inject
	UsersFacade usersFacade;
	@Inject
	PersonFacade personFacde;
	@Inject
	ServiceFacade serviceFacade;
	@Inject
	ReportFacade reportFacade;
	@Inject
	InvoiceFacade invoiceFacade;
	@Inject
	TimeRecordingHistoryFacade timeRecordingHistoryFacade;

	private List<TimeRecordingHistory> selectedHistBillable;

	private List<BuildingSite> regieBuildingSiteList = new ArrayList<>();

	private List<BuildingSite> regieBuildingSiteListSelected = new ArrayList<>();

	private List<ServiceContract> regieServiceContractList = new ArrayList<>();

	private List<ServiceContract> regieServiceContractListSelected = new ArrayList<>();

	private List<ServiceType> regieServiceTypeList = new ArrayList<>();

	private List<ServiceType> regieServiceTypeListSelected = new ArrayList<>();

	private List<MappedPerson> custCustomerList = new ArrayList<>();

	private List<MappedPerson> custCustomerListSelected = new ArrayList<>();

	private List<ServiceContract> contContractList = new ArrayList<>();

	private ServiceContract contContractListSelected;

	private List<UebersichtVertragGesamt> uList = new ArrayList<>();

	private Date custFromdate;

	private Date custTodate;

	private List<String> regieBillingStateList = new ArrayList<>();

	private List<String> regieBillingStateListSelected = new ArrayList<>();

	private Date regieFromDate;

	private Date regieToDate;

	private Date contFromDate;

	private Date contToDate;

	/*
	 * Repair report values
	 */
	private BuildingSite selectedSiteRepair;

	private List<RepairReportChecklistEntry> repairList;

	private List<RepairReportChecklistEntry> repairSelectedList;

	private void fillBillingState() {
		this.regieBillingStateList.add("verrechnet");
		this.regieBillingStateList.add("zu verrechnen");
	}

	@PostConstruct
	private void init() {
		regieBuildingSiteList = buildingSiteFacade.findAllActive();
		regieServiceContractList = serviceContractFacade.findAll();
		regieServiceTypeList = serviceTypeFacade.findAllActive();
		custCustomerList = personFacde.findAllCustomers();
		contContractList = serviceContractFacade.findAll();
		this.repairList = new ArrayList<>();
		if (regieBuildingSiteList != null && !regieBuildingSiteList.isEmpty()) {
			this.selectedSiteRepair = this.regieBuildingSiteList.get(0);			
			// Create table entries.
			this.repairList = this.serviceHistoryToRepairReportChecklistEntry(this.timeRecordingHistoryFacade.findRepairEntriesForSite(this.selectedSiteRepair.getId()));
		} else {
			this.selectedSiteRepair = null;
		}
		this.repairSelectedList = new ArrayList<>();
		fillBillingState();
	}
	
	private List<RepairReportChecklistEntry> serviceHistoryToRepairReportChecklistEntry(final List<ServiceHistory> entries)
	{
		final List<RepairReportChecklistEntry> result = new ArrayList<>();
		if(entries != null && !entries.isEmpty() )
		{
			final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			for(ServiceHistory entry : entries)
			{
				if (entry.getStartTime() != null) {
					Boolean createNewEntry = true;
					// Check if an entry for the current element exists.
					for(RepairReportChecklistEntry rrc : result)
					{
						if(entry.getStartTime().equals(rrc.getDate()))
						{
							// Entry already exists, adding...
							createNewEntry = false;
							rrc.getEntries().add(entry);
							break;
						}
					}
					if(createNewEntry && entry.getElement() != null)
					{
						// No entry found, generating...
						final RepairReportChecklistEntry rrc = new RepairReportChecklistEntry();
						rrc.setDate(entry.getStartTime());
						try
						{
							rrc.setEntryDate(formatter.format(entry.getStartTime()));
						}
						catch(Exception e)
						{						
							rrc.setEntryDate("");
						}
						rrc.setElementId(entry.getElement().getId());
						rrc.setEntries(new ArrayList<ServiceHistory>());
						rrc.getEntries().add(entry);			
						result.add(rrc);
					}
				}
			}
		}
		return result;
	}

	private List<RepairReportChecklistEntry> timeHistoryToRepairReportChecklistEntry(
			final List<TimeRecordingHistory> times) {
		final List<RepairReportChecklistEntry> result = new ArrayList<>();
		if (times != null && !times.isEmpty()) {
			final SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");
			for (TimeRecordingHistory time : times) {
				Boolean createNewEntry = true;
				// Check if an entry for the selected date exists.
				for (RepairReportChecklistEntry entry : result) {
					if (entry.getDate().equals(time.getRecordDate())) {
						// Entry already exists, adding...
						createNewEntry = false;
						entry.getTimes().add(time);
						break;
					}
				}
				if (createNewEntry) {
					// No entry found, creating...
					final RepairReportChecklistEntry entry = new RepairReportChecklistEntry();
					entry.setDate(time.getRecordDate());
					entry.setTimes(new ArrayList<TimeRecordingHistory>());
					entry.setEntryDate(formatter.format(time.getRecordDate()));
					entry.getTimes().add(time);
					result.add(entry);
				}
			}
		}
		return result;
	}

	public void filterByServiceContractType() {
		List<TimeRecordingHistory> filtered = new ArrayList<>();
		if (this.regieServiceTypeListSelected != null && this.regieServiceTypeListSelected.size() > 0) {
			for (TimeRecordingHistory trh : selectedHistBillable) {
				for (ServiceType test : regieServiceTypeListSelected) {
					if (trh.getService() != null) {
						if (trh.getService().getServiceType().getId().equals(test.getId())) {
							filtered.add(trh);
						}
					}
				}
			}

			this.selectedHistBillable = new ArrayList<>(filtered);
		}
	}

	public void filterByServiceContract() {
		List<TimeRecordingHistory> filtered = new ArrayList<>();
		if (this.regieServiceContractListSelected != null && this.regieServiceContractListSelected.size() > 0) {
			for (TimeRecordingHistory trh : selectedHistBillable) {
				for (ServiceContract test : regieServiceContractListSelected) {
					if (trh.getService() != null && trh.getService().getServiceContract() != null) {
						if (trh.getService().getServiceContract().getId().equals(test.getId())) {
							filtered.add(trh);
						}
					}
				}
			}

			this.selectedHistBillable = new ArrayList<>(filtered);
		}
	}

	public void filterByBuildingSite() {
		List<TimeRecordingHistory> filtered = new ArrayList<>();
		if (this.regieBuildingSiteListSelected != null && this.regieBuildingSiteListSelected.size() > 0) {
			for (TimeRecordingHistory trh : selectedHistBillable) {
				for (BuildingSite test : regieBuildingSiteListSelected) {
					if (trh.getBuildingSite() != null) {

						if (trh.getBuildingSite().getId().equals(test.getId())) {
							filtered.add(trh);
						}
					}

				}
			}

			this.selectedHistBillable = new ArrayList<>(filtered);
		}
	}

	public void filterByIsBillableFlag() {
		List<TimeRecordingHistory> filtered = new ArrayList<>();
		if (this.regieBillingStateListSelected != null && this.regieBillingStateListSelected.size() > 0) {
			for (TimeRecordingHistory trh : selectedHistBillable) {
				for (String test : regieBillingStateListSelected) {
					if (test.equalsIgnoreCase(RegiezeitenReport.VERRECHNET)) {
						if (!trh.getBillable()) {
							filtered.add(trh);
						}
					}
					if (test.equalsIgnoreCase(RegiezeitenReport.ZUVERRECHNEN)) {
						if (trh.getBillable()) {
							filtered.add(trh);
						}
					}

				}
			}

			this.selectedHistBillable = new ArrayList<>(filtered);
		}
	}

	public void filterByCustomer() {
		List<TimeRecordingHistory> filtered = new ArrayList<>();
		if (this.custCustomerListSelected != null && this.custCustomerListSelected.size() > 0) {
			for (TimeRecordingHistory trh : selectedHistBillable) {
				for (MappedPerson test : custCustomerListSelected) {
					if (trh.getService() != null) {
						if (trh.getService().getCustomer().getId().equals(test.getId())) {
							filtered.add(trh);
						}
					}
				}

			}
			this.selectedHistBillable = new ArrayList<>(filtered);
		}

	}

	public void setSelectedBillableHistByFilter() {
		this.selectedHistBillable = new ArrayList<>();
		selectedHistBillable = timeRecordingHistoryFacade.findAllByDate(this.regieFromDate, this.regieToDate);
		filterByBuildingSite();
		filterByServiceContract();
		filterByServiceContractType();
		filterByIsBillableFlag();
		filterByCustomer();
	}

	public void setGesamtuebersichtBericht() {
		UebersichtVertragGesamt u = new UebersichtVertragGesamt();
		List<IntervalOverview> ioList = new ArrayList<>();
		List<Invoice> rechnungsListe = new ArrayList<>();
		uList = new ArrayList<>();
		if (this.contContractListSelected != null) {
			ioList = serviceFacade.findIntervallOverview(this.contContractListSelected);
			rechnungsListe = invoiceFacade.findAllForServiceContract(this.contContractListSelected.getId());
			while (ioList.remove(null))
				;
		}

		List<RegiezeitenReport> rList = new ArrayList<>();
		RegiezeitenReport r = new RegiezeitenReport();
		rList.add(r);
		u.setIntervalOverviewList(ioList);
		selectedHistBillable = timeRecordingHistoryFacade.findAllByDate(this.custFromdate, this.custTodate);
		u.setRegieZeitReportList(reportFacade.mapTimeHistToRegieReport(selectedHistBillable));
		u.setInvoiceList(rechnungsListe);
		u.setContractName(this.contContractListSelected.getDescription());
		u.setContractCode(this.contContractListSelected.getServiceContractNumber());
		u.setBuldingSite(this.contContractListSelected.getBuildingSite().getCode());
		Person technician = this.contContractListSelected.getTechnician();
		u.setTechnician(technician.getFirstname() + ", " + technician.getLastname());

		System.out.println("ioList - Size: " + ioList.size());
		System.out.println("rList - Size: " + rList.size());

		uList.add(u);
	}

	public void setSelectedBillableHistByFilterCustomer() {
		this.selectedHistBillable = new ArrayList<>();
		selectedHistBillable = timeRecordingHistoryFacade.findAllByDate(this.custFromdate, this.custTodate);
		filterByCustomer();
	}

	public void generateReportGesamtuebersicht() throws IOException, InterruptedException, SQLException {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		setGesamtuebersichtBericht();

		System.out.println("u-List-Size: " + uList.size());

		ReportGeneratorBean reportGeneratorBean = (ReportGeneratorBean) FacesContext.getCurrentInstance()
				.getApplication().getELResolver().getValue(elContext, null, "reportGeneratorBean");		
		reportGeneratorBean.generateGesamtuebersicht(uList);
	}

	public void generateReportRegiezeitXLSCustomer() throws IOException, InterruptedException, SQLException {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		setSelectedBillableHistByFilterCustomer();
		ReportGeneratorBean reportGeneratorBean = (ReportGeneratorBean) FacesContext.getCurrentInstance()
				.getApplication().getELResolver().getValue(elContext, null, "reportGeneratorBean");

		reportGeneratorBean.generateTimeRecordingPayedXLS(selectedHistBillable);

	}

	public void generateReportRegiezeitCustomer() throws IOException, InterruptedException, SQLException {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		setSelectedBillableHistByFilterCustomer();
		ReportGeneratorBean reportGeneratorBean = (ReportGeneratorBean) FacesContext.getCurrentInstance()
				.getApplication().getELResolver().getValue(elContext, null, "reportGeneratorBean");

		reportGeneratorBean.generateTimeRecordingPayed(selectedHistBillable);

	}

	public void generateReportRegiezeitXLS() throws IOException, InterruptedException, SQLException {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		setSelectedBillableHistByFilter();
		ReportGeneratorBean reportGeneratorBean = (ReportGeneratorBean) FacesContext.getCurrentInstance()
				.getApplication().getELResolver().getValue(elContext, null, "reportGeneratorBean");

		reportGeneratorBean.generateTimeRecordingPayedXLS(selectedHistBillable);

	}

	public void generateReportRegiezeit() throws IOException, InterruptedException, SQLException {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		setSelectedBillableHistByFilter();
		ReportGeneratorBean reportGeneratorBean = (ReportGeneratorBean) FacesContext.getCurrentInstance()
				.getApplication().getELResolver().getValue(elContext, null, "reportGeneratorBean");

		reportGeneratorBean.generateTimeRecordingPayed(selectedHistBillable);

	}

	/*
	 * Repair report generation
	 */

	public void generateRepairList() {
		if (this.getSelectedSiteRepair() != null && this.getSelectedSiteRepair().getId() != null) {
			// Clear current selection.
			this.setRepairSelectedList(new ArrayList<RepairReportChecklistEntry>());
			// Fetch available repairs.			
			this.setRepairList(this.serviceHistoryToRepairReportChecklistEntry(this.timeRecordingHistoryFacade.findRepairEntriesForSite(this.getSelectedSiteRepair().getId())));
		}
	}

	public void generateRepairReport() throws IOException, InterruptedException, SQLException {
		if(this.repairSelectedList != null && !this.repairSelectedList.isEmpty())
		{
			// Repairs selected, generating report.
			ELContext elContext = FacesContext.getCurrentInstance().getELContext();
			ReportGeneratorBean reportGeneratorBean = (ReportGeneratorBean) FacesContext.getCurrentInstance()
					.getApplication().getELResolver().getValue(elContext, null, "reportGeneratorBean");	
			final List<ServiceHistory> entries = new ArrayList<>();
			for(RepairReportChecklistEntry entry : this.repairSelectedList)
			{				
				if(entry.getEntries() != null && !entry.getEntries().isEmpty())
				{
					entries.addAll(entry.getEntries());
				}
			}
			reportGeneratorBean.generateRepairHistoryReport(this.selectedSiteRepair, entries);
		}
		else
		{
			// No repair selected, notifying user.
			FacesContext context = FacesContext.getCurrentInstance();
			context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler", "Bitte eine oder mehrere Reparaturen auswählen."));
		}
	}

	/*
	 * Getters & Setters
	 */

	public List<BuildingSite> getRegieBuildingSiteList() {
		return regieBuildingSiteList;
	}

	public void setRegieBuildingSiteList(List<BuildingSite> regieBuildingSiteList) {
		this.regieBuildingSiteList = regieBuildingSiteList;
	}

	public List<ServiceContract> getRegieServiceContractList() {
		return regieServiceContractList;
	}

	public void setRegieServiceContractList(List<ServiceContract> regieServiceContractList) {
		this.regieServiceContractList = regieServiceContractList;
	}

	public List<ServiceType> getRegieServiceTypeList() {
		return regieServiceTypeList;
	}

	public void setRegieServiceTypeList(List<ServiceType> regieServiceTypeList) {
		this.regieServiceTypeList = regieServiceTypeList;
	}

	public Date getRegieFromDate() {
		return regieFromDate;
	}

	public void setRegieFromDate(Date regieFromDate) {
		this.regieFromDate = regieFromDate;
	}

	public Date getRegieToDate() {
		return regieToDate;
	}

	public void setRegieToDate(Date regieToDate) {
		this.regieToDate = regieToDate;
	}

	public Date getCustFromdate() {
		return custFromdate;
	}

	public void setCustFromdate(Date custFromdate) {
		this.custFromdate = custFromdate;
	}

	public Date getCustTodate() {
		return custTodate;
	}

	public void setCustTodate(Date custTodate) {
		this.custTodate = custTodate;
	}

	public List<ServiceContract> getContContractList() {
		return contContractList;
	}

	public void setContContractList(List<ServiceContract> contContractList) {
		this.contContractList = contContractList;
	}

	public Date getContFromDate() {
		return contFromDate;
	}

	public void setContFromDate(Date contFromDate) {
		this.contFromDate = contFromDate;
	}

	public Date getContToDate() {
		return contToDate;
	}

	public void setContToDate(Date contToDate) {
		this.contToDate = contToDate;
	}

	public List<BuildingSite> getRegieBuildingSiteListSelected() {
		return regieBuildingSiteListSelected;
	}

	public void setRegieBuildingSiteListSelected(List<BuildingSite> regieBuildingSiteListSelected) {
		this.regieBuildingSiteListSelected = regieBuildingSiteListSelected;
	}

	public List<ServiceContract> getRegieServiceContractListSelected() {
		return regieServiceContractListSelected;
	}

	public void setRegieServiceContractListSelected(List<ServiceContract> regieServiceContractListSelected) {
		this.regieServiceContractListSelected = regieServiceContractListSelected;
	}

	public List<ServiceType> getRegieServiceTypeListSelected() {
		return regieServiceTypeListSelected;
	}

	public void setRegieServiceTypeListSelected(List<ServiceType> regieServiceTypeListSelected) {
		this.regieServiceTypeListSelected = regieServiceTypeListSelected;
	}

	public ServiceContract getContContractListSelected() {
		return contContractListSelected;
	}

	public void setContContractListSelected(ServiceContract contContractListSelected) {
		this.contContractListSelected = contContractListSelected;
	}

	public List<String> getRegieBillingStateList() {
		return regieBillingStateList;
	}

	public void setRegieBillingStateList(List<String> regieBillingStateList) {
		this.regieBillingStateList = regieBillingStateList;
	}

	public List<String> getRegieBillingStateListSelected() {
		return regieBillingStateListSelected;
	}

	public void setRegieBillingStateListSelected(List<String> regieBillingStateListSelected) {
		this.regieBillingStateListSelected = regieBillingStateListSelected;
	}

	public List<MappedPerson> getCustCustomerList() {
		return custCustomerList;
	}

	public void setCustCustomerList(List<MappedPerson> custCustomerList) {
		this.custCustomerList = custCustomerList;
	}

	public List<MappedPerson> getCustCustomerListSelected() {
		return custCustomerListSelected;
	}

	public void setCustCustomerListSelected(List<MappedPerson> custCustomerListSelected) {
		this.custCustomerListSelected = custCustomerListSelected;
	}

	public BuildingSite getSelectedSiteRepair() {
		return selectedSiteRepair;
	}

	public void setSelectedSiteRepair(BuildingSite selectedSiteRepair) {
		this.selectedSiteRepair = selectedSiteRepair;
	}

	public List<RepairReportChecklistEntry> getRepairList() {
		return repairList;
	}

	public void setRepairList(List<RepairReportChecklistEntry> repairList) {
		this.repairList = repairList;
	}

	public List<RepairReportChecklistEntry> getRepairSelectedList() {
		return repairSelectedList;
	}

	public void setRepairSelectedList(List<RepairReportChecklistEntry> repairSelectedList) {
		this.repairSelectedList = repairSelectedList;
	}
}
