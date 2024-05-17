/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 * @author jeibarbosa
 */
@ApiObject(
        group = "Operación - Numero Ordenes",
        name = "NUmero de Orden para Busquedas",
        description = "Representa una orden de laboratorio con pocos datos utilizadas para las busquedas y retorna unicamente el numero de orden"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class OrderNumSearch
{
    @ApiObjectField(name = "orders", description = "list de Numero de Orden", order = 1)
    private List<Long> orders = new ArrayList<>();
  
    
}
