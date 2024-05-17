/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.securitynt.domain.masters.user;

import net.cltech.securitynt.domain.common.AuthorizedUser;
import net.cltech.securitynt.domain.masters.test.Area;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la combinación entre área y usuario
 *
 * @version 1.0.0
 * @author cmartin
 * @since 09/05/2017
 * @see Creacion
 */
@ApiObject(
        group = "Usuario",
        name = "Area por usuario",
        description = "Representa la combinación entre área y usuario"
)
public class AreaByUser
{

    @ApiObjectField(name = "access", description = "Acceso", required = false, order = 1)
    private boolean access;
    @ApiObjectField(name = "validate", description = "Validar", required = true, order = 2)
    private boolean validate;
    @ApiObjectField(name = "user", description = "Usuario", required = true, order = 3)
    private AuthorizedUser user;
    @ApiObjectField(name = "area", description = "Area", required = true, order = 4)
    private Area area;

    public AreaByUser()
    {
        user = new AuthorizedUser();
        area = new Area();
    }

    public boolean isAccess()
    {
        return access;
    }

    public void setAccess(boolean access)
    {
        this.access = access;
    }

    public boolean isValidate()
    {
        return validate;
    }

    public void setValidate(boolean validate)
    {
        this.validate = validate;
    }

    public AuthorizedUser getUser()
    {
        return user;
    }

    public void setUser(AuthorizedUser user)
    {
        this.user = user;
    }

    public Area getArea()
    {
        return area;
    }

    public void setArea(Area area)
    {
        this.area = area;
    }
}
