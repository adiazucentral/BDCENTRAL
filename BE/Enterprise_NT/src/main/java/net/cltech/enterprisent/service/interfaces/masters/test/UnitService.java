package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.Unit;

/**
 * Servicios de configuracion general
 *
 * @version 1.0.0
 * @author eacuna
 * @since 17/04/2017
 * @see Creacion
 */
public interface UnitService
{

    /**
     * Registra una nueva unidad de medida en la base de datos.
     *
     * @param unit Instancia con los datos de la unidad de medida.
     *
     * @return Instancia con los datos de la unidad de medida.
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
    public Unit filterById(Integer id) throws Exception;

    /**
     * Lee la información de una unidad de medida.
     *
     * @param name Nombre de la unidad de medida a ser consultada.
     *
     * @return Instancia con los datos de la unidad de medida.
     * @throws Exception Error en la base de datos.
     */
    public Unit filterByName(String name) throws Exception;

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
     * Lista de unidades deacuerdo al estado
     *
     * @param state
     *
     * @return Lista de unidades de medida
     * @throws Exception
     */
    public List<Unit> filterByState(boolean state) throws Exception;

}
