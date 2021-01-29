package lu.origer.serviceagree.frontend.contract.edit;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lu.origer.serviceagree.backend.contract.BuildingSiteFacade;
import lu.origer.serviceagree.frontend.main.BasicFormBean;
import lu.origer.serviceagree.models.contract.BuildingSite;

@Named
@ViewScoped
public class BuildingSiteEditBean extends BasicFormBean<BuildingSite> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Inject
	private BuildingSiteFacade buildingSiteFacade;

	@Override
	protected void checkInput() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void create() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void update() {
		// TODO Auto-generated method stub
		
	} 
	
	

}
