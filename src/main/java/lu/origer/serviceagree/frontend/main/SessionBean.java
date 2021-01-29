/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lu.origer.serviceagree.frontend.main;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;

import lu.origer.serviceagree.backend.main.UsersFacade;
import lu.origer.serviceagree.models.main.Users;

/**
 *
 * @author ffreres
 */
@Named
@SessionScoped
public class SessionBean implements Serializable {

    @Inject
    ApplicationBean applicationBean;

    @Inject
    private UsersFacade usersFacade;

    private Users currentUser;
    
    private Boolean isAdmin; 

    private Date currentCalendarNavigationDate;

    private Boolean eventOpenendFromBackend = false;
    
    private Locale userLocale;

    /**
     * Creates a new instance of SessionBean
     */
    public SessionBean() {
    }

    public Boolean getEventOpenendFromBackend() {
	return eventOpenendFromBackend;
    }

    public void setEventOpenendFromBackend(Boolean eventOpenendFromBackend) {
	this.eventOpenendFromBackend = eventOpenendFromBackend;
    }

    public Date getCurrentCalendarNavigationDate() {
	return currentCalendarNavigationDate;
    }

    public void setCurrentCalendarNavigationDate(Date currentCalendarNavigationDate) {
	this.currentCalendarNavigationDate = currentCalendarNavigationDate;
    }

    public ApplicationBean getApplicationBean() {
	return applicationBean;
    }

    public void setApplicationBean(ApplicationBean applicationBean) {
	this.applicationBean = applicationBean;
    }

    public Users getCurrentUser() {
	return currentUser;
    }

    public void setCurrentUser(Users currentUser) {
	this.currentUser = currentUser;
    }

    public String logout() {
	Faces.invalidateSession();
	return "/index.xhtml";
    }

    public void refreshUser() {
	currentUser = usersFacade.findByUsername(currentUser.getUsername());
    }

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public Locale getUserLocale()
	{
		return userLocale;
	}

	public void setUserLocale(Locale userLocale)
	{
		this.userLocale = userLocale;
	}
}
