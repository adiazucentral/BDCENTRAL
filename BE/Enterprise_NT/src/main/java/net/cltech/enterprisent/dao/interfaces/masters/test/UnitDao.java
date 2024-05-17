package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Unit;

/**
 * Representa los metodos de acceso a base de datos para la informacion de la
 * base de datos
 *
 * @version 1.0.0
 * @author eacuna
 * @since 17/04/2017
 * @see Creación
 */
public interface UnitDao
{

    /**
     * Registra una nueva unidad de medida en la base de datos.
     *
     * @param unit Instancia con los datos de la unidad de medida.
     *
     * @return {@link net.cltech.enterprisent.domain.masters.test.Unit}
     * @throws Exception Error en la base de datos.
     */
    public Unit create(Unit unit) throws Exception;

    /**
     * Lista las unidades de medida desde la base de datos.
     *
     * @return Lista de unidades de medida.
     * @throws Exception Error en la base de datos.
     */
    public List<Unit> list() throws Exception;

    /**
     * Lee la información de una unidad de medida.
     *
     * @param id ID de la unidad de medida a ser consultada.
     *
     * @return Instancia con los datos de la unidad de medida.
     * @throws Exception Error en la base de datos.
     */
    public Unit findById(Integer id) throws Exception;

    /**
     * Lee la información de una unidad de medida.
     *
     * @param name Nombre de la unidad de medida a ser consultada.
     *
     * @return Instancia con los datos de la unidad de medida.
     * @throws Exception Error en la base de datos.
     */
    public Unit findByName(String name) throws Exception;

    /**
     * Actualiza la información de una unidad de medida en la base de datos.
     *
     * @param unit Instancia con los datos de la unidad de medida.
     *
     * @return unit
     *
     * @throws Exception Error en la base de datos.
     */
    public Unit update(Unit unit) throws Exception;

    /**
     *
     * Elimina una unidad de medida de la base de datos.
     *
     * @param id ID de la unidad de medida.
     *
     * @throws Exception Error en base de datos.
     */
    public void delete(Integer id) throws Exception;
}
