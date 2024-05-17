package net.cltech.enterprisent.service.interfaces.security;

import net.cltech.enterprisent.domain.access.Module;

/**
 * Interfaz de servicios sobre la autorizacion
 *
 * @version 1.0.0
 * @author equijano
 * @since 17/10/2019
 * @see Creación
 */
public interface AuthorizationService
{

    /**
     * Revisa la autorización del usuario a un modulo
     *
     * @param moduleCode
     * @return Modulos.
     * @throws Exception Error en la base de datos.
     */
    public Module authorization(int moduleCode) throws Exception;
    
}
