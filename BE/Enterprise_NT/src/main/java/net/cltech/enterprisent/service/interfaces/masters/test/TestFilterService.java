package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.TestFilter;

/**
 * Interfaz de servicios a la informacion del maestro grupo
 *
 * @version 1.0.0
 * @author eacuna
 * @since 25/10/2017
 * @see Creación
 */
public interface TestFilterService
{

    /**
     * Lista los grupos desde la base de datos.
     *
     * @return Lista de grupos.
     * @throws Exception Error en la base de datos.
     */
    public List<TestFilter> list() throws Exception;
    
    /**
     * Lista los grupos y sus examenes desde la base de datos.
     *
     * @return Lista de grupos.
     * @throws Exception Error en la base de datos.
     */
    public List<TestFilter> listTestGroup() throws Exception;
    
    /**
     * Registra una nueva grupo en la base de datos.
     *
     * @param filterTest Instancia con los datos de la grupo.
     *
     * @return Instancia con los datos de la grupo.
     * @throws Exception Error en la base de datos.
     */
    public TestFilter create(TestFilter filterTest) throws Exception;

    /**
     * Obtener información de una grupo por un campo especifico.
     *
     * @param name Nombre de la grupo a ser consultada.
     *
     * @return Instancia con los datos de la grupo.
     * @throws Exception Error en la base de datos.
     */
    public TestFilter findByName(String name) throws Exception;

    /**
     * Obtiene el filtro por id
     *
     * @param id id del filtro
     *
     * @return Filtro
     * @throws Exception
     */
    public TestFilter findById(Integer id) throws Exception;

    /**
     * Actualiza la información de una grupo en la base de datos.
     *
     * @param filterTest Instancia con los datos de la grupo.
     *
     * @return Objeto de la grupo modificada.
     * @throws Exception Error en la base de datos.
     */
    public TestFilter update(TestFilter filterTest) throws Exception;

    /**
     * Obtener información de una grupo por estado.
     *
     * @param state Estado de los grupos a ser consultados
     *
     * @return Instancia con los datos de los grupos.
     * @throws Exception Error en la base de datos.
     */
    public List<TestFilter> list(boolean state) throws Exception;
}
