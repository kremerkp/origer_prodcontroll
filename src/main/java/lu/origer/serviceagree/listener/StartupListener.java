package lu.origer.serviceagree.listener;

import java.util.ArrayList;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import lu.origer.serviceagree.backend.assoc.AssocServiceElementFacade;
import lu.origer.serviceagree.backend.contact.AddressFacade;
import lu.origer.serviceagree.backend.contact.ContactFacade;
import lu.origer.serviceagree.backend.contact.ContactTypeFacade;
import lu.origer.serviceagree.backend.contact.PersonFacade;
import lu.origer.serviceagree.backend.contact.PersonTypeFacade;
import lu.origer.serviceagree.backend.contract.BuildingSiteFacade;
import lu.origer.serviceagree.backend.contract.ChecklistFacade;
import lu.origer.serviceagree.backend.contract.ChecklistItemFacade;
import lu.origer.serviceagree.backend.contract.ContractTypeFacade;
import lu.origer.serviceagree.backend.contract.DinTypeFacade;
import lu.origer.serviceagree.backend.contract.DirectionTypeFacade;
import lu.origer.serviceagree.backend.contract.ElementTypeFacade;
import lu.origer.serviceagree.backend.contract.FileArchiveFacade;
import lu.origer.serviceagree.backend.contract.FrontTypeFacade;
import lu.origer.serviceagree.backend.contract.ServiceContractFacade;
import lu.origer.serviceagree.backend.contract.ServiceElementFacade;
import lu.origer.serviceagree.backend.contract.ServiceFacade;
import lu.origer.serviceagree.backend.contract.ServiceTypeFacade;
import lu.origer.serviceagree.backend.synch.ServiceHistoryFacade;

@Stateless
public class StartupListener implements ServletContextListener {

	@Inject
	private ContactTypeFacade contactTypeFacade;
	@Inject
	private ServiceTypeFacade serviceTypeFacade;
	@Inject
	private ContractTypeFacade contractTypeFacade;
	@Inject
	private BuildingSiteFacade buildingSiteFacade;
	@Inject
	private PersonTypeFacade personTypeFacade;
	@Inject
	private ServiceFacade serviceFacade;
	@Inject
	private PersonFacade personFacade;
	@Inject
	private ServiceContractFacade serviceContractFacade;
	@Inject
	private ChecklistFacade checklistFacade;
	@Inject
	private ChecklistItemFacade checklistItemFacade;
	@Inject
	private ServiceElementFacade serviceElementFacade;
	@Inject
	private AddressFacade addressFacade;
	@Inject
	private DinTypeFacade dinTypFacade;
	@Inject
	private ContactFacade contactFacade;
	@Inject
	private ElementTypeFacade elementTypeFacade;
	@Inject
	private FrontTypeFacade frontTypeFacade;
	@Inject
	private DirectionTypeFacade directionTypeFacade;
	@Inject
	private AssocServiceElementFacade assocServiceElementFacade;
	@Inject
	private ServiceHistoryFacade serviceHistoryFacade;
	@Inject
	FileArchiveFacade fileArchiveFacade;
	@Inject
	DinTypeFacade dinTypeFacade;

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		/*
			personFacade.findCustomers();
			personFacade.findTechnician();
			elementTypeFacade.findAllActive();
			frontTypeFacade.findAllActiveFront();
			dinTypFacade.findAllActive();
			directionTypeFacade.findAll();
			dinTypeFacade.findAll();
			serviceFacade.findAllServicesOpenOffer();
			serviceElementFacade.findOpenOffers();
			serviceFacade.findAllServices();
			serviceHistoryFacade.findAll();
			contactFacade.findAll();
			addressFacade.findAll();
			serviceContractFacade.findAll();
			checklistFacade.findAll();
			checklistItemFacade.findAll();
			contactTypeFacade.findAll();
			serviceTypeFacade.findAll();
			contractTypeFacade.findAll();
			buildingSiteFacade.findAll();
			personTypeFacade.findAll();
			serviceFacade.findAllServicesGrouped();
		*/

	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub

	}

}
