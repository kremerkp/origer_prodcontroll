/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lu.origer.serviceagree.backend.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.models.assocs.QAssoc_Users_Roles;
import lu.origer.serviceagree.models.main.QRoles;
import lu.origer.serviceagree.models.main.QUsers;
import lu.origer.serviceagree.models.main.Roles;
import lu.origer.serviceagree.models.main.Users;

/**
 *
 * @author ffreres
 */
@Stateless
public class UsersFacade extends AbstractFacade<Users> {

	public static final Integer ADMIN_ID = 1;
	
	public static final Integer ANDROID_USER_ID = -1;
   

    @PersistenceContext(unitName = "ORIGERPU")
    private EntityManager em;

    public Users findByEmail(String email) {
        try {
            return (Users) em.createQuery("SELECT u FROM Users u WHERE u.email = :email")
                    .setParameter("email", email)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    @SuppressWarnings("unchecked")
	public List<Users> findAllCustomers(String query){
    	
    	QAssoc_Users_Roles ur = QAssoc_Users_Roles.assoc_Users_Roles;  
        
        JPAQuery q = new JPAQuery(this.em);
        q.from(ur); 
        q.where(ur.roles.eq(RolesFacade.CUSTOMER_FLAG)); 
        List<Integer> userList = q.list(ur.user);
        List<Users> usList = new ArrayList<>(); 
        
        for ( Integer i : userList){
        	Users us = this.find(i);
        	if (us != null){        		
        		usList.add(this.find(i));
        	}
        }
        
        return usList;
    }

    public Users findByUsername(String username) {
        QUsers user = QUsers.users;
        JPAQuery q = new JPAQuery(this.em); 
        q.from(user); 
        q.where(user.username.eq(username)); 
        return q.singleResult(user);
        
//        return new JPAQuery(em).from(user)
//                .join(user.rolesList).fetch()
//                .where(user.username.eq(username)).singleResult(user);
    }
    
    public List<Users> findAllActive() {
        QUsers user = QUsers.users;
        JPAQuery q = new JPAQuery(this.em); 
        q.from(user); 
        q.where(user.id.ne(UsersFacade.ANDROID_USER_ID)); 
        return q.list(user);
        
    }
    
    public List<Users> findAllSortByLastnameASC() {
        List<Users> res = new ArrayList<>();
        res.addAll(em.createQuery("SELECT u FROM Users u ORDER BY u.name ASC").getResultList());
        return res;
    }

    @Override
    public List<Users> findAll() {
        List<Users> res = new ArrayList<>();
        res.addAll(em.createQuery("SELECT u FROM Users u ORDER BY u.username ASC").getResultList());
        return res;
    }

    public UsersFacade() {
        super(Users.class);
    }

    @Override
    public Users find(Object id) {
        QUsers user = QUsers.users;
        Users tmpUser = new JPAQuery(em)
                .from(user)
                .where(user.id.eq((Integer) id))
                .singleResult(user);
        if (tmpUser != null ){
        	tmpUser.getRolesList();        	
        }
        return tmpUser;
    }

}
