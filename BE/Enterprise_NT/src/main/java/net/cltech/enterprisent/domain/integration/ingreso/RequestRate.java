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
        name = "Objeto Tarifa a homologar",
        description = "Obtendra la petición de test a homologar"
)
public class RequestRate
{

    @ApiObjectField(name = "id", description = "El id de la tarifa", order = 1)
    private Integer id;

    public RequestRate()
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

}
