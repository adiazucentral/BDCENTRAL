package net.cltech.enterprisent.dao.interfaces.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Branch;

/**
 * Representa los métodos de acceso a base de datos para la información de las
 * Sedes.
 *
 * @version 1.0.0
 * @author cmartin
 * @since 08/05/2017
 * @see Creación
 */
public interface BranchDao
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
     * Lista las sedes a las que tiene acceso el usuario
     *
     * @param username de usuario
     *
     * @return Lista de sedes
     * @throws Exception
     */
    public List<Branch> filterByUsername(String username) throws Exception;
}
