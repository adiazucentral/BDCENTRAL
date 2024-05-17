package net.cltech.enterprisent.domain.integration.middleware;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa una informacion de los demograficos extendida
 * 
 * @version 1.0.0
 * @author javila
 * @since 22/01/2020
 * @see Creación
 */
@ApiObject(
        group = "Middleware",
        name = "Demográfico ítem",
        description = "Muestra informacion de los demograficos item"
)
public class DemographicItemMiddleware {
    
     @ApiObjectField(name = "id", description = "El id LIS del demográfico ítem", required = false, order = 1)
     private Integer id;
     @ApiObjectField(name = "code", description = "El código LIS del demográfico ítem", required = true, order = 2)
     private String code;
     @ApiObjectField(name = "name", description = "El nombre LIS del demográfico ítem", required = true, order = 3)
     private String name;
     @ApiObjectField(name = "active", description = "El estado del demográfico ítem en el LIS", required = true, order = 4)
     private boolean active;

    public DemographicItemMiddleware() {
    }

    public DemographicItemMiddleware(Integer id, String code, String name, boolean active) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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
}
