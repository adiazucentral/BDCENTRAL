package net.cltech.enterprisent.service.interfaces.integration;

import net.cltech.enterprisent.domain.integration.his.BranchHis;
import net.cltech.enterprisent.domain.integration.his.BranchHisState;
import net.cltech.enterprisent.domain.integration.his.UserHis;
import net.cltech.enterprisent.domain.integration.his.UserStatus;

/**
 * Interfaz de servicios a la informacion para integrar con el his.
 *
 * @version 1.0.0
 * @author omendez
 * @since 01/02/2021
 * @see Creación
 */
public interface IntegrationHisService
{

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param user Instancia con los datos del usuario.
     *
     * @return Instancia con los datos del usuario.
     * @throws Exception Error en la base de datos.
     */
    public UserHis create(UserHis user) throws Exception;

    /**
     * Cambiar estado usuario
     *
     * @param user Instancia con los datos del usuario.
     *
     * @return Objeto del usuario modificada.
     * @throws Exception Error en la base de datos.
     */
    public UserStatus changeStateUser(UserStatus user) throws Exception;

    /**
     * Crea una sede desde el HIS
     *
     * @param branchhis
     *
     * @return Instancia con los datos de la sede
     * @throws Exception Error en la base de datos.
     */
    public BranchHis createBranch(BranchHis branchhis) throws Exception;

    /**
     * Actualiza una sede desde el HIS
     *
     * @param branchstate
     *
     * @return Instancia de la sede con los datos de su modificación
     * @throws Exception Error en la base de datos.
     */
    public BranchHisState updateBranch(BranchHisState branchstate) throws Exception;

    /**
     * Registra un nuevo usuario en la base de datos.
     *
     * @param user Instancia con los datos del usuario.
     *
     * @return Instancia con los datos del usuario.
     * @throws Exception Error en la base de datos.
     */
  

}
