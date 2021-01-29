/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lu.origer.serviceagree.frontend.main;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.omnifaces.util.Faces;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.LoggingFacade;
import lu.origer.serviceagree.backend.main.UsersFacade;
import lu.origer.serviceagree.models.main.Logging;
import lu.origer.serviceagree.models.main.Users;

/**
 *
 * @author Ian Husting
 */
@SuppressWarnings("rawtypes")
@ManagedBean
@ViewScoped
public class UsersLoginListBean extends BasicListBean{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;	
	
	@Inject
	private LoggingFacade loggingFacade;

    /**
     * Creates a new instance of usersLoginList
     */
    public UsersLoginListBean() {

    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void filterList() {
        // No filter needed, list to small
    }

    @Override
    public List<Users> getData() {
        return data;
    }

    @PostConstruct
    public void init() {    	
        data = this.loggingFacade.findLogins();
    }
    
    @Override
    public String edit() {
        return "/origer/serveAgree/admin/user/users_edit.xhtml?faces-redirect=true&id=" + ((Users) selectedData).getId();
    }
    
    @Override
    public String add() {
    	return "/origer/serveAgree/admin/user/users_edit.xhtml?faces-redirect=true";
    }
    
    public void openUser() {
        try {
            Faces.redirect("/origer/faces/origer/serveAgree/admin/user/users_edit.xhtml?faces-redirect=true&id=" + ((Users) selectedData).getId());
        } catch (IOException ex) {
            Logger.getLogger(UsersLoginListBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
