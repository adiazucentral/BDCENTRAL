package net.cltech.enterprisent.domain.integration.middleware;

import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el objeto de respuesta de examen que se le enviara al middleware
 * con los respectivos valores de referencia
 * 
 * @version 1.0.0
 * @author Julian
 * @since 05/05/2020
 * @see Creación
 */

@ApiObject(
        group = "Middleware",
        name = "TestToMiddleware",
        description = "Representa un objeto de examen con atributos específicos requeridos por middleware"
)
public class TestToMiddleware
{
    @ApiObjectField(name = "id", description = "El id LIS del examen", required = false, order = 1)
    private int id;
    @ApiObjectField(name = "name", description = "El nombre LIS del examen", required = false, order = 2)
    private String name;
    @ApiObjectField(name = "code", description = "El codigo LIS del examen", required = false, order = 2)
    private String code;
    @ApiObjectField(name = "abbr", description = "La abreviatura del examen en el LIS", required = false, order = 3)
    private String abbr;
    @ApiObjectField(name = "deltaCheck", description = "El objeto que contendra valores de referencia delta", required = false, order = 4)
    private DeltaCheckMiddleware deltaCheck;
    @ApiObjectField(name = "referenceValues", description = "Los valores de referencia del examen en el LIS", required = false, order = 5)
    private List<ReferenceValueMiddleware> referenceValues;
    @ApiObjectField(name = "analyzerReferenceValues", description = "Los valores de referencia del examen por analizador en el LIS", required = false, order = 6)
    private List<ReferenceValueMiddleware> analyzerReferenceValues;

    public TestToMiddleware()
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

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
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

    public DeltaCheckMiddleware getDeltaCheck()
    {
        return deltaCheck;
    }

    public void setDeltaCheck(DeltaCheckMiddleware deltaCheck)
    {
        this.deltaCheck = deltaCheck;
    }

    public List<ReferenceValueMiddleware> getAnalyzerReferenceValues()
    {
        return analyzerReferenceValues;
    }

    public void setAnalyzerReferenceValues(List<ReferenceValueMiddleware> analyzerReferenceValues)
    {
        this.analyzerReferenceValues = analyzerReferenceValues;
    }

    public List<ReferenceValueMiddleware> getReferenceValues()
    {
        return referenceValues;
    }

    public void setReferenceValues(List<ReferenceValueMiddleware> referenceValues)
    {
        this.referenceValues = referenceValues;
    }
}
