/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.statistic;

import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa los tiempos entre estados de los examenes
 *
 * @version 1.0.0
 * @author adiaz
 * @since 30/03/2022
 * @see Creacion
 */
@Getter
@Setter
public class StaticticAveragetimes
{
    @ApiObjectField(name = "timeEntryTake", description = "Tiempos promedio ingreso a la toma", required = false, order = 38)
    private Long timeEntryTake = 0L;
    @ApiObjectField(name = "timeTakeTransport", description = "Tiempos promedio toma al transporte", required = false, order = 38)
    private Long timeTakeTransport = 0L;
    @ApiObjectField(name = "timeTransportVerific", description = "Tiempos promedio transporte a la verificacion", required = false, order = 38)
    private Long timeTransportVerific = 0L;
    @ApiObjectField(name = "timeVerificResult", description = "Tiempos promedio verificacion al resultado", required = false, order = 38)
    private Long timeVerificResult = 0L;
    @ApiObjectField(name = "timeResultValidate", description = "Tiempos promedio resultado a la validacion", required = false, order = 38)
    private Long timeResultValidate = 0L;
    @ApiObjectField(name = "timeValidatePrint", description = "Tiempos promedio validacion a la entrega", required = false, order = 38)
    private Long timeValidatePrint = 0L;
    @ApiObjectField(name = "timeEntryVerific", description = "Tiempos promedio ingreso a la verificacion", required = false, order = 38)
    private Long timeEntryVerific = 0L;
    @ApiObjectField(name = "timeVerificValidate", description = "Tiempos promedio verificacion a la validacion", required = false, order = 38)
    private Long timeVerificValidate = 0L;
    @ApiObjectField(name = "timeVerificPrint", description = "Tiempos promedio verificacion a la entrega", required = false, order = 38)
    private Long timeVerificPrint = 0L;
    @ApiObjectField(name = "nameArea", description = "Nombre area", required = false, order = 37)
    private String nameArea;
    @ApiObjectField(name = "nameTest", description = "Nombre examen", required = false, order = 38)
    private String nameTest;
    @ApiObjectField(name = "nameService", description = "Nombre servicio", required = false, order = 39)
    private String nameService;
    @ApiObjectField(name = "nameUser", description = "Nombre usuario", required = false, order = 40)
    private String nameUser;
    @ApiObjectField(name = "totalTest", description = "Total de pruebas", required = false, order = 41)
    private Integer totalTest = 0;
    @ApiObjectField(name = "totalTestTransport", description = "Total de pruebas que hayan pasado por el estado de transporte", required = false, order = 42)
    private Integer totalTestTransport = 0;
    @ApiObjectField(name = "totalTestPrint", description = "Total de pruebas que hayan pasado por el estado de impresion", required = false, order = 43)
    private Integer totalTestPrint = 0;
 
}
