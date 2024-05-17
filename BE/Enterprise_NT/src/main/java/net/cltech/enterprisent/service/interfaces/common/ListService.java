package net.cltech.enterprisent.service.interfaces.common;

import java.util.List;
import net.cltech.enterprisent.domain.masters.common.Item;

/**
 * Interfaz de servicios a la informacion de las listas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 19/04/2017
 * @see Creación
 */
public interface ListService
{

    /**
     * Lista los items desde la base de datos.
     *
     * @param id Id del padre de los items a ser consultados.
     *
     * @return Lista.
     * @throws Exception Error en la base de datos.
     */
    public List<Item> list(int id) throws Exception;

    /**
     * Actualiza valor adicional de las listas
     *
     * @param item
     *
     * @return Item de la lista actualizados
     * @throws Exception Error en la base de datos.
     */
    public Item update(Item item) throws Exception;
}
