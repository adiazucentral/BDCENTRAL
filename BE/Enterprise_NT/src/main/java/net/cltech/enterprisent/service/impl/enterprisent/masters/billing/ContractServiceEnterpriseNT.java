package net.cltech.enterprisent.service.impl.enterprisent.masters.billing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.billing.ContractDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.masters.billing.ActivationValidator;
import net.cltech.enterprisent.domain.masters.billing.Contract;
import net.cltech.enterprisent.domain.masters.billing.CustomerContractFilter;
import net.cltech.enterprisent.domain.masters.billing.RatesByContract;
import net.cltech.enterprisent.domain.masters.demographic.RatesOfAccount;
import net.cltech.enterprisent.service.interfaces.masters.billing.ContractService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.AccountService;
import net.cltech.enterprisent.tools.log.orders.OrderCreationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementación de los servicios para la información del maestro Contratos
 *
 * @version 1.0.0
 * @author jbarbosa
 * @since 30/06/2021
 * @see Creación
 */
@Service
public class ContractServiceEnterpriseNT implements ContractService
{

    @Autowired
    private ContractDao contractDao;
    @Autowired
    private AccountService accountService;

    @Override
    public Contract getContract(int id) throws Exception
    {
        try
        {
            Contract contract = contractDao.getContract(id, null, null);
            //Impuestos del cliente 
            contract.setTaxs(contractDao.getTheContractTaxes(id));
            //Tarifas del cliente 
            // Obtengo TODAS las tarifas por cliente:
            List<RatesByContract> rates = contractDao.allRatesPerCustomerAndContract(contract.getIdclient(), id, contract.isState());
            // Removemos las tarifas del contrato nulas
            
            List<RatesByContract> ratesActive = new ArrayList<>();
            List<RatesByContract> ratesInactive = new ArrayList<>();
            ratesActive = rates.stream().filter(r -> r.getStatusType() == 1)
				.collect(Collectors.toList());
            ratesInactive = rates.stream().filter(r -> r.getStatusType() == 0)
				.collect(Collectors.toList());
            
            
            List<RatesByContract> filterrate = new ArrayList<>();
            for (RatesByContract rate : ratesInactive)
            {
                filterrate = ratesActive.stream().filter(r -> Objects.equals(r.getRateId(), rate.getRateId())).collect(Collectors.toList());
                if(filterrate.isEmpty()){

                    rate.setStatusType(rate.getContractId() == 0 ? (short)2 : (rate.getContractState() == 0 ? (short)2 : 0));

                    ratesActive.add(rate);
                }  
                else{
                    
                    if(filterrate.get(0).getContractId() == 0 && rate.getContractId() != 0 ){
                        rate.setStatusType(rate.getContractState() == 0 ? (short)2 : 0);
                        int index = ratesActive.indexOf(filterrate.get(0));
                        
                        ratesActive.set(index, rate);
                    }
                }
            }
            

            contract.setRates(ratesActive);
            return contract;
        }
        catch (Exception ex)
        {
            return null;
        }
    }
    


    @Override
    public Contract createContract(Contract contract) throws Exception
    {
        List<String> errors = validateFields(false, contract);
        if (errors.isEmpty())
        {
            Contract contract1 = contractDao.create(contract);
            if (contract.getTaxs() != null)
            {
                //Impuestos del cliente 
                contractDao.addTaxesPerContract(contract.getTaxs(), contract1.getId());
            }
            //Tarifas del cliente
            contractDao.insertRatesWhitId(contract.getRates(), contract1.getId());
            return contract1;
        }
        else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Contract updateContract(Contract contract) throws Exception
    {
        List<String> errors = validateFields(true, contract);
        if (errors.isEmpty())
        {
            Contract contract1 = contractDao.update(contract);
            if (contract.getTaxs() != null)
            {
                //Impuestos del cliente 
                contractDao.addTaxesPerContract(contract.getTaxs(), contract1.getId());
            }
            //Tarifas del cliente 
            contractDao.insertRatesWhitId(contract.getRates(), contract1.getId());
            return contract1;
        }
        else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<Contract> getContractByCustomerId(int idCustomer) throws Exception
    {
        try
        {
            return contractDao.getContractByCustomerId(idCustomer, 0);
        }
        catch (Exception e)
        {
            OrderCreationLog.error("Error contratos " + e);
            return null;
        }
    }

    @Override
    public List<RatesOfAccount> getRatesByContractId(int customerId) throws Exception
    {
        try
        {
            // Obtengo TODAS las tarifas por cliente:
            List<RatesOfAccount> allRatesOfAccounts = accountService.getRatesByAccount(customerId);
            // Consultamos las tarifas de todos los contratos de un cliente
            List<RatesOfAccount> contractRates = contractDao.ratesOfACustomersContracts(customerId);
            for (RatesOfAccount rate : contractRates)
            {
                allRatesOfAccounts.removeIf(rateTwo -> rateTwo.getRate().getId().equals(rate.getRate().getId()));
            }
            return allRatesOfAccounts;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    private List<String> validateFields(boolean isEdit, Contract contract) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (contract.getId() == null)
            {
                errors.add("0|id");
                return errors;
            }
            else
            {
                if (contractDao.getContract(contract.getId(), null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }

            if (contract.getRates().isEmpty())
            {
                errors.add("0|A contract cannot be updated without fees");
                return errors;
            }
        }

        if (contract.getName() != null && !contract.getName().isEmpty())
        {
            Contract contractOne = contractDao.getContract(null, contract.getName(), null);
            if (contractOne != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(contractOne.getId(), contract.getId()))
                    {
                        errors.add("1|name");
                    }
                }
                else
                {
                    errors.add("1|name");
                }
            }
        }
        else
        {
            errors.add("0|name");
        }

        if (contract.getCode() != null && !contract.getCode().isEmpty())
        {
            Contract contractTwo = contractDao.getContract(null, null, contract.getCode());
            if (contractTwo != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(contractTwo.getId(), contract.getId()))
                    {
                        errors.add("1|code");
                    }
                }
                else
                {
                    errors.add("1|code");
                }
            }
        }
        else
        {
            errors.add("0|code");
        }

        if (contract.getRates().isEmpty())
        {
            errors.add("0|A contract cannot be created without fees");
            return errors;
        }

        return errors;
    }

    @Override
    public List<Contract> getContractsForTheAssociatedRates(CustomerContractFilter filter) throws Exception
    {
        try
        {
            return contractDao.getContractsForTheAssociatedRates(filter);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @Override
    public List<RatesByContract> validatesContractActivation(ActivationValidator validator) throws Exception
    {
        try
        {
            return contractDao.validatesContractActivation(validator);
        }
        catch (Exception e)
        {
            return null;
        }
    }
}
