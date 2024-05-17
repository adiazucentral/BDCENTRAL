package net.cltech.enterprisent.domain.integration.skl;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.cltech.enterprisent.domain.operation.orders.Result;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa las pruebas de una orden con consentimiento
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 05/06/2020
 * @see Creación
 */
@ApiObject(
        group = "SKL",
        name = "Prueba Basica",
        description = "Prueba con consentimiento informado en la orden"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestConsent
{
    @ApiObjectField(name = "id", description = "Id de la prueba", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo de la prueba", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "abbr", description = "Abreviatura de la prueba", required = true, order = 3)
    private String abbr;
    @ApiObjectField(name = "name", description = "Nombre de la prueba", required = true, order = 4)
    private String name;
    @ApiObjectField(name = "section", description = "Nombre de la sección", required = true, order = 5)
    private String section;
    @ApiObjectField(name = "profile", description = "perfil", required = true, order = 6)
    private Integer profile;
    @ApiObjectField(name = "type", description = "tipo de prueba", required = true, order = 7)
    private Integer type;
    @ApiObjectField(name = "dependentTest", description = "Examenes dependientes", required = true, order = 8)
    private Integer dependentTest;
    @ApiObjectField(name = "result", description = "Resultado", required = true, order = 9)
    private Result result;
    
    public TestConsent()
    {
        this.result = new Result();
    }

    public TestConsent(Integer id, String code, String abbr, String name, String section, Integer profile, Integer type, Integer dependentTest, Result result)
    {
        this.id = id;
        this.code = code;
        this.abbr = abbr;
        this.name = name;
        this.section = section;
        this.profile = profile;
        this.type = type;
        this.dependentTest = dependentTest;
        this.result = result;
    }
    
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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

    public String getSection()
    {
        return section;
    }

    public void setSection(String section)
    {
        this.section = section;
    }
    
    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }
    
    public Integer getProfile()
    {
        return profile;
    }

    public void setProfile(Integer profile)
    {
        this.profile = profile;
    }
    
    public Integer getDependentTest()
    {
        return dependentTest;
    }

    public void setDependentTest(Integer dependentTest)
    {
        this.dependentTest = dependentTest;
    }

    public Result getResult()
    {
        return result;
    }

    public void setResult(Result result)
    {
        this.result = result;
    }
}
