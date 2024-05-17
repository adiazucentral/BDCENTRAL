package net.cltech.enterprisent.service.interfaces.security;

import java.util.List;
import net.cltech.enterprisent.controllers.common.License;

/**
 * Servicios de licencias para NT.
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 21/02/2020
 * @see Creacion
 */
public interface LicenseService
{
    
     /**
     * Lista las licencias de los modulos externos
     *
     * @return las licencias de los modulos externos
     * @throws Exception Error en la base de datos.
     */
    public List<License> licenses() throws Exception;
    
    
}
