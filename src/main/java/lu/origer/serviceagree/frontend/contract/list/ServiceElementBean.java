package lu.origer.serviceagree.frontend.contract.list;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import lu.origer.serviceagree.backend.contract.ServiceElementFacade;
import lu.origer.serviceagree.frontend.main.BasicBean;
import lu.origer.serviceagree.frontend.report.ReportGeneratorBean;
import lu.origer.serviceagree.models.checklist.ServiceElements;
import lu.origer.serviceagree.models.contract.Service;

@SuppressWarnings("rawtypes")
@ManagedBean
@ViewScoped
public class ServiceElementBean  extends BasicBean{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject 
	ServiceElementFacade serviceElementFacade; 
	
	List<ServiceElements> serviceElements; 
	
	List<ServiceElements> selectedServiceElements;
	
	List<ServiceElements> filteredServiceElements;
	
	
    @PostConstruct
    public void init() {
    	//serviceElements = serviceElementFacade.findAllServiceElementsForPrintingBarcode();
    	serviceElements = serviceElementFacade.findAllServiceElementsForPrintingBarcode();
    	serviceElements = new ArrayList<>(serviceElements);
    }
    
    
	public void setBarcodesPrinted() {
		if(selectedServiceElements != null && selectedServiceElements.size() > 0 ){			
			resetElementList();
	    	serviceElements = serviceElementFacade.findAllServiceElementsForPrintingBarcode();
	    	serviceElements = new ArrayList<>(serviceElements);
		}
	}
    
    
	public void generateBarcodeReport() throws IOException, InterruptedException, SQLException {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ReportGeneratorBean reportGeneratorBean = (ReportGeneratorBean) FacesContext.getCurrentInstance()
				.getApplication().getELResolver().getValue(elContext, null, "reportGeneratorBean");
		if(selectedServiceElements != null && selectedServiceElements.size() > 0 ){			
			reportGeneratorBean.generateBarcodeReport(selectedServiceElements);
			// resetElementList();
		}
	}
    
    public void print(){
    	
    }
    
    public void resetElementList(){
    	serviceElementFacade.setResetElementBarcode(selectedServiceElements);
    }

	public List<ServiceElements> getServiceElements() {
		return serviceElements;
	}

	public void setServiceElements(List<ServiceElements> serviceElements) {
		this.serviceElements = serviceElements;
	}

	public List<ServiceElements> getFilteredServiceElements() {
		return filteredServiceElements;
	}

	public void setFilteredServiceElements(List<ServiceElements> filteredServiceElements) {
		this.filteredServiceElements = filteredServiceElements;
	}

	public List<ServiceElements> getSelectedServiceElements() {
		return selectedServiceElements;
	}

	public void setSelectedServiceElements(List<ServiceElements> selectedServiceElements) {
		this.selectedServiceElements = selectedServiceElements;
	}
	
	

}
