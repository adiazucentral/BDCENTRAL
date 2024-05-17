/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.user;

import net.cltech.enterprisent.domain.masters.test.CentralSystem;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Homologación de usuarios
 *
 * @version 1.0.0
 * @author cmartin
 * @since 25/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Usuario",
        name = "Homologación de Usuarios",
        description = "Muestra el maestro homologación de usuarios."
)
public class StandardizationUser extends CentralSystem
{

    @ApiObjectField(name = "user", description = "Usuario", required = true, order = 1)
    private User user;
    @ApiObjectField(name = "centralCode", description = "Codigo Central", required = true, order = 1)
    private String centralCode;

    public StandardizationUser()
    {
        user = new User();
    }

    public User getUserStandardization()
    {
        return user;
    }

    public void setUserStandardization(User user)
    {
        this.user = user;
    }

    public String getCentralCode()
    {
        return centralCode;
    }

    public void setCentralCode(String centralCode)
    {
        this.centralCode = centralCode;
    }
    
    

}
