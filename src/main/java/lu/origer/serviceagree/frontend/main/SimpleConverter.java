package lu.origer.serviceagree.frontend.main;

import java.lang.reflect.InvocationTargetException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ManagedBean
@RequestScoped
public class SimpleConverter implements Converter {

    @PersistenceContext
    EntityManager em;

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        try {
            String jpaClass = value.split("_")[0];
            Integer id = Integer.parseInt(value.split("_")[1]);
            return em.createQuery("SELECT c FROM " + jpaClass + " c WHERE c.id = :id").setParameter("id", id).getSingleResult();
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        try {
            Integer id = (Integer) value.getClass().getDeclaredMethod("getId").invoke(value);
            return (id == null) ? null : value.getClass().getSimpleName() + "_" + id.toString();
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NullPointerException ex) {
            return "";
        }
    }
}
