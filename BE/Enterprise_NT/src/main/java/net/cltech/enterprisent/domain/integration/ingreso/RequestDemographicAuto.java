package net.cltech.enterprisent.domain.integration.ingreso;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase para la auto creacion de demograficos por sistema central a homologar
 * 
 * @version 1.0.0
 * @author javila
 * @since 03/02/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Items de demografico a autocrear",
        description = "Auto creacion de items"
)
public class RequestDemographicAuto
{
    @ApiObjectField(name = "idSystem", description = "El id del Sistema Central", order = 1)
    private int idSystem;
    @ApiObjectField(name = "idDemographic", description = "El id LIS del demografico", order = 2)
    private int idDemographic;
    @ApiObjectField(name = "idItemDemographicHis", description = "El id HIS del item del demografico", order = 3)
    private String idItemDemographicHis;
    @ApiObjectField(name = "descItemDemographic", description = "La descripcion o nombre del item demografico", order = 4)
    private String descItemDemographic;

    public RequestDemographicAuto()
    {
    }

    public RequestDemographicAuto(int idSystem, int idDemographic, String idItemDemographicHis, String descItemDemographic)
    {
        this.idSystem = idSystem;
        this.idDemographic = idDemographic;
        this.idItemDemographicHis = idItemDemographicHis;
        this.descItemDemographic = descItemDemographic;
    }

    public int getIdSystem()
    {
        return idSystem;
    }

    public void setIdSystem(int idSystem)
    {
        this.idSystem = idSystem;
    }

    public int getIdDemographic()
    {
        return idDemographic;
    }

    public void setIdDemographic(int idDemographic)
    {
        this.idDemographic = idDemographic;
    }

    public String getIdItemDemographicHis()
    {
        return idItemDemographicHis;
    }

    public void setIdItemDemographicHis(String idItemDemographicHis)
    {
        this.idItemDemographicHis = idItemDemographicHis;
    }

    public String getDescItemDemographic()
    {
        return descItemDemographic;
    }

    public void setDescItemDemographic(String descItemDemographic)
    {
        this.descItemDemographic = descItemDemographic;
    }

    @Override
    public String toString()
    {
        return "RequestDemographicAuto{" + "idSystem=" + idSystem + ", idDemographic=" + idDemographic + ", idItemDemographicHis=" + idItemDemographicHis + ", descItemDemographic=" + descItemDemographic + '}';
    }   
}
