package lu.origer.serviceagree.frontend.contract.edit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.omnifaces.util.Faces;
import org.primefaces.model.DualListModel;

import lu.origer.serviceagree.backend.contract.ChecklistFacade;
import lu.origer.serviceagree.backend.contract.ChecklistItemFacade;
import lu.origer.serviceagree.backend.contract.ContractTypeFacade;
import lu.origer.serviceagree.backend.contract.ServiceTypeFacade;
import lu.origer.serviceagree.frontend.main.BasicFormBean;
import lu.origer.serviceagree.frontend.main.Constants;
import lu.origer.serviceagree.models.checklist.Checklist;
import lu.origer.serviceagree.models.checklist.ChecklistItem;
import lu.origer.serviceagree.models.contract.ContractType;
import lu.origer.serviceagree.models.contract.ServiceType;

@ManagedBean
@ViewScoped
public class ContractTypeEditBean extends BasicFormBean<ContractType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String LOG_REPORTER = "ChecklistEditBean";

	@Inject
	ContractTypeFacade contractTypeFacade;

	private ContractType contractType;
	
	//private Boolean disabled = false; 


	@PostConstruct
	public void init() {
		Integer id = getApplicationBean().getIdFromURL();
		if (id == null || id == -1) {
			contractType = new ContractType();
			contractType.setActive(true);
		} else {
			contractType = contractTypeFacade.find(id);
			//disabled = contractTypeFacade.serviceTypeReferenceExists(id);
		}
	}

	@Override
	public void save() {
		if (Faces.getContext().getMessageList().isEmpty()) {
			// No Errors occured
			if (contractType.getId() != null) {
				update();
			} else {
				create();
			}
		}

	}

	public void saveAndClose() {
		// save();
		try {
			save();
			Faces.redirect("faces/origer/serveAgree/admin/service_types/contract_types_list.xhtml");
		} catch (IOException ex) {
			Logger.getLogger(ChecklistItemCategoryEditBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	protected void create() {
		
		contractType.setCreateDate(new Date());
		contractType.setEditDate(new Date());
		contractType.setCreateUser(getSessionBean().getCurrentUser().getId());
		contractType.setEditUser(getSessionBean().getCurrentUser().getId());
		contractTypeFacade.create(contractType);

	}

	@Override
	protected void update() {
		// Update checklistItemCategory
		contractType.setEditUser(getSessionBean().getCurrentUser().getId());
		contractType.setEditDate(new Date());
		contractTypeFacade.edit(contractType);
	}

	@Override
	protected void checkInput() {
		// TODO Auto-generated method stub

	}


	
	public ContractType getContractType() {
		return contractType;
	}

	public void setContractType(ContractType contractType) {
		this.contractType = contractType;
	}

//	public Boolean getDisabled() {
//		return disabled;
//	}
//
//	public void setDisabled(Boolean disabled) {
//		this.disabled = disabled;
//	}
//	
	

}
