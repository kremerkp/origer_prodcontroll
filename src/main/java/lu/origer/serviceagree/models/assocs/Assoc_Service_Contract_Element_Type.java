package lu.origer.serviceagree.models.assocs;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

import lu.origer.serviceagree.models.checklist.Checklist;
import lu.origer.serviceagree.models.checklist.ChecklistItem;
import lu.origer.serviceagree.models.checklist.State;

@Entity
@Table(name = "assoc_service_contract_element_typ")
@XmlRootElement
public class Assoc_Service_Contract_Element_Type implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4324016875806625626L;
	@Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer           id;
	
    @JoinColumn(name = "fk_element", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ChecklistItem checklistItem;
    
    @JoinColumn(name = "fk_checklist", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Checklist checklist;
    

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ChecklistItem getChecklistItem() {
		return checklistItem;
	}

	public void setChecklistItem(ChecklistItem checklistItem) {
		this.checklistItem = checklistItem;
	}

	public Checklist getChecklist() {
		return checklist;
	}

	public void setChecklist(Checklist checklist) {
		this.checklist = checklist;
	}


}
