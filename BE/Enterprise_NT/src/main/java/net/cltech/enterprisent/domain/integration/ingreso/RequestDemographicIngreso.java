package net.cltech.enterprisent.domain.integration.ingreso;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase para la importación de demograficos a homologar
 *
 * @author BValero
 * @version 1.0.0
 * @since 22/01/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Demograficos a homologar",
        description = "Representa un demografico del HIS"
)
public class RequestDemographicIngreso {
    
    @ApiObjectField(name = "idDemographic", description = "El id LIS del demografico", order = 1)
    private int idDemographic;
    @ApiObjectField(name = "idItemDemographicHis", description = "El id HIS del item del demografico", order = 2)
    private String idItemDemographicHis;

    public RequestDemographicIngreso(int idDemographic, String idItemDemographicHis) {
        this.idDemographic = idDemographic;
        this.idItemDemographicHis = idItemDemographicHis;
    }

    public RequestDemographicIngreso() {
        this.idDemographic = 0;
        this.idItemDemographicHis = "";
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

    @Override
    public String toString() {
        return "RequestDemographicIngreso{" + "idDemographic=" + idDemographic + ", idItemDemographicHis=" + idItemDemographicHis + '}';
    }
    
}
