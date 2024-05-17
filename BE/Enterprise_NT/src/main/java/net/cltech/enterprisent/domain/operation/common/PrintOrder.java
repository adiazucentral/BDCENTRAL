package net.cltech.enterprisent.domain.operation.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import net.cltech.enterprisent.domain.masters.demographic.Physician;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase para tener informacion de la orden para imprimir
 *
 * @version 1.0.0
 * @author equijano
 * @since 27/05/2019
 * @see Creación
 */
@ApiObject(
        group = "Operación - Comunes",
        name = "Imprimir orden",
        description = "Representa la informacion para imprimir la orden."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PrintOrder
{

    @ApiObjectField(name = "physician", description = "Informacion del medico", required = false, order = 1)
    private Physician physician;
    @ApiObjectField(name = "listOrders", description = "Lista de ordenes con la informacion para imprimir", required = false, order = 2)
    private List<PrintOrderInfo> listOrders;

    public Physician getPhysician()
    {
        return physician;
    }

    public void setPhysician(Physician physician)
    {
        this.physician = physician;
    }

    public List<PrintOrderInfo> getListOrders()
    {
        return listOrders;
    }

    public void setListOrders(List<PrintOrderInfo> listOrders)
    {
        this.listOrders = listOrders;
    }

}
