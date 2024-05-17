package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la hoja de trabajo asignada a un examen
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 09/10/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultado",
        name = "Hoja de Trabajo",
        description = "Representa la hoja de trabajo a la que pertenece un examen"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Worklist
{

    @ApiObjectField(name = "id", description = "Id de la hoja de trabajo", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "consecutive", description = "Número consecutivo de la hoja de trabajo", required = false, order = 2)
    private Integer consecutive;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getConsecutive()
    {
        return consecutive;
    }

    public void setConsecutive(Integer consecutive)
    {
        this.consecutive = consecutive;
    }

}
