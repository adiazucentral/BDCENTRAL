/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.tools;

import net.cltech.enterprisent.domain.tools.BarcodeDesigner;
import net.cltech.enterprisent.service.interfaces.tools.UpdateDateHistoricsService;
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
 *
 * @author hpoveda
 */
@Api(
        name = "UpdateDateHistorics",
        group = "Herramientas",
        description = "Servicios actualiza las fechas de historicos cuando no aparecen"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/UpdateDateHistorics")
public class UpdateDateHistoricsController
{

    @Autowired
    public UpdateDateHistoricsService service;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "actualiza las fechas de historicos cuando no aparecen",
            path = "/api/UpdateDateHistorics",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = BarcodeDesigner.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateDateHistorics() throws Exception
    {
        return new ResponseEntity<>(service.updateHistorics(), HttpStatus.OK);
    }

}
