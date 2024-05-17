package net.cltech.securitynt.dao.interfaces.masters.configuration;

import java.util.List;
import net.cltech.securitynt.domain.masters.configuration.Configuration;

/**
 * Representa los metodos de acceso a base de datos para la informacion de la
 * configuración general
 *
 * @version 1.0.0
 * @author dcortes
 * @since 14/04/2017
 * @see Creación
 */
public interface ConfigurationDao
{

    /**
     * Obtiene todas las llaves de configuracion
     *
     * @return Lista de
     * {@link net.cltech.securitynt.domain.masters.configuration.Configuration}
     * @throws Exception Error en base de datos
     */
    public List<Configuration> get() throws Exception;

    /**
     * Obtiene una llave de configuraciòn
     *
     * @param key Llave de configuraciòn
     * @return
     * {@link net.cltech.securitynt.domain.masters.configuration.Configuration},
     * null en caso de que no se encuentre la llave
     * @throws net.cltech.securitynt.domain.exception.EnterpriseNTException
     */
    public Configuration get(String key) throws Exception;

    /**
     * Actualiza una llave de configuración si existe
     *
     * @param configuration
     * {@link net.cltech.securitynt.domain.masters.configuration.Configuration}
     * @throws Exception Error en base de datos
     */
    public void update(Configuration configuration) throws Exception;
}
