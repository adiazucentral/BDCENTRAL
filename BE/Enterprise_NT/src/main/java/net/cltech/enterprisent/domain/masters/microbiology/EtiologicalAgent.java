/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.microbiology;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa agente etiologico
 *
 * @version 1.0.0
 * @author omendez
 * @since 02/06/2022
 * @see Creacion
 */
@ApiObject(
        group = "Microbiología",
        name = "Agente etiologico",
        description = "Representa agentes etiologicos"
)
public class EtiologicalAgent extends MasterAudit {
    
    @ApiObjectField(name = "id", description = "Identificador autonumerico de base de datos", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "searchBy", description = "Buscar por", required = true, order = 2)
    private Integer searchBy;
    @ApiObjectField(name = "microorganism", description = "Microorganismo", required = true, order = 3)
    private String microorganism;
    @ApiObjectField(name = "code", description = "Código del grupo", required = true, order = 4)
    private String code;
    @ApiObjectField(name = "clasification", description = "Clasificación", required = true, order = 5)
    private Integer clasification;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(Integer searchBy) {
        this.searchBy = searchBy;
    }

    public String getMicroorganism() {
        return microorganism;
    }

    public void setMicroorganism(String microorganism) {
        this.microorganism = microorganism;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getClasification() {
        return clasification;
    }

    public void setClasification(Integer clasification) {
        this.clasification = clasification;
    }
}
