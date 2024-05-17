/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.controllers.masters.appointment;


import java.util.List;
import net.cltech.enterprisent.domain.masters.appointment.Shift;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.service.interfaces.masters.appointment.ShiftService;
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
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Servicios REST para jornadas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 14/08/2018
 * @see Creacion
 */
@Api(
        group = "Citas",
        name = "Jornada",
        description = "Servicios sobre jornadas"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/shifts")
public class ShiftController
{

    @Autowired
    private ShiftService service;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista las jornadas registradas",
            path = "/api/shifts",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Shift.class)
    @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Shift>> list() throws Exception
    {
        List<Shift> list = service.list();

        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ LISTAR POR ESTADO ----------------
    @ApiMethod(
            description = "Lista las jornadas registrados por estado",
            path = "/api/shifts/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Shift.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Shift>> list(@ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") boolean state) throws Exception
    {
        List<Shift> list = service.list(state);

        if (list.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(list, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR ID ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una jornada",
            path = "/api/shifts/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Shift.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Shift> getById(@ApiPathParam(name = "id", description = "Id de la jornada") @PathVariable(name = "id") int id) throws Exception
    {
        Shift shift = service.get(id, null);

        if (shift == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(shift, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR CODIGO ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una jornada",
            path = "/api/shifts/filter/code/{code}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Shift.class)
    @RequestMapping(value = "/filter/code/{code}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Shift> getByCode(@ApiPathParam(name = "code", description = "Codigo de la jornada") @PathVariable(name = "code") String code) throws Exception
    {
        Shift shift = service.get(null, code);

        if (shift == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(shift, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una jornada",
            path = "/api/shifts/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Shift.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Shift> getByName(@ApiPathParam(name = "name", description = "Nombre de la jornada") @PathVariable(name = "name") String name) throws Exception
    {
        Shift shift = service.get(null, name);

        if (shift == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(shift, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea una nueva jornada",
            path = "/api/shifts",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Shift.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Shift> create(@ApiBodyObject(clazz = Shift.class) @RequestBody Shift shift, UriComponentsBuilder ucBuilder) throws Exception
    {
        Shift created = service.create(shift);

        return new ResponseEntity<>(created, HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar una jornada",
            path = "/api/shifts",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Shift.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Shift> update(@ApiBodyObject(clazz = Shift.class) @RequestBody Shift shift, UriComponentsBuilder ucBuilder) throws Exception
    {
        Shift updated = service.update(shift);

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
    
        //------------ ACTUALIZAR JORNADAS ----------------
    @ApiMethod(
            description = "Actualiza la información de jornadas - sedes",
            path = "/api/shifts/branch",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Branch.class)
    @RequestMapping(value = "/branch", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertShifts(@ApiBodyObject(clazz = Branch.class) @RequestBody Branch branch, UriComponentsBuilder ucBuilder) throws Exception
    {
        return new ResponseEntity<>(service.insertShiftsbyBranch(branch), HttpStatus.OK);
    }
    
        //------------ CONSULTA POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene la informacion de una jornada por sede",
            path = "/api/shifts/filter/branch/{idBranch}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Shift.class)
    @RequestMapping(value = "/filter/branch/{idBranch}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Shift>> listShiftsbyBranch(@ApiPathParam(name = "idBranch", description = "id de la sede") @PathVariable(name = "idBranch") Integer idBranch) throws Exception
    {
        List<Shift> shift = service.listShiftsbyBranch(idBranch);

        if (shift.isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(shift, HttpStatus.OK);
        }
    }

}