package lu.origer.serviceagree.models.checklist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

public class LazyServiceElements extends LazyDataModel<ServiceElements> {

	private List<ServiceElements> datasource;
	
    public LazyServiceElements(List<ServiceElements> datasource) {
        this.datasource = datasource;
    }
    
    
    @Override
    public ServiceElements getRowData(String rowKey) {
        for(ServiceElements s : datasource) {
            if(s.getId().equals(rowKey))
                return s;
        }
 
        return null;
    }
    
    @Override
    public Object getRowKey(ServiceElements s) {
        return s.getId();
    }
 
    @Override
    public List<ServiceElements> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
        List<ServiceElements> data = new ArrayList<ServiceElements>();
 
        //filter
        for(ServiceElements s : datasource) {
            boolean match = true;
 
            if (filters != null) {
                for (Iterator<String> it = filters.keySet().iterator(); it.hasNext();) {
                    try {
                        String filterProperty = it.next();
                        Object filterValue = filters.get(filterProperty);
                        String fieldValue = String.valueOf(s.getClass().getField(filterProperty).get(s));
 
                        if(filterValue == null || fieldValue.startsWith(filterValue.toString())) {
                            match = true;
                    }
                    else {
                            match = false;
                            break;
                        }
                    } catch(Exception e) {
                        match = false;
                    }
                }
            }
 
            if(match) {
                data.add(s);
            }
        }
 
        //sort
        if(sortField != null) {
            Collections.sort(data, new LazyServiceElementsSorter(sortField, sortOrder));
        }
 
        //rowCount
        int dataSize = data.size();
        this.setRowCount(dataSize);
 
        //paginate
        if(dataSize > pageSize) {
            try {
                return data.subList(first, first + pageSize);
            }
            catch(IndexOutOfBoundsException e) {
                return data.subList(first, first + (dataSize % pageSize));
            }
        }
        else {
            return data;
        }
    }
    

}
