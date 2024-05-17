package net.cltech.enterprisent.domain.masters.demographic;

import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del Demografico Padre con sus hijos
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 08/07/2020
 * @see Creación
 */
@ApiObject(
        group = "Demografico",
        name = "DemograficoFatherSons",
        description = "Representa el demografico hijo del padre con sus items hijos"
)
public class DemographicFatherSons
{

    @ApiObjectField(name = "idDemographicSon", description = "Id del demografico Hijo", required = true, order = 1)
    private Integer idDemographicSon;
    @ApiObjectField(name = "demographicSonItems", description = "Lista de Item Demograficos Hijos", required = true, order = 1)
    private List<Integer> demographicSonItems;

    public DemographicFatherSons()
    {
    }

    public Integer getIdDemographicSon()
    {
        return idDemographicSon;
    }

    public void setIdDemographicSon(Integer idDemographicSon)
    {
        this.idDemographicSon = idDemographicSon;
    }

    public List<Integer> getDemographicSonItems()
    {
        return demographicSonItems;
    }

    public void setDemographicSonItems(List<Integer> demographicSonItems)
    {
        this.demographicSonItems = demographicSonItems;
    }

}
