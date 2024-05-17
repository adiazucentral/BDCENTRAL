/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.billing.integration;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;

/**
 * @version 1.0.0
 * @author hpoveda
 * @since 12/05/2022
 * @see Creación
 */
@ApiObject(
        group = "Operación - Facturación",
        name = "Order Facturacion",
        description = "Representa datos de facturacion de una orden "
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestBilling
{

    private String testCode;
    private String name;
    private BigDecimal price;
    private Double tax;
    private BigDecimal discount;
    private BigDecimal customePay;
    private long testId;
    private String status; // vigente o eliminado
    private String cpt;

}
