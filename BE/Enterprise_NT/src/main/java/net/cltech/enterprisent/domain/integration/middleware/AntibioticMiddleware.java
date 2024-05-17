package net.cltech.enterprisent.domain.integration.middleware;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa Antibioticos sin auditoria
 * 
 * @version 1.0.0
 * @author javila
 * @since 20/01/2020
 * @see Creación
 */
@ApiObject(
        group = "Middleware",
        name = "Antibiotico",
        description = "Representa antibioticos"
)
public class AntibioticMiddleware {
    
    @ApiObjectField(name = "id", description = "El id LIS del antibiótico", required =  false, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "El nombre LIS del antibiótico", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "active", description = "El estado del antibiótico en el LIS", required = true, order = 3)
    private boolean active;

    public AntibioticMiddleware() {
    }
    
    public AntibioticMiddleware(Integer id, String name, boolean active) {
        this.id = id;
        this.name = name;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
