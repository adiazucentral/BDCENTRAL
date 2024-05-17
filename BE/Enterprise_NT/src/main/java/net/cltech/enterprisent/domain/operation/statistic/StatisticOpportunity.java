package net.cltech.enterprisent.domain.operation.statistic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa los tiempos entre estados de los resultados
 *
 * @version 1.0.0
 * @author eacuna
 * @since 05/02/2018
 * @see Creacion
 */
@ApiObject(
        group = "Estadisticas",
        name = "Tiempos Oportunidad",
        description = "Representa resultados para estadisticas"
)
@JsonInclude(Include.NON_NULL)
@Getter
@Setter
public class StatisticOpportunity
{

    @ApiObjectField(name = "id", description = "Id examen", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "orderNumber", description = "Numero de la orden", required = false, order = 2)
    private Long orderNumber;
    @ApiObjectField(name = "patientId", description = "Id del paciente", required = false, order = 2)
    private Integer patientId;
    @ApiObjectField(name = "entryDate", description = "Fecha ingreso", required = false, order = 3)
    private Date entryDate;
    @ApiObjectField(name = "entryElapsedTime", description = "Tiempo transcurrido ingreso", required = false, order = 4)
    private Long entryElapsedTime;
    @ApiObjectField(name = "entryUser", description = "Usuario ingreso", required = false, order = 5)
    private Integer entryUser;
    @ApiObjectField(name = "takeDate", description = "Fecha ingreso", required = false, order = 6)
    private Date takeDate;
    @ApiObjectField(name = "transportDate", description = "Fecha de transporte de la muestra", required = false, order = 7)
    private Date transportDate;
    @ApiObjectField(name = "printSampleDate", description = "Fecha de impresion de la muestra", required = false, order = 8)
    private Date printSampleDate;
    @ApiObjectField(name = "printSampleUser", description = "Usuario de impresion de la muestra", required = false, order = 9)
    private Integer printSampleUser;
    @ApiObjectField(name = "takeElapsedTime", description = "Tiempo transcurrido ingreso", required = false, order = 10)
    private Long takeElapsedTime;
    @ApiObjectField(name = "entryUser", description = "Usuario ingreso", required = false, order = 11)
    private Integer takeUser;
    @ApiObjectField(name = "verifyDate", description = "Fecha verificación", required = false, order = 12)
    private Date verifyDate;
    @ApiObjectField(name = "verifyElapsedTime", description = "Tiempo transcurrido verificación", required = false, order = 13)
    private Long verifyElapsedTime;
    @ApiObjectField(name = "verifyUser", description = "Usuario verifica", required = false, order = 14)
    private Integer verifyUser;
    @ApiObjectField(name = "resultDate", description = "Fecha resultado", required = false, order = 15)
    private Date resultDate;
    @ApiObjectField(name = "resultElapsedTime", description = "Tiempo transcurrido resultado", required = false, order = 16)
    private Long resultElapsedTime;
    @ApiObjectField(name = "resultUser", description = "Usuario resultado", required = false, order = 17)
    private Integer resultUser;
    @ApiObjectField(name = "validDate", description = "Fecha validado", required = false, order = 18)
    private Date validDate;
    @ApiObjectField(name = "validElapsedTime", description = "Tiempo transcurrido validado", required = false, order = 19)
    private Long validElapsedTime;
    @ApiObjectField(name = "validUser", description = "Usuario valida", required = false, order = 20)
    private Integer validUser;
    @ApiObjectField(name = "printDate", description = "Fecha impresion", required = false, order = 21)
    private Date printDate;
    @ApiObjectField(name = "printElapsedTime", description = "Tiempo transcurrido impresion", required = false, order = 22)
    private Long printElapsedTime;
    @ApiObjectField(name = "printUser", description = "Usuario imprime", required = false, order = 23)
    private Integer printUser;
    @ApiObjectField(name = "expectedTime", description = "Tiempo esperado", required = false, order = 24)
    private Long expectedTime;
    @ApiObjectField(name = "maxTime", description = "Tiempo maximo", required = false, order = 4)
    private Long maxTime;
    @ApiObjectField(name = "currentDate", description = "Fecha actual", required = false, order = 2)
    private Date currentDate;
    @ApiObjectField(name = "elapsedTime", description = "Tiempo transcurrido actual", required = false, order = 4)
    private Long elapsedTime;
    @ApiObjectField(name = "totalTime", description = "Tiempo total hasta la validación ", required = true, order = 34)
    private Long totalTime;
    @ApiObjectField(name = "entryResultTime", description = "Tiempo de entrada hasta la resultado", required = true, order = 35)
    private Long entryResultTime;
    @ApiObjectField(name = "entryValidTime", description = "Tiempo de entrada hasta la validación", required = true, order = 36)
    private Long entryValidTime;
    @ApiObjectField(name = "nameArea", description = "Nombre area", required = false, order = 37)
    private String nameArea;
    @ApiObjectField(name = "nameTest", description = "Nombre examen", required = false, order = 38)
    private String nameTest;
    @ApiObjectField(name = "nameService", description = "Nombre servicio", required = false, order = 39)
    private String nameService;
    @ApiObjectField(name = "nameUser", description = "Nombre usuario", required = false, order = 40)
    private String nameUser;
    @ApiObjectField(name = "totalTest", description = "Total de pruebas", required = false, order = 40)
    private Integer totalTest = 0;
    @ApiObjectField(name = "observedTimeDays", description = "Tiempo observado en Dias", required = false, order = 41)
    private Long observedTimeDays;

    public StatisticOpportunity()
    {
    }

    public StatisticOpportunity(Integer id, Long orderNumber)
    {
        this.id = id;
        this.orderNumber = orderNumber;
    }

   
    

}
