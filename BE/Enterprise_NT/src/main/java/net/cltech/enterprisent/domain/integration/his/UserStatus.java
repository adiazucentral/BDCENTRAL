package net.cltech.enterprisent.domain.integration.his;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el objeto del usuario para el his
 * 
 * @version 1.0.0
 * @author omendez
 * @since 2021/02/02
 * @see Creación
 */

@ApiObject(
        group = "Integración",
        name = "HIS",
        description = "Estados de Usuario"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserStatus 
{
    @ApiObjectField(name = "usuario", description = "Nickname del usuario", required = true, order = 1)
    private String usuario;
    @ApiObjectField(name = "estado", description = "Si el usuario esta activo", required = true, order = 2)
    private int estado;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

  
}
