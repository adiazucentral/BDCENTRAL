package net.cltech.enterprisent.service.interfaces.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.LiteralByTest;
import net.cltech.enterprisent.domain.masters.test.LiteralResult;

/**
 * Interfaz de servicios a la informacion del maestro Resultado Literal
 *
 * @version 1.0.0
 * @author eacuna
 * @since 22/05/2017
 * @see Creación
 */
public interface LiteralResultService
{

    /**
     * Lista resultado literal desde la base de datos.
     *
     * @return Lista de resultado literal.
     * @throws Exception Error en la base de datos.
     */
    public List<LiteralResult> list() throws Exception;

    /**
     * Registra resultado literal en la base de datos.
     *
     * @param create Instancia con los datos del resultado literal.
     *
     * @return Instancia con los datos del resultado literal.
     * @throws Exception Error en la base de datos.
     */
    public LiteralResult create(LiteralResult create) throws Exception;

    /**
     * Obtener información de resultado literal por un campo especifico.
     *
     * @param id ID de resultado literal a consultar.
     *
     * @return Instancia con los datos del resultado literal.
     * @throws Exception Error en la base de datos.
     */
    public LiteralResult filterById(Integer id) throws Exception;

    /**
     * Obtener información de resultado literal por un campo especifico.
     *
     * @param name name de resultado literal a consultar.
     *
     * @return Instancia con los datos del resultado literal.
     * @throws Exception Error en la base de datos.
     */
    public LiteralResult filterByName(String name) throws Exception;

    /**
     * Obtiene resultados literales asignados a un examen.
     *
     * @param id del examen.
     *
     * @return Lista LiteralByTest campo assign en true para resultados
     *         asignados al examen
     * @throws Exception Error en la base de datos.
     */
    public List<LiteralByTest> filterByTest(Integer id) throws Exception;

    /**
     * Obtener información de resultado literal por un campo especifico.
     *
     * @param state estado activo(true) o inactivo(false).
     *
     * @return Instancia con los datos del resultado literal.
     * @throws Exception Error en la base de datos.
     */
    public List<LiteralResult> filterByState(boolean state) throws Exception;

    /**
     * Actualiza la información de un resultado literal en la base de datos.
     *
     * @param update Instancia con los datos del resultado literal.
     *
     * @return Objeto de resultado literal modificada.
     * @throws Exception Error en la base de datos.
     */
    public LiteralResult update(LiteralResult update) throws Exception;

    /**
     * Asigna los resultados literales a un examen
     *
     * @param results lista de LiteralByTest
     *
     * @return numero de registros insertados
     * @throws Exception
     */
    public int assignResults(List<LiteralByTest> results) throws Exception;

    /**
     * Consulta los resultados literales activos de todos los exámenes, para el reporte de resultados
     *
     * @return Lista de resultados literales
     * @throws Exception
     */    
    public List<LiteralResult> listWithTestId() throws Exception;
    
    /**
     * Consulta los resultados literales activos por orden
     *
     * @return Lista de resultados literales
     * @throws Exception
     */    
    public List<LiteralResult> listByOrder(long order) throws Exception;

}
