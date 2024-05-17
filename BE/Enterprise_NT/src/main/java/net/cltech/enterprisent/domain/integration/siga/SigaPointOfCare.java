package net.cltech.enterprisent.domain.integration.siga;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa una tarifa para el Siga.
 *
 * @version 1.0.0
 * @author equijano
 * @since 16/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Siga",
        name = "Taquilla",
        description = "Representa el objeto de las taquillas del Siga."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaPointOfCare
{

    @ApiObjectField(name = "id", description = "Id de la taquilla", order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo de la taquilla", order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre de la taquilla", order = 3)
    private String name;
    @ApiObjectField(name = "managePriority", description = "Maneja Prioridad", order = 4)
    private boolean managePriority;
    @ApiObjectField(name = "service", description = "Servicio al cual pertenece la taquilla", order = 5)
    private SigaService service;
    @ApiObjectField(name = "branch", description = "Sede a la cual pertenece la taquilla", order = 6)
    private SigaBranch branch;
    @ApiObjectField(name = "state", description = "Estado del servicio (1->Activo, 2->Inactivo)", order = 7)
    private Integer state;

    public SigaPointOfCare()
    {
    }

    public SigaPointOfCare(Integer id, String code, String name, boolean managePriority, SigaService service, SigaBranch branch, Integer state)
    {
        this.id = id;
        this.code = code;
        this.name = name;
        this.managePriority = managePriority;
        this.service = service;
        this.branch = branch;
        this.state = state;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isManagePriority() {
        return managePriority;
    }

    public void setManagePriority(boolean managePriority) {
        this.managePriority = managePriority;
    }

    public SigaService getService() {
        return service;
    }

    public void setService(SigaService service) {
        this.service = service;
    }

    public SigaBranch getBranch() {
        return branch;
    }

    public void setBranch(SigaBranch branch) {
        this.branch = branch;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

}
