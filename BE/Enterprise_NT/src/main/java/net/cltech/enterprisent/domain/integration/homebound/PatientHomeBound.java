package net.cltech.enterprisent.domain.integration.homebound;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el objeto requerido por HomeBound
 * 
 * @version 1.0.0
 * @author Julian
 * @since 2020/06/08
 * @see Creación
 */

@ApiObject(
        group = "HomeBound",
        name = "Paciente",
        description = "Datos de un paciente"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class PatientHomeBound
{
    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "documentType", description = "Tipo de Documento", required = true, order = 2)
    private DocumentTypeHomeBound documentType;
    @ApiObjectField(name = "patientId", description = "Historia", required = true, order = 3)
    private String patientId;
    @ApiObjectField(name = "lastName", description = "Apellido", required = true, order = 4)
    private String lastName;
    @ApiObjectField(name = "surName", description = "Segundo Apellido", required = true, order = 5)
    private String surName;
    @ApiObjectField(name = "name1", description = "Nombre", required = true, order = 6)
    private String name1;
    @ApiObjectField(name = "name2", description = "Segundo Nombre", required = true, order = 7)
    private String name2;
    @ApiObjectField(name = "birthday", description = "Fecha de nacimiento", required = true, order = 8)
    private Date birthday;
    @ApiObjectField(name = "sex", description = "Sexo", required = true, order = 9)
    private GenderHomeBound sex;
    @ApiObjectField(name = "email", description = "Correo Electronico", required = true, order = 10)
    private String email;
    @ApiObjectField(name = "diagnosis", description = "Diagnostico permanente", required = true, order = 11)
    private String diagnosis;
    @ApiObjectField(name = "passwordTemp", description = "Contraseña temporal", required = false, order = 12)
    private boolean passwordTemp;
    @ApiObjectField(name = "demographics", description = "Demograficos", required = false, order = 13)
    private List<DemographicHomeBound> demographics;
    @ApiObjectField(name = "password", description = "Contraseña", required = false, order = 14)
    private String password;
    @ApiObjectField(name = "photoBase64", description = "Foto del paciente", required = true, order = 15)
    private String photoBase64;
}
