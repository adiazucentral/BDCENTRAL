package net.cltech.enterprisent.service.interfaces.masters.billing;

import java.util.List;
import net.cltech.enterprisent.domain.masters.billing.ActivationValidator;
import net.cltech.enterprisent.domain.masters.billing.Contract;
import net.cltech.enterprisent.domain.masters.billing.CustomerContractFilter;
import net.cltech.enterprisent.domain.masters.billing.RatesByContract;
import net.cltech.enterprisent.domain.masters.demographic.RatesOfAccount;

/**
 * Interfaz de servicios para la información del maestro Contratos
 *
 * @version 1.0.0
 * @author jbarbosa
 * @since 30/06/2021
 * @see Creación
 */
public interface ContractService
{
    /**
     * Obtiene la información de un contrato por el id del mismo
     * 
     * @param id
     * @return Contrato
     * @throws Exception 
     */
    public Contract getContract(int id) throws Exception;
    
    /**
     * Crea un nuevo contrato
     * 
     * @param contract
     * @return Contrato creado
     * @throws Exception 
     */
    public Contract createContract(Contract contract) throws Exception;
    
    /**
     * Crea un nuevo contrato
     * 
     * @param contract
     * @return Contrato actualizado
     * @throws Exception 
     */
    public Contract updateContract(Contract contract) throws Exception;
    
    /**
     * Consulta la lista de contratos por el id del cliente
     * 
     * @param idCustomer id del cliente
     * @return Lista de contratos por cliente
     * @throws Exception 
     */
    public List<Contract> getContractByCustomerId(int idCustomer) throws Exception;
    
    /**
     * Consulta la lista de tarifas que no son parte de ningun contrato del cliente, por el id del mismo
     * 
     * @param customerId id del cliente
     * @return Lista de contratos por cliente
     * @throws Exception 
     */
    public List<RatesOfAccount> getRatesByContractId(int customerId) throws Exception;
    
    /**
     * Consulta la lista de contratos que participan a la hora de facturar a un cliente dependiendo de las tarifas asociadas al mismo
     * 
     * @param filter Filtros para la consulta de contratos
     * @return Lista de contratos por cliente
     * @throws Exception 
     */
    public List<Contract> getContractsForTheAssociatedRates(CustomerContractFilter filter) throws Exception;
    
    /**
     * Valida si un contrato puede ser desactivado o si ya existe un contrato activado con esas tarifas
     * 
     * @param validator Validador de activación
     * @return Contrato encontrado con las mismas tarifas y activado
     * @throws Exception 
     */
    public List<RatesByContract> validatesContractActivation(ActivationValidator validator) throws Exception;
}
