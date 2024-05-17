/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.service.interfaces.operation.statistics;

import java.util.List;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.operation.filters.AgileStatisticFilter;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.statistic.AgileStatisticTest;

/**
 * Interfaz de servicios a la informacion de estadisticas rapidas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 04/04/2018
 * @see Creación
 */
public interface AgileStatisticService
{

    /**
     * Consulta la información de estadisticas rapidas: Años.
     *
     * @return Lista de años.
     * @throws Exception Error en la base de datos.
     */
    public List<String> getYearsAgileStatistic() throws Exception;

    /**
     * Consulta la información de estadisticas rapidas: Sede - Prueba.
     *
     * @param idBranch Id de la sede.
     * @param date Fecha en formato yyyymmdd.
     * @param idTest Id del examem
     *
     * @return Si ya existe creado el campo.
     * @throws Exception Error en la base de datos.
     */
    public AgileStatisticTest getStatisticsTestBranch(int date, int idBranch, int idTest) throws Exception;

    /**
     * Consulta la información de estadisticas rapidas: Sede - Prueba por años.
     *
     * @param filter Filtras para estadisticas rapidas.
     *
     * @return Lista de Estadisticas especiales: Sede - Prueba.
     * @throws Exception Error en la base de datos.
     */
    public List<AgileStatisticTest> listStatisticsTestDate(AgileStatisticFilter filter) throws Exception;

    /**
     * Consulta la información de estadisticas rapidas: Sede - Prueba.
     *
     * @param filter Filtros para estadisticas rapidas.
     *
     * @return Lista de Estadisticas especiales: Sede - Examen.
     * @throws Exception Error en la base de datos.
     */
    public List<AgileStatisticTest> listStatisticsTestYears(AgileStatisticFilter filter) throws Exception;

    /**
     * Consulta la información de estadisticas rapidas: Sede.
     *
     * @param filter Filtras para estadisticas rapidas.
     *
     * @return Lista de Estadisticas especiales: Sede.
     * @throws Exception Error en la base de datos.
     */
    public List<AgileStatisticTest> listStatisticsBranchDate(AgileStatisticFilter filter) throws Exception;

    /**
     * Consulta la información de estadisticas rapidas: Sede.
     *
     * @param filter Filtros para estadisticas rapidas.
     *
     * @return Lista de Estadisticas especiales: Sede.
     * @throws Exception Error en la base de datos.
     */
    public List<AgileStatisticTest> listStatisticsBranchYears(AgileStatisticFilter filter) throws Exception;

    /**
     * Actualiza la información de estadisticas rapidas: Sede - Prueba.
     *
     * @param idOrder Numero de la orden.
     * @param idTest Id del examen.
     * @param add Indica si se agregan o disminuyen las cantidades de los campos
     * @param type Indica que campo se actualizara: 1 -> Ingresados, 2 ->
     * Validados, 3 -> Impresos, 4 -> Patologicos.
     *
     * @throws Exception Error en la base de datos.
     */
    public void updateTestBranch(long idOrder, int idTest, boolean add, Integer type) throws Exception;

    /**
     * Actualiza la información de estadisticas rapidas: Sede - Prueba.
     *
     * @param idOrder Numero de la orden.
     * @param add Indica si se agregan o disminuyen las cantidades de los campos
     *
     * @throws Exception Error en la base de datos.
     */
    public void updateOrderBranch(long idOrder, boolean add) throws Exception;

    /**
     * Actualiza la información de estadisticas rapidas: Sede - Prueba.
     *
     * @param idOrder Numero de la orden.
     * @param add Indica si se agregan o disminuyen las cantidades de los campos
     *
     * @throws Exception Error en la base de datos.
     */
    public void updateOrderBranch(long idOrder, boolean add, AuthorizedUser authorizedUser) throws Exception;

    /**
     * *
     * Agrega la información de los examenes creados en la orden.
     *
     * @param idOrder Numero de la orden
     * @param tests Lista de Examenes.
     * @throws java.lang.Exception Error en la base de datos
     */
    public void addOrderTests(long idOrder, List<Test> tests) throws Exception;

    /**
     * Actualiza la información de estadisticas rapidas: Sede - Area cuando se
     * adicionan examenes.
     *
     * @param idOrder Numero de la orden.
     * @param tests lista de examenes
     *
     * @throws Exception Error en la base de datos.
     */
    public void addAreaBranch(long idOrder, List<Test> tests) throws Exception;

    /**
     * Actualiza la información de estadisticas rapidas: Sede - Area cuando se
     * eliminan examenes.
     *
     * @param idOrder Numero de la orden
     * @param tests lista examenes eliminados con la información del area
     * @throws Exception Error en el servicio
     */
    public void deleteAreaBranch(long idOrder, List<Test> tests) throws Exception;

    /**
     * Descuenta por los estados que posee el examen
     *
     * @param idOrder numero de la orden
     * @param idTest id del examen
     */
    public void discountAreaBranch(long idOrder, int idTest);

    /**
     * Consulta la información de estadisticas rapidas: Sede - Prueba por años.
     *
     * @param filter Filtras para estadisticas rapidas.
     *
     * @return Lista de Estadisticas especiales: Sede - Examen.
     * @throws Exception Error en la base de datos.
     */
    public List<AgileStatisticTest> listBranchAreaDate(AgileStatisticFilter filter) throws Exception;

    /**
     * Consulta la información de estadisticas rapidas: Sede - Prueba.
     *
     * @param filter Filtros para estadisticas rapidas.
     *
     * @return Lista de Estadisticas especiales: Sede - Examen.
     * @throws Exception Error en la base de datos.
     */
    public List<AgileStatisticTest> listBranchAreaYears(AgileStatisticFilter filter) throws Exception;
}
