/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.service.impl.enterprisent.masters.configuration;

import java.util.List;
import java.util.stream.Collectors;
import net.cltech.outreach.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.outreach.domain.masters.configuration.Configuration;
import net.cltech.outreach.domain.masters.configuration.DocumentType;
import net.cltech.outreach.service.interfaces.masters.configuration.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a configuracion general para Enterprise NT
 *
 * @version 1.0.0
 * @author dcortes
 * @since 14/04/2017
 * @see Creación
 */
@Service
public class ConfigurationServiceEnterpriseNT implements ConfigurationService
{

    @Autowired
    private ConfigurationDao configurationDao;

    @Override
    public List<Configuration> get() throws Exception
    {
        return configurationDao.getEncrypted();
    }
    
     @Override
    public List<Configuration> getEncrypted() throws Exception
    {
                      
        
        return configurationDao.getEncrypted();
        
    }

    @Override
    public Configuration get(String key) throws Exception
    {
        Configuration config = configurationDao.getLISConfiguration().stream().filter(c -> c.getKey().equalsIgnoreCase(key)).findFirst().orElse(null);
        if (config != null)
        {
            return config;
        } else
        {
            return configurationDao.get(key);
        }
    }

    @Override
    public String getValue(String key) throws Exception
    {
        return configurationDao.get(key) == null ? null : configurationDao.get(key).getValue();
    }

    @Override
    public void update(List<Configuration> configuration) throws Exception
    {
        Configuration oldConfiguration = null;
        for (Configuration config : configuration)
        {
            oldConfiguration = configurationDao.get(config.getKey());
            if (oldConfiguration != null)
            {
                configurationDao.update(config);
            }
        }
    }

    @Override
    public List<DocumentType> listDocumentType(boolean state) throws Exception
    {
        return configurationDao.listDocumentType().stream().filter(document -> document.isState() == state).collect(Collectors.toList());
    }
}
