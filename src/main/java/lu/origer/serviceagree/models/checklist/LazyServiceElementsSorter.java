package lu.origer.serviceagree.models.checklist;

import java.util.Comparator;

import org.primefaces.model.SortOrder;

public class LazyServiceElementsSorter implements Comparator<ServiceElements> {
	
	public String sortField; 
	
	private SortOrder sortOrder;
	
    public LazyServiceElementsSorter(String sortField, SortOrder sortOrder) {
        this.sortField = sortField;
        this.sortOrder = sortOrder;
    }

	@Override
    public int compare(ServiceElements s1, ServiceElements s2) {
        try {
            Object value1 = ServiceElements.class.getField(this.sortField).get(s1);
            Object value2 = ServiceElements.class.getField(this.sortField).get(s2);
 
            @SuppressWarnings({ "unchecked", "rawtypes" })
			int value = ((Comparable)value1).compareTo(value2);
             
            return SortOrder.ASCENDING.equals(sortOrder) ? value : -1 * value;
        }
        catch(Exception e) {
            throw new RuntimeException();
        }
    }
	
	

}
