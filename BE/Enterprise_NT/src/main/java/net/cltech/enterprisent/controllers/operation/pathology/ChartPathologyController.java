/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.operation.pathology;

import java.util.List;
import net.cltech.enterprisent.domain.operation.pathology.ChartPathology;
import net.cltech.enterprisent.service.interfaces.operation.pathology.ChartPathologyService;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiAuthToken;
import org.jsondoc.core.annotation.ApiMethod;
import org.jsondoc.core.annotation.ApiResponseObject;
import org.jsondoc.core.annotation.ApiVersion;
import org.jsondoc.core.pojo.ApiVerb;
import org.jsondoc.core.pojo.ApiVisibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Servicios para el acceso a la información de las graficas utilizadas en patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 22/07/2021
 * @see Creacion
 */
@Api(
    name = "Graficas",
    group = "Patología",
    description = "Servicios para el acceso a la información de las graficas de patologia"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/pathology/chart")

public class ChartPathologyController 
{
    @Autowired
    private ChartPathologyService chartPathologyService;
    
    
    //------------ PROCESADOR DE TEJIDOS ----------------
    @ApiMethod(
            description = "Datos para las graficas del procesador de tejidos",
            path = "/api/pathology/chart/tissueprocessor",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = ChartPathology.class)
    @RequestMapping(value = "/tissueprocessor", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ChartPathology>> getTissueProcessor() throws Exception
    {
        List<ChartPathology> chart = chartPathologyService.getChartTissueProcessor();
        if (!chart.isEmpty())
        {
            return new ResponseEntity(chart, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
    }
}
