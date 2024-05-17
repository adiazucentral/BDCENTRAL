package net.cltech.enterprisent.domain.masters.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el objeto de configuración para la encriptación de reportes
 * por cada item correspondiente a un demografico fijo o dinamico
 * 
 * @version 1.0.0
 * @author Julian
 * @since 04/05/2020
 * @see Creación
 */

@ApiObject(
        name = "Encriptación de reporte por demográfico",
        group = "Configuración",
        description = "Configuración para la encriptación de reportes para cada item de un demográfico sea fijo o dinamico"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DemographicReportEncryption
{
    @ApiObjectField(name = "idDemographic", description = "id del demográfico", required = true, order = 1)
    private int idDemographic;
    @ApiObjectField(name = "idDemographicItem", description = "id del item demográfico", required = true, order = 2)
    private int idDemographicItem;
    @ApiObjectField(name = "encryption", description = "Encriptación del reporte de resultados: 0 -> No encriptado, 1 -> Encriptado", required = true, order = 3)
    private int encryption;
    @ApiObjectField(name = "selected", description = "True - El item esta relacionado con el demografico, False - El item no esta relacionado con el demografico", required = false, order = 4)
    private boolean selected;
    @ApiObjectField(name = "code", description = "Codigo del item del demografico", required = false, order = 5)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre del item del demografico", required = false, order = 6)
    private String name;

    public DemographicReportEncryption()
    {
    }

    public int getIdDemographic()
    {
        return idDemographic;
    }

    public void setIdDemographic(int idDemographic)
    {
        this.idDemographic = idDemographic;
    }

    public int getIdDemographicItem()
    {
        return idDemographicItem;
    }

    public void setIdDemographicItem(int idDemographicItem)
    {
        this.idDemographicItem = idDemographicItem;
    }

    public int getEncryption()
    {
        return encryption;
    }

    public void setEncryption(int encryption)
    {
        this.encryption = encryption;
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

    public boolean isSelected()
    {
        return selected;
    }

    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
}
