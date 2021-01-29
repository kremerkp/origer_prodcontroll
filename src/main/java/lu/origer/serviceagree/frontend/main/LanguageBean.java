package lu.origer.serviceagree.frontend.main;

import java.util.Locale;

import javax.faces.bean.RequestScoped;
import javax.inject.Named;

@Named
@RequestScoped
public class LanguageBean extends BasicBean
{
	private static final long serialVersionUID = 1L;
	
	private Locale locale;

	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}
}
