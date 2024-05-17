package net.cltech.enterprisent.service.interfaces.operation.result;

import java.util.List;
import net.cltech.enterprisent.domain.operation.common.Common;
import net.cltech.enterprisent.domain.operation.results.worklist.WorklistFilter;
import net.cltech.enterprisent.domain.operation.results.worklist.WorklistResult;

/**
 * Interfaz de servicios a la informacion de hojas de trabajo.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 09/10/2017
 * @see Creación
 */
public interface WorklistService
{

    /**
     * Lista las ordenes por un filtro especifico dado por la clase de listados.
     *
     * @param filter Clase con los filtros definidos
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public WorklistResult list(WorklistFilter filter) throws Exception;

    /**
     * Obtine las ordenes de una hoja de trabajo generada
     *
     * @param worksheet id de la hoja de trabajo
     * @param group     numero de grupo con el que se genero la hoja de trabajo
     *
     * @return Hoja de trabajo con las ordenes encontradas
     * @throws Exception Error
     */
    public WorklistResult previous(int worksheet, int group) throws Exception;

    /**
     * Reinicia secuencia de la hoja de trabajo
     *
     * @param worksheet id de hoja de trabajo
     *
     * @return registros afectados
     * @throws Exception Error
     */
    public int reset(int worksheet) throws Exception;

    /**
     * Lista las secuencias de una hoja de trabajo
     *
     * @param worksheet id de la hoja de trabajo
     *
     * @return Lista de las secuencias
     * @throws Exception
     */
    public List<Common> listSecuence(int worksheet) throws Exception;

}
