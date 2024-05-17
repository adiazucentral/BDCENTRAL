/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.domain.operation;

import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un filtro para consulta de los resultados históricos de las
 * pruebas
 *
 * @version 1.0.0
 * @author cmartin
 * @since 21/05/2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados - Filtro",
        name = "Filtro Histórico",
        description = "Representa un filtro para consulta de los resultados históricos de las pruebas"
)
public class HistoryFilter
{

    @ApiObjectField(name = "id", description = "Identificador del paciente", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "testId", description = "Lista de pruebas que se requieren consultas", required = false, order = 1)
    private List<Integer> testId;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public List<Integer> getTestId()
    {
        return testId;
    }

    public void setTestId(List<Integer> testId)
    {
        this.testId = testId;
    }

}
