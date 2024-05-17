package net.cltech.enterprisent.service.interfaces.operation.results;

import java.util.List;
import net.cltech.enterprisent.domain.masters.interview.PanicInterview;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.OrderList;
import net.cltech.enterprisent.domain.operation.results.ResultTest;

/**
 * Interfaz de servicios a la informacion de hojas de trabajo.
 *
 * @version 1.0.0
 * @author eacuna
 * @since 09/10/2017
 * @see Creación
 */
public interface CheckResultService
{

    /**
     * Lista las ordenes por un filtro especifico dado por la clase de listados.
     *
     * @param filter Clase con los filtros definidos
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
        public List<OrderList> listPending(Filter filter) throws Exception;

    /**
     * Lista las ordenes con sus resultados filtrando por los criterios enviados
     *
     * @param filter Criterios de consulta
     *
     * @return LIsta de ordenes con resultados
     * @throws Exception Error en el servicio
     */
    public List<OrderList> list(Filter filter) throws Exception;

    /**
     * Lista los examenes de un paciente
     *
     * @param record   Historia del paciente
     * @param document tipo de documento
     * @param test     id del examen
     *
     * @return Lista de resultados
     * @throws Exception Error
     */
    public List<ResultTest> listByRecord(String record, int document, int test) throws Exception;

     /**
     * Lista las entrevistas de panicos por un filtro especifico.
     *
     * @param filter Clase con los filtros definidos
     *
     * @return Lista de entrevistas.
     * @throws Exception Error en la base de datos.
     */
    public List<PanicInterview> getPanicInterview(Filter filter) throws Exception;
    
    
     /**
     * Lista los resultados utilizados para los valores criticos
     *
     * @param filter Clase con los filtros definidos
     *
     * @return Lista de resultados.
     * @throws Exception Error en la base de datos.
     */
    public List<ResultTest> getCriticalValues(Filter filter) throws Exception;
}
