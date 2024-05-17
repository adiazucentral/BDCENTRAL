/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.operation.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.operation.pathology.CaseSearch;
import net.cltech.enterprisent.domain.operation.pathology.Case;
import net.cltech.enterprisent.domain.operation.pathology.FilterPathology;
import net.cltech.enterprisent.service.interfaces.operation.pathology.CaseService;
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
 * Servicios para el acceso a la información de los casos de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 23/02/2021
 * @see Creacion
 */
@Api(
    name = "Casos",
    group = "Patología",
    description = "Servicios para el acceso a la información de los casos de patologia"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/pathology/case")
public class CaseController 
{
    @Autowired
    private CaseService caseService;
    
    //------------ CONSULTA POR ID ----------------
    @ApiMethod(
            description = "Obtiene un caso de patologia por id",
            path = "/api/pathology/case/filter/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Case.class)
    @RequestMapping(value = "filter/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Case> getById(
            @ApiPathParam(name = "id", description = "Id del caso") @PathVariable(name = "id") Integer id
    ) throws Exception
    {
        Case casePat = caseService.get(id, null, null, null);
        if (casePat == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(casePat, HttpStatus.OK);
        }
    }
    
    //------------ CONSULTA POR TIPO DE ESTUDIO Y NUMERO DE CASO ----------------
    @ApiMethod(
            description = "Obtiene un caso de patologia por tipo de estudio y numero de caso",
            path = "/api/pathology/case/filter/studytype/{studytype}/number/{number}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Case.class)
    @RequestMapping(value = "filter/studytype/{studytype}/number/{number}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Case> getByNumber(
            @ApiPathParam(name = "studytype", description = "Id del tipo de estudio") @PathVariable(name = "studytype") Integer studytype,
            @ApiPathParam(name = "number", description = "Numero del caso") @PathVariable(name = "number") Long number
    ) throws Exception
    {
        Case casePat = caseService.get(null, studytype, number, null);
        if (casePat == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(casePat, HttpStatus.OK);
        }
    }
    
    //------------ CONSULTA POR TIPO DE ESTUDIO Y NUMERO DE ORDEN ----------------
    @ApiMethod(
            description = "Obtiene un caso de patologia por tipo de estudio y numero de orden",
            path = "/api/pathology/case/filter/studytype/{studytype}/order/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Case.class)
    @RequestMapping(value = "filter/studytype/{studytype}/order/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Case> getByOrder(
            @ApiPathParam(name = "studytype", description = "Id del tipo de estudio") @PathVariable(name = "studytype") Integer studytype,
            @ApiPathParam(name = "order", description = "Numero de orden") @PathVariable(name = "order") Long order
    ) throws Exception
    {
        Case casePat = caseService.get(null, studytype, null, order);
        if (casePat == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(casePat, HttpStatus.OK);
        }
    }
    
    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea un nuevo caso de patologia",
            path = "/api/pathology/case",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Case.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Case> create(
            @ApiBodyObject(clazz = Case.class) @RequestBody Case casePat
    ) throws Exception
    {
        return new ResponseEntity<>(caseService.create(casePat), HttpStatus.OK);
    }
    
    //------------ BUSCAR POR FECHA Y SEDE ----------------
    @ApiMethod(
            description = "Busca un caso por fecha de ingreso y sede",
            path = "/api/pathology/case/filter/entryDate/{entryDate}/{branch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CaseSearch.class)
    @RequestMapping(value = "/filter/entryDate/{entryDate}/{branch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CaseSearch>> getByEntryDate(
            @ApiPathParam(name = "entryDate", description = "Fecha de ingreso en formato YYYYMMDD") @PathVariable("entryDate") int entryDate,
            @ApiPathParam(name = "branch", description = "Id Sede") @PathVariable("branch") int branch
    ) throws Exception
    {
        List<CaseSearch> records = caseService.getByEntryDate(entryDate, branch);
        if (!records.isEmpty())
        {
            return new ResponseEntity(records, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
    
    //------------ ACTUALIZA UN CASO DE PATOLOGIA ----------------
    @ApiMethod(
            description = "Actualiza un caso en el sistema",
            path = "/api/pathology/case",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Case.class)
    @RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Case> update(@ApiBodyObject(clazz = Case.class) @RequestBody Case casePat
    ) throws Exception
    {
        return new ResponseEntity<>(caseService.update(casePat), HttpStatus.OK);
    }
    
    //------------ OBTIENE UNA LISTA DE CASOS DE PATOLOGIA POR FILTROS ----------------
    @ApiMethod(
            description = "Obtiene una lista de casos por filtros",
            path = "/api/pathology/case/filters",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = CaseSearch.class)
    @RequestMapping(value = "/filters", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CaseSearch>> getFilterCases(
            @ApiBodyObject(clazz = FilterPathology.class) @RequestBody FilterPathology filter
    ) throws Exception
    {
        List<CaseSearch> list = caseService.getFilterCases(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    //------------ MODIFICAR EL ESTADO DE UN CASO ----------------
    @ApiMethod(
            description = "Modifica el estado de un caso",
            path = "/api/pathology/case/changestatus",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Case.class)
    @RequestMapping(value = "/changestatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Case> changeStatus(@ApiBodyObject(clazz = Case.class) @RequestBody Case casePat
    ) throws Exception
    {        
        caseService.changeStatus(casePat);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
