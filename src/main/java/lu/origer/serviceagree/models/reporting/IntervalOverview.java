package lu.origer.serviceagree.models.reporting;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public class IntervalOverview implements Serializable{
	
	private String serviceContract; 
	
	private Integer serviceInterval; 
	
	private BigDecimal targetTime; 
	
	private BigDecimal actualTime; 
	
	private BigDecimal estimatedTime;
	
	private BigDecimal contractValue; 
	
	private List<IntervalChart> chartData; 
	
	private String intervallName; 
	
	private String monteure;

	public String getServiceContract() {
		return serviceContract;
	}

	public void setServiceContract(String serviceContract) {
		this.serviceContract = serviceContract;
	}


	public Integer getServiceInterval() {
		return serviceInterval;
	}

	public void setServiceInterval(Integer serviceInterval) {
		this.serviceInterval = serviceInterval;
	}

	public BigDecimal getTargetTime() {
		return targetTime;
	}

	public void setTargetTime(BigDecimal targetTime) {
		this.targetTime = targetTime;
	}

	public BigDecimal getActualTime() {
		return actualTime;
	}

	public void setActualTime(BigDecimal actualTime) {
		this.actualTime = actualTime;
	}

	public BigDecimal getEstimatedTime() {
		return estimatedTime;
	}

	public void setEstimatedTime(BigDecimal estimatedTime) {
		this.estimatedTime = estimatedTime;
	}

	public BigDecimal getContractValue() {
		return contractValue;
	}

	public void setContractValue(BigDecimal contractValue) {
		this.contractValue = contractValue;
	}

	public String getMonteure() {
		return monteure;
	}

	public void setMonteure(String monteure) {
		this.monteure = monteure;
	}

	public List<IntervalChart> getChartData() {
		return chartData;
	}

	public void setChartData(List<IntervalChart> chartData) {
		this.chartData = chartData;
	}

	public String getIntervallName() {
		return intervallName;
	}

	public void setIntervallName(String intervallName) {
		this.intervallName = intervallName;
	}
	

}
