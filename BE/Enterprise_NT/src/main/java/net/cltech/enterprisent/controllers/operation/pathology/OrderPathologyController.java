/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.operation.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.pathology.SampleRejection;
import net.cltech.enterprisent.domain.operation.pathology.OrderPathology;
import net.cltech.enterprisent.domain.operation.results.ResultFilter;
import net.cltech.enterprisent.service.interfaces.operation.pathology.OrderPathologyService;
import net.cltech.enterprisent.service.interfaces.operation.pathology.SampleRejectionService;
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
 * Servicios para el acceso a la información de las ordenes de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 06/10/2020
 * @see Creacion
 */
@Api(
    name = "Ordenes de Patología",
    group = "Patología",
    description = "Servicios para el acceso a la información de las ordenes de patologia"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/pathology/order")
public class OrderPathologyController 
{
    @Autowired
    private OrderPathologyService orderPathologyService;
    @Autowired
    private SampleRejectionService sampleRejectionService;
    
    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista las ordenes de patologia registradas",
            path = "/api/pathology/order",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderPathology.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderPathology>> list() throws Exception
    {
        List<OrderPathology> list = orderPathologyService.list();
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    //------------ LISTAR POR FILTROS ----------------
    @ApiMethod(
            description = "Lista las ordenes de patologia filtradas",
            path = "/api/pathology/order/filters",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = OrderPathology.class)
    @RequestMapping(value = "/filters", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OrderPathology>> getByFilters(
            @ApiBodyObject(clazz = ResultFilter.class) @RequestBody ResultFilter filter
    ) throws Exception
    {        
        List<OrderPathology> list = orderPathologyService.listByFilters(filter);
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    //------------ OBTENER LISTA DE ESPECIMENES DE UNA ORDEN ----------------
    @ApiMethod(
            description = "Obtiene la lista de especimenes de una orden.",
            path = "/api/pathology/order/specimens/{order}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Specimen.class)
    @RequestMapping(value = "/specimens/{order}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Specimen>> specimenByOrden(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long idOrder
    ) throws Exception
    {
        List<Specimen> list = orderPathologyService.specimenByOrden(idOrder);        
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }
    
    //------------ OBTENER UN RECHAZO POR TIPO DE ESTUDIO Y ORDEN ----------------
    @ApiMethod(
            description = "Obtiene un rechazo por tipo de estudio y orden.",
            path = "/api/pathology/order/{order}/rejection/stutytype/{studytype}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SampleRejection.class)
    @RequestMapping(value = "/{order}/rejection/stutytype/{studytype}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SampleRejection> getRejection(
            @ApiPathParam(name = "order", description = "Orden") @PathVariable(name = "order") long order,
            @ApiPathParam(name = "studytype", description = "studytype") @PathVariable(name = "studytype") Integer studytype
    ) throws Exception
    {
        SampleRejection rejection = sampleRejectionService.get(studytype, order);       
        if (rejection == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else
        {
            return new ResponseEntity<>(rejection, HttpStatus.OK);
        }
    }
   
    //------------ RECHAZAR ORDEN ----------------
    @ApiMethod(
            description = "Rechaza una orden",
            path = "/api/pathology/order/rejection",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SampleRejection.class)
    @RequestMapping(value = "/rejection", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SampleRejection> create(
            @ApiBodyObject(clazz = SampleRejection.class) @RequestBody SampleRejection rejection
    ) throws Exception
    {
        return new ResponseEntity<>(sampleRejectionService.create(rejection), HttpStatus.OK);
    }
    
    //------------ CONSULTAR LISTA DE MUESTRAS RECHAZADAS----------------
    @ApiMethod(
            description = "Obtiene listado de muestras rechazadas en un rango",
            path = "/api/pathology/order/rejection",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SampleRejection.class)
    @RequestMapping(value = "/rejection", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SampleRejection>> listDeleted(
            @ApiBodyObject(clazz = Filter.class) @RequestBody() Filter filter
    ) throws Exception
    {
        List<SampleRejection> rejectList = sampleRejectionService.listByFilters(filter);
        if (rejectList.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(rejectList, HttpStatus.OK);
        }
    }
    
    //------------ ACTIVAR MUESTRAS RECHAZADAS----------------
    @ApiMethod(
            description = "Activa muestras rechazadas",
            path = "/api/pathology/order/activate",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = SampleRejection.class)
    @RequestMapping(value = "/activate", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SampleRejection>> activate(
            @ApiBodyObject(clazz = SampleRejection.class) @RequestBody List<SampleRejection> samples
    ) throws Exception
    {
        List<SampleRejection> activated = sampleRejectionService.activeSamples(samples);
        if (!activated.isEmpty())
        {
            return new ResponseEntity<>(activated, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
