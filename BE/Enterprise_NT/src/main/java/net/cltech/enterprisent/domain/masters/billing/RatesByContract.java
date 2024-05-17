package net.cltech.enterprisent.domain.masters.billing;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la tarifa por contrato
 * 
 * @version 1.0.0
 * @author javila
 * @since 01/07/2021
 * @see Creación
 */

@ApiObject(
        group = "Facturacion",
        name = "Tarifa Por Contrato",
        description = "Representa la tarifa por contrato"
)
@Setter
@Getter
@NoArgsConstructor
public class RatesByContract
{
    @ApiObjectField(name = "rateId", description = "Id de la tarifa", required = true, order = 1)
    private Integer rateId;
    @ApiObjectField(name = "rateCode", description = "Codigo de la tarifa", required = true, order = 2)
    private String rateCode;
    @ApiObjectField(name = "nameRate", description = "Nombre de la tarifa", required = true, order = 3)
    private String nameRate;
    @ApiObjectField(name = "statusType", description = "Tipo de estado: 0 -> Esta activa en otro contrato, 1 -> Activo, 2 -> Inactivo", required = true, order = 4)
    private Short statusType;
    @ApiObjectField(name = "clientId", description = "Id del cliente", required = true, order = 5)
    private Integer clientId;
    @ApiObjectField(name = "contractId", description = "Id del contrato", required = true, order = 6)
    private Integer contractId;
    @ApiObjectField(name = "contractName", description = "Nombre del contrato", required = true, order = 7)
    private String contractName;
    @ApiObjectField(name = "contractCode", description = "Codigo del contrato", required = true, order = 8)
    private String contractCode;
    @ApiObjectField(name = "contractState", description = "estado del contrato", required = true, order = 9)
    private Integer contractState;
}
