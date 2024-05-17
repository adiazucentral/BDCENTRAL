package net.cltech.securitynt.domain.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import net.cltech.securitynt.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Tipo Orden
 *
 * @version 1.0.0
 * @author eacuna
 * @since 16/05/2017
 * @see Creación
 */
@ApiObject(
        group = "Demográfico",
        name = "Tipo Orden",
        description = "Muestra informacion de  servicios "
)
@JsonInclude(Include.NON_NULL)
public class OrderType extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id base de datos", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Código", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "color", description = "Color", required = false, order = 4)
    private String color;
    @ApiObjectField(name = "state", description = "Estado del servicio", required = true, order = 6)
    private boolean state;

    public OrderType()
    {
    }

    public OrderType(Integer id)
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

}
