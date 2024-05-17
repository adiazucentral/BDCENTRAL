package net.cltech.enterprisent.dao.interfaces.masters.configuration;

import java.util.List;
import net.cltech.enterprisent.domain.masters.configuration.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

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
     * Obtiene la coneccion a la base de datos
     * @return jdbc Template de Sprint para acceso a datos
     */
    public JdbcTemplate getJdbcTemplate();
    
    /**
     * Obtiene todas las llaves de configuracion
     *
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.masters.configuration.Configuration}
     * @throws Exception Error en base de datos
     */
    public List<Configuration> get() throws Exception;

    /**
     * Obtiene una llave de configuraciòn
     *
     * @param key Llave de configuraciòn
     * @return
     * {@link net.cltech.enterprisent.domain.masters.configuration.Configuration},
     * null en caso de que no se encuentre la llave
     * @throws Exception Error en base de datos
     */
    public Configuration get(String key) throws Exception;

    /**
     * Actualiza una llave de configuración si existe
     *
     * @param configuration
     * {@link net.cltech.enterprisent.domain.masters.configuration.Configuration}
     * @throws Exception Error en base de datos
     */
    public void update(Configuration configuration) throws Exception;
    
    /**
     * Limpiamos de registros la tabla que se envie como parametro
     *
     * @param tableToTruncate Tabla a truncar
     * @throws Exception Error en el servicio
     */
    default void tableToTruncate(String tableToTruncate) throws Exception
    {
        getJdbcTemplate().execute("TRUNCATE " + tableToTruncate);
    }
    
    /**
     * Usa la función o el procedimiento almacenado pertinente para realizar el 
     * renombramiento de las tablas de operación y todos sus componentes (llaves y constrains)
     *
     * @param year Año con el que se renombrará esa tabla
     * @throws Exception Error en base de datos
     */
    public void renameOperationTablesByYear(Integer year) throws Exception;
    
    /**
     * Usa la función o el procedimiento almacenado pertinente para realizar el 
     * renombramiento de las tablas de operación y todos sus componentes (llaves y constrains)
     * en la BD de Estadistica
     *
     * @param year Año con el que se renombrará esa tabla
     * @throws Exception Error en base de datos
     */
    public void renameOperationTablesStat(Integer year) throws Exception;
}
