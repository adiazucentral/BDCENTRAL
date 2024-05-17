package net.cltech.enterprisent.domain.integration.ingreso;

import java.util.ArrayList;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase para la xportacion de demograficos por sistema central a homologar
 *
 * @author BValero
 * @version 1.0.0
 * @since 22/01/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Demograficos por systema central homologados",
        description = "Representa los demograficos del LIS homologados"
)
public class ResponsHomologationDemographicIngreso {
    
    @ApiObjectField(name = "idSytem", description = "El id del Sistema Central", order = 1)
    private int idSystem;
    @ApiObjectField(name = "demographic", description = "Arreglo de demograficos homologados", order = 2)
    private List<ResponsDemographicIngreso> demographic;

    public ResponsHomologationDemographicIngreso(int idSystem, List<ResponsDemographicIngreso> demographic) {
        this.idSystem = idSystem;
        this.demographic = demographic;
    }

    public ResponsHomologationDemographicIngreso() {
        this.idSystem = 0;
        this.demographic = new ArrayList<>();       
    }

    public int getIdSystem() {
        return idSystem;
    }

    public void setIdSystem(int idSystem) {
        this.idSystem = idSystem;
    }

    public List<ResponsDemographicIngreso> getDemographic() {
        return demographic;
    }

    public void setDemographic(List<ResponsDemographicIngreso> demographic) {
        this.demographic = demographic;
    }

    @Override
    public String toString() {
        return "ResponsHomologationIngreso{" + "idSystem=" + idSystem + ", demographic=" + demographic + '}';
    }

}
