package net.cltech.securitynt.domain.common;

import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la respuesta de un token delicencia de la aplicacion
 *
 * @version 1.0.0
 * @author equijano
 * @since 25/10/2019
 * @see Creación
 */
public class LicenseResponse
{

    @ApiObjectField(name = "errorCode", description = "Respuesta true -> aceptada false -> no aceptada", required = true, order = 1)
    private String errorCode;
    @ApiObjectField(name = "message", description = "Cadena de token", required = true, order = 2)
    private String message;
    @ApiObjectField(name = "date", description = "Fecha en la que se consulta la licencia", required = true, order = 3)
    private String date;

    public String getErrorCode()
    {
        return errorCode;
    }

    public void setErrorCode(String errorCode)
    {
        this.errorCode = errorCode;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

}
