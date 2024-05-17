package net.cltech.enterprisent.service.interfaces.security;

import java.util.List;
import net.cltech.enterprisent.domain.common.AuthenticationSession;

/**
 * Interfaz de servicios sobre las sesiones activas
 *
 * @version 1.0.0
 * @author equijano
 * @since 30/11/2018
 * @see Creación
 */
public interface SessionService
{

    /**
     * Lista sesiones activas
     *
     * @return Modulos.
     * @throws Exception Error en la base de datos.
     */
    public List<AuthenticationSession> list() throws Exception;

    /**
     * Adiciona sesion activa
     *
     * @param authenticationSession
     * @return registros afectados
     * @throws Exception Error en la base de datos.
     */
    public AuthenticationSession create(AuthenticationSession authenticationSession) throws Exception;

    /**
     * Elimina una sesion activa
     *
     * @param idSession
     * @param deleteWs
     * @return registros afectados
     * @throws Exception Error en la base de datos.
     */
    public int deleteBySession(AuthenticationSession idSession, boolean deleteWs) throws Exception;

    /**
     * Elimina todas las sesiones activas
     *
     * @throws Exception Error en la base de datos.
     */
    public void deleteAll() throws Exception;

}
