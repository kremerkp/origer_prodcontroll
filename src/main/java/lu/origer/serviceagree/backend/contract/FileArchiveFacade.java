package lu.origer.serviceagree.backend.contract;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.mysema.query.Query;
import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.backend.main.AbstractFacade;
import lu.origer.serviceagree.backend.main.UsersFacade;
import lu.origer.serviceagree.frontend.main.SessionBean;
import lu.origer.serviceagree.models.contract.BuildingSite;
import lu.origer.serviceagree.models.contract.Invoice;
import lu.origer.serviceagree.models.contract.Offers;
import lu.origer.serviceagree.models.contract.QBuildingSite;
import lu.origer.serviceagree.models.contract.QOffers;
import lu.origer.serviceagree.models.contract.QService;
import lu.origer.serviceagree.models.contract.QServiceContract;
import lu.origer.serviceagree.models.contract.Service;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.main.FileArchive;
import lu.origer.serviceagree.models.main.QFileArchive;
import lu.origer.serviceagree.models.main.Users;
import lu.origer.serviceagree.models.synch.ServiceHistory;

@Stateless
public class FileArchiveFacade extends AbstractFacade<FileArchive>
{

	public static final String PATH_TO_FILEARCHIVE_OFFER = "\\\\SERVER-SQL\\upload\\offer";
	public static final String PATH_TO_FILEARCHIVE_INVOICE = "\\\\SERVER-SQL\\upload\\invoice";

	@Inject
	UsersFacade userfacade;

	@Inject
	BuildingSiteFacade buildingSiteFacade;

	@Inject
	SessionBean sessionBean;

	@PersistenceContext(unitName = "ORIGERPU")
	private EntityManager em;

	public FileArchiveFacade()
	{
		super(FileArchive.class);
	}

	@Override
	protected EntityManager getEntityManager()
	{
		return em;
	}

	public FileArchive findForHistory(ServiceHistory sh)
	{
		QFileArchive f = QFileArchive.fileArchive;
		JPAQuery q = new JPAQuery(this.em);
		q.from(f);
		q.where(f.serviceHistory.eq(sh)).orderBy(f.id.desc());
		return q.singleResult(f);
	}

	public List<FileArchive> findFilesByUrl(String url)
	{
		QFileArchive f = QFileArchive.fileArchive;
		JPAQuery q = new JPAQuery(this.em);
		q.from(f);
		q.where(f.url.contains(url));
		return q.list(f);
	}

	public List<FileArchive> findFilesByName(String name)
	{
		QFileArchive f = QFileArchive.fileArchive;
		JPAQuery q = new JPAQuery(this.em);
		q.from(f);
		q.where(f.name.contains(name));
		return q.list(f);
	}

	public List<FileArchive> findAllBuildingSiteFilesForBS(Integer bsId)
	{
		QFileArchive f = QFileArchive.fileArchive;
		JPAQuery q = new JPAQuery(this.em);
		q.from(f);
		q.where(f.buildingSite.id.eq(bsId));
		return q.list(f);
	}

	public List<FileArchive> findAllBuildingSiteFilesForCustomer(Integer user)
	{
		Users us = userfacade.find(user);

		List<Integer> bsList = buildingSiteFacade.findBuildingSiteForCustomer(us);
		QFileArchive f = QFileArchive.fileArchive;
		JPAQuery q = new JPAQuery(this.em);
		q.from(f);
		q.where(f.buildingSite.id.in(bsList));
		return q.list(f);
	}

	public List<FileArchive> findAllBuildingSiteFiles()
	{
		QFileArchive f = QFileArchive.fileArchive;
		JPAQuery q = new JPAQuery(this.em);
		q.from(f);
		q.where(f.buildingSite.isNotNull());
		return q.list(f);
	}

	public List<FileArchive> findfilesForBuildingSite(BuildingSite bs)
	{
		QFileArchive f = QFileArchive.fileArchive;
		JPAQuery q = new JPAQuery(this.em);
		q.from(f);
		q.where(f.buildingSite.eq(bs));
		return q.list(f);
	}

	public List<FileArchive> findFilesForOffer(Offers offer)
	{
		QFileArchive f = QFileArchive.fileArchive;
		JPAQuery q = new JPAQuery(this.em);
		q.from(f);
		q.where(f.offer.eq(offer));
		return q.list(f);
	}

	public List<FileArchive> findFilesForInvoice(Invoice invoice)
	{
		QFileArchive f = QFileArchive.fileArchive;
		JPAQuery q = new JPAQuery(this.em);
		q.from(f);
		q.where(f.invoice.eq(invoice));
		return q.list(f);

	}
	
	public FileArchive findApprovalSignForServiceHistoryRepair(final Long id)
	{
		QFileArchive f = QFileArchive.fileArchive;
		JPAQuery q = new JPAQuery(this.em);
		q.from(f);
		q.where(f.serviceHistory.id.eq(id).and(f.signature.isTrue()));
		FileArchive result = q.singleResult(f);
		if (result != null)
		{
			result.setUrl(result.getUrl().replace("/etc", ""));
		}

		return result;
	}

	public String findApprovalSignForService(Service service)
	{
		QFileArchive f = QFileArchive.fileArchive;
		JPAQuery q = new JPAQuery(this.em);
		q.from(f);
		q.where(f.service.eq(service));
		q.where(f.signature.isTrue()).orderBy(f.id.desc());
		String result = q.singleResult(f.url);

		if (result != null) {
		{
			result = result.replace("/etc", "");
			result = result.replace("/etc", "");
			result = result.replace("/usr/local/tomee/", "/origer/");
		}
		}


		return result;
	}
	
	public FileArchive findApprovalSignForTimeRecording(final Long recordId)
	{
		QFileArchive f = QFileArchive.fileArchive;
		JPAQuery q = new JPAQuery(this.em);
		q.from(f);
		q.where(f.timeRecordingHistory.id.eq(recordId));
		q.where(f.signature.isTrue()).orderBy(f.id.desc());
		return q.singleResult(f);
	}
	
	public FileArchive findApprovalSignForService(final Integer serviceId)
	{
		QFileArchive f = QFileArchive.fileArchive;
		JPAQuery q = new JPAQuery(this.em);
		q.from(f).where(f.service.id.eq(serviceId).and(f.signature.isTrue()));		
		return q.singleResult(f);		
	}
	
	public FileArchive findApprovalSignForService(final Integer serviceId, final Date createDate)
	{
		QFileArchive f = QFileArchive.fileArchive;
		JPAQuery q = new JPAQuery(this.em);
		final Calendar cal = new GregorianCalendar();
		cal.setTime(createDate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);		
		cal.set(Calendar.MILLISECOND, 0);
		q.from(f).where(f.service.id.eq(serviceId).and(f.signature.isTrue().and(f.createDate.eq(cal.getTime()))));		
		return q.singleResult(f);		
	}
	
	public FileArchive findApprovalSignForHistory(Long historyId)
	{
		QFileArchive f = QFileArchive.fileArchive;
		JPAQuery q = new JPAQuery(this.em);
		q.from(f);
		q.where(f.serviceHistory.id.eq(historyId));
		return q.singleResult(f);
	}

	public String findApprovalSignNameForService(Service service)
	{
		QFileArchive f = QFileArchive.fileArchive;
		JPAQuery q = new JPAQuery(this.em);
		q.from(f);
		q.where(f.service.eq(service));
		q.where(f.signature.isTrue()).orderBy(f.id.desc());;		
		String result = q.singleResult(f.description);

		return q.singleResult(f.description);
	}

	/**
	 * Add Suffix to Filename if already exists
	 * 
	 * @param url
	 * @return
	 */
	public String getFileName(final String name)
	{
		int count = 0;
		for (FileArchive f : this.findFilesByName(name))
		{
			if (f.getName().contains(name))
			{
				count++;
			}
		}
		return count > 0 ? name + "_" + count : name;
	}

	/**
	 * Add Suffix to URL if already exists
	 * 
	 * @param url
	 * @return
	 */
	public String getUrl(final String url)
	{
		String newUrl = url;
		int count = 0;
		for (FileArchive f : this.findFilesByUrl(url))
		{
			if (f.getUrl().contains(url))
			{
				count++;
			}
		}
		return count > 0 ? newUrl + "_" + count : newUrl;
	}

	/**
	 * Deletes the selected file and removes its respective file archive entry.
	 * 
	 * @param archive
	 * @return true on success, false if an error occurred.
	 */
	public Boolean deleteFile(final FileArchive archive)
	{
		Boolean result = true;
		final File file = new File(sessionBean.getApplicationBean().getPathToFileArchiv() + "/" + archive.getSubfolder()
				+ "/" + archive.getName());
		if (file.exists())
		{
			System.out.println("File found: " + file.getAbsolutePath());
			if (file.delete())
			{
				System.out.println("File deleted for " + archive.getId() + " removing DB entry...");
				try
				{
					this.remove(archive);
				}
				catch (Exception e)
				{
					System.out.println("Error removing file archive " + archive.getId());
					e.printStackTrace();
					result = false;
				}
			}
		}
		else
		{
			System.out.println("No file found for " + file.getAbsolutePath());
			result = false;
		}

		return result;
	}
	
	/**
	 * Returns the first service id for the selected archive.
	 * 
	 * @param archiveId
	 * @return building site id if found, null on error or no result.
	 */
	public Integer findServiceIdByFileArchive(final Integer archiveId)
	{
		Integer result = null;
		if(archiveId != null)
		{
			final String query = "SELECT ONE ser.id "
					+ "FROM service ser "
					+ "INNER JOIN service_contract con ON con.id = ser.fk_service_contract "
					+ "INNER JOIN building_site bs ON bs.id = con.fk_building_site "
					+ "INNER JOIN filearchive fa ON bs.id = fa.fk_building_site"
					+ "WHERE fa.id = " + archiveId;
			System.out.println("Sending query: " + query);
			final javax.persistence.Query q = this.em.createNativeQuery(query);
			result = q.getFirstResult();
		}
		return result;
	}

	public List<FileArchive> findAllBuildingSiteFilesByUser(Users currentUser) {
		List<ServiceContract> con = currentUser.getServiceContractList();
		List<BuildingSite> bsList = new ArrayList<>(); 
		for(ServiceContract sc : con){
			bsList.add(sc.getBuildingSite());
		}
		
		QFileArchive f = QFileArchive.fileArchive;
		JPAQuery q = new JPAQuery(this.em);
		q.from(f);
		q.where(f.buildingSite.in(bsList));
		q.where(f.buildingSite.isNotNull());
		return q.list(f);
	}
}
