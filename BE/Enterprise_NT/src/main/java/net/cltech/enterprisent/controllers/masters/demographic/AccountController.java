package net.cltech.enterprisent.controllers.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.demographic.RatesOfAccount;
import net.cltech.enterprisent.service.interfaces.masters.demographic.AccountService;
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
 * Servicios para el acceso a la informacion del maestro Clientes
 *
 * @version 1.0.0
 * @author cmartin
 * @since 12/04/2017
 * @see Creacion
 */
@Api(
        name = "Cliente",
        group = "Demografico",
        description = "Servicios de informacion del maestro Clientes"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/accounts")
public class AccountController
{

    @Autowired
    private AccountService accountService;

    //------------ LISTAR ----------------
    @ApiMethod(
            description = "Lista los clientes registrados",
            path = "/api/accounts",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Account.class)
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Account>> list() throws Exception
    {
        List<Account> list = accountService.list();
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
            description = "Lista los clientes registrados por estado",
            path = "/api/accounts/filter/state/{state}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Account.class)
    @RequestMapping(value = "/filter/state/{state}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Account>> list(
            @ApiPathParam(name = "state", description = "Estado") @PathVariable(name = "state") boolean state
    ) throws Exception
    {
        List<Account> list = accountService.list(state);
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
            description = "Obtiene la informacion de un cliente",
            path = "/api/accounts/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Account.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getById(
            @ApiPathParam(name = "id", description = "Id del cliente") @PathVariable(name = "id") int id
    ) throws Exception
    {
        Account account = accountService.get(id, null, null, null, null);
        if (account == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(account, HttpStatus.OK);
        }
    }
    //------------ CONSULTA POR NOMBRE ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un cliente",
            path = "/api/accounts/filter/name/{name}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Account.class)
    @RequestMapping(value = "/filter/name/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getByName(
            @ApiPathParam(name = "name", description = "Nombre del cliente") @PathVariable(name = "name") String name
    ) throws Exception
    {
        Account account = accountService.get(null, name, null, null, null);
        if (account == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(account, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR USUARIO ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un cliente",
            path = "/api/accounts/filter/username/{username}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Account.class)
    @RequestMapping(value = "/filter/username/{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getByUsername(
            @ApiPathParam(name = "username", description = "Usuario del cliente") @PathVariable(name = "username") String username
    ) throws Exception
    {
        Account account = accountService.get(null, null, null, null, username);
        if (account == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(account, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR NIT ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un cliente",
            path = "/api/accounts/filter/nit/{nit}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Account.class)
    @RequestMapping(value = "/filter/nit/{nit}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getByNit(
            @ApiPathParam(name = "nit", description = "Nit del cliente") @PathVariable(name = "nit") String nit
    ) throws Exception
    {
        Account account = accountService.get(null, null, nit, null, null);
        if (account == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(account, HttpStatus.OK);
        }
    }

    //------------ CONSULTA POR CODIGO EPS ----------------
    @ApiMethod(
            description = "Obtiene la informacion de un cliente",
            path = "/api/accounts/filter/codeeps/{codeeps}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Account.class)
    @RequestMapping(value = "/filter/codeeps/{codeeps}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> getByCodeEps(
            @ApiPathParam(name = "codeeps", description = "Codigo EPS del cliente") @PathVariable(name = "codeeps") String codeEps
    ) throws Exception
    {
        Account account = accountService.get(null, null, null, codeEps, null);
        if (account == null)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(account, HttpStatus.OK);
        }
    }

    //------------ CREAR ----------------
    @ApiMethod(
            description = "Crea un nuevo cliente",
            path = "/api/accounts",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Account.class)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> create(
            @ApiBodyObject(clazz = Account.class) @RequestBody Account account
    ) throws Exception
    {
        return new ResponseEntity<>(accountService.create(account), HttpStatus.OK);
    }

    //------------ ACTUALIZAR ----------------
    @ApiMethod(
            description = "Actualizar un cliente",
            path = "/api/accounts",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Account.class)
    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Account> update(
            @ApiBodyObject(clazz = Account.class) @RequestBody Account account
    ) throws Exception
    {
        return new ResponseEntity<>(accountService.update(account), HttpStatus.OK);
    }

    //------------ TARIFAS POR CLIENTE -> CONSULTA POR ID ----------------
    @ApiMethod(
            description = "Obtiene las tarifas asociadas el cliente",
            path = "/api/accounts/ratesofaccount/{idaccount}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = RatesOfAccount.class)
    @RequestMapping(value = "/ratesofaccount/{idaccount}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RatesOfAccount>> getRates(
            @ApiPathParam(name = "idaccount", description = "Id del cliente") @PathVariable(name = "idaccount") int idaccount
    ) throws Exception
    {
        List<RatesOfAccount> RatesOfAccounts = accountService.getRates(idaccount);
        if (RatesOfAccounts.size() < 1)
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
        {
            return new ResponseEntity<>(RatesOfAccounts, HttpStatus.OK);
        }
    }

    //------------ CREAR TARIFAS POR CLIENTE ----------------
    @ApiMethod(
            description = "Asocia las tarifas al cliente",
            path = "/api/accounts/ratesofaccount",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = RatesOfAccount.class)
    @RequestMapping(value = "/ratesofaccount", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> createRates(
            @ApiBodyObject(clazz = RatesOfAccount.class) @RequestBody List<RatesOfAccount> ratesOfAccounts
    ) throws Exception
    {
        return new ResponseEntity<>(accountService.insertRates(ratesOfAccounts), HttpStatus.OK);
    }

    //------------ ELIMINA TARIFAS POR CLIENTE ----------------
    @ApiMethod(
            description = "Elimina las tarifas asociadas al cliente",
            path = "/api/accounts/ratesofaccount/{idaccount}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.DELETE,
            produces = MediaType.TEXT_PLAIN_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Integer.class)
    @RequestMapping(value = "/ratesofaccount/{idaccount}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> deleteRatesOfAccount(
            @ApiPathParam(name = "idaccount", description = "Id del cliente") @PathVariable(name = "idaccount") Integer idaccount
    ) throws Exception
    {
        return new ResponseEntity<>(accountService.deleteRates(idaccount), HttpStatus.OK);
    }

}
