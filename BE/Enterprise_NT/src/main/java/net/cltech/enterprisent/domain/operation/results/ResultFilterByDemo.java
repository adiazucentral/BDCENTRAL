/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.results;

import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un el filtro de resultados por el resultado de la prueba
 *
 * @version 1.0.0
 * @author jblanco
 * @since 12/04/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados - Filtro",
        name = "Filtro Por Demográfico",
        description = "Representa un el filtro de órdenes por demográficos"
)
public class ResultFilterByDemo
{
    @ApiObjectField(name = "id", description = "Identificador del demográfico", required = false, order = 1)
    private int id;
    @ApiObjectField(name = "demographicItems", description = "Lista de items demográficos", required = false, order = 2)
    private List<Integer> demographicItems;

    public ResultFilterByDemo()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public List<Integer> getDemographicItems()
    {
        return demographicItems;
    }

    public void setDemographicItems(List<Integer> demographicItems)
    {
        this.demographicItems = demographicItems;
    }

}

