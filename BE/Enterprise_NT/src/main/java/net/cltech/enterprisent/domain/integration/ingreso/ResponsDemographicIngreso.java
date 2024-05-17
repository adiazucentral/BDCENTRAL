package net.cltech.enterprisent.domain.integration.ingreso;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase para la exportacion del demograficos a homologar
 *
 * @author BValero
 * @version 1.0.0
 * @since 22/01/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Demograficos homologados",
        description = "Representa un demografico del Lis"
)
public class ResponsDemographicIngreso {
    
    @ApiObjectField(name = "idDemographic", description = "El id LIS del demografico", order = 1)
    private int idDemographic;
    @ApiObjectField(name = "idItemDemographicHis", description = "El id HIS del item del demografico", order = 2)
    private String idItemDemographicHis;
    @ApiObjectField(name = "idItemDemographicLis", description = "El id LIS del item del demografico", order = 3)
    private int idItemDemographicLis;
    @ApiObjectField(name = "nameDemographic", description = "Nombre del demografico", order = 4)
    private String nameDemographic;

    public ResponsDemographicIngreso(int idDemographic, String idItemDemographicHis, int idItemDemographicLis, String nameDemographic) {
        this.idDemographic = idDemographic;
        this.idItemDemographicHis = idItemDemographicHis;
        this.idItemDemographicLis = idItemDemographicLis;
        this.nameDemographic = nameDemographic;
    }

    public ResponsDemographicIngreso() {
        this.idDemographic = 0;
        this.idItemDemographicHis = "";
        this.idItemDemographicLis = 0;
    }

    public int getIdDemographic() {
        return idDemographic;
    }

    public void setIdDemographic(int idDemographic) {
        this.idDemographic = idDemographic;
    }

    public String getIdItemDemographicHis() {
        return idItemDemographicHis;
    }

    public void setIdItemDemographicHis(String idItemDemographicHis) {
        this.idItemDemographicHis = idItemDemographicHis;
    }

    public int getIdItemDemographicLis() {
        return idItemDemographicLis;
    }

    public void setIdItemDemographicLis(int idItemDemographicLis) {
        this.idItemDemographicLis = idItemDemographicLis;
    }

    @Override
    public String toString() {
        return "ResponsDemographicIngreso{" + "idDemographic=" + idDemographic + ", idItemDemographicHis=" + idItemDemographicHis + ", idItemDemographicLis=" + idItemDemographicLis + '}';
    }

    public String getNameDemographic()
    {
        return nameDemographic;
    }

    public void setNameDemographic(String nameDemographic)
    {
        this.nameDemographic = nameDemographic;
    }
}
