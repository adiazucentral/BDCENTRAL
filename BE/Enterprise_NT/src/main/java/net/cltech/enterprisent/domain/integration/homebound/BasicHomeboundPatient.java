package net.cltech.enterprisent.domain.integration.homebound;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa a el paciente con sus datos básicos requeridos por Homebound
 * 
 * @version 1.0.0
 * @author javila
 * @since 15/06/2021
 * @see Creación
 */

@ApiObject(
        group = "Homebound",
        name = "Información Básica Del Paciente",
        description = "Representa la información basica de un paciente del LIS."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class BasicHomeboundPatient
{
    @ApiObjectField(name = "history", description = "Historia del paciente (Número De Identificación)", order = 1)
    private String history;
    @ApiObjectField(name = "documentTypeCode", description = "Código del tipo de documento", order = 2)
    private String documentTypeCode;
    @ApiObjectField(name = "firstName", description = "Primer nombre del paciente", order = 3)
    private String firstName;
    @ApiObjectField(name = "secondName", description = "Segundo nombre del paciente", order = 4)
    private String secondName;
    @ApiObjectField(name = "firstSurname", description = "Primer apellido del paciente", order = 5)
    private String firstSurname;
    @ApiObjectField(name = "secondSurname", description = "Segundo apellido del paciente", order = 6)
    private String secondSurname;
    @ApiObjectField(name = "orderNumber", description = "order number", order = 7)
    private Long orderNumber;
}
