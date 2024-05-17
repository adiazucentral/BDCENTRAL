package net.cltech.enterprisent.service.impl.enterprisent.security;

import net.cltech.enterprisent.domain.access.Module;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.security.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementacion de autorizacion para Enterprise NT
 *
 * @version 1.0.0
 * @author equijano
 * @since 17/10/2019
 * @see Creacion
 */
@Service
public class AuthorizationServiceEnterpriseNT implements AuthorizationService
{

    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private IntegrationService integrationService;

    @Override
    public Module authorization(int moduleCode) throws Exception
    {
        final String url = configurationService.getValue("UrlSecurity") + "/api/authorization/" + moduleCode;
        return integrationService.get(Module.class, url);
    }
    
}
