package net.cltech.enterprisent.domain.integration.middleware;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase para la importacion de demográficos al Middleware
 *
 * @author JDuarte
 * @version 1.0.0
 * @since 20/01/2019
 * @see Creación
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiObject(
        group = "Prueba",
        name = "Demograficos al Middleware",
        description = "Representa un demografico"
)
public class DemographicMiddleware {

    @ApiObjectField(name = "id", description = "El id LIS del demográfico", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "El nombre LIS del demográfico", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "source", description = "El origen del demográfico, siendo H - Historia, O - Orden", required = true, order = 3)
    private String source;
    @ApiObjectField(name = "codified", description = "Si el demográfico es codificado o no", required = true, order = 4)
    private boolean codified;
    @ApiObjectField(name = "active", description = "El estado del demográfico en el LIS", required = true, order = 5)
    private boolean active;

    public DemographicMiddleware() {

    }
    
    public DemographicMiddleware(Integer id, String name, String source, boolean codified, boolean active)
    {
        this.id = id;
        this.name = name;
        this.source = source;
        this.codified = codified;
        this.active = active;
    }

    public DemographicMiddleware(Integer id) {
        this.id = id;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isCodified() {
        return codified;
    }

    public void setCodified(boolean codified) {
        this.codified = codified;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


}