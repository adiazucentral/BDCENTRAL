package net.cltech.enterprisent.domain.integration.mobile;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Agregar una descripción de la clase
 *
 * @version 1.0.0
 * @author nmolina
 * @since 24/08/2020
 * @see Creacion
 */
@ApiObject(
        group = "Itegración",
        name = "Objeto de Test Et",
        description = "Representa el objeto de los examenes Et"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestEt
{

    @ApiObjectField(name = "id", description = "id del Examen Et", required = true, order = 1)
    private int id;
    @ApiObjectField(name = "code", description = "Codigo del Examen Et", required = true, order = 1)
    private String code;
    @ApiObjectField(name = "abbr", description = "Abreviatura del Examen Et", required = true, order = 1)
    private String abbr;
    @ApiObjectField(name = "name", description = "Nombre del Examen Et", required = true, order = 1)
    private String name;
    @ApiObjectField(name = "info", description = "Informacion del Examen Et", required = true, order = 1)
    private String info;
    @ApiObjectField(name = "sample", description = "Muestra para del Examen Et", required = true, order = 1)
    private Sample sample;
    @ApiObjectField(name = "requirement", description = "Requisitos para el Examen Et", required = true, order = 1)
    private List<Requirement> requirement;
    @ApiObjectField(name = "active", description = "Estado del Examen Et", required = true, order = 1)
    private boolean active;
    @ApiObjectField(name = "viewInOrder", description = "Visible en la orden del Examen Et", required = true, order = 1)
    private boolean viewInOrder;

    public TestEt()
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

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }

    public Sample getSample()
    {
        return sample;
    }

    public void setSample(Sample sample)
    {
        this.sample = sample;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public boolean isViewInOrder()
    {
        return viewInOrder;
    }

    public void setViewInOrder(boolean viewInOrder)
    {
        this.viewInOrder = viewInOrder;
    }

    public List<Requirement> getRequirement()
    {
        return requirement;
    }

    public void setRequirement(List<Requirement> requirement)
    {
        this.requirement = requirement;
    }

}
