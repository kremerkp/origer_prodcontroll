/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lu.origer.serviceagree.frontend.main;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.codec.digest.DigestUtils;
import org.omnifaces.util.Faces;
import org.primefaces.model.DualListModel;

import lu.origer.serviceagree.backend.contract.ServiceContractFacade;
import lu.origer.serviceagree.backend.main.RolesFacade;
import lu.origer.serviceagree.backend.main.UsersFacade;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.main.Roles;
import lu.origer.serviceagree.models.main.Users;

/**
 * 
 * @author kai.kremer
 *
 */
@ManagedBean
@ViewScoped
public class UsersEditBean extends BasicFormBean<Users> {

    private static final String LOG_REPORTER = "Benutzerverwaltung";

    private String oldUsername;
    private String oldEmail;
    private String oldPassword;
    private String confirmPassword;

    private Users user;
    private List<Roles> rolesList;
    private List<ServiceContract> serviceContractList; 
    @Inject
    private UsersFacade usersFacade;
    @Inject
    private RolesFacade rolesFacade;
    @Inject
    private ServiceContractFacade serviceContractFacade;
    
	List<Roles> sourceRoles;
	List<Roles> targetRoles;
	
	List<ServiceContract> sourceServiceContract; 
	List<ServiceContract> targetServiceContract; 
	
	private List<Roles> allroles;

    private DualListModel<Roles> rolesPicklist;
    
    private DualListModel<ServiceContract> serviceContractPickList; 

    /**
     * Creates a new instance of usersEdit
     */
    public UsersEditBean() {
    }

    public DualListModel<Roles> getRolesPicklist() {
        return rolesPicklist;
    }

    public void setRolesPicklist(DualListModel<Roles> rolesPicklist) {
        this.rolesPicklist = rolesPicklist;
    }

    public List<Roles> getRolesList() {
        return rolesList;
    }

    public void setRolesList(List<Roles> rolesList) {
        this.rolesList = rolesList;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
    
    public void initRolesPickList(Boolean isCreate){
    	allroles = rolesFacade.findAll();
    	if (!isCreate){
    		targetRoles = user.getRolesList();
    		sourceRoles = rolesFacade.findAll();
    		List<Roles> sourceCopy = new ArrayList<>();
    		sourceCopy.addAll(sourceRoles);
    		sourceCopy.removeAll(targetRoles);
    		sourceRoles =sourceCopy;
    		rolesPicklist = new DualListModel<>(sourceRoles, targetRoles);
    	} else {
    		sourceRoles = rolesFacade.findAll();
    		targetRoles = new ArrayList<>();
    		rolesPicklist = new DualListModel<>(sourceRoles, targetRoles);
    	}
    	
    }
    
    public void initServiceContractPickList(Boolean isCreate){
    	serviceContractList = serviceContractFacade.findAll();
    	if (!isCreate) {
    		if ( user != null ){
    			targetServiceContract = user.getServiceContractList();    			
    		} else {
    			targetServiceContract = new ArrayList<>();
    		}
			sourceServiceContract = serviceContractList; 
			
			List<ServiceContract> sourceContractCopy = new ArrayList<>(); 
			sourceContractCopy.addAll(sourceServiceContract);
			sourceContractCopy.removeAll(targetServiceContract);
			sourceServiceContract = sourceContractCopy;			
			serviceContractPickList = new DualListModel<>(sourceServiceContract, targetServiceContract);
			//serviceContractList = serviceContractFacade.findAll();
    	} else {
			sourceServiceContract = serviceContractFacade.findAll(); 
			targetServiceContract = new ArrayList<>(); 
			serviceContractPickList = new DualListModel<>(sourceServiceContract, targetServiceContract);
		
    	}
    	
    }

    @PostConstruct
    public void init() {
        Integer id = getApplicationBean().getIdFromURL();
        //initServiceContractPickList(id != null && id != -1);
        if (id != null && id != -1) {
            user = usersFacade.find(id);
            initRolesPickList(false);
            initServiceContractPickList(false);
            if (user.getEmail() != null) {
                oldEmail = user.getEmail();
            } else {
                oldEmail = "";
            }
            oldUsername = user.getUsername();
            oldPassword = user.getPassword();
        } else {
        	initRolesPickList(true);
        	initServiceContractPickList(true);
            user = new Users();
            user.setActive(true);
        }
    }

    @Override
    public void save() {
        checkInput();
        if (Faces.getContext().getMessageList().isEmpty()) {
            // No Errors occured
            if (user.getId() != null) {
                update();
            } else {
                create();
            }
            //Update display language
            if(user.equals(getSessionBean().getCurrentUser()))
            {
	            if(user.getLang().equals("DE"))
	            {
	            	getSessionBean().setUserLocale(Locale.GERMANY);
	            }
	            else
	            {
	            	getSessionBean().setUserLocale(Locale.FRANCE);
	            }
            }            
            getApplicationBean().refreshPersonenList();
        }
    }

    public void saveAndClose() {
        try {
            save();
            Faces.redirect("faces/origer/serveAgree/admin/user/user_list.xhtml");
        } catch (IOException ex) {
            Logger.getLogger(UsersEditBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void create() {
        user.setCreatedBy(getSessionBean().getCurrentUser().getId());
        user.setCreateDate(new Date());
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        assignRoles();
        assignServiceContracts();
        usersFacade.create(user);
        String message = "Neuer Benutzer [" + user.getUsername() + "] wurde angelegt";
        getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.INFO);
    }

    @Override
    public void update() {
        // Update User
    	user.setUpdatedBy(getSessionBean().getCurrentUser().getId());
        user.setUpdateDate(new Date());
        if (user.getPassword().isEmpty()) {
            user.setPassword(oldPassword);
        } else {
            user.setPassword(DigestUtils.md5Hex(user.getPassword()));
        }
        assignRoles();
        user.setServiceContractList(this.serviceContractPickList.getTarget());
        // assignServiceContracts();
        user.setUpdateDate(new Date());
        user.setUpdatedBy(getSessionBean().getCurrentUser().getId());
        usersFacade.edit(user);

        // Log Message
        String message = "Benutzer [" + user.getUsername() + "] wurde aktualisiert";
        getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.INFO);
    }

    private void assignServiceContracts() {
        user.setServiceContractList(serviceContractPickList.getTarget());
    }
    
    private void assignRoles() {
        if (user.getId() != null && user.getId().equals(UsersFacade.ADMIN_ID)) { // Don't modify admin
            user.setRolesList(rolesFacade.findAll());
        } else {
            user.setRolesList(rolesPicklist.getTarget());
        }
    }

    public void verifyEmailUniqueness() {
        Users tmpUser = usersFacade.findByEmail(user.getEmail());
        if (tmpUser != null) {
            String message = "Email wird schon verwendet!";
            Faces.getContext().addMessage("frmUsers:email", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            Faces.validationFailed();
        }
    }

    public void verifyUsernameUniqueness() {
        Users tmpUser = usersFacade.findByUsername(user.getUsername());
        if (tmpUser != null) {
            String message = "Benutzername wird schon verwendet!";
            Faces.getContext().addMessage("frmUsers:username", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            Faces.validationFailed();
        }
    }

    public void verifyPasswords() {
        // Check matching passwords
        if (!user.getPassword().equals(confirmPassword)) {
            String message = "Die beiden Passwörter müssen identisch sein!";
            Faces.getContext().addMessage("frmUsers:password", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            Faces.validationFailed();
        }
    }

    @Override
    public void checkInput() {
        if (user.getId() == null) { // User adding
            verifyUsernameUniqueness();
            verifyPasswords();
            // verifyEmailUniqueness();
        } else { // User updating
            if (!user.getPassword().isEmpty() || !confirmPassword.isEmpty()) {
                verifyPasswords();
            }
            if (oldUsername != null && !oldUsername.equals(user.getUsername())) {
                verifyUsernameUniqueness();
            }
        }
    }


	public List<Roles> getSource() {
		return sourceRoles;
	}

	public void setSource(List<Roles> source) {
		this.sourceRoles = source;
	}

	public List<Roles> getTarget() {
		return targetRoles;
	}

	public void setTarget(List<Roles> target) {
		this.targetRoles = target;
	}

	public List<Roles> getAllroles() {
		return allroles;
	}

	public void setAllroles(List<Roles> allroles) {
		this.allroles = allroles;
	}

	public DualListModel<ServiceContract> getServiceContractPickList() {
		return serviceContractPickList;
	}

	public void setServiceContractPickList(DualListModel<ServiceContract> serviceContractPickList) {
		this.serviceContractPickList = serviceContractPickList;
	}

	public List<ServiceContract> getServiceContractList() {
		return serviceContractList;
	}

	public void setServiceContractList(List<ServiceContract> serviceContractList) {
		this.serviceContractList = serviceContractList;
	}

	public List<ServiceContract> getTargetServiceContract() {
		return targetServiceContract;
	}

	public void setTargetServiceContract(List<ServiceContract> targetServiceContract) {
		this.targetServiceContract = targetServiceContract;
	}
	
	
	

}
