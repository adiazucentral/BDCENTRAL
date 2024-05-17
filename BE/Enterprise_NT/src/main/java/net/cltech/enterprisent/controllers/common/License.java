package net.cltech.enterprisent.controllers.common;

import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de las licencias.
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 21/02/2020
 * @see Creación
 */
public class License
{

    @ApiObjectField(name = "code", description = "Codigo", required = true, order = 1)
    public String code;
    @ApiObjectField(name = "name", description = "Nombre de la aplicacion", required = true, order = 2)
    public String name;
    @ApiObjectField(name = "active", description = "Estado de la activacion true-> Activo False->Desactivado", required = true, order = 3)
    public Boolean active;

    public License(String code, String name, boolean active)
    {
        this.code = code;
        this.name = name;
        this.active = active;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive(Boolean active)
    {
        this.active = active;
    }

}
