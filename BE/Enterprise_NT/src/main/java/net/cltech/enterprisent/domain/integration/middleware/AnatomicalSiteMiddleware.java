package net.cltech.enterprisent.domain.integration.middleware;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa Sitio Anatomico
 * 
 * @version  1.0.0
 * @author javila
 * @since 21/01/2020
 * @see Creación
 */
@ApiObject(
        group = "Middleware",
        name = "Sitio anatómico",
        description = "Representa un sitio anatómico"
)
public class AnatomicalSiteMiddleware {
    
    @ApiObjectField(name = "id", description = "El id LIS del sitio anatómico", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "El código LIS del sitio anatómico", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "El nombre LIS del sitio anatómico", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "active", description = "El estado del sitio anatómico en el LIS", required = true, order = 4)
    private boolean active;

    public AnatomicalSiteMiddleware() {
    }

    public AnatomicalSiteMiddleware(Integer id, String code, String name, boolean active) {
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
