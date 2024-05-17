package net.cltech.enterprisent.dao.interfaces.start;

/**
 * Interfaz de acceso a base de datos para ejecutar los scripts iniciales
 *
 * @version 1.0.0
 * @author dcortes
 * @since 12/04/2017
 * @see Creacion
 */
public interface StartAppDao
{

    /**
     * Ejecuta el script inicial de la aplicacion
     *
     * @param startScript Script inicial de la app
     *
     * @throws Exception Error ejecutando el script inicial
     */
    public void execStartScript(String startScript) throws Exception;

    /**
     * Ejecuta el script documentos de la aplicacion
     *
     * @param startScript Script inicial de la app
     *
     * @throws Exception Error ejecutando el script inicial
     */
    public void execStartScriptDocs(String startScript) throws Exception;

    /**
     * Ejecuta el script reporte his de la aplicacion
     *
     * @param startScript Script inicial de la app
     *
     * @throws Exception Error ejecutando el script inicial
     */
    public void execStartScriptRep(String startScript) throws Exception;

    /**
     * Ejecuta el script estadisticas de la aplicacion
     *
     * @param startScript Script inicial de la app
     *
     * @throws Exception Error ejecutando el script inicial
     */
    public void execStartScriptStat(String startScript) throws Exception;

    /**
     * Ejecuta el script tablas de control de la aplicacion
     *
     * @param startScript Script inicial de la app
     *
     * @throws Exception Error ejecutando el script inicial
     */
    public void execStartScriptControl(String startScript) throws Exception;

    /**
     * Ejecuta el script estadisticas rapidas de la aplicacion
     *
     * @throws Exception Error ejecutando el script inicial
     */
    public void execStartScriptStatAgile() throws Exception;

    /**
     * Ejecuta el script de patologia de la aplicacion
     *
     * @param startScript Script inicial de la app
     *
     * @throws Exception Error ejecutando el script inicial
     */
    public void execStartScriptPat(String startScript) throws Exception;
    
    /**
     * Ejecuta el script de patologia de la aplicacion
     *
     * @param isSQL
     * @param script
     * @param isTaskJob
     * @throws Exception Error ejecutando el script inicial
     */
    public void execStartScriptMaintenanceDB(boolean isSQL, String script, boolean isTaskJob) throws Exception;
    
}
