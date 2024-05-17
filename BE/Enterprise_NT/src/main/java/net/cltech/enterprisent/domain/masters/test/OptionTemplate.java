package net.cltech.enterprisent.domain.masters.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
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
        name = "Opción Plantilla",
        description = "Almacena las opciones de la plantilla"
)
public class OptionTemplate extends MasterAudit
{

    @ApiObjectField(name = "idTest", description = "id del examen", required = true, order = 1)
    private Integer idTest;
    @ApiObjectField(name = "id", description = "id", required = true, order = 2)
    private Integer id;
    @ApiObjectField(name = "option", description = "Nombre opción de la plantilla", required = true, order = 3)
    private String option;
    @ApiObjectField(name = "comment", description = "Comentario de la opción", required = true, order = 4)
    private String comment;
    @ApiObjectField(name = "result", description = "Resultado", required = true, order = 5)
    private String result;
    @ApiObjectField(name = "normalResult", description = "Resultado Normal", required = true, order = 6)
    private String normalResult;
    @ApiObjectField(name = "sort", description = "Orden de inserción", required = true, order = 7)
    private int sort;
    @ApiObjectField(name = "results", description = "Lista de items de la opción", required = true, order = 8)
    private List<ResultTemplate> results;
    @ApiObjectField(name = "order", description = "Numero de la Orden", required = true, order = 9)
    private Long order;
    @ApiObjectField(name = "testName", description = "Nombre del examen", required = false, order = 10)
    private String testName;

    public OptionTemplate()
    {
        results = new ArrayList<>();
    }

    public String getOption()
    {
        return option;
    }

    public void setOption(String option)
    {
        this.option = option;
    }

    public String getComment()
    {
        return comment;
    }

    public void setComment(String comment)
    {
        this.comment = comment;
    }

    public int getSort()
    {
        return sort;
    }

    public void setSort(int sort)
    {
        this.sort = sort;
    }

    public List<ResultTemplate> getResults()
    {
        return results;
    }

    public void setResults(List<ResultTemplate> results)
    {
        this.results = results;
    }

    public String getResult()
    {
        return result;
    }

    public void setResult(String result)
    {
        this.result = result;
    }

    public String getNormalResult()
    {
        return normalResult;
    }

    public void setNormalResult(String normalResult)
    {
        this.normalResult = normalResult;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getIdTest()
    {
        return idTest;
    }

    public void setIdTest(Integer idTest)
    {
        this.idTest = idTest;
    }

    public Long getOrder()
    {
        return order;
    }

    public void setOrder(Long order)
    {
        this.order = order;
    }

    public String getTestName()
    {
        return testName;
    }

    public void setTestName(String testName)
    {
        this.testName = testName;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final OptionTemplate other = (OptionTemplate) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }

}
