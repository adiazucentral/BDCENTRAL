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
* Representa el cambio de estado de una sede gestionadas desde el HIS
*
* @version 1.0.0
* @author adiaz
* @since 27/04/2021
* @see Creación
*/

@ApiObject(
        group = "Integración",
        name = "HIS",
        description = "Estado de la sede"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BranchHisState {
    @ApiObjectField(name = "estado", description = "Estado de la sede", required = false, order = 1)
    private int estado;
    @ApiObjectField(name = "codigo", description = "Codigo de la sede", required = false, order = 2)
    private String codigo;

    public int getState() {
        return estado;
    }

    public void setState(int estado) {
        this.estado = estado;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
}
