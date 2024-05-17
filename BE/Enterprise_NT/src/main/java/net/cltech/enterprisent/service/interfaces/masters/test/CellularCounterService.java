/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.CellularCounter;

/**
 * Interfaz de servicios a la informacion del maestro Contador Hematologico
 *
 * @version 1.0.0
 * @author cmartin
 * @since 01/08/2017
 * @see Creación
 */
public interface CellularCounterService
{
    /**
     * Lista los contadores hematologicos desde la base de datos.
     *
     * @return Lista de contadores hematologicos.
     * @throws Exception Error en la base de datos.
     */
    public List<CellularCounter> list() throws Exception;

    /**
     * Registra un nuevo Contador Hematologico en la base de datos.
     *
     * @param cellularCounter Instancia con los datos del Contador Hematologico.
     *
     * @return Instancia con los datos del Contador Hematologico.
     * @throws Exception Error en la base de datos.
     */
    public CellularCounter create(CellularCounter cellularCounter) throws Exception;

    /**
     * Obtener información de un Contador Hematologico por un campo especifico.
     *
     * @param id   ID del Contador Hematologico a ser consultado.
     * @param key Tecla del Contador Hematologico a ser consultado.
     *
     * @return Instancia con los datos de la Contador Hematologico.
     * @throws Exception Error en la base de datos.
     */
    public CellularCounter get(Integer id, String key) throws Exception;
    
      /**
     * Obtener información de un Contador Hematologico por un campo especifico.
     *
     * @param id   ID del examen.   
     *
     * @return Instancia con los datos de la Contador Hematologico.
     * @throws Exception Error en la base de datos.
     */
    public List<CellularCounter> geTest(Integer id) throws Exception;

    /**
     * Actualiza la información de un Contador Hematologico en la base de datos.
     *
     * @param cellularCounter Instancia con los datos de la Contador Hematologico.
     *
     * @return Objeto de la Contador Hematologico modificada.
     * @throws Exception Error en la base de datos.
     */
    public CellularCounter update(CellularCounter cellularCounter) throws Exception;

    /**
     * Obtener información de un Contador Hematologico por estado.
     *
     * @param state Estado de los contadores hematologicos a ser consultados
     *
     * @return Instancia con los datos del Contador Hematologico.
     * @throws Exception Error en la base de datos.
     */
    public List<CellularCounter> list(boolean state) throws Exception;
}
