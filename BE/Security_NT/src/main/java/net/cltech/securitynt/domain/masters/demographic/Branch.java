/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.domain.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Objects;
import net.cltech.securitynt.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Sedes
 *
 * @version 1.0.0
 * @author cmartin
 * @since 08/05/2017
 * @see Creación
 */
@ApiObject(
        group = "Demografico",
        name = "Sede",
        description = "Muestra informacion del maestro Sedes que usa el API"
)
@JsonInclude(Include.NON_NULL)
public class Branch extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id de la sede", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo de la sede", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "abbreviation", description = "Abreviación de la sede", required = true, order = 2)
    private String abbreviation;
    @ApiObjectField(name = "responsable", description = "Responsable de la sede", required = true, order = 2)
    private String responsable;
    @ApiObjectField(name = "name", description = "Nombre de la sede", required = true, order = 4)
    private String name;
    @ApiObjectField(name = "address", description = "Dirección de la sede", required = true, order = 5)
    private String address;
    @ApiObjectField(name = "phone", description = "Telefono de la sede", required = true, order = 6)
    private String phone;
    @ApiObjectField(name = "email", description = "Email de la sede", required = true, order = 6)
    private String email;
    @ApiObjectField(name = "minimum", description = "Minimo", required = true, order = 7)
    private Integer minimum;
    @ApiObjectField(name = "maximum", description = "Telefono de la sede", required = true, order = 8)
    private Integer maximum;
    @ApiObjectField(name = "state", description = "Estado de la sede", required = true, order = 9)
    private boolean state;

    public Branch()
    {
    }

    public Branch(Integer id, String code, String name)
    {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Branch(Integer id)
    {
        this.id = id;
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

    public String getAbbreviation()
    {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation)
    {
        this.abbreviation = abbreviation;
    }

    public String getResponsable()
    {
        return responsable;
    }

    public void setResponsable(String responsable)
    {
        this.responsable = responsable;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public Integer getMinimum()
    {
        return minimum;
    }

    public void setMinimum(Integer minimum)
    {
        this.minimum = minimum;
    }

    public Integer getMaximum()
    {
        return maximum;
    }

    public void setMaximum(Integer maximum)
    {
        this.maximum = maximum;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
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
        final Branch other = (Branch) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

}
