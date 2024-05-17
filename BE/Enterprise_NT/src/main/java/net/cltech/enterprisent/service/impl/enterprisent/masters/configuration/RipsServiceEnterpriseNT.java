/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.impl.enterprisent.masters.configuration;

import java.util.List;
import net.cltech.enterprisent.dao.interfaces.masters.configuration.RipsDao;
import net.cltech.enterprisent.domain.masters.configuration.RIPS;
import net.cltech.enterprisent.service.interfaces.masters.configuration.RipsService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de acceso a configuracion RIPS para Enterprise NT
 *
 * @version 1.0.0
 * @author omendez
 * @since 20/01/2021
 * @see Creación
 */
@Service
public class RipsServiceEnterpriseNT implements RipsService
{
    @Autowired
    private RipsDao ripsDao;
    @Autowired
    private TrackingService trackingService;
    
    @Override
    public List<RIPS> get() throws Exception
    {
        return ripsDao.get();
    }
    
    @Override
    public RIPS get(String key) throws Exception
    {
        return ripsDao.get(key);
    }

    @Override
    public String getValue(String key) throws Exception
    {
        return ripsDao.get(key) == null ? null : ripsDao.get(key).getValue();
    }

    @Override
    public void update(List<RIPS> configuration) throws Exception
    {        
        for (RIPS config : configuration)
        {
            RIPS oldConfiguration = ripsDao.get(config.getKey());
            if (oldConfiguration != null)
            {
                ripsDao.update(config);
                trackingService.registerConfigurationTracking(oldConfiguration, config, RIPS.class);
            }
        }
    }
    
    @Override
    public List<RIPS> getDemographic() throws Exception
    {
        return ripsDao.getDemographic();
    }
}
