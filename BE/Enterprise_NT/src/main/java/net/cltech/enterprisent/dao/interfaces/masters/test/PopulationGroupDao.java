package net.cltech.enterprisent.dao.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.PopulationGroup;

/**
 * Representa los métodos de acceso a base de datos para la información del
 * maestro.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 22/05/2017
 * @see Creación
 */
public interface PopulationGroupDao
{

    /**
     * Lista grupos poblacionales desde la base de datos.
     *
     * @return Lista de grupos poblacionals.
     * @throws Exception Error en la base de datos.
     */
    public List<PopulationGroup> list() throws Exception;

    /**
     * Registra grupos poblacional en la base de datos.
     *
     * @param create Instancia con los datos de grupos poblacional.
     *
     * @return Instancia con los datos de grupos poblacional.
     * @throws Exception Error en la base de datos.
     */
    public PopulationGroup create(PopulationGroup create) throws Exception;

    /**
     * Obtener información de grupos poblacional por nombre.
     *
     * @param name Nombre de grupos poblacional a ser consultada.
     *
     * @return Instancia con los datos de grupos poblacional.
     * @throws Exception Error en la base de datos.
     */
    public PopulationGroup filterByName(String name) throws Exception;

    /**
     * Obtener información de grupos poblacional por nombre.
     *
     * @param id id de grupos poblacional.
     *
     * @return Instancia con los datos de grupos poblacional.
     * @throws Exception Error en la base de datos.
     */
    public PopulationGroup filterById(Integer id) throws Exception;

    /**
     * Actualiza la información de grupos poblacional en la base de datos.
     *
     * @param update Instancia con los datos de grupos poblacional.
     *
     * @return Objeto de grupos poblacional modificada.
     * @throws Exception Error en la base de datos.
     */
    public PopulationGroup update(PopulationGroup update) throws Exception;

}
