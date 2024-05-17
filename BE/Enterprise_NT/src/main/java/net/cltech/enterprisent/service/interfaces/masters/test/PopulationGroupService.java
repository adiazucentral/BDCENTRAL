package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.PopulationGroup;

/**
 * Interfaz de servicios a la informacion del maestro Grupo poblacional
 *
 * @version 1.0.0
 * @author eacuna
 * @since 22/05/2017
 * @see Creación
 */
public interface PopulationGroupService
{

    /**
     * Lista grupo poblacional desde la base de datos.
     *
     * @return Lista de grupo poblacional.
     * @throws Exception Error en la base de datos.
     */
    public List<PopulationGroup> list() throws Exception;

    /**
     * Registra grupo poblacional en la base de datos.
     *
     * @param create Instancia con los datos del grupo poblacional.
     *
     * @return Instancia con los datos del grupo poblacional.
     * @throws Exception Error en la base de datos.
     */
    public PopulationGroup create(PopulationGroup create) throws Exception;

    /**
     * Obtener información de grupo poblacional por un campo especifico.
     *
     * @param id ID de grupo poblacional a consultar.
     *
     * @return Instancia con los datos del grupo poblacional.
     * @throws Exception Error en la base de datos.
     */
    public PopulationGroup filterById(Integer id) throws Exception;

    /**
     * Obtener información de grupo poblacional por un campo especifico.
     *
     * @param name name de grupo poblacional a consultar.
     *
     * @return Instancia con los datos del grupo poblacional.
     * @throws Exception Error en la base de datos.
     */
    public PopulationGroup filterByName(String name) throws Exception;

    /**
     * Obtener información de grupo poblacional por un campo especifico.
     *
     * @param state estado activo(true) o inactivo(false).
     *
     * @return Instancia con los datos del grupo poblacional.
     * @throws Exception Error en la base de datos.
     */
    public List<PopulationGroup> filterByState(boolean state) throws Exception;

    /**
     * Actualiza la información de un grupo poblacional en la base de datos.
     *
     * @param update Instancia con los datos del grupo poblacional.
     *
     * @return Objeto de grupo poblacional modificada.
     * @throws Exception Error en la base de datos.
     */
    public PopulationGroup update(PopulationGroup update) throws Exception;

}
