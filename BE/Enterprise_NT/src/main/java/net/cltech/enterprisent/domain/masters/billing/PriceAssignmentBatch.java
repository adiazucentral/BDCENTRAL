/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.billing;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.common.AuthorizedUser;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la configuracion precios de examenes masivamente
 *
 * @version 1.0.0
 * @author omendez
 * @since 09/09/2022
 * @see Creación
 */
@ApiObject(
        group = "Facturacion",
        name = "Assignación Precios Masivamente",
        description = "Objeto con los precios de los examenes"
)
@Getter
@Setter
public class PriceAssignmentBatch {
    
    @ApiObjectField(name = "idTest", description = "Id de la prueba", required = true, order = 1)
    private Integer idTest;
    @ApiObjectField(name = "code", description = "Codigo de la prueba", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "price", description = "Precio de la prueba", required = true, order = 3)
    private Double price;
    @ApiObjectField(name = "patientPercentage", description = "Porcentaje del paciente", required = false, order = 4)
    private Double patientPercentage;
    @ApiObjectField(name = "idRate", description = "Id de la tarifa", required = true, order = 5)
    private Integer idRate;
    @ApiObjectField(name = "idValid", description = "Id de la vigencía", required = true, order = 6)
    private Integer idValid;
    @ApiObjectField(name = "centralSystem", description = "Id sistema central de homologación <br> 0 - Código LIS ", required = true, order = 7)
    private Integer centralSystem;
    @ApiObjectField(name = "user", description = "Usuario que realiza que realiza la operación", required = false, order = 8)
    private AuthorizedUser user;
    
}
