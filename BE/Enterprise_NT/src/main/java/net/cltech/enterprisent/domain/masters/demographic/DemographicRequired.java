package net.cltech.enterprisent.domain.masters.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;



/**
 * Clase que representa la informacion del maestro Homologación de demograficos
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 22/05/2020
 * @see Creación
 */
@ApiObject(
        group = "Demografico",
        name = "Requerido",
        description = "Muestra informacion del maestro Demografico"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DemographicRequired
{

    @ApiObjectField(name = "idDemographic", description = "Demografico", required = true, order = 1)
    private Integer idDemographic;
    @ApiObjectField(name = "defaultValueRequired", description = "Valor por defecto requerido", required = false, order = 2)
    private String defaultValueRequired;

    public DemographicRequired()
    {
    }

    public Integer getIdDemographic()
    {
        return idDemographic;
    }

    public void setIdDemographic(Integer idDemographic)
    {
        this.idDemographic = idDemographic;
    }

    public String getDefaultValueRequired()
    {
        return defaultValueRequired;
    }

    public void setDefaultValueRequired(String defaultValueRequired)
    {
        this.defaultValueRequired = defaultValueRequired;
    }

}
