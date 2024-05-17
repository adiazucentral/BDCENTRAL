/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.widgets;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un examen para el registro de resultados
 *
 * @version 1.0.0
 * @author jblanco
 * @since 02/07/2017
 * @see Creación
 */
@ApiObject(
        group = "Widgets",
        name = "Widgets - Conteo Estados",
        description = "Representa el conteo de ordenes para cada estado (Resultado, VAlidacion y Prevalidación)."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StateCount
{

    @ApiObjectField(name = "result", description = "# de ordenes pendiente de resultado", required = true, order = 1)
    private long result;
    @ApiObjectField(name = "preview", description = "# de ordenes pendiente de prevalidación", required = true, order = 2)
    private long preview;
    @ApiObjectField(name = "validation", description = "# de ordenes pendiente de validación", required = true, order = 3)
    private long validation;
    @ApiObjectField(name = "total", description = "# de ordenes totales", required = true, order = 4)
    private long total;

    public StateCount()
    {
    }

    public long getResult()
    {
        return result;
    }

    public void setResult(long result)
    {
        this.result = result;
    }

    public long getPreview()
    {
        return preview;
    }

    public void setPreview(long preview)
    {
        this.preview = preview;
    }

    public long getValidation()
    {
        return validation;
    }

    public void setValidation(long validation)
    {
        this.validation = validation;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

}
