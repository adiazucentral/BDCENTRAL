package net.cltech.enterprisent.service.interfaces.start;

/**
 * Interfaz de servicios de inicio de la aplicacion
 *
 * @version 1.0.0
 * @author dcortes
 * @since 12/04/2017
 * @see Creacion
 */
public interface StartAppService
{

    /**
     * Motor de base de datos de postgreSQL
     */
    public static final String POSTGRESQL = "postgresql";
    /**
     * Motor de base de datos SQLServer
     */
    public static final String SQL_SERVER = "sqlserver";
    /**
     * Ruta de los scripts de base de datos de PostgreSQL
     */
    public static final String POSTGRESQL_PATH = "/scripts/postgresql";
    /**
     * Ruta de los scripts de base de datos de SQLServer
     */
    public static final String SQL_SERVER_PATH = "/scripts/sqlserver";

    /**
     * Ejecuta el script de actualizacion de base de datos
     *
     * @param sqlEngine Define el motor de base de datos a utilizar
     *
     * @throws Exception Error en el servicio
     */
    public void execStartScript(String sqlEngine) throws Exception;

    /**
     * Ejecuta el script de actualizacion de base de datos (Datos)
     *
     * @param sqlEngine Define el motor de base de datos a utilizar
     *
     * @throws Exception Error en el servicio
     */
    public void execStartScriptData(String sqlEngine) throws Exception;

    /**
     * Ejecuta el script de actualizacion de base de datos de documentos
     *
     * @param sqlEngine Define el motor de base de datos a utilizar
     *
     * @throws Exception Error en el servicio
     */
    public void execStartScriptDocs(String sqlEngine) throws Exception;

    /**
     * Ejecuta el script de actualizacion de base de datos de estadisticas
     *
     * @param sqlEngine Define el motor de base de datos a utilizar
     *
     * @throws Exception Error en el servicio
     */
    public void execStartScriptStat(String sqlEngine) throws Exception;

    /**
     * Ejecuta el script de actualización de base de datos de Control
     *
     * @param sqlEngine Define el motor de base de datos a utilizar
     * @throws Exception Error en el servicio
     */
    public void execStartScriptControl(String sqlEngine) throws Exception;

    /**
     * Ejecuta el script de actualizacion de base de datos de reporte his
     *
     * @param sqlEngine Define el motor de base de datos a utilizar
     *
     * @throws Exception Error en el servicio
     */
    public void execStartScriptRepo(String sqlEngine) throws Exception;

    /**
     * Ejecuta el script de actualizacion de base de datos de patologia
     *
     * @param sqlEngine Define el motor de base de datos a utilizar
     *
     * @throws Exception Error en el servicio
     */
    public void execStartScriptPat(String sqlEngine) throws Exception;
    
    /**
     * Ejecuta el script de creación de las funciones o procedimientos almacenados (segun el motor de BD)
     * para renombrear todas las tablas de operación de NT (BD Estadistica)
     *
     * @param sqlEngine Define el motor de base de datos a utilizar
     * @param fileName
     * @param operation
     *
     * @throws Exception Error en el servicio
     */
    public void executeAnyScript(String sqlEngine, String fileName, String operation) throws Exception;
    
    /**
     * Ejecuta el script de mantenimiento diario a la base de datos
     *
     * @throws Exception Error en el servicio
     */
    public void execStartScriptMaintenanceDBDaily() throws Exception;
    
    /**
     * Ejecuta el script de mantenimiento diario a la base de datos
     *
     * @throws Exception Error en el servicio
     */
    public void execScriptMaintenanceDBDaily() throws Exception;
    
    /**
     * Ejecuta el servicio de envio de resultados automaticos por medio de un email
     *
     * @throws Exception Error en el servicio
     */
    public void sendEmailAutomaticResult() throws Exception;
}
