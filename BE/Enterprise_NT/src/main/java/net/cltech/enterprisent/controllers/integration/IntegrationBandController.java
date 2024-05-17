package net.cltech.enterprisent.controllers.integration;

import java.util.List;
import net.cltech.enterprisent.domain.integration.band.BandSample;
import net.cltech.enterprisent.domain.integration.band.BandSampleCheck;
import net.cltech.enterprisent.domain.integration.band.BandVerifiedSample;
import net.cltech.enterprisent.domain.integration.statusBandReason.StatusBandReason;
import net.cltech.enterprisent.domain.integration.statusBandReason.StatusBandReasonUser;
import net.cltech.enterprisent.domain.masters.common.Motive;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationBandService;
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
 * Controlador de servicios Rest sobre la integracion de la banda transportadora
 *
 * @version 1.0.0
 * @author javila
 * @since 22/05/2020
 * @see Creación
 */
@Api(
        name = "Integración Banda Transportadora",
        group = "Integración",
        description = "Servicios Rest de la integración de NT con la banda transportadora"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/band")
public class IntegrationBandController
{

    @Autowired
    private IntegrationBandService integrationBandService;

    @ApiMethod(
            description = "Verifica la muestra según el id de la orden y el id de la muestra que sean enviados desde el S.I de banda",
            path = "/api/band/sampleCheck",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "sampleCheck", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> bandSampleCheck(
            @ApiBodyObject(clazz = BandSampleCheck.class) @RequestBody BandSampleCheck sampleCheck
    ) throws Exception
    {
        return new ResponseEntity<>(integrationBandService.bandSampleCheck(sampleCheck), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Verifica la muestra en el destino",
            path = "/api/band/verififyDestination",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/verififyDestination", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> bandVerififyDestination(
            @ApiBodyObject(clazz = BandSampleCheck.class) @RequestBody BandSampleCheck sampleCheck
    ) throws Exception
    {
        return new ResponseEntity<>(integrationBandService.bandVerifyDestination(sampleCheck), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Obtiene un listado con las muestras verificadas el dia actual (HOY)",
            path = "/api/band/getVerifiedSamples/idDestination/{idDestination}/{idBranch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = BandVerifiedSample.class)
    @RequestMapping(value = "/getVerifiedSamples/idDestination/{idDestination}/{idBranch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BandVerifiedSample>> listVerifiedSamples(
            @ApiPathParam(name = "idDestination", description = "Id del destino") @PathVariable("idDestination") int idDestination,
            @ApiPathParam(name = "idBranch", description = "Id de la sede") @PathVariable("idBranch") int idBranch
    ) throws Exception
    {
        List<BandVerifiedSample> verifiedSamples = integrationBandService.listVerifiedSamples(idDestination,idBranch);
        if (verifiedSamples != null && !verifiedSamples.isEmpty())
        {
            return new ResponseEntity<>(verifiedSamples, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene un listado con las muestras no verificadas el dia actual (HOY)",
            path = "/api/band/getUnverifiedSamples/idDestination/{idDestination}/{idBranch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = BandVerifiedSample.class)
    @RequestMapping(value = "/getUnverifiedSamples/idDestination/{idDestination}/{idBranch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BandVerifiedSample>> listUnverifiedSamples(
            @ApiPathParam(name = "idDestination", description = "Id del destino") @PathVariable("idDestination") int idDestination,
            @ApiPathParam(name = "idBranch", description = "Id de la sede") @PathVariable("idBranch") int idBranch
    ) throws Exception
    {
        List<BandVerifiedSample> unverifiedSamples = integrationBandService.listUnverifiedSamples(idDestination, idBranch);
        if (unverifiedSamples != null && !unverifiedSamples.isEmpty())
        {
            return new ResponseEntity<>(unverifiedSamples, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    @ApiMethod(
            description = "Obtiene el lista de las muestras",
            path = "/api/band/samples",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = BandSample.class)
    @RequestMapping(value = "/samples", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BandSample>> samples() throws Exception
    {
        List<BandSample> samples = integrationBandService.listSamples();
        if (samples != null && !samples.isEmpty())
        {
            return new ResponseEntity<>(samples, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    //------------ RECIBE UN JSON DE  PARA CREAR UN NUEVO ESTADO DE BANDA Y MOTIVO ----------------     
    @ApiMethod(
            description = "Registra Estado de Banda y Motivo",
            path = "/api/band/reason",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StatusBandReason.class)
    @RequestMapping(value = "/reason",method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatusBandReason> createStatusBandReason(@ApiBodyObject(clazz = StatusBandReason.class) @RequestBody(required = true) StatusBandReason statusBandReason) throws Exception
    {
        return new ResponseEntity<>(integrationBandService.createStatusBandReason(statusBandReason), HttpStatus.OK);

    }
    
    
    //------------ LISTA LOS ESTADOS DE BANDA Y MOTIVO ----------------  
    
    @ApiMethod(
            description = "lista los estados de banda y motivo",
            path = "/api/band/reason",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
         //   consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = StatusBandReasonUser.class)
    @RequestMapping(value = "/reason", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StatusBandReasonUser>> reason() throws Exception
    {
        List<StatusBandReasonUser> reason = integrationBandService.listReason();
        if (reason != null && !reason.isEmpty())
        {
            return new ResponseEntity<>(reason, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    
    
    //------------ LISTA LOS MOTIVOS DE LA BANDA------------------------
    
    @ApiMethod(
            description = "lista los Motivos de la banda",
            path = "/api/band/reasonBand",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
         //   consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Motive.class)
    @RequestMapping(value = "/reasonBand", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Motive>> reasonBand() throws Exception
    {
        List<Motive> reasonBand = integrationBandService.listReasonBand();
        if (reasonBand != null && !reasonBand.isEmpty())
        {
            return new ResponseEntity<>(reasonBand, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    
}
