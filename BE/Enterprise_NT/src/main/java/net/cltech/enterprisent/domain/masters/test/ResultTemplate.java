package net.cltech.enterprisent.domain.masters.test;

import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro plantilla resultados
 *
 * @author eacuna
 * @since 31/07/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Resultado Plantilla",
        description = "Almacena posibles resultados de una opción"
)
@Getter
@Setter
public class ResultTemplate
{

    @ApiObjectField(name = "result", description = "Resultado de la opción", required = true, order = 1)
    private String result;
    @ApiObjectField(name = "reference", description = "Valor de referencia", required = true, order = 2)
    private boolean reference;
    @ApiObjectField(name = "sort", description = "Orden de inserción", required = true, order = 3)
    private int sort;
    @ApiObjectField(name = "comment", description = "Comentario", required = true, order = 4)
    private String comment;

    public ResultTemplate()
    {
    }

    public ResultTemplate(String result)
    {
        this.result = result;
    }

    public ResultTemplate(String result, boolean reference)
    {
        this.result = result;
        this.reference = reference;
    }
}
