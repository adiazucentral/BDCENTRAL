package net.cltech.enterprisent.controllers.integration;

import java.util.List;
import net.cltech.enterprisent.domain.integration.siga.SigaBranch;
import net.cltech.enterprisent.domain.integration.siga.SigaFilterOrders;
import net.cltech.enterprisent.domain.integration.siga.SigaLogUser;
import net.cltech.enterprisent.domain.integration.siga.SigaMainLogUser;
import net.cltech.enterprisent.domain.integration.siga.SigaPointOfCare;
import net.cltech.enterprisent.domain.integration.siga.SigaReason;
import net.cltech.enterprisent.domain.integration.siga.SigaRequestLog;
import net.cltech.enterprisent.domain.integration.siga.SigaService;
import net.cltech.enterprisent.domain.integration.siga.SigaTransferByServices;
import net.cltech.enterprisent.domain.integration.siga.SigaTransferData;
import net.cltech.enterprisent.domain.integration.siga.SigaTurnCall;
import net.cltech.enterprisent.domain.integration.siga.SigaTurnGrid;
import net.cltech.enterprisent.domain.integration.siga.SigaTurnMovement;
import net.cltech.enterprisent.domain.integration.siga.SigaUrlBranch;
import net.cltech.enterprisent.domain.operation.orders.Order;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationSigaService;
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
 * Implementacion de integración para SIGA.
 *
 * @version 1.0.0
 * @author Jrodriguez
 * @since 16/10/2018
 * @see Creacion
 */
@Api(
        name = " Integracion Siga",
        group = "Integración",
        description = "Servicios generales de integración con SIGA"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/integration/siga")
public class IntegrationSigaController
{

    @Autowired
    private IntegrationSigaService integrationSigaService;

    // LISTA SE SEDES DEL SIGA 
    @ApiMethod(
            description = "Lista las sedes del siga",
            path = "/api/integration/siga/branches",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SigaBranch.class)
    @RequestMapping(value = "/branches", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SigaBranch>> listBranches() throws Exception
    {
        List<SigaBranch> list = integrationSigaService.listBranches();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    // LISTA DE SERVICIOS DEL SIGA
    @ApiMethod(
            description = "Lista las servicios del siga",
            path = "/api/integration/siga/services/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SigaService.class)
    @RequestMapping(value = "/services/{branch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SigaService>> listService(
            @ApiPathParam(name = "branch", description = "Id de la sede") @PathVariable("branch") int branch
    ) throws Exception
    {
        List<SigaService> list = integrationSigaService.listService(branch);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    // LISTA DE TAQUILLAS DEL SIGA
    @ApiMethod(
            description = "Lista de las taquilas del siga",
            path = "/api/integration/siga/pointOfCares/{branch}/{service}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SigaPointOfCare.class)
    @RequestMapping(value = "/pointOfCares/{branch}/{service}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SigaPointOfCare>> getByBranchService(
            @ApiPathParam(name = "branch", description = "Id de la sede") @PathVariable("branch") int branch,
            @ApiPathParam(name = "service", description = "Id del servicio") @PathVariable("service") int service
    ) throws Exception
    {
        List<SigaPointOfCare> list = integrationSigaService.getByBranchService(branch, service);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    // VERIFICA LA URL DEL SIGA
    @ApiMethod(
            description = "Verificacion de la url del siga",
            path = "/api/integration/siga/verification/url",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SigaBranch.class)
    @RequestMapping(value = "/verification/url", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SigaBranch>> listBranches(
            @ApiBodyObject(clazz = SigaUrlBranch.class) @RequestBody SigaUrlBranch url
    ) throws Exception
    {
        List<SigaBranch> list = integrationSigaService.test(url.getUrl());
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    // OBTIENE LOS SERVICIOS DEL SIGA POR URL Y SEDE
    @ApiMethod(
            description = "Obtiene los servicios del siga por url y sede ",
            path = "/api/integration/siga/service/url/branch",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SigaBranch.class)
    @RequestMapping(value = "/service/url/branch", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SigaService>> SigaService(
            @ApiBodyObject(clazz = SigaUrlBranch.class) @RequestBody SigaUrlBranch url
    ) throws Exception
    {
        List<SigaService> list = integrationSigaService.listServices(url);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    // INICIAR TRABAJO
    @ApiMethod(
            description = "Iniciar trabajo de taquilla del siga",
            path = "/api/integration/siga/startWork",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SigaLogUser.class)
    @RequestMapping(value = "/startWork", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SigaLogUser> startDailyWork(
            @ApiBodyObject(clazz = SigaRequestLog.class) @RequestBody SigaRequestLog requestLog
    ) throws Exception
    {
        requestLog.setAction(1);
        return new ResponseEntity<>(integrationSigaService.changeStateWork(requestLog), HttpStatus.OK);
    }

    // DETENER TRABAJO
    @ApiMethod(
            description = "Detener trabajo de taquilla del siga",
            path = "/api/integration/siga/stopWork",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SigaLogUser.class)
    @RequestMapping(value = "/stopWork", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SigaLogUser> stopDailyWork(
            @ApiBodyObject(clazz = SigaRequestLog.class) @RequestBody SigaRequestLog requestLog
    ) throws Exception
    {
        requestLog.setAction(4);
        return new ResponseEntity<>(integrationSigaService.changeStateWork(requestLog), HttpStatus.OK);
    }

    // PAUSAR TRABAJO
    @ApiMethod(
            description = "Pausar trabajo de taquilla del siga",
            path = "/api/integration/siga/pauseWork",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SigaLogUser.class)
    @RequestMapping(value = "/pauseWork", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SigaLogUser> pauseDailyWork(
            @ApiBodyObject(clazz = SigaRequestLog.class) @RequestBody SigaRequestLog requestLog
    ) throws Exception
    {
        requestLog.setAction(2);
        return new ResponseEntity<>(integrationSigaService.changeStateWork(requestLog), HttpStatus.OK);
    }

    //OBTENER LISTA DE TURNOS DISPONIBLES
    @ApiMethod(
            description = "Lista de turnos activos del siga",
            path = "/api/integration/siga/turnsActive/{branch}/{service}/{point}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SigaTurnGrid.class)
    @RequestMapping(value = "/turnsActive/{branch}/{service}/{point}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SigaTurnGrid>> turnsActive(
            @ApiPathParam(name = "branch", description = "Sede") @PathVariable(name = "branch") int branch,
            @ApiPathParam(name = "service", description = "Servicio") @PathVariable(name = "service") int service,
            @ApiPathParam(name = "point", description = "Taquilla") @PathVariable(name = "point") int point
    ) throws Exception
    {
        return new ResponseEntity<>(integrationSigaService.listActiveTurns(branch, service, point), HttpStatus.OK);
    }

    //SELECCIONA UN TURNO DE FORMA AUTOMATICA
    @ApiMethod(
            description = "Selecciona un turno automatico del siga",
            path = "/api/integration/siga/turnAutomatic/{branch}/{service}/{point}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SigaTurnGrid.class)
    @RequestMapping(value = "/turnAutomatic/{branch}/{service}/{point}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SigaTurnGrid> turnAutomatic(
            @ApiPathParam(name = "branch", description = "Sede") @PathVariable(name = "branch") int branch,
            @ApiPathParam(name = "service", description = "Servicio") @PathVariable(name = "service") int service,
            @ApiPathParam(name = "point", description = "Punto") @PathVariable(name = "point") int point
    ) throws Exception
    {
        return new ResponseEntity<>(integrationSigaService.turnAutomatic(branch, service, point), HttpStatus.OK);
    }

    //SELECCIONA UN TURNO DE FORMA MANUAL
    @ApiMethod(
            description = "Selecciona un turno de forma manual del siga",
            path = "/api/integration/siga/turnmanual",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SigaTurnGrid.class)
    @RequestMapping(value = "/turnmanual", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SigaTurnGrid> turnmanual(
            @ApiBodyObject(clazz = SigaTurnCall.class) @RequestBody SigaTurnCall turn
    ) throws Exception
    {
        return new ResponseEntity<>(integrationSigaService.turnmanual(turn), HttpStatus.OK);
    }

    //PROBAR SI EL TURNO ESTA DISPONIBLE
    @ApiMethod(
            description = "Probar si el turno esta disponible del siga",
            path = "/api/integration/siga/turncall/{turn}/{service}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Boolean.class)
    @RequestMapping(value = "/turncall/{turn}/{service}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> turncall(
            @ApiPathParam(name = "turn", description = "Turno") @PathVariable(name = "turn") int turn,
            @ApiPathParam(name = "service", description = "Servicio") @PathVariable(name = "service") int service
    ) throws Exception
    {
        return new ResponseEntity<>(integrationSigaService.turncall(turn, service), HttpStatus.OK);
    }

    // LISTAS DE MOTIVOS DEL SIGA
    @ApiMethod(
            description = "Lista los motivos de aplazamiento del siga",
            path = "/api/integration/siga/reasons/postponement",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = SigaBranch.class)
    @RequestMapping(value = "/reasons/postponement", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SigaReason>> listPostponement() throws Exception
    {
        List<SigaReason> list = integrationSigaService.listReason();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Lista los motivos de descanso del siga",
            path = "/api/integration/siga/reasons/break",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = SigaBranch.class)
    @RequestMapping(value = "/reasons/break", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SigaReason>> listBreak() throws Exception
    {
        List<SigaReason> list = integrationSigaService.listReasonBreak();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Lista los motivos de cancelacion del siga",
            path = "/api/integration/siga/reasons/cancel",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiResponseObject(clazz = SigaBranch.class)
    @RequestMapping(value = "/reasons/cancel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SigaReason>> listCancel() throws Exception
    {
        List<SigaReason> list = integrationSigaService.listReasonCancel();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //ATIENDE UN TURNO
    @ApiMethod(
            description = "Atiende un turno del siga",
            path = "/api/integration/siga/attendturn",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/attendturn", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> attendTurn(
            @ApiBodyObject(clazz = SigaTurnMovement.class) @RequestBody SigaTurnMovement turnmovement
    ) throws Exception
    {
        turnmovement.setState(3);
        return new ResponseEntity<>(integrationSigaService.changeStateTurn(turnmovement), HttpStatus.OK);
    }

    //FINALIZAR UN TURNO
    @ApiMethod(
            description = "Finalizar un turno del siga",
            path = "/api/integration/siga/endturn",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/endturn", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> endTurn(
            @ApiBodyObject(clazz = SigaTurnMovement.class) @RequestBody SigaTurnMovement turnmovement
    ) throws Exception
    {
        turnmovement.setState(6);
        return new ResponseEntity<>(integrationSigaService.changeStateTurn(turnmovement), HttpStatus.OK);
    }

    //CANCELAR UN TURNO
    @ApiMethod(
            description = "Cancelar un turno del siga",
            path = "/api/integration/siga/cancelturn",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/cancelturn", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> cancelTurn(
            @ApiBodyObject(clazz = SigaTurnMovement.class) @RequestBody SigaTurnMovement turnmovement
    ) throws Exception
    {
        turnmovement.setState(4);
        return new ResponseEntity<>(integrationSigaService.changeStateTurn(turnmovement), HttpStatus.OK);
    }

    //POSPONER UN TURNO
    @ApiMethod(
            description = "Posponer un turno del siga",
            path = "/api/integration/siga/postponeturn",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/postponeturn", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> postponeTurn(
            @ApiBodyObject(clazz = SigaTurnMovement.class) @RequestBody SigaTurnMovement turnmovement
    ) throws Exception
    {
        turnmovement.setState(5);
        integrationSigaService.changeStateTurn(turnmovement);
        turnmovement.setState(1);
        return new ResponseEntity<>(integrationSigaService.changeStateTurn(turnmovement), HttpStatus.OK);
    }

    //RESERVAR UN TURNO
    @ApiMethod(
            description = "Reservar un turno del siga",
            path = "/api/integration/siga/reserverturn",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/reserverturn", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> reserverTurn(
            @ApiBodyObject(clazz = SigaTurnMovement.class) @RequestBody SigaTurnMovement turnmovement
    ) throws Exception
    {
        turnmovement.setState(7);
        return new ResponseEntity<>(integrationSigaService.changeStateTurn(turnmovement), HttpStatus.OK);
    }

    //SERVICIO PARA OBTENER LOS SERVICIOS A LOS CUALES SE PUEDE TRANSFERIR UN TURNO
    @ApiMethod(
            description = "Servicio para obtener los servicios a los cuales se puede transferir un turno",
            path = "/api/integration/siga/transferservicies/{branch}/{service}/{turn}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SigaTransferByServices.class)
    @RequestMapping(value = "/transferservicies/{branch}/{service}/{turn}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SigaTransferByServices>> transferServicies(
            @ApiPathParam(name = "branch", description = "Id de la sede") @PathVariable("branch") int branch,
            @ApiPathParam(name = "service", description = "Id del servicio") @PathVariable("service") int service,
            @ApiPathParam(name = "turn", description = "Id del turno") @PathVariable("turn") int turn
    ) throws Exception
    {
        List<SigaTransferByServices> list = integrationSigaService.transferServicies(branch, service, turn);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //TRANSFERENCIA DE UN SERVICIO
    @ApiMethod(
            description = "Invoca la transferencia de un servicio de siga",
            path = "/api/integration/siga/transfers",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/transfers", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> transfers(
            @ApiBodyObject(clazz = SigaTransferData.class) @RequestBody SigaTransferData data
    ) throws Exception
    {
        return new ResponseEntity<>(integrationSigaService.transfers(data), HttpStatus.OK);
    }

    //VALIDAR SI UN SERVICIO SE CALIFICA O NO
    @ApiMethod(
            description = "Validar se un servicio se califica o no desde el siga",
            path = "/api/integration/siga/servicerating",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/servicerating", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> serviceRating(
            @ApiBodyObject(clazz = SigaTurnMovement.class) @RequestBody SigaTurnMovement data
    ) throws Exception
    {
        return new ResponseEntity<>(integrationSigaService.serviceRating(data), HttpStatus.OK);
    }

    //---------------- Historial del usuario en taquilla y turno dentro de siga ---------------
    @ApiMethod(
            description = "Obtener historial o log del usuario",
            path = "/api/integration/siga/getUserHistory/userName/{userName}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SigaMainLogUser.class)
    @RequestMapping(value = "/getUserHistory/userName/{userName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SigaMainLogUser> getUserHistory(
            @ApiPathParam(name = "userName", description = "Nombre de usuario") @PathVariable(name = "userName") String userName
    ) throws Exception
    {
        SigaMainLogUser logUser = integrationSigaService.getUserHistory(userName);
        if (logUser == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(logUser, HttpStatus.OK);
        }
    }

    //---------------- Obtenemos todos los puntos de atención ---------------
    @ApiMethod(
            description = "obtener todos los puntos de atención",
            path = "/api/integration/siga/getAllPointsOfCare",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/getAllPointsOfCare", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SigaPointOfCare>> getAllPointsOfCare() throws Exception
    {
        List<SigaPointOfCare> points = integrationSigaService.getAllPointsOfCare();
        if (points == null || points.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(points, HttpStatus.OK);
        }
    }

    //---------------- Lista de ordenes ---------------
    @ApiMethod(
            description = "Lista ordenes filtradas",
            path = "/api/integration/siga/orders",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Order.class)
    @RequestMapping(value = "/orders", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Long>> getOrders(
            @ApiBodyObject(clazz = SigaFilterOrders.class) @RequestBody SigaFilterOrders filter
    ) throws Exception
    {
        List<Long> orders = integrationSigaService.getOrdersByDate(filter);

        if (orders == null || orders.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
    }

    //VALIDAR SI UN SERVICIO SE CALIFICA O NO
    @ApiMethod(
            description = "Servicio para asociar el turno a una orden",
            path = "/api/integration/siga/turnorder",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/turnorder", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> serviceturnorder(
            @ApiBodyObject(clazz = SigaTurnMovement.class) @RequestBody SigaTurnMovement data
    ) throws Exception
    {
        return new ResponseEntity<>(integrationSigaService.serviceTurnOrder(data), HttpStatus.OK);
    }
}
