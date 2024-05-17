package net.cltech.enterprisent.domain.masters.demographic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.test.CentralSystem;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Homologación de demograficos
 *
 * @version 1.0.0
 * @author cmartin
 * @since 02/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Demográfico",
        name = "Homologación de Demograficos",
        description = "Muestra el maestro homologación de resultados."
)
public class StandardizationDemographic extends CentralSystem
{

    @ApiObjectField(name = "demographic", description = "Demografico", required = true, order = 1)
    private Demographic demographic;
    @ApiObjectField(name = "demographicItem", description = "Demografico Item", required = true, order = 2)
    private DemographicItem demographicItem;
    @ApiObjectField(name = "centralCode", description = "Codigo Central", required = true, order = 3)
    private List<String> centralCode;
    @ApiObjectField(name = "centralName", description = "Nombre del Codigo Central", required = false, order = 4)
    private String centralName;

    public StandardizationDemographic()
    {
        demographic = new Demographic();
        demographicItem = new DemographicItem();
        centralCode = new ArrayList<>();
    }

    public StandardizationDemographic(Integer idCentralSystem, Integer idDemographic, Integer idDemographicItem, String name, String code, List<String> centralCode)
    {
        demographic = new Demographic();
        demographic.setId(idDemographic);
        demographicItem = new DemographicItem();
        demographicItem.setId(idDemographicItem);
        demographicItem.setName(name);
        demographicItem.setCode(code);
        this.centralCode = centralCode;
        this.setId(idCentralSystem);
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

    public List<String> getCentralCode()
    {
        return centralCode;
    }

    public void setCentralCode(List<String> centralCode)
    {
        this.centralCode = centralCode;
    }

    @Override
    public int hashCode()
    {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.centralCode);
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
        final StandardizationDemographic other = (StandardizationDemographic) obj;
        if (Objects.equals(this.centralCode, other.centralCode))
        {
            return true;
        } 
        return Objects.equals(this.getDemographicItem().getId(), other.getDemographicItem().getId());
    }

    public String getCentralName()
    {
        return centralName;
    }

    public void setCentralName(String centralName)
    {
        this.centralName = centralName;
    }

}
