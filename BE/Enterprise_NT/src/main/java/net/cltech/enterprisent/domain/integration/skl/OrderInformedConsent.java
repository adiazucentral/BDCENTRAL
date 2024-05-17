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
        name = "Orden Consentimiento Informado",
        description = "Orden Consentimiento Informado"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderInformedConsent
{

    @ApiObjectField(name = "answer", description = "Objeto InformedConsentAnswer", required = true, order = 1)
    private InformedConsentAnswer answer;
    @ApiObjectField(name = "document", description = "Documento", required = true, order = 2)
    private String document;

    public OrderInformedConsent()
    {
    }

    public InformedConsentAnswer getAnswer()
    {
        return answer;
    }

    public void setAnswer(InformedConsentAnswer answer)
    {
        this.answer = answer;
    }

    public String getDocument()
    {
        return document;
    }

    public void setDocument(String document)
    {
        this.document = document;
    }

}
