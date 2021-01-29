/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lu.origer.serviceagree.backend.main;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.models.main.QRoles;
import lu.origer.serviceagree.models.main.Roles;

/**
 *
 * @author ffreres
 */
@Stateless
public class RolesFacade extends AbstractFacade<Roles> {
    
    public static final Integer USER_FLAG = 1;
    public static final String USER = "user";
    public static final Integer ADMIN_FLAG = 2;
    public static final String ADMIN = "admin";
    public static final Integer AUTHOR_FLAG = 3;
    public static final String AUTHOR = "author";
    public static final Integer CUSTOMER_FLAG = 4;
    public static final String CUSTOMER_NAME = "customer";
    
    @PersistenceContext(unitName = "ORIGERPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public RolesFacade() {
        super(Roles.class);
    }

    @Override
    public List<Roles> findAll() {
    	QRoles r = QRoles.roles; 
    	JPAQuery q = new JPAQuery(this.em);
    	q.from(r); 
    	q.orderBy(r.roleName.asc());
    	return q.list(r);
        //return em.createQuery("SELECT r FROM Roles r ORDER BY r.roleName ASC").getResultList();
    }

    public List<Roles> findAllOrderByRoleName() {
    	QRoles r = QRoles.roles; 
    	JPAQuery q = new JPAQuery(this.em);
    	q.from(r); 
    	q.orderBy(r.description.asc());
    	return q.list(r);
    	
        //return em.createQuery("SELECT r FROM Roles r ORDER BY r.description ASC").getResultList(); // Exclude user
    }
    
    
    
}
