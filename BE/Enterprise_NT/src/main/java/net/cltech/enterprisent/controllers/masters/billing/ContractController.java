package net.cltech.enterprisent.controllers.masters.billing;

import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.ActivationValidator;
import net.cltech.enterprisent.domain.masters.billing.Contract;
import net.cltech.enterprisent.domain.masters.billing.CustomerContractFilter;
import net.cltech.enterprisent.domain.masters.billing.RatesByContract;
import net.cltech.enterprisent.domain.masters.demographic.RatesOfAccount;
import net.cltech.enterprisent.service.interfaces.masters.billing.ContractService;
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
 * Servicios de información del maestro contratos
 *
 * @version 1.0.0
 * @author jbarbosa
 * @since 30/06/2021
 * @see Creación
 */
@Api(
        name = "Contratos",
        group = "Facturacion",
        description = "Servicios de informacion del maestro contratos"
)
@ApiVersion(since = "1.0.0")
@RestController
@RequestMapping("/api/contracts")
public class ContractController
{

    @Autowired
    private ContractService contractService;

    @ApiMethod(
            description = "Obtiene la informacion de un contrato",
            path = "/api/contracts/getContract/id/{id}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Contract.class)
    @RequestMapping(value = "/getContract/id/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Contract> getById(
            @ApiPathParam(name = "id", description = "Id del contrato") @PathVariable(name = "id") int id
    ) throws Exception
    {
        Contract contract = contractService.getContract(id);
        if (contract == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else
        {
            return new ResponseEntity<>(contract, HttpStatus.OK);
        }
    }

    @ApiMethod(
            description = "Crea un nuevo contrato",
            path = "/api/contracts/newcontract",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Contract.class)
    @RequestMapping(value = "/newcontract", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Contract> createContract(
            @ApiBodyObject(clazz = Contract.class) @RequestBody Contract contract
    ) throws Exception
    {
        return new ResponseEntity<>(contractService.createContract(contract), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Actualiza la inforacion de un contrato",
            path = "/api/contracts/updcontract",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = Contract.class)
    @RequestMapping(value = "/updcontract", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Contract> updateContract(
            @ApiBodyObject(clazz = Contract.class) @RequestBody Contract contract
    ) throws Exception
    {
        return new ResponseEntity<>(contractService.updateContract(contract), HttpStatus.OK);
    }

    @ApiMethod(
            description = "Consulta los contratos por el id del cliente",
            path = "/api/contracts/idCustomer/{idCustomer}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/idCustomer/{idCustomer}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Contract>> getContractByCustomerId(
            @ApiPathParam(name = "idCustomer", description = "Id del cliente") @PathVariable("idCustomer") int idCustomer
    ) throws Exception
    {
        List<Contract> contractList = contractService.getContractByCustomerId(idCustomer);
        if(contractList == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if(!contractList.isEmpty())
        {
            return new ResponseEntity<>(contractList, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    @ApiMethod(
            description = "Consulta las tarifas por el id del cliente y que tarifas se han asignado a ciertos contratos",
            path = "/api/contracts/getRatesByCustomerId/customerId/{customerId}",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/getRatesByCustomerId/customerId/{customerId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RatesOfAccount>> getRatesByCustomerId(
            @ApiPathParam(name = "customerId", description = "Id del cliente") @PathVariable("customerId") int customerId
    ) throws Exception
    {
        List<RatesOfAccount> listRates = contractService.getRatesByContractId(customerId);
        if(listRates == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if(!listRates.isEmpty())
        {
            return new ResponseEntity<>(listRates, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    @ApiMethod(
            description = "Consulta los contratos que participan al momento de generar una factura para un cliente",
            path = "/api/contracts/getContractsForTheAssociatedRates",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/getContractsForTheAssociatedRates", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Contract>> getContractsForTheAssociatedRates(
            @ApiBodyObject(clazz = CustomerContractFilter.class) @RequestBody CustomerContractFilter filter
    ) throws Exception
    {
        List<Contract> contracts = contractService.getContractsForTheAssociatedRates(filter);
        if(contracts == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if(!contracts.isEmpty())
        {
            return new ResponseEntity<>(contracts, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    
    @ApiMethod(
            description = "Valida si un contrato puede ser desactivado o si ya existe un contrato activado con esas tarifas",
            path = "/api/contracts/validatesContractActivation",
            visibility = ApiVisibility.PUBLIC,
            verb = ApiVerb.POST,
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            responsestatuscode = "200 - OK, 204 - NO CONTENT. 401 - UNAUTHORIZED"
    )
    @ApiAuthToken(scheme = "JWT")
    @ApiResponseObject(clazz = List.class)
    @RequestMapping(value = "/validatesContractActivation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RatesByContract>> validatesContractActivation(
            @ApiBodyObject(clazz = ActivationValidator.class) @RequestBody ActivationValidator validator
    ) throws Exception
    {
        List<RatesByContract> contract = contractService.validatesContractActivation(validator);
        if(contract == null)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        else if(!contract.isEmpty())
        {
            return new ResponseEntity<>(contract, HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
