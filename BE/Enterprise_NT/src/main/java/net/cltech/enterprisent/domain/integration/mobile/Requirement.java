package net.cltech.enterprisent.domain.integration.mobile;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Agregar una descripción de la clase
 *
 * @version 1.0.0
 * @author nmolina
 * @since 24/08/2020
 * @see Creacion
 */
@ApiObject(
        group = "Itegración",
        name = "Objeto de Requerimientos para examenes",
        description = "Representa el objeto de requerimiento para examenes"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Requirement
{

    @ApiObjectField(name = "id", description = "id de requerimiento para Examenes", required = true, order = 1)
    private int id;
    @ApiObjectField(name = "code", description = "Codigo de requerimiento para Examenes", required = true, order = 1)
    private String code;
    @ApiObjectField(name = "description", description = "Nombre del requerimiento para Examenes", required = true, order = 1)
    private String description;

    public Requirement()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
