package net.cltech.enterprisent.domain.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.Item;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que represent un resultado
 *
 * @author Equijano
 * @version 1.0.0
 * @since 17/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Listados",
        name = "Informacion de Examen de Laboratorio",
        description = "Representa un la informacion de un Examen"
)
@JsonInclude(Include.NON_NULL)
public class TestInformation
{

    @ApiObjectField(name = "id", description = "Id de la prueba", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "area", description = "Area ", required = true, order = 2)
    private Area area;
    @ApiObjectField(name = "code", description = "Codigo", required = true, order = 3)
    private String code;
    @ApiObjectField(name = "abbr", description = "Abreviatura", required = true, order = 4)
    private String abbr;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 5)
    private String name;
    @ApiObjectField(name = "testType", description = "Tipo de la prueba: 0 -> Examen, 1 -> Perfil, 2 -> Paquete", required = true, order = 6)
    private Short testType;
    @ApiObjectField(name = "unit", description = "Unidad", required = false, order = 7)
    private Unit unit;
    @ApiObjectField(name = "listSample", description = "Lista de Muestra", required = true, order = 8)
    private List<Sample> listSample;
    @ApiObjectField(name = "automaticResult", description = "Resultado automatico de la prueba", required = true, order = 9)
    private String automaticResult;
    @ApiObjectField(name = "tests", description = "Lista de examenes para perfiles /paquetes", required = true, order = 10)
    private List<TestInformation> tests;
    @ApiObjectField(name = "technique", description = "Tecnica", required = false, order = 11)
    private Technique technique;
    @ApiObjectField(name = "gender", description = "Genero de la prueba", required = true, order = 12)
    private Item gender;
    @ApiObjectField(name = "minAge", description = "Edad minima de la prueba", required = true, order = 13)
    private Integer minAge;
    @ApiObjectField(name = "maxAge", description = "Edad maxima de la prueba", required = true, order = 14)
    private Integer maxAge;
    @ApiObjectField(name = "unitAge", description = "Unidad edad de la prueba", required = true, order = 15)
    private Short unitAge;
    @ApiObjectField(name = "resultType", description = "Tipo de Resultado: 1->Numerico, 2->Texto", required = false, order = 16)
    private Integer resultType;
    @ApiObjectField(name = "processingDays", description = "Dias de procesamiento de la prueba", required = true, order = 17)
    private String processingDays;
    @ApiObjectField(name = "resultRequest", description = "Solicitar resultado en ingreso de ordenes", required = true, order = 18)
    private boolean resultRequest;
    @ApiObjectField(name = "generalInformation", description = "Comentario informacion general de la prueba", required = true, order = 19)
    private String generalInformation;
    @ApiObjectField(name = "requirements", description = "Requerimientos de la prueba", required = true, order = 20)
    private List<Requirement> requirements;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Area getArea()
    {
        return area;
    }

    public void setArea(Area area)
    {
        this.area = area;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getAbbr()
    {
        return abbr;
    }

    public void setAbbr(String abbr)
    {
        this.abbr = abbr;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Short getTestType()
    {
        return testType;
    }

    public void setTestType(Short testType)
    {
        this.testType = testType;
    }

    public Unit getUnit()
    {
        return unit;
    }

    public void setUnit(Unit unit)
    {
        this.unit = unit;
    }

    public List<Sample> getListSample()
    {
        return listSample;
    }

    public void setListSample(List<Sample> listSample)
    {
        this.listSample = listSample;
    }

    public String getAutomaticResult()
    {
        return automaticResult;
    }

    public void setAutomaticResult(String automaticResult)
    {
        this.automaticResult = automaticResult;
    }

    public List<TestInformation> getTests()
    {
        return tests;
    }

    public void setTests(List<TestInformation> tests)
    {
        this.tests = tests;
    }

    public Technique getTechnique()
    {
        return technique;
    }

    public void setTechnique(Technique technique)
    {
        this.technique = technique;
    }

    public Item getGender()
    {
        return gender;
    }

    public void setGender(Item gender)
    {
        this.gender = gender;
    }

    public Integer getMinAge()
    {
        return minAge;
    }

    public void setMinAge(Integer minAge)
    {
        this.minAge = minAge;
    }

    public Integer getMaxAge()
    {
        return maxAge;
    }

    public void setMaxAge(Integer maxAge)
    {
        this.maxAge = maxAge;
    }

    public Short getUnitAge()
    {
        return unitAge;
    }

    public void setUnitAge(Short unitAge)
    {
        this.unitAge = unitAge;
    }

    public Integer getResultType()
    {
        return resultType;
    }

    public void setResultType(Integer resultType)
    {
        this.resultType = resultType;
    }

    public String getProcessingDays()
    {
        return processingDays;
    }

    public void setProcessingDays(String processingDays)
    {
        this.processingDays = processingDays;
    }

    public boolean isResultRequest()
    {
        return resultRequest;
    }

    public void setResultRequest(boolean resultRequest)
    {
        this.resultRequest = resultRequest;
    }

    public String getGeneralInformation()
    {
        return generalInformation;
    }

    public void setGeneralInformation(String generalInformation)
    {
        this.generalInformation = generalInformation;
    }

    public List<Requirement> getRequirements()
    {
        return requirements;
    }

    public void setRequirements(List<Requirement> requirements)
    {
        this.requirements = requirements;
    }

}
