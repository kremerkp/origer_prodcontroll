/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lu.origer.serviceagree.frontend.main;

import java.util.Date;
import java.util.List;

/**
 *
 * @author ffreres
 * @param <T>
 */
public abstract class BasicListBean<T> extends BasicBean {

    protected Date startDateFilter;
    protected Date endDateFilter;
    protected List<T> data;
    protected List<T> filteredData;
    protected T selectedData;

    public T getSelectedData() {
        return selectedData;
    }

    public void setSelectedData(T selectedData) {
        this.selectedData = selectedData;
    }

    public Date getStartDateFilter() {
        return startDateFilter;
    }

    public void setStartDateFilter(Date startDateFilter) {
        this.startDateFilter = startDateFilter;
    }

    public Date getEndDateFilter() {
        return endDateFilter;
    }

    public void setEndDateFilter(Date endDateFilter) {
        this.endDateFilter = endDateFilter;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<T> getFilteredData() {
        return filteredData;
    }

    public void setFilteredData(List<T> filteredData) {
        this.filteredData = filteredData;
    }
    
    public abstract void filterList();
    
    public abstract String add();
    
    public abstract String edit();
    
    public abstract void delete();

}
