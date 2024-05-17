/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.dao.interfaces.masters.user;

import java.util.List;
import net.cltech.enterprisent.domain.masters.user.Module;

/**
 * Representa los métodos de acceso a base de datos para la información de los
 * Modulos.
 *
 * @author enavas
 * @version 1.0.0
 * @since 26/05/2017
 * @see Creación
 */
public interface ModuleDao
{

    /**
     * Lista los modulos desde la base de datos
     *
     * @return Lista de modulos
     * @throws Exception Error en base de datos
     */
    public List<Module> list() throws Exception;

    /**
     * Lista módulos asociados al usuario
     *
     * @param id id base de datos del usuario
     *
     * @return Lista de modulos
     * @throws Exception
     */
    public List<Module> findByUser(Integer id) throws Exception;
}
