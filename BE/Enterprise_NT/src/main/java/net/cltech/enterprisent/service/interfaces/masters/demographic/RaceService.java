package net.cltech.enterprisent.service.interfaces.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Race;

/**
 * Interfaz de servicios a la informacion del maestro Raza
 *
 * @version 1.0.0
 * @author eacuna
 * @since 09/05/2017
 * @see Creación
 */
public interface RaceService
{

    /**
     * Lista raza desde la base de datos.
     *
     * @return Lista de raza.
     * @throws Exception Error en la base de datos.
     */
    public List<Race> list() throws Exception;

    /**
     * Registra una nueva raza en la base de datos.
     *
     * @param create Instancia con los datos del raza.
     *
     * @return Instancia con los datos del raza.
     * @throws Exception Error en la base de datos.
     */
    public Race create(Race create) throws Exception;

    /**
     * Obtener información de raza por un campo especifico.
     *
     * @param id ID del raza a ser consultada.
     *
     * @return Instancia con los datos del raza.
     * @throws Exception Error en la base de datos.
     */
    public Race filterById(Integer id) throws Exception;

    /**
     * Obtener información de raza por un campo especifico.
     *
     * @param name ID del raza a ser consultada.
     *
     * @return Instancia con los datos del raza.
     * @throws Exception Error en la base de datos.
     */
    public Race filterByName(String name) throws Exception;
    
    /**
     * Obtener información de raza por un campo especifico.
     *
     * @param code Codigo del raza a ser consultada.
     *
     * @return Instancia con los datos del raza.
     * @throws Exception Error en la base de datos.
     */
    public Race filterByCode(String code) throws Exception;

    /**
     * Obtener información de raza por un campo especifico.
     *
     * @param state estado activo(true) o inactivo(false).
     *
     * @return Instancia con los datos del raza.
     * @throws Exception Error en la base de datos.
     */
    public List<Race> filterByState(boolean state) throws Exception;

    /**
     * Actualiza la información de un raza en la base de datos.
     *
     * @param update Instancia con los datos del raza.
     *
     * @return Objeto del area modificada.
     * @throws Exception Error en la base de datos.
     */
    public Race update(Race update) throws Exception;

}
