/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.statusBandReason;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import lombok.Data;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *Representa a un paciente,estadoBanda y Motivo
 * @version 1.0.0
 * @author deicy
 */


@ApiObject(
    group = "Banda Transportadora",
    name = "Paciente Banda motivo",
    description = "Representa un paciente, Estado de la banda y Motivo"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class StatusBandReasonUser {
    
    
    
    @ApiObjectField(name = "userid", description = "Id del usuario", order = 1)
    private Integer userid;
    @ApiObjectField(name = "userName", description = "Descripcion del comentario", order = 2)
    private String userName;
    @ApiObjectField(name = "idreason", description = "Id del Motivo", order = 3)
    private Integer idreason;
    @ApiObjectField(name = "reasonName", description = "Nombre del Motivo", order = 4)
    private String reasonName;
    @ApiObjectField(name = "descriptionReason", description = "Descripcion del Motivo", order = 5)
    private String descriptionReason;
    @ApiObjectField(name = "registrationDate", description = "Fecha de Registro", order = 6)
    private Timestamp registrationDate;
    @ApiObjectField(name = "commentary", description = "Descripcion del comentario", order = 7)
    private String commentary;
    


    
}
