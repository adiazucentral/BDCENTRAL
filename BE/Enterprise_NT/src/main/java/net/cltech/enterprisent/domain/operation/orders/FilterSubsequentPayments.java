package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa el filtro que haremos para obtener el listado de ordenes 
* con las que se realizaran los pagos posteriores
*
* @version 1.0.0
* @author Julian
* @since 27/10/2020
* @see Creación
*/
@ApiObject(
        group = "Operación - Ordenes",
        name = "Filtrar Pagos Posteriores",
        description = "Permitirá realizar el filtro de ordenes a las que se les realizara el pago posterior"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterSubsequentPayments 
{
    @ApiObjectField(name = "initialDate", description = "Fecha inicial del rango de busqueda de ordenes", required = false, order = 1)
    private Integer initialDate;
    @ApiObjectField(name = "endDate", description = "Fecha final del rango de busqueda de ordenes", required = false, order = 2)
    private Integer endDate;
    @ApiObjectField(name = "firstName", description = "Primer nombre", required = false, order = 3)
    private String firstName;
    @ApiObjectField(name = "secondName", description = "Segundo nombre", required = false, order = 4)
    private String secondName;
    @ApiObjectField(name = "firstSurname", description = "Primer apellido", required = false, order = 5)
    private String firstSurname;
    @ApiObjectField(name = "secondSurname", description = "Segundo apellido", required = false, order = 6)
    private String secondSurname;
    @ApiObjectField(name = "outstandingBalance", description = "Saldo pendiente", required = false, order = 8)
    private boolean outstandingBalance;
    @ApiObjectField(name = "demographic", description = "Filtro por demografico", required = false, order = 7)
    private FilterDemographic demographic;

    public FilterSubsequentPayments()
    {
    }

    public Integer getInitialDate()
    {
        return initialDate;
    }

    public void setInitialDate(Integer initialDate)
    {
        this.initialDate = initialDate;
    }

    public Integer getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Integer endDate)
    {
        this.endDate = endDate;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getSecondName()
    {
        return secondName;
    }

    public void setSecondName(String secondName)
    {
        this.secondName = secondName;
    }

    public String getFirstSurname()
    {
        return firstSurname;
    }

    public void setFirstSurname(String firstSurname)
    {
        this.firstSurname = firstSurname;
    }

    public String getSecondSurname()
    {
        return secondSurname;
    }

    public void setSecondSurname(String secondSurname)
    {
        this.secondSurname = secondSurname;
    }

    public boolean isOutstandingBalance()
    {
        return outstandingBalance;
    }

    public void setOutstandingBalance(boolean outstandingBalance)
    {
        this.outstandingBalance = outstandingBalance;
    }

    public FilterDemographic getDemographic()
    {
        return demographic;
    }

    public void setDemographic(FilterDemographic demographic)
    {
        this.demographic = demographic;
    }
}