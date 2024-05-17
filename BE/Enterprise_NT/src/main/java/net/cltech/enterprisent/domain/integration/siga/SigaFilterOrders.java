package net.cltech.enterprisent.domain.integration.siga;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la clase para filtrar las ordenes para Siga
 *
 * @version 1.0.0
 * @author omendez
 * @since 09/02/2021
 * @see Creación
 */
@ApiObject(
        group = "Siga",
        name = "Filtro Ordenes",
        description = "Representa el objeto para filtrar las ordenes para Siga."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaFilterOrders 
{
    @ApiObjectField(name = "dateFilter", description = "Fecha inicial", required = true, order = 1)
    private Date dateFilter;
    @ApiObjectField(name = "type", description = "Tipo de documento", required = true, order = 3)
    private String type;
    @ApiObjectField(name = "history", description = "Historia", required = true, order = 4)
    private String history;
    @ApiObjectField(name = "shift", description = "Turno", required = true, order = 5)
    private String shift;

    public SigaFilterOrders()
    {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public String getShift()
    {
        return shift;
    }

    public void setShift(String shift)
    {
        this.shift = shift;
    }

    public Date getDateFilter()
    {
        return dateFilter;
    }

    public void setDateFilter(Date dateFilter)
    {
        this.dateFilter = dateFilter;
    }
}
