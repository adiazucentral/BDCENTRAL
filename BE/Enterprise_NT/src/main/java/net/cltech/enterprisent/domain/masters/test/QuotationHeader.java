/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
        description = "Representa la cabecera de una cotizacion"
)
public class QuotationHeader
{

    @ApiObjectField(name = "id", description = "Id de la cabecera de la cotizacion", order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre completo", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "rate", description = "Tarifa", required = true, order = 4)
    private Rate rate;
    @ApiObjectField(name = "discount", description = "Descuento", required = true, order = 5)
    private Integer discount;
    @ApiObjectField(name = "priceTotal", description = "Precio total", required = true, order = 6)
    private Integer priceTotal;
    @ApiObjectField(name = "date", description = "Fecha modificacion", required = false, order = 7)
    private Date Date;
    @ApiObjectField(name = "user", description = "Usuario modificacion", required = false, order = 8)
    private Long user;
    @ApiObjectField(name = "tax", description = "impuesto por prueba", required = false, order = 8)
    private BigDecimal tax;
    @ApiObjectField(name = "quotationDetail", description = " Detalles de la cotizacion ", required = true, order = 9)
    private List<QuotationDetail> quotationDetail;
    @ApiObjectField(name = "addressCharge", description = "Cargo de domicilio", required = false, order = 10)
    private BigDecimal addressCharge;
    
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Rate getRate()
    {
        return rate;
    }

    public void setRate(Rate rate)
    {
        this.rate = rate;
    }

    public Integer getDiscount()
    {
        return discount;
    }

    public void setDiscount(Integer discount)
    {
        this.discount = discount;
    }

    public Integer getPriceTotal()
    {
        return priceTotal;
    }

    public void setPriceTotal(Integer priceTotal)
    {
        this.priceTotal = priceTotal;
    }

    public Date getDate()
    {
        return Date;
    }

    public void setDate(Date Date)
    {
        this.Date = Date;
    }

    public Long getUser()
    {
        return user;
    }

    public void setUser(Long user)
    {
        this.user = user;
    }

    public List<QuotationDetail> getQuotationDetail()
    {
        return quotationDetail;
    }

    public void setQuotationDetail(List<QuotationDetail> quotationDetail)
    {
        this.quotationDetail = quotationDetail;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final QuotationHeader other = (QuotationHeader) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getAddressCharge() {
        return addressCharge;
    }

    public void setAddressCharge(BigDecimal addressCharge) {
        this.addressCharge = addressCharge;
    }
    
}
