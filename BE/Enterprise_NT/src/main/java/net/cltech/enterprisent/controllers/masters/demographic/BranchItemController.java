package net.cltech.enterprisent.controllers.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.BranchDemographic;
import net.cltech.enterprisent.domain.masters.demographic.DemographicBranch;
import net.cltech.enterprisent.domain.masters.demographic.DemographicRequired;
import net.cltech.enterprisent.service.interfaces.masters.demographic.DemographicItemService;
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
 * Servicios para el acceso a la informacion del maestro Usuarios
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 18/05/2020
 * @see Creacion
 *
 * @see Se agregaron metodos para el funcionamiento maestro de sedes y
 * demograficos.
 */
@Api(
        name = "Sedes-Demograficos",
        group = "Demografico",
        description = "Servicios de informacion del maestro de Demograficos"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/branchitem")
public class BranchItemController
{

    @Autowired
    private DemographicItemService demographicItemService;

    //------------ LISTA DE DEMOGRAFICOS ITEM  POR SEDE Y ID DE DEMOGRAFICO----------------
    @ApiMethod(
            description = "Lista de demograficos item por sede y demografico",
            path = "/api/branchitem/itemsdemographic/branch/{branch}/demographic/{demographic}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = BranchDemographic.class)
    @RequestMapping(value = "/itemsdemographic/branch/{branch}/demographic/{demographic}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BranchDemographic>> branchDemographics(
            @ApiPathParam(name = "branch", description = "Sede") @PathVariable(name = "branch") int branch,
            @ApiPathParam(name = "demographic", description = "Demografico") @PathVariable(name = "demographic") int demographic
    ) throws Exception
    {
        List<BranchDemographic> list = demographicItemService.demographicsItemList(branch, demographic);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ GUARDA DE DEMOGRAFICOS ITEM CON SEDE ----------------
    @ApiMethod(
            description = "Guarda item por sede y demografico",
            path = "/api/branchitem/itemsdemographic/saves",
            visibility = ApiVisibility.PUBLIC,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            verb = ApiVerb.POST,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/itemsdemographic/saves", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> branchDemographicsSave(
            @ApiBodyObject(clazz = BranchDemographic.class) @RequestBody List<BranchDemographic> demographics
    ) throws Exception
    {
        demographicItemService.saveDemographicBranch(demographics);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //------------ LISTA DE DEMOGRAFICOS  POR SEDE Y ID DE DEMOGRAFICO----------------
    @ApiMethod(
            description = "Lista de demograficos por sede",
            path = "/api/branchitem/branch/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicBranch.class)
    @RequestMapping(value = "/branch/{branch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DemographicBranch>> demographicBranch(
            @ApiPathParam(name = "branch", description = "Sede") @PathVariable(name = "branch") int branch
    ) throws Exception
    {
        List<DemographicBranch> list = demographicItemService.demographics(branch);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ GUARDA DE DEMOGRAFICOS CON SEDE ----------------
    @ApiMethod(
            description = "Guarda relacion de demograficos por sede",
            path = "/api/branchitem/savesdemographics",
            visibility = ApiVisibility.PUBLIC,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            verb = ApiVerb.POST,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/savesdemographics", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> demographicBranchSave(
            @ApiBodyObject(clazz = DemographicBranch.class) @RequestBody List<DemographicBranch> demographics
    ) throws Exception
    {
        demographicItemService.demographicBranchSave(demographics);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //------------ ACTUALIZA DE DEMOGRAFICOS ----------------
    @ApiMethod(
            description = "Actualiza por el id de Demografico el valor por defecto requerido",
            path = "/api/branchitem/demographics/valuerequired",
            visibility = ApiVisibility.PUBLIC,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            verb = ApiVerb.PUT,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/demographics/valuerequired", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> demographicValueRequired(
            @ApiBodyObject(clazz = DemographicRequired.class) @RequestBody List<DemographicRequired> demographics
    ) throws Exception
    {
        demographicItemService.demographicValueRequired(demographics);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
