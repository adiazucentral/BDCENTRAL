package net.cltech.enterprisent.controllers.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.DemographicFather;
import net.cltech.enterprisent.domain.masters.demographic.DemographicFatherSons;
import net.cltech.enterprisent.domain.masters.demographic.ItemDemographicSon;
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
 * Servicios para el acceso a la informacion del item demografico hijo
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 07/07/2020
 * @see Creacion
 */
@Api(
        name = "DemograficoItemSon",
        group = "Demografico",
        description = "Servicios para el acceso a la informacion del item demografico hijo"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/demographicsitemssons")
public class DemographicItemSonController
{

    @Autowired
    private DemographicItemService demographicItemService;

    //------------ LISTA DE ITEM DEMOGRAFICOS HIJOS ----------------
    @ApiMethod(
            description = "Lista los demograficos items hijos",
            path = "/api/demographicsitemssons/idfather/{idfather}/idfatheritem/{idfatheritem}/idson/{idson}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ItemDemographicSon.class)
    @RequestMapping(value = "/idfather/{idfather}/idfatheritem/{idfatheritem}/idson/{idson}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ItemDemographicSon>> listSons(
            @ApiPathParam(name = "idfather", description = "Id del demografico padre") @PathVariable(name = "idfather") int idfather,
            @ApiPathParam(name = "idfatheritem", description = "Id del demograficos item del padre") @PathVariable(name = "idfatheritem") int idfatheritem,
            @ApiPathParam(name = "idson", description = "Id del demografico del hijo") @PathVariable(name = "idson") int idson
    ) throws Exception

    {
        List<ItemDemographicSon> listSons = demographicItemService.listSons(idfather, idfatheritem, idson);
        if (listSons.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(listSons, HttpStatus.OK);
        }
    }

    //------------ ACTUALIZAR EL DEMOGAFICO PADRE CON SUS HIJOS----------------
    @ApiMethod(
            description = "Actualizar el demografico Padre y sus hijos",
            path = "/api/demographicsitemssons",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicFather.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemographicFather> updateDemographicFather(
            @ApiBodyObject(clazz = DemographicFather.class) @RequestBody DemographicFather demographicFather
    ) throws Exception
    {
        return new ResponseEntity<>(demographicItemService.updateDemographicFather(demographicFather), HttpStatus.OK);
    }

    //------------ LISTA DE ITEM DEMOGRAFICOS HIJOS ----------------
    @ApiMethod(
            description = "Lista los demograficos items hijos con solo dos parametros Demografico Padre y su item",
            path = "/api/demographicsitemssons/idfather/{idfather}/idfatheritem/{idfatheritem}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = DemographicFatherSons.class)
    @RequestMapping(value = "/idfather/{idfather}/idfatheritem/{idfatheritem}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DemographicFatherSons> listSon(
            @ApiPathParam(name = "idfather", description = "Id del demografico padre") @PathVariable(name = "idfather") int idfather,
            @ApiPathParam(name = "idfatheritem", description = "Id del demograficos item del padre") @PathVariable(name = "idfatheritem") int idfatheritem
    ) throws Exception

    {
            DemographicFatherSons listSon = demographicItemService.listSon(idfather, idfatheritem);
        {
            return new ResponseEntity<>(listSon, HttpStatus.OK);
        }
    }

    //------------ RETORANA EL ID DEMOGRAFICO HIJO ASOCIADO ----------------
    @ApiMethod(
            description = "Retorna el id demografico hijo asociado",
            path = "/api/demographicsitemssons/idfather/{idfather}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/idfather/{idfather}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> getIdDemographicSon(
            @ApiPathParam(name = "idfather", description = "Id del demografico padre") @PathVariable(name = "idfather") int idfather
    ) throws Exception
    {
        Integer idDemographicSon = demographicItemService.getIdDemographicSon(idfather);
        {
            return new ResponseEntity<>(idDemographicSon, HttpStatus.OK);
        }
    }
}
