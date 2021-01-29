/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lu.origer.serviceagree.frontend.main;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.el.ValueExpression;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.util.Faces;
import org.primefaces.component.autocomplete.AutoComplete;
import org.primefaces.model.StreamedContent;

import lu.origer.serviceagree.backend.contact.CountryFacade;
import lu.origer.serviceagree.backend.contact.PersonFacade;
import lu.origer.serviceagree.backend.contract.BuildingSiteFacade;
import lu.origer.serviceagree.backend.contract.ServiceContractFacade;
import lu.origer.serviceagree.backend.main.LoggingFacade;
import lu.origer.serviceagree.backend.main.RolesFacade;
import lu.origer.serviceagree.backend.main.UsersFacade;
import lu.origer.serviceagree.models.contact.Country;
import lu.origer.serviceagree.models.contact.Person;
import lu.origer.serviceagree.models.contract.BuildingSite;
import lu.origer.serviceagree.models.contract.ServiceContract;
import lu.origer.serviceagree.models.main.LegendToolTip;
import lu.origer.serviceagree.models.main.Logging;
import lu.origer.serviceagree.models.main.Roles;
import lu.origer.serviceagree.models.main.Users;

/**
 *
 * @author ffreres
 */
@Named
@ApplicationScoped
public class ApplicationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private LoggingFacade loggingFacade;
	@Inject
	private UsersFacade usersFacade;
	@Inject
	private RolesFacade rolesFacade;
	@Inject
	private CountryFacade countryFacade;
	@Inject
	private BuildingSiteFacade buildingSiteFacade;
	@Inject
	private PersonFacade personFacade;
	@Inject
	private ServiceContractFacade serviceContractFacade;

	private List<Roles> rolesList;

	private String pathToFileArchiv;

	private List<SelectItem> usersList;
	private List<SelectItem> messageLevelList;
	private HashMap<String, String> config;
	private List<SelectItem> categoriesList;
	private List<SelectItem> personenList;
	private List<LegendToolTip> legendOffer;
	private List<LegendToolTip> legendHistorie;

	private String applicationMessage;

	private String test;
	private String test2;

	private static Scanner sc;

	/**
	 * Creates a new instance of ApplicationBean
	 */
	public ApplicationBean() {
	}

	public String getApplicationMessage() {
		return applicationMessage;
	}

	public void setApplicationMessage(String applicationMessage) {
		this.applicationMessage = applicationMessage;
	}

	public List<Roles> getRolesList() {
		return rolesList;
	}

	public void setRolesList(List<Roles> rolesList) {
		this.rolesList = rolesList;
	}

	public void initLegendToolTip() {
		legendOffer = new ArrayList<>();
		LegendToolTip tt = new LegendToolTip();
		tt.setDescription("Angebot wurde abgelehnt");
		tt.setUrl("/resources/img/no.png");
		legendOffer.add(tt);
		tt = new LegendToolTip();
		tt.setDescription("Angebot wurde versendet");
		tt.setUrl("/resources/img/wait.png");
		legendOffer.add(tt);
		tt = new LegendToolTip();
		tt.setDescription("Angebot wurde angenommen");
		tt.setUrl("/resources/img/green.png");
		legendOffer.add(tt);

		legendHistorie = new ArrayList<>();
		LegendToolTip tt2 = new LegendToolTip();
		tt2.setDescription("Intervall wird gerade bearbeitet");
		tt2.setUrl("/resources/img/green.png");
		legendHistorie.add(tt2);
		tt2 = new LegendToolTip();
		tt2.setDescription("Intervall wird nicht bearbeitet");
		tt2.setUrl("/resources/img/wait.png");
		legendHistorie.add(tt2);

	}

	@PostConstruct
	public void init() {
		System.out.println("Starting ApplicationBean of orgier");
		applicationMessage = "";

		messageLevelList = new ArrayList<>();
		messageLevelList.add(new SelectItem(""));
		messageLevelList.add(new SelectItem(Constants.MessageLevel.ERROR));
		messageLevelList.add(new SelectItem(Constants.MessageLevel.INFO));
		messageLevelList.add(new SelectItem(Constants.MessageLevel.DEBUG));

		categoriesList = new ArrayList<>();
		categoriesList.add(new SelectItem(""));
		refreshAllLists();
	}

	public void refreshAllLists() {
		// refreshUserList();
		refreshPersonenList();
		initLegendToolTip();
		refreshRolesList();
	}

	public void refreshRolesList() {
		if (rolesList == null || rolesList.size() > 0) {
			rolesList = new ArrayList<>(rolesFacade.findAllOrderByRoleName());
		} else {
			System.out.println("Not neccassary to update rolesList");
		}
	}

	public void refreshPersonenList() {
		personenList = new ArrayList<>();
		List<Users> source = usersFacade.findAllOrderByCol("name", "prename");
		for (Users person : source) {
			personenList.add(new SelectItem(person, person.getName() + " " + person.getPrename()));
		}
	}
	//
	// public void refreshUserList() {
	// List<Users> tmpUsersList = usersFacade.findAll();
	// usersList = new ArrayList<>();
	// usersList.add(new SelectItem(""));
	// usersList.add(new SelectItem("Guest"));
	// for (Users user : tmpUsersList) {
	// usersList.add(new SelectItem(user.getUsername()));
	// }
	// }

	public List<SelectItem> getBooleanList() {
		List<SelectItem> res = new ArrayList<>();
		res.add(new SelectItem(null, "-- Auswählen --"));
		res.add(new SelectItem(true, "Ja"));
		res.add(new SelectItem(false, "Nein"));
		return res;
	}

	public List<SelectItem> getPersonenList() {
		return personenList;
	}

	public void setPersonenList(List<SelectItem> personenList) {
		this.personenList = personenList;
	}

	public List<SelectItem> getBooleanOptions() {
		List<SelectItem> res = new ArrayList<>();
		res.add(new SelectItem(null, ""));
		res.add(new SelectItem(Boolean.TRUE.toString(), "Ja"));
		res.add(new SelectItem(Boolean.FALSE.toString(), "Nein"));
		return res;
	}

	public boolean booleanDataTableFilter(Object value, Object filter, Locale locale) {
		return true;
	}

	public static String getDateFormatted(Date date) {
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
		return df.format(date);
	}

	public static String getDateFormattedSmart(Date date) {
		DateFormat df = new SimpleDateFormat("dd.MM.yy");
		return df.format(

				date);
	}

	public static String getDateMonth(Date date) {
		DateFormat df = new SimpleDateFormat("MM");
		return df.format(date);
	}

	public static String getDateYearYY(Date date) {
		DateFormat df = new SimpleDateFormat("yy");
		return df.format(date);
	}

	public static String getDateYear(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy");
		return df.format(date);
	}

	public String getOnlyTimeFormat() {
		return "HH:mm";
	}

	public String getDateFormat() {
		return "dd.MM.yyyy HH:mm";
	}

	public String getDateWithoutTime() {
		return "dd.MM.yyyy";
	}

	public String getDateOnlyYear() {
		return "yyyy";
	}

	public static String getDateFormattedWithoutTime(Date date) {
		DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
		return df.format(date);
	}

	public List<SelectItem> getUsersList() {
		return usersList;
	}

	public void setUsersList(List<SelectItem> usersList) {
		this.usersList = usersList;
	}

	public List<SelectItem> getMessageLevelList() {
		return messageLevelList;
	}

	public void setMessageLevelList(List<SelectItem> messageLevelList) {
		this.messageLevelList = messageLevelList;
	}

	public void logging(String reporter, String message, String message_level) {
		String user;
		try
		{
			user = Faces.getRemoteUser();
			user = (user == null) ? "Guest" : user;
		}
		catch(Exception e)
		{
			user = "Guest";			
		}
		logging(reporter, message, message_level, user);
	}

	public void logging(String reporter, String message, String message_level, String user) {
		// String desiredLoggingLevel = getConfigParam(reporter.toUpperCase());
		// boolean logFlag = desiredLoggingLevel.equals(message_level) ||
		// message_level.equals(Constants.MessageLevel.ERROR);
		//
		// if (logFlag) {
		Logging logging = new Logging();
		logging.setCreateDate(new Date());
		logging.setReporter(reporter);
		logging.setMessage(message);
		logging.setUser(user);
		logging.setMessageLevel(message_level);
		logging.setIp(Faces.getRemoteAddr());
		try
		{
		loggingFacade.create(logging);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		// }
	}

	public void createDirectory(File file) {
		// if the directory does not exist, create it
		if (!file.exists()) {
			System.out.println("creating directory: " + file.getAbsolutePath());
			boolean result = false;

			try {
				file.mkdir();
				result = true;
			} catch (SecurityException se) {
				// handle it
			}
			if (result) {
				System.out.println("DIR created");
			}
		}

	}

	public static Date addMonths(Date date, Integer months) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months);
		return cal.getTime();
	}

	public static Date addYears(Date date, Integer years) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, years);
		return cal.getTime();
	}

	public static Date addOneHourToDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, 1);
		return cal.getTime();
	}

	public static Date setDateToBeginOfYear2016() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 1);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.YEAR, 2016);
		return cal.getTime();
	}

	public static Date setDateToBeginOfYear2015(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 1);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.YEAR, 2015);
		return cal.getTime();
	}

	public static Date setDateToEndOfLastYear2015(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		cal.set(Calendar.YEAR, 2015);
		return cal.getTime();
	}

	public static Date setDateToBeginOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 1);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	public static Date setDateToEndOfYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DAY_OF_MONTH, 31);
		return cal.getTime();
	}

	public static Date setDateToBeginOfDayZero(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.add(Calendar.SECOND, -1);
		return cal.getTime();
	}

	public static Date setDateToBeginOfDayPlusWhenNotSet(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cal.get(Calendar.HOUR) == 0 && cal.get(Calendar.MINUTE) == 0 && cal.get(Calendar.SECOND) == 0) {
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.SECOND, 59);
		}
		return cal.getTime();
	}

	public static Date setDateToBeginOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	public static Date setDateToEndOfDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	public static Date setOuterBeginDate() {
		return new Date(0);
	}

	public static Date setOuterEndDate() {
		SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
		try {
			return df.parse("31123000");
		} catch (ParseException ex) {
			Logger.getLogger(ApplicationBean.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}

	public static Date setStartDateEinsaetzeTest() {
		SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
		try {
			return df.parse("01012015");
		} catch (ParseException ex) {
			Logger.getLogger(ApplicationBean.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}

	public static Date setEndDateEinsaetzeTest() {
		SimpleDateFormat df = new SimpleDateFormat("ddMMyyyy");
		try {
			return df.parse("31122015");
		} catch (ParseException ex) {
			Logger.getLogger(ApplicationBean.class.getName()).log(Level.SEVERE, null, ex);
		}

		return null;
	}

	public List<Users> getReporterList() {
		return usersFacade.findAll();
	}

	public void setCategoriesList(List<SelectItem> categoriesList) {
		this.categoriesList = categoriesList;
	}

	public StreamedContent getImage() {
		return null;
	}

//	public void redirectToMonitoring() {
//		try {
//			Faces.redirect("monitoring");
//		} catch (IOException ex) {
//			Logger.getLogger(ApplicationBean.class.getName()).log(Level.SEVERE, null, ex);
//		}
//	}

	public static Date getToday() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		Date res = cal.getTime();
		res = ApplicationBean.setDateToBeginOfDay(res);
		return res;
	}

	public static Date getFirstOfActualYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(cal.get(Calendar.YEAR), Calendar.JANUARY, 1, 0, 0);
		return cal.getTime();
	}

	public static Date getLastOfActualYear(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(cal.get(Calendar.YEAR), Calendar.DECEMBER, 31, 23, 59);
		return cal.getTime();
	}

	public static Date getFirstOfActualMonth() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date res = cal.getTime();
		res = ApplicationBean.setDateToBeginOfDay(res);
		return res;
	}

	public static Date getLastDayOfActualMonth() {
		Integer day = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH, day);
		Date res = cal.getTime();
		res = ApplicationBean.setDateToEndOfDay(res);
		return res;
	}

	public static Date nextMonthFirstDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, +1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	public static Date prevMonthFirstDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	public static Date nextMonthLastDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, +1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	public static Date prevMonthLastDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	public static Date nextMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, +1);
		return cal.getTime();
	}

	public static Date prevMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		return cal.getTime();
	}

	public static Boolean getlangFromURLIsFrench() {
		String lang = Faces.getRequestParameter("lang");
		if (lang != null && lang.equals("fr")) {
			return true;
		} else {
			return false;
		}
	}

	public Integer getBuildingSiteIdFromUrl() {
		String id_s = Faces.getRequestParameter("buildingSiteId");
		Integer id = -1;
		try {
			id = Integer.parseInt(id_s);
		} catch (NumberFormatException e) {
			return null;
		}
		return id;
	}

	public Integer getContractIdFromUrl() {
		String id_s = Faces.getRequestParameter("serviceContractID");
		Integer id = -1;
		try {
			id = Integer.parseInt(id_s);
		} catch (NumberFormatException e) {
			return null;
		}
		return id;
	}

	public Integer getserviceIDFromURL() {
		String id_s = Faces.getRequestParameter("serviceID");
		Integer id = -1;
		try {
			id = Integer.parseInt(id_s);
		} catch (NumberFormatException e) {
			return null;
		}
		return id;
	}
	
	public static String getWithLeadingZero(Integer val) {
		String res = "";
		if (val < 10) {
			if (val == 0) {
				res = "00";
			} else {
				res = "0" + val.toString();
			}
		} else {
			res = val.toString();
		}
		return res;
	}
	
	
	public static String splitToComponentTimeString(Integer sec) {
		Integer hours = sec / 3600;
		Integer remainder = (Integer) sec - hours * 3600;
		Integer mins = remainder / 60;
		remainder = remainder - mins * 60;
		Integer secs = remainder;
		String res = "";

		String h = getWithLeadingZero(hours);
		String m = getWithLeadingZero(mins);
		String s = getWithLeadingZero(secs);

		return h + ":" + m + ":" + s;

	}

	public List<Integer> getElements() {
		List<Integer> resList = new ArrayList<>();
		String id_s = Faces.getRequestParameter("elements");
		if(id_s != null && id_s.length() > 0){			
			String[] idsArr;
			Integer id = -1;
			idsArr = id_s.split(",");
			for (String i : idsArr) {
				try {
					Integer j = Integer.parseInt(i);
					resList.add(j);
				} catch (NumberFormatException e) {
					// TODO: handle exception
				}
			}
		}
		
		return resList;

	}

	public Integer getContractId() {
		String id_s = Faces.getRequestParameter("contractId");
		Integer id = -1;
		try {
			id = Integer.parseInt(id_s);
		} catch (NumberFormatException e) {
			return null;
		}
		return id;
	}

	public Integer getIdFromURL() {
		String id_s = Faces.getRequestParameter("id");
		Integer id = -1;
		try {
			id = Integer.parseInt(id_s);
		} catch (NumberFormatException e) {
			return null;
		}
		return id;
	}

	public Integer getCustomerIdFromURL() {
		String id_s = Faces.getRequestParameter("customerID");
		Integer id = -1;
		try {
			id = Integer.parseInt(id_s);
		} catch (NumberFormatException e) {
			return null;
		}
		return id;
	}

	public String gSp(Integer count) {
		String result = "";
		for (int i = 1; i <= count; i++) {
			result += " ";
		}
		return result;
	}

	public Integer getEditModeFromURL() {
		String id_s = Faces.getRequestParameter("editMode");
		Integer id = -1;
		try {
			id = Integer.parseInt(id_s);
		} catch (NumberFormatException e) {
			return null;
		}
		return id;
	}

	public static void main(String[] args) {
		try {
			// wmic command for diskdrive id: wmic DISKDRIVE GET SerialNumber
			// wmic command for cpu id : wmic cpu get ProcessorId
			Process process = Runtime.getRuntime().exec(new String[] { "wmic", "bios", "get", "serialnumber" });
			process.getOutputStream().close();
			sc = new Scanner(process.getInputStream());
			String property = sc.next();
			String serial = sc.next();
			System.out.println(property + ": " + serial);
		} catch (IOException ex) {
			Logger.getLogger(ApplicationBean.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void reloadPage() {
		try {
			ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {

		}
	}

	public String getYearFromDate(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String year = String.valueOf(cal.get(Calendar.YEAR));

		return year;
	}

	public String getPathToFileArchiv(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		String year = String.valueOf(cal.get(Calendar.YEAR));
		String path = getPathToFileArchiv();

		return path + "/" + year;
	}

	public HashMap<String, String> getConfig() {
		return config;
	}

	public void setConfig(HashMap<String, String> config) {
		this.config = config;
	}

	public List<SelectItem> getCategoriesList() {
		return categoriesList;
	}

	/**
	 * @param query
	 * @return
	 */
	public List<Country> completeCountries(String query) {

		ValueExpression exp = Faces.getApplication().getExpressionFactory().createValueExpression(
				Faces.getContext().getELContext(), "#{c.alpha2}" + " - " + "#{c.name}", String.class);
		AutoComplete autoComplete = (AutoComplete) UIComponent.getCurrentComponent(Faces.getContext());
		autoComplete.setValueExpression("itemLabel", exp);
		List<Country> ct = countryFacade.findByName(query);
		return ct;
	}

	/**
	 * @param query
	 * @return
	 */
	public List<ServiceContract> completeServiceContractNumber(String query) {
		ValueExpression exp = Faces.getApplication().getExpressionFactory()
				.createValueExpression(Faces.getContext().getELContext(), "#{c.serviceContractNumber}", String.class);
		AutoComplete autoComplete = (AutoComplete) UIComponent.getCurrentComponent(Faces.getContext());
		autoComplete.setValueExpression("itemLabel", exp);
		List<ServiceContract> sc = serviceContractFacade.findForAutocomplete(query);
		return sc;
	}

	/**
	 * @param query
	 * @return
	 */
	public List<BuildingSite> completeBuildingSite(String query) {
		ValueExpression exp = Faces.getApplication().getExpressionFactory()
				.createValueExpression(Faces.getContext().getELContext(), "#{c.code} - #{c.name}", String.class);
		AutoComplete autoComplete = (AutoComplete) UIComponent.getCurrentComponent(Faces.getContext());
		autoComplete.setValueExpression("itemLabel", exp);
		List<BuildingSite> bs = buildingSiteFacade.findForAutocomplete(query);
		return bs;
	}

	/**
	 * @param query
	 * @return
	 */
	public List<Users> completeCustomerUser(String query) {
		ValueExpression exp = Faces.getApplication().getExpressionFactory().createValueExpression(
				Faces.getContext().getELContext(), "#{c.name}" + ", " + "#{c.prename}", String.class);
		AutoComplete autoComplete = (AutoComplete) UIComponent.getCurrentComponent(Faces.getContext());
		autoComplete.setValueExpression("itemLabel", exp);
		List<Users> p = usersFacade.findAllCustomers(query);
		return p;
	}

	/**
	 * @param query
	 * @return
	 */
	public List<Person> completePerson(String query) {
		ValueExpression exp = Faces.getApplication().getExpressionFactory().createValueExpression(
				Faces.getContext().getELContext(), "#{c.lastname}" + ", " + "#{c.firstname}", String.class);
		AutoComplete autoComplete = (AutoComplete) UIComponent.getCurrentComponent(Faces.getContext());
		autoComplete.setValueExpression("itemLabel", exp);
		List<Person> p = personFacade.findCustomersByName(query);
		return p;
	}

	/**
	 * @param query
	 * @return
	 */
	public List<Person> completeTechnician(String query) {
		ValueExpression exp = Faces.getApplication().getExpressionFactory().createValueExpression(
				Faces.getContext().getELContext(), "#{c.lastname}" + ", " + "#{c.firstname}", String.class);
		AutoComplete autoComplete = (AutoComplete) UIComponent.getCurrentComponent(Faces.getContext());
		autoComplete.setValueExpression("itemLabel", exp);
		List<Person> p = personFacade.findTechnicianByName(query);
		return p;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	public String getTest2() {
		return test2;
	}

	public void setTest2(String test2) {
		this.test2 = test2;
	}

	public String getPathToFileArchiv() {
		return pathToFileArchiv;
	}

	public void setPathToFileArchiv(String pathToFileArchiv) {
		this.pathToFileArchiv = pathToFileArchiv;
	}

	public List<LegendToolTip> getLegendOffer() {
		return legendOffer;
	}

	public void setLegendOffer(List<LegendToolTip> legendOffer) {
		this.legendOffer = legendOffer;
	}

	public List<LegendToolTip> getLegendHistorie() {
		return legendHistorie;
	}

	public void setLegendHistorie(List<LegendToolTip> legendHistorie) {
		this.legendHistorie = legendHistorie;
	}

}
