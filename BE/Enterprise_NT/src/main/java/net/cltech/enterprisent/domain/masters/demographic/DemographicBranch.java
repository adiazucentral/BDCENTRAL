package net.cltech.enterprisent.domain.masters.demographic;

import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import org.jsondoc.core.annotation.Api;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Homologación de demograficos
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 18/05/2020
 * @see Creación
 */
@Api(
        name = "DemograficoItem",
        group = "Demografico",
        description = "Servicios de informacion del maestro Demografico"
)
public class DemographicBranch
{

    @ApiObjectField(name = "id", description = "Demografico", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "nameDemographic", description = "Demografico", required = true, order = 2)
    private String nameDemographic;
    @ApiObjectField(name = "encoded", description = "0 -> no codificado 1 -> codificado", required = true, order = 2)
    private Boolean encoded;
    @ApiObjectField(name = "selected", description = "Asociacion", required = false, order = 3)
    private Boolean selected;
    @ApiObjectField(name = "idBranch", description = "id de la sede", required = false, order = 4)
    private Integer idBranch;
    @ApiObjectField(name = "required", description = "El demografico es obligatorio", required = false, order = 5)
    private boolean required;
    @ApiObjectField(name = "defaultValue", description = "Valor requerido por defecto", required = false, order = 6)
    private String defaultValue;

    public DemographicBranch(DemographicValue demographicValue)
    {
        this.id = demographicValue.getCodifiedId();
        this.nameDemographic = demographicValue.getDemographic();
        this.encoded = demographicValue.isEncoded();

    }

    public DemographicBranch()
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

    public String getNameDemographic()
    {
        return nameDemographic;
    }

    public void setNameDemographic(String nameDemographic)
    {
        this.nameDemographic = nameDemographic;
    }

    public Boolean getEncoded()
    {
        return encoded;
    }

    public void setEncoded(Boolean encoded)
    {
        this.encoded = encoded;
    }

    public Boolean getSelected()
    {
        return selected;
    }

    public void setSelected(Boolean selected)
    {
        this.selected = selected;
    }

    public Integer getIdBranch()
    {
        return idBranch;
    }

    public void setIdBranch(Integer idBranch)
    {
        this.idBranch = idBranch;
    }

    public boolean isRequired()
    {
        return required;
    }

    public void setRequired(boolean required)
    {
        this.required = required;
    }

    public String getDefaultValue()
    {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue)
    {
        this.defaultValue = defaultValue;
    }
}
