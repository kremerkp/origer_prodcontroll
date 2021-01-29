/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lu.origer.serviceagree.frontend.main;

import java.io.Serializable;

import javax.inject.Inject;

/**
 *
 * @author ffreres
 */
public abstract class BasicBean implements Serializable{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	@Inject
    private SessionBean sessionBean;
    @Inject
    private ApplicationBean applicationBean;

    public BasicBean() {
    }

    public SessionBean getSessionBean() {
        return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    public ApplicationBean getApplicationBean() {
        return applicationBean;
    }

    public void setApplicationBean(ApplicationBean applicationBean) {
        this.applicationBean = applicationBean;
    }

    
    
}
