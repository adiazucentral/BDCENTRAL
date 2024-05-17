/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la modificacion de la contraseña del usuario
 *
 * @version 1.0.0
 * @author jdiaz
 * @since 09/02/2022
 * @see Creacion
 */
@ApiObject(
        group = "Usuario",
        name = "recuperación la contraseña",
        description = "Representa el objeto para realizar la recuperación de contraseña"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class UserRecoveryPassword {

    @ApiObjectField(name = "userName", description = "username del usuario", required = true, order = 1)
    private String userName;
    @ApiObjectField(name = "email", description = "email del usuario", required = true, order = 2)
    private String email;

}
