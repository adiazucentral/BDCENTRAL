package net.cltech.enterprisent.domain.integration.his;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
* Representa la sede a la que esta autorizado un usuario
*
* @version 1.0.0
* @author Julian
* @since 19/02/2021
* @see Creación
*/

@ApiObject(
        group = "Integración",
        name = "HIS",
        description = "Usuario"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizedBranch 
{
    @ApiObjectField(name = "codigo", description = "Codigo de la sede", required = false, order = 1)
    private String codigo;
    @ApiObjectField(name = "nombre", description = "Nombre de la sede", required = true, order = 2)
    private String nombre;

    public AuthorizedBranch()
    {
    }

    public AuthorizedBranch(String codigo, String nombre)
    {
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
}