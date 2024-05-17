package net.cltech.enterprisent.domain.integration.middleware;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase para el envio de mensajes ASTM al Middleware
 *
 * @version 1.0.0
 * @author Julian
 * @since 21/08/2020
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Envio de mensaje ASTM al Middleware",
        description = "Representa un registro sobre la tabla cont02"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendAstmMiddleware
{

    @ApiObjectField(name = "laboratoryRoute", description = "Ruta del laboratorio sobre la cual se escribira ese archivo plano", order = 1)
    private String laboratoryRoute;
    @ApiObjectField(name = "restServiceRoute", description = "Ruta del servicio que consumiremos", order = 2)
    private String restServiceRoute;
    @ApiObjectField(name = "messageASTM", description = "Mensaje ASTM registrado para el envío al middleware", order = 3)
    private String messageASTM;
    @ApiObjectField(name = "indicator", description = "Indicador de envio. 0 -> Sin enviar, 1 -> Enviado", order = 4)
    private Integer indicator;

    public SendAstmMiddleware()
    {
    }

    public String getLaboratoryRoute()
    {
        return laboratoryRoute;
    }

    public void setLaboratoryRoute(String laboratoryRoute)
    {
        this.laboratoryRoute = laboratoryRoute;
    }

    public String getRestServiceRoute()
    {
        return restServiceRoute;
    }

    public void setRestServiceRoute(String restServiceRoute)
    {
        this.restServiceRoute = restServiceRoute;
    }

    public String getMessageASTM()
    {
        return messageASTM;
    }

    public void setMessageASTM(String messageASTM)
    {
        this.messageASTM = messageASTM;
    }

    public Integer getIndicator()
    {
        return indicator;
    }

    public void setIndicator(Integer indicator)
    {
        this.indicator = indicator;
    }
}
