package net.cltech.enterprisent.domain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de un usuario autorizado
 *
 * @version 1.0.0
 * @author dcortes
 * @since 31/03/2017
 * @see Creación
 */
@ApiObject(
        group = "Usuarios",
        name = "AuthorizedUser",
        description = "Representa un usuario del sistema",
        show = false
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class AuthorizedUser
{
    @ApiObjectField(name = "id", description = "Id del usuario", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "userName", description = "Usuario", required = true, order = 2)
    private String userName;
    @ApiObjectField(name = "lastName", description = "Apellido del usuario", required = true, order = 3)
    private String lastName;
    @ApiObjectField(name = "name", description = "Nombre del usuario", required = true, order = 4)
    private String name;
    @ApiObjectField(name = "passwordExpiration", description = "Fecha de expiracion del usuario", required = true, order = 5)
    private Date passwordExpiration;
    @ApiObjectField(name = "branch", description = "Id de la sede", required = true, order = 6)
    private Integer branch;
    @ApiObjectField(name = "photo", description = "Foto del usuario", required = true, order = 7)
    private String photo;
    @ApiObjectField(name = "confidential", description = "Tiene permisos a examenes confidenciales", required = true, order = 8)
    private boolean confidential;
    @ApiObjectField(name = "administrator", description = "Indica si tiene un rol administrados", required = false, order = 9)
    private boolean administrator;
    @ApiObjectField(name = "type", description = "Tipo de usuario", required = true, order = 10)
    private Integer type;
    @ApiObjectField(name = "maxDiscount", description = "Maximo descuento permitido", required = true, order = 11)
    private Double maxDiscount;
    @ApiObjectField(name = "orderType", description = "Tipo de orden por defecto", required = true, order = 12)
    private Integer orderType;
    @ApiObjectField(name = "licenses", description = "Licencias del producto", required = true, order = 13)
    private HashMap<String, Boolean> licenses;
    @ApiObjectField(name = "updateUserId", description = "Id usuario crea o actualiza", required = false, order = 14)
    private int updateUserId;
    @ApiObjectField(name = "creatingItems", description = "Creación de items en ingreso", order = 15)
    private boolean creatingItems;
    @ApiObjectField(name = "discount", description = "Descuento del usuario para homebound", required = true, order = 16)
    private String discount;
    @ApiObjectField(name = "editOrderCash", description = "Validar si tiene permisos para editar los examenes de una orden con caja", order = 17)
    private boolean editOrderCash;

    public AuthorizedUser(Integer id)
    {
        this.id = id;
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final AuthorizedUser other = (AuthorizedUser) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }
}
