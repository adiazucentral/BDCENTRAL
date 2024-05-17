package net.cltech.enterprisent.domain.masters.demographic;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro demografico Item
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 07/07/2020
 * @see Creación
 */
@ApiObject(
        group = "Demografico",
        name = "DemograficoItemSon",
        description = "Muestra informacion del item demograficos asociados a un demografico hijo"
)
public class ItemDemographicSon extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id del demografico Item", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo del demografico Item", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre del demografico Item", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "selected", description = "Si se encuantra en la tabla lab190", required = true, order = 7)
    private Boolean selected;

    public ItemDemographicSon()
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

    public Boolean getSelected()
    {
        return selected;
    }

    public void setSelected(Boolean selected)
    {
        this.selected = selected;
    }

}
