package net.cltech.enterprisent.service.interfaces.integration;

import java.util.List;
import net.cltech.enterprisent.domain.integration.middleware.AnatomicalSiteMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.AntibioticMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.CheckDestination;
import net.cltech.enterprisent.domain.integration.middleware.DemographicItemMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.DemographicMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.DestinationCovered;
import net.cltech.enterprisent.domain.integration.middleware.MicroorganismsMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.MiddlewareMessage;
import net.cltech.enterprisent.domain.integration.middleware.MiddlewareUrl;
import net.cltech.enterprisent.domain.integration.middleware.SampleMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.TestMiddleware;
import net.cltech.enterprisent.domain.integration.middleware.TestToMiddleware;
import net.cltech.enterprisent.domain.operation.microbiology.MicrobiologyGrowth;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;

/**
 * Interface para integracion de middleware
 *
 * @version 1.0.0
 * @author eacuna
 * @since 18/04/2018
 * @see Creación
 */
public interface IntegrationMiddlewareService
{

    /**
     * Prepara la orden para enviar al sistema del middleware
     *
     * @param order Objeto con la informacion de la orden y el id del
     * laboratorio
     * @param listTestBefore Lista de examenes actuales de la orden
     * @param Sample numero de orden
     * @param origin De donde viene 1 -> Ingreso 2 -> Verificacion
     * @param laboratorys Ids de los laboratios de procesamiento
     * @param microbiologyGrowth
     * @param listTestDelete
     * @param branch
     * @param retake indica si es una retoma
     * @return Numero de mensajes enviados
     */
    public int sendOrderASTM(long order, List<Test> listTestBefore, String Sample, int origin, String laboratorys, MicrobiologyGrowth microbiologyGrowth, List<Test> listTestDelete, int branch, Boolean retake);

    
    

    /**
     * Envía la orden al sistema del Middleware
     *
     * @param json Lista de
     * net.cltech.enterprisent.domain.integration.middleware.MiddlewareMessage
     * @param url Url del servicio de Middleware
     * @return
     * @throws Exception
     */
    public boolean sendMiddleware(List<MiddlewareMessage> json, String url) throws Exception;
    

    /**
     * Envía la orden al sistema del Middleware
     * @param order    
     * @param branch   
     * @param retake indica si es una retoma
     * @return
     * @throws Exception
     */
    public boolean sendTestDeleteMiddleware(Order order, int branch, Boolean retake) throws Exception;


    /**
     * Prueba la url del laboratorio en el middleware
     *
     * @param url Objeto con la informacion de la url del middleware
     * @return boolean La url es valida o no
     * @throws Exception Error en el servicio
     */
    public boolean testASTM(MiddlewareUrl url) throws Exception;

    /**
     * Reenvio de ordenes al middleware
     *
     * @param resultFilter
     * @return boolean La url es valida o no
     * @throws Exception Error en el servicio
     */
    public int resend(ResultFilter resultFilter) throws Exception;

    /**
     * Lista con datos especificos de muestras desde la base de datos
     *
     * @return Lista de Muestras al middleware
     * @throws Exception Error en base de datos
     */
    public List<SampleMiddleware> list() throws Exception;

    /**
     * Lista la importación de exámenes al Middleware
     *
     * @return Lista de Examenes al middleware
     * @throws Exception Error en base de datos
     */
    public List<TestMiddleware> listTest() throws Exception;

    /**
     * Lista la importación de demográficos al Middleware
     *
     * @return Lista de demográficos al Middleware
     * @throws Exception Error en base de datos
     */
    public List<DemographicMiddleware> listDemographicAll() throws Exception;

    /**
     * Lista para la importación de antibióticos al Middleware
     *
     * @return Lista de antibioticos de la base de datos
     * @throws java.lang.Exception
     */
    public List<AntibioticMiddleware> listAntibiotics() throws Exception;

    /**
     * Lista para la importación de sitios anatómicos al Middleware
     *
     * @return lista de sitios anatomicos
     * @throws Exception
     */
    public List<AnatomicalSiteMiddleware> listAnatomicalSite() throws Exception;

    /**
     * Lista para la importación de microorganismos al Middleware
     *
     * @return lista de microorganismos
     * @throws Exception Error en base de datos
     */
    public List<MicroorganismsMiddleware> listMicroorganisms() throws Exception;

    /**
     * Lista los demograficos con un parametro extra
     *
     * @param demographic
     * @return los resultados segun el parametro enviado
     * @throws java.lang.Exception
     */
    public List<DemographicItemMiddleware> listDemographicItem(Integer demographic) throws Exception;

    /**
     * Inserta los resultados enviados por el Middleware
     *
     * @param messages Mensaje enviado del Middlware
     * @return True si inserta resultados<br>False si hay errores en la
     * insercion
     * @throws Exception Error en el servicio
     */
    public boolean middlewareResults(List<MiddlewareMessage> messages) throws Exception;

    /**
     * Obtiene una lista de examenes con valores de referencia para ser envaidos
     * al middleware
     *
     * @return Lista de examenes para el middleware
     * @throws Exception Error en el servicio
     */
    public List<TestToMiddleware> deltacheck() throws Exception;

    /**
     * Verifica si la muestra lleva tapa o no
     *
     * @param order
     * @param samplecode Codigo de la muestra
     *
     * @return numero de registros afectados
     * @throws Exception Error base de datos
     */
    public DestinationCovered covered(String order, String samplecode) throws Exception;

    /**
     * Verifica la muestra de una orden en un destino especifico
     *
     * @param checkDestination
     *
     * @return numero de registros afectados
     * @throws Exception Error base de datos
     */
    public CheckDestination checkDestinationMiddleware(CheckDestination checkDestination) throws Exception;

}
