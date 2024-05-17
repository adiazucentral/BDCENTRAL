/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.integration.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un paciente enviado de sitio externo
 *
 * @author oarango
 * @since 2022-04-14
 * @see Creación
 */
@ApiObject(
        name = "Paciente",
        group = "Orden",
        description = "Paciente de sitio externo"
)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientDto
{
     @ApiObjectField(name = "record", description = "Historia del paciente", required = true)
    private String record;
    @ApiObjectField(name = "name", description = "Nombre del paciente", required = true)
    private String name;
    @ApiObjectField(name = "lastName", description = "Apellido del paciente", required = true)
    private String lastName;
    @ApiObjectField(name = "secondLastName", description = "Segundo apellido del paciente")
    private String secondLastName;
    @ApiObjectField(name = "gender", description = "Género del paciente", required = true)
    private int gender;
    @ApiObjectField(name = "documentType", description = "Tipo de documento del paciente")
    private DocumentType documentType = new DocumentType();
    
    @ApiObjectField(name = "birthdate", description = "Fecha de nacimiento del paciente", required = true)
    private String birthdate;
    @ApiObjectField(name = "comment", description = "Comentario del paciente")
    private String comment;
    @ApiObjectField(name = "demographics", description = "Demográficos del paciente", required = true)
    private List<DemographicDto> demographics;

    public PatientDto()
    {
    }

    public PatientDto(String record, String name, String lastName)
    {
        this.record = record;
        this.name = name;
        this.lastName = lastName;
    }
}
