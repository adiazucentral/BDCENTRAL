package net.cltech.enterprisent.service.interfaces.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Branch;

/**
 * Interfaz de servicios a la informacion del maestro Sedes
 *
 * @version 1.0.0
 * @author cmartin
 * @since 08/05/2017
 * @see Creación
 */
public interface BranchService
{

    /**
     * Lista las sedes desde la base de datos.
     *
     * @return Lista de sedes.
     * @throws Exception Error en la base de datos.
     */
    public List<Branch> list() throws Exception;

    /**
     * Registra una nueva sede en la base de datos.
     *
     * @param branch Instancia con los datos de la sede.
     *
     * @return Instancia con los datos de lka sede.
     * @throws Exception Error en la base de datos.
     */
    public Branch create(Branch branch) throws Exception;

    /**
     * Obtener información de una sede por un campo especifico.
     *
     * @param id ID de la sede a ser consultada.
     * @param name Nombre de la sede a ser consultada.
     * @param abbr Abreviatura de la sede a ser consultada.
     * @param code Codigo de la sede.
     *
     * @return Instancia con los datos del sede.
     * @throws Exception Error en la base de datos.
     */
    public Branch get(Integer id, String name, String abbr, String code) throws Exception;
    
    /**
     * Obtener información de una sede por un campo especifico.
     *
     * @param id ID de la sede a ser consultada.
     * @param name Nombre de la sede a ser consultada.
     * @param abbr Abreviatura de la sede a ser consultada.
     * @param code Codigo de la sede.
     *
     * @return Instancia con los datos del sede.
     * @throws Exception Error en la base de datos.
    */
    public Branch getBasic(Integer id, String name, String abbr, String code) throws Exception;

    /**
     * Actualiza la información de una sede en la base de datos.
     *
     * @param branch Instancia con los datos de la sede.
     *
     * @return Objeto de la sede modificada.
     * @throws Exception Error en la base de datos.
     */
    public Branch update(Branch branch) throws Exception;

    /**
     *
     * Elimina una sede de la base de datos.
     *
     * @param id ID de la sede.
     *
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;

    /**
     * Lista las sedes desde la base de datos por estado.
     *
     * @param state Estado.
     *
     * @return Lista de sedes.
     * @throws Exception Error en la base de datos.
     */
    public List<Branch> list(boolean state) throws Exception;
    
     /**
     * Lista sedes asignadas al usuario
     *
     *
     * @return Lista {@link Branch} sedes
     * @throws Exception
     */
    public List<Branch> listLogin() throws Exception;

    /**
     * Lista sedes asignadas al usuario
     *
     * @param username
     *
     * @return Lista {@link Branch} sedes
     * @throws Exception
     */
    public List<Branch> filterByUsername(String username) throws Exception;
    /**
     * Lista sedes asignadas al usuario
     *
     * @param username
     *
     * @return Lista {@link Branch} sedes
     * @throws Exception
     */
    public List<Branch> filterByUsernameLogin(String username) throws Exception;

}
