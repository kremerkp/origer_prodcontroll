/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lu.origer.serviceagree.frontend.main;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.codec.digest.DigestUtils;
import org.omnifaces.util.Faces;

import lu.origer.serviceagree.backend.main.UsersFacade;
import lu.origer.serviceagree.models.main.Users;

/**
 *
 * @author ffreres
 */
@Named
@ViewScoped
public class usersAccountBean extends BasicFormBean{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Users user;
    private String newPassword;
    private String confirmNewPassword;
    @Inject
    private UsersFacade usersFacade;

    /**
     * Creates a new instance of usersAccountBean
     */
    public usersAccountBean() {
    }

    @Override
    protected void create() {
        throw new UnsupportedOperationException("Not needed here");
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    @PostConstruct
    public void init() {
        user = getSessionBean().getCurrentUser();
        
    }

    @Override
    public void save() {
        checkInput();
        System.out.println(user.getPrename());
        System.out.println(user.getName());
        System.out.println(user.getEmail());
        System.out.println(newPassword);
        
        if (Faces.getContext().getMessageList().isEmpty()) { // No errors
            if (!newPassword.isEmpty()) {
                user.setPassword(DigestUtils.md5Hex(newPassword));
            }
            user.setUpdateDate(new Date());
            user.setUpdatedBy(getSessionBean().getCurrentUser().getId());
            usersFacade.edit(user);
            Faces.getContext().addMessage("frmUsersAccount:password", new FacesMessage(FacesMessage.SEVERITY_INFO, "Informationen wurden gespeichert", null));
        }
    }

    @Override
    protected void checkInput() {
        if (newPassword.isEmpty() && confirmNewPassword.isEmpty()) { // No Password change wanted
            return;
        }
        if (!newPassword.equals(confirmNewPassword)) {
            String message = "Die beiden Passwörter müssen identisch sein!";
            Faces.getContext().addMessage("frmUsers:password", new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
            Faces.validationFailed();
        }
    }

    @Override
    protected void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
