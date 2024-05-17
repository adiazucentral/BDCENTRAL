package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Detalle simple de una factura
*
* @version 1.0.0
* @author Julian
* @since 22/04/2021
* @see Creación
*/

@ApiObject(
        group = "Operación - Facturación",
        name = "Detalle Simple De La Factura",
        description = "Representa el detalle simple de la factura"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleInvoiceDetail 
{
    @ApiObjectField(name = "customersName", description = "Nombres de los clientes", required = true, order = 1)
    private List<String> customersName;
    @ApiObjectField(name = "ratesNames", description = "Nombres de las tarifas (Sucursales)", required = true, order = 2)
    private List<String> ratesNames;
    @ApiObjectField(name = "branchName", description = "Nombre de la sede", required = true, order = 3)
    private String branchName;
    @ApiObjectField(name = "value", description = "Valor restante de la factura", required = true, order = 4)
    private Double value;

    public SimpleInvoiceDetail()
    {
    }

    public List<String> getCustomersName() {
        return customersName;
    }

    public void setCustomersName(List<String> customersName) {
        this.customersName = customersName;
    }

    public String getBranchName()
    {
        return branchName;
    }

    public void setBranchName(String branchName)
    {
        this.branchName = branchName;
    }

    public Double getValue()
    {
        return value;
    }

    public void setValue(Double value)
    {
        this.value = value;
    }

    public List<String> getRatesNames()
    {
        return ratesNames;
    }

    public void setRatesNames(List<String> ratesNames)
    {
        this.ratesNames = ratesNames;
    }
}