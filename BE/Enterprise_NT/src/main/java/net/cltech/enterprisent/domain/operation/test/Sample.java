/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.tracking.Destination;
import net.cltech.enterprisent.domain.operation.orders.Test;
import net.cltech.enterprisent.domain.operation.tracking.SampleState;
import net.cltech.enterprisent.domain.operation.tracking.SampleTracking;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro de Muestra
 *
 * @author enavas
 * @version 1.0.0
 * @since 28/04/2017
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "Muestra",
        description = "Representa una Muestra"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Sample extends SuperSample
{
    @ApiObjectField(name = "printable", description = "Opcion si imprime el cogido de barras", order = 3)
    private boolean printable;
    @ApiObjectField(name = "canstiker", description = "Cantidad de stike", order = 4)
    private Integer canstiker;
    @ApiObjectField(name = "check", description = "Opcion si se verifica la muestra", order = 5)
    private boolean check;
    @ApiObjectField(name = "managementsample", description = "Descripción del manejo de la muestra", order = 6)
    private String managementsample;
    @ApiObjectField(name = "daysstored", description = "Numero de dias de almacenamiento", order = 7)
    private Integer daysstored;
    @ApiObjectField(name = "state", description = "Estado de la muestra", order = 8)
    private boolean state;
    @ApiObjectField(name = "codesample", description = "Codigo de la muestra", order = 10)
    private String codesample;
    @ApiObjectField(name = "laboratorytype", description = "Tipo de laboratorio", order = 11)
    private String laboratorytype;
    @ApiObjectField(name = "typebarcode", description = "Tipo de codigo de barras 0 : 39, 1:128 ", order = 13)
    private boolean typebarcode;
    @ApiObjectField(name = "subSamples", description = "Lista de sub-muestras asignadas ", order = 14)
    private List<Sample> subSamples = new ArrayList<>();
    @ApiObjectField(name = "selected", description = "Si esta asignada la muestra", order = 15)
    private boolean selected;
    @ApiObjectField(name = "tests", description = "Lista de Examenes - Operación.", order = 14)
    private List<Test> tests = new ArrayList<>();
    @ApiObjectField(name = "destinatios", description = "Lista de destinos de la muestra", order = 14)
    private List<Destination> destinatios;
    @ApiObjectField(name = "sampleTrackings", description = "Trazabilidad de la Muestra - Operación.", order = 14)
    private List<SampleTracking> sampleTrackings = new ArrayList<>();
    @ApiObjectField(name = "quantityDestination", description = "Cantidad de Destinos", order = 15)
    private Integer quantityDestination;
    @ApiObjectField(name = "quantityVerifyDestination", description = "Cantidad de Destinos Verificados", order = 16)
    private Integer quantityVerifyDestination;
    @ApiObjectField(name = "sampleState", description = "Estado Actual de la Muestra", order = 17)
    private SampleState sampleState;
    @ApiObjectField(name = "qualityTime", description = "Tiempo en minutos de calidad de la muestra", order = 18)
    private Long qualityTime;
    @ApiObjectField(name = "qualitypercentage", description = "Porcentaje para alarma de calidad de la muestra", order = 18)
    private Integer qualityPercentage;
    @ApiObjectField(name = "qualityFlag", description = "Identifica la calidad de la muestra<br>"
            + "1 - Si el tiempo transcurrido no ha sobrepasadoel porcentaje de alarma<br>"
            + "2 - Si el tiempo se encuentra entre el porcentaje de alarma y la caducidad de la muestra<br> "
            + "3 - Si se sobrepasa el tiempo en que caduca la muestra", order = 18)
    private Integer qualityFlag;
    @ApiObjectField(name = "takeDate", description = "Fecha de la toma", order = 19)
    private Date takeDate;
    @ApiObjectField(name = "specialStorage", description = "Indica si se almacena en una gradilla especial ", order = 20)
    private Boolean specialStorage = false;
    @ApiObjectField(name = "minimumTemperature", description = "Numero de dias de almacenamiento", order = 21)
    private Float minimumTemperature;
    @ApiObjectField(name = "maximumTemperature", description = "Numero de dias de almacenamiento", order = 22)
    private Float maximumTemperature;
    @ApiObjectField(name = "coveredSample", description = "Muestra tapada", order = 23)
    private boolean coveredSample;
    @ApiObjectField(name = "temperature", description = "Temperatura de la muestra", order = 24)
    private Double temperature;
}
