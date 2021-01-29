package lu.origer.serviceagree.models.reporting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import lu.origer.serviceagree.backend.reporting.ReportFacadeRemote;
import lu.origer.serviceagree.models.checklist.ServiceElements;

public class BarcodeReport implements Serializable {
	
	private String barcode;
	
	private String buildingSite; 

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	} 
	

    public static Collection<BarcodeReport> generateCollection() {
        List<BarcodeReport> res = new ArrayList<>();

        try {
            Properties p = new Properties();

            p.put("java.naming.factory.initial", "org.apache.openejb.client.RemoteInitialContextFactory");
            p.put("java.naming.provider.url", "http://localhost:8080/tomee/ejb");

            InitialContext     ctx          = new InitialContext(p);
            ReportFacadeRemote reportFacade = (ReportFacadeRemote) ctx.lookup("ReportFacadeRemote");
            List<ServiceElements> sList = new ArrayList<>(); 
            ServiceElements s = new ServiceElements(); 
            s.setElementnumber("*fe0000001*");
            sList.add(s);
            res = reportFacade.createReportBarcodes(sList);
        } catch (NamingException ex) {
            System.out.println(ex);
            Logger.getLogger(BarcodeReport.class.getName()).log(Level.SEVERE, null, ex);
        }

        return res;
    }

	public String getBuildingSite() {
		return buildingSite;
	}

	public void setBuildingSite(String buildingSite) {
		this.buildingSite = buildingSite;
	}
    
    

	

}
