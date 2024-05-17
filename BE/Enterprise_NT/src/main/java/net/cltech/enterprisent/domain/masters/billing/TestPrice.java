package net.cltech.enterprisent.domain.masters.billing;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import net.cltech.enterprisent.domain.masters.test.Area;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de examenes
 *
 * @version 1.0.0
 * @author eacuna
 * @since 15/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Facturacion",
        name = "Precios Exames",
        description = "Precio del examen"
)
public class TestPrice
{

    @ApiObjectField(name = "id", description = "Id de la prueba", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo de la prueba", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "standarizationCode", description = "Codigo de homologación", required = true, order = 3)
    private String standarizationCode;
    @ApiObjectField(name = "abbr", description = "Abreviatura de la prueba", required = true, order = 4)
    private String abbr;
    @ApiObjectField(name = "name", description = "Nombre de la prueba", required = true, order = 5)
    private String name;
    @ApiObjectField(name = "area", description = "Area de la prueba", required = true, order = 6)
    private Area area = new Area();
    @ApiObjectField(name = "price", description = "Precio de la prueba", required = true, order = 7)
    private BigDecimal price;
    @ApiObjectField(name = "patientPercentage", description = "Porcentaje del paciente", required = false, order = 8)
    private Double patientPercentage;
    @ApiObjectField(name = "lastTransaction", description = "Fecha de la creación o ultima actualización", required = false, order = 9)
    private Date lastTransaction;
    @ApiObjectField(name = "user", description = "Usuario que realiza que realiza la operación", required = false, order = 10)
    private AuthorizedUser user;

    public TestPrice()
    {
        area = new Area();
        user = new AuthorizedUser();
    }

    public TestPrice(Integer id)
    {
        this.id = id;
        area = new Area();
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAbbr()
    {
        return abbr;
    }

    public void setAbbr(String abbr)
    {
        this.abbr = abbr;
    }

    public BigDecimal getPrice()
    {
        return price;
    }

    public TestPrice setPrice(BigDecimal price)
    {
        this.price = price;
        return this;
    }

    public Area getArea()
    {
        return area;
    }

    public void setArea(Area area)
    {
        this.area = area;
    }

    public String getStandarizationCode()
    {
        return standarizationCode;
    }

    public void setStandarizationCode(String standarizationCode)
    {
        this.standarizationCode = standarizationCode;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
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
        final TestPrice other = (TestPrice) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

    public Double getPatientPercentage()
    {
        return patientPercentage;
    }

    public void setPatientPercentage(Double patientPercentage)
    {
        this.patientPercentage = patientPercentage;
    }

    public Date getLastTransaction()
    {
        return lastTransaction;
    }

    public void setLastTransaction(Date lastTransaction)
    {
        this.lastTransaction = lastTransaction;
    }

    public AuthorizedUser getUser()
    {
        return user;
    }

    public void setUser(AuthorizedUser user)
    {
        this.user = user;
    }
    
}
