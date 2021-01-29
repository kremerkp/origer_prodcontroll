package lu.origer.serviceagree.models.main;

import java.io.Serializable;

import org.jfree.data.general.SeriesChangeListener;

public class LegendToolTip  implements Serializable{
	
	private String url; 
	
	private String description;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	} 
	

}
