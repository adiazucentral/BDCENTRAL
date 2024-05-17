/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.statusBandReason;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;
import java.util.Date;
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
public class StatusBandReason {
    
    @ApiObjectField(name = "id", description = "Id", order = 1)
    private Integer id;
    @ApiObjectField(name = "userid", description = "Id del usuario", order = 2)
    private Integer userid;
    @ApiObjectField(name = "registrationDate", description = "Fecha de Registro", order = 3)
    private Timestamp registrationDate;
    @ApiObjectField(name = "idstatusBand", description = "id del Estado de la Banda", order = 4)
    private Integer idstatusBand;
    @ApiObjectField(name = "idreason", description = "Id del Motivo", order = 5)
    private Integer idreason;
    @ApiObjectField(name = "commentary", description = "Descripcion del comentario", order = 6)
    private String commentary;

    
       
    
}
