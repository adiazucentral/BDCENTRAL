package net.cltech.enterprisent.domain.integration.homebound;

import java.math.BigDecimal;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un prueba para ser cobrada desde Home Bound
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 12/06/2020
 * @see Creación
 */
@ApiObject(
        group = "Facturación", 
        name = "Prueba con cobro", 
        description = "Representa una prueba para ser cobrada"
)
public class BillingTestHomeBound
{

    @ApiObjectField(order = 1, name = "id", description = "Identificador Prueba")
    private int id;
    @ApiObjectField(order = 2, name = "code", description = "Codigo Prueba")
    private String code;
    @ApiObjectField(order = 3, name = "abbr", description = "Abreviatura Prueba")
    private String abbr;
    @ApiObjectField(order = 4, name = "name", description = "Nombre Prueba")
    private String name;
    @ApiObjectField(order = 5, name = "rate", description = "Tarifa")
    private RateHomeBound rate;
    @ApiObjectField(order = 6, name = "price", description = "Precio")
    private BigDecimal price;
    @ApiObjectField(order = 7, name = "sample", description = "Muestra del examen")
    private SampleHomeBound sample;

    public BillingTestHomeBound()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getAbbr()
    {
        return abbr;
    }

    public void setAbbr(String abbr)
    {
        this.abbr = abbr;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public RateHomeBound getRate()
    {
        return rate;
    }

    public void setRate(RateHomeBound rate)
    {
        this.rate = rate;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public void setPrice(BigDecimal price)
    {
        this.price = price;
    }

    public SampleHomeBound getSample()
    {
        return sample;
    }

    public void setSample(SampleHomeBound sample)
    {
        this.sample = sample;
    }

}
