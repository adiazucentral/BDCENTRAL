package net.cltech.enterprisent.domain.masters.demographic;

import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Homologación de demograficos
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 14/05/2020
 * @see Creación
 */
@Api(
        name = "DemograficoItem",
        group = "Demografico",
        description = "Servicios de informacion del maestro Demografico Item"
)
public class BranchDemographic extends Branch
{

    @ApiObjectField(name = "demographic", description = "Demografico", required = true, order = 1)
    private Demographic demographic;
    @ApiObjectField(name = "demographicItem", description = "Demografico Item", required = true, order = 2)
    private DemographicItem demographicItem;
    @ApiObjectField(name = "selected", description = "Asociacion del demografico item ara la sede", required = false, order = 3)
    private Boolean selected;

    public BranchDemographic()
    {
        demographic = new Demographic();
        demographicItem = new DemographicItem();

    }

    public BranchDemographic(Integer branch, Integer idDemographic, Integer idDemographicItem, String name, String code, Boolean selected)
    {
        demographic = new Demographic();
        demographic.setId(idDemographic);
        demographicItem = new DemographicItem();
        demographicItem.setId(idDemographicItem);
        demographicItem.setName(name);
        demographicItem.setCode(code);
        this.setId(branch);
        this.setSelected(selected);
    }

    public Demographic getDemographic()
    {
        return demographic;
    }

    public void setDemographic(Demographic demographic)
    {
        this.demographic = demographic;
    }

    public DemographicItem getDemographicItem()
    {
        return demographicItem;
    }

    public void setDemographicItem(DemographicItem demographicItem)
    {
        this.demographicItem = demographicItem;
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
