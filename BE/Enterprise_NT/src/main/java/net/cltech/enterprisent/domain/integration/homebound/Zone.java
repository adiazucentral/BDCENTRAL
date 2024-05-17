package net.cltech.enterprisent.domain.integration.homebound;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de una zona
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 11/06/2020
 * @see Creación
 */
@ApiObject(
        group = "Configuracion",
        name = "Zona",
        description = "Zona de la aplicación"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Zone
{

    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "state", description = "Estado", required = true, order = 4)
    private boolean state;
    @ApiObjectField(name = "selected", description = "Seleccionado", required = true, order = 5)
    private boolean selected;

    public Zone()
    {
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

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }

}
