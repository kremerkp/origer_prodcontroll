/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lu.origer.serviceagree.backend.main;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lu.origer.serviceagree.models.main.ObjectComments;

/**
 * 
 * @author kai.kremer
 *
 */
@Stateless
public class ObjectCommentFacade extends AbstractFacade<ObjectComments> {
    
    
    @PersistenceContext(unitName = "ORIGERPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ObjectCommentFacade() {
        super(ObjectComments.class);
    }

}
