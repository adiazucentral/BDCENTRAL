package net.cltech.enterprisent.domain.operation.tracking;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa con información para el almacenamiento de la muestra
 *
 * @version 1.0.0
 * @author eacuna
 * @since 05/06/2018
 * @see Creación
 */
@ApiObject(
        group = "Trazabilidad",
        name = "Almacenamiento Muestra",
        description = "Datos para almacenar la muestra en la gradilla respectiva"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleStore
{

    @ApiObjectField(name = "order", description = "Orden", required = false, order = 1)
    private Long order;
    @ApiObjectField(name = "sample", description = "Código de la Muestra", required = false, order = 2)
    private String sample;
    @ApiObjectField(name = "racks", description = "Id´s de las gradillas para almacenar la muestra", required = false, order = 3)
    private List<Integer> racks;
    @ApiObjectField(name = "position", description = "Posicion donde se retira la muestra", required = false, order = 4)
    private String position;
    @ApiObjectField(name = "rackId", description = "Id de la gradilla donde se va a retirar la muestra", required = false, order = 5)
    private Integer rackId;
    @ApiObjectField(name = "rackCode", description = "Codigo de la gradilla donde se va a retirar la muestra", required = false, order = 6)
    private String rackCode;
    @ApiObjectField(name = "orderPartial", description = "orderPartial", required = false, order = 7)
    private String orderPartial;

    public Long getOrder()
    {
        return order;
    }

    public void setOrder(Long order)
    {
        this.order = order;
    }

    public String getSample()
    {
        return sample;
    }

    public void setSample(String sample)
    {
        this.sample = sample;
    }

    public List<Integer> getRacks()
    {
        return racks;
    }

    public void setRacks(List<Integer> racks)
    {
        this.racks = racks;
    }

    public String getPosition()
    {
        return position;
    }

    public void setPosition(String position)
    {
        this.position = position;
    }

    public Integer getRackId()
    {
        return rackId;
    }

    public void setRackId(Integer rackId)
    {
        this.rackId = rackId;
    }

    public String getRackCode()
    {
        return rackCode;
    }

    public void setRackCode(String rackCode)
    {
        this.rackCode = rackCode;
    }
    
    public String getOrderPartial() {
        return orderPartial;
    }

    public void setOrderPartial(String orderPartial) {
        this.orderPartial = orderPartial;
    }

    public static int STATE_STORED = 1;
    public static int STATE_REMOVED = 2;

}
