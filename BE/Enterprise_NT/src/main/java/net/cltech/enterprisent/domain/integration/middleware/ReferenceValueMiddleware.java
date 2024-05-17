package net.cltech.enterprisent.domain.integration.middleware;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.math.BigDecimal;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el objeto de respuesta que se le enviara al middleware
 * con los respectivos valores de referencia
 * 
 * @version 1.0.0
 * @author Julian
 * @since 05/05/2020
 * @see Creación
 */

@ApiObject(
        group = "Middleware",
        name = "ReferenceValueMiddleware",
        description = "Representa un objeto que contendra valores de referencia solicitados por el middleware"
)
public class ReferenceValueMiddleware
{
    @ApiObjectField(name = "analyzerName", description = "Nombre de la conexión (usuario) del analizador. Null si es un valor de referencia por defecto", required = false, order = 1)
    private String analyzerName;
    @ApiObjectField(name = "gender", description = "Género en inglés del paciente a evaluar. Male, Female, Both", required = false, order = 2)
    private String gender;
    @ApiObjectField(name = "age", description = "Unidad en inglés de edad del paciente a evaluar. Years, Months o Days", required = false, order = 3)
    private String age;
    @ApiObjectField(name = "minAge", description = "Valor mínimo del campo age", required = false, order = 4)
    private Integer minAge;
    @ApiObjectField(name = "maxAge", description = "Valor máximo del campo age", required = false, order = 5)
    private Integer maxAge;
    @ApiObjectField(name = "minReference", description = "Valor de referencia mínimo cuando el resultado del examen es numérico", required = false, order = 6)
    private BigDecimal minReference;
    @ApiObjectField(name = "maxReference", description = "Valor de referencia maximo cuando el resultado del examen es numérico", required = false, order = 7)
    private BigDecimal maxReference;
    @ApiObjectField(name = "minPanic", description = "Valor de pánico mínimo cuando el resultado del examen es numérico", required = false, order = 8)
    private BigDecimal minPanic;
    @ApiObjectField(name = "maxPanic", description = "Valor de pánico maximo cuando el resultado del examen es numérico", required = false, order = 9)
    private BigDecimal maxPanic;
    @ApiObjectField(name = "textReference", description = "Valor de resultado literal cuando el resultado del examen es texto", required = false, order = 10)
    private String textReference;
    @ApiObjectField(name = "id", description = "Id del valor de referencia", required = false, order = 10)
    private Integer id;

    public ReferenceValueMiddleware()
    {
    }

    public String getAnalyzerName()
    {
        return analyzerName;
    }

    public void setAnalyzerName(String analyzerName)
    {
        this.analyzerName = analyzerName;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getAge()
    {
        return age;
    }

    public void setAge(String age)
    {
        this.age = age;
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

    public BigDecimal getMinReference()
    {
        return minReference;
    }

    public void setMinReference(BigDecimal minReference)
    {
        this.minReference = minReference;
    }

    public BigDecimal getMaxReference()
    {
        return maxReference;
    }

    public void setMaxReference(BigDecimal maxReference)
    {
        this.maxReference = maxReference;
    }

    public BigDecimal getMinPanic()
    {
        return minPanic;
    }

    public void setMinPanic(BigDecimal minPanic)
    {
        this.minPanic = minPanic;
    }

    public BigDecimal getMaxPanic()
    {
        return maxPanic;
    }

    public void setMaxPanic(BigDecimal maxPanic)
    {
        this.maxPanic = maxPanic;
    }

    public String getTextReference()
    {
        return textReference;
    }

    public void setTextReference(String textReference)
    {
        this.textReference = textReference;
    }
    
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }
}
