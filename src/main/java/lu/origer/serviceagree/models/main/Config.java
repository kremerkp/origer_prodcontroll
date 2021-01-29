/*******************************************************************************
 * Copyright (c) 2016 Eye-T S.àr.l. .
 *******************************************************************************/



/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package lu.origer.serviceagree.models.main;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Kai Kremer
 * @Version 1.0
 */
@Entity
@Table(name = "config")
@XmlRootElement
@NamedQueries({ @NamedQuery(
    name  = "Config.findAll",
    query = "SELECT c FROM Config c"
) })
public class Config implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @NotNull
    @Basic(optional = false)
    @Size(
        min                                    = 1,
        max                                    = 255
    )
    @Column(name = "pk_key")
    private String            configKey;
    @Size(max = 255)
    @Column(name = "value")
    private String            configValue;

    public Config() {}

    public Config(String configKey) {
        this.configKey = configKey;
    }

    @Override
    public boolean equals(Object object) {

        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Config)) {
            return false;
        }

        Config other = (Config) object;

        if (((this.configKey == null) && (other.configKey != null))
                || ((this.configKey != null) &&!this.configKey.equals(other.configKey))) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 0;

        hash += ((configKey != null)
                 ? configKey.hashCode()
                 : 0);

        return hash;
    }

    @Override
    public String toString() {
        return "lu.cimw.models.Config[ configKey=" + configKey + " ]";
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
