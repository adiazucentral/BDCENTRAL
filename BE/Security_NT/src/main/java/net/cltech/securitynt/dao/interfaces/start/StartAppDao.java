/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.dao.interfaces.start;

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
     * Ejecuta el script estadisticas de la aplicacion
     *
     * @param startScript Script inicial de la app
     *
     * @throws Exception Error ejecutando el script inicial
     */
    public void execStartScriptStat(String startScript) throws Exception;

}
