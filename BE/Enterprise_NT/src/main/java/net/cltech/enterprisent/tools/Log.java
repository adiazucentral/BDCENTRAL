/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.tools;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase utilitaria para log.
 *
 * @version 3.0.0
 * @author CMercado
 * @since 09/11/2016
 * @see Creación
 */
public class Log
{

    /**
     * Nombre en logs.
     */
    private static final String LOG_NAME = "Enterprise_NT_Logger";

    /**
     * Template que se usa al mostrar el mensaje.
     */
    private static final String TEMPLATE = "Enterprise NT LOG: {0}";

    /**
     * Mensaje de log nivel 'info'.
     *
     * @param cls Clase.
     * @param message Mensaje.
     */
    public static void info(Class cls, String message)
    {
        Logger.getLogger(Log.LOG_NAME).log(Level.INFO, Log.TEMPLATE, message);
    }

    /**
     * Mensaje de log nivel 'warning'.
     *
     * @param cls Clase.
     * @param message Mensaje.
     */
    public static void warning(Class cls, String message)
    {
        Logger.getLogger(Log.LOG_NAME).log(Level.WARNING, Log.TEMPLATE, message);
    }

    /**
     * Mensaje de log nivel 'error'.
     *
     * @param cls Clase.
     * @param message Mensaje.
     */
    public static void error(Class cls, String message)
    {
        Logger.getLogger(Log.LOG_NAME).log(Level.SEVERE, Log.TEMPLATE, message);
    }

    /**
     * Mensaje de log nivel 'error'.
     *
     * @param cls Clase.
     * @param e Instancia de la excepción.
     */
    public static void error(Class cls, Exception e)
    {
        Logger.getLogger(Log.LOG_NAME).log(Level.SEVERE, Log.TEMPLATE, e);
    }

    /**
     * Mensaje de log nivel 'debug'.
     *
     * @param cls Clase.
     * @param message Mensaje.
     */
    public static void debug(Class cls, String message)
    {
        Logger.getLogger(Log.LOG_NAME).log(Level.FINEST, Log.TEMPLATE, message);
    }

}
