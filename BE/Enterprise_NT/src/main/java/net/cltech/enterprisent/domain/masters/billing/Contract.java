package net.cltech.enterprisent.domain.masters.billing;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 * @author jeibarbosa
 */
@ApiObject(
        group = "Facturacion",
        name = "Contrato",
        description = "Muestra informacion del maestro de contrato qeu usa el API"
)
@Setter
@Getter
public class Contract
{
    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre de Conontrato", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "state", description = "estado de contrato", required = true, order = 3)
    private boolean state;
    @ApiObjectField(name = "maxAmount", description = "monto maximo", required = true, order = 4)
    private Double maxAmount;
    @ApiObjectField(name = "currentAmount", description = "monto actual", required = true, order = 5)
    private Double currentAmount;
    @ApiObjectField(name = "alertAmount", description = "monto alarma", required = true, order = 6)
    private Float alertAmount;
    @ApiObjectField(name = "capitated", description = "capitado", required = true, order = 7)
    private Integer capitated;
    @ApiObjectField(name = "discount", description = "descuento", required = true, order = 8)
    private Float discount;
    @ApiObjectField(name = "advancePercentage", description = "anticipo", required = true, order = 9)
    private Integer advancePercentage;
    @ApiObjectField(name = "cashPaymentDays", description = "dias de pago efectivo ", required = true, order = 10)
    private Integer cashPaymentDays;
    @ApiObjectField(name = "creditPayDays", description = "dias de pago credito", required = true, order = 11)
    private Integer creditPayDays;
    @ApiObjectField(name = "paymentPeriod", description = "plazo de pago ", required = true, order = 12)
    private Integer paymentPeriod;
    @ApiObjectField(name = "moderatingFee", description = "cuota moderadora ", required = true, order = 13)
    private boolean moderatingFee;
    @ApiObjectField(name = "copayment", description = "copago ", required = true, order = 14)
    private boolean copayment;
    @ApiObjectField(name = "regimen", description = "regimen ", required = true, order = 15)
    private String regimen;
    @ApiObjectField(name = "vendorName", description = "nombre del vendedor", required = true, order = 16)
    private String vendorName;
    @ApiObjectField(name = "vendorIdentifier", description = "identificacion de vendedor", required = true, order = 17)
    private String vendorIdentifier;
    @ApiObjectField(name = "signatureOfSeller", description = "firma de vendedor", required = false, order = 18)
    private String signatureOfSeller;
    @ApiObjectField(name = "idclient", description = "cliente", required = true, order = 19)
    private Integer idclient;
    @ApiObjectField(name = "taxs", description = "impuestos", required = true, order = 20)
    private List<DiscountRate> taxs;
    @ApiObjectField(name = "rates", description = "tarifas", required = true, order = 21)
    private List<RatesByContract> rates;
    @ApiObjectField(name = "code", description = "Codigo del contrato", required = true, order = 22)
    private String code;
    @ApiObjectField(name = "city", description = "Id del item demografico de la ciudad", required = true, order = 23)
    private Integer city;
    @ApiObjectField(name = "department", description = "Id del item demografico del departamento", required = true, order = 24)
    private Integer department;
    @ApiObjectField(name = "capitatedContract", description = "Contrato por capitado: 0 -> No, 1 -> Si", required = true, order = 25)
    private boolean capitatedContract;
    @ApiObjectField(name = "monthlyValue", description = "Valor mensual", required = true, order = 26)
    private Double monthlyValue;

    public Contract() {
        this.rates = new LinkedList<>();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Contract other = (Contract) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }
}
