package net.cltech.enterprisent.domain.integration.ingreso;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Recipientes
 *
 * @author JDuarte
 * @version 1.0.0
 * @since 12/05/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Test a homologar",
        description = "Obtendra la petición de test a homologar"
)
public class RequestTest
{

    @ApiObjectField(name = "id", description = "El id del Examen", order = 1)
    private int id;
    @ApiObjectField(name = "code", description = "Codigo del examen", order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre del examen", order = 3)
    private String name;
    @ApiObjectField(name = "testType", description = "Tipo del examen", order = 4)
    private Integer testType;
    @ApiObjectField(name = "rate", description = "Tarifa", order = 5)
    private RequestRate rate;
    @ApiObjectField(name = "area", description = "Area", order = 6)
    private RequestArea area;
    @ApiObjectField(name = "price", description = "El Precio del examen", order = 7)
    private Integer price;
    @ApiObjectField(name = "print", description = "El print del examen", order = 8)
    private Integer print;
    @ApiObjectField(name = "abbr", description = "Abreviatura del examen", order = 9)
    private String abbr;
    @ApiObjectField(name = "sample", description = "Muestra", order = 10)
    private RequestSample sample;
    @ApiObjectField(name = "profile", description = "El perfil del examen", order = 11)
    private Integer profile;
    @ApiObjectField(name = "idLaboratory", description = "id del laboratorio", order = 11)
    private Integer idLaboratory;

    public RequestTest()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Integer getTestType()
    {
        return testType;
    }

    public void setTestType(Integer testType)
    {
        this.testType = testType;
    }

    public RequestRate getRate()
    {
        return rate;
    }

    public void setRate(RequestRate rate)
    {
        this.rate = rate;
    }

    public RequestArea getArea()
    {
        return area;
    }

    public void setArea(RequestArea area)
    {
        this.area = area;
    }

    public Integer getPrice()
    {
        return price;
    }

    public void setPrice(Integer price)
    {
        this.price = price;
    }

    public Integer getPrint()
    {
        return print;
    }

    public void setPrint(Integer print)
    {
        this.print = print;
    }

    public String getAbbr()
    {
        return abbr;
    }

    public void setAbbr(String abbr)
    {
        this.abbr = abbr;
    }

    public RequestSample getSample()
    {
        return sample;
    }

    public void setSample(RequestSample sample)
    {
        this.sample = sample;
    }

    public Integer getProfile()
    {
        return profile;
    }

    public void setProfile(Integer profile)
    {
        this.profile = profile;
    }

    public Integer getIdLaboratory()
    {
        return idLaboratory;
    }

    public void setIdLaboratory(Integer idLaboratory)
    {
        this.idLaboratory = idLaboratory;
    }
}
