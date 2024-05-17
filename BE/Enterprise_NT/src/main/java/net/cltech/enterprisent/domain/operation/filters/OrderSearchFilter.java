package net.cltech.enterprisent.domain.operation.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import net.cltech.enterprisent.domain.operation.list.FilterDemographic;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con filtros para busquedas de ordenes
 *
 * @version 1.0.0
 * @author eacuna
 * @since 04/12/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Filtros",
        name = "Busquedas Ordenes",
        description = "Representa filtro con parametros para busquedas de ordenes."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderSearchFilter
{

    @ApiObjectField(name = "rangeType", description = "Rango de busqueda: 0 -> Fecha<br> 1->Orden", order = 1)
    private Integer rangeType;
    @ApiObjectField(name = "init", description = "Rango inicial", order = 2)
    private Long init;
    @ApiObjectField(name = "end", description = "Rango final", order = 3)
    private Long end;
    @ApiObjectField(name = "demographics", description = "Lista de filtro por demograficos", order = 4)
    private List<FilterDemographic> demographics;
    @ApiObjectField(name = "sections", description = "Lista de filtro por secciones", order = 5)
    private List<Integer> sections;
    @ApiObjectField(name = "tests", description = "Lista de filtro por examenes", order = 6)
    private List<Integer> tests;
    @ApiObjectField(name = "record", description = "Numero identificacion del paciente", order = 7)
    private String record;
    @ApiObjectField(name = "documentType", description = "Tipo de documento", order = 8)
    private Integer documentType;
    @ApiObjectField(name = "lastName", description = "Apellido del paciente (obligatorio el genero)", order = 9)
    private String lastName;
    @ApiObjectField(name = "gender", description = "Id genero (Listados) ", order = 10)
    private Integer gender;

    public OrderSearchFilter()
    {
    }

    public OrderSearchFilter(Integer rangeType, Long init, Long end)
    {
        this.rangeType = rangeType;
        this.init = init;
        this.end = end;
    }

    public Integer getRangeType()
    {
        return rangeType;
    }

    public void setRangeType(Integer rangeType)
    {
        this.rangeType = rangeType;
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

    public List<FilterDemographic> getDemographics()
    {
        return demographics;
    }

    public void setDemographics(List<FilterDemographic> demographics)
    {
        this.demographics = demographics;
    }

    public List<Integer> getSections()
    {
        return sections;
    }

    public void setSections(List<Integer> sections)
    {
        this.sections = sections;
    }

    public List<Integer> getTests()
    {
        return tests;
    }

    public void setTests(List<Integer> tests)
    {
        this.tests = tests;
    }

    public String getRecord()
    {
        return record;
    }

    public void setRecord(String record)
    {
        this.record = record;
    }

    public Integer getDocumentType()
    {
        return documentType;
    }

    public void setDocumentType(Integer documentType)
    {
        this.documentType = documentType;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public Integer getGender()
    {
        return gender;
    }

    public void setGender(Integer gender)
    {
        this.gender = gender;
    }

    public static int RANGE_TYPE_DATE = 0;
    public static int RANGE_TYPE_ORDER = 1;

}
