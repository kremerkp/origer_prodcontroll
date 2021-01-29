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
import lu.origer.serviceagree.backend.contract.ServiceTypeFacade;
import lu.origer.serviceagree.frontend.main.BasicFormBean;
import lu.origer.serviceagree.frontend.main.Constants;
import lu.origer.serviceagree.models.checklist.Checklist;
import lu.origer.serviceagree.models.checklist.ChecklistItem;
import lu.origer.serviceagree.models.contract.ServiceType;

@ManagedBean
@ViewScoped
public class ServiceTypeEditBean extends BasicFormBean<ServiceType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String LOG_REPORTER = "ChecklistEditBean";

	@Inject
	ServiceTypeFacade serviceTypeFacade;

	private ServiceType serviceType;
	
	private Boolean disabled = false; 


	@PostConstruct
	public void init() {
		Integer id = getApplicationBean().getIdFromURL();
		if (id == null || id == -1) {
			serviceType = new ServiceType();
			serviceType.setActive(true);
		} else {
			serviceType = serviceTypeFacade.find(id);
			disabled = serviceTypeFacade.serviceTypeReferenceExists(id);
		}
	}

	@Override
	public void save() {
		if (Faces.getContext().getMessageList().isEmpty()) {
			// No Errors occured
			if (serviceType.getId() != null) {
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
			Faces.redirect("faces/origer/serveAgree/admin/service_types/service_types_list.xhtml");
		} catch (IOException ex) {
			Logger.getLogger(ChecklistItemCategoryEditBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@Override
	protected void create() {
		
		serviceType.setCreateDate(new Date());
		serviceType.setEditDate(new Date());
		serviceType.setCreateUser(getSessionBean().getCurrentUser().getId());
		serviceType.setEditUser(getSessionBean().getCurrentUser().getId());
		serviceTypeFacade.create(serviceType);

	}

	@Override
	protected void update() {
		// Update checklistItemCategory
		serviceType.setEditUser(getSessionBean().getCurrentUser().getId());
		serviceType.setEditDate(new Date());
		serviceTypeFacade.edit(serviceType);
	}

	@Override
	protected void checkInput() {
		// TODO Auto-generated method stub

	}

	public ServiceType getServiceType() {
		return serviceType;
	}

	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}
	
	

}
