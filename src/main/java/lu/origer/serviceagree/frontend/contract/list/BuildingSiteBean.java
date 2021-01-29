package lu.origer.serviceagree.frontend.contract.list;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.omnifaces.util.Faces;
import org.primefaces.event.CellEditEvent;

import lu.origer.serviceagree.backend.contract.BuildingSiteFacade;
import lu.origer.serviceagree.backend.contract.FileArchiveFacade;
import lu.origer.serviceagree.frontend.main.BasicListBean;
import lu.origer.serviceagree.models.checklist.Checklist;
import lu.origer.serviceagree.models.main.FileArchive;

@SuppressWarnings("rawtypes")
@ManagedBean
@ViewScoped
public class BuildingSiteBean extends BasicListBean
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<FileArchive> buildingSiteFiles;

	private List<FileArchive> buildingSiteFilesFiltered;

	private FileArchive buildingSiteFileSelected;

	private Integer buildingSiteID;

	@Inject
	BuildingSiteFacade buildingSiteFacade;

	@Inject
	FileArchiveFacade fileArchiveFacade;

	@Override
	public void filterList()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String add()
	{
		return "/origer/serveAgree/admin/building_site/building_site_edit.xhtml?faces-redirect=true";
	}

	@Override
	public String edit()
	{
		try
		{
			return "/origer/serveAgree/admin/building_site/building_site_edit.xhtml?faces-redirect=true&id="
					+ ((Checklist) selectedData).getId();
		}
		catch (Exception e)
		{
			// TODO: handle exception
			return "";
		}
	}

	@Override
	public void delete()
	{
		// TODO Auto-generated method stub

	}

	public void setActive()
	{
		System.out.println("test");
		this.getBuildingSiteFileSelected().setSynchronizeAndroid(true);
		fileArchiveFacade.edit(this.buildingSiteFileSelected);
	}

	public void setInActive()
	{
		System.out.println("test");
		this.getBuildingSiteFileSelected().setSynchronizeAndroid(false);
		fileArchiveFacade.edit(this.buildingSiteFileSelected);
	}

	@PostConstruct
	public void init()
	{

		data = buildingSiteFacade.findAll();
		data = new ArrayList<>(data);
		Integer bsId = getApplicationBean().getBuildingSiteIdFromUrl();
		setBuildingSiteID(bsId);
		Integer custId = getApplicationBean().getCustomerIdFromURL();
		System.out.println(custId);

		if (bsId != null && bsId > 0)
		{
			buildingSiteFiles = fileArchiveFacade.findAllBuildingSiteFilesForBS(bsId);
			buildingSiteFiles = new ArrayList<>(buildingSiteFiles);
		}
		else
		{
			if (custId == null)
			{
				if (getSessionBean().getIsAdmin()){					
					buildingSiteFiles = fileArchiveFacade.findAllBuildingSiteFiles();
					buildingSiteFiles = new ArrayList<>(buildingSiteFiles);
				} else {
					buildingSiteFiles = fileArchiveFacade.findAllBuildingSiteFilesByUser(getSessionBean().getCurrentUser());
					buildingSiteFiles = new ArrayList<>(buildingSiteFiles);					
				}
			}
			else
			{
				buildingSiteFiles = fileArchiveFacade.findAllBuildingSiteFilesForCustomer(custId);
				buildingSiteFiles = new ArrayList<>(buildingSiteFiles);
			}
		}

	}

	public void refreshSite()
	{

	}

	public void editFile()
	{

	}

	public void deleteFile()
	{
		System.out.println("Called delete!");
		if (this.buildingSiteFileSelected != null)
		{
			// File exists, deleting...
			if (this.fileArchiveFacade.deleteFile(this.buildingSiteFileSelected))
			{
				// File deleted, update table
				System.out.println("File archive deleted.");
				this.init();
			}
			else
			{
				System.out.println("Error deleting file archive.");
			}
		}
	}

	public void openBuildingSite()
	{
		try
		{
			System.out.println("Contract details called.");
			final Integer id = fileArchiveFacade.findServiceIdByFileArchive(buildingSiteFileSelected.getId());
			if (id != null && id > 0)
			{
				System.out.println("Service [" + id + "] found, redirecting...");
				Faces.redirect("/origer/faces/origer/serveAgree/contract/contract_edit.xhtml?faces-redirect=true&id="
						+ (id));
			}
			else
			{
				System.out.println("No service found for archive.");
			}
		}
		catch (IOException ex)
		{
			Logger.getLogger(ChecklistItemCategoryBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public List<FileArchive> getBuildingSiteFiles()
	{
		return buildingSiteFiles;
	}

	public void setBuildingSiteFiles(List<FileArchive> buildingSiteFiles)
	{
		this.buildingSiteFiles = buildingSiteFiles;
	}

	public List<FileArchive> getBuildingSiteFilesFiltered()
	{
		return buildingSiteFilesFiltered;
	}

	public void setBuildingSiteFilesFiltered(List<FileArchive> buildingSiteFilesFiltered)
	{
		this.buildingSiteFilesFiltered = buildingSiteFilesFiltered;
	}

	public FileArchive getBuildingSiteFileSelected()
	{
		return buildingSiteFileSelected;
	}

	public void setBuildingSiteFileSelected(FileArchive buildingSiteFileSelected)
	{
		this.buildingSiteFileSelected = buildingSiteFileSelected;
	}

	public Integer getBuildingSiteID()
	{
		return buildingSiteID;
	}

	public void setBuildingSiteID(Integer buildingSiteID)
	{
		this.buildingSiteID = buildingSiteID;
	}

}
