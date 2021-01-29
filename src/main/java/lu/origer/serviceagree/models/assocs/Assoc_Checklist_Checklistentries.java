package lu.origer.serviceagree.models.assocs;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import lu.origer.serviceagree.models.checklist.Checklist;
import lu.origer.serviceagree.models.checklist.ChecklistItem;

@Entity
@Table(name = "assoc_checklist_checklistitems")
@XmlRootElement
public class Assoc_Checklist_Checklistentries implements Serializable {

	/**
	 * 
	 */

	private static final long serialVersionUID = 3753831141523506595L;
	@Id
	@Basic(optional = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "has_fc")
	private Boolean fc;

	@Column(name = "has_vc")
	private Boolean vc;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "fk_checklist", referencedColumnName = "id")
	private Checklist checkList;
	
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "fk_checklist_entry", referencedColumnName = "id")	
	private ChecklistItem checklistItem;
	
	public Boolean getFc() {
		return fc;
	}

	public void setFc(Boolean fc) {
		this.fc = fc;
	}

	public Boolean getVc() {
		return vc;
	}

	public void setVc(Boolean vc) {
		this.vc = vc;
	}

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

	public Checklist getCheckList() {
		return checkList;
	}

	public void setCheckList(Checklist checkList) {
		this.checkList = checkList;
	}

}
