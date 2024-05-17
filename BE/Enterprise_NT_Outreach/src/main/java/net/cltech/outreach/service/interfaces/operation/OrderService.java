package net.cltech.outreach.service.interfaces.operation;

import java.util.List;
import net.cltech.outreach.domain.operation.Filter;
import net.cltech.outreach.domain.operation.HistoryFilter;
import net.cltech.outreach.domain.operation.OrderSearch;
import net.cltech.outreach.domain.operation.ReportFilter;
import net.cltech.outreach.domain.operation.ResultTest;
import net.cltech.outreach.domain.operation.TestHistory;

/**
 * Servicios de ordenes para Consulta WEB
 *
 * @version 1.0.0
 * @author cmartin
 * @since 07/05/2018
 * @see Creación
 */
public interface OrderService
{
    /**
     * Obtiene las ordenes asociadas a los filtros ingresados
     * 
     * @param filter Filtros
     * @return Lista de ordenes.
     * @throws java.lang.Exception Error en la base de datos.
     */
    public List<OrderSearch> listOrders(Filter filter) throws Exception;
    
    /**
     * Obtiene las resultados asociados a una orden.
     * 
     * @param idOrder Numero de la orden.
     * @param area
     * @return Lista de ordenes.
     * @throws java.lang.Exception Error en la base de datos.
     */
    public List<ResultTest> listResults(long idOrder, int area) throws Exception;
    
    /**
     * Lista las histórico de resultados de una o varias pruebas.
     *
     * @param filter Filtros para el historico de resultados.
     * @return Lista de resultados históricos
     * @throws Exception Error en el servicio
     */
    public String listTestHistory(HistoryFilter filter) throws Exception;
    
    /**
     * Lista ordenes reportadas segun los filtros indicados.
     *
     * @param filter Filtros.
     * @return Lista de ordenes reportadas.
     * @throws Exception Error en el servicio.
     */
    public String listReport(ReportFilter filter) throws Exception;
    
     /**
     * Lista las histórico de resultados de una o varias pruebas.
     *
     * @param filter Filtros para el historico de resultados.
     * @return Lista de resultados históricos
     * @throws Exception Error en el servicio
     */
    public List<TestHistory> listHistory(HistoryFilter filter) throws Exception;
}
