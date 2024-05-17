package net.cltech.securitynt.service.impl.securitynt.masters.configuration;

import java.util.List;
import net.cltech.securitynt.dao.interfaces.masters.configuration.ConfigurationDao;
import net.cltech.securitynt.domain.masters.configuration.Configuration;
import net.cltech.securitynt.service.interfaces.masters.configuration.ConfigurationService;
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
public class ConfigurationServiceSecurityNT implements ConfigurationService
{

    @Autowired
    private ConfigurationDao configurationDao;

    @Override
    public List<Configuration> get() throws Exception
    {
        return configurationDao.get();
    }

    @Override
    public Configuration get(String key) throws Exception
    {
        return configurationDao.get(key);
    }

    @Override
    public String getValue(String key) throws Exception
    {
        return configurationDao.get(key) == null ? null : configurationDao.get(key).getValue();
    }
}
