package net.cltech.enterprisent.service.interfaces.common;

import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;

/**
 * Interfaz de servicios a la informacion
 *
 * @version 1.0.0
 * @author eacuna
 * @since 03/10/2017
 * @see Creación
 */
public interface ChatService
{

    /**
     * Lista los usuarios registrados en el chat
     *
     * @return Modulos.
     * @throws Exception Error en la base de datos.
     */
    public List<AuthorizedUser> list() throws Exception;

}
