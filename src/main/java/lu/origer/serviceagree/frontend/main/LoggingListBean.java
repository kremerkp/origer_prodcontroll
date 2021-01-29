/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lu.origer.serviceagree.frontend.main;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import lu.origer.serviceagree.backend.main.LoggingFacade;
import lu.origer.serviceagree.models.main.Logging;

/**
 *
 * @author ffreres
 */
@Named
@ViewScoped
public class LoggingListBean extends BasicListBean<Logging> {

    private List<SelectItem> reporterList;
    @Inject
    private LoggingFacade loggingFacade;

    /**
     * Creates a new instance of LoggingList
     */
    public LoggingListBean() {
    }

    @Override
    public String add() {
        return null;
    }

    @Override
    public void delete() {
        
    }

    @Override
    public String edit() {
        return null;
    }

    @Override
    public void filterList() {
        data = loggingFacade.findLoggingsByDate(startDateFilter, endDateFilter);
    }

    @Override
    public List<Logging> getData() {
        return data;
    }

    public List<SelectItem> getReporterList() {
        return reporterList;
    }

    @PostConstruct
    public void init() {
        filterList();
        reporterList = new ArrayList<>();
        reporterList.add(new SelectItem(""));
        for (String reporter : loggingFacade.getReporterList()) {
            reporterList.add(new SelectItem(reporter));
        }
    }
}
