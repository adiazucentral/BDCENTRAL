/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.masters.test.Worksheet;

/**
 * Interfaz de servicios a la informacion del maestro Hoja de Trabajo
 *
 * @version 1.0.0
 * @author cmartin
 * @since 31/07/2017
 * @see Creación
 */
public interface WorksheetService
{

    /**
     * Lista las hojas de trabajo desde la base de datos.
     *
     * @return Lista de hojas de trabajo.
     * @throws Exception Error en la base de datos.
     */
    public List<Worksheet> list() throws Exception;

    /**
     * Registra una nueva Hoja de Trabajo en la base de datos.
     *
     * @param worksheet Instancia con los datos de la Hoja de Trabajo.
     *
     * @return Instancia con los datos de la Hoja de Trabajo.
     * @throws Exception Error en la base de datos.
     */
    public Worksheet create(Worksheet worksheet) throws Exception;

    /**
     * Obtener información de una Hoja de Trabajo por un campo especifico.
     *
     * @param id ID de la Hoja de Trabajo a ser consultada.
     * @param name Nombre de la Hoja de Trabajo a ser consultada.
     *
     * @return Instancia con los datos de la Hoja de Trabajo.
     * @throws Exception Error en la base de datos.
     */
    public Worksheet get(Integer id, String name) throws Exception;

    /**
     * Actualiza la información de una Hoja de Trabajo en la base de datos.
     *
     * @param worksheet Instancia con los datos de la Hoja de Trabajo.
     *
     * @return Objeto de la Hoja de Trabajo modificada.
     * @throws Exception Error en la base de datos.
     */
    public Worksheet update(Worksheet worksheet) throws Exception;

    /**
     * Obtener información de una hoja de trabajo por estado.
     *
     * @param state Estado de las hojas de trabajo a ser consultados
     *
     * @return Instancia con los datos de las hojas de trabajo.
     * @throws Exception Error en la base de datos.
     */
    public List<Worksheet> list(boolean state) throws Exception;

    /**
     * Obtener los examenes asociados a una hoja de trabajo.
     *
     *
     * @param id
     * @return Instancia con los datos de las hojas de trabajo.
     * @throws Exception Error en la base de datos.
     */
    public List<TestBasic> testsByWorkSheets(int id) throws Exception;
}
