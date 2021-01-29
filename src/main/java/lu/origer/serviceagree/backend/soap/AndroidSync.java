package lu.origer.serviceagree.backend.soap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.ejb.Stateless;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.net.ssl.HttpsURLConnection;
import javax.sql.DataSource;

import lu.origer.serviceagree.models.contact.types.DinType;
import lu.origer.serviceagree.models.main.FileArchive;
import lu.origer.serviceagree.models.synch.SynchJobs;

/**
 * 
 * @author ian.husting
 *
 */
@Stateless
public class AndroidSync {
	private static Long TECHNICIAN_TYPE_ID = 5L;	
	private static String SYNC_URL_PROD_CONTROL = "http://maintenance.origer.lu:57080/origer/synchroniZe/origer/";
	private static String SYNC_URL_PROD_CONTROL_TEST = "http://maintenance.origer.lu:56080/origer/synchroniZe/origer/";
	public static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	public BuildingSiteSO[] getEligibleSites(final Date startDate, final Date endDate) {

		ArrayList<BuildingSiteSO> sites = new ArrayList<>();
		if (startDate != null && endDate != null) {
			try {
				Context context = new InitialContext();
				DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/origer_pc_test");

				Connection conn = dataSource.getConnection();
				Statement stmt = conn.createStatement();

				java.sql.Date bsStartDate = new java.sql.Date(startDate.getTime());
				java.sql.Date bsEndDate = new java.sql.Date(endDate.getTime());

				ResultSet rs = stmt.executeQuery(
						"SELECT DISTINCT BS.ID AS BS_ID, BS.NAME AS BS_NAME " + "FROM building_site AS BS "
								+ "INNER JOIN service_contract AS SC ON BS.ID = SC.FK_BUILDING_SITE "
								+ "INNER JOIN service AS IV ON IV.FK_SERVICE_CONTRACT = SC.ID "
								+ "WHERE IV.LATEST_SERVICE_DATE BETWEEN \'" + bsStartDate + "\' AND \'" + bsEndDate
								+ "\' " + "AND IV.ACTIVE = true");
				if (rs != null) {
					ResultsetMapper mapper = new ResultsetMapper();
					while (rs.next()) {
						sites.add(mapper.resultsetToBuildingSiteSO(rs, false));
					}
				}
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		return sites.toArray(new BuildingSiteSO[sites.size()]);
	}

	public SoapParcel downloadData(final SoapParcel bsIds, final Date startDate, final Date endDate) {
		final SoapParcel parcel = new SoapParcel();
		if (bsIds != null && bsIds.getPrintRequestElements() != null && bsIds.getPrintRequestElements().length > 0) {
			System.out.println("Received ids");
			String idList = "(";

			for (int c = 0; c < bsIds.getPrintRequestElements().length; c++) {
				idList += bsIds.getPrintRequestElements()[c].toString();

				if (c != bsIds.getPrintRequestElements().length - 1) {
					idList += ",";
				}
			}
			idList += ")";

			try {
				Context context = new InitialContext();
				final ArrayList<ServiceSO> services = new ArrayList<>();
				final ArrayList<SyncControlsSO> controls = new ArrayList<>();
				final ArrayList<OfferSO> offers = new ArrayList<>();
				final ArrayList<FileArchiveSO> archives = new ArrayList<>();
				final ArrayList<ServiceTechnicianAssocSO> assocs = new ArrayList<>();
				DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/origer_pc_test");

				Connection conn = dataSource.getConnection();
				Statement stmt = conn.createStatement();
				Statement offerStmt = conn.createStatement();
				ResultSet rs = null;

				if (endDate != null) {
					// End date found, use it to prevent downloading entries from future services.					
					java.sql.Date bsStartDate = new java.sql.Date(startDate.getTime());
					java.sql.Date bsEndDate = new java.sql.Date(endDate.getTime());					
					final String query = "SELECT DISTINCT SE.*, "
							+ "SC.ID AS SC_ID, SC.ACTIVE AS SC_ACTIVE, SC.DESCRIPTION AS SC_DESCRIPTION, SC.FROM_DATE, SC.TO_DATE, SC.FK_BUILDING_SITE, "
							+ "BS.ID AS BS_ID, BS.ACTIVE AS BS_ACTIVE, BS.DESCRIPTION AS BS_DESCRIPTION, BS.NAME AS BS_NAME, BS.SHN AS BS_SHN, BS.CODE,  "
							+ "ST.ID AS ST_ID, ST.ACTIVE AS ST_ACTIVE, ST.DESCRIPTION AS ST_DESCRIPTION, ST.NAME AS ST_NAME, ST.SHN AS ST_SHN "
							+ "FROM service AS SE "
							+ "INNER JOIN service_contract AS SC ON SC.ID = SE.FK_SERVICE_CONTRACT "
							+ "INNER JOIN building_site AS BS ON BS.ID = SC.FK_BUILDING_SITE "
							+ "INNER JOIN service_type AS ST ON ST.ID = SE.FK_SERVICE_TYPE "
							+ "WHERE SE.LATEST_SERVICE_DATE <= \'" + bsEndDate + "\' AND BS.ID IN " + idList + " "
							+ "GROUP BY SE.ID, SC_ID, BS_ID, ST_ID";					
					rs = stmt.executeQuery(query);
				} else {					
					rs = stmt.executeQuery("SELECT DISTINCT SE.*, "
							+ "SC.ID AS SC_ID, SC.ACTIVE AS SC_ACTIVE, SC.DESCRIPTION AS SC_DESCRIPTION, SC.FROM_DATE, SC.TO_DATE, SC.FK_BUILDING_SITE, "
							+ "BS.ID AS BS_ID, BS.ACTIVE AS BS_ACTIVE, BS.DESCRIPTION AS BS_DESCRIPTION, BS.NAME AS BS_NAME, BS.SHN AS BS_SHN, BS.CODE,  "
							+ "ST.ID AS ST_ID, ST.ACTIVE AS ST_ACTIVE, ST.DESCRIPTION AS ST_DESCRIPTION, ST.NAME AS ST_NAME, ST.SHN AS ST_SHN "
							+ "FROM service AS SE "
							+ "INNER JOIN service_contract AS SC ON SC.ID = SE.FK_SERVICE_CONTRACT "
							+ "INNER JOIN building_site AS BS ON BS.ID = SC.FK_BUILDING_SITE "
							+ "INNER JOIN service_type AS ST ON ST.ID = SE.FK_SERVICE_TYPE "
							+ "INNER JOIN service AS IV ON IV.FK_SERVICE_CONTRACT = SC.ID " + "WHERE BS.ID IN " + idList
							+ " " + "GROUP BY SE.ID, SC_ID, BS_ID, ST_ID");
				}

				if (rs != null) {
					final ArrayList<Integer> ids = new ArrayList<>();
					final ArrayList<Integer> controlIds = new ArrayList<>();
					final ResultsetMapper mapper = new ResultsetMapper();
					while (rs.next()) {
						if (rs.getObject(1) != null) {
							services.add(mapper.resultsetToServiceSO(rs, true));
							ids.add(mapper.resultsetToServiceSO(rs, false).getId());
						}
					}

					rs.close();

					// Get checklists
					for (Integer id : ids) {
						ResultSet rsControls = stmt
								.executeQuery("SELECT SYC.ID AS SYC_ID, SYC.fk_element, SYC.startTime, SYC.endTime, "
										+ "SYC.CHECKING_SEC, SYC.ERROR_HISTORY_FLAG, SYC.SETUP_SEC, "
										+ "SYC.DESCRIPTION AS SYC_DESCRIPTION, SYC.FK_CHECKLISTITEM, "
										+ "SYC.VISUALCONTROL, SYC.FUNCTIONALCONTROL, SYC.ISOK, SYC.ISLACKING, "
										+ "SYC.ISDEFECT, SYC.CREATEOFFER, SYC.FK_SERVICE, SYC.ISREPAIRED, SYC.VF, "
										+ "SL.ID AS SL_ID, SL.NAME AS SL_NAME, SL.FLOOR, SL.ROOM, SL.ACTIVE AS SL_ACTIVE, SL.ELEMENTNUMBER, SL.FK_DIN_TYPE, SL.DESCRIPTION AS ELEMENT_DESCRIPTION, "
										+ "SL.FK_ELEMENT_TYPE, SL.PRINT_NEW_BARCODE, SL.LAST_SERVICE_DATE, SL.ELEMENT_STATE, "
										+ "OT.ID AS SL_ORIENTATION_NAME, FT.NAME AS SL_FRONT_NAME, "
										+ "ET.ID AS SET_ID, ET.NAME AS SET_NAME, ET.ACTIVE AS SET_ACTIVE, "
										+ "CI.ID AS CI_ID, CI.NAME AS CI_NAME, CI.ACTIVE AS CI_ACTIVE, CI.FK_CATEGORY, "
										+ "CIC.ID AS CIC_ID, CIC.NAME AS CIC_NAME, CIC.SHN AS CIC_SHN, CIC.DESCRIPTION AS CIC_DESCRIPTION, "
										+ "service.ID " + "FROM service_history AS SYC "
										+ "LEFT JOIN service_elements AS SL ON SL.ID = SYC.FK_ELEMENT "
										+ "LEFT JOIN orientation_type AS OT ON OT.ID = SL.FK_ORIENTATION_TYPE "
										+ "LEFT JOIN front_type AS FT ON FT.ID = SL.FK_FRONT_TYPE "
										+ "LEFT JOIN element_type AS ET ON ET.ID = SL.FK_ELEMENT_TYPE "
										+ "LEFT JOIN checklist_items AS CI ON CI.ID = SYC.FK_CHECKLISTITEM "
										+ "LEFT JOIN checklist_item_category AS CIC ON CIC.ID = CI.FK_CATEGORY "
										+ "LEFT JOIN service ON service.ID = SYC.FK_SERVICE " + "WHERE service.ID = "
										+ id);

						if (rsControls != null) {
							while (rsControls.next()) {
								final SyncControlsSO temp = mapper.resultsetToSyncControlsSO(rsControls, true);
								controlIds.add(temp.getId());
								// Get offers for elements
								if (temp.getElement() != null) {
									ResultSet rsOffers = offerStmt.executeQuery(
											"SELECT OFF.ID AS OF_ID, OFF.NAME AS OF_NAME, OFF.ACTIVE AS OF_ACTIVE, OFF.OFFERDATE, OFF.AMOUNT, OFF.STATE "
													+ "FROM offers AS OFF "
													+ "INNER JOIN assoc_offers_elements AS ASSOC ON OFF.ID = ASSOC.FK_OFFER "
													+ "WHERE ASSOC.FK_ELEMENT = " + temp.getElement().getId());
									if (rsOffers != null) {
										final ArrayList<Integer> offerIds = new ArrayList<>();
										while (rsOffers.next()) {
											final OfferSO tempOffer = mapper.resultSetToOfferSO(rsOffers);
											offers.add(tempOffer);
											offerIds.add(tempOffer.getId());
										}
										temp.getElement().setOfferIds(new Integer[offerIds.size()]);
										temp.getElement()
												.setOfferIds(offerIds.toArray(temp.getElement().getOfferIds()));
									}
									rsOffers.close();
									// Get offer-element assocs									
									final String fuQuery = "SELECT AOE.ID, AOE.FK_OFFER AS AOE_OFFER, AOE.FK_ELEMENT AS AOE_ELEMENT, AOE.OFFER_STATE AS AOE_OFFER_STATE "
											+ "FROM assoc_offers_elements AS AOE " + "WHERE AOE.FK_ELEMENT = "
											+ temp.getElement().getId();									
									ResultSet rsAOE = offerStmt.executeQuery(fuQuery);
									if (rsAOE != null) {
										// Hijack technician assocs for offer assoc transfer
										while (rsAOE.next()) {
											assocs.add(mapper.resultsetToElementOfferAssocSO(rsAOE));											
										}
										rsAOE.close();
									}
								}
								controls.add(temp);
							}
						}
						rsControls.close();
					}

					// Get file archives & files
					if (controlIds != null && controlIds.size() > 0) {
						for (int c = 0; c <= controlIds.size() - 1; c++) {
							ResultSet rsArchives = stmt.executeQuery(
									"SELECT DISTINCT FA.ID AS FA_ID, FA.URL, FA.FK_SERVICE_HISTORY, FA.NAME AS FA_NAME, FA.DESCRIPTION AS FA_DESCRIPTION, FA.FK_BUILDING_SITE AS FA_SITE, FA.IS_SIGNATURE "
											+ "FROM filearchive AS FA " + "WHERE FA.FK_SERVICE_HISTORY = "
											+ controlIds.get(c));

							if (rsArchives != null) {
								while (rsArchives.next()) {
									final FileArchiveSO tempArchive = mapper.resultsetToFileArchiveSO(rsArchives);
									if (tempArchive != null
											&& (tempArchive.getUrl() != null && tempArchive.getName() != null)) {
										try {
											File f = new File(tempArchive.getUrl());
											InputStream input = new FileInputStream(f);
											tempArchive.setData(new byte[(int) f.length()]);
											input.read(tempArchive.getData());
											input.close();
										} catch (Exception e) {
											e.printStackTrace();
										} finally {
											// Only add archive if file has been
											// found
											archives.add(tempArchive);
										}
									}
								}
							}
							rsArchives.close();
						}
					}

					// Get buildingsite archives
					System.out.println("DOWNLOAD - Fetching buildingsite plans");
					ResultSet rsArchives = stmt.executeQuery(
							"SELECT DISTINCT FA.ID AS FA_ID, FA.URL, FA.FK_SERVICE_HISTORY, FA.NAME AS FA_NAME, FA.DESCRIPTION AS FA_DESCRIPTION, FA.FK_BUILDING_SITE AS FA_SITE, FA.IS_SIGNATURE "
									+ "FROM filearchive AS FA " + "WHERE FA.FK_BUILDING_SITE IN " + idList
									+ " AND FA.SYNC = TRUE");
					if (rsArchives != null) {
						while (rsArchives.next()) {
							final FileArchiveSO tempArchive = mapper.resultsetToFileArchiveSO(rsArchives);							
							if (tempArchive != null
									&& (tempArchive.getUrl() != null && tempArchive.getName() != null)) {
								try {									
									File f = new File(tempArchive.getUrl());
									InputStream input = new FileInputStream(f);
									tempArchive.setData(new byte[(int) f.length()]);
									input.read(tempArchive.getData());
									input.close();
								} catch (Exception e) {
									System.out.println("DOWNLOAD - Fetching data failed");
									e.printStackTrace();
								} finally {
									// Only add archive if file has been
									// found									
									archives.add(tempArchive);
								}
							}
						}
					}
					rsArchives.close();

					final ServiceSO[] resultServices = new ServiceSO[services.size()];
					final SyncControlsSO[] resultControls = new SyncControlsSO[controls.size()];
					final OfferSO[] resultOffers = new OfferSO[offers.size()];
					final FileArchiveSO[] resultArchive = new FileArchiveSO[archives.size()];
					final ServiceTechnicianAssocSO[] resultAssoc = new ServiceTechnicianAssocSO[assocs.size()];

					parcel.setServices(services.toArray(resultServices));
					parcel.setControls(controls.toArray(resultControls));
					parcel.setOffers(offers.toArray(resultOffers));
					parcel.setFileArchive(archives.toArray(resultArchive));
					parcel.setTechnicians(assocs.toArray(resultAssoc));
					parcel.setServiceSize(resultServices.length);
					parcel.setControlSize(resultControls.length);
					parcel.setOfferSize(resultOffers.length);
					parcel.setFileArchiveSize(resultArchive.length);
					parcel.setTechSize(resultAssoc.length);
					rs.close();
					stmt.close();
					conn.close();
					System.out.println("Done");
				}

			} catch (NamingException | SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		return parcel;
	}

	public Boolean uploadData(final SoapParcel uploadData) {
		System.out.println("CALLED UPLOAD!");
		Boolean success = false;

		if (uploadData != null) {
			String jobDescription = "";
			final SynchJobs jobs = new SynchJobs();
			jobs.setTargetElementsCount(0);
			jobs.setActualCountedElements(0);

			if (this.validateSoapParcel(uploadData, jobs)) {
				// TODO Remove dis				
				try
				{
					jobDescription = "Controls: " + (uploadData.getControlSize() != null ? uploadData.getControlSize() : "0") 
					+  " Elements: " + (uploadData.getElementsSize() != null ? uploadData.getElementsSize() : "0")
					+ " Archives: " + (uploadData.getFileArchiveSize()!= null ? uploadData.getFileArchiveSize() : "0")
					+ " Offers: " + (uploadData.getOfferSize()!= null ? uploadData.getOfferSize() : "0")
					+ " Services "  + (uploadData.getServiceSize()!= null ? uploadData.getServiceSize() : "0")
					+ " Technicians: " + (uploadData.getTechSize()!= null ? uploadData.getTechSize() : "0")
					+ " Times: " + (uploadData.getRecordingSize()!= null ? uploadData.getRecordingSize() : "0");
				}
				catch(Exception e)
				{
					jobDescription = "Transfer complete";
				}

				if(!jobs.getDescription().isEmpty())
				{
					jobDescription += " " + jobs.getDescription();
				}
					
				System.out.println("UPDATE - Parcel-check completed, status: " + jobDescription);

				Context context;
				try {
					context = new InitialContext();
					DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/origer_pc_test");
					Connection conn = dataSource.getConnection();
					Statement stmt = conn.createStatement();
					SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_PATTERN);

					Integer jobId = null;
					final String pathQuery = "SELECT VALUE FROM config WHERE PK_KEY = \'UNIX_FILEARCHIVE_PATH\'";
					ResultSet pathSet = stmt.executeQuery(pathQuery);
					pathSet.next();
					String imgPath = pathSet.getString("VALUE");
					pathSet.close();

					final String jobQuery = "INSERT INTO synch_jobs(TARGET_ELEMENTS, ACTUAL_ELEMENTS, STATE_ANDROID, CREATE_DATE, DESCRIPTION) "
							+ "VALUES (" + jobs.getTargetElementsCount() + "," + jobs.getActualCountedElements() + ",\'"
							+ jobs.getAndroidState() + "\', CURRENT_TIMESTAMP() ,\'"
							+ jobDescription.replaceAll("'", "") + "\')";
					stmt.executeUpdate(jobQuery);
					final String getJobId = "SELECT ID FROM synch_jobs ORDER BY ID DESC LIMIT 1";
					ResultSet jobIdSet = stmt.executeQuery(getJobId);

					if (jobIdSet != null) {
						jobIdSet.next();
						jobId = jobIdSet.getInt("ID");
						jobs.setId(jobId);
					}
					jobIdSet.close();					

					if (uploadData.getPrintRequestElements() != null
							&& uploadData.getPrintRequestElements().length > 0) {
						final ArrayList<Integer> updateIds = new ArrayList<Integer>(
								Arrays.asList(uploadData.getPrintRequestElements()));
						String updateElements = "UPDATE service_elements SET PRINT_NEW_BARCODE = TRUE, FK_EDIT_USER = -1 , EDIT_DATE = CURRENT_TIMESTAMP() WHERE ID IN (";
						for (int c = 0; c < updateIds.size(); c++) {
							updateElements += updateIds.get(c);
							if (c != updateIds.size() - 1) {
								updateElements += ",";
							}
						}
						updateElements += ")";
						stmt.executeUpdate(updateElements);
					}

					for (SyncControlsSO control : uploadData.getControls()) {
						final String query = "REPLACE INTO synch_controls(ID, STARTTIME, ENDTIME, DESCRIPTION, "
								+ "VISUALCONTROL, FUNCTIONALCONTROL, ISOK, ISDEFECT, ISLACKING, CREATEOFFER, FK_SERVICE, "
								+ "FK_CHECKLISTITEM, FK_ELEMENT, FK_SYNCH_JOBS, ISREPAIRED, ERROR_HISTORY_FLAG, VF) "
								+ "VALUES (" + control.getId() + ","
								+ (control.getStartTime() == null ? "null"
										: "\'" + formatter.format(control.getStartTime()) + "\'")
								+ ","
								+ (control.getEndTime() == null ? "null"
										: "\'" + formatter.format(control.getEndTime()) + "\'")
								+ ",\'" + control.getDescription().replaceAll("'", "") + "\',"
								+ control.getVisualControl() + "," + control.getFunctionalControl() + ","
								+ control.getIsOk() + "," + control.getIsDefect() + "," + control.getIsFaulty() + ","
								+ control.getCreateOffer() + "," + control.getService().getId() + ","
								+ control.getChecklistItem().getId() + "," + control.getElement().getId() + "," + jobId
								+ "," + control.getIsRepaired() + "," + control.getErrorHistoryFlag() + ","
								+ control.getVf() + ")";
						stmt.executeUpdate(query);						
						if (control.getService() != null && (control.getService().getDescription() != null
								&& !control.getService().getDescription().isEmpty())) {
							final String updateServiceQuery = "UPDATE service SET DESCRIPTION = \'"
									+ control.getService().getDescription().replaceAll("'", "")
									+ "\' WHERE service.ID = " + control.getService().getId();
							stmt.executeUpdate(updateServiceQuery);							
						}
					}

					// Store the new file archive IDs for later use
					final ArrayList<ServiceTechnicianAssocSO> rosetta = new ArrayList<>();

					for (TimeRecordingSO recording : uploadData.getRecordings()) {
						java.sql.Timestamp bsDate = null;
						if (recording.getRecordingDate() != null) {
							bsDate = new java.sql.Timestamp(recording.getRecordingDate().getTime());
						} else {
							bsDate = new java.sql.Timestamp(new Date().getTime());
						}
						final String query = "INSERT INTO synch_time_recordings(TIME_IN_SECONDS, FK_SERVICE, FK_FILE_ARCHIVE, FK_MECHANIC, RECORD_DATE, TYPE, FK_ELEMENT, FK_SYNCH_JOB, DESCRIPTION, FK_BUILDING_SITE) "
								+ "VALUES(" + recording.getSeconds() + ","
								+ (recording.getService() == null ? "null" : recording.getService().getId()) + ","
								+ (recording.getSignature() == null ? "null" : recording.getSignature().getId()) + ","
								+ recording.getMechanic() + ",\'" + bsDate + "\', \'" + recording.getType() + "\',"
								+ (recording.getElement() == null ? "null" : recording.getElement().getId()) + ","
								+ jobId + ",\'"
								+ (recording.getComment() == null ? "" : recording.getComment().replaceAll("'", ""))
								+ "\'," + (recording.getSite() == null ? "null" : recording.getSite()) + ")";
						stmt.executeUpdate(query);						

						if (recording.getType().equals("RGZ")) {
							final String idQuery = "SELECT id FROM synch_time_recordings ORDER BY id DESC";
							ResultSet latestId = stmt.executeQuery(idQuery);

							if (latestId != null) {
								if (latestId.first()) {
									final ServiceTechnicianAssocSO temp = new ServiceTechnicianAssocSO();
									temp.setServiceId(latestId.getLong("id"));
									temp.setTechnicianId(recording.getId());									
									rosetta.add(temp);
								}
								latestId.close();
							}
						}
					}

					if (imgPath != null && !"".equals(imgPath) && uploadData.getFileArchive() != null) {
						for (FileArchiveSO archive : uploadData.getFileArchive()) {
							String folderName = "";
							SimpleDateFormat format = new SimpleDateFormat("yyyy");
							if (archive.getCreateDate() != null) {
								folderName = format.format(archive.getCreateDate());
							} else {
								folderName = format.format(new Date());
							}
							File subFolder = new File(imgPath + File.separator + folderName);
							if (!subFolder.exists()) {
								subFolder.mkdir();
							}							
							if (archive.getData() != null) {
								File imgFile = new File(imgPath + "/" + folderName + "/" + archive.getName());
								if (!imgFile.exists()) {
									imgFile.createNewFile();
								}
								FileOutputStream fos = new FileOutputStream(imgFile);
								fos.write(archive.getData());
								fos.close();								
							}
							java.sql.Date bsDate = null;
							if (archive.getCreateDate() != null) {
								bsDate = new java.sql.Date(archive.getCreateDate().getTime());
							} else {
								bsDate = new java.sql.Date(new Date().getTime());
							}
							if (archive.getRecording() != null) {
								for (ServiceTechnicianAssocSO temp : rosetta) {
									if (temp.getTechnicianId().equals(archive.getRecording().getId())) {										
										archive.getRecording().setId(temp.getServiceId());
										break;
									}
								}
							}
							final String query = "REPLACE INTO filearchive(URL, FK_SERVICE_HISTORY, NAME, DESCRIPTION, CREATE_DATE, FK_SYNCH_TIME_RECORDING, FK_CREATE_USER, FK_SERVICE, SUBFOLDER, IS_SIGNATURE) "
									+ "VALUES (\'" + imgPath + "/" + folderName + "/" + archive.getName() + "\',"
									+ (archive.getControls() == null ? "null" : archive.getControls().getId()) + ",\'"
									+ archive.getName() + "\',\'" + archive.getDescription().replaceAll("'", "")
									+ "\',\'" + bsDate + "\',"
									+ (archive.getRecording() == null ? "null" : archive.getRecording().getId()) + ","
									+ (archive.getCreateUser() == null ? "null" : archive.getCreateUser().getId()) + ","
									+ (archive.getService() == null ? "null" : archive.getService().getId()) + ",\'"
									+ folderName + "\',"
									+ (archive.getIsSignature() == null ? false : archive.getIsSignature()) + ")";							
							stmt.executeUpdate(query);							
						}
					}
					if (uploadData.getElements() != null) {
						for (ServiceElementSO element : uploadData.getElements()) {
							String query = "UPDATE service_elements " + "SET ROOM = \'"
									+ (element.getRoom() == null ? "" : element.getRoom().replaceAll("'", "")) + "\',"
									+ "service_elements.FLOOR = \'"
									+ (element.getFloor() == null ? "" : element.getFloor().replaceAll("'", "")) + "\'"
									+ ((element.getElementType() != null && element.getElementType().getId() != null)
											? ", FK_ELEMENT_TYPE = " + element.getElementType().getId() + " "
											: " ")
									+ ((element.getOrientation() != null)
											? ", FK_ORIENTATION_TYPE = " + element.getOrientation() + " "
											: " ")
									+ ",FK_EDIT_USER = -1 , EDIT_DATE = CURRENT_TIMESTAMP(), FK_DIN_TYPE = "
									+ element.getDinId() + ", DESCRIPTION = \'"
									+ (element.getDescription() == null ? ""
											: element.getDescription().replaceAll("'", ""))
									+ "\' " + " WHERE id = " + element.getId();
							stmt.executeUpdate(query);							
						}
					}

					if (uploadData.getTechnicians() != null) {
						for (ServiceTechnicianAssocSO tech : uploadData.getTechnicians()) {
							String query = "INSERT INTO synch_service_technician(FK_SERVICE, FK_TECHNICIAN) "
									+ "VALUES(" + tech.getServiceId() + "," + tech.getTechnicianId() + ")";
							stmt.executeUpdate(query);							
						}
					}

					// Hijacked offers to transfer IDs of finished services
					if (uploadData.getOffers() != null && uploadData.getOffers().length > 0) {
						for (OfferSO service : uploadData.getOffers()) {
							String query = "INSERT INTO sync_service(FK_SERVICE, FK_SYNC_JOB, DESCRIPTION) VALUES ("
									+ service.getId() + "," + jobId + ",\'" + service.getName().replaceAll("'", "")
									+ "\')";
							stmt.executeUpdate(query);
						}
					}

					conn.close();
					success = true;					
					// Sync prod control
					URL url = new URL(SYNC_URL_PROD_CONTROL);
					HttpURLConnection connectionProdControl = (HttpURLConnection) url.openConnection();
					if (connectionProdControl.getResponseCode() == 200) {
						System.out.println("UPDATE - Prod control synchronsation successful." + connectionProdControl.getResponseCode());
					} else {
						System.out.println("UPDATE - Prod control on test synchronsation." + connectionProdControl.getResponseCode());
					}
					// Sync prod control test
//					url = new URL(SYNC_URL_PROD_CONTROL_TEST);
//					HttpURLConnection connectionProdControlTest = (HttpURLConnection) url.openConnection();
//					if (connectionProdControlTest.getResponseCode() == 200) {
//						System.out.println("UPDATE - Prod control synchronsation successful." + connectionProdControlTest.getResponseCode());
//					} else {
//						System.out.println("UPDATE - Prod control on test synchronsation." + connectionProdControlTest.getResponseCode());
//					}
				} catch (Exception e) {
					e.printStackTrace();
					success = false;
				}
			} else {
				try {
					Context context = new InitialContext();
					DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/origer_pc_test");

					Connection conn = dataSource.getConnection();
					Statement stmt = conn.createStatement();
					success = false;
					System.out.println("UPDATE - Parcel-check completed, status: " + jobDescription);
					final String jobQuery = "INSERT INTO synch_jobs(TARGET_ELEMENTS, ACTUAL_ELEMENTS, STATE_ANDROID, CREATE_DATE, DESCRIPTION) "
							+ "VALUES (" + jobs.getTargetElementsCount() + "," + jobs.getActualCountedElements() + ",\'"
							+ jobs.getAndroidState() + "\', CURRENT_TIMESTAMP() ,\'"
							+ jobs.getDescription().replaceAll("'", "") + "\')";
					stmt.executeUpdate(jobQuery);
					// Call URL to synchronize data.					
					URL url = new URL(SYNC_URL_PROD_CONTROL);
					HttpURLConnection connectionProdControl = (HttpURLConnection) url.openConnection();
					if (connectionProdControl.getResponseCode() == 200) {
						System.out.println("UPDATE - Prod control synchronsation successful." + connectionProdControl.getResponseCode());
					} else {
						System.out.println("UPDATE - Prod control on test synchronsation." + connectionProdControl.getResponseCode());
					}
					// Sync prod control test
//					url = new URL(SYNC_URL_PROD_CONTROL_TEST);
//					HttpURLConnection connectionProdControlTest = (HttpURLConnection) url.openConnection();
//					if (connectionProdControlTest.getResponseCode() == 200) {
//						System.out.println("UPDATE - Prod control synchronsation successful." + connectionProdControlTest.getResponseCode());
//					} else {
//						System.out.println("UPDATE - Prod control on test synchronsation." + connectionProdControlTest.getResponseCode());
//					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			success = false;
		}
		System.out.println("UPDATE - Done, success is " + success.toString());
		return success;
	}

	public ConfigParcel loadConfig() {
		final ArrayList<PersonSO> technicians = new ArrayList<>();
		final ArrayList<ServiceElementTypeSO> types = new ArrayList<>();
		final ArrayList<DirectionSO> directions = new ArrayList<>();
		final ArrayList<DinType> dins = new ArrayList<>();

		final ConfigParcel parcel = new ConfigParcel();
		Context context;
		try {
			final ResultsetMapper mapper = new ResultsetMapper();
			context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/origer_pc_test");

			Connection conn = dataSource.getConnection();
			Statement stmt = conn.createStatement();

			final String query = "SELECT * FROM person WHERE FK_TYP = " + TECHNICIAN_TYPE_ID + " AND ACTIVE = TRUE";
			final ResultSet techSet = stmt.executeQuery(query);

			if (techSet != null) {
				while (techSet.next()) {
					technicians.add(mapper.resultsetToPersonSO(techSet));
				}
			}
			final String elementQuery = "SELECT ID AS SET_ID, NAME AS SET_NAME, ACTIVE AS SET_ACTIVE FROM element_type";
			final ResultSet typeSet = stmt.executeQuery(elementQuery);
			if (typeSet != null) {
				while (typeSet.next()) {
					types.add(mapper.resultsetToServiceElementTypeSO(typeSet));
				}
			}
			final String directionQuery = "SELECT * FROM orientation_type";
			final ResultSet dirSet = stmt.executeQuery(directionQuery);
			if (dirSet != null && dirSet.getMetaData().getColumnCount() > 0) {
				while (dirSet.next()) {
					directions.add(mapper.resultsetToDirectionSO(dirSet));
				}
			}
			final String dinQuery = "SELECT ID AS DIN_ID, NAME AS DIN_NAME_DE, NAME_FR AS DIN_NAME_FR FROM din_type";
			final ResultSet dinSet = stmt.executeQuery(dinQuery);
			if (dinSet != null) {
				while (dinSet.next()) {
					dins.add(mapper.resultsetToDinType(dinSet));
				}
			}
			parcel.setDins(dins.toArray(new DinType[dins.size()]));
			parcel.setElementTypes(types.toArray(new ServiceElementTypeSO[types.size()]));
			parcel.setPersons(technicians.toArray(new PersonSO[technicians.size()]));
			parcel.setDirections(directions.toArray(new DirectionSO[directions.size()]));

			conn.close();
			return parcel;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public SoapParcel downloadUpdatedControls(SoapParcel intervalId) {
		final SoapParcel parcel = new SoapParcel();
		final ArrayList<SyncControlsSO> controls = new ArrayList<>();
		final ArrayList<FileArchiveSO> archives = new ArrayList<>();
		Context context;
		try {
			final ResultsetMapper mapper = new ResultsetMapper();
			context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/origer_pc_test");

			Connection conn = dataSource.getConnection();
			Statement stmt = conn.createStatement();

			String idList = "(";

			for (int c = 0; c < intervalId.getPrintRequestElements().length; c++) {
				idList += intervalId.getPrintRequestElements()[c].toString();

				if (c != intervalId.getPrintRequestElements().length - 1) {
					idList += ",";
				}
			}
			idList += ")";

			final String query = "SELECT SYC.ID AS SYC_ID, SYC.fk_element, SYC.startTime, SYC.endTime, "
					+ "SYC.CHECKING_SEC, SYC.ERROR_HISTORY_FLAG, SYC.SETUP_SEC, "
					+ "SYC.DESCRIPTION AS SYC_DESCRIPTION, SYC.FK_CHECKLISTITEM, "
					+ "SYC.VISUALCONTROL, SYC.FUNCTIONALCONTROL, SYC.ISOK, SYC.ISLACKING, "
					+ "SYC.ISDEFECT, SYC.CREATEOFFER, SYC.FK_SERVICE, SYC.ISREPAIRED, SYC.VF, "
					+ "SYC.FK_ELEMENT AS SYC_ELEMENT " + "FROM service_history AS SYC " + "WHERE SYC.FK_SERVICE IN "
					+ idList + " AND SYC.STARTTIME IS NOT NULL";
			final ResultSet controlSet = stmt.executeQuery(query);
			if (controlSet != null) {
				final ArrayList<Integer> controlIds = new ArrayList<>();
				while (controlSet.next()) {
					final SyncControlsSO temp = mapper.resultsetToSyncControlsSO(controlSet, false);					
					controls.add(temp);
					controlIds.add(temp.getId());
				}
				// Get file archives & files
				if (controlIds != null && controlIds.size() > 0) {
					for (int c = 0; c <= controlIds.size() - 1; c++) {
						ResultSet rsArchives = stmt.executeQuery(
								"SELECT DISTINCT FA.ID AS FA_ID, FA.URL, FA.FK_SERVICE_HISTORY, FA.NAME AS FA_NAME, FA.DESCRIPTION AS FA_DESCRIPTION, FA.FK_BUILDING_SITE AS FA_SITE, FA.IS_SIGNATURE "
										+ "FROM filearchive AS FA " + "WHERE FA.FK_SERVICE_HISTORY = "
										+ controlIds.get(c));

						if (rsArchives != null) {
							while (rsArchives.next()) {
								final FileArchiveSO tempArchive = mapper.resultsetToFileArchiveSO(rsArchives);
								if (tempArchive != null
										&& (tempArchive.getUrl() != null && tempArchive.getName() != null)) {
									try {
										File f = new File(tempArchive.getUrl());
										InputStream input = new FileInputStream(f);
										tempArchive.setData(new byte[(int) f.length()]);
										input.read(tempArchive.getData());
										input.close();
									} catch (Exception e) {
										e.printStackTrace();
									} finally {
										// Only add archive if file has been
										// found
										archives.add(tempArchive);
									}
								}
							}
						}
						rsArchives.close();
					}
				}
			}
			// Fetch service element barcode updates
			final String elementQuery = "SELECT SE.ID, SE.PRINT_NEW_BARCODE " + "FROM service_elements SE "
					+ "INNER JOIN service_history AS SYC ON SE.id = SYC.FK_ELEMENT " + "WHERE SYC.FK_SERVICE IN "
					+ idList;
			final ResultSet elementResult = stmt.executeQuery(elementQuery);
			if (elementResult != null) {
				final ArrayList<ServiceElementSO> elements = new ArrayList<>();
				while (elementResult.next()) {
					final ServiceElementSO element = new ServiceElementSO();
					element.setId(elementResult.getInt("ID"));
					element.setPrintLabel(elementResult.getBoolean("PRINT_NEW_BARCODE"));

					elements.add(element);
				}
				elementResult.close();
				parcel.setElements(elements.toArray(new ServiceElementSO[elements.size()]));
			}
			// Fetch interval updates
			final String intervalQuery = "SELECT DISTINCT * FROM service WHERE id IN " + idList;
			final ResultSet intervalResult = stmt.executeQuery(intervalQuery);
			if (intervalResult != null) {
				final ArrayList<ServiceSO> services = new ArrayList<>();
				while (intervalResult.next()) {
					final ServiceSO service = new ServiceSO();
					service.setId(intervalResult.getInt("ID"));
					service.setName(intervalResult.getString("name"));
					services.add(service);
				}
				intervalResult.close();
				parcel.setServices(services.toArray(new ServiceSO[services.size()]));

			}
			parcel.setControls(controls.toArray(new SyncControlsSO[controls.size()]));
			parcel.setFileArchive(archives.toArray(new FileArchiveSO[archives.size()]));
			conn.close();
			return parcel;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	/**
	 * Generates a string output with relavant informations for the server log.
	 * 
	 * @param archive
	 * @return
	 */
	private String generateArchiveInfo(final FileArchiveSO archive)
	{
		String info = "{";
		if(archive != null)
		{
			try
			{
				if(archive.getId() != null)
				{
					info += " ID: " + archive.getId().toString();
				}
				if(archive.getName() != null)
				{
					info += " NAME:  " + archive.getName();
				}
				if(archive.getService() != null && archive.getService().getId() != null)
				{
					info += " SERVICE: " + archive.getService().getId().toString();
				}
				if(archive.getControls() != null && archive.getControls().getId() != null)
				{
					info += " HISTORY: " + archive.getControls().getId().toString();
				}
				if(archive.getRecording() != null && archive.getRecording().getId() != null)
				{
					info += " TIME RECORDING " + archive.getRecording().getId().toString();
				}
				if(archive.getSite() != null)
				{
					info += " SITE: " + archive.getSite().toString();
				}
				if(archive.getIsSignature() != null && archive.getIsSignature())
				{
					info += " IS SIGNATURE ";
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				info += " }";
			}
		}
		return info;
	}
	

	private Boolean validateSoapParcel(final SoapParcel parcel, final SynchJobs job) {
		Boolean isValid = true;

		try {
			Context context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/origer_pc_test");

			Connection conn = dataSource.getConnection();
			Statement stmt = conn.createStatement();

			job.setDescription("");

			// Filearchive validation
			if (parcel.getFileArchive() != null) {
				job.setActualCountedElements(job.getActualCountedElements() + parcel.getFileArchive().length);
				if (parcel.getFileArchive().length != parcel.getFileArchiveSize()) {
					job.setDescription(job.getDescription() + "Filearchive Transfer incomplete. ");
					isValid = false;
				}
				// Boolean value to prevent duplicate error messages.
				Boolean hasError = false;
				// Array containing IDs of faulty archives.
				ArrayList<String> faultyArchives = new ArrayList<>();
				for (FileArchiveSO archive : parcel.getFileArchive()) {
					// Check if archive data is set.					
					if ((archive.getData() == null || archive.getData().length == 0)) {						
						//isValid = false;
						// Update upload status.
						if (!hasError) {
							job.setDescription(job.getDescription() + " Warning: Filearchive data missing.");
							hasError = true;
						}
						// Add archive ID if not already set.
						if(!faultyArchives.contains(archive.getId()))
						{
							faultyArchives.add(this.generateArchiveInfo(archive));
						}
					}
				}
				if(hasError && !faultyArchives.isEmpty())
				{
					//  Write error message with affected IDs into log.
					String idText = "";
					for(int c = 0; c < faultyArchives.size(); c++)
					{
						if(idText != "")
						{
							idText+=", ";
						}
						idText += faultyArchives.get(c).toString();
					}
					System.out.println("UPLOAD - Filearchive(s) [ " + idText + " ] contain no data.");
				}
			}
			if (parcel.getFileArchiveSize() != null) {
				job.setTargetElementsCount(job.getTargetElementsCount() + parcel.getFileArchiveSize());
			}

			// Timerecordings validation
			if (parcel.getRecordings() != null) {
				job.setActualCountedElements(job.getActualCountedElements() + parcel.getRecordings().length);
				if (parcel.getRecordings().length != parcel.getRecordingSize()) {
					job.setDescription(job.getDescription() + "Timerecordings transfer incomplete. ");
					isValid = false;
				}
				// Boolean values to prevent duplicate error messages
				Boolean hasTypeError = false;
				Boolean hasTimeError = false;
				for (TimeRecordingSO record : parcel.getRecordings()) {
					// Check if type is set
					if (record.getType() == null || record.getType().isEmpty()) {
						System.out.println("UPLOAD - TimeRecord [" + record.getId() + "] contains no type.");
						isValid = false;
						if (!hasTypeError) {
							job.setDescription(job.getDescription() + "Timerecord type missing. ");
							hasTypeError = true;
						}
					}
					// Check if time is set
					if (record.getSeconds() == null) {
						System.out.println("UPLOAD - TimeRecord [" + record.getId() + "] contains no time.");
						isValid = false;
						if (!hasTimeError) {
							job.setDescription(job.getDescription() + "Timerecord time missing. ");
							hasTimeError = true;
						}
					}
				}
			}

			if (parcel.getRecordingSize() != null) {
				job.setTargetElementsCount(job.getTargetElementsCount() + parcel.getRecordingSize());
			}
			// Controls validation
			if (parcel.getControls() != null) {
				job.setActualCountedElements(job.getActualCountedElements() + parcel.getControls().length);
				if (parcel.getControls().length != parcel.getControlSize()) {
					job.setDescription(job.getDescription() + "Controls transfer incomplete. ");
					isValid = false;
				}

				// Boolean values to prevent duplicate error messages
				Boolean hasNoServiceError = false;
				Boolean serviceNotExistantError = false;
				Boolean hasNoElementError = false;
				Boolean elementNotExistantError = false;
				Boolean hasNoEntryError = false;
				Boolean entryNotExistantError = false;

				for (SyncControlsSO controls : parcel.getControls()) {
					// Check the service
					if (controls.getService() == null) {
						isValid = false;
						System.out.println("UPLOAD - SyncControl [" + controls.getId() + "] contains no service.");
						if (!hasNoServiceError) {
							job.setDescription(job.getDescription() + "Controls service missing. ");
							hasNoServiceError = true;
						}
					} else {
						final String query = "SELECT id FROM service WHERE id = " + controls.getService().getId();
						ResultSet serviceResult = stmt.executeQuery(query);
						if (!serviceResult.isBeforeFirst()) {
							System.out.println("UPLOAD - Service [" + controls.getService().getId() + "] "
									+ "for SyncControl [" + controls.getId() + "] does not exist.");
							isValid = false;
							if (!serviceNotExistantError) {
								job.setDescription(job.getDescription() + "Controls service does not exist. ");
								serviceNotExistantError = true;
							}
						}
					}

					if (controls.getElement() == null) {
						isValid = false;
						System.out.println("UPLOAD - SyncControl [" + controls.getId() + "] contains no element.");
						if (!hasNoElementError) {
							job.setDescription(job.getDescription() + "Controls element missing. ");
							hasNoElementError = true;
						}
					} else {
						final String query = "SELECT id FROM service_elements WHERE id = "
								+ controls.getElement().getId();
						ResultSet serviceResult = stmt.executeQuery(query);
						if (!serviceResult.isBeforeFirst()) {
							System.out.println("UPLOAD - Element [" + controls.getElement().getId() + "] "
									+ "for SyncControl [" + controls.getId() + "] does not exist.");
							isValid = false;
							if (!elementNotExistantError) {
								job.setDescription(job.getDescription() + "Controls element does not exist. ");
								elementNotExistantError = true;
							}
						}
					}

					if (controls.getChecklistItem() == null) {
						isValid = false;
						System.out.println(
								"UPLOAD - SyncControl [" + controls.getId() + "] contains no checklist entry.");
						if (!hasNoEntryError) {
							job.setDescription(job.getDescription() + "Controls checklist entry missing. ");
							hasNoEntryError = true;
						}
					} else {
						final String query = "SELECT id FROM checklist_items WHERE id = "
								+ controls.getChecklistItem().getId();
						ResultSet serviceResult = stmt.executeQuery(query);
						if (!serviceResult.isBeforeFirst()) {
							System.out.println("UPLOAD - Checklist entry [" + controls.getElement().getId() + "] "
									+ "for SyncControl [" + controls.getId() + "] does not exist.");
							System.out.println("FAILED QUERY " + query);
							isValid = false;
							if (!entryNotExistantError) {
								job.setDescription(job.getDescription() + "Controls checklist entry does not exist. ");
								entryNotExistantError = true;
							}
						}
					}
				}
			}
			if (parcel.getElementsSize() != null) {
				job.setTargetElementsCount(job.getTargetElementsCount() + parcel.getElementsSize());
			}
			// Elements validation
			if (parcel.getElements() != null) {
				job.setActualCountedElements(job.getActualCountedElements() + parcel.getElements().length);
				if (parcel.getElements().length != parcel.getElementsSize()) {
					job.setDescription(job.getDescription() + "Element transfer incomplete. ");
					isValid = false;
				}

				Boolean noElementTypeError = false;
				Boolean noTypeSetError = false;
				Boolean noOrientationError = false;
				Boolean noOrientationSetError = false;

				for (ServiceElementSO element : parcel.getElements()) {
					if (element.getElementType() != null) {
						final String query = "SELECT id FROM element_type WHERE id = "
								+ element.getElementType().getId();
						ResultSet elementTypeResult = stmt.executeQuery(query);
						if (!elementTypeResult.isBeforeFirst()) {
							isValid = false;
							if (!noTypeSetError) {
								noTypeSetError = true;
								job.setDescription(job.getDescription() + "Element type entry does not exist. ");
							}
						}
					} else {
						isValid = false;
						if (!noElementTypeError) {
							noElementTypeError = true;
							job.setDescription(job.getDescription() + "No element type set. ");
						}
					}
					if (element.getOrientation() != null) {
						final String query = "SELECT id FROM orientation_type WHERE id = " + element.getOrientation();
						ResultSet orientationResult = stmt.executeQuery(query);
						if (!orientationResult.isBeforeFirst()) {
							isValid = false;
							if (!noOrientationError) {
								noOrientationError = true;
								job.setDescription(job.getDescription() + "Orientation entry does not exist. ");
							}
						}
					} else {
						isValid = false;
						if (!noOrientationSetError) {
							noOrientationError = true;
							job.setDescription(job.getDescription() + "No orientation set. ");
						}
					}
				}
			}

			if (parcel.getTechSize() != null) {
				job.setTargetElementsCount(job.getTargetElementsCount() + parcel.getTechSize());
			}
			// Technician validation
			if (parcel.getTechnicians() != null) {
				job.setActualCountedElements(job.getActualCountedElements() + parcel.getTechnicians().length);
				if (parcel.getTechnicians().length != parcel.getTechSize()) {
					job.setDescription(job.getDescription() + "Technician transfer incomplete. ");
					isValid = false;
				}

				Boolean noServiceSetError = false;
				Boolean noTechnicianSetError = false;
				Boolean noServiceError = false;
				Boolean noTechnicianError = false;
				if (parcel.getTechnicians().length > 0) {
					for (final ServiceTechnicianAssocSO assoc : parcel.getTechnicians()) {
						if (assoc.getTechnicianId() != null) {
							String query = "SELECT id FROM person WHERE id = " + assoc.getTechnicianId();
							final ResultSet techResult = stmt.executeQuery(query);
							if (!techResult.isBeforeFirst()) {
								isValid = false;
								if (!noTechnicianError) {
									noTechnicianError = true;
									job.setDescription(job.getDescription() + "Technician does not exist. ");
								}
							}
							techResult.close();
						} else {
							isValid = false;
							if (!noTechnicianSetError) {
								noTechnicianSetError = true;
								job.setDescription(job.getDescription() + "No technician set in assoc. ");
							}
						}
						if (assoc.getServiceId() != null) {
							final String query = "SELECT id FROM service WHERE id = " + assoc.getServiceId();
							final ResultSet serviceResult = stmt.executeQuery(query);
							if (!serviceResult.isBeforeFirst()) {
								isValid = false;
								if (!noServiceError) {
									noServiceError = true;
									job.setDescription(
											job.getDescription() + "Service in service assoc does not exist. ");
								}
							}
							serviceResult.close();
						} else {
							isValid = false;
							if (!noServiceSetError) {
								noServiceSetError = true;
								job.setDescription(job.getDescription() + "No service set in assoc. ");
							}
						}
					}
				} else {
					isValid = false;
					job.setDescription(job.getDescription() + "No technician/service assoc set. ");
				}

			}

			if (parcel.getControlSize() != null) {
				job.setTargetElementsCount(job.getTargetElementsCount() + parcel.getControlSize());
			}

			if (isValid) {
				if (job.getTargetElementsCount().equals(job.getActualCountedElements())) {
					job.setAndroidState("Complete");
				} else {
					job.setAndroidState("Incomplete");
					isValid = false;
				}
			} else {
				job.setAndroidState("Validation failed");
				job.setActualCountedElements(0);
			}

			conn.close();
		} catch (Exception e) {
			isValid = false;
			job.setDescription(job.getDescription() + "Exception occured during validation: " + e.getClass().getName() );
			e.printStackTrace();
		}
		return isValid;
	}

	public BuildingSiteSO[] getRepairSites(final String siteName) {

		ArrayList<BuildingSiteSO> sites = new ArrayList<>();

		try {
			Context context = new InitialContext();
			DataSource dataSource = (DataSource) context.lookup("java:comp/env/jdbc/origer_pc_test");

			Connection conn = dataSource.getConnection();
			Statement stmt = conn.createStatement();

			String query = "SELECT DISTINCT BS.ID AS BS_ID, BS.NAME AS BS_NAME " + "FROM building_site AS BS "
					+ "INNER JOIN service_contract AS SC ON BS.ID = SC.FK_BUILDING_SITE "
					+ "INNER JOIN service AS IV ON IV.FK_SERVICE_CONTRACT = SC.ID "
					+ "INNER JOIN service_history AS SH ON IV.ID = SH.FK_SERVICE "
					+ "WHERE (SH.ISDEFECT = 1 OR SH.ISLACKING = 1) " + "AND SH.ISREPAIRED = FALSE ";

			if (siteName != null && !siteName.isEmpty()) {
				query += "AND BS.NAME LIKE \'%" + siteName + "%\'";
			}

			ResultSet rs = stmt.executeQuery(query);
			if (rs != null) {
				ResultsetMapper mapper = new ResultsetMapper();
				while (rs.next()) {
					sites.add(mapper.resultsetToBuildingSiteSO(rs, false));
				}
			}
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return sites.toArray(new BuildingSiteSO[sites.size()]);
	}
}
