package lu.origer.serviceagree.frontend.main;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

import lu.origer.serviceagree.models.checklist.ChecklistItem;

@FacesConverter(value = "entityConverter")
public class EntityConverter implements Converter {

	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return getObjectFromUIPickListComponent(component, value);
	}

	public String getAsString(FacesContext context, UIComponent component, Object object) {
		String string;
		if (object == null) {
			string = "";
		} else {
			try {
				string = String.valueOf(((ChecklistItem) object).getId());
			} catch (ClassCastException cce) {
				throw new ConverterException();
			}
		}
		return string;
	}

	@SuppressWarnings("unchecked")
	private ChecklistItem getObjectFromUIPickListComponent(UIComponent component, String value) {
		final DualListModel<ChecklistItem> dualList;
		try {
			dualList = (DualListModel<ChecklistItem>) ((PickList) component).getValue();
			ChecklistItem resource = getObjectFromList(dualList.getSource(), Long.valueOf(value));
			if (resource == null) {
				resource = getObjectFromList(dualList.getTarget(), Long.valueOf(value));
			}

			return resource;
		} catch (ClassCastException cce) {
			throw new ConverterException();
		} catch (NumberFormatException nfe) {
			throw new ConverterException();
		}
	}

	private ChecklistItem getObjectFromList(final List<?> list, final Long identifier) {
		for (final Object object : list) {
			final ChecklistItem resource = (ChecklistItem) object;
			if (resource.getId().equals(identifier)) {
				return resource;
			}
		}
		return null;
	}
}
