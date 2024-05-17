package net.cltech.enterprisent.service.interfaces.operation.statistics;

import java.util.List;
import net.cltech.enterprisent.domain.operation.filters.StatisticFilter;
import net.cltech.enterprisent.domain.operation.microbiology.WhonetPlain;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.statistic.SampleByDestinationHeader;
import net.cltech.enterprisent.domain.operation.statistic.StatisticOrder;
import net.cltech.enterprisent.tools.enums.LISEnum;

/**
 * Interfaz de servicios a la informacion de estadisticas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 05/12/2017
 * @see Creación
 */
public interface StatisticService
{

    /**
     * Registra orden para estadisticas
     *
     * @param order información de la orden
     */
    public void saveOrder(Long order);

    /**
     * Registra paciente para estadisticas
     *
     * @param patient informacion de l paciente
     */
    public void savePatient(int patient);

    /**
     * Registra resultado para estadisticas
     *
     * @param order orden con informacion de los examenes
     * @param test id examen
     */
    public void saveTest(Long order, Integer test);

    /**
     * Actualiza el estado de las ordenes enviasdas
     *
     * @param orders lista de ordenes
     * @param state estado a actualizar
     */
    public void disableOrders(List<Long> orders, LISEnum.ResultOrderState state);

    /**
     * Lista las estadisticas de ordenes por un filtro especifico dado por la
     * clase de listados.
     *
     * @param search Clase con los filtros definidos.
     * @param patient Indica si se consulta información adicional del paciente
     * @param prices
     * @param repeated Indica si consulta solo exámenes con repeticiones
     * @param opportunity
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<StatisticOrder> listFilters(StatisticFilter search, boolean patient, boolean prices, boolean repeated, Integer opportunity) throws Exception;
    /**
     * Lista las estadisticas de ordenes por un filtro especifico dado por la
     * clase de listados, independientemente si tiene caja.
     *
     * @param search Clase con los filtros definidos.
     * @param patient Indica si se consulta información adicional del paciente
     * @param prices
     * @param repeated Indica si consulta solo exámenes con repeticiones
     * @param opportunity
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<StatisticOrder> listFiltersBox(StatisticFilter search, boolean patient, boolean prices, boolean repeated, Integer opportunity) throws Exception;

    /**
     * Lista las estadisticas especiales por un filtro especifico dado por la
     * clase de listados.
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<Order> listFiltersStatisticsSpecial(StatisticFilter search) throws Exception;

    /**
     * Lista las estadisticas de microbiologia por un filtro especifico dado por
     * la clase de listados.
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de ordenes.
     * @throws Exception Error en la base de datos.
     */
    public List<StatisticOrder> listFiltersStatisticsMicrobiology(StatisticFilter search) throws Exception;

    /**
     * Lista las estadisticas de motivos de rechazo por un filtro especifico.
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de muestras.
     * @throws Exception Error en la base de datos.
     */
    public List<StatisticOrder> listFiltersSampleReject(StatisticFilter search) throws Exception;

    /**
     * Habilita en estadisticas las lordenes enviadas
     *
     * @param orders lista de ordenes
     */
    public void enableOrders(Long orders);

    /**
     * Estadisticas de examenes con repeticiones
     *
     * @param search filtro de busqueda
     * @return Lista de ordenes encontradas
     * @throws Exception Error en base de datos
     */
    public List<StatisticOrder> statisticRepeated(StatisticFilter search) throws Exception;

    /**
     * Cambio en el estado del exámen o la muestra en tiempos de examen
     *
     * @param state de LISEnum.ResultSampleState
     * @param order
     * @param sample
     */
    public void sampleStateChanged(Integer state, Long order, Integer sample);

    /**
     * Registro de tiempos trascurridos entre estados
     *
     * @param state
     * @param order numero de orden
     * @param testId id del examen
     */
    public void testStateChanged(Integer state, Long order, Integer testId);

    /**
     * Lista de ordenes para muestras en destino
     *
     * @param filter filtro de muestras en destino
     * @return Informacion de muestras en destino
     * @throws Exception Error en el servicio
     */
    public SampleByDestinationHeader sampleByDestiny(StatisticFilter filter) throws Exception;

    /**
     * Lista informacion de microbiología para plano whonet.
     *
     * @param search Clase con los filtros definidos.
     *
     * @return Lista de registros whonet.
     * @throws Exception Error en la base de datos.
     */
    public List<WhonetPlain> listMicrobiologyWhonet(StatisticFilter search) throws Exception;
    
    /**
     * Cambio en el estado de la muestra en Estadisticas - Resultados
     *
     * @param state de LISEnum.ResultSampleState
     * @param order
     * @param tests
     * @throws java.lang.Exception
     */
    public void updateSampleStatus(Integer state, Long order, String tests) throws Exception;
    
    /**
     * Registra orden para estadisticas
     *
     * @param order información de la orden
     */
    public void saveOrderUpdate(Long order);
    
    /**
     * Actualiza el estado de las ordenes enviasdas
     *
     * @param orders lista de ordenes
     * @param state estado a actualizar
     */
    public void disableOrder(Long orders, LISEnum.ResultOrderState state);
}
