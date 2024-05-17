package net.cltech.enterprisent.service.interfaces.masters.user;

import java.util.List;
import net.cltech.enterprisent.domain.masters.user.Role;

/**
 * Servicios de rol
 *
 * @version 1.0.0
 * @author eacuna
 * @since 05/05/2017
 * @see Creacion
 */
public interface RoleService
{

    /**
     * Registra una nueva rol en la base de datos.
     *
     * @param rol Instancia con los datos del rol.
     *
     * @return Instancia con los datos del rol.
     * @throws Exception Error en la base de datos.
     */
    public Role create(Role rol) throws Exception;

    /**
     * Lista rols desde la base de datos.
     *
     * @return Lista de rol.
     * @throws Exception Error en la base de datos.
     */
    public List<Role> list() throws Exception;

    /**
     * Lee la información por id.
     *
     * @param id ID del rol.
     *
     * @return Instancia con los datos del rol.
     * @throws Exception Error en la base de datos.
     */
    public Role findById(Integer id) throws Exception;

    /**
     * Lee la información de una rol.
     *
     * @param name Nombre del rol.
     *
     * @return Instancia con los datos de la rol.
     * @throws Exception Error en la base de datos.
     */
    public Role findByName(String name) throws Exception;

    /**
     * Actualiza la información rol en la base de datos.
     *
     * @param rol Instancia rol con datos a actualizar.
     *
     * @return Instancia con los datos del rol
     *
     * @throws Exception Error en la base de datos.
     */
    public Role update(Role rol) throws Exception;

    /**
     * Obtiene roels por su estado.
     *
     * @param state estado del rol activo o inactivo.
     *
     * @return Instancia con los datos del rol.
     * @throws Exception Error en la base de datos.
     */
    public List<Role> filterByState(boolean state) throws Exception;

    /**
     * Nuevo registro de Modulos por Rol en la base de datos.
     *
     * @param rol Instancia con los datos del rol y modulos a insertar.
     *
     * @return Registros afectados
     * @throws Exception Error en la base de datos.
     */
    public int createModules(Role rol) throws Exception;

}
