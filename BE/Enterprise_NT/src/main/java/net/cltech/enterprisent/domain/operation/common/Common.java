package net.cltech.enterprisent.domain.operation.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con filtros para busquedas
 *
 * @version 1.0.0
 * @author eacuna
 * @since 08/09/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Comunes",
        name = "Objeto general",
        description = "Representa objeto comun."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Common
{

    @ApiObjectField(name = "number", description = "Número", order = 1)
    private Integer number;
    @ApiObjectField(name = "date", description = "Fecha", order = 2)
    private Date date;
    @ApiObjectField(name = "user", description = "Usuario", order = 2)
    private AuthorizedUser user;

    public Integer getNumber()
    {
        return number;
    }

    public void setNumber(Integer number)
    {
        this.number = number;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public AuthorizedUser getUser()
    {
        return user;
    }

    public void setUser(AuthorizedUser user)
    {
        this.user = user;
    }

}
