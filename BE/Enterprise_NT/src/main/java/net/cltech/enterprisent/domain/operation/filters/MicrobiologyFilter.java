/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.filters;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa clase con filtros para busquedas en microbiologia
 *
 * @version 1.0.0
 * @author cmartin
 * @since 31/01/2018
 * @see Creación
 */
@ApiObject(
        group = "Operación - Filtros",
        name = "Busquedas Microbiologia",
        description = "Representa filtro con parametros para busquedas en microbiologia."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MicrobiologyFilter
{

    @ApiObjectField(name = "rangeType", description = "Rango de busqueda: 0 -> Fecha Muestra<br> 1-> Ordenes", order = 1)
    private Integer rangeType;
    @ApiObjectField(name = "init", description = "Rango inicial", order = 2)
    private Long init;
    @ApiObjectField(name = "end", description = "Rango final", order = 3)
    private Long end;
    @ApiObjectField(name = "test", description = "Examen", order = 4)
    private Integer test;
    @ApiObjectField(name = "report", description = "Reportar: 0 -> Ambos, 1 -> Cultivo, 2 -> Directos", order = 5)
    private Integer report;
    @ApiObjectField(name = "task", description = "Tareas: ", order = 6)
    private Integer task;
    @ApiObjectField(name = "pendingStates", description = "Estado Pendiente:  1 -> Resultado, 2 -> Pre-Validación, 3 -> Validación <br> Enviar lista vacia para no filtrar", order = 7)
    private List<Integer> pendingStates;
    @ApiObjectField(name = "codeSample", description = "Codigo de la muestra", order = 8)
    private String codeSample;

    public Integer getRangeType()
    {
        return rangeType;
    }

    public void setRangeType(Integer rangeType)
    {
        this.rangeType = rangeType;
    }

    public Long getInit()
    {
        return init;
    }

    public void setInit(Long init)
    {
        this.init = init;
    }

    public Long getEnd()
    {
        return end;
    }

    public void setEnd(Long end)
    {
        this.end = end;
    }

    public Integer getTest()
    {
        return test;
    }

    public void setTest(Integer test)
    {
        this.test = test;
    }

    public Integer getReport()
    {
        return report;
    }

    public void setReport(Integer report)
    {
        this.report = report;
    }

    public Integer getTask()
    {
        return task;
    }

    public void setTask(Integer task)
    {
        this.task = task;
    }

    public List<Integer> getPendingStates()
    {
        return pendingStates;
    }

    public void setPendingStates(List<Integer> pendingStates)
    {
        this.pendingStates = pendingStates;
    }

    public String getCodeSample()
    {
        return codeSample;
    }

    public void setCodeSample(String codeSample)
    {
        this.codeSample = codeSample;
    }

}
