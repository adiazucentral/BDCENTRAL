package net.cltech.enterprisent.controllers.operation.results;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.integration.resultados.DetailFilter;
import net.cltech.enterprisent.domain.integration.resultados.DetailStatus;
import net.cltech.enterprisent.domain.integration.resultados.ResultHeader;
import net.cltech.enterprisent.domain.integration.resultados.ResultHeaderFilter;
import net.cltech.enterprisent.domain.integration.resultados.TestDetail;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.interview.TypeInterview;
import net.cltech.enterprisent.domain.masters.test.Area;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.test.TestBasic;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.domain.operation.orders.Result;
import net.cltech.enterprisent.domain.operation.results.BatchResultFilter;
import net.cltech.enterprisent.domain.operation.results.CentralSystemResults;
import net.cltech.enterprisent.domain.operation.results.FindShippedOrders;
import net.cltech.enterprisent.domain.operation.results.HistoryFilter;
import net.cltech.enterprisent.domain.operation.results.PerfilOrder;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import net.cltech.enterprisent.domain.operation.results.ResultOrder;
import net.cltech.enterprisent.domain.operation.results.ResultOrderDetail;
import net.cltech.enterprisent.domain.operation.results.ResultStatistic;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.domain.operation.results.ResultTestBlock;
import net.cltech.enterprisent.domain.operation.results.ResultTestComment;
import net.cltech.enterprisent.domain.operation.results.ResultTestPrint;
import net.cltech.enterprisent.domain.operation.results.ResultTestRepetition;
import net.cltech.enterprisent.domain.operation.results.ResultTestValidate;
import net.cltech.enterprisent.domain.operation.results.TestHistory;
import net.cltech.enterprisent.domain.operation.results.UpdateResult;
import net.cltech.enterprisent.domain.operation.tracking.TestInformationTracking;
import net.cltech.enterprisent.service.interfaces.operation.results.ResultsService;
import net.cltech.enterprisent.tools.JWT;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiPathParam;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.annotation.ApiVersion;
import org.jsondoc.core.pojo.ApiVerb;
import org.jsondoc.core.pojo.ApiVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador para el módulo de registro de resultados
 *
 * @version 1.0.0
 * @author jblanco
 * @since 03/07/2017
 * @see Creación
 */
@Api(
        name = "Resultados",
        group = "Operación - Resultados",
        description = "Servicios Rest para el módulo de resultados"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/results")
public class ResultsController
{

    @Autowired
    private ResultsService resultsService;

    @ApiMethod(
            description = "Obtiene la información estadística del módulo de registro de resultados",
            path = "/api/results/orders/statistics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultStatistic.class)
    @RequestMapping(value = "/orders/statistics", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultStatistic> statistic(
            @ApiBodyObject(clazz = ResultFilter.class) @RequestBody ResultFilter filter
    ) throws Exception
    {
        ResultStatistic resultStatistict = resultsService.getStatistic(filter);
        if (resultStatistict == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(resultStatistict, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Obtiene la lista de órdenes para realizar el registro de resultados",
            path = "/api/results/orders",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultOrder.class)
    @RequestMapping(value = "/orders", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResultOrder>> get(
            @ApiBodyObject(clazz = ResultFilter.class) @RequestBody ResultFilter filter
    ) throws Exception
    {
        List<ResultOrder> resultOrderList = resultsService.getOrders(filter);
        if (resultOrderList.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(resultOrderList, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Realiza la actualización del comentario de la orden",
            path = "/api/results/saveinternalcomment",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultTestComment.class)
    @RequestMapping(value = "/saveinternalcomment", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultTestComment> saveInternalComment(
            @ApiBodyObject(clazz = ResultTestComment.class) @RequestBody ResultTestComment detail
    ) throws Exception
    {
        return new ResponseEntity<>(resultsService.saveInternalComment(detail), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Realiza la actualización del comentario de la orden",
            path = "/api/results/saveobservations",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultTestComment.class)
    @RequestMapping(value = "/saveobservations", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultTestComment> saveobservations(
            @ApiBodyObject(clazz = ResultTestComment.class) @RequestBody ResultTestComment detail
    ) throws Exception
    {
        return new ResponseEntity<>(resultsService.saveObservations(detail), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene el comentario interno de un resultado",
            path = "/api/results/internalcomment/order/{order}/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultTestComment.class)
    @RequestMapping(value = "/internalcomment/order/{order}/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultTestComment> getInternalComment(
            @ApiPathParam(name = "order", description = "Numero de la Orden") @PathVariable("order") long order,
            @ApiPathParam(name = "test", description = "Identificador del examen") @PathVariable("test") int test,
            HttpServletRequest request
    ) throws Exception
    {
        return new ResponseEntity<>(resultsService.getInternalComment(order, test), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene el comentario interno de un resultado",
            path = "/api/results/observations/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultTestComment.class)
    @RequestMapping(value = "/observations/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultTestComment> getInternalObservations(
            @ApiPathParam(name = "order", description = "Numero de la Orden") @PathVariable("order") long order,
            HttpServletRequest request
    ) throws Exception
    {
        return new ResponseEntity<>(resultsService.getInternalObservations(order), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene la lista de resultados de una orden",
            path = "/api/results/orders/tests",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultTest.class)
    @RequestMapping(value = "/orders/tests", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResultTest>> listTest(
            @ApiBodyObject(clazz = ResultFilter.class) @RequestBody ResultFilter filter
    ) throws Exception
    {
        List<ResultTest> resultTestList = resultsService.getTests(filter, null);
        if (resultTestList == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(resultTestList, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Realiza la actualización de un examen",
            path = "/api/results",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultTest.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultTest> update(
            @ApiBodyObject(clazz = ResultTest.class) @RequestBody ResultTest test, HttpServletRequest request
    ) throws Exception
    {
        test.setUserId(JWT.decode(request).getId());
        return new ResponseEntity<>(resultsService.reportedTest(test), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Realiza la actualización de resultados de exámenes en lote",
            path = "/api/results/batch",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/batch", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> batchUpdate(
            @ApiBodyObject(clazz = BatchResultFilter.class) @RequestBody BatchResultFilter filter
    ) throws Exception
    {
        return new ResponseEntity<>(resultsService.batchUpdateResult(filter), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Realiza la actualización comentario de un examen",
            path = "/api/results/comment",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultTest.class)
    @RequestMapping(value = "/comment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultTest> updateCommentResult(
            @ApiBodyObject(clazz = ResultTest.class) @RequestBody ResultTest test, HttpServletRequest request
    ) throws Exception
    {
        test.setUserId(JWT.decode(request).getId());
        return new ResponseEntity<>(resultsService.reportedCommentTest(test), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Verifica examenes",
            path = "/api/results/validate/order/{order}/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/validate/order/{order}/test/{test}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> validateTest(
            @ApiPathParam(name = "order", description = "Numero de la Orden") @PathVariable("order") long order,
            @ApiPathParam(name = "test", description = "Identificador del examen") @PathVariable("test") int test,
            HttpServletRequest request
    ) throws Exception
    {
        resultsService.validatedTest(order, test, JWT.decode(request).getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiMethod(
            description = "Imprime un examen",
            path = "/api/results/printedTest/order/{order}/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/printedTest/order/{order}/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> printedTest(
            @ApiPathParam(name = "order", description = "Numero de la Orden") @PathVariable("order") long order,
            @ApiPathParam(name = "test", description = "Identificador del examen") @PathVariable("test") int test,
            HttpServletRequest request
    ) throws Exception
    {
        resultsService.printedTest(order, test, JWT.decode(request).getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene la información del detalle de la orden para el módulo de registro de resultados",
            path = "/api/results/orders/detail/{orderId}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultStatistic.class)
    @RequestMapping(value = "/orders/detail/{orderId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultOrderDetail> detail(
            @ApiPathParam(name = "orderId", description = "Identificador de la orden") @PathVariable("orderId") Long orderId
    ) throws Exception
    {
        ResultOrderDetail orderDetail = resultsService.getOrderDetail(orderId);
        if (orderDetail == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(orderDetail, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Realiza la actualización del comentario de la orden",
            path = "/api/results/orders/detail",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultOrderDetail.class)
    @RequestMapping(value = "/orders/detail", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultOrderDetail> updateComment(
            @ApiBodyObject(clazz = ResultOrderDetail.class) @RequestBody ResultOrderDetail detail
    ) throws Exception
    {
        return new ResponseEntity<>(resultsService.updateOrderDetail(detail), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Realiza la actualización de un examen",
            path = "/api/results/repeats/order/{order}/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultTestRepetition.class)
    @RequestMapping(value = "/repeats/order/{order}/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResultTestRepetition>> listTestRepetition(
            @ApiPathParam(name = "order", description = "Numero de la orden") @PathVariable("order") Long order,
            @ApiPathParam(name = "test", description = "Id del exámen") @PathVariable("test") int test
    ) throws Exception
    {
        List<ResultTestRepetition> list = resultsService.listTestRepetitions(order, test);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Realiza la consulta de los resultados históricos de un examen",
            path = "/api/results/history",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultTest.class)
    @RequestMapping(value = "/history", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestHistory>> listTestHistory(
            @ApiBodyObject(clazz = HistoryFilter.class) @RequestBody HistoryFilter filter
    ) throws Exception
    {
        List<TestHistory> list = resultsService.listTestHistory(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    
    @ApiMethod(
            description = "Realiza consulta de los resultados de historico de un examen por Tipo de usuario y id",
            path = "/api/results/history/byRolUser",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestHistory.class )
    @RequestMapping(value = "/history/byRolUser", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestHistory>> listTestHistoryFilterIduserTypeUser(
    
            @ApiBodyObject(clazz = HistoryFilter.class) @RequestBody HistoryFilter historyFilterIduserTypeUser 
        )throws Exception
    {
        List<TestHistory> list = resultsService.listTestHistoryFilterUser(historyFilterIduserTypeUser);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
   

    @ApiMethod(
            description = "Agrega o elimina examenes de una orden",
            path = "/api/results/tests/addremove/type/{type}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/tests/addremove/type/{type}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResultTest>> addRemoveTest(
            @ApiPathParam(name = "type", description = "Tipo: 2 -> Ordenada, 4 -> Verificada") @PathVariable("type") int type,
            @ApiBodyObject(clazz = Order.class) @RequestBody Order order, HttpServletRequest request
    ) throws Exception
    {
        return new ResponseEntity<>(resultsService.addRemoveTest(order, type, JWT.decode(request)), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Cambio de estado a reportado",
            path = "/api/results/reported/tests",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/reported/tests", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> batchUpdate(
            @ApiBodyObject(clazz = Order.class) @RequestBody Order order,
            HttpServletRequest request
    ) throws Exception
    {
        AuthorizedUser user = JWT.decode(request);
        return new ResponseEntity<>(resultsService.reportTests(order, -1, user.getName() + " " + user.getLastName(), false), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Validación de pruebas",
            path = "/api/results/order/validate",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/order/validate", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultTestValidate> validateTests(
            @ApiBodyObject(clazz = ResultTestValidate.class) @RequestBody ResultTestValidate list,
            HttpServletRequest request
    ) throws Exception
    {
        return new ResponseEntity<>(resultsService.validatedTests(list, JWT.decode(request), false), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Alarmas para validación de pruebas",
            path = "/api/results/order/validate/alarms",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/order/validate/alarms", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultTestValidate> validateTestsAlarms(
            @ApiBodyObject(clazz = ResultTestValidate.class) @RequestBody ResultTestValidate list,
            HttpServletRequest request
    ) throws Exception
    {
        return new ResponseEntity<>(resultsService.validatedTests(list, JWT.decode(request), true), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Bloqueo  o desbloqueo de examenes",
            path = "/api/results/blocked",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/blocked", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultTestBlock> blockTest(
            @ApiBodyObject(clazz = ResultTestBlock.class) @RequestBody ResultTestBlock test
    ) throws Exception
    {
        ResultTestBlock obj = resultsService.blockTest(test);
        if (obj == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "imprime o no imprime el resultado del examenes",
            path = "/api/results/print",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/print", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultTestPrint> printTest(
            @ApiBodyObject(clazz = ResultTestPrint.class) @RequestBody ResultTestPrint test
    ) throws Exception
    {

        ResultTestPrint obj = resultsService.printTest(test);

        if (obj == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(obj, HttpStatus.OK);
        }
    }

    //CONSULTAR INFORMACION DE UN RESULTADO
    @ApiMethod(
            description = "Obtiene la información de un resultado de una orden",
            path = "/api/results/information/order/{order}/test/{test}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Result.class)
    @RequestMapping(value = "/information/order/{order}/test/{test}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TestInformationTracking> getInformation(
            @ApiPathParam(name = "order", description = "Identificador de la orden") @PathVariable("order") Long idOrder,
            @ApiPathParam(name = "test", description = "Identificador de la prueba") @PathVariable("test") Integer idTest
    ) throws Exception
    {
        TestInformationTracking result = resultsService.getInformation(idOrder, idTest);
        if (result == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    //OBTENER LA ENTREVISTA DE PÁNICO
    @ApiMethod(
            description = "Obtiene la entrevista de pánico para la validación de los resultados",
            path = "/api/results/survey",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Question.class)
    @RequestMapping(value = "/survey", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Question>> getPanicSurvey() throws Exception
    {
        List<Question> questions = resultsService.getPanicSurvey();
        if (questions == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(questions, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Obtiene la union de los resultados de los examenes padre e hijo para rellamado",
            path = "/api/results/getTestsuniondaughter",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultTest.class)
    @RequestMapping(value = "/getTestsuniondaughter", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResultTest>> getTestsuniondaughter(
            @ApiBodyObject(clazz = Integer.class) @RequestBody List<Long> orders
    ) throws Exception
    {
        List<ResultTest> results = resultsService.getTestsUnionDaughter(orders, "", false);
        if (results == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(results, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Obtiene lista de resultados por orden con datos para panico",
            path = "/api/results/getresults/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ResultTest.class)
    @RequestMapping(value = "/getresults/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResultTest>> getResults(
            @ApiPathParam(name = "order", description = "Identificador de la orden") @PathVariable("order") Long idOrder
    ) throws Exception
    {
        List<ResultTest> results = resultsService.get(idOrder);
        if (results.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(results, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Asignacion del resultado de la prueba, calculandolo apartir de la formula",
            path = "/api/results/order/assignformulavalue",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/order/assignformulavalue", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultTestValidate> assignFormulaValue(
            @ApiBodyObject(clazz = ResultTestValidate.class) @RequestBody ResultTestValidate list,
            HttpServletRequest request
    ) throws Exception
    {
        return new ResponseEntity<>(resultsService.assignFormulaValue(list, JWT.decode(request)), HttpStatus.OK);
    }

    // -------------------- ACTUALIZACIÓN DE LA ORDEN ---------------------
    @ApiMethod(
            description = "Actualización de la orden según el id del examen",
            path = "/api/results/updateResultForExam",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/updateResultForExam", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateOrderForExam(
            @ApiBodyObject(clazz = UpdateResult.class) @RequestBody UpdateResult updateResult
    ) throws Exception
    {
        resultsService.updateResultForExam(updateResult);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //------------ LISTAR NOMBRE DE EXAMENES POR ORDEN Y MUESTRA ----------------
    @ApiMethod(
            description = "Lista nombre de examenes por orden y muestra",
            path = "/api/results/gettestby/order/{order}/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TestBasic.class)
    @RequestMapping(value = "/gettestby/order/{order}/sample/{sample}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestBasic>> listNameTests(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long order,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") int sample
    ) throws Exception
    {
        return new ResponseEntity<>(resultsService.getTestByOrderSample(order, sample), HttpStatus.OK);
    }

    //------------ LISTAR NOMBRE DE AREAS POR ORDEN Y MUESTRA ----------------
    @ApiMethod(
            description = "Lista nombre de examenes por orden y muestra",
            path = "/api/results/getareasby/order/{order}/sample/{sample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Area.class)
    @RequestMapping(value = "/getareasby/order/{order}/sample/{sample}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Area>> listNameAreas(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long order,
            @ApiPathParam(name = "sample", description = "Muestra") @PathVariable(name = "sample") int sample
    ) throws Exception
    {
        return new ResponseEntity<>(resultsService.getAreasByOrderSample(order, sample), HttpStatus.OK);
    }

    //------------ LISTAR MUESTRAS PENDIENTES POR TOMA DE MUESTRA ----------------
    @ApiMethod(
            description = "Lista nombre de examenes por orden y muestra",
            path = "/api/results/getsamplestotake/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Sample.class)
    @RequestMapping(value = "/getsamplestotake/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Sample>> listSampleToTake(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long order
    ) throws Exception
    {
        return new ResponseEntity<>(resultsService.getSamplesToTake(order), HttpStatus.OK);
    }

    //------------ LISTAR LOS PERFILES ----------------
    @ApiMethod(
            description = "Lista los perfiles",
            path = "/api/results/getprofiles/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/getprofiles/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Integer>> listProfiles(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long order
    ) throws Exception
    {
        List<Integer> listProfiles = resultsService.getProfiles(order);
        if (listProfiles.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(listProfiles, HttpStatus.OK);
        }
    }

    //------------ VERIFICA SI LOS HIJOS DE UN PERFIL ESTAN MARCADOS COMO TOMADOS ----------------
    @ApiMethod(
            description = "Verifica los hijos de un perfil",
            path = "/api/results/childtake/order/{order}/idprofile{idProfile}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PerfilOrder.class)
    @RequestMapping(value = "/childtake/order/{order}/idprofile{idProfile}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> childTake(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long order,
            @ApiPathParam(name = "idProfile", description = "id del perfil") @PathVariable(name = "idProfile") int idProfile
    ) throws Exception
    {
        return new ResponseEntity<>(resultsService.isAllChildTaked(order, idProfile), HttpStatus.OK);
    }

    //------------ MARCA EL PERFIL COMO TOMADO ----------------
    @ApiMethod(
            description = "Marca el perfil como tomado",
            path = "/api/results/checkprofile/order{order}/profile{profile}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/checkprofile/order{order}/profile{profile}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> updateCheckProfile(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long order,
            @ApiPathParam(name = "profile", description = "id del perfil") @PathVariable(name = "profile") int profile
    ) throws Exception
    {
        resultsService.checkProfileAsTaked(order, profile);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //------------ VERIFICA SI LA MUESTRA ESTA TOMADA ----------------
    @ApiMethod(
            description = "Verifica si la muestra esta tomada",
            path = "/api/results/sampletaken/order/{order}/codesample/{codeSample}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = PerfilOrder.class)
    @RequestMapping(value = "/sampletaken/order/{order}/codesample/{codeSample}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> sampleTake(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long order,
            @ApiPathParam(name = "codeSample", description = "Codigo de la muestra") @PathVariable(name = "codeSample") String codeSample
    ) throws Exception
    {
        return new ResponseEntity<>(resultsService.isSampleTaken(order, codeSample), HttpStatus.OK);
    }

    //------------ OBTIENE UN LISTADO DE LAS ORDENES QUE NO SE HAN ENVIADO AL HIS ----------------
    @ApiMethod(
            description = "Obtiene un listado de las ordenes que no se han enviado al sistema central",
            path = "/api/results/sendOrderResultsCentralSystem",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/sendOrderResultsCentralSystem", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> sendOrderResultsCentralSystem(
            @ApiBodyObject(clazz = CentralSystemResults.class) @RequestBody CentralSystemResults centralSystemResults
    ) throws Exception
    {
        List<Order> listFoundOrders = resultsService.sendOrderResultsCentralSystem(centralSystemResults);
        if (!listFoundOrders.isEmpty())
        {
            return new ResponseEntity<>(listFoundOrders, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
    }

    //------------ SE ENCARGA DE ACTUALIZAR LAB57C50 - ENVIO AL SISTEMA EXTERNO ----------------
    @ApiMethod(
            description = "Actualiza el resultado de un examen de una orden, indicando que este ya se envio a un sistema central",
            path = "/api/results/updateSentCentralSystem/idOrder/{idOrder}/idTest/{idTest}/idCentralSystem/{idCentralSystem}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/updateSentCentralSystem/idOrder/{idOrder}/idTest/{idTest}/idCentralSystem/{idCentralSystem}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> updateSentCentralSystem(
            @ApiPathParam(name = "idOrder", description = "id de la orden") @PathVariable(name = "idOrder") long idOrder,
            @ApiPathParam(name = "idTest", description = "id del examen") @PathVariable(name = "idTest") int idTest,
            @ApiPathParam(name = "idCentralSystem", description = "id del sistema central") @PathVariable(name = "idCentralSystem") int idCentralSystem
    ) throws Exception
    {
        return new ResponseEntity<>(resultsService.updateSentCentralSystem(idOrder, idTest, idCentralSystem), HttpStatus.OK);
    }

    //------------ ORDENES QUE YA HAN SIDO ENVIADAS A UN SISTEMA CENTRAL EXTERNO ----------------
    @ApiMethod(
            description = "Retorna las ordenes que han sido enviadas a un sistema central externo",
            path = "/api/results/findShippedOrders/{idTest}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/findShippedOrders/{idTest}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FindShippedOrders>> findShippedOrdersCentralSystem(
            @ApiPathParam(name = "idTest", description = "id del examen") @PathVariable(name = "idTest") int idTest
    ) throws Exception
    {
        List<FindShippedOrders> listFoundOrders = resultsService.findShippedOrdersCentralSystem(idTest);
        if (!listFoundOrders.isEmpty())
        {
            return new ResponseEntity<>(listFoundOrders, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
    }

    //------------ ELIMINAR REGISTRO DE RESULTADOS ANULADOS ----------------
    @ApiMethod(
            description = "Elimina el registro asociado al examen que tienen el resultado desvalidado",
            path = "/api/results/removeDevalidatedResults/{idOrden}/{idTest}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/removeDevalidatedResults/{idOrden}/{idTest}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> removeDevalidatedResults(
            @ApiPathParam(name = "idOrden", description = "id de la orden") @PathVariable(name = "idOrden") Long idOrden,
            @ApiPathParam(name = "idTest", description = "id del examen") @PathVariable(name = "idTest") int idTest
    ) throws Exception
    {
        int patientResult = resultsService.removeDevalidatedResults(idOrden, idTest);

        if (patientResult != -1)
        {
            return new ResponseEntity(patientResult, HttpStatus.OK);
        } else
        {
            return new ResponseEntity(-1, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiMethod(
            description = "Actualiza los valores de referencia de examenes de la orden sin resultados",
            path = "/api/results/updateReferenceValues/idOrder/{idOrder}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/updateReferenceValues/idOrder/{idOrder}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> updateReferenceValues(
            @ApiPathParam(name = "idOrder", description = "Id de la orden") @PathVariable("idOrder") Long idOrder
    ) throws Exception
    {
        return new ResponseEntity(resultsService.updateReferenceValues(idOrder), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene lista ordenes por resultado de cada orden procesada en el dia",
            path = "/api/results/getOrdersResults/{date}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/getOrdersResults/{date}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getOrdersResults(
            @ApiPathParam(name = "date", description = "Fecha") @PathVariable("date") Long date
    ) throws Exception
    {
        List<Order> orders = resultsService.getOrdersResults(date);
        if (!orders.isEmpty())
        {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
    }

    //OBTENER LOS EXAMENES ASOCIADOS A LA ENTREVISTA DE PANICO
    @ApiMethod(
            description = "Obtiene los examenes asociados a la entrevista de pánico para la validación de los resultados",
            path = "/api/results/survey/tests",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = TypeInterview.class)
    @RequestMapping(value = "/survey/tests", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TypeInterview>> getTestPanicSurvey() throws Exception
    {
        List<TypeInterview> list = resultsService.getTestPanicSurvey();
        if (list == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Obtiene el estado minimo de la lista de examenes de una orden",
            path = "/api/results/checkvalidation/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/checkvalidation/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> checkValidationOrder(
            @ApiPathParam(name = "order", description = "Identificador de la orden") @PathVariable("order") Long idOrder
    ) throws Exception
    {
        return new ResponseEntity<>(resultsService.checkValidationOrder(idOrder), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene las cabeceras de las ordenes para la integracion api de resultados",
            path = "/api/results/header",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT"
    )
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/header", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ResultHeader>> header(
            @ApiBodyObject(clazz = ResultHeaderFilter.class) @RequestBody ResultHeaderFilter filter
    ) throws Exception
    {
        List<ResultHeader> list = resultsService.header(filter);
        if (!list.isEmpty())
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene el detalle de una orden para la integracion api de resultados",
            path = "/api/results/detail",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT"
    )
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/detail", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TestDetail>> detail(
            @ApiBodyObject(clazz = DetailFilter.class) @RequestBody DetailFilter filter
    ) throws Exception
    {
        List<TestDetail> list = resultsService.detail(filter);
        if (!list.isEmpty())
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Marca el envio de un examen - api de resultados",
            path = "/api/results/status",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT"
    )
    @ApiResponseObject(clazz = DetailStatus.class)
    @RequestMapping(value = "/status", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DetailStatus> status(
            @ApiBodyObject(clazz = DetailStatus.class) @RequestBody DetailStatus status
    ) throws Exception
    {
        return new ResponseEntity<>(resultsService.status(status), HttpStatus.OK);
    }  
}
