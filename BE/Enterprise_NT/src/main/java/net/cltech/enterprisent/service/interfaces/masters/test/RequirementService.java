package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Requirement;

/**
 * Servicios de requisito
 *
 * @version 1.0.0
 * @author eacuna
 * @since 28/04/2017
 * @see Creacion
 */
public interface RequirementService
{

    /**
     * Registra una nueva requisito en la base de datos.
     *
     * @param requirement Instancia con los datos del requisito.
     *
     * @return Instancia con los datos del requisito.
     * @throws Exception Error en la base de datos.
     */
    public Requirement create(Requirement requirement) throws Exception;

    /**
     * Lista requisitos desde la base de datos.
     *
     * @return Lista de requisito.
     * @throws Exception Error en la base de datos.
     */
    public List<Requirement> list() throws Exception;

    /**
     * Lee la información por id.
     *
     * @param id ID del requisito.
     *
     * @return Instancia con los datos del requisito.
     * @throws Exception Error en la base de datos.
     */
    public Requirement filterById(Integer id) throws Exception;

    /**
     * Lee la información de una requisito.
     *
     * @param code Nombre del requisito.
     *
     * @return Instancia con los datos de la requisito.
     * @throws Exception Error en la base de datos.
     */
    public Requirement filterByCode(String code) throws Exception;

    /**
     * Obtiene requisitos por su estado.
     *
     * @param state estado activo(true), inactivo(false).
     *
     * @return Lista con los requisitos.
     * @throws Exception Error en la base de datos.
     */
    public List<Requirement> filterByState(boolean state) throws Exception;

    /**
     * Actualiza la información requisito en la base de datos.
     *
     * @param requirement Instancia requisito con datos a actualizar.
     *
     * @throws Exception Error en la base de datos.
     */
    public void update(Requirement requirement) throws Exception;

}
