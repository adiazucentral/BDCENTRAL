/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.service.interfaces.exception;

import java.util.List;
import net.cltech.outreach.domain.exception.WebException;

/**
 * Interfaz de servicios sobre los errores de la aplicacion
 *
 * @version 1.0.0
 * @author dcortes
 * @since 25/04/2017
 * @see Creacion
 */
public interface ExceptionService
{

    /**
     * Guarda un nuevo error en la tabla de errores
     *
     * @param exception
     * {@link net.cltech.outreach.domain.exception.WebException}
     * @return {@link net.cltech.outreach.domain.exception.WebException}
     * generado con fecha y id
     * @throws Exception Error presentado en base de datos
     */
    public WebException save(WebException exception) throws Exception;

    /**
     * Obtiene todos los errores registrados en el rango de fechas enviado
     *
     * @param initialDate Fecha inicial en formato yyyyMMdd
     * @param finalDate Fecha final en formato yyyyMMdd
     * @return Lista de
     * {@link net.cltech.outreach.domain.exception.WebException}
     * @throws Exception Error presentado en base de datos
     */
    public List<WebException> get(int initialDate, int finalDate) throws Exception;

    /**
     * Obtiene todos los errores registrados en un rango de fechas y filtrando
     * por el tipo de error
     *
     * @param initialDate Fecha inicial en formato yyyyMMdd
     * @param finalDate Fecha final en formato yyyyMMdd
     * @param type 0->Error en base de datos, 1->Error no controlador
     * @return Lista de
     * {@link net.cltech.outreach.domain.exception.WebException}
     * @throws Exception Error en base de datos
     */
    public List<WebException> get(int initialDate, int finalDate, int type) throws Exception;
}
