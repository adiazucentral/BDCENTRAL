/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.pathology;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa Diseñador de códigos de barras
 *
 * @version 1.0.0
 * @author omendez
 * @since 10/05/2021
 * @see Creacion
 */
@ApiObject(
        group = "Herramientas - Patologia",
        name = "Diseñador Barras",
        description = "Representa informacion del diseñador de códigos de barras"
)
public class BarcodePathologyDesigner 
{
     @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "template", description = "Plantilla del diseñador", required = true, order = 2)
    private String template;
    @ApiObjectField(name = "active", description = "Indica si el registro se encuentra activo", required = true, order = 3)
    private boolean active;
    @ApiObjectField(name = "version", description = "Version de la etiqueta para el codigo de barras", required = true, order = 4)
    private Integer version;
    @ApiObjectField(name = "command", description = "Commando epl/zpl para la impresión del código de barras", required = true, order = 5)
    private String command;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}



