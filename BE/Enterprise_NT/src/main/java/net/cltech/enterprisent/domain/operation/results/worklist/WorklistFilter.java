package net.cltech.enterprisent.domain.operation.results.worklist;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.operation.common.Filter;
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
        group = "Operación - Comunes",
        name = "Filtro Busquedas",
        description = "Representa filtro con parametros para busquedas."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WorklistFilter extends Filter
{

    @ApiObjectField(name = "resultPending", description = "Indica si se genera reporte con los pendientes por resultados o todos", order = 1)
    private boolean resultPending;
    @ApiObjectField(name = "workLists", description = "Id´s de hojas de trabajo a filtrar", order = 2)
    private List<Integer> workLists = new ArrayList<>();
      @ApiObjectField(name = "branch", description = "Id de la sede", order = 3)
    private Integer branch;

    public boolean isResultPending()
    {
        return resultPending;
    }

    public void setResultPending(boolean resultPending)
    {
        this.resultPending = resultPending;
    }

    public List<Integer> getWorkLists()
    {
        return workLists;
    }

    public void setWorkLists(List<Integer> workLists)
    {
        this.workLists = workLists;
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
