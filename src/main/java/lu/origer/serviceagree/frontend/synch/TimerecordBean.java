package lu.origer.serviceagree.frontend.synch;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.Entity;

import org.primefaces.event.CellEditEvent;

import lu.origer.serviceagree.backend.contract.ChecklistItemFacade;
import lu.origer.serviceagree.backend.contract.FileArchiveFacade;
import lu.origer.serviceagree.backend.contract.ServiceElementFacade;
import lu.origer.serviceagree.backend.contract.ServiceFacade;
import lu.origer.serviceagree.backend.synch.ServiceHistoryFacade;
import lu.origer.serviceagree.backend.synch.SynchControlFacade;
import lu.origer.serviceagree.backend.synch.SynchJobsFacade;
import lu.origer.serviceagree.backend.synch.SynchTimeRecordingFacade;
import lu.origer.serviceagree.backend.synch.TimeRecordingHistoryFacade;
import lu.origer.serviceagree.frontend.main.BasicBean;
import lu.origer.serviceagree.frontend.report.ReportGeneratorBean;
import lu.origer.serviceagree.models.synch.ServiceHistory;
import lu.origer.serviceagree.models.synch.SynchControl;
import lu.origer.serviceagree.models.synch.SynchJobs;
import lu.origer.serviceagree.models.synch.SynchTimeRecording;
import lu.origer.serviceagree.models.synch.TimeRecordingHistory;

@SuppressWarnings("rawtypes")
@ManagedBean
@ViewScoped
public class TimerecordBean extends BasicBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	TimeRecordingHistoryFacade timeRecordingHistoryFacade;

	List<TimeRecordingHistory> timeHist;

	List<TimeRecordingHistory> selectedHist;

	List<TimeRecordingHistory> filteredHist;

	List<TimeRecordingHistory> timeHistBillable;

	List<TimeRecordingHistory> selectedHistBillable;

	List<TimeRecordingHistory> filteredHistBillable;

	@PostConstruct
	public void init() {
		timeHist = timeRecordingHistoryFacade.findAllTransient();
		timeHist = new ArrayList<>(timeHist);
		timeHistBillable = timeRecordingHistoryFacade.findAllTransientBillable();
	}

	public void generateReport() throws IOException, InterruptedException, SQLException {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ReportGeneratorBean reportGeneratorBean = (ReportGeneratorBean) FacesContext.getCurrentInstance()
				.getApplication().getELResolver().getValue(elContext, null, "reportGeneratorBean");
		reportGeneratorBean.generateTimeRecordingPayed(selectedHistBillable);

	}
	
	public void deleteSelectedEntries(){
		for(TimeRecordingHistory th : this.selectedHist){
			this.timeHist.remove(th);
			this.filteredHist.remove(th);
			timeRecordingHistoryFacade.remove(th);
		}
	}

	public void setTimeBillable() {
		List<TimeRecordingHistory> trh = this.selectedHistBillable;
		for (TimeRecordingHistory t : trh) {
			t.setPayed(true);
			this.timeRecordingHistoryFacade.edit(t);
		}
	}

	public void setTimeBilled() {
		List<TimeRecordingHistory> trh = this.selectedHistBillable;
		for (TimeRecordingHistory t : trh) {
			t.setPayed(false);
			this.timeRecordingHistoryFacade.edit(t);
		}
	}

	public TimeRecordingHistoryFacade getTimeRecordingHistoryFacade() {
		return timeRecordingHistoryFacade;
	}

	public void onCellEdit(CellEditEvent event) {
		String oldValue = (String) event.getOldValue();
		String newValue = (String) event.getNewValue();
		String tt = event.getRowKey();
		FacesContext context = FacesContext.getCurrentInstance();
		TimeRecordingHistory entity = context.getApplication().evaluateExpressionGet(context, "#{time}",
				TimeRecordingHistory.class);

		System.out.println(newValue);
		if (newValue == null || newValue.length() <= 7) {
			newValue = oldValue;
		} else {
			String hours = newValue.substring(0, 2);
			String min = newValue.substring(3, 5);
			String sec = newValue.substring(6, 8);
			Integer seconds = (Integer.valueOf(hours) * 60 * 60) + (Integer.valueOf(min) * 60) + (Integer.valueOf(sec));
			System.out.println(entity.getTimeInSeconds());
			entity.setTime(newValue);
			entity.setTimeInSeconds(seconds);
			System.out.println(newValue + " " + "h:" + hours + " min: " + min + " sec: " + sec);

			timeRecordingHistoryFacade.edit(entity);
			System.out.println(entity.getTimeInSeconds());
		}

		// Integer newSec = (hours * 60 * 60) + (min * 60) + sec;

		// System.out.println(newSec);

		// if(newValue != null && !newValue.equals(oldValue)) {
		// FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell
		// Changed", "Old: " + oldValue + ", New:" + newValue);
		// FacesContext.getCurrentInstance().addMessage(null, msg);
		// System.out.println("Cell Changed" + "Old: " + oldValue + ", New:" +
		// newValue);
		// }
	}

	public void setTimeRecordingHistoryFacade(TimeRecordingHistoryFacade timeRecordingHistoryFacade) {
		this.timeRecordingHistoryFacade = timeRecordingHistoryFacade;
	}

	public List<TimeRecordingHistory> getTimeHist() {
		return timeHist;
	}

	public void setTimeHist(List<TimeRecordingHistory> timeHist) {
		this.timeHist = timeHist;
	}

	public List<TimeRecordingHistory> getSelectedHist() {
		return selectedHist;
	}

	public void setSelectedHist(List<TimeRecordingHistory> selectedHist) {
		this.selectedHist = selectedHist;
	}

	public List<TimeRecordingHistory> getFilteredHist() {
		return filteredHist;
	}

	public void setFilteredHist(List<TimeRecordingHistory> filteredHist) {
		this.filteredHist = filteredHist;
	}

	public List<TimeRecordingHistory> getTimeHistBillable() {
		return timeHistBillable;
	}

	public void setTimeHistBillable(List<TimeRecordingHistory> timeHistBillable) {
		this.timeHistBillable = timeHistBillable;
	}

	public List<TimeRecordingHistory> getSelectedHistBillable() {
		return selectedHistBillable;
	}

	public void setSelectedHistBillable(List<TimeRecordingHistory> selectedHistBillable) {
		this.selectedHistBillable = selectedHistBillable;
	}

	public List<TimeRecordingHistory> getFilteredHistBillable() {
		return filteredHistBillable;
	}

	public void setFilteredHistBillable(List<TimeRecordingHistory> filteredHistBillable) {
		this.filteredHistBillable = filteredHistBillable;
	}

}
