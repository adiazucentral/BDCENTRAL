package net.cltech.enterprisent.domain.integration.middleware;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa Microorganismos
 * 
 * @version 1.0.0
 * @author javila
 * @since 22/01/2020
 * @see Creación
 */
@ApiObject(
        group = "Middleware",
        name = "Microorganismos al Middleware",
        description = "Representa un microorganismo"
)
public class MicroorganismsMiddleware {
    
    @ApiObjectField(name = "id", description = "El id LIS del microorganismo", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "El nombre LIS del microorganismo", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "active", description = "El estado del microorganismo en el LIS", required = true, order = 3)
    private boolean active;

    public MicroorganismsMiddleware() {
    }

    public MicroorganismsMiddleware(Integer id, String name, boolean active) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
