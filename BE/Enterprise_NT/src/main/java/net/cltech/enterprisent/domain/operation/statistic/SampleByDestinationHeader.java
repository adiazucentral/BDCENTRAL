package net.cltech.enterprisent.domain.operation.statistic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.List;
import net.cltech.enterprisent.domain.operation.orders.Order;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa
 *
 * @version 1.0.0
 * @author eacuna
 * @since 23/02/2018
 * @see Creacion
 */
@ApiObject(
        group = "Estadisticas",
        name = "Muestra en Destino Cabecera",
        description = "Representa la cabecera para las muestras en destino"
)
@JsonInclude(Include.NON_NULL)
public class SampleByDestinationHeader
{

    @ApiObjectField(name = "dataGroup", description = "Agrupacion y conteo de muestras", required = true, order = 1)
    private List<SampleByDestinationCount> dataGroup;
    @ApiObjectField(name = "dataDetail", description = "Detallado de las muestras", required = true, order = 2)
    private List<Order> dataDetail;

    public SampleByDestinationHeader()
    {

    }

    public List<SampleByDestinationCount> getDataGroup()
    {
        return dataGroup;
    }

    public void setDataGroup(List<SampleByDestinationCount> dataGroup)
    {
        this.dataGroup = dataGroup;
    }

    public List<Order> getDataDetail()
    {
        return dataDetail;
    }

    public void setDataDetail(List<Order> dataDetail)
    {
        this.dataDetail = dataDetail;
    }

}
