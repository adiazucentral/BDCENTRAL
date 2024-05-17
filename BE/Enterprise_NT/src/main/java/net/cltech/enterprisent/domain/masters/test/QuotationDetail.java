/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.test;

import java.math.BigDecimal;
import java.util.Date;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de una cotizacion
 *
 * @author jrodriguez
 * @version 1.0.0
 * @since 31/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Cotizacion",
        description = "Representa el detalle de una cotizacion"
)
public class QuotationDetail
{

    @ApiObjectField(name = "id", description = "Id", order = 1)
    private Integer id;
    @ApiObjectField(name = "idHeader", description = "Id de la cabecera de la cotizacion", order = 2)
    private Integer idHeader;
    @ApiObjectField(name = "test", description = " Examen", order = 3)
    private TestBasic test;
    @ApiObjectField(name = "testRate", description = "Tarifa del examen", order = 4)
    private Rate testRate;
    @ApiObjectField(name = "price", description = "Precio", order = 5)
    private BigDecimal price;
    @ApiObjectField(name = "date", description = "Fecha modificacion", required = false, order = 6)
    private Date Date;
    @ApiObjectField(name = "user", description = "Usuario modificacion", required = false, order = 7)
    private int user;
    @ApiObjectField(name = "count", description = "cantidad de pruebas", required = false, order = 8)
    private int count;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getIdHeader()
    {
        return idHeader;
    }

    public void setIdHeader(Integer idHeader)
    {
        this.idHeader = idHeader;
    }

    public TestBasic getTest()
    {
        return test;
    }

    public void setTest(TestBasic test)
    {
        this.test = test;
    }

    public Rate getTestRate()
    {
        return testRate;
    }

    public void setTestRate(Rate testRate)
    {
        this.testRate = testRate;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public Date getDate()
    {
        return Date;
    }

    public void setDate(Date Date)
    {
        this.Date = Date;
    }

    public int getUser()
    {
        return user;
    }

    public void setUser(int user)
    {
        this.user = user;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
    

}
