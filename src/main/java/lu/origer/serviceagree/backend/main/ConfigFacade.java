/*******************************************************************************
 * Copyright (c) 2016 Eye-T S.àr.l. .
 *******************************************************************************/



/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package lu.origer.serviceagree.backend.main;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mysema.query.jpa.impl.JPAQuery;

import lu.origer.serviceagree.models.main.Config;
import lu.origer.serviceagree.models.main.QConfig;

/**
 *
 * @author Kai Kremer
 * @Version 1.0
 */
@Stateless
public class ConfigFacade extends AbstractFacade<Config> {
	public static final String SYSTEM_MESSAGE = "SYSTEM_MESSAGE";
	
	public static final String WIN_FILE_PATH = "WIN_FILEARCHIVE_PATH";
	public static final String UNIX_FILE_PATH = "UNIX_FILEARCHIVE_PATH";
    
    
    @PersistenceContext(unitName = "ORIGERPU")
    private EntityManager      em;

    public ConfigFacade() {
        super(Config.class);
    }

    public void saveSystemMessage(String message) {
        QConfig  config     = QConfig.config;
        JPAQuery query      = new JPAQuery(em);
        Config   configItem = query.from(config).where(config.configKey.eq(SYSTEM_MESSAGE)).singleResult(config);

        if (configItem != null) {
            configItem.setConfigValue(message);
            edit(configItem);
        } else {
            configItem = new Config();
            configItem.setConfigKey(SYSTEM_MESSAGE);
            configItem.setConfigValue(message);
            create(configItem);
        }
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public String getSystemMessage() {
        QConfig config = QConfig.config;
        Config  res    = new JPAQuery(em).from(config).where(config.configKey.eq(SYSTEM_MESSAGE)).singleResult(config);

        return (res == null)
               ? ""
               : res.getConfigValue();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
