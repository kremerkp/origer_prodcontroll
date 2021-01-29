package lu.origer.serviceagree.backend.reporting;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import lu.origer.serviceagree.backend.contract.ChecklistItemFacade;
import lu.origer.serviceagree.backend.contract.FileArchiveFacade;
import lu.origer.serviceagree.backend.contract.ServiceElementFacade;
import lu.origer.serviceagree.backend.synch.TimeRecordingHistoryFacade;
import lu.origer.serviceagree.frontend.main.ApplicationBean;
import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contract.BuildingSite;
import lu.origer.serviceagree.models.main.FileArchive;
import lu.origer.serviceagree.models.reporting.AbnahmeBericht;
import lu.origer.serviceagree.models.reporting.BarcodeReport;
import lu.origer.serviceagree.models.reporting.PrintHistoryMaster;
import lu.origer.serviceagree.models.reporting.RegiezeitenReport;
import lu.origer.serviceagree.models.reporting.RepairReport;
import lu.origer.serviceagree.models.reporting.SubRepairDateReport;
import lu.origer.serviceagree.models.reporting.SubRepairElementReport;
import lu.origer.serviceagree.models.reporting.SubRepairReport;
import lu.origer.serviceagree.models.reporting.UebersichtVertragGesamt;
import lu.origer.serviceagree.models.reporting.Zertifikat;
import lu.origer.serviceagree.models.synch.ServiceHistory;
import lu.origer.serviceagree.models.synch.TimeRecordingHistory;

@Stateless
@LocalBean
public class ReportFacade implements ReportFacadeRemote {

	@Inject
	ServiceElementFacade serviceElementFacade;

	@Inject
	TimeRecordingHistoryFacade timeRecordingHistoryFacade;

	@Inject
	FileArchiveFacade fileArchiveFacade;

	@Inject
	ChecklistItemFacade checklistItemFacade;

	@Override
	public List<BarcodeReport> createReportBarcodes(List<ServiceElements> elementList) {
		List<BarcodeReport> result = new ArrayList<>();
		Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Generiere Bericht mit Barcodes: ");
		Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Anzahl Elemente: " + elementList.size());

		for (ServiceElements s : elementList) {
			BarcodeReport r = new BarcodeReport();
			r.setBarcode(s.getElementnumber());
			r.setBuildingSite(serviceElementFacade.getBuildingSiteCodeForElement(s));
			result.add(r);
		}
		return result;

	}

	public List<RegiezeitenReport> mapTimeHistToRegieReport(List<TimeRecordingHistory> tList) {
		List<RegiezeitenReport> reportList = new ArrayList<>();
		for (TimeRecordingHistory h : tList) {
			RegiezeitenReport r = new RegiezeitenReport();
			r.setComment(h.getDescription());
			r.setTime(h.getTime());
			r.setDate(h.getRecordDate());
			r.setDayDate(ApplicationBean.setDateToBeginOfDay(h.getRecordDate()));
			r.setMonteur(h.getMechanic().getFirstname() + ", " + h.getMechanic().getLastname());
			if (h.getBuildingSite() != null && h.getBuildingSite().getName() != null) {
				r.setBuildingSite(h.getBuildingSite().getName());
			} else {
				r.setBuildingSite("");
			}
			if (h.getBuildingSite() != null && h.getBuildingSite().getCode() != null) {
				r.setBuildingSiteCode(h.getBuildingSite().getCode());
			} else {
				r.setBuildingSiteCode("");
			}
			r.setTimeInSeconds(h.getTimeInSeconds());
			
			Boolean first = true;
			for (FileArchive file : h.getFiles()) {
				if (file.getSignature()) {
					r.setFileUrlSign(file.getUrl().replace("etc/", ""));
					r.setUnterschrift(file.getDescription());
				} else {
					if (first) {
						first = false;
						r.setFileUrl1(file.getUrl().replace("etc/", ""));
					} else {
						r.setFileUrl2(file.getUrl().replace("etc/", ""));
					}
				}
			}
			if (h.getServiceElement() != null) {
				r.setElement(h.getServiceElement().getElementnumber());
			}
			if (h.getBillable()) {
				r.setVerrechnungsArt(RegiezeitenReport.ZUVERRECHNEN);
			} else {
				r.setVerrechnungsArt(RegiezeitenReport.VERRECHNET);
			}
			if (h.getService() != null) {
				r.setKunde(h.getService().getCustomer().getFirstname() + ", "
						+ h.getService().getCustomer().getLastname());

				r.setServieIntervall(h.getService().getIntervall().toString());
				if (h.getService().getServiceContract() != null
						&& h.getService().getServiceContract().getServiceContractNumber() != null) {
					r.setServiceContract(h.getService().getServiceContract().getServiceContractNumber());
				} else {
					r.setServiceContract("");
				}
			} else {
				r.setKunde("");
				r.setServieIntervall("");
				r.setServiceContract("");
			}

			reportList.add(r);
		}
		return reportList;
	}

	/**
	 * Maps the repair data to a class readable by the report.
	 * 
	 * @param site
	 * @param repairList
	 * @return
	 */
	public RepairReport createRepairHistoryReportData(final BuildingSite site, final List<ServiceHistory> repairList) {
		final RepairReport data = new RepairReport();
		if (site != null) {
			final String STATUS_OK = "Ok";
			final String STATUS_FAULTY = "Mangelhaft";
			final String STATUS_DEFECT = "Defekt";
			// Set header data.
			data.setBuildingSite(site.getName());
			data.setBuildingSiteCode(site.getCode());
			final List<SubRepairElementReport> repairs = new ArrayList<>();
			final ArrayList<Long> excludeIds = new ArrayList<>();
			// Set sub-report-data.
			if (repairList != null && !repairList.isEmpty()) {
				for (ServiceHistory repair : repairList) {
					// Create entry.
					final SubRepairReport srr = new SubRepairReport();
					srr.setComment(repair.getDescription());

					if (repair.getCheckListItem() != null) {
						srr.setItem(repair.getCheckListItem().getName());
					}
					// Check if entry for element already exists
					Boolean elementInList = false;
					for (SubRepairElementReport entry : repairs) {
						if (entry.getElement().equals(repair.getElement().getElementnumber())) {
							// Element entry found, check if date entry exists for element.
							Boolean dateInList = false;
							elementInList = true;
							for (SubRepairDateReport sdr : entry.getDates()) {
								if (sdr.getStartTime().equals(repair.getStartTime())) {
									// Date found, insert entry for existing date and element.
									dateInList = true;
									sdr.getRepairs().add(srr);
									// OK < FAULTY < DEFECT
									if (sdr.getStatus() != STATUS_DEFECT) {
										if (sdr.getStatus().equals(STATUS_FAULTY) && repair.getCheckedAsDefect()) {
											sdr.setStatus(STATUS_DEFECT);
										} else {
											if (repair.getCheckedAsDefect()) {
												sdr.setStatus(STATUS_DEFECT);
											} else if (repair.getCheckedAsLack()) {
												sdr.setStatus(STATUS_FAULTY);
											}
										}
									}
									break;
								}
							}
							if (!dateInList) {
								// Date not set for element, creating entry...
								final SubRepairDateReport sdr = new SubRepairDateReport();
								sdr.setStartTime(repair.getStartTime());
								if (repair.getStartTime() != null) {
									sdr.setTime(repair.getStartTime().toString());
								}
								sdr.setRepairs(new ArrayList<SubRepairReport>());
								sdr.getRepairs().add(srr);
								// Set status.
								if (repair.getCheckedAsDefect()) {
									sdr.setStatus(STATUS_DEFECT);
								} else if (repair.getCheckedAsLack()) {
									sdr.setStatus(STATUS_FAULTY);
								} else {
									sdr.setStatus(STATUS_OK);
								}
								// Fetch calculated time.
								final ArrayList<TimeRecordingHistory> repairTimes = new ArrayList<>();
								repairTimes.addAll(this.timeRecordingHistoryFacade.findRepairTimesForServiceHistory(
										repair.getElement().getId(), repair.getService().getId(),
										repair.getDescription()));
								if (!repairTimes.isEmpty()) {
									for (TimeRecordingHistory time : repairTimes) {
										if (!excludeIds.contains(time.getId()) && time.getTimeInSeconds() != null) {
											sdr.setUsedTime(time.getTimeInSeconds().toString());
											try {
												final Integer hours = time.getTimeInSeconds() / 3600;
												Integer seconds = time.getTimeInSeconds() - (hours * 3600);
												final Integer minutes = seconds / 60;
												seconds -= (minutes * 60);
												sdr.setUsedTime(String.format("%02d", hours) + ":"
														+ String.format("%02d", minutes) + ":"
														+ String.format("%02d", seconds));
											} catch (Exception e) {
												sdr.setUsedTime("00:00:00");
											}
											// Fetch signature
											FileArchive signature = null;
											// Check for signature attached to history entry.
											if(repair.getId() != null)
											{
												signature = this.fileArchiveFacade.findApprovalSignForHistory(repair.getId());
											}
											// No directly assigned signature found, check by time recordings.
											if(signature == null || signature.getId() == null)
											{
												signature = this.fileArchiveFacade
														.findApprovalSignForTimeRecording(time.getId());
											}
											if (signature != null) {
												srr.setSignature(signature.getUrl());
												srr.setCustomer(signature.getDescription());
											}
											
											excludeIds.add(time.getId());
											break;
										}
									}
								} else {
									sdr.setUsedTime("00:00:00");
								}
								entry.getDates().add(sdr);
							}
						}
					}
					if (!elementInList) {
						// Element not found, creating entry...
						final SubRepairElementReport ser = new SubRepairElementReport();
						ser.setElement(repair.getElement().getElementnumber());
						ser.setDates(new ArrayList<SubRepairDateReport>());
						// Create first date entry.
						final SubRepairDateReport sdr = new SubRepairDateReport();
						// Set status.
						if (repair.getCheckedAsDefect()) {
							sdr.setStatus(STATUS_DEFECT);
						} else if (repair.getCheckedAsLack()) {
							sdr.setStatus(STATUS_FAULTY);
						} else {
							sdr.setStatus(STATUS_OK);
						}
						sdr.setStartTime(repair.getStartTime());
						if (repair.getStartTime() != null) {
							sdr.setTime(repair.getStartTime().toString());
						}
						sdr.setRepairs(new ArrayList<SubRepairReport>());
						sdr.getRepairs().add(srr);

						ser.getDates().add(sdr);
						// Fetch calculated time.
						final ArrayList<TimeRecordingHistory> repairTimes = new ArrayList<>();
						repairTimes.addAll(this.timeRecordingHistoryFacade.findRepairTimesForServiceHistory(
								repair.getElement().getId(), repair.getService().getId(), repair.getDescription()));
						if (!repairTimes.isEmpty()) {
							for (TimeRecordingHistory time : repairTimes) {
								if (!excludeIds.contains(time.getId()) && time.getTimeInSeconds() != null) {
									sdr.setUsedTime(time.getTimeInSeconds().toString());
									try {
										final Integer hours = time.getTimeInSeconds() / 3600;
										Integer seconds = time.getTimeInSeconds() - (hours * 3600);
										final Integer minutes = seconds / 60;
										seconds -= (minutes * 60);
										sdr.setUsedTime(
												String.format("%02d", hours) + ":" + String.format("%02d", minutes)
														+ ":" + String.format("%02d", seconds));
									} catch (Exception e) {
										sdr.setUsedTime("00:00:00");
									}
									// Fetch signature
									FileArchive signature = null;									
									// Check for signature attached to history entry.
									if(repair.getId() != null)
									{
										signature = this.fileArchiveFacade.findApprovalSignForHistory(repair.getId());
									}
									// No directly assigned signature found, check by time recordings.
									if(signature == null || signature.getId() == null)
									{
										signature = this.fileArchiveFacade
												.findApprovalSignForTimeRecording(time.getId());
									}
									if (signature != null) {
										srr.setSignature(signature.getUrl());
										srr.setCustomer(signature.getDescription());
									}
									excludeIds.add(time.getId());
									break;
								}
							}
						} else {
							sdr.setUsedTime("00:00:00");
						}
						repairs.add(ser);
					}
					// No signature found, check for repair app signature.
					if (srr.getSignature() == null || srr.getSignature().isEmpty()) {
						if (repair.getService() != null && repair.getService().getId() != null) {
							final FileArchive signature = this.fileArchiveFacade
									.findApprovalSignForService(repair.getService().getId(), repair.getStartTime());
							if (signature != null) {
								srr.setSignature(signature.getUrl());
								srr.setCustomer(signature.getDescription());
							} else {
								// No signature found, fetch service signature.
								final FileArchive serviceSignature = this.fileArchiveFacade
										.findApprovalSignForService(repair.getService().getId());
								if (serviceSignature != null) {
									srr.setSignature(serviceSignature.getUrl());
									srr.setCustomer(serviceSignature.getDescription());
								} else {
									srr.setSignature(null);
									srr.setCustomer("Keine Unterschrift");
								}
							}
						} else {							
							srr.setSignature(null);
							srr.setCustomer("Keine Unterschrift");
						}

					}
				}

			}
			data.setSubRepair(repairs);
		}
		return data;
	}

	@Override
	public List<RegiezeitenReport> createReportTimePayed(List<TimeRecordingHistory> rList) {
		List<RegiezeitenReport> result = mapTimeHistToRegieReport(rList);
		return result;
	}

	@Override
	public List<Zertifikat> createReportCertificate(List<Zertifikat> zertifikat) {
		return zertifikat;
	}

	@Override
	public List<AbnahmeBericht> createReportApproval(List<AbnahmeBericht> abnahmeBericht) {
		return abnahmeBericht;
	}

	@Override
	public List<UebersichtVertragGesamt> createReportOverviewContractTotal(List<UebersichtVertragGesamt> uList) {
		return uList;
	}

	@Override
	public List<PrintHistoryMaster> createReportPrintHistory(List<PrintHistoryMaster> pList) {
		return pList;
	}

}
