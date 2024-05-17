/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Area;

/**
 * Interfaz de servicios a la informacion del maestro Áreas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 12/04/2017
 * @see Creación
 */
public interface AreaService
{
    /**
     * Lista las áreas desde la base de datos.
     *
     * @return Lista de áreas.
     * @throws Exception Error en la base de datos.
     */
    public List<Area> list() throws Exception;

    /**
     * Registra una nueva área en la base de datos.
     *
     * @param area Instancia con los datos del área.
     * @return Instancia con los datos del área.
     * @throws Exception Error en la base de datos.
     */
    public Area create(Area area) throws Exception;
    
    /**
     * Obtener información de un área por un campo especifico.
     *
     * @param id ID del área a ser consultada.
     * @param ordering Codigo del área a ser consultada.
     * @param name Nombre del área a ser consultada.
     * @param abbr Abreviatura del área a ser consultada.
     * @return Instancia con los datos del área.
     * @throws Exception Error en la base de datos.
     */
    public Area get(Integer id, Integer ordering, String name, String abbr) throws Exception;

    /**
     * Actualiza la información de un área en la base de datos.
     *
     * @param area Instancia con los datos del área.
     * @return Objeto del area modificada.
     * @throws Exception Error en la base de datos.
     */
    public Area update(Area area) throws Exception;

    /**
     *
     * Elimina un área de la base de datos.
     *
     * @param id ID del área.
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;
    
    /**
     * Obtener información de un área por estado.
     *
     * @param state Estado de las areas a ser consultadas
     * @return Instancia con los datos del área.
     * @throws Exception Error en la base de datos.
     */
    public List<Area> list(boolean state) throws Exception;
}
