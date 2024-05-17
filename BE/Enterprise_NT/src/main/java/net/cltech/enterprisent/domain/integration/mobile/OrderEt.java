package net.cltech.enterprisent.domain.integration.mobile;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa una orden para la app móvil
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 21/08/2020
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Orden AppMovil",
        description = "Representa una orden del paciente para la app Móvil"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderEt
{

    @ApiObjectField(name = "order", description = "Usuario - Paciente - email", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "date", description = "Ultima Contraseña de usuario", required = true, order = 2)
    private Date date;
    @ApiObjectField(name = "type", description = "Usuario - Paciente - email", required = true, order = 3)
    private String type;

    public OrderEt()
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

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

}
