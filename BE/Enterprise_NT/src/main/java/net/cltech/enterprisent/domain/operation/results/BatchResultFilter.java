package net.cltech.enterprisent.domain.operation.results;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import net.cltech.enterprisent.domain.operation.common.Filter;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con filtros para busquedas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 08/11/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Comunes",
        name = "Filtro Resultados en Lote",
        description = "Representa filtro con parametros para busquedas."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BatchResultFilter extends Filter
{

    @ApiObjectField(name = "result", description = "Resultado del examen", order = 0)
    private String result;
    @ApiObjectField(name = "comment", description = "Comentario", order = 1)
    private String comment;
    @ApiObjectField(name = "test", description = "Id del examen", order = 2)
    private Integer test;
    @ApiObjectField(name = "resultEnglish", description = "Resultado del examen en ingles", order = 3)
    private String resultEnglish;
    @ApiObjectField(name = "filterByDemo", description = "Filtro por demográficos", required = false, order = 19)
    private List<FilterDemographic> filterByDemo;

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public Integer getTest()
    {
        return test;
    }

    public void setTest(Integer test)
    {
        this.test = test;
    }

    public String getResultEnglish()
    {
        return resultEnglish;
    }

    public void setResultEnglish(String resultEnglish)
    {
        this.resultEnglish = resultEnglish;
    }

    public List<FilterDemographic> getFilterByDemo()
    {
        return filterByDemo;
    }

    public void setFilterByDemo(List<FilterDemographic> filterByDemo)
    {
        this.filterByDemo = filterByDemo;
    }
}
