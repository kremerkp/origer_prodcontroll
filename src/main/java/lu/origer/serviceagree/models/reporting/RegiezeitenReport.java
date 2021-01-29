package lu.origer.serviceagree.models.reporting;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RegiezeitenReport implements Serializable {
	
	public static final String VERRECHNET = "verrechnet"; 
	
	public static final String ZUVERRECHNEN = "zu verrechnen" ;
	
	private String time; 
	
	private Integer timeInSeconds; 
	
	private Date date; 
	
	private Date dayDate; 
	
	private String fileUrlSign;
	
	private String fileUrl1;
	
	private String fileUrl2;
	
	private String comment; 
	
	private String buildingSite;
	
	private String buildingSiteCode;
	
	private String element; 
	
	private String servieIntervall;
	
	private String serviceContract;
	
	private String unterschrift; 
	
	private String Monteur; 
	
	private String verrechnungsArt; 
	
	private String kunde; 

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getFileUrlSign() {
		return fileUrlSign;
	}

	public void setFileUrlSign(String fileUrlSign) {
		this.fileUrlSign = fileUrlSign;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getBuildingSite() {
		return buildingSite;
	}

	public void setBuildingSite(String buildingSite) {
		this.buildingSite = buildingSite;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public String getServieIntervall() {
		return servieIntervall;
	}

	public void setServieIntervall(String servieIntervall) {
		this.servieIntervall = servieIntervall;
	}

	public String getFileUrl1() {
		return fileUrl1;
	}

	public void setFileUrl1(String fileUrl1) {
		this.fileUrl1 = fileUrl1;
	}

	public String getFileUrl2() {
		return fileUrl2;
	}

	public void setFileUrl2(String fileUrl2) {
		this.fileUrl2 = fileUrl2;
	}

	public String getServiceContract() {
		return serviceContract;
	}

	public void setServiceContract(String serviceContract) {
		this.serviceContract = serviceContract;
	}

	public String getUnterschrift() {
		return unterschrift;
	}

	public void setUnterschrift(String unterschrift) {
		this.unterschrift = unterschrift;
	}

	public Date getDayDate() {
		return dayDate;
	}

	public void setDayDate(Date dayDate) {
		this.dayDate = dayDate;
	}

	public String getMonteur() {
		return Monteur;
	}

	public void setMonteur(String monteur) {
		Monteur = monteur;
	}

	public String getBuildingSiteCode() {
		return buildingSiteCode;
	}

	public void setBuildingSiteCode(String buildingSiteCode) {
		this.buildingSiteCode = buildingSiteCode;
	}

	public Integer getTimeInSeconds() {
		return timeInSeconds;
	}

	public void setTimeInSeconds(Integer timeInSeconds) {
		this.timeInSeconds = timeInSeconds;
	}

	public String getKunde() {
		return kunde;
	}

	public void setKunde(String kunde) {
		this.kunde = kunde;
	}

	public String getVerrechnungsArt() {
		return verrechnungsArt;
	}

	public void setVerrechnungsArt(String verrechnungsArt) {
		this.verrechnungsArt = verrechnungsArt;
	}
	

}
