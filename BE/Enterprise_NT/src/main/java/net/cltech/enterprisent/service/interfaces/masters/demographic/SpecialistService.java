package net.cltech.enterprisent.service.interfaces.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Specialist;

/**
 * Interfaz de servicios a la informacion del maestro Especialista
 *
 * @version 1.0.0
 * @author eacuna
 * @since 11/05/2017
 * @see Creación
 */
public interface SpecialistService
{

    /**
     * Lista especialista desde la base de datos.
     *
     * @return Lista de especialista.
     * @throws Exception Error en la base de datos.
     */
    public List<Specialist> list() throws Exception;

    /**
     * Registra una nueva especialista en la base de datos.
     *
     * @param create Instancia con los datos del especialista.
     *
     * @return Instancia con los datos del especialista.
     * @throws Exception Error en la base de datos.
     */
    public Specialist create(Specialist create) throws Exception;

    /**
     * Obtener información de especialista por un campo especifico.
     *
     * @param id ID del especialista a ser consultada.
     *
     * @return Instancia con los datos del especialista.
     * @throws Exception Error en la base de datos.
     */
    public Specialist filterById(Integer id) throws Exception;

    /**
     * Obtener información de especialista por un campo especifico.
     *
     * @param name ID del especialista a ser consultada.
     *
     * @return Instancia con los datos del especialista.
     * @throws Exception Error en la base de datos.
     */
    public Specialist filterByName(String name) throws Exception;

    /**
     * Obtener información de especialista por un campo especifico.
     *
     * @param state estado activo(true) o inactivo(false).
     *
     * @return Instancia con los datos del especialista.
     * @throws Exception Error en la base de datos.
     */
    public List<Specialist> filterByState(boolean state) throws Exception;

    /**
     * Actualiza la información de un especialista en la base de datos.
     *
     * @param update Instancia con los datos del especialista.
     *
     * @return Objeto del area modificada.
     * @throws Exception Error en la base de datos.
     */
    public Specialist update(Specialist update) throws Exception;

}
