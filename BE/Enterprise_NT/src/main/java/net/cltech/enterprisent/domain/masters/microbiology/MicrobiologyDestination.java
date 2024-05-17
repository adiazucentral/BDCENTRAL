/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.microbiology;

import java.util.List;
import net.cltech.enterprisent.domain.masters.common.MasterAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Destinos
 *
 * @version 1.0.0
 * @author cmartin
 * @since 14/02/2017
 * @see Creación
 */
@ApiObject(
        group = "Microbiologia",
        name = "Destino de Microbiologia",
        description = "Muestra informacion del maestro Destinos de Microbiologia que usa el API"
)
public class MicrobiologyDestination extends MasterAudit
{
    @ApiObjectField(name = "id", description = "Identificador de base de datos", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo", required = true, order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "reportTask", description = "Indica si el destino reporta tareas", required = true, order = 4)
    private boolean reportTask;
    @ApiObjectField(name = "state", description = "Si esta activo", required = true, order = 5)
    private boolean state;
    @ApiObjectField(name = "analyzersMicrobiologyDestinations", description = "lista de obbjetos de analizador con destinos de microbiologia", required = true, order = 6)
    private List<AnalyzerMicrobiologyDestination> analyzersMicrobiologyDestinations;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public boolean isReportTask()
    {
        return reportTask;
    }

    public void setReportTask(boolean reportTask)
    {
        this.reportTask = reportTask;
    }

    public boolean isState()
    {
        return state;
    }

    public void setState(boolean state)
    {
        this.state = state;
    }

    public List<AnalyzerMicrobiologyDestination> getAnalyzersMicrobiologyDestinations()
    {
        return analyzersMicrobiologyDestinations;
    }

    public void setAnalyzersMicrobiologyDestinations(List<AnalyzerMicrobiologyDestination> analyzersMicrobiologyDestinations)
    {
        this.analyzersMicrobiologyDestinations = analyzersMicrobiologyDestinations;
    }
}
