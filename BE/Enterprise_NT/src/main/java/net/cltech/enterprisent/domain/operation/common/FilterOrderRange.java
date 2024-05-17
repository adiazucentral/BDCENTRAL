/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con filtros para busquedas
 *
 * @version 1.0.0
 * @author jvellojin
 * @since 14/02/2021
 * @see Creación
 */
@ApiObject(
        group = "Operación - Comunes",
        name = "Filtro Busquedas",
        description = "Representa filtro con parametros para busquedas."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FilterOrderRange
{

    @ApiObjectField(name = "init", description = "Rango inicial", order = 2)
    private Long init;
    @ApiObjectField(name = "end", description = "Rango final", order = 3)
    private Long end;

    public FilterOrderRange()
    {
    }

    public FilterOrderRange(Long init, Long end)
    {
        this.init = init;
        this.end = end;
    }

    public Long getInit()
    {
        return init;
    }

    public void setInit(Long init)
    {
        this.init = init;
    }

    public Long getEnd()
    {
        return end;
    }

    public void setEnd(Long end)
    {
        this.end = end;
    }

}
