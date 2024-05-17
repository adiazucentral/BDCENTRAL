/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de los examenes con muestras de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 20/10/2020
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Estudio",
        description = "Muestra informacion de los examenes con muestras de patologia que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Study extends MasterAudit
{
     @ApiObjectField(name = "id", description = "Id de la prueba", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo de la prueba", required = true, order = 3)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre de la prueba", required = true, order = 4)
    private String name;
    @ApiObjectField(name = "abbr", description = "Abreviatura de la prueba", required = true, order = 5)
    private String abbr;
    @ApiObjectField(name = "sample", description = "Muestra de la prueba", required = true, order = 6)
    private Integer sample;
    @ApiObjectField(name = "sampleName", description = "Nombre de la muestra", required = false, order = 7)
    private String sampleName;
    
    public Study()
    {
    }
    
    public Study(Integer id)
    {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setAbbr(String abbr) {
        this.abbr = abbr;
    }

    public Integer getSample() {
        return sample;
    }

    public void setSample(Integer sample) {
        this.sample = sample;
    } 

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }
}
