package net.cltech.enterprisent.domain.masters.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Motivos
 *
 * @version 1.0.0
 * @author cmartin
 * @since 06/06/2017
 * @see Creación
 */
@ApiObject(
        group = "Comun",
        name = "Motivo",
        description = "Muestra informacion del maestro Motivo que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Motive extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id del motivo", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre del motivo", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "description", description = "Descripción del motivo", required = true, order = 3)
    private String description;
    @ApiObjectField(name = "type", description = "Tipo del motivo", required = true, order = 4)
    private Item type;
    @ApiObjectField(name = "state", description = "Estado del motivo", required = true, order = 5)
    private boolean state;
    @ApiObjectField(name = "order", description = "Numero de orden", required = true, order = 6)
    private Long order;
    @ApiObjectField(name = "test", description = "Examen (agrupar)", required = true, order = 7)
    private Integer test;

    public Motive()
    {
        type = new Item();
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Item getType()
    {
        return type;
    }

    public void setType(Item type)
    {
        this.type = type;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public Long getOrder()
    {
        return order;
    }

    public void setOrder(Long order)
    {
        this.order = order;
    }

    public Integer getTest()
    {
        return test;
    }

    public void setTest(Integer test)
    {
        this.test = test;
    }

}
