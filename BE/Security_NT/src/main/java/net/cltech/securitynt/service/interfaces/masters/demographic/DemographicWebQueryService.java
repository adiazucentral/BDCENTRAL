/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.service.interfaces.masters.demographic;

import java.util.List;
import net.cltech.securitynt.domain.masters.demographic.DemographicWebQuery;

/**
 *
 * @author adiaz
 */
public interface DemographicWebQueryService {
    /**
     * Realiza la validacion de inactividad para desactivar los usuarios.
     *
     * @param users Lista de
     * {@link net.cltech.securitynt.domain.masters.user.User}
     * @param token
     * @throws Exception Error en la base de datos.
     */
    public void deactivateUsersDemographicwebquery(List<DemographicWebQuery> users, String token) throws Exception;
}
