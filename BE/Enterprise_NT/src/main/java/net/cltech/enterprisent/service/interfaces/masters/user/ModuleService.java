/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.masters.user;

import java.util.List;
import net.cltech.enterprisent.domain.masters.user.Module;

/**
 * Interfaz de servicios a la informacion del maestro de Modulos
 *
 * @version 1.0.0
 * @author enavas
 * @since 26/05/2017
 * @see Creación
 */
public interface ModuleService
{

    /**
     * Lista los modulos desde la base de datos
     *
     * @return Lista de modulos
     * @throws Exception Error en base de datos
     */
    public List<Module> list() throws Exception;

    /**
     * Lista los modulos relacionados con el usuario
     *
     * @param id id del modulo
     *
     * @return Lista de modulos
     * @throws Exception
     */
    public List<Module> filterByUser(Integer id) throws Exception;

}
