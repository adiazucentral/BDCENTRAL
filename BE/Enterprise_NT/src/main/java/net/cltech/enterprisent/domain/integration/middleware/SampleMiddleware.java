package net.cltech.enterprisent.domain.integration.middleware;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Objects;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase para la importacion de muestra al middleware
 *
 * @author JDuarte
 * @version 1.0.0
 * @since 15/01/2019
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Muestra al Middleware",
        description = "Representa una Muestra"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleMiddleware {

    @ApiObjectField(name = "id", description = "El id Lis de la muestra", order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "El codigo Lis muestra", order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "El nombre Lis de la muestra", order = 3)
    private String name;
    @ApiObjectField(name = "microbiology", description = "Si la muestra es de microbiologia", order = 4)
    private Boolean microbiology;
    @ApiObjectField(name = "active", description = "El estado de la muestra en el Lis", order = 5)
    private Boolean active;  
    
    
    

    public SampleMiddleware() {
        
        
    }

    public SampleMiddleware(Integer id) {

       this();
       this.id = id;
    }

    public SampleMiddleware(SampleMiddleware sampleMiddleware) {
        this();
        this.id = sampleMiddleware.getId();
        this.code = sampleMiddleware.getCode();
        this.name = sampleMiddleware.getName();
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
    
    public Boolean getMicrobiology() {
        return microbiology;
    }

    public void setMicrobiology(Boolean microbiology) {
        this.microbiology = microbiology;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
    
      @Override
    public int hashCode()
    {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final SampleMiddleware other = (SampleMiddleware) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }
    

    

}
