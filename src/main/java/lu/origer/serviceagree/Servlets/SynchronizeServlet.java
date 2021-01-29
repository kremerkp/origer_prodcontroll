package lu.origer.serviceagree.Servlets;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lu.origer.serviceagree.backend.main.UsersFacade;
import lu.origer.serviceagree.backend.synch.SynchJobsFacade;
import lu.origer.serviceagree.frontend.synch.SynchJobBean;
import lu.origer.serviceagree.models.synch.SynchJobs;

/**
 * Servlet implementation class HelloServlet
 */
@WebServlet("/synchroniZe/origer/")
public class SynchronizeServlet extends HttpServlet {
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private SynchJobBean synchBean; 
	
	@Inject
	SynchJobsFacade synchJobsFacade;
	
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final java.io.Writer writer = response.getWriter();
		try
		{
			response.setContentType("text/html"); 
			System.out.println("UPDATE - Starting sync-servlet.");
			List<SynchJobs> res = synchJobsFacade.findAllNotImported();
			synchBean.setSelectedSynchJobsServlet(res);
			synchBean.synchJobsFunctionServlet();			
			writer.append("all Good");
		}
		catch(Exception e)
		{
			e.printStackTrace();
			writer.append("Exception occurred.");
		}
	}


}
