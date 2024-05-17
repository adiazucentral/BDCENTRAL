package net.cltech.enterprisent.service.interfaces.operation.statistics;

import java.util.List;
import net.cltech.enterprisent.domain.operation.filters.StatisticFilter;
import net.cltech.enterprisent.domain.operation.statistic.Histogram;
import net.cltech.enterprisent.domain.operation.statistic.StatisticOrder;
import net.cltech.enterprisent.domain.operation.statistic.StatisticOrderAverageTimes;
import net.cltech.enterprisent.domain.operation.statistic.StatisticResult;

/**
 * Interfaz de servicios a la informacion de estadisticas tiempos entre estados
 *
 * @version 1.0.0
 * @author eacuna
 * @since 06/02/2018
 * @see Creación
 */
public interface OpportunityService
{

    /**
     * Registra orden para estadisticas
     *
     * @param test Informacion de fechas de estados del examen
     */
    public void saveTest(StatisticResult test);

    /**
     * Lista ordenes que no se encuentran validadas y estan proximas a vencerse
     * o ya estan vencidas
     *
     * @param filter filtros de la orden
     * @return Lista de ordenes
     * @throws java.lang.Exception
     */
    public List<StatisticOrder> controlList(StatisticFilter filter) throws Exception;

    /**
     * Lista ordenes validadas con sus tiempos de oportunidad
     *
     * @param filter filtros de ordenes y examenes
     * @return Lista de ordenes
     * @throws Exception Error no controlado
     */
    public List<StatisticOrder> opportunityIndicators(StatisticFilter filter) throws Exception;

    /**
     * Lista los tiempos promedio de una orden y un examen.
     *
     * @param filter filtros de ordenes y examenes
     * @return Lista de ordenes
     * @throws Exception Error no controlado
     */
    public StatisticOrderAverageTimes averageTimes(StatisticFilter filter) throws Exception;
    /**
     * Consulta tiempos de oportunidad, Lista todos los exámenes con sus tiempos
     * registrados
     *
     * @param filter filtros de ordenes y examenes
     * @param averageTime Indica si se consultan los tiempos promedio.
     * @return Lista de ordenes
     * @throws Exception Error no controlado
     */
    public List<StatisticOrder> opportunityTimes(StatisticFilter filter, boolean averageTime) throws Exception;

    /**
     * Datos para graficar el histograma
     *
     * @param filter filtros de ordenes y examenes
     * @return Objeto con información para generar el histograma de tiempos
     * @throws Exception Error no controlado.
     */
    public Histogram histogram(StatisticFilter filter) throws Exception;
    
    /**
     * Registra orden para estadisticas
     *
     * @param tests Informacion de fechas de estados de los examenes
     */
    public void saveTestBatch(List<StatisticResult> tests, Long orderNumber);

}
