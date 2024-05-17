/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.controllers.exceptions;

import java.sql.SQLException;
import java.util.List;
import net.cltech.outreach.domain.exception.WebException;
import net.cltech.outreach.service.interfaces.exception.ExceptionService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Servicios RESTFul para la gestion de errores
 *
 * @version 1.0.0
 * @author dcortes
 * @since 25/04/2017
 * @see Creacion
 */
@Api(
        name = "Gestion de Errores",
        group = "General",
        description = "Servicios para gestion de errores"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/exceptions")
public class ExceptionController
{

    @Autowired
    private ExceptionService service;

    @ApiMethod(
            description = "Obtiene los errores en un rango de fecha",
            path = "/api/exceptions/filter/date/{initial}/{final}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = WebException.class)
    @RequestMapping(value = "/filter/date/{initial}/{final}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WebException>> get(
            @ApiPathParam(name = "initial", description = "Fecha inicial en formato yyyyMMdd") @PathVariable("initial") int initialDate,
            @ApiPathParam(name = "final", description = "Fecha final en formato yyyyMMdd") @PathVariable("final") int finalDate
    ) throws Exception
    {
        List<WebException> records = service.get(initialDate, finalDate);
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Obtiene los errores en un rango de fecha y por tipo",
            path = "/api/exceptions/filter/date/{initial}/{final}/type/{type}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = WebException.class)
    @RequestMapping(value = "/filter/date/{initial}/{final}/type/{type}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WebException>> get(
            @ApiPathParam(name = "initial", description = "Fecha inicial en formato yyyyMMdd") @PathVariable("initial") int initialDate,
            @ApiPathParam(name = "final", description = "Fecha final en formato yyyyMMdd") @PathVariable("final") int finalDate,
            @ApiPathParam(name = "type", description = "Tipo de error que se quiere consultar: 0->Base de datos, 1->No controlados") @PathVariable("type") int type
    ) throws Exception
    {
        List<WebException> records = service.get(initialDate, finalDate, type);
        if (records != null && !records.isEmpty())
        {
            return new ResponseEntity<>(records, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @ApiMethod(
            description = "Genera un error SQL, solo se usa para pruebas internas",
            path = "/api/exceptions/test/sql/exception",
            visibility = ApiVisibility.PRIVATE,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "500 - SERVER_INTERNAL_ERROR"
    )
    @ApiAuthToken(scheme = "JWT")
    @RequestMapping(value = "/test/sql/exception", method = RequestMethod.POST)
    public void generateSQLException() throws Exception
    {
        throw new SQLException("Error en base de datos");
    }

    @ApiMethod(
            description = "Genera un error SQL, solo se usa para pruebas internas",
            path = "/api/exceptions/test/not_control/exception",
            visibility = ApiVisibility.PRIVATE,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "500 - SERVER_INTERNAL_ERROR"
    )
    @ApiAuthToken(scheme = "JWT")
    @RequestMapping(value = "/test/not_control/exception", method = RequestMethod.POST)
    public void generateException() throws Exception
    {
        throw new NullPointerException("Error no controlado");
    }
}
