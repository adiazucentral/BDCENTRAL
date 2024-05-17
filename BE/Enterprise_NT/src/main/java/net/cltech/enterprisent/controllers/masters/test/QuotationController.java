/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.test;

import java.util.Date;
import java.util.List;
import net.cltech.enterprisent.domain.masters.test.QuotationHeader;
import net.cltech.enterprisent.service.interfaces.masters.test.QuotationService;
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
 * Servicios REST para la Cotizacion
 *
 * @version 1.0.0
 * @author jrodriguez
 * @since 31/10/2018
 * @see Creacion
 */
@Api(
        group = "Prueba",
        name = "Cotización",
        description = "Servicios sobre la cotizacion de examenes"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/quotation")
public class QuotationController
{

    @Autowired
    private QuotationService quotationService;

    //------------ GUARDA LAS COTIZACIONES  ----------------
    @ApiMethod(
            description = "Guarda las cotizaciones",
            path = "/api/quotation/quotation",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 304 - NOT MODIFIED, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Void.class)
    @RequestMapping(value = "/quotation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> insertQuotations(
            @ApiBodyObject(clazz = QuotationHeader.class) @RequestBody QuotationHeader quotationHeader) throws Exception
    {
        quotationService.insertQuotations(quotationHeader);
        return new ResponseEntity(HttpStatus.OK);

    }

    // CONSULTA LAS COTIZACIONES REGISTRADAS POR NOMBRE
    @ApiMethod(
            description = "Obtiene cotizacion por nombre del paciente",
            path = "/api/quotation/quotation/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT, 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = QuotationHeader.class)
    @RequestMapping(value = "/quotation/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuotationHeader>> listQuotationHeader(
            @ApiPathParam(name = "name", description = "Nombre completo") @PathVariable("name") String name
    ) throws Exception
    {
        List<QuotationHeader> list = quotationService.getPatientBy(name);
        if (list != null)
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        } else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    // CONSULTA LAS COTIZACIONES REGISTRADAS POR UN RANGO DE FECHAS. 
    @ApiMethod(
            description = "Obtiene la información de una cotizacion en un rango de fechas.",
            path = "/api/quotation/quotation/init/{init}/end/{end}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = QuotationHeader.class)
    @RequestMapping(value = "/quotation/init/{init}/end/{end}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<QuotationHeader>> listQuotationHeader(
            @ApiPathParam(name = "init", description = "Fecha Inicial") @PathVariable(name = "init") long init,
            @ApiPathParam(name = "end", description = "Fecha Final") @PathVariable(name = "end") long end
    ) throws Exception
    {
        List<QuotationHeader> list = quotationService.listQuotationHeader(new Date(init), new Date(end));
        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

}
