package lu.origer.serviceagree.frontend.main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;

import org.apache.commons.lang3.SystemUtils;
import org.omnifaces.util.Faces;

import lu.origer.serviceagree.backend.main.ConfigFacade;
import lu.origer.serviceagree.backend.main.RolesFacade;
import lu.origer.serviceagree.backend.main.UsersFacade;
import lu.origer.serviceagree.models.main.Roles;
import lu.origer.serviceagree.models.main.Users;

@Named
@RequestScoped
public class UsersBean extends BasicBean {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String LOG_REPORTER = "Benutzerverwaltung";

    Users user;
    @Inject
    UsersFacade usersFacade;
    @Inject 
    ConfigFacade configFacade;
    @Inject
    LanguageBean languageBean;
    @Inject
    SessionBean sessionBean;

    /**
     * Creates a new instance of UsersBean
     *
     * @throws java.io.IOException
     */
    public UsersBean() throws IOException {
    }
    
    public void doSomething() {  
        try {  
            // simulate a long running request  
            Thread.sleep(2000);  
        } catch (Exception e) {  
            // ignore  
        }  
    }  

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
    
    public Boolean userIsAdmin(Users user){
    	List<Roles> rList = user.getRolesList(); 
    	for (Roles r : rList){
    		if(r.getRoleName().equals(RolesFacade.ADMIN)){
    			return true;
    		}
    	}
    	return false; 
    }
    
    public void setFilePaths(){
    	if (SystemUtils.IS_OS_WINDOWS){
    		getApplicationBean().setPathToFileArchiv(configFacade.find(ConfigFacade.WIN_FILE_PATH).getConfigValue());    		
    	} else if (SystemUtils.IS_OS_LINUX){
    		getApplicationBean().setPathToFileArchiv(configFacade.find(ConfigFacade.UNIX_FILE_PATH).getConfigValue());
    	} 
    }
    
    public Boolean userIsOnlyCustomer (Users user){
    	Boolean userIsOnlyCustomer = false;
    	List<Roles> rList =  user.getRolesList();
    	if(rList != null)
    	{
    		System.out.println("ROLES CHECK: " + rList.size());
    	}
    	 for(Roles r : rList){
         	if (rList.size() == 1){
             	if (r.getRoleName().equals(RolesFacade.CUSTOMER_NAME)){
             		userIsOnlyCustomer = true; 
             	}        		
         	} 
         }
    	 return userIsOnlyCustomer;
    }

    @PostConstruct
    public void init() {
        getApplicationBean(); // Trigger Init after restart
        user = new Users();
        FacesContext.getCurrentInstance().getViewRoot().setLocale(sessionBean.getUserLocale());
    }

    public void checkForValidSession() {
        if (Faces.getApplication().getProjectStage().toString().equals("Development123")) {
            try {
                Faces.getRequest().login("admin", "admin");
                Users usr = usersFacade.findByUsername("admin");
                getSessionBean().setCurrentUser(usr);
                getSessionBean().setIsAdmin(userIsAdmin(usr));
                setFilePaths();
                Faces.redirect("faces/origer/serveAgree/navigation/tile-start.xhtml");
                Faces.responseComplete();
            } catch (IOException | ServletException ex) {
            	getApplicationBean().logging(LOG_REPORTER, "Redirect failed!", Constants.MessageLevel.ERROR);
            }
        } else {
            if (Faces.getRemoteUser() != null) {
                try {
                	Faces.redirect("faces/origer/serveAgree/navigation/tile-start.xhtml");
                } catch (IOException ex) {
                    getApplicationBean().logging(LOG_REPORTER, "Redirect failed!", Constants.MessageLevel.ERROR);
                }
            }
        }
    }

    public void login() throws IOException {
        try {
            Faces.getRequest().login(user.getUsername(), user.getPassword());
        } catch (ServletException e) {
            String message = "Login failed for user [" + user.getUsername() + "]";
            getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.ERROR);
            Faces.validationFailed();
            Faces.getFlash().put("error", true);
            return;
        }

        String message = "Login success for user [" + user.getUsername() + "]";
        getApplicationBean().logging(LOG_REPORTER, message, Constants.MessageLevel.INFO);

        Users usr = usersFacade.findByUsername(user.getUsername());
        //Set user language from DB
        if(usr.getLang().equals("FR"))
        {
        	sessionBean.setUserLocale(Locale.FRANCE);
        }
        else
        {
        	sessionBean.setUserLocale(Locale.GERMANY);
        }
        FacesContext.getCurrentInstance().getViewRoot().setLocale(sessionBean.getUserLocale());
        
        getSessionBean().setCurrentUser(usr);
        //Faces.redirect("faces/origer/serveAgree/user/users_account.xhtml");
        Boolean navToCustomer = userIsOnlyCustomer(getSessionBean().getCurrentUser());
        getSessionBean().setIsAdmin(userIsAdmin(usr));        
        
        setFilePaths();       
        if(navToCustomer){
        	System.out.println("NAVIGATING TO CUSTOMER " + usr.getName());
        	Faces.redirect("faces/origer/serveAgree/customer/customer.xhtml");
        } else {
        	System.out.println("NAVIGATING TO ADMIN PANEL" + usr.getName());
        	Faces.redirect("faces/origer/serveAgree/navigation/tile-start.xhtml");        	
        }
        
    }

    public void logout() {
        try {
            Faces.logout();
        } catch (ServletException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
