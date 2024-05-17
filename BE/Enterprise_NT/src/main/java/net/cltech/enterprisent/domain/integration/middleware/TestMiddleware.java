package net.cltech.enterprisent.domain.integration.middleware;

import java.util.Objects;
import net.cltech.enterprisent.domain.masters.test.Sample;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase para la importación de exámenes al Middleware
 *
 * @author JDuarte
 * @version 1.0.0
 * @since 16/01/2019
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Examen al Middleware",
        description = "Representa una examen"
)
public class TestMiddleware {

    @ApiObjectField(name = "id", description = "El id LIS del examen", order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "El código LIS del examen", order = 2)
    private String code;
    @ApiObjectField(name = "abbr", description = "La abreviatura el examen", order = 3)
    private String abbr;
    @ApiObjectField(name = "name", description = "El nombre LIS del examen", order = 4)
    private String name;
    @ApiObjectField(name = "decimals", description = "Los decimales para los resultados del examen", order = 5)
    private Integer decimals;
    @ApiObjectField(name = "type", description = "El tipo de resultado para el examen", order = 6)
    private Integer type;
    @ApiObjectField(name = "active", description = "El estado del examen en el LIS", order = 7)
    private Boolean active;
    @ApiObjectField(name = "sample", description = "Un objeto del tipo muestra (descrito anteriormente), con la información de 	la muestra del examen", order = 8)
    private SampleMiddleware sample;

     public TestMiddleware() {
       
       sample = new SampleMiddleware();
        
    }
    
    
    public TestMiddleware(Integer id) {
       
       this.id = id;
        
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

    public String getAbbr() {
        return abbr;
    }

    
    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDecimals() {
        return decimals;
    }

    public void setDecimals(Integer decimals) {
        this.decimals = decimals;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

   
    public SampleMiddleware getSample() {
        return sample;
    }

    public void setSample(SampleMiddleware sample) {
        this.sample = sample;
    }

    

}
