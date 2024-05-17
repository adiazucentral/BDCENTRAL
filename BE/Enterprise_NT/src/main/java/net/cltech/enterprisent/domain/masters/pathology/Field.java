/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.cltech.enterprisent.domain.masters.common.PathologyAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de los campos para las plantillas de macroscopia
 *
 * @version 1.0.0
 * @author omendez
 * @since 08/06/2021
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Plantilla Macroscopia",
        description = "Muestra informacion del maestro de campos para las plantillas de macroscopia que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Field extends PathologyAudit
{
    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "type", description = "Tipo", required = true, order = 3)
    private Integer type;
    @ApiObjectField(name = "grid", description = "Grilla", required = true, order = 4)
    private Integer grid;
    @ApiObjectField(name = "required", description = "Requerido", required = true, order = 5)
    private Integer required;
    @ApiObjectField(name = "status", description = "Estado", required = true, order = 6)
    private Integer status;
    @ApiObjectField(name = "selected", description = "Si esta asignado a la muestra", order = 7)
    private boolean selected;
    @ApiObjectField(name = "order", description = "Orden", required = true, order = 8)
    private Integer order;
    @ApiObjectField(name = "template", description = "Plantilla", required = true, order = 9)
    private Integer template;
    @ApiObjectField(name = "value", description = "Valor", required = true, order = 10)
    private String value;
    
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getGrid() {
        return grid;
    }

    public void setGrid(Integer grid) {
        this.grid = grid;
    }

    public Integer getRequired() {
        return required;
    }

    public void setRequired(Integer required) {
        this.required = required;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Integer isState()
    {
        return status;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Integer getTemplate() {
        return template;
    }

    public void setTemplate(Integer template) {
        this.template = template;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
