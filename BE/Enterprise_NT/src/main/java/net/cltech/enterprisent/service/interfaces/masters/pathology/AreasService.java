/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Area;

/**
 * interface de Servicios para el maestro de areas de patología
 *
 * @version 1.0.0
 * @author etoro
 * @since 08/10/2020
 * @see Creacion
 */
public interface AreasService {
    
    /**
    * lista de areas de patologia.
    *
    *
    * @return la lista de areas.
    * @throws Exception Error en la base de datos.
    */
    public List<Area> list() throws Exception;

    /**
     * Registra una nueva area en la base de datos.
     *
     * @param area Instancia con los datos del area.
     * @return Instancia con los datos del area.
     * @throws Exception Error en la base de datos.
     */
    public Area create(Area area) throws Exception;

    /**
    * Obtener información de un area por un campo especifico.
    *
    * @param id ID del area a ser consultada.
    * @param name Nombre del area a ser consultada.
    * @param code Codigo del area a ser consultada.
    * @return Instancia con los datos del archivo.
    * @throws Exception Error en la base de datos.
    */
    public Area get(Integer id, String name, String code) throws Exception;

    /**
    * Actualiza un area de la base de datos.
    *
    * @param area Instancia con los datos del area.
    * @return la instancia del objeto area.
    * @throws Exception Error en la base de datos.
    */
    public Area update(Area area) throws Exception;
    
    /**
    * Obtener información de un área por estado.
    *
    * @param state Estado de las areas a ser consultadas
    * @return Instancia con los datos del área.
    * @throws Exception Error en la base de datos.
    */
    public List<Area> list(int state) throws Exception;

}
