/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.audit;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un objeto con la informacion rips de caja
 *
 * @version 1.0.0
 * @author adiaz
 * @since 26/03/2021
 * @see Creacion
 */
@ApiObject(
        group = "Auditoria",
        name = "Caja RIPS",
        description = "Representa un objeto con la informacion de la auditoria"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuditGeneral {
    @ApiObjectField(name = "accion", description = "Accion que se realiza en la auditoria", required = true, order = 1)
    private String accion;
    @ApiObjectField(name = "code", description = "Accion que se realiza en la auditoria", required = true, order = 1)
    private String code;

  
}
