package net.cltech.enterprisent.service.interfaces.integration;

import java.util.List;
import net.cltech.enterprisent.domain.DTO.integration.resultados.ResponseOrderDetailResultHIS;
import net.cltech.enterprisent.domain.integration.resultados.RequestOrdersResultados;
import net.cltech.enterprisent.domain.integration.resultados.RequestUpdateSendResult;
import net.cltech.enterprisent.domain.integration.resultados.ResponseDetailMicroorganisms;
import net.cltech.enterprisent.domain.integration.resultados.ResponseOrderDetailResult;
import net.cltech.enterprisent.domain.integration.resultados.ResponseOrderResult;
import net.cltech.enterprisent.domain.operation.orders.OrderList;

/**
 * Interface para la integracion de la interfaz de resultados
 *
 * @version 1.0.0
 * @author javila
 * @since 04/03/2020
 * @see Creación
 */
public interface IntegrationResultadosService
{

    /**
     * Obtendra una lista de ordenes dentro del rango de una orden inicial hasta
     * una orden final
     *
     * @param requestOrdersResultados
     * @return
     * @throws Exception Error al retornar el rango de ordenes especificado
     */
    public List<ResponseOrderResult> ordersRange(RequestOrdersResultados requestOrdersResultados) throws Exception;

    /**
     * Consulta las ordenes con sus demograficos para ser enviadas con examenes
     * validados para ser enviados al HIS
     *
     * @return
     * @throws Exception Error al retornar el rango de ordenes especificado
     */
    public List<OrderList> orderspendingsendhis() throws Exception;

    /**
     * Resultados para la interfaz de resultados por orden y codigo central
     *
     * @param order
     * @param centralSystem
     * @param funtionProfileId Este parametro se encargara de devolver el id del
     * perfil de una manera u otra
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    public List<ResponseOrderDetailResult> resultsByOrderByCentralCodes(long order, int centralSystem, boolean funtionProfileId) throws Exception;
   
    /**
     * Resultados para la interfaz de resultados por orden y codigo central
     *
     * @param order
     * @param centralSystem

     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    public List<ResponseOrderDetailResultHIS> resultsByOrderByCentralCodes(long order, int centralSystem) throws Exception;

    /**
     * Resultados para la interfaz de resultados por filtro de consulta
     *
     * @param resultsQueryFilter Filtro de consulta
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws java.lang.Exception
     */
   // public List<DTOResultHis> resultsByCentralSystemByDemographicItems(ResultsQueryFilterGlobal resultsQueryFilter) throws Exception;

    /**
     * responde Resultado para la interfaz de resultados HIS por orden y por test
     *
     * @param OrderToSearch class basica orden y idtest
     * @return DTOResultHis objEto general de resultado HIS 
     * @throws java.lang.Exception
     */
  //  public DTOResultHis resultsByCentralSystemByOrderAndIdtest(OrderToSearch orderToSearch) throws Exception;



    /**
     * Actualizar el estado del resultado con su respectiva fecha de
     * actualización
     *
     * @param requestUpdateSendResult
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    public int updateResultStateAndDateUpdate(RequestUpdateSendResult requestUpdateSendResult) throws Exception;

    /**
     * Resultados de microorganismos, antibioticos y antibiogramas para la
     * interfaz de resultados por id orden y id test
     *
     * @param idOrder
     * @param idTest
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    public List<ResponseDetailMicroorganisms> listDetailMicroorganisms(long idOrder, int idTest) throws Exception;

    /**
     * Verifica la muestra para una orden y un examen especifico
     *
     * @param idOrder
     * @param idTest
     * @return Boolean
     * @throws Exception Error en la base de datos.
     */
    public boolean verifySampleLih(long idOrder, int idTest) throws Exception;

    /**
     * Obtendra una lista de ordenes dentro del rango de una orden inicial hasta
     * una orden final
     *
     * @param requestOrdersResultados
     * @return
     * @throws Exception Error al retornar el rango de ordenes especificado
     */
    public List<ResponseOrderResult> getOrdersForOthers(RequestOrdersResultados requestOrdersResultados) throws Exception;

    /**
     * Consulta los resultados de las ordenes que seran enviados a MyM por id de
     * la orden y id del sistema central
     *
     * @param order
     * @param centralSystem
     * @param funtionProfileId Este parametro se encargara de devolver el id del
     * perfil de una manera u otra
     * @return Lista de
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    public List<ResponseOrderDetailResult> getMyMResults(long order, int centralSystem, boolean funtionProfileId) throws Exception;

    /**
     * Actualiza estado de envio al his
     *
     * @param idOrder
     * @param idTest codigo examen
     * @param idCentralSyste estado en el his perfil de una manera u otra
     * @return int
     * {@link net.cltech.enterprisent.domain.operation.results.ResultOrder}
     * @throws Exception Error en la base de datos.
     */
    public int updateSentCentralSystem(long idOrder, String idTest, Integer idCentralSystem) throws Exception;
}
