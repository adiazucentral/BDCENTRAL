package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la relacion entre turno y orden
 *
 * @version 1.0.0
 * @author equijano
 * @since 29/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Turno a ordenes",
        description = "Representa la relacion entre el turno y las ordenes"
)
@JsonInclude(Include.NON_NULL)
public class ShiftOrder
{

    @ApiObjectField(name = "turn", description = "Numero del turno", required = true, order = 1)
    private String turn;
    @ApiObjectField(name = "id", description = "id del turno", required = true, order = 2)
    private Integer id;
    @ApiObjectField(name = "orders", description = "Lista de ordenes", required = true, order = 3)
    private List<String> orders;

    public String getTurn() {
        return turn;
    }

    public void setTurn(String turn) {
        this.turn = turn;
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    
    
}
