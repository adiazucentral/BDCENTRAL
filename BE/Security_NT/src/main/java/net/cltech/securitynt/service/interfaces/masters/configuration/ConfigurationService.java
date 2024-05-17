package net.cltech.securitynt.service.interfaces.masters.configuration;

import java.util.List;
import net.cltech.securitynt.domain.masters.configuration.Configuration;

/**
 * Servicios de configuracion general
 *
 * @version 1.0.0
 * @author dcortes
 * @since 14/04/2017
 * @see Creacion
 */
public interface ConfigurationService
{

    /**
     * Obtiene todas las llaves de configuracion
     *
     * @return Lista de
     * {@link net.cltech.securitynt.domain.masters.configuration.Configuration}
     * @throws Exception Error en el servicio
     */
    public List<Configuration> get() throws Exception;

    /**
     * Obtiene una llave de configuraciòn
     *
     * @param key Llave de configuraciòn
     *
     * @return
     * {@link net.cltech.securitynt.domain.masters.configuration.Configuration},
     * null en caso de que no se encuentre la llave
     * @throws net.cltech.securitynt.domain.exception.EnterpriseNTException
     */
    public Configuration get(String key) throws Exception;

    /**
     * Obtiene el valor de configuración
     *
     * @param key Llave de configuraciòn
     *
     * @return LLave de configuración null en caso de que no se encuentre la
     * llave
     * @throws Exception Error en el servicio
     */
    public String getValue(String key) throws Exception;

}
