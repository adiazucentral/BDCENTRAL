package net.cltech.enterprisent.domain.masters.demographic;

import java.util.List;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del Demografico Padre con sus hijos
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 07/07/2020
 * @see Creación
 */
@ApiObject(
        group = "Demografico",
        name = "DemograficoFather",
        description = "Representa el demografico padre con sus hijos"
)
public class DemographicFather extends MasterAudit
{

    @ApiObjectField(name = "idDemographicFather", description = "Id del demografico del padre", required = true, order = 1)
    private Integer idDemographicFather;
    @ApiObjectField(name = "idDemographicFatherItem", description = "Id del demografico Item del padre", required = true, order = 2)
    private Integer idDemographicFatherItem;
    @ApiObjectField(name = "idDemographicSon", description = "Id del demografico Item", required = true, order = 3)
    private Integer idDemographicSon;
    @ApiObjectField(name = "demographicSonItems", description = "Lista de Item Demograficos Hijos", required = true, order = 4)
    private List<ItemDemographicSon> demographicSonItems;

    public DemographicFather()
    {
    }

    public Integer getIdDemographicFather()
    {
        return idDemographicFather;
    }

    public void setIdDemographicFather(Integer idDemographicFather)
    {
        this.idDemographicFather = idDemographicFather;
    }

    public Integer getIdDemographicFatherItem()
    {
        return idDemographicFatherItem;
    }

    public void setIdDemographicFatherItem(Integer idDemographicFatherItem)
    {
        this.idDemographicFatherItem = idDemographicFatherItem;
    }

    public Integer getIdDemographicSon()
    {
        return idDemographicSon;
    }

    public void setIdDemographicSon(Integer idDemographicSon)
    {
        this.idDemographicSon = idDemographicSon;
    }

    public List<ItemDemographicSon> getDemographicSonItems()
    {
        return demographicSonItems;
    }

    public void setDemographicSonItems(List<ItemDemographicSon> demographicSonItems)
    {
        this.demographicSonItems = demographicSonItems;
    }

}
