package net.cltech.enterprisent.domain.integration.ingreso;

import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase para la importación de demograficos por sistema central a homologar
 *
 * @author BValero
 * @version 1.0.0
 * @since 22/01/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Demograficos por systema central a homologar",
        description = "Representa los demograficos del HIS a homologar"
)
public class RequestHomologationDemographicIngreso {
    
    @ApiObjectField(name = "idSystem", description = "El id del Sistema Central", order = 1)
    private int idSystem;
    @ApiObjectField(name = "demographic", description = "Arreglo de demograficos a homologar", order = 2)
    private List<RequestDemographicIngreso> demographic;
    @ApiObjectField(name = "field", description = "Ambito", order = 3)
    private String field;

    public RequestHomologationDemographicIngreso(int idSystem, List<RequestDemographicIngreso> demographic) {
        this.idSystem = idSystem;
        this.demographic = demographic;
    }

    public RequestHomologationDemographicIngreso() {
    }

    public int getIdSystem() {
        return idSystem;
    }

    public void setIdSystem(int idSystem) {
        this.idSystem = idSystem;
    }

    public List<RequestDemographicIngreso> getDemographic() {
        return demographic;
    }

    public void setDemographic(List<RequestDemographicIngreso> demographic) {
        this.demographic = demographic;
    }

    @Override
    public String toString() {
        return "RequestHomologationIngreso{" + "idSystem=" + idSystem + ", demographic=" + demographic + '}';
    }

    public String getField()
    {
        return field;
    }

    public void setField(String field)
    {
        this.field = field;
    }
}
