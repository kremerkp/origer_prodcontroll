package lu.origer.serviceagree.frontend.synch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.persistence.Entity;

import org.apache.openejb.config.sys.StackHandler.ServiceElement;

import lu.origer.serviceagree.backend.contact.PersonFacade;
import lu.origer.serviceagree.backend.contract.ChecklistItemFacade;
import lu.origer.serviceagree.backend.contract.FileArchiveFacade;
import lu.origer.serviceagree.backend.contract.ServiceContractFacade;
import lu.origer.serviceagree.backend.contract.ServiceElementFacade;
import lu.origer.serviceagree.backend.contract.ServiceFacade;
import lu.origer.serviceagree.backend.main.LoggingFacade;
import lu.origer.serviceagree.backend.synch.ServiceHistoryFacade;
import lu.origer.serviceagree.backend.synch.SynchControlFacade;
import lu.origer.serviceagree.backend.synch.SynchJobsFacade;
import lu.origer.serviceagree.backend.synch.SynchServiceFacade;
import lu.origer.serviceagree.backend.synch.SynchTimeRecordingFacade;
import lu.origer.serviceagree.backend.synch.TimeRecordingHistoryFacade;
import lu.origer.serviceagree.frontend.main.BasicBean;
import lu.origer.serviceagree.frontend.main.Constants;
import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contract.Service;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.main.FileArchive;
import lu.origer.serviceagree.models.main.Logging;
import lu.origer.serviceagree.models.synch.ServiceHistory;
import lu.origer.serviceagree.models.synch.SynchControl;
import lu.origer.serviceagree.models.synch.SynchJobs;
import lu.origer.serviceagree.models.synch.SynchService;
import lu.origer.serviceagree.models.synch.SynchTimeRecording;
import lu.origer.serviceagree.models.synch.TimeRecordingHistory;

@SuppressWarnings("rawtypes")
@ManagedBean
@ViewScoped
public class SynchJobBean extends BasicBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	SynchJobsFacade synchJobsFacade;

	@Inject
	SynchControlFacade synchControlFacade;

	@Inject
	ServiceHistoryFacade serviceHistoryFacade;

	@Inject
	FileArchiveFacade fileArchiveFacade;

	@Inject
	TimeRecordingHistoryFacade timeRecordingHistoryFacade;

	@Inject
	SynchTimeRecordingFacade synchTimeRecordingFacade;

	@Inject
	ServiceElementFacade serviceElementFacade;

	@Inject
	ServiceFacade serviceFacade;

	@Inject
	PersonFacade personFacade;

	@Inject
	ServiceContractFacade serviceContractFacade;

	@Inject
	SynchServiceFacade synchServiceFacade;

	@Inject
	ChecklistItemFacade checklistItemFacade;	

	List<SynchJobs> synchJobs;
	
	SynchJobs actualSynchJob; 

	List<SynchJobs> selectedSynchJobs;
	
	List<SynchJobs> selectedSynchJobsServlet;

	List<SynchJobs> filteredSynchJobs;

	List<String> errorOnSynch = new ArrayList<>();
	
	private static final String LOG_REPORTER = "Synchronisierung";
	private static final Integer ADMIN_ID = 4;

	@PostConstruct
	public void init() {
		// serviceElements =
		// serviceElementFacade.findAllServiceElementsForPrintingBarcode();
		synchJobs = synchJobsFacade.findAllOrderByDateDesc();
		synchJobs = new ArrayList<>(synchJobs);
	}

	public void setLastControlDateForElement(ServiceHistory sh) {
		ServiceElements se = sh.getElement();
		if (se.getControlDate() != null && sh.getStartTime() != null) {
			if (se.getControlDate().before(sh.getStartTime())) {
				se.setControlDate(sh.getStartTime());
				se.setEditDate(new Date());
				// Current user is not set if function is called via servlet.
				if(getSessionBean() != null && getSessionBean().getCurrentUser() != null)
				{
					se.setEditUser(getSessionBean().getCurrentUser().getId());
				}
				else
				{
					se.setEditUser(ADMIN_ID);
				}
				serviceElementFacade.edit(se);
			}
		} else if (se.getControlDate() == null && sh.getStartTime() != null) {
			se.setControlDate(sh.getStartTime());
			se.setEditDate(new Date());
			if(getSessionBean() != null && getSessionBean().getCurrentUser() != null)
			{
				se.setEditUser(getSessionBean().getCurrentUser().getId());
			}
			else
			{
				se.setEditUser(ADMIN_ID);
			}
			serviceElementFacade.edit(se);
		}

	}

	public void updateNewHistoryElement(ServiceHistory newSh, ServiceHistory sh, SynchControl sc) {
		try {
			// Anstatt edit create (Jeder Eintrag wird historisiert!!
			serviceHistoryFacade.edit(newSh);
			ServiceElements se = sh.getElement();
			se.setControlDate(sh.getStartTime());
			setLastControlDateForElement(sh);
			try {
				// löscht Historieneintrag aus der Control
				synchControlFacade.remove(sc);
			} catch (Exception e) {
				System.out.println("UPDATE - Eintrag bereits gelöscht.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorOnSynch.add("Hist-Eintrag konnte nicht erstellt werden SC-ID: " + sc.getId());
		}
	}

	public void createNewHistoryElement(ServiceHistory newSh, SynchControl sc) {
		try {
			newSh.setId(null);
			serviceHistoryFacade.create(newSh);
			setLastControlDateForElement(newSh);
			
			try {
				synchControlFacade.remove(sc);
			} catch (Exception e) {
				System.out.println("UPDATE - Eintrag bereits gelöscht.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			errorOnSynch.add("Hist-Eintrag konnte nicht erstellt werden SC-ID: " + sc.getId());
		}
	}

	/**
	 * Updated 24/08/17
	 * Columns Comment and vf added to SynchControl
	 * @param sc
	 * @return
	 */
	public ServiceHistory setServiceHistoryValues(SynchControl sc) {
		ServiceHistory newSh = new ServiceHistory();
		newSh.setId(sc.getId());
		newSh.setCheckedAsDefect(sc.getCheckedAsDefect());
		newSh.setCheckedAsLack(sc.getCheckedAsLack());
		newSh.setCheckedAsOk(sc.getCheckedAsOk());
		newSh.setCheckingSeconds(sc.getCheckingSeconds());
		newSh.setCheckListItem(checklistItemFacade.find(sc.getCheckListItem()));
		newSh.setCreateOffer(sc.getCreateOffer());
		newSh.setDescription(sc.getDescription());
		newSh.setErrorHistoryFlag(sc.getErrorHistoryFlag());
		newSh.setElement(serviceElementFacade.find(sc.getElement()));
		newSh.setEndTime(sc.getEndTime());		
		newSh.setComment(sc.getComment());
		newSh.setVisualAndFuntionControl(sc.getVf());
		newSh.setFunctionalControl(sc.getFunctionalControl());
		newSh.setService(serviceFacade.find(sc.getService()));
		newSh.setSetupSeconds(sc.getSetupSeconds());
		newSh.setStartTime(sc.getStartTime());
		newSh.setCheckedAsRepaired(sc.getCheckedAsRepaired());
		newSh.setVisualControl(sc.getVisualControl());
		return newSh;
	}

	/**
	 * Funktion fügt beim "ersten Durchlauf" ein Update auf den Historieneintrag
	 * durch Ab dem zweiten Durchlauf d.h. sobald das starttime nicht null ist
	 * wird ein zusätzlicher Eintrag in der Servicehistorie eingefügt.
	 * 
	 * @param sc
	 * @param sh
	 */
	public void updateHistory(SynchControl sc, ServiceHistory sh) {
		// Wenn History startdate == null Update sonst neuer Eintrag!
		if (sh == null || sc.getId().equals(sh.getId())) {
			ServiceHistory newSh = new ServiceHistory();
			newSh = setServiceHistoryValues(sc);
			setLastControlDateForElement(newSh);
			// ServiceElement / erster durchlauf, Startdatum nicht gesetzt
			// (Eintrag wird aktualisiert)
			if (sh == null) {
				createNewHistoryElement(newSh, sc);
				// min. zweiter durchlauf
			} else if (sh.getStartTime() == null || sh.getStartTime().equals(sc.getStartTime())) {				
				updateNewHistoryElement(newSh, sh, sc);
				// min. zweiter Durchlauf, es wird ein zusätzlicher (neuer
				// Eintrag) erzeugt.
			} else {
				createNewHistoryElement(newSh, sc);
			}
		}

	}

	public void synchServices() {
		List<SynchService> sList = synchServiceFacade.findAll();
		try {
			for (SynchService ss : sList) {
				Service s = serviceFacade.find(ss.getServiceId());
				Integer scId = serviceFacade.getServiceContractIdFromService(s.getId());
				ServiceContract sc;
				if (scId != null) {
					sc = serviceContractFacade.find(scId);
					s.setServiceContract(sc);
					s.setActive(false);
					s.setPercentReady(100F);
					s.setReady(true);
					s.setDescription(ss.getDescription());
					serviceFacade.setNextServiceActive(s, sc.getId());
					serviceFacade.edit(s);
				}
				synchServiceFacade.remove(ss);
			}

		} catch (Exception e) {
			// TODO: handle exception
			errorOnSynch.add("Synch-Services fehlgeschlagen: " + e);
		}
		
		if (errorOnSynch.size() > 0) {
			this.actualSynchJob.setWebappState(SynchJobsFacade.JOB_ERROR);
			String accDesc = this.actualSynchJob.getDescription();
			this.actualSynchJob.setDescription(accDesc + " " + "; SERVICE_SYNCH_ERROR: " + errorOnSynch.get(0));			
		}
		
	}

	public void synchTimeRecordings() {
		// List<SynchTimeRecording> sList =
		// synchTimeRecordingFacade.findAllTimeRec();
		System.out.println("Calling synchTimeRecordings");
		List<SynchTimeRecording> findAllTimeRec = synchTimeRecordingFacade.findAllTimeRec();
		try {
			for (SynchTimeRecording st : findAllTimeRec) {
				TimeRecordingHistory trh = new TimeRecordingHistory();
				trh.setMechanic(personFacade.find(st.getMechanic()));
				trh.setRecordDate(st.getRecordDate());
				trh.setService(serviceFacade.find(st.getService()));
				trh.setServiceElement(serviceElementFacade.find(st.getServiceElement()));
				trh.setTimeInSeconds(st.getTimeInSeconds());
				trh.setBuildingSite(st.getBuildingSite());
				if (st.getType() != null && st.getType().toLowerCase().equals("rgz")) {
					trh.setBillable(true);
					trh.setPayed(false);
				} else {
					trh.setBillable(false);
					trh.setPayed(false);
				}
				trh.setType(st.getType());
				trh.setChecklistItem(st.getChecklistItem());
				trh.setDescription(st.getDescription());
				// Check database for duplicates before saving.
				Integer serviceId = null;
				Integer elementId = null;
				Integer mechanicId = null;
				if(trh.getService() != null)
				{
					serviceId = trh.getService().getId();
				}
				if(trh.getServiceElement() != null)
				{
					elementId = trh.getServiceElement().getId();
				}
				if(trh.getMechanic() != null)
				{
					mechanicId = trh.getMechanic().getId();
				}
				if(!timeRecordingHistoryFacade.isDuplicate(trh.getTimeInSeconds(), serviceId, elementId, mechanicId))
				{					
					timeRecordingHistoryFacade.create(trh);
					if (st.getFiles() != null && st.getFiles().size() > 0) {
						for (FileArchive fa : st.getFiles()) {							
							fa.setSynchTimeRecording(null);
							fa.setTimeRecordingHistory(trh);
							fileArchiveFacade.edit(fa);
						}						
					}
				}
				else
				{
					// Duplicate found, create log entry.
					System.out.println("Duplicate time recording found");															
				}
				synchTimeRecordingFacade.remove(st);
			}

		} catch (Exception e) {
			// TODO: handle exception
			errorOnSynch.add("Synch-Timerecording fehlgeschlagen: " + e);
		}

		if (errorOnSynch.size() > 0) {
			this.actualSynchJob.setWebappState(SynchJobsFacade.JOB_ERROR);
			if(this.actualSynchJob != null)
			{
				String accDesc = this.actualSynchJob.getDescription();
				this.actualSynchJob.setDescription(accDesc + " " + "; TIMERECORDING_SYNCH_ERROR: "  + errorOnSynch.get(0));			
			}
		}

		errorOnSynch = new ArrayList<>();

	}
	
	public void synchJobsServ() {
        // alle ausgeählten Jobs
        for (SynchJobs sy : selectedSynchJobsServlet) {
            // nur die Job die erfolgreich über Android comitted wurden
            if (sy.getAndroidState().equals(SynchJobsFacade.JOB_COMPLETE)) {
                this.actualSynchJob = sy;
                List<SynchControl> sc = synchControlFacade.findSynchControlForJob(sy.getId());
                for (SynchControl con : sc) {
                    ServiceHistory sh = serviceHistoryFacade.find(con.getId());
                    if (con.getCreateOffer()) {
                        ServiceElements se = serviceElementFacade.find(con.getElement());
                        se.setCreateOffer(true);
                        serviceElementFacade.edit(se);
                    }
                    this.errorOnSynch = new ArrayList<>(); 
                    updateHistory(con, sh);
                }
            }

            if (errorOnSynch.size() > 0) {
                sy.setWebappState(SynchJobsFacade.JOB_ERROR);
                String err = "";
                for (String s : errorOnSynch) {
                    err += s + "\n";
                }
                System.out.println("**");
                System.out.println("Fehler: " + err);
                System.out.println("**");
                String lines[] = err.split("\\n");
                if((lines != null && lines.length > 0) && lines.length > 21)
                {
                    // Reduce error text length.
                	for(int i = 0; i < 20; i++) {
                		err += lines[i];
                	}
                }
                sy.setDescription("HIST_SYNCH: " + err);
                synchJobsFacade.edit(sy);
            } else {
                sy.setWebappState(SynchJobsFacade.JOB_COMPLETE);
                synchJobsFacade.edit(sy);
            }

            errorOnSynch = new ArrayList<>();

        }
    }

	public void synchJobs() {
		// alle ausgewählten Jobs
		for (SynchJobs sy : selectedSynchJobs) {
			// nur die Job die erfolgreich über Android comitted wurden
			if (sy.getAndroidState().equals(SynchJobsFacade.JOB_COMPLETE)) {
				this.actualSynchJob = sy;
				List<SynchControl> sc = synchControlFacade.findSynchControlForJob(sy.getId());
				for (SynchControl con : sc) {
					ServiceHistory sh = serviceHistoryFacade.find(con.getId());
					if (con.getCreateOffer()) {
						ServiceElements se = serviceElementFacade.find(con.getElement());
						se.setCreateOffer(true);
						serviceElementFacade.edit(se);
					}

					updateHistory(con, sh);
				}
			}

			if (errorOnSynch.size() > 0) {
				sy.setWebappState(SynchJobsFacade.JOB_ERROR);
				String err = "";
				for (String s : errorOnSynch) {
					err += s + "\n";
				}
				sy.setDescription("HIST_SYNCH: " + err);
				synchJobsFacade.edit(sy);
			} else {
				sy.setWebappState(SynchJobsFacade.JOB_COMPLETE);
				synchJobsFacade.edit(sy);
			}

			errorOnSynch = new ArrayList<>();

		}
	}

	public void synchJobsFunction() {
		synchJobs();
		synchTimeRecordings();
		synchServices();
	}
	
	public void synchJobsFunctionServlet() {
		synchJobsServ();
		synchTimeRecordings();
		synchServices();			
	}

	public void print() {

	}

	public List<SynchJobs> getSynchJobs() {
		return synchJobs;
	}

	public void setSynchJobs(List<SynchJobs> synchJobs) {
		this.synchJobs = synchJobs;
	}

	public List<SynchJobs> getFilteredSynchJobs() {
		return filteredSynchJobs;
	}

	public void setFilteredSynchJobs(List<SynchJobs> filteredSynchJobs) {
		this.filteredSynchJobs = filteredSynchJobs;
	}

	public List<SynchJobs> getSelectedSynchJobs() {
		return selectedSynchJobs;
	}

	public void setSelectedSynchJobs(List<SynchJobs> selectedSynchJobs) {
		this.selectedSynchJobs = selectedSynchJobs;
	}

	public List<SynchJobs> getSelectedSynchJobsServlet() {
		return selectedSynchJobsServlet;
	}

	public void setSelectedSynchJobsServlet(List<SynchJobs> selectedSynchJobsServlet) {
		this.selectedSynchJobsServlet = selectedSynchJobsServlet;
	}
	
	

}
