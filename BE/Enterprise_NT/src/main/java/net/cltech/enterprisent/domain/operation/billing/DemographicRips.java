/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.billing;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase para busquedas por demografico en configuracion rips
 *
 * @version 1.0.0
 * @author omendez
 * @since 22/01/2021
 * @see Creación
 */
@ApiObject(
        group = "Facturacion",
        name = "Demograficos RIPS",
        description = " Representa clase para busquedas por demografico en configuracion rips."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DemographicRips 
{
    @ApiObjectField(name = "demographic", description = "Id demográfico : 0 - No filtrar", order = 1)
    private Integer demographic;
    @ApiObjectField(name = "origin", description = "Origen", required = true, order = 2)
    private String origin;
    @ApiObjectField(name = "name", description = "Nombre del demografico", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "encoded", description = "Tipo del demografico", required = true, order = 4)
    private boolean encoded;
    @ApiObjectField(name = "value", description = "Valor del demografico para rips", required = true, order = 5)
    private String value;
    @ApiObjectField(name = "ripsProperty", description = "Propiedad RIPS", required = true, order = 6)
    private String ripsProperty;
    @ApiObjectField(name = "codedemographic", description = "codigo del demografico para rips", required = true, order = 7)
    private String codedemographic;

    public Integer getDemographic() {
        return demographic;
    }

    public void setDemographic(Integer demographic) {
        this.demographic = demographic;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEncoded() {
        return encoded;
    }

    public void setEncoded(boolean encoded) {
        this.encoded = encoded;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getRipsProperty() {
        return ripsProperty;
    }

    public void setRipsProperty(String ripsProperty) {
        this.ripsProperty = ripsProperty;
    }

    public String getCodedemographic() {
        return codedemographic;
    }

    public void setCodedemographic(String codedemographic) {
        this.codedemographic = codedemographic;
    }
    
    
}
