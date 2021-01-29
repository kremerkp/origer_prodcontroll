/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lu.origer.serviceagree.backend.main;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

import lu.origer.serviceagree.frontend.main.ApplicationBean;

/**
 *
 * @author fred.freres
 */
@Singleton
@Startup
public class startupBean {

    @Inject
    private ApplicationBean applicationBean;
    
    @PostConstruct
    public void init() {
        System.out.println("Startup EJB Bean");
        //System.out.println(applicationBean);
    }
}
