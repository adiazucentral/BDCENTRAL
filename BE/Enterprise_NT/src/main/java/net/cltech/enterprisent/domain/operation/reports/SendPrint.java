/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la clase el envio a imprimir
 *
 * @version 1.0.0
 * @author equijano
 * @since 07/05/2019
 * @see Creación
 */
@ApiObject(
        group = "Operación - Informes",
        name = "Enviar a imprimir",
        description = "Representa el objeto de envio a imprimir."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SendPrint
{

    @ApiObjectField(name = "json", description = "Serial unico", order = 1)
    private String json;
    @ApiObjectField(name = "serial", description = "Serial unico", order = 2)
    private String serial;
    @ApiObjectField(name = "type", description = "Tipo de impresion", order = 3)
    private int type;

    public String getJson()
    {
        return json;
    }

    public void setJson(String json)
    {
        this.json = json;
    }

    public String getSerial()
    {
        return serial;
    }

    public void setSerial(String serial)
    {
        this.serial = serial;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

}
