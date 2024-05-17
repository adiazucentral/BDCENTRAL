package net.cltech.enterprisent.domain.integration.his;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el objeto del usuario para el his
 * 
 * @version 1.0.0
 * @author omendez
 * @since 2021/02/01
 * @see Creación
 */

@ApiObject(
        group = "Integración",
        name = "HIS",
        description = "Usuario"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserHis 
{
    @ApiObjectField(name = "id", description = "Identificador autonumerico de base de datos", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "usuario", description = "Nickname del usuario", required = true, order = 2)
    private String usuario;
    @ApiObjectField(name = "nombres", description = "Nombres del ususario", required = true, order = 3)
    private String nombres;
    @ApiObjectField(name = "apellidos", description = "Apellidos del usuario", required = true, order = 4)
    private String apellidos;
    @ApiObjectField(name = "contraseña", description = "Ultima Contraseña de usuario", required = true, order = 6)
    private String contraseña;
    @ApiObjectField(name = "foto", description = "Foto del usuario (Imagen)", order = 7)
    private String foto;
    @ApiObjectField(name = "correo", description = "Email del ususario", required = true, order = 8)
    private String correo;
    @ApiObjectField(name = "sedes", description = "Sedes a las que esta autorizado el usuario", required = true, order = 9)
    private List<AuthorizedBranch> sedes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public List<AuthorizedBranch> getSedes() {
        return sedes;
    }

    public void setSedes(List<AuthorizedBranch> sedes) {
        this.sedes = sedes;
    }

   
}
