package lu.origer.serviceagree.backend.soap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.net.ntp.TimeStamp;

import lu.origer.serviceagree.models.contact.types.DinType;

public class ResultsetMapper {
	
	private static String SE_ID = "id";
    private static String SE_NAME = "name";
    private static String SE_SHN = "shn";
    private static String SE_ACTIVE = "active";
    private static String SE_DESCRIPTION = "description";
    private static String SE_LATEST_DATE = "latest_service_date";
    private static String SE_START_DATE = "startDate";
    private static String SE_END_DATE = "endDate";
    private static String SE_CONTRACT = "fk_service_contract";
    private static String SE_SERVICE_TYPE = "fk_service_type";
    private static String SE_INTERVAL = "intervall";
    
    private static String SC_ID = "SC_ID";
    private static String SC_ACTIVE = "SC_ACTIVE";
    private static String SC_DESCRIPTION = "SC_DESCRIPTION";
    private static String SC_FROM_DATE = "from_Date";
    private static String SC_TO_DATE = "to_Date";
    private static String SC_SITE = "fk_building_Site";

    private static String BS_ID = "BS_ID";
    private static String BS_NAME = "BS_NAME";
    private static String BS_SHN = "BS_SHN";
    private static String BS_ACTIVE = "BS_ACTIVE";
    private static String BS_DESCRIPTION = "BS_DESCRIPTION";
    private static String BS_BARCODE = "code";    
    
    private static String ST_ID = "ST_ID";
    private static String ST_NAME = "ST_NAME";
    private static String ST_SHN = "ST_SHN";
    private static String ST_ACTIVE = "ST_ACTIVE";
    private static String ST_DESCRIPTION = "ST_DESCRIPTION";  
    
    private static String CIC_ID = "CIC_ID";
    private static String CIC_NAME = "CIC_NAME";
    private static String CIC_SHN = "CIC_SHN";
    private static String CIC_DESCRIPTION = "CIC_DESCRIPTION";
    
    private static String CI_ID = "CI_ID";
    private static String CI_NAME = "CI_NAME";
    private static String CI_ACTIVE = "CI_ACTIVE";
    private static String CI_CATEGORY = "FK_CATEGORY";    
    
    private static String SET_ID = "SET_ID";
    private static String SET_NAME = "SET_NAME";
    private static String SET_ACTIVE = "SET_ACTIVE";
    
    private static String SL_ID = "SL_ID";
    private static String SL_NAME = "SL_NAME";
    private static String SL_ELEMENT_TYPE = "fk_element_type";
    private static String SL_FLOOR = "floor";    
    private static String SL_ORIENTATION_NAME = "SL_ORIENTATION_NAME";    
    private static String SL_ROOM = "room";    
    private static String SL_FRONT_NAME = "SL_FRONT_NAME";
    private static String SL_ACTIVE = "SL_ACTIVE";
    private static String SL_NUMBER = "ELEMENTNUMBER";
    private static String SL_PRINT_LABEL = "print_new_barcode";
    private static String SL_LAST_SERVICE_DATE = "last_service_date";
    private static String SL_ELEMENT_STATE = "element_state";
    private static String SL_DIN_ID = "fk_din_type";
    private static String SL_DESCRIPTION = "ELEMENT_DESCRIPTION";
    
    private static String SYC_ID = "SYC_ID";
    private static String SYC_CHECKLIST_ITEM = "fk_checklistitem";
    private static String SYC_START_TIME = "startTime";
    private static String SYC_END_TIME = "endTime";
    private static String SYC_CHECK_MINUTES = "checking_sec";
    private static String SYC_ERROR_HISTORY_FLAG = "error_history_flag";
    private static String SYC_SETUP_MINUTES = "setup_sec";    
    private static String SYC_DESCRIPTION = "SYC_DESCRIPTION";
    private static String SYC_VISUAL_CONTROL = "visualcontrol";
    private static String SYC_FUNCTIONAL_CONTROL ="functionalcontrol";
    private static String SYC_OK = "isok";
    private static String SYC_FAULTY = "islacking";
    private static String SYC_DEFECT = "isdefect";
    private static String SYC_CREATE_OFFER = "createoffer";
    private static String SYC_SERVICE = "fk_service";
    private static String SYC_ELEMENT = "fk_element";
    private static String SYC_REPAIRED = "isRepaired";
    private static String SYC_VF = "vf";
    
	private static String OF_ID = "OF_ID";
	private static String OF_NAME = "OF_NAME";
	private static String OF_ACTIVE = "OF_ACTIVE";
	private static String OF_DATE = "offerdate";
	private static String OF_AMOUNT = "amount";
	private static String OF_STATE = "state";
	
	private static String FA_ID = "FA_ID";
	private static String FA_URL = "url";
	private static String FA_CHECKLIST_CONTROLS = "fk_service_history";
	private static String FA_NAME = "FA_NAME";
	private static String FA_DESCRIPTION = "FA_DESCRIPTION";
	private static String FA_SITE = "FA_SITE";
	private static String FA_SIG = "is_signature";
	
	private static String PE_ID = "id";
	private static String PE_NAME = "name";
	private static String PE_FIRST_NAME = "firstName";
	private static String PE_LAST_NAME = "lastName";
	private static String PE_TITLE = "title";
	
	private static String DIN_ID = "din_id";
	private static String DIN_NAME_DE = "din_name_de";
	private static String DIN_NAME_FR = "din_name_fr";
		
	private static String AOE_OFFER = "AOE_OFFER";
	private static String AOE_ELEMENT = "AOE_ELEMENT";
	private static String AOE_OFFER_STATE = "AOE_OFFER_STATE";
	
	public ServiceSO resultsetToServiceSO(final ResultSet source, final Boolean resolve)
	{
		try 
		{
			if(source != null && source.getMetaData().getColumnCount() > 0)
			{
				final ServiceSO result = new ServiceSO();
				
				result.setId(source.getInt(SE_ID));
				if(resolve)
				{
					result.setName(source.getString(SE_NAME));
					result.setShortName(source.getString(SE_SHN));
					result.setLatestServiceDate(source.getDate(SE_LATEST_DATE));
					result.setActive(source.getBoolean(SE_ACTIVE));
					result.setStartDate(source.getDate(SE_START_DATE));
					result.setEndDate(source.getDate(SE_END_DATE));
					if(source.getObject(SE_CONTRACT) != null)
					{
						result.setContract(this.resultsetToServiceContractSO(source));
					}
					if(source.getObject(SE_SERVICE_TYPE) != null)
					{
						result.setServiceType(this.resultsetToServiceTypeSO(source));
					}
					result.setInterval(source.getInt(SE_INTERVAL));
					result.setDescription(source.getString(SE_DESCRIPTION));
				}
				return result;
			}
			else
			{
				return null;
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public ServiceContractSO resultsetToServiceContractSO(final ResultSet source)
	{
		try {
			if(source != null && source.getMetaData().getColumnCount() > 0)
			{
				final ServiceContractSO result = new ServiceContractSO();
				result.setId(source.getInt(SC_ID));	
				result.setActive(source.getBoolean(SC_ACTIVE));
				result.setDescription(source.getString(SC_DESCRIPTION));				
				result.setFromDate(source.getDate(SC_FROM_DATE));
				result.setToDate(source.getDate(SC_TO_DATE));
				if(source.getObject(SC_SITE) != null)
				{
					result.setBuildingSite(this.resultsetToBuildingSiteSO(source, true));
				}
				return result;
			}
			else
			{
				return null;
			}
		} 
		catch (SQLException e) 
		{ 
			e.printStackTrace();
			return null;
		}
	}
	
	public ServiceTypeSO resultsetToServiceTypeSO(final ResultSet source)
	{
		try {
			if(source != null && source.getMetaData().getColumnCount() > 0)
			{
				final ServiceTypeSO result = new ServiceTypeSO();
				result.setId(source.getInt(ST_ID));	
				result.setActive(source.getBoolean(ST_ACTIVE));
				result.setDescription(source.getString(ST_DESCRIPTION));
				result.setName(source.getString(ST_NAME));
				result.setShortName(source.getString(ST_SHN));
				
				return result;
			}
			else
			{
				return null;
			}
		} 
		catch (SQLException e) 
		{ 
			e.printStackTrace();
			return null;
		}
	}
	
	public BuildingSiteSO resultsetToBuildingSiteSO(final ResultSet source, Boolean resolve)
	{
		try {
			if(source != null && source.getMetaData().getColumnCount() > 0)
			{
				final BuildingSiteSO result = new BuildingSiteSO();				
				result.setId(source.getLong(BS_ID));	
				result.setName(source.getString(BS_NAME));
				if(resolve)
				{
					result.setActive(source.getBoolean(BS_ACTIVE));
					result.setDescription(source.getString(BS_DESCRIPTION));					
					result.setShortname(source.getString(BS_SHN));
					result.setCode(source.getString(BS_BARCODE));
				}
				
				return result;
			}
			else
			{
				return null;
			}
		} 
		catch (SQLException e) 
		{ 
			e.printStackTrace();
			return null;
		}
	}
	
	public ChecklistItemCategorySO resultsetToChecklistItemCategory(final ResultSet source)
	{
		try
		{
			if(source != null && source.getMetaData().getColumnCount() > 0)
			{
				final ChecklistItemCategorySO result = new ChecklistItemCategorySO();
				result.setId(source.getLong(CIC_ID));
				result.setName(source.getString(CIC_NAME));
				result.setShortName(source.getString(CIC_SHN));
				result.setDescription(source.getString(CIC_DESCRIPTION));
				
				return result;
			}
			else
			{
				return null;
			}
		}
		catch (SQLException e) 
		{ 
			e.printStackTrace();
			return null;
		}
	}
	
	public ChecklistItemSO resultsetToChecklistitemSO(final ResultSet source)
	{
		try
		{
			if(source != null && source.getMetaData().getColumnCount() > 0)
			{
				final ChecklistItemSO result = new ChecklistItemSO();
				
				result.setId(source.getLong(CI_ID));
				result.setName(source.getString(CI_NAME));
				result.setActive(source.getBoolean(CI_ACTIVE));
				if(source.getObject(CI_CATEGORY) != null)
				{
					result.setCategory(this.resultsetToChecklistItemCategory(source));
				}				
				return result;
			}
			else
			{
				return null;
			}
		}
		catch (SQLException e) 
		{ 
			e.printStackTrace();
			return null;
		}
	}
	
	public ServiceElementTypeSO resultsetToServiceElementTypeSO(final ResultSet source)
	{
		try
		{
			if(source != null && source.getMetaData().getColumnCount() > 0)
			{
				final ServiceElementTypeSO result = new ServiceElementTypeSO();
				result.setId(source.getInt(SET_ID));
				result.setName(source.getString(SET_NAME));
				result.setActive(source.getBoolean(SET_ACTIVE));
				
				return result;
			}
			else
			{
				return null;
			}
		}
		catch (SQLException e) 
		{ 
			e.printStackTrace();
			return null;
		}
	}
	
	public ServiceElementSO resultsetToServiceElementSO(final ResultSet source)
	{
		try
		{
			if(source != null && source.getMetaData().getColumnCount() > 0)
			{
				final ServiceElementSO result = new ServiceElementSO();
				result.setId(source.getInt(SL_ID));
				result.setActive(source.getBoolean(SL_ACTIVE));
				result.setElementNumber(source.getString(SL_NUMBER));
				if(source.getObject(SL_ELEMENT_TYPE) != null)
				{
					result.setElementType(this.resultsetToServiceElementTypeSO(source));
				}
					
				result.setFloor(source.getString(SL_FLOOR));				
				result.setName(source.getString(SL_NAME));
				
				result.setFront(source.getString(SL_FRONT_NAME));
				result.setOrientation(source.getString(SL_ORIENTATION_NAME));				
				result.setRoom(source.getString((SL_ROOM)));
				result.setPrintLabel(source.getBoolean(SL_PRINT_LABEL));
				result.setStatus(source.getString(SL_ELEMENT_STATE));
				result.setLastServiceDate(source.getDate(SL_LAST_SERVICE_DATE));
				if(source.getObject(SL_DIN_ID) != null)
				{
					result.setDinId(source.getInt(SL_DIN_ID));
				}
				result.setDescription(source.getString(SL_DESCRIPTION));
				return result;
			}
			else
			{
				return null;
			}
		}
		catch (SQLException e) 
		{ 
			e.printStackTrace();
			return null;
		}
	}
	
	public SyncControlsSO resultsetToSyncControlsSO(final ResultSet source, final Boolean resolve)
	{
		try
		{
			if(source != null && source.getMetaData().getColumnCount() > 0)
			{				
				final SyncControlsSO result = new SyncControlsSO();				
				result.setCheckingMinutes(source.getInt(SYC_CHECK_MINUTES));
				if(source.getObject(SYC_CHECKLIST_ITEM) != null)
				{
					if(resolve)
					{
						result.setChecklistItem(this.resultsetToChecklistitemSO(source));
					}
					else
					{
						result.setChecklistItem(new ChecklistItemSO());
						result.getChecklistItem().setId(source.getLong(SYC_CHECKLIST_ITEM));
					}
				}
				result.setDescription(source.getString(SYC_DESCRIPTION));
				result.setErrorHistoryFlag(source.getBoolean(SYC_ERROR_HISTORY_FLAG));				
				TimeZone tz = TimeZone.getTimeZone("CET");
				Timestamp tempEnd = source.getTimestamp(SYC_END_TIME, Calendar.getInstance(tz));										
				if(tempEnd != null)
				{									
					result.setEndTime(new Date(tempEnd.getTime()));
				}				
				result.setFunctionalControl(source.getBoolean(SYC_FUNCTIONAL_CONTROL));
				result.setId(source.getInt(SYC_ID));
				result.setIsDefect(source.getBoolean(SYC_DEFECT));
				result.setIsFaulty(source.getBoolean(SYC_FAULTY));
				result.setIsOk(source.getBoolean(SYC_OK));
				result.setSetupMinutes(source.getInt(SYC_SETUP_MINUTES));						
				Timestamp tempStart = source.getTimestamp(SYC_START_TIME, Calendar.getInstance(tz));				
				if(tempStart != null)
				{								
					result.setStartTime(new Date(tempStart.getTime()));
				}				
				result.setVisualControl(source.getBoolean(SYC_VISUAL_CONTROL));		
				result.setCreateOffer(source.getBoolean(SYC_CREATE_OFFER));
				if(source.getObject(SYC_SERVICE) != null)
				{
					if(resolve)
					{
						result.setService(this.resultsetToServiceSO(source, false));
					}
					else
					{
						result.setService(new ServiceSO());
						result.getService().setId(source.getInt(SYC_SERVICE));
					}
				}
				
				if(source.getObject(SYC_ELEMENT) != null)
				{
					if(resolve)
					{
						result.setElement(this.resultsetToServiceElementSO(source));
					}
					else
					{
						result.setElement(new ServiceElementSO());
						result.getElement().setId(source.getInt(SYC_ELEMENT));
					}
				}
				if(source.getObject(SYC_REPAIRED) != null)
				{
					result.setIsRepaired(source.getBoolean(SYC_REPAIRED));
				}
				if(source.getObject(SYC_VF) != null)
				{
					result.setVf(source.getInt(SYC_VF));
				}
				else
				{
					result.setVf(0);
				}
				
				return result;
			}
			else
			{
				return null;
			}
		}
		catch (SQLException e) 
		{ 
			e.printStackTrace();
			return null;
		}
	}
	
	public OfferSO resultSetToOfferSO(final ResultSet source)
	{
		try
		{
			if(source != null && source.getMetaData().getColumnCount() > 0)
			{
				final OfferSO result = new OfferSO();
				result.setActive(source.getBoolean(OF_ACTIVE));
				result.setAmount(source.getInt(OF_AMOUNT));
				result.setId(source.getInt(OF_ID));
				result.setName(source.getString(OF_NAME));
				Timestamp tempDate = source.getTimestamp(OF_DATE);
				if(tempDate != null)
				{
					result.setOfferDate(new Date(tempDate.getTime()));
				}
				result.setState(source.getString(OF_STATE));
				
				return result;
			}
			else
			{
				return null;
			}
		}
		catch (SQLException e) 
		{ 
			e.printStackTrace();
			return null;
		}
	}
	
	public FileArchiveSO resultsetToFileArchiveSO(final ResultSet source)
	{
		try
		{
			if(source != null && source.getMetaData().getColumnCount() > 0)
			{
				final FileArchiveSO result = new FileArchiveSO();
				result.setId(source.getLong(FA_ID));
				result.setDescription(source.getString(FA_DESCRIPTION));
				result.setUrl(source.getString(FA_URL));
				if(source.getObject(FA_CHECKLIST_CONTROLS) != null)
				{
					final SyncControlsSO temp = new SyncControlsSO();
					temp.setId(source.getInt(FA_CHECKLIST_CONTROLS));
					result.setControls(temp);
				}
				if(source.getObject(FA_SITE) != null)
				{
					result.setSite(source.getInt(FA_SITE));
				}
				result.setName(source.getString(FA_NAME));
				if(source.getObject(FA_SIG) != null)
				{
					result.setIsSignature(source.getBoolean(FA_SIG));
				}
				else
				{
					result.setIsSignature(false);
				}
				return result;
			}
			else
			{
				return null;
			}
		}
		catch (SQLException e) 
		{ 
			e.printStackTrace();
			return null;
		}
	}
	
	public PersonSO resultsetToPersonSO(final ResultSet source)
	{

		try
		{
			if(source != null && source.getMetaData().getColumnCount() > 0)
			{
				final PersonSO result = new PersonSO();
				result.setId(source.getInt(PE_ID));
				result.setName(source.getString(PE_NAME));
				result.setFirstName(source.getString(PE_FIRST_NAME));
				result.setLastName(source.getString(PE_LAST_NAME));
				result.setTitle(source.getString(PE_TITLE));
				
				return result;
			}
			else
			{
				return null;
			}
		}
		catch (SQLException e) 
		{ 
			e.printStackTrace();
			return null;
		}
	}
	
	public DirectionSO resultsetToDirectionSO(final ResultSet source)
	{
		try
		{
			if(source != null && source.getMetaData().getColumnCount() > 0)
			{
				final DirectionSO result = new DirectionSO();
				result.setId(source.getInt("id"));
				result.setName(source.getString("name"));
				result.setActive(source.getBoolean("active"));
				
				return result;
			}
			else
			{
				return null;
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public DinType resultsetToDinType(final ResultSet source)
	{
		try
		{
			if(source != null && source.getMetaData().getColumnCount() > 0)
			{
				final DinType result = new DinType();
				result.setId(source.getInt(DIN_ID));
				result.setName(source.getString(DIN_NAME_DE));
				result.setNameFrench(source.getString(DIN_NAME_FR));
				
				return result;
			}
			else
			{
				return null;
			}
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public ServiceTechnicianAssocSO resultsetToElementOfferAssocSO(final ResultSet source)
	{
		try {
			if(source != null && source.getMetaData().getColumnCount() > 0)
			{
				final ServiceTechnicianAssocSO result = new ServiceTechnicianAssocSO();				
				result.setServiceId(source.getLong(AOE_OFFER));
				result.setTechnicianId(source.getLong(AOE_ELEMENT));
				result.setDescription(source.getString(AOE_OFFER_STATE));								
				return result;
			}
			else
			{
				return null;
			}
		} 
		catch (SQLException e) 
		{ 
			e.printStackTrace();
			return null;
		}
	}
}
