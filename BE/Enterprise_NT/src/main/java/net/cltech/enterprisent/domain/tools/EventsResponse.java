package net.cltech.enterprisent.domain.tools;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la respuesta basica de un evento
 *
 * @version 1.0.0
 * @author equijano
 * @since 15/01/2018
 * @see Creacion
 */
@ApiObject(
        group = "Herramientas",
        name = "Respuesta de eventos",
        description = "Representa informacion basica de respuesta de un examen"
)
public class EventsResponse
{

    @ApiObjectField(name = "url", description = "Url del metodo", required = true, order = 1)
    private String url;
    @ApiObjectField(name = "response", description = "Respuesta del metodo", required = true, order = 2)
    private String response;

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getResponse()
    {
        return response;
    }

    public void setResponse(String response)
    {
        this.response = response;
    }

}
