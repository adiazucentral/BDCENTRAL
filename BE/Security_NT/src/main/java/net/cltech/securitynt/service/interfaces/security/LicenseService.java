package net.cltech.securitynt.service.interfaces.security;

import java.util.HashMap;
import net.cltech.securitynt.domain.common.License;
import net.cltech.securitynt.domain.common.LicenseResponse;

/**
 * Interfaz de servicios sobre las licencias de la aplicacion
 *
 * @version 1.0.0
 * @author equijano
 * @since 25/10/2019
 * @see Creación
 */
public interface LicenseService
{

    /**
     * Obtener licencia de un elemento proyecto, cantidad de usuarios o
     * submodulos
     *
     * @param license
     * @return licencia {@link net.cltech.securitynt.domain.common.License}.
     * @throws Exception Error en la base de datos.
     */
    public LicenseResponse getLicenses(License license) throws Exception;

    /**
     * Obtener la configuracion de la lista de licencias
     *
     * @param branchCode Codigo de la sede
     * @return
     * @throws Exception Error en la base de datos.
     */
    public HashMap<String, Boolean> licences(String branchCode) throws Exception;
    
    /**
     * Obtener la configuracion de la lista de licencias
     * @param llave
     * @return
     * @throws Exception Error en la base de datos.
     */
    public Boolean validateLicenseInterface(String llave) throws Exception;
    
    /**
     * Obtener las licencias de los tableros
     *
     * @return Licencias de los tableros
     * @throws Exception Error en la base de datos.
     */
    public HashMap<String, Boolean> boardLicenses() throws Exception;
}
