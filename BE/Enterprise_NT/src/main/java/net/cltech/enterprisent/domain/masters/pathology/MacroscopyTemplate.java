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
 * Clase que representa la informacion para las plantillas de patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 09/06/2021
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Plantilla",
        description = "Muestra informacion del maestro para las plantillas de patologia que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MacroscopyTemplate extends PathologyAudit
{
    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "specimen", description = "Muestra", required = true, order = 2)
    private Specimen specimen = new Specimen();
    @ApiObjectField(name = "fields", description = "Campos", required = true, order = 3)
    private List<Field> fields = new ArrayList<>();
    
    public MacroscopyTemplate() {
    }
    
    public MacroscopyTemplate(Integer id)
    {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Specimen getSpecimen() {
        return specimen;
    }

    public void setSpecimen(Specimen specimen) {
        this.specimen = specimen;
    }

    public List<Field> getFields() {
        return fields;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }
}
