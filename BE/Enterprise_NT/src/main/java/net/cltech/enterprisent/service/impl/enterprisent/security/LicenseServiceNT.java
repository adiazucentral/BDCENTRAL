package net.cltech.enterprisent.service.impl.enterprisent.security;

import java.util.Arrays;
import java.util.List;
import net.cltech.enterprisent.controllers.common.License;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.security.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los metodos de licencias para homebound
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 21/02/2020
 * @see Creación
 */
@Service
public class LicenseServiceNT implements LicenseService
{
    
    @Autowired
    private ConfigurationService configurationServices;
    
    
    private Configuration TomaMuestraHospitalaria;
    private Configuration Homebound;  
    
    
    
    @Override
    public List<License> licenses() throws Exception
    { 
        
        TomaMuestraHospitalaria  = new Configuration();
        Homebound  = new Configuration();      
        
       
        TomaMuestraHospitalaria.setKey("TomaMuestraHospitalaria");  
        Homebound.setKey("Homebound"); 
        TomaMuestraHospitalaria = configurationServices.get(TomaMuestraHospitalaria.getKey());
        Homebound = configurationServices.get(Homebound.getKey()); 
        
       return Arrays.asList(
                new License("01", "Homebound",Boolean.parseBoolean(Homebound.getValue())),
                new License("02", "Toma Muestra Hospitalaria", Boolean.parseBoolean(TomaMuestraHospitalaria.getValue()))
                
        );

    }
    
}
