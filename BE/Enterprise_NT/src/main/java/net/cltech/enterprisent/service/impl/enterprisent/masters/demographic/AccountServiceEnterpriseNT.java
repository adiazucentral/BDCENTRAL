package net.cltech.enterprisent.service.impl.enterprisent.masters.demographic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import net.cltech.enterprisent.dao.interfaces.masters.demographic.AccountDao;
import net.cltech.enterprisent.domain.exception.EnterpriseNTException;
import net.cltech.enterprisent.domain.integration.siigo.AccountSiigo;
import net.cltech.enterprisent.domain.integration.siigo.CitySiigo;
import net.cltech.enterprisent.domain.integration.siigo.ContactSiigo;
import net.cltech.enterprisent.domain.integration.siigo.PhoneSiigo;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.demographic.RatesOfAccount;
import net.cltech.enterprisent.service.interfaces.integration.BillingIntegrationService;
import net.cltech.enterprisent.service.interfaces.masters.configuration.ConfigurationService;
import net.cltech.enterprisent.service.interfaces.masters.demographic.AccountService;
import net.cltech.enterprisent.service.interfaces.masters.tracking.TrackingService;
import net.cltech.enterprisent.tools.Tools;
import net.sf.cglib.core.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementa los servicios de informacion del maestro Clientes para Enterprise NT
 *
 * @version 1.0.0
 * @author cmartin
 * @see 31/05/2017
 * @see Creaciòn
 */
@Service
public class AccountServiceEnterpriseNT implements AccountService
{

    @Autowired
    private AccountDao dao;
    @Autowired
    private TrackingService trackingService;
    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private BillingIntegrationService billingIntegrationService;

    @Override
    public List<Account> list() throws Exception
    {
        return dao.list();
    }

    @Override
    public Account create(Account account) throws Exception
    {
        List<String> errors = validateFields(false, account);
        if (errors.isEmpty())
        {
            Account created = dao.create(account);
            trackingService.registerConfigurationTracking(null, created, Account.class);
            // Hilo para la creación de tercero en Siigo
            CompletableFuture.runAsync(() ->
            {
                try
                {
                    if (configurationService.get("IntegracionSiigo").getValue().equalsIgnoreCase("True"))
                    {
                        AccountSiigo accountSiigo = new AccountSiigo();
                        CitySiigo citySiigo = new CitySiigo();
                        PhoneSiigo phoneSiigo = new PhoneSiigo();
                        ContactSiigo contactSiigo = new ContactSiigo();
                        
                        accountSiigo.getName().add(created.getName());
                        accountSiigo.setPerson_type("Company");
                        accountSiigo.setId_type("31");
                        // Razon social
                        accountSiigo.setIdentification(created.getNit());
                        accountSiigo.setIdDocumentType(0);

                        // Cargamos el objeto ciudad:
                        citySiigo.setCity_code("05001");
                        citySiigo.setCountry_code("Co");
                        citySiigo.setState_code("05");

                        accountSiigo.getAddress().setCity(citySiigo);
                        accountSiigo.getAddress().setAddress(created.getAddress());
                        accountSiigo.getAddress().setPostal_code(created.getPostalCode());
                        //Cargamos el telefono:
                        phoneSiigo.setExtension("");
                        phoneSiigo.setIndicative("");
                        if (Tools.isInteger(created.getPhone()))
                        {
                            phoneSiigo.setNumber(created.getPhone());
                        }
                        else
                        {
                            phoneSiigo.setNumber("0");
                        }
                        accountSiigo.getPhones().add(phoneSiigo);
                        
                        // Preguntar a angelica:
                        contactSiigo.setFirst_name(created.getEmail());
                        contactSiigo.setLast_name(created.getEmail());
                        contactSiigo.setEmail(created.getEmail());
                        contactSiigo.setPhone(phoneSiigo);
                        accountSiigo.getContacts().add(contactSiigo);
                        //accountSiigo.setDirectorID(0);
                        //accountSiigo.setSalesmanID(0);
                        //accountSiigo.setCollectorID(0);
                        //accountSiigo.setPrincipalContactID(0);
                        //accountSiigo.setFiscalResponsibilities(new ArrayList<>());
                        billingIntegrationService.sendToCreateAThird(accountSiigo);
                    }
                }
                catch (Exception ex)
                {
                    Logger.getLogger(AccountServiceEnterpriseNT.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            return created;
        }
        else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public Account get(Integer id, String name, String nit, String codeEps, String username) throws Exception
    {
        return dao.get(id, name, nit, codeEps, username);
    }

    @Override
    public Account update(Account account) throws Exception
    {
        List<String> errors = validateFields(true, account);
        if (errors.isEmpty())
        {
            Account accountC = dao.get(account.getId(), null, null, null, null);
            Account modifited = dao.update(account);
            trackingService.registerConfigurationTracking(accountC, modifited, Account.class);
            return modifited;
        }
        else
        {
            throw new EnterpriseNTException(errors);
        }
    }

    @Override
    public List<Account> list(boolean state) throws Exception
    {
        List<Account> filter = new ArrayList<>(CollectionUtils.filter(dao.list(), (Object o) -> ((Account) o).isState() == state));
        return filter;
    }

    private List<String> validateFields(boolean isEdit, Account account) throws Exception
    {
        //0 -> Datos vacios
        //1 -> Esta duplicado
        //2 -> Id no existe solo aplica para modificar
        List<String> errors = new ArrayList<>();
        if (isEdit)
        {
            if (account.getId() == null)
            {
                errors.add("0|id");
                return errors;
            }
            else
            {
                if (dao.get(account.getId(), null, null, null, null) == null)
                {
                    errors.add("2|id");
                    return errors;
                }
            }
        }

        /*if (account.getNit() != null)
        {
            Account accountC = dao.get(null, null, account.getNit(), null, null);
            if (accountC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(account.getId(), accountC.getId()))
                    {
                        errors.add("1|nit");
                    }
                }
                else
                {
                    errors.add("1|nit");
                }
            }
        }*/

        if (account.getEpsCode() != null && !account.getEpsCode().isEmpty())
        {
            Account accountC = dao.get(null, null, null, account.getEpsCode(), null);
            if (accountC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(account.getId(), accountC.getId()))
                    {
                        errors.add("1|eps code");
                    }
                }
                else
                {
                    errors.add("1|eps code");
                }
            }
        }

        if (account.getName() != null && !account.getName().isEmpty())
        {
            Account accountC = dao.get(null, account.getName(), null, null, null);
            if (accountC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(account.getId(), accountC.getId()))
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

        if (account.getEmail() != null && !account.getEmail().isEmpty())
        {
            Account accountC = dao.get(null, null, null, null, account.getEmail());
            if (accountC != null)
            {
                if (isEdit)
                {
                    if (!Objects.equals(account.getId(), accountC.getId()))
                    {
                        errors.add("1|email");
                    }
                }
                else
                {
                    errors.add("1|email");
                }
            }
        }

        if (account.getNamePrint() == null || account.getNamePrint().isEmpty())
        {
            errors.add("0|name to print");
        }

        if (account.getUser().getId() == null || account.getUser().getId() == 0)
        {
            errors.add("0|user");
        }

        return errors;
    }

    @Override
    public List<RatesOfAccount> getRatesByAccount(Integer id) throws Exception
    {
        return dao.getRatesByAccount(id);
    }
    
    @Override
    public List<RatesOfAccount> getRates(Integer id) throws Exception
    {
        return dao.getRates(id);
    }

    @Override
    public int insertRates(List<RatesOfAccount> ratesOfAccounts) throws Exception
    {
        int registros = 0;
        //Filtramos los seleccionados
        List<RatesOfAccount> filterList = ratesOfAccounts.stream()
                .filter(filter -> filter.isApply())
                .collect(Collectors.toList());

        if (filterList.size() > 0)
        {
            List<RatesOfAccount> previous = getRates(filterList.get(0).getAccount().getId());
            registros = dao.insertRates(filterList);
            trackingService.registerConfigurationTracking(previous, filterList, RatesOfAccount.class);
        }
        return registros;
    }

    @Override
    public int deleteRates(Integer idAccount) throws Exception
    {
        return dao.deleteRates(idAccount);
    }

}
