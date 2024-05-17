package net.cltech.enterprisent.domain.masters.demographic;

import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro de tarifas por Cliente
 *
 * @version 1.0.0
 * @author enavas
 * @since 03/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Demografico",
        name = "TarifasXClientes",
        description = "Muestra relación tarifa por clientes que usa el API"
)
public class RatesOfAccount extends MasterAudit
{

    @ApiObjectField(name = "account", description = "cliente", required = true, order = 1)
    private Account account;
    @ApiObjectField(name = "rate", description = "tarifa", required = true, order = 2)
    private Rate rate;
    @ApiObjectField(name = "apply", description = "Aplicar", required = true, order = 3)
    private boolean apply;

    public RatesOfAccount()
    {
        account = new Account();
        rate = new Rate();
    }

    public RatesOfAccount(int IdAccount, int IdRate)
    {
        account = new Account();
        rate = new Rate();
        account.setId(IdAccount);
        rate.setId(IdRate);
        apply = true;
    }

    public Account getAccount()
    {
        return account;
    }

    public void setAccount(Account account)
    {
        this.account = account;
    }

    public Rate getRate()
    {
        return rate;
    }

    public void setRate(Rate rate)
    {
        this.rate = rate;
    }

    public boolean isApply()
    {
        return apply;
    }

    public void setApply(boolean apply)
    {
        this.apply = apply;
    }

}
