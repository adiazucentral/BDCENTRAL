package net.cltech.enterprisent.controllers.masters.test;

import java.util.List;
import net.cltech.enterprisent.domain.masters.test.LaboratoryByBranch;
import net.cltech.enterprisent.domain.masters.test.LaboratorysByBranch;
import net.cltech.enterprisent.service.interfaces.masters.test.LaboratorysByBranchesService;
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
 * Implementacion para saber los laboratorios de una sede.
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 31/01/2019
 * @see Creacion
 */
@Api(
        name = "Laboratorios por sede",
        group = "Prueba",
        description = "Servicios sobre los Diagnosticos"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/branches")
public class LaboratorysByBranchesController
{

    @Autowired
    private LaboratorysByBranchesService laboratorysByBranchesService;

    //------------ LISTAR LABORATORIOS DE UNA SEDE ---------------- 
    @ApiMethod(
            description = "Para la importación de muestras al middleware",
            path = "/api/branches/laboratories/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = LaboratoryByBranch.class)
    @RequestMapping(value = "/laboratories/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LaboratoryByBranch>> list(
            @ApiPathParam(name = "id", description = "Id del diagnostico") @PathVariable(name = "id") Integer id
    ) throws Exception
    {
        List<LaboratoryByBranch> list = laboratorysByBranchesService.listLaboratorysByBranches(id);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);

        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ ASIGNACION DE LABORATORIOS A UNA SEDE ----------------
    @ApiMethod(
            description = "Actualiza asignacion de laboratorios a una sede",
            path = "/api/branches/laboratories",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/laboratories", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> assignLaboratoriesByBranch(
            @ApiBodyObject(clazz = LaboratorysByBranch.class) @RequestBody(required = true) LaboratorysByBranch update
    ) throws Exception
    {
        return new ResponseEntity<>(laboratorysByBranchesService.assignLaboratoriesByBranch(update), HttpStatus.OK);
    }

}
