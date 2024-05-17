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
        name = "Objeto Muestra a homologar",
        description = "Obtendra muestra a homologar"
)
public class RequestSample
{

    @ApiObjectField(name = "id", description = "El id de la muestra", order = 1)
    private Integer id;
    @ApiObjectField(name = "codesample", description = "Codigo de la muestra", order = 2)
    private String codesample;

    public RequestSample()
    {
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCodesample()
    {
        return codesample;
    }

    public void setCodesample(String codesample)
    {
        this.codesample = codesample;
    }

}
