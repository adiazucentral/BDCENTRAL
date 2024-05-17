/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.statistic;

import java.util.HashMap;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObjectField;


@Getter
@Setter
public class StatisticOrderAverageTimes
{
    //Informacion general para estadisticas
    @ApiObjectField(name = "generalOpportunityArea", description = "Tiempos promedio areas", required = false, order = 38)
    private List<StaticticAveragetimes> generalOpportunityArea;
    @ApiObjectField(name = "generalOpportunityTest", description = "Tiempos promedio pruebas", required = false, order = 39)
    private List<StaticticAveragetimes> generalOpportunityTest;
    @ApiObjectField(name = "generalOpportunityServices", description = "Tiempos promedio servicios", required = false, order = 40)
    private List<StaticticAveragetimes> generalOpportunityServices;
    @ApiObjectField(name = "generalOpportunityUser", description = "Tiempos promedio usuarios", required = false, order = 41)
    private List<StaticticAveragetimes> generalOpportunityUser;
    @ApiObjectField(name = "generalOpportunityUserArea", description = "Tiempos promedio usuarios/area", required = false, order = 42)
    private HashMap<String, List<StaticticAveragetimes>> generalOpportunityUserArea;
    @ApiObjectField(name = "generalOpportunityUserTest", description = "Tiempos promedio usuarios/examen", required = false, order = 43)
    private HashMap<String, List<StaticticAveragetimes>> generalOpportunityUserTest;    
}
