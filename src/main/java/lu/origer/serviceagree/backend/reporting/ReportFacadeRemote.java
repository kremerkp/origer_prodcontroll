package lu.origer.serviceagree.backend.reporting;

import java.util.List;

import javax.ejb.Remote;

import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.reporting.AbnahmeBericht;
import lu.origer.serviceagree.models.reporting.BarcodeReport;
import lu.origer.serviceagree.models.reporting.PrintHistoryMaster;
import lu.origer.serviceagree.models.reporting.RegiezeitenReport;
import lu.origer.serviceagree.models.reporting.UebersichtVertragGesamt;
import lu.origer.serviceagree.models.reporting.Zertifikat;
import lu.origer.serviceagree.models.synch.TimeRecordingHistory;

@Remote	
public interface ReportFacadeRemote  {
	
	public List<BarcodeReport> createReportBarcodes(List<ServiceElements> elementList);
	
	public List<RegiezeitenReport> createReportTimePayed(List<TimeRecordingHistory> rList);
	
	public List<Zertifikat> createReportCertificate(List<Zertifikat> zertifikat);
	
	public List<AbnahmeBericht> createReportApproval(List<AbnahmeBericht> abnahmeBericht);
	
	public List<UebersichtVertragGesamt> createReportOverviewContractTotal(List<UebersichtVertragGesamt> uList);
	
	public List<PrintHistoryMaster> createReportPrintHistory(List<PrintHistoryMaster> pList);


}
