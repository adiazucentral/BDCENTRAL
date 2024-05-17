/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.PathologyAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro de configuración de tipos de estudio de Patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 26/10/2020
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Tipo de Estudio",
        description = "Muestra informacion del maestro de configuración de tipos de estudio de patologia usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudyType extends PathologyAudit
{
    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Código", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "status", description = "Estado", required = true, order = 4)
    private Integer status;
    @ApiObjectField(name = "studies", description = "Lista de estudios", required = true, order = 7)
    private List<Study> studies;
   
    public StudyType()
    {
        studies = new ArrayList<>();
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public List<Study> getStudies() {
        return studies;
    }

    public void setStudies(List<Study> studies) {
        this.studies = studies;
    }
    
    public Integer isState()
    {
        return status;
    }
}
