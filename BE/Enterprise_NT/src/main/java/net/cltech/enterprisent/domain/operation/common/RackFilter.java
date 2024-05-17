package net.cltech.enterprisent.domain.operation.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con filtros para gradillas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 01/06/2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Comunes",
        name = "Filtro Gradillas",
        description = "Representa filtro con parametros para busquedas."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RackFilter
{

    @ApiObjectField(name = "type", description = "Tipo de Gradilla: 0 -> No filtra<br> 1 -> General<br>2->Pendientes<br>3->Confidenciales", order = 1)
    private int type;
    @ApiObjectField(name = "state", description = "Estado: -1 -> No filtra<br> 0->Abierta<br> 1->Cerrada<br> 2->Desechada", order = 2)
    private int state;
    @ApiObjectField(name = "branch", description = "Filtra por sede: 0-> No filtra,<br> -1-> Filtra por la sede del token<br> Id de la sede  ", order = 3)
    private Integer branch;

    public RackFilter()
    {
    }

    public RackFilter(int type, int state, Integer branch)
    {
        this.type = type;
        this.state = state;
        this.branch = branch;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int state)
    {
        this.state = state;
    }

    public Integer getBranch()
    {
        return branch;
    }

    public void setBranch(Integer branch)
    {
        this.branch = branch;
    }

}
