package net.cltech.enterprisent.domain.integration.mobile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Agregar una descripción de la clase
 *
 * @version 1.0.0
 * @author nmolina
 * @since 20/08/2020
 * @see Creacion
 */

@Getter
@Setter
@NoArgsConstructor
@ApiObject(
        group = "Itegración",
        name = "Objeto de paciente LisPAtient",
        description = "Representa el objeto de paciente en el lis"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LisPatient
{
    @ApiObjectField(name = "id", description = "id del paciente", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "documentType", description = "Tipo de documento del paciente", required = true, order = 1)
    private String documentType;
    @ApiObjectField(name = "patientId", description = "Numero de historia clinica de paciente", required = true, order = 1)
    private String patientId;
    @ApiObjectField(name = "firstLastname", description = "Primer apellido del paciente", required = true, order = 1)
    private String firsLastname;
    @ApiObjectField(name = "secondLastname", description = "Segundo apellido del paciente", required = true, order = 1)
    private String secondLastname;
    @ApiObjectField(name = "firstName", description = "Primer nombre del paciente", required = true, order = 1)
    private String firstName;
    @ApiObjectField(name = "secondName", description = "Segundo nombre del paciente", required = true, order = 1)
    private String secondName;
    @ApiObjectField(name = "email", description = "Correo electronico del paciente", required = true, order = 1)
    private String email;
    @ApiObjectField(name = "photoBase64", description = "Foto del paciente", required = true, order = 1)
    private String photoBase64;
    @ApiObjectField(name = "passwordTemp", description = "Contraseña temporal", required = true, order = 1)
    private boolean passwordTemp;

   

}
