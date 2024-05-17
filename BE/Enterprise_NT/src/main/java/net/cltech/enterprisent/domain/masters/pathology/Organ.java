package net.cltech.enterprisent.domain.masters.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.cltech.enterprisent.domain.masters.common.PathologyAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro de organos de Patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 08/04/2021
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Organo",
        description = "Muestra información del maestro de organos de patologia que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Organ extends PathologyAudit
{
    @ApiObjectField(name = "id", description = "Id del organo", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Código del organo", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre del organo", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "status", description = "Estado del organo", required = true, order = 4)
    private Integer status;
    @ApiObjectField(name = "selected", description = "Si esta asignado a un patologo", order = 9)
    private boolean selected;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer isState()
    {
        return status;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
