/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lu.origer.serviceagree.frontend.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.omnifaces.util.Faces;

import lu.origer.serviceagree.backend.contract.ServiceElementFacade;
import lu.origer.serviceagree.backend.reporting.ReportFacade;
import lu.origer.serviceagree.frontend.main.ApplicationBean;
import lu.origer.serviceagree.frontend.main.BasicBean;
import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contract.BuildingSite;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.reporting.AbnahmeBericht;
import lu.origer.serviceagree.models.reporting.BarcodeReport;
import lu.origer.serviceagree.models.reporting.PrintHistoryMaster;
import lu.origer.serviceagree.models.reporting.RegiezeitenReport;
import lu.origer.serviceagree.models.reporting.RepairReport;
import lu.origer.serviceagree.models.reporting.UebersichtVertragGesamt;
import lu.origer.serviceagree.models.reporting.Zertifikat;
import lu.origer.serviceagree.models.synch.ServiceHistory;
import lu.origer.serviceagree.models.synch.TimeRecordingHistory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

/**
 *
 * @author fred.freres
 */
@Named(value = "reportGeneratorBean")
@ViewScoped
public class ReportGeneratorBean extends BasicBean {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private Date fromdate;
	private byte[] content;
	private Date enddate;

	@Inject
	ReportFacade reportFacade;

	@Inject
	ServiceElementFacade serviceElementFacade;

	public ReportGeneratorBean() {
	}

	public void generateTestReport() {

	}
	
	public void generateHistReport(List<PrintHistoryMaster> histMasterList, Locale userLocale) throws SQLException, IOException {
		InputStream path;
		InputStream subPath;
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		String outPdfName = "Historie.pdf";
		path = getClass().getResourceAsStream("/reports/printHist.jrxml");
		subPath = getClass().getResourceAsStream("/reports/printHist_elementTable.jrxml");

		try {
			getConnection();
			HashMap<String, Object> params = new HashMap<>();
			params.put(JRParameter.REPORT_LOCALE, userLocale);
			
			List<PrintHistoryMaster> reportData = reportFacade.createReportPrintHistory(histMasterList);
			
			JasperReport barcodes = JasperCompileManager.compileReport(path);
			JasperReport sub1 = JasperCompileManager.compileReport(subPath);
			params.put("sub1", sub1);
			JasperPrint jasperPrint = JasperFillManager.fillReport(barcodes, params,
					new JRBeanArrayDataSource(reportData.toArray()));
			connection.close();
			content = JasperExportManager.exportReportToPdf(jasperPrint);
			Faces.sendFile(content, outPdfName, false);

		} catch (JRException ex) {
			Logger.getLogger(ReportGeneratorBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void generateHistReportDetail(List<PrintHistoryMaster> histMasterList, Locale userLocale, final String documentName) throws SQLException, IOException{
		InputStream path;
		InputStream subPath;
		InputStream subPath2; 
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		String outPdfName = "Historie.pdf";
		
		if(documentName != null && !documentName.isEmpty())
		{
			outPdfName = documentName;
		}		
		path = getClass().getResourceAsStream("/reports/printHist_subChecklist.jrxml");
		subPath = getClass().getResourceAsStream("/reports/printHist_elementTable_subChecklist.jrxml");
		subPath2 = getClass().getResourceAsStream("/reports/printHist_checklistElement.jrxml");

		try {
			getConnection();
			HashMap<String, Object> params = new HashMap<>();
			params.put(JRParameter.REPORT_LOCALE, userLocale);					
			
			List<PrintHistoryMaster> reportData = reportFacade.createReportPrintHistory(histMasterList);
			
			JasperReport barcodes = JasperCompileManager.compileReport(path);
			JasperReport sub1 = JasperCompileManager.compileReport(subPath);
			JasperReport sub2 = JasperCompileManager.compileReport(subPath2);
			params.put("sub1", sub1);
			params.put("sub2", sub2);
			JasperPrint jasperPrint = JasperFillManager.fillReport(barcodes, params,
					new JRBeanArrayDataSource(reportData.toArray()));
			connection.close();
			content = JasperExportManager.exportReportToPdf(jasperPrint);
			Faces.sendFile(content, outPdfName, false);

		} catch (JRException ex) {
			Logger.getLogger(ReportGeneratorBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void generateApprovalReport(List<AbnahmeBericht> abnahmebericht, Locale userLocale)throws IOException, InterruptedException, SQLException { 
		InputStream path;
		InputStream subPath;
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		String outPdfName = "Abnahmeprotokoll.pdf";
		path = getClass().getResourceAsStream("/reports/proof.jrxml");
		subPath = getClass().getResourceAsStream("/reports/subproof.jrxml");

		try {
			getConnection();
			HashMap<String, Object> params = new HashMap<>();
			params.put(JRParameter.REPORT_LOCALE, userLocale);
			List<AbnahmeBericht> reportData = reportFacade.createReportApproval(abnahmebericht); 
			JasperReport barcodes = JasperCompileManager.compileReport(path);
			JasperReport sub1 = JasperCompileManager.compileReport(subPath);
			params.put("sub1", sub1);
			JasperPrint jasperPrint = JasperFillManager.fillReport(barcodes, params,
					new JRBeanArrayDataSource(reportData.toArray()));
			connection.close();
			content = JasperExportManager.exportReportToPdf(jasperPrint);
			Faces.sendFile(content, outPdfName, false);

		} catch (JRException ex) {
			Logger.getLogger(ReportGeneratorBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void generateCertification(List<Zertifikat> zertifikat, Locale userLocale) throws IOException, InterruptedException, SQLException {
		InputStream path;
		InputStream subPath;
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		String outPdfName = "Wartungszertifikat-Origer.pdf";
		path = getClass().getResourceAsStream("/reports/cert.jrxml");
		subPath =getClass().getResourceAsStream("/reports/sub_cert.jrxml"); 

		try {
			getConnection();
			HashMap<String, Object> params = new HashMap<>();
			params.put(JRParameter.REPORT_LOCALE, userLocale);
			List<Zertifikat> reportData = reportFacade.createReportCertificate(zertifikat); 
			JasperReport barcodes = JasperCompileManager.compileReport(path);
			JasperReport sub1 = JasperCompileManager.compileReport(subPath);
			params.put("sub1", sub1);
			JasperPrint jasperPrint = JasperFillManager.fillReport(barcodes, params,
					new JRBeanArrayDataSource(reportData.toArray()));
			connection.close();
			content = JasperExportManager.exportReportToPdf(jasperPrint);
			Faces.sendFile(content, outPdfName, false);

		} catch (JRException ex) {
			Logger.getLogger(ReportGeneratorBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public void generateGesamtuebersicht(List<UebersichtVertragGesamt> uList)  throws IOException, InterruptedException, SQLException {
		InputStream path;
		InputStream subPath1;
		InputStream subPath11;
		InputStream subPath2;
		InputStream subPath3;
		InputStream subPath4;
		InputStream subPath5;
		InputStream subPath6;
		InputStream subPath7;
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		String outPdfName = "Gesamtuebersicht.pdf";
		path = getClass().getResourceAsStream("/reports/gesamt.jrxml");
		//subPath1 = getClass().getResourceAsStream("/reports/gesamt_sub_bilanz.jrxml");
		subPath11 = getClass().getResourceAsStream("/reports/sub.jrxml");
		subPath2 = getClass().getResourceAsStream("/reports/gesamt_sub_regie.jrxml");
		subPath3 = getClass().getResourceAsStream("/reports/gesamt_sub_bilanz_chart.jrxml");
		subPath4 = getClass().getResourceAsStream("/reports/gesamt_sub_bilanz_MOD1.jrxml");
		subPath5 = getClass().getResourceAsStream("/reports/gesamt_sub_bilanz_MOD2.jrxml");
		subPath6 = getClass().getResourceAsStream("/reports/gesamt_sub_bilanz_MOD3.jrxml");
		subPath7 = getClass().getResourceAsStream("/reports/gesamt_sub_rechnung.jrxml");

		try {
			getConnection();
			HashMap<String, Object> params = new HashMap<>();
			params.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);
			List<UebersichtVertragGesamt> reportData = reportFacade.createReportOverviewContractTotal(uList); 
			JasperReport barcodes = JasperCompileManager.compileReport(path);
			//JasperReport sub1 = JasperCompileManager.compileReport(subPath1);
			JasperReport sub11 = JasperCompileManager.compileReport(subPath11);
			JasperReport sub2 = JasperCompileManager.compileReport(subPath2);
			JasperReport sub3 = JasperCompileManager.compileReport(subPath3);
			JasperReport sub4 = JasperCompileManager.compileReport(subPath4);
			JasperReport sub5 = JasperCompileManager.compileReport(subPath5);
			JasperReport sub6 = JasperCompileManager.compileReport(subPath6);
			JasperReport sub7 = JasperCompileManager.compileReport(subPath7);
			params.put("sub11", sub11);
			//params.put("sub1", sub1);			
			params.put("sub2", sub2);
			params.put("sub3", sub3);
			params.put("sub4", sub4);
			params.put("sub5", sub5);
			params.put("sub6", sub6);
			params.put("sub7", sub7);
			JasperPrint jasperPrint = JasperFillManager.fillReport(barcodes, params,
					new JRBeanArrayDataSource(reportData.toArray()));
			connection.close();
			content = JasperExportManager.exportReportToPdf(jasperPrint);
			Faces.sendFile(content, outPdfName, false);

		} catch (JRException ex) {
			Logger.getLogger(ReportGeneratorBean.class.getName()).log(Level.SEVERE, null, ex);			
		}
	}
	
	public void generateTimeRecordingPayedXLS(List<TimeRecordingHistory> rList) throws IOException, InterruptedException, SQLException {
		InputStream path;
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		String name = "Regiezeiten-Origer.xls";
		path = getClass().getResourceAsStream("/reports/regie_xls.jrxml");

		try {
			getConnection();
			HashMap<String, Object> params = new HashMap<>();
			params.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);
			List<RegiezeitenReport> reportData = reportFacade.createReportTimePayed(rList); 
			JasperReport barcodes = JasperCompileManager.compileReport(path);
			List<JasperPrint> jasperPrintList = new ArrayList<JasperPrint>();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			JasperPrint jasperPrint = JasperFillManager.fillReport(barcodes, params,
					new JRBeanArrayDataSource(reportData.toArray()));
			connection.close();
			jasperPrintList.add(jasperPrint);
			JRXlsxExporter Xlsxexporter = new JRXlsxExporter();
			Xlsxexporter.setExporterInput(SimpleExporterInput.getInstance(jasperPrintList));
			Xlsxexporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
			Xlsxexporter.exportReport();
			Faces.sendFile(out.toByteArray(), name, false);

		} catch (JRException ex) {
			Logger.getLogger(ReportGeneratorBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	/**
	 * The current active function for repair report generation.
	 * 
	 * @param site
	 * @param repairList
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public void generateRepairHistoryReport(final BuildingSite site, final List<ServiceHistory> repairList) throws IOException, InterruptedException, SQLException
	{
		InputStream path;
		InputStream subPath;
		InputStream subPath2;
		InputStream subPath3;
		String outPdfName = "Reparaturbericht-Origer.pdf";
		path = getClass().getResourceAsStream("/reports/repair.jrxml");
		subPath =getClass().getResourceAsStream("/reports/subRepairElement.jrxml");
		subPath2 =getClass().getResourceAsStream("/reports/subRepairDate.jrxml"); 
		subPath3 =getClass().getResourceAsStream("/reports/subRepair.jrxml"); 		

		try {
			getConnection();
			HashMap<String, Object> params = new HashMap<>();	
			List<RepairReport> data = new ArrayList<>();
			data.add(reportFacade.createRepairHistoryReportData(site, repairList));
			JasperReport report = JasperCompileManager.compileReport(path);
			JasperReport sub1 = JasperCompileManager.compileReport(subPath);
			JasperReport sub2 = JasperCompileManager.compileReport(subPath2);
			JasperReport sub3 = JasperCompileManager.compileReport(subPath3);
			params.put("sub1", sub1);
			params.put("sub2", sub2);
			params.put("sub3", sub3);
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, params,
					new JRBeanArrayDataSource(data.toArray()));
			connection.close();
			content = JasperExportManager.exportReportToPdf(jasperPrint);
			Faces.sendFile(content, outPdfName, false);

		} catch (JRException ex) {
			Logger.getLogger(ReportGeneratorBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void generateTimeRecordingPayed(List<TimeRecordingHistory> rList) throws IOException, InterruptedException, SQLException {
		InputStream path;
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		String outPdfName = "Regiezeiten-Origer.pdf";
		path = getClass().getResourceAsStream("/reports/regie.jrxml");

		try {
			getConnection();
			HashMap<String, Object> params = new HashMap<>();
			params.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);
			List<RegiezeitenReport> reportData = reportFacade.createReportTimePayed(rList); 
			JasperReport barcodes = JasperCompileManager.compileReport(path);
			JasperPrint jasperPrint = JasperFillManager.fillReport(barcodes, params,
					new JRBeanArrayDataSource(reportData.toArray()));
			connection.close();
			content = JasperExportManager.exportReportToPdf(jasperPrint);
			Faces.sendFile(content, outPdfName, false);

		} catch (JRException ex) {
			Logger.getLogger(ReportGeneratorBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void generateBarcodeReport(List<ServiceElements> sList)
			throws IOException, InterruptedException, SQLException {
		InputStream path;
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
				.getResponse();
		String outPdfName = "Barcodes-Origer.pdf";
		path = getClass().getResourceAsStream("/reports/barcodes.jrxml");

		try {
			getConnection();
			HashMap<String, Object> params = new HashMap<>();
			params.put(JRParameter.REPORT_LOCALE, Locale.GERMAN);
			List<BarcodeReport> reportData = reportFacade.createReportBarcodes(sList);
			JasperReport barcodes = JasperCompileManager.compileReport(path);
			JasperPrint jasperPrint = JasperFillManager.fillReport(barcodes, params,
					new JRBeanArrayDataSource(reportData.toArray()));
			connection.close();
			content = JasperExportManager.exportReportToPdf(jasperPrint);
			Faces.sendFile(content, outPdfName, false);

		} catch (JRException ex) {
			Logger.getLogger(ReportGeneratorBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@PostConstruct
	public void init() {
		this.fromdate = ApplicationBean.setDateToBeginOfYear(new Date());
		this.enddate = ApplicationBean.setDateToEndOfYear(new Date());
		Logger.getLogger(this.getClass().getName()).log(Level.INFO, "Init ReportGeneratorBean:");
	}

	private Connection getConnection() {
		try {
			InitialContext ctx = new InitialContext();
			DataSource ds = (DataSource) ctx.lookup("java:comp/env/origer_pc_prod");

			connection = ds.getConnection();
		} catch (NamingException | SQLException ex) {
			Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
		}

		return connection;
	}

	public Date getEnddate() {
		return enddate;
	}

	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	public Date getFromdate() {
		return fromdate;
	}

	public void setFromdate(Date fromdate) {
		this.fromdate = fromdate;
	}


}
