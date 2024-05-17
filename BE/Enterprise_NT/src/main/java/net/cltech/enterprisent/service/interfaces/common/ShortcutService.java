package net.cltech.enterprisent.service.interfaces.common;

import java.util.List;
import net.cltech.enterprisent.domain.access.Shortcut;
import net.cltech.enterprisent.domain.masters.user.Module;

/**
 * Interfaz de servicios a la informacion
 *
 * @version 1.0.0
 * @author eacuna
 * @since 19/09/2017
 * @see Creación
 */
public interface ShortcutService
{

    /**
     * Lista los formularios shortcut del usuario para un módulo
     *
     * @param user   id user
     * @param module modulo (LIS, Billing, Inventarios, etc)
     *
     * @return Modulos.
     * @throws Exception Error en la base de datos.
     */
    public List<Module> list(int user, int module) throws Exception;

    /**
     * Adiciona shortcut al usuario.
     *
     * @param shortcut
     *
     * @return registros afectados
     * @throws Exception Error en la base de datos.
     */
    public int add(Shortcut shortcut) throws Exception;

    /**
     * Elimina shortcut al usuario.
     *
     * @param shortcut
     *
     * @return registros afectados
     * @throws Exception Error en la base de datos.
     */
    public int delete(Shortcut shortcut) throws Exception;
}
