/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.domain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de un correo
 *
 * @version 1.0.0
 * @author jrodriguez
 * @since 04/12/2018
 * @see Creación
 */
@ApiObject(
        group = "Correo",
        name = "Correo",
        description = "Datos correspondientes al envio de correo"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Email
{

    @ApiObjectField(name = "recipients", description = "Destinatarios del correo", required = true, order = 1)
    private List<String> recipients;
    @ApiObjectField(name = "subject", description = "Asunto del correo", required = true, order = 2)
    private String subject;
    @ApiObjectField(name = "body", description = "Contenido del correo", required = true, order = 3)
    private String body;
    @ApiObjectField(name = "attachment", description = "Lista de imagenes ha enviar", required = false, order = 4)
    private List<Image> attachment;

    public List<String> getRecipients()
    {
        return recipients;
    }

    public void setRecipients(List<String> recipients)
    {
        this.recipients = recipients;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public List<Image> getAttachment()
    {
        return attachment;
    }

    public void setAttachment(List<Image> attachment)
    {
        this.attachment = attachment;
    }

}
