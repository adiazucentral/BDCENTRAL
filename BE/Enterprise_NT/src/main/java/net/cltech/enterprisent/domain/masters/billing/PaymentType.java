package net.cltech.enterprisent.domain.masters.billing;

import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Tipo de Pago
 *
 * @version 1.0.0
 * @author eacuna
 * @since 30/08/2017
 * @see Creación
 */
@ApiObject(
        group = "Facturacion",
        name = "Tipo de Pago",
        description = "Muestra informacion del maestro Tipo de pago que usa el API"
)
public class PaymentType extends MasterAudit
{

    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "card", description = "Habilita tarjeta", required = true, order = 3)
    private boolean card;
    @ApiObjectField(name = "bank", description = "Habilita banco", required = true, order = 4)
    private boolean bank;
    @ApiObjectField(name = "number", description = "Habilita Número", required = true, order = 5)
    private boolean number;
    @ApiObjectField(name = "adjustment", description = "Utilizado para ajustes", required = true, order = 6)
    private boolean adjustment;
    @ApiObjectField(name = "state", description = "Estado activo", required = true, order = 7)
    private boolean state;
    @ApiObjectField(name = "code", description = "codigo del tipo de pago", required = true, order = 8)
    private String code;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public boolean isCard()
    {
        return card;
    }

    public void setCard(boolean card)
    {
        this.card = card;
    }

    public boolean isBank()
    {
        return bank;
    }

    public void setBank(boolean bank)
    {
        this.bank = bank;
    }

    public boolean isNumber()
    {
        return number;
    }

    public void setNumber(boolean number)
    {
        this.number = number;
    }

    public boolean isAdjustment()
    {
        return adjustment;
    }

    public void setAdjustment(boolean adjustment)
    {
        this.adjustment = adjustment;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
