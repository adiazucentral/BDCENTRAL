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
        name = "Objeto de Muestras para examenes",
        description = "Representa el objeto de la muestra para examenes"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Sample
{

    @ApiObjectField(name = "id", description = "id de la muestra para Examenes", required = true, order = 1)
    private int id;
    @ApiObjectField(name = "code", description = "Codigo de la muestra para Examenes", required = true, order = 1)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre de la muestra para Examenes", required = true, order = 1)
    private String name;

    public Sample()
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
