package net.cltech.enterprisent.controllers.integration;

import net.cltech.enterprisent.domain.integration.his.BranchHis;
import net.cltech.enterprisent.domain.integration.his.BranchHisState;

import net.cltech.enterprisent.domain.integration.his.UserHis;
import net.cltech.enterprisent.domain.integration.his.UserStatus;
import net.cltech.enterprisent.service.interfaces.integration.IntegrationHisService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiBodyObject;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.annotation.ApiVersion;
import org.jsondoc.core.pojo.ApiVerb;
import org.jsondoc.core.pojo.ApiVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de servicios rest sobre la integracion con el his
 *
 * @version 1.0.0
 * @author omendez
 * @since 01/02/2021
 * @see Creación
 */
@Api(
        name = "His",
        group = "Integración",
        description = "Servicios rest de la integración de NT con el his"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/his")
public class IntegrationHisController
{

    @Autowired
    private IntegrationHisService integrationHisService;

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea o actualiza un nuevo usuario",
            path = "/api/his/usuario",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT."
    )
    @ApiResponseObject(clazz = UserHis.class)
    @RequestMapping(value = "/usuario", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserHis> create(
            @ApiBodyObject(clazz = UserHis.class) @RequestBody UserHis user
    ) throws Exception
    {
        return new ResponseEntity<>(integrationHisService.create(user), HttpStatus.OK);
    }

    //------------ CAMBIAR ESTADO ----------------
    @ApiMethod(
            description = "Cambiar estado de un usuario",
            path = "/api/his/usuario",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT"
    )
    @ApiResponseObject(clazz = UserHis.class)
    @RequestMapping(value = "/usuario", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserStatus> changeState(
            @ApiBodyObject(clazz = UserStatus.class) @RequestBody UserStatus user
    ) throws Exception
    {
        return new ResponseEntity<>(integrationHisService.changeStateUser(user), HttpStatus.OK);
    }

    //------------ Creación de la sede desde los servicios HIS ----------------
    @ApiMethod(
            description = "Crea o actualiza una nueva sede",
            path = "/api/his/sede",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT"
    )
    @ApiResponseObject(clazz = BranchHis.class)
    @RequestMapping(value = "/sede", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BranchHis> createBranch(
            @ApiBodyObject(clazz = BranchHis.class) @RequestBody BranchHis branchhis
    ) throws Exception
    {
        BranchHis createdBranch = integrationHisService.createBranch(branchhis);
        if (createdBranch == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else
        {
            return new ResponseEntity<>(createdBranch, HttpStatus.OK);
        }
    }

    //------------ Actualiza una sede desde los servicios HIS ----------------
    @ApiMethod(
            description = "Actualiza el estado de una sede",
            path = "/api/his/sede",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT"
    )
    @ApiResponseObject(clazz = BranchHisState.class)
    @RequestMapping(value = "/sede", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BranchHisState> updateBranch(
            @ApiBodyObject(clazz = BranchHisState.class) @RequestBody BranchHisState branchstate
    ) throws Exception
    {
        BranchHisState updateBranch = integrationHisService.updateBranch(branchstate);
        if (updateBranch == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } else
        {
            return new ResponseEntity<>(updateBranch, HttpStatus.OK);
        }
    }

  
}
