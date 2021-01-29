/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lu.origer.serviceagree.backend.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import lu.origer.serviceagree.frontend.main.ApplicationBean;
import lu.origer.serviceagree.models.main.Logging;

/**
 *
 * @author ffreres
 */
@Stateless
public class LoggingFacade extends AbstractFacade<Logging> {
    @PersistenceContext(unitName = "ORIGERPU")
    private EntityManager em;

    public List<Logging> findLoggingsByDate(Date startDate, Date endDate) {        
        if (startDate == null) {
          startDate = ApplicationBean.setDateToBeginOfDay(new Date());
        } 
        
        endDate = endDate == null ? ApplicationBean.setOuterEndDate() : endDate;
        if (endDate == null) {
            endDate = ApplicationBean.setOuterEndDate();
        }
        List<Logging> result = new ArrayList<>();
        result.addAll(em.createQuery("SELECT l FROM Logging l WHERE l.createDate BETWEEN :start AND :end ORDER BY l.createDate DESC")
                .setParameter("start", startDate)
                .setParameter("end", endDate)
                .getResultList());
        
        return result;
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public LoggingFacade() {
        super(Logging.class);
    }
    
    @Override
    public List<Logging> findAll() {
        return em.createQuery("SELECT l FROM Logging l ORDER BY l.createDate DESC").getResultList();
    }
    
    public List<String> getReporterList() {
        return em.createQuery("SELECT DISTINCT(l.reporter) FROM Logging l ORDER BY l.reporter").getResultList();
    }
        
    public List<Logging> findLogins() {
        return em.createQuery("SELECT l FROM Logging l WHERE l.message LIKE 'Login%' ORDER BY l.createDate DESC").getResultList();
    }
}
