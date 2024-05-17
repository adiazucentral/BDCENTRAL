/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.service.interfaces.start;

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
     * Ejecuta el script de actualizacion de base de datos
     *
     * @param sqlEngine Define el motor de base de datos a utilizar
     *
     * @throws Exception Error en el servicio
     */
    public void execStartScript(String sqlEngine) throws Exception;

    /**
     * Obtiene motod de base de datos configurado
     *
     * @return sqlserver o postgresql
     * @throws Exception Error en el servicio
     */
    public String getDataBasaEngine() throws Exception;

}
