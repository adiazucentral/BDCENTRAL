package net.cltech.enterprisent.domain.integration.mobile;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa cambio de clave del paciente de la aplicacion para la app móvil
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 20/08/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Representa cambio de clave del paciente",
        description = "Realiza el cambio de contraseña del paciente en la aplicación"
)
public class ChangePassword
{

    @ApiObjectField(name = "id", description = "id del paciente de la base de datos", order = 1)
    private Integer id;
    @ApiObjectField(name = "password", description = "Contraseña del paciente", order = 2)
    private String password;
    @ApiObjectField(name = "correct", description = "True o False indicando si se hizo el cambio de contraseña", order = 3)
    private boolean correct;

    public ChangePassword()
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

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean isCorrect()
    {
        return correct;
    }

    public void setCorrect(boolean correct)
    {
        this.correct = correct;
    }

}
