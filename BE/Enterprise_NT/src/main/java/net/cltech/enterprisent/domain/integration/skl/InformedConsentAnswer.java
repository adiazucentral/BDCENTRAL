package net.cltech.enterprisent.domain.integration.skl;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa consentimiento informado
 *
 * @author JDuarte
 * @version 1.0.0
 * @since 08/06/2020
 * @see Creación
 */
@ApiObject(
        group = "SKL",
        name = "Respuesta Consentimiento Informado",
        description = "Respuesta consentimiento informado"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InformedConsentAnswer
{

    @ApiObjectField(name = "order", description = "Numero de orden", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "idTest", description = "Id del examen", required = true, order = 2)
    private int idTest;

    public InformedConsentAnswer()
    {
    }

    public long getOrder()
    {
        return order;
    }

    public void setOrder(long order)
    {
        this.order = order;
    }

    public int getIdTest()
    {
        return idTest;
    }

    public void setIdTest(int idTest)
    {
        this.idTest = idTest;
    }

}
