package lu.origer.serviceagree.frontend.contract.list;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.omnifaces.util.Faces;

import lu.origer.serviceagree.backend.contract.ContractTypeFacade;
import lu.origer.serviceagree.backend.contract.ServiceTypeFacade;
import lu.origer.serviceagree.frontend.main.BasicListBean;
import lu.origer.serviceagree.models.contract.ContractType;
import lu.origer.serviceagree.models.contract.ServiceType;

@SuppressWarnings("rawtypes")
@ManagedBean
@ViewScoped
public class ContractTypeBean extends BasicListBean<ContractType>{
	
	private static final long serialVersionUID = 1L;
	@Inject
	ContractTypeFacade contractTypeFacade;
	

	@Override
	public void filterList() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String add() {
		return "/origer/serveAgree/admin/service_types/contract_types_edit.xhtml?faces-redirect=true";
	}

	@Override
	public String edit() {
		try {
			return "/origer/serveAgree/admin/service_types/contract_types_edit.xhtml?faces-redirect=true&id=" + ((ContractType) selectedData ).getId();			
		} catch (Exception e) {
			// TODO: handle exception
			return ""; 
		}
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}
	
    @PostConstruct
    public void init() {
        data = contractTypeFacade.findAllContractTypes();
        data = new ArrayList<>(data);
    }
    
    public void openServiceType(){
		try {
			Faces.redirect("/origer/faces/origer/serveAgree/admin/service_types/contract_types_edit.xhtml?faces-redirect=true&id=" + ((ContractType) selectedData ).getId());			
		} catch (IOException ex) {
			Logger.getLogger(ChecklistItemCategoryBean.class.getName()).log(Level.SEVERE, null, ex);
		}
    }


}
