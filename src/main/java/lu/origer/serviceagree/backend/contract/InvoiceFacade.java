package lu.origer.serviceagree.backend.contract;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.models.contract.Invoice;
import lu.origer.serviceagree.models.contract.QInvoice;

@Stateless
public class InvoiceFacade extends AbstractFacade<Invoice>  {
	
    public InvoiceFacade() {
        super(Invoice.class);
    }

    @PersistenceContext(unitName = "ORIGERPU")
    private EntityManager em;

    
    public List<Invoice> findAllForServiceContract(Integer scId){
    	QInvoice inv = QInvoice.invoice; 
    	JPAQuery q = new JPAQuery(this.em);
    	q.from(inv); 
    	q.where(inv.serviceContract.id.eq(scId)); 
    	return q.list(inv);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    

}
