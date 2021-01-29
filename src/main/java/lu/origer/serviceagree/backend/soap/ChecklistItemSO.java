package lu.origer.serviceagree.backend.soap;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author ian.husting
 *
 * RPC-confirm SOAP object for checklist items
 */
@XmlRootElement
public class ChecklistItemSO {
	private Long id;
	private String name;
	private Boolean active;
	private ChecklistItemCategorySO category;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public ChecklistItemCategorySO getCategory() {
		return category;
	}
	public void setCategory(ChecklistItemCategorySO category) {
		this.category = category;
	}
}
