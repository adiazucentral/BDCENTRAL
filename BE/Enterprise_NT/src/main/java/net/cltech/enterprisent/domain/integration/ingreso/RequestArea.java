package net.cltech.enterprisent.domain.integration.ingreso;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Recipientes
 *
 * @author JDuarte
 * @version 1.0.0
 * @since 12/05/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Area a homologar",
        description = "Area del test a homologar"
)
public class RequestArea
{

    @ApiObjectField(name = "id", description = "El id del area", order = 1)
    private Integer id;
    @ApiObjectField(name = "ordering", description = "Ordenamiento del area", order = 2)
    private Integer ordering;
    @ApiObjectField(name = "abbreviation", description = "Abreviatura del area", order = 3)
    private String abbreviation;
    @ApiObjectField(name = "name", description = "Nombre del area", order = 4)
    private String name;
    @ApiObjectField(name = "color", description = "Color del area", order = 5)
    private String color;
    @ApiObjectField(name = "state", description = "Estado del area", order = 6)
    private boolean state;
    @ApiObjectField(name = "partialValidation", description = "Parcial del area", order = 7)
    private boolean partialValidation;

    public RequestArea()
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

    public Integer getOrdering()
    {
        return ordering;
    }

    public void setOrdering(Integer ordering)
    {
        this.ordering = ordering;
    }

    public String getAbbreviation()
    {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation)
    {
        this.abbreviation = abbreviation;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getColor()
    {
        return color;
    }

    public void setColor(String color)
    {
        this.color = color;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public boolean isPartialValidation()
    {
        return partialValidation;
    }

    public void setPartialValidation(boolean partialValidation)
    {
        this.partialValidation = partialValidation;
    }

}
