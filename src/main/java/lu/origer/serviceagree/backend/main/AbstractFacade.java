/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lu.origer.serviceagree.backend.main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.models.main.ObjectComments;
import lu.origer.serviceagree.models.main.QObjectComments;
import lu.origer.serviceagree.models.main.Users;
import lu.origer.serviceagree.models.synch.SynchJobs;

/**
 *
 * @author ffreres
 */
public abstract class AbstractFacade<T> {
	
	public static final String OBJECT_BUILDING_SITE = "Baustelle"; 
	public static final String OBJECT_CUSTOMER = "Kunde"; 
	public static final String OBJECT_TECHNICIAN = "Techniker"; 
	public static final String OBJECT_CONTRACT = "Wartungsvertrag"; 

    private Class<T> entityClass;
    
	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;
	

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public String decodeConstrainViolation(ConstraintViolationException e) {
        ConstraintViolationException cvex = (ConstraintViolationException) e;
        StringBuilder exceptionMessage = new StringBuilder();
        for (ConstraintViolation cv : cvex.getConstraintViolations()) {
            exceptionMessage.append(cv.getMessage());
            exceptionMessage.append(" (Fehlerhafter Wert: '");
            exceptionMessage.append(cv.getInvalidValue());
            exceptionMessage.append("' für Attribut: '");
            exceptionMessage.append(cv.getPropertyPath());
            exceptionMessage.append("' in Klasse: '");
            exceptionMessage.append(cv.getRootBeanClass().getName());
            exceptionMessage.append("\n");
        }
        return exceptionMessage.toString();
    }
    

    public void create(T entity) {
        try {
            getEntityManager().persist(entity);
            getEntityManager().flush();
            System.out.println("Created entry "  + entity.getClass().toString());
        } catch (ValidationException e) {
            System.out.println("VE: " + e.getMessage());
            System.out.println(decodeConstrainViolation((ConstraintViolationException) e));
        }
    }

    public void edit(T entity) {
        try {
            getEntityManager().merge(entity);
            getEntityManager().flush();            
        } catch (ValidationException e) {
            System.out.println("VE: " + e.getMessage());
            System.out.println(decodeConstrainViolation((ConstraintViolationException) e));
        }
    }
    
    public void persist(T entity) {
        try {
            getEntityManager().persist(entity);
            getEntityManager().flush();
        } catch (ValidationException e) {
            System.out.println("VE: " + e.getMessage());
            System.out.println(decodeConstrainViolation((ConstraintViolationException) e));
        }
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));        
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }

    public List<T> findAllOrderByCol(String colName) {
        return new ArrayList<>(getEntityManager().createQuery("SELECT t FROM " + entityClass.getSimpleName() + " t ORDER BY t." + colName).getResultList());
    }

    public List<T> findAllOrderByCol(String colName1, String colName2) {
        return new ArrayList<>(getEntityManager().createQuery("SELECT t FROM " + entityClass.getSimpleName() + " t ORDER BY t." + colName1 + ", t." + colName2).getResultList());
    }

    public List<T> findAllActiveOrderByCol(String colName) {
        return new ArrayList<>(getEntityManager().createQuery("SELECT t FROM " + entityClass.getSimpleName() + " t WHERE t.active = :active ORDER BY t." + colName)
                .setParameter("active", true)
                .getResultList());
    }

    public List<T> findAllActiveOrderByCol(String colName1, String colName2) {
        return new ArrayList<>(getEntityManager().createQuery("SELECT t FROM " + entityClass.getSimpleName() + " t WHERE t.active = :active ORDER BY t." + colName1 + ", t." + colName2)
                .setParameter("active", true)
                .getResultList());
    }
    

    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0] + 1);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    public Boolean checkForUniqueness(String fieldname, String value) {
        return checkForUniqueness(entityClass.getSimpleName(), fieldname, value, -1);
    }

    public Boolean checkForUniqueness(String tableName, String fieldName, String value) {
        return checkForUniqueness(tableName, fieldName, value, -1);
    }

    public Boolean checkForUniqueness(String fieldName, String value, Integer id) {
        return checkForUniqueness(entityClass.getSimpleName(), fieldName, value, id);
    }

    public Boolean checkForUniqueness(String tableName, String fieldName, String value, Integer id) {
        try {
            Long cnt = (Long) getEntityManager()
                    .createQuery("SELECT COUNT(t) AS cnt FROM " + tableName + " t WHERE t." + fieldName + " = :compare AND t.id <> :id")
                    .setParameter("compare", value)
                    .setParameter("id", id)
                    .getSingleResult();
            return cnt == 0L;
        } catch (NoResultException e) {
            return true;
        }
    }

    public Boolean isUsed(Class classObject, String fieldName, String value) {
        long count = (long) getEntityManager().createQuery("SELECT COUNT(t) FROM AssocEinsaetzeMateriale t WHERE t." + fieldName + " = :value")
                .setParameter("value", value)
                .getSingleResult();
        return count > 0;
    }

    public Boolean isUsed(String className, String fieldName, Integer value) {
        long count = (long) getEntityManager().createQuery("SELECT COUNT(t) FROM " + className + " t WHERE t." + fieldName + " = :value")
                .setParameter("value", value)
                .getSingleResult();
        return count > 0;
    }
    
    public Integer getNewId() {
        Integer max = (Integer) getEntityManager().createQuery("SELECT MAX(t.id) FROM " + entityClass.getSimpleName() + " t").getSingleResult();
        max++;
        return max;
    }
    
    public List<ObjectComments> getObjectComments(Class c) {
		List<ObjectComments> res = new ArrayList<>(); 
		QObjectComments oc = QObjectComments.objectComments; 
		JPAQuery q = new JPAQuery(this.em);
		q.from(oc); 
		q.where(oc.objectType.eq(c.getName()));
		res = q.list(oc);
		return res;
    	
    }
    
    public T findForCreator(Integer entityId, Integer userId) {
        try {
        return (T) getEntityManager().createQuery("SELECT t FROM " + entityClass.getSimpleName() + " t WHERE t.createdBy = :userId AND t.id = :id")
                .setParameter("userId", userId)
                .setParameter(("id"), entityId)
                .getSingleResult(); 
        } catch (NoResultException ex) {
            return null;
        }
    }
}
