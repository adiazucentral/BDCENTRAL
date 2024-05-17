package net.cltech.enterprisent.domain.integration.mobile;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* ¨Representa la actualización del correo electronico de un paciente
*
* @version 1.0.0
* @author Julian
* @since 1/12/2020
* @see Creación
*/

@ApiObject(
        group = "Integración",
        name = "Actualización de correo electronico del paciente",
        description = "Objeto usado para la actualización del correo electrónico del paciente"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PatientEmailUpdate 
{
    @ApiObjectField(name = "id", description = "id del paciente", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "email", description = "Dirección de correo electrónico del paciente", required = true, order = 2)
    private String email;

    public PatientEmailUpdate()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}