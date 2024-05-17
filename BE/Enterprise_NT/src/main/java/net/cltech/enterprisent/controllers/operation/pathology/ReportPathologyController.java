/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.operation.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.operation.pathology.FilterBarcodePathology;
import net.cltech.enterprisent.domain.operation.pathology.BarcodeLog;
import net.cltech.enterprisent.service.interfaces.operation.pathology.ReportPathologyService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
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
 * Controlador de servicios Rest sobre los reportes de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 10/05/2021
 * @see Creacion
 */
@Api(
        name = "Patologia - Informes",
        group = "Patología",
        description = "Servicios Rest sobre informes"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/pathology/reports")

public class ReportPathologyController 
{
    @Autowired
    private ReportPathologyService reportService;
    
    //------------ IMPRESION DE CODIGO DE BARRAS ----------------
    @ApiMethod(
            description = "Imprimir codigo de barras del caso",
            path = "/api/pathology/reports/printingbybarcode",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = BarcodeLog.class)
    @RequestMapping(value = "/printingbybarcode", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BarcodeLog>> printingByBarcode(
            @ApiBodyObject(clazz = FilterBarcodePathology.class) @RequestBody FilterBarcodePathology filter
    ) throws Exception
    {
        return new ResponseEntity<>(reportService.printingByBarcode(filter), HttpStatus.OK);
    }
}
