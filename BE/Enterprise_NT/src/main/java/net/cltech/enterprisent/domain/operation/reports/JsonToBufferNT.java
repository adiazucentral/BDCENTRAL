package net.cltech.enterprisent.domain.operation.reports;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Objeto que representa un json que contiene una llave data
 * la cual sera convertida a buffer
 * 
 * @version 1.0.0
 * @author Julian
 * @since 11/05/2020
 * @see Creación
 */

@ApiObject(
        group = "Operación - Informes",
        name = "De Json a Buffer",
        description = "Representa un json que contiene una llave data la cual sera convertida a buffer"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonToBufferNT
{
    @ApiObjectField(name = "type", description = "Tipo de dato", order = 1)
    private String type;
    @ApiObjectField(name = "data", description = "Arreglo de datos", order = 2)
    private byte[] data;

    public JsonToBufferNT()
    {
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public byte[] getData()
    {
        return data;
    }

    public void setData(byte[] data)
    {
        this.data = data;
    }
}
