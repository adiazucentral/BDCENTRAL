/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.user;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la modificacion de la contraseña del usuario
 *
 * @version 1.0.0
 * @author equijano
 * @since 01/08/2019
 * @see Creacion
 */
@ApiObject(
        group = "Usuario",
        name = "Usuaro de integracion",
        description = "Representa el objeto para consultar el usuarios de integracion"
)
@Getter
@Setter
public class UserIntegration {
    @ApiObjectField(name = "id", description = "Identificador autonumerico de base de datos", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "password", description = "Ultima Contraseña de usuario", required = true, order = 5)
    private String password;
}
