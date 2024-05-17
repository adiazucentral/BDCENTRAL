/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.ingreso;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import lombok.Data;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la clase que controla los estados enviados por examen 
 * 
 * @version 1.0.0
 * @author omendez
 * @since 08/08/2022
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name =  "Estado de examenes enviados",
        description = "Controla los estados enviados por examen"
)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestTestStatus {
    
    @ApiObjectField(name = "order", description = "Id de la orden", order = 1)
    private Long order;
    @ApiObjectField(name = "accountNumber", description = "Número de cuenta", order = 2)
    private String accountNumber;
    @ApiObjectField(name = "homologationCode", description = "Código de homologación", order = 3)
    private String homologationCode;
    @ApiObjectField(name = "status", description = "Estado", order = 4)
    private String status;
    @ApiObjectField(name = "sendingDate", description = "Fecha de envio", order = 5)
    private Timestamp sendingDate;
    @ApiObjectField(name = "numberSending", description = "Cantidad de envios", order = 6)
    private Integer numberSending;
    @ApiObjectField(name = "sendWithError", description = "Envio con error 0-> No, 1-> Si", order = 7)
    private Integer sendWithError;
    @ApiObjectField(name = "action", description = "Envio con error 0-> insertar , 1-> actualizar", order = 8)
    private Integer action;
}
