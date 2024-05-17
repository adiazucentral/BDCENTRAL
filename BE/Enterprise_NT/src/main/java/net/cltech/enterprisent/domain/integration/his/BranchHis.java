/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.his;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la sede gestionadas desde el HIS
*
* @version 1.0.0
* @author adiaz
* @since 27/04/2021
* @see Creación
*/

@ApiObject(
        group = "Integración",
        name = "HIS",
        description = "sede"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BranchHis {
    @ApiObjectField(name = "id", description = "identificador de la sede", required = false, order = 1)
    private int id;
    @ApiObjectField(name = "codigo", description = "Codigo de la sede", required = false, order = 2)
    private String codigo;
    @ApiObjectField(name = "nombre", description = "Nombre de la sede", required = true, order = 3)
    private String nombre;
    @ApiObjectField(name = "abreviatura", description = "abreviatura de la sede", required = true, order = 4)
    private String abreviatura;
    @ApiObjectField(name = "direccion", description = "Direccion de la sede", required = true, order = 5)
    private String direccion;
    @ApiObjectField(name = "telefono", description = "Telefono de la sede", required = true, order = 6)
    private String telefono;

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    
}
