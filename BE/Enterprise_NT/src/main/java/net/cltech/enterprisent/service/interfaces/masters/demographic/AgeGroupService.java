package net.cltech.enterprisent.service.interfaces.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.AgeGroup;

/**
 * Interfaz de servicios a la informacion del maestro Grupo Etario
 *
 * @version 1.0.0
 * @author eacuna
 * @since 31/01/2018
 * @see Creación
 */
public interface AgeGroupService
{

    /**
     * Lista grupo etario desde la base de datos.
     *
     * @return Lista de grupo etario.
     * @throws Exception Error en la base de datos.
     */
    public List<AgeGroup> list() throws Exception;

    /**
     * Registra una nueva grupo etario en la base de datos.
     *
     * @param create Instancia con los datos del grupo etario.
     *
     * @return Instancia con los datos del grupo etario.
     * @throws Exception Error en la base de datos.
     */
    public AgeGroup create(AgeGroup create) throws Exception;

    /**
     * Obtener información de grupo etario por un campo especifico.
     *
     * @param id ID del grupo etario a ser consultada.
     *
     * @return Instancia con los datos del grupo etario.
     * @throws Exception Error en la base de datos.
     */
    public AgeGroup filterById(Integer id) throws Exception;

    /**
     * Obtener información de grupo etario por un campo especifico.
     *
     * @param name ID del grupo etario a ser consultada.
     *
     * @return Instancia con los datos del grupo etario.
     * @throws Exception Error en la base de datos.
     */
    public AgeGroup filterByName(String name) throws Exception;

    /**
     * Obtener información de grupo etario por un campo especifico.
     *
     * @param code Codigo del grupo etario a ser consultada.
     *
     * @return Instancia con los datos del grupo etario.
     * @throws Exception Error en la base de datos.
     */
    public AgeGroup filterByCode(String code) throws Exception;

    /**
     * Obtener información de grupo etario por un campo especifico.
     *
     * @param state estado activo(true) o inactivo(false).
     *
     * @return Instancia con los datos del grupo etario.
     * @throws Exception Error en la base de datos.
     */
    public List<AgeGroup> filterByState(boolean state) throws Exception;

    /**
     * Actualiza la información de un grupo etario en la base de datos.
     *
     * @param update Instancia con los datos del grupo etario.
     *
     * @return Objeto del area modificada.
     * @throws Exception Error en la base de datos.
     */
    public AgeGroup update(AgeGroup update) throws Exception;

}
