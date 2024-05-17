package net.cltech.enterprisent.domain.operation.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.ArrayList;
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
        name = "Estadisticas",
        description = "Representa filtro con parametros para busquedas en estadisticas."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatisticFilter
{

    @ApiObjectField(name = "rangeType", description = "Rango de busqueda: 0 -> Fecha Ingreso<br> 1->Fecha Validación<br> 2-> Rango de ordenes", order = 1)
    private Integer rangeType;
    @ApiObjectField(name = "init", description = "Rango inicial", order = 2)
    private Long init;
    @ApiObjectField(name = "end", description = "Rango final", order = 3)
    private Long end;
    @ApiObjectField(name = "testState", description = "Estado del examenes: 1 -> Examenes Ordenados, 2 -> Examenes Validados, 3 -> Examenes Impresos.", order = 4)
    private Integer testState;
    @ApiObjectField(name = "demographics", description = "Lista de filtro por demograficos", order = 5)
    private List<FilterDemographic> demographics = new ArrayList<>();
    @ApiObjectField(name = "areas", description = "Lista de filtro por secciones", order = 6)
    private List<Integer> areas;
    @ApiObjectField(name = "levels", description = "Lista de filtro por niveles del examen", order = 7)
    private List<Integer> levels;
    @ApiObjectField(name = "laboratories", description = "Lista de filtro por examenes", order = 8)
    private List<Integer> laboratories;
    @ApiObjectField(name = "tests", description = "Lista de filtro por examenes", order = 9)
    private List<Integer> tests;
    //Microbiologia
    @ApiObjectField(name = "microorganisms", description = "Lista de filtro por microorganismos", order = 10)
    private List<Integer> microorganisms;
    @ApiObjectField(name = "antibiotics", description = "Lista de filtro por antibioticos", order = 11)
    private List<Integer> antibiotics;
    //Estadisticas Especiales
    @ApiObjectField(name = "resultType", description = "Tipo de Resultado: 0 -> Numerico<br>1 -> Literal<br>2 -> Comentario", order = 12)
    private Integer resultType;
    @ApiObjectField(name = "test", description = "Examen", order = 13)
    private Integer test;
    @ApiObjectField(name = "gender", description = "Genero", order = 14)
    private Integer gender;
    @ApiObjectField(name = "ageMin", description = "Edad Minima", order = 15)
    private Integer ageMin;
    @ApiObjectField(name = "ageMax", description = "Edad Maxima", order = 16)
    private Integer ageMax;
    @ApiObjectField(name = "refMin", description = "Referencia Minima", order = 17)
    private BigDecimal refMin;
    @ApiObjectField(name = "refMax", description = "Referencia Maxima", order = 18)
    private BigDecimal refMax;
    @ApiObjectField(name = "result", description = "Resultado", order = 19)
    private String result;
    @ApiObjectField(name = "comment", description = "Comentario", order = 20)
    private String comment;
    @ApiObjectField(name = "samples", description = "Lista de filtro por muestras", order = 21)
    private List<Integer> samples;
    //Oportunidad
    @ApiObjectField(name = "opportunityTime", description = "Filtro de exámenes que superen este tiempo en minutos desde el ingreso hasta la validación.", order = 22)
    private Integer opportunityTime;
    @ApiObjectField(name = "startDate", description = "Indica si se calcula el tiempo total desde 1.Verificacion Muestra, 2.Ingreso de la orden, por defecto 1", order = 23)
    private Integer startDate;
    @ApiObjectField(name = "samplesDestiny", description = "Lista de filtro por destinos de la muestra", order = 24)
    private List<Integer> samplesDestiny;
    @ApiObjectField(name = "groupProfiles", description = "Indica si se va a agrupar por perfiles", order = 25)
    private boolean groupProfiles;
    @ApiObjectField(name = "typeGrouping", description = "Indica el tipo de agrupacion para los tiempos promedio.", order = 26)
    private Integer typeGrouping;
    @ApiObjectField(name = "filterType", description = "Indica el tipo de filtro", order = 27)
    private Integer filterType;
    @ApiObjectField(name = "testFilterType", description = "Filtro por examen: 0 - Todos<br> 1 - Sección<br> 2-Examen<br> 3-Confidenciales", order = 28)
    private int testFilterType;
    @ApiObjectField(name = "listtestState", description = "Lista de estados a filtrar Estado del examenes: 1 -> Examenes Ordenados, 2 -> Examenes Validados, 3 -> Examenes Impresos.", order = 4)
    private List<Integer> listTestState;

    public List<Integer> getListTestState()
    {
        return listTestState;
    }

    public void setListTestState(List<Integer> listTestState)
    {
        this.listTestState = listTestState;
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

    public Integer getTestState()
    {
        return testState;
    }

    public void setTestState(Integer testState)
    {
        this.testState = testState;
    }

    public List<FilterDemographic> getDemographics()
    {
        return demographics;
    }

    public void setDemographics(List<FilterDemographic> demographics)
    {
        this.demographics = demographics;
    }

    public List<Integer> getAreas()
    {
        return areas;
    }

    public void setAreas(List<Integer> areas)
    {
        this.areas = areas;
    }

    public List<Integer> getLevels()
    {
        return levels;
    }

    public void setLevels(List<Integer> levels)
    {
        this.levels = levels;
    }

    public List<Integer> getLaboratories()
    {
        return laboratories;
    }

    public void setLaboratories(List<Integer> laboratories)
    {
        this.laboratories = laboratories;
    }

    public List<Integer> getTests()
    {
        return tests;
    }

    public void setTests(List<Integer> tests)
    {
        this.tests = tests;
    }

    public Integer getResultType()
    {
        return resultType;
    }

    public void setResultType(Integer resultType)
    {
        this.resultType = resultType;
    }

    public Integer getTest()
    {
        return test;
    }

    public void setTest(Integer test)
    {
        this.test = test;
    }

    public Integer getGender()
    {
        return gender;
    }

    public void setGender(Integer gender)
    {
        this.gender = gender;
    }

    public Integer getAgeMin()
    {
        return ageMin;
    }

    public void setAgeMin(Integer ageMin)
    {
        this.ageMin = ageMin;
    }

    public Integer getAgeMax()
    {
        return ageMax;
    }

    public void setAgeMax(Integer ageMax)
    {
        this.ageMax = ageMax;
    }

    public BigDecimal getRefMin()
    {
        return refMin;
    }

    public void setRefMin(BigDecimal refMin)
    {
        this.refMin = refMin;
    }

    public BigDecimal getRefMax()
    {
        return refMax;
    }

    public void setRefMax(BigDecimal refMax)
    {
        this.refMax = refMax;
    }

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

    public List<Integer> getSamples()
    {
        return samples;
    }

    public void setSamples(List<Integer> samples)
    {
        this.samples = samples;
    }

    public Integer getOpportunityTime()
    {
        return opportunityTime;
    }

    public void setOpportunityTime(Integer opportunityTime)
    {
        this.opportunityTime = opportunityTime;
    }

    public Integer getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Integer startDate)
    {
        this.startDate = startDate;
    }

    public List<Integer> getSamplesDestiny()
    {
        return samplesDestiny;
    }

    public void setSamplesDestiny(List<Integer> samplesDestiny)
    {
        this.samplesDestiny = samplesDestiny;
    }

    public List<Integer> getMicroorganisms()
    {
        return microorganisms;
    }

    public void setMicroorganisms(List<Integer> microorganisms)
    {
        this.microorganisms = microorganisms;
    }

    public List<Integer> getAntibiotics()
    {
        return antibiotics;
    }

    public void setAntibiotics(List<Integer> antibiotics)
    {
        this.antibiotics = antibiotics;
    }

    public boolean isGroupProfiles()
    {
        return groupProfiles;
    }

    public void setGroupProfiles(boolean groupProfiles)
    {
        this.groupProfiles = groupProfiles;
    }

    public Integer getTypeGrouping()
    {
        return typeGrouping;
    }

    public void setTypeGrouping(Integer typeGrouping)
    {
        this.typeGrouping = typeGrouping;
    }

    public Integer getFilterType() {
        return filterType;
    }

    public void setFilterType(Integer filterType) {
        this.filterType = filterType;
    }

    public int getTestFilterType() {
        return testFilterType;
    }

    public void setTestFilterType(int testFilterType) {
        this.testFilterType = testFilterType;
    }

    public static final int FILTER_TEST_ALL = 1;
    public static final int FILTER_TEST_VALID_PRINTED = 2;
    public static final int FILTER_TEST_PRINTED = 3;
    public static final int FILTER_TEST_NOT_VALID = 4;
    public static final int FILTER_TEST_RESULT = 5;
    public static final int FILTER_TEST_VALID = 6;

}
