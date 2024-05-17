package net.cltech.enterprisent.domain.integration.middleware;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa el examen que es una alicuota con su respectivo
*
* @version 1.0.0
* @author Julian
* @since 11/09/2020
* @see 
*/

@ApiObject(
        group = "Middleware",
        name = "Alicuota y volumen",
        description = "Representa un examen que es una alicuota y contiene el respectivo volumen de cada una de ellas"
)
public class AlicuotaWithVolum 
{
    @ApiObjectField(name = "idTest", description = "id del examen (Alicuota)", required = true, order = 1)
    private Integer idTest;
    @ApiObjectField(name = "volumen", description = "Volumen de la alicuota", required = true, order = 2)
    private Double volumen;

    public AlicuotaWithVolum()
    {
    }
    
    public Integer getIdTest()
    {
        return idTest;
    }

    public void setIdTest(Integer idTest)
    {
        this.idTest = idTest;
    }

    public Double getVolumen()
    {
        return volumen;
    }

    public void setVolumen(Double volumen)
    {
        this.volumen = volumen;
    }
}