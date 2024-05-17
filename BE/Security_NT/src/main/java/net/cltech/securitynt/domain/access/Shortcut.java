package net.cltech.securitynt.domain.access;

import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase para el manejo de accesos directos
 *
 * @author EAcuna
 * @version 1.0.0
 * @since 19/09/2017
 * @see Creación
 */
@ApiObject(
        group = "Común",
        name = "Acceso directo",
        description = "Representa un objeto para almacenar accesos directos"
)
public class Shortcut
{

    @ApiObjectField(name = "module", description = "Id módulo del Sistema", order = 1)
    private int module;
    @ApiObjectField(name = "user", description = "Id del usuario ", order = 2)
    private int user;
    @ApiObjectField(name = "form", description = "Id de la pagina/formulario del acceso directo", order = 3)
    private int form;

    public Shortcut()
    {
    }

    public Shortcut(int module, int user, int form)
    {
        this.module = module;
        this.user = user;
        this.form = form;
    }

    public int getModule()
    {
        return module;
    }

    public void setModule(int module)
    {
        this.module = module;
    }

    public int getUser()
    {
        return user;
    }

    public void setUser(int user)
    {
        this.user = user;
    }

    public int getForm()
    {
        return form;
    }

    public void setForm(int form)
    {
        this.form = form;
    }

}
