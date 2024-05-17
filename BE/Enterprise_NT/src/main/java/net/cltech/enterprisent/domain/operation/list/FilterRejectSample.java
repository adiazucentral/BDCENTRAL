/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.list;

import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con filtros para busquedas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 08/09/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Listados",
        name = "Filtro Busquedas - Muestra",
        description = "Representa filtro con parametros para busqueda de muestras rechazadas."
)
public class FilterRejectSample
{

    @ApiObjectField(name = "init", description = "Rango inicial", order = 1)
    private Long init;
    @ApiObjectField(name = "end", description = "Rango final", order = 2)
    private Long end;
    @ApiObjectField(name = "rejectSample", description = "El filtro es de muestras rechazadas", order = 3)
    private boolean rejectSample;
    @ApiObjectField(name = "branch", description = "Sede: 0 -> Todas", order = 4)
    private Integer branch;
    @ApiObjectField(name = "samples", description = "Muestras", order = 4)
    private List<Integer> samples;

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

    public boolean isRejectSample()
    {
        return rejectSample;
    }

    public void setRejectSample(boolean rejectSample)
    {
        this.rejectSample = rejectSample;
    }

    public Integer getBranch()
    {
        return branch;
    }

    public void setBranch(Integer branch)
    {
        this.branch = branch;
    }

    public List<Integer> getSamples()
    {
        return samples;
    }

    public void setSamples(List<Integer> samples)
    {
        this.samples = samples;
    }

}
