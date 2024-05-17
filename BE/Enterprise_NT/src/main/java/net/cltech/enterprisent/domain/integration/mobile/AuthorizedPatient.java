package net.cltech.enterprisent.domain.integration.mobile;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa el usuario autorizado para ingresar a la app móvil
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 19/08/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Paciente Autorizado",
        description = "Representa un usuario autorizado para el sistema"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizedPatient
{

    @ApiObjectField(name = "id", description = "Id de base de datos del paciente", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "patientId", description = "Historia del paciente", required = true, order = 2)
    private String patientId;
    @ApiObjectField(name = "lastName", description = "Apellido del paciente", required = true, order = 3)
    private String lastName;
    @ApiObjectField(name = "surName", description = "Segundo Apellido del paciente.", required = true, order = 4)
    private String surName;
    @ApiObjectField(name = "name1", description = " Nombre del paciente", required = true, order = 5)
    private String name1;
    @ApiObjectField(name = "email", description = " Email del paciente", required = true, order = 6)
    private String email;
    @ApiObjectField(name = "temporalPassword", description = "Si el paciente tiene una contraseña temporal", required = false, order = 7)
    private boolean temporalPassword;

    public AuthorizedPatient()
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

    public String getPatientId()
    {
        return patientId;
    }

    public void setPatientId(String patientId)
    {
        this.patientId = patientId;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getSurName()
    {
        return surName;
    }

    public void setSurName(String surName)
    {
        this.surName = surName;
    }

    public String getName1()
    {
        return name1;
    }

    public void setName1(String name1)
    {
        this.name1 = name1;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public boolean isTemporalPassword()
    {
        return temporalPassword;
    }

    public void setTemporalPassword(boolean temporalPassword)
    {
        this.temporalPassword = temporalPassword;
    }

}
