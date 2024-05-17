package net.cltech.enterprisent.domain.operation.results;

import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Race;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa una orden para el registro de resultados
 *
 * @version 1.0.0
 * @author jblanco
 * @since 02/07/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Resultados",
        name = "Orden Resultados",
        description = "Representa una orden para el filtro de ordenes dentro del módulo de registro de resultados"
)
@Getter
@Setter
@NoArgsConstructor
public class ResultOrder
{
    @ApiObjectField(name = "order", description = "Número de orden", required = true, order = 1)
    private long order;
    @ApiObjectField(name = "orderType", description = "Código del tipo de orden", required = true, order = 2)
    private String orderType;
    @ApiObjectField(name = "orderTypeName", description = "Nombre del tipo de orden", required = true, order = 3)
    private String orderTypeName;
    @ApiObjectField(name = "attachmentOrder", description = "Cantidad adjuntos de orden", required = true, order = 4)
    private int attachmentOrder;
    @ApiObjectField(name = "totalTest", description = "Cantidad de exámenes por órden", required = true, order = 5)
    private int totalTest;
    @ApiObjectField(name = "patientId", description = "Identificador del paciente", required = true, order = 6)
    private int patientId;
    @ApiObjectField(name = "IDType", description = "Tipo de documento del paciente", required = true, order = 7)
    private String IDType;
    @ApiObjectField(name = "patientCode", description = "Documento del paciente", required = true, order = 8)
    private String patientCode;
    @ApiObjectField(name = "patientName1", description = "Primer nombre del paciente", required = true, order = 9)
    private String patientName1;
    @ApiObjectField(name = "patientName2", description = "Segundo nombre del paciente", required = false, order = 10)
    private String patientName2;
    @ApiObjectField(name = "patientLastName1", description = "Primer apellido del paciente", required = true, order = 11)
    private String patientLastName1;
    @ApiObjectField(name = "patientLastName2", description = "Segundo apellido del paciente", required = false, order = 12)
    private String patientLastName2;
    @ApiObjectField(name = "pathology", description = "Patología del examen con respecto al valor de referencia", required = true, order = 13)
    private int pathology;
    @ApiObjectField(name = "branch", description = "Sede que realizó el ordenamiento", required = true, order = 14)
    private String branch;
    @ApiObjectField(name = "service", description = "Servicio que realizó el ordenamiento", required = true, order = 15)
    private String service;
    @ApiObjectField(name = "inconsistency", description = "Indica si el paciente asociado a la orden tiene registrada una incosistencia", required = true, order = 16)
    private boolean inconsistency;
    @ApiObjectField(name = "birthday", description = "Fecha de nacimiento del paciente", required = false, order = 17)
    private Date birthday;
    @ApiObjectField(name = "sex", description = "Descipción del sexo del paciente", required = false, order = 18)
    private Item sex = new Item();
    @ApiObjectField(name = "createDate", description = "Fecha de creación de la orden", required = false, order = 19)
    private Date createDate;
    @ApiObjectField(name = "test", description = "Id de la prueba", required = true, order = 20)
    private int test;
    @ApiObjectField(name = "testCode", description = "Codigo de la prueba", required = true, order = 20)
    private String testCode;
    @ApiObjectField(name = "testName", description = "Nombre de la prueba", required = true, order = 20)
    private String testName;
    @ApiObjectField(name = "testResultType", description = "Tipo de resultado de la prueba", required = true, order = 20)
    private Short testResultType;
    @ApiObjectField(name = "testState", description = "Estado de la prueba", required = true, order = 21)
    private int testState;
    @ApiObjectField(name = "testResult", description = "Resultado de las pruebas para aplicar el filtro por resultado", required = false, order = 22)
    private String testResult;
    @ApiObjectField(name = "attachmentTest", description = "Cantidad adjuntos de la prueba", required = true, order = 23)
    private int attachmentTest;
    @ApiObjectField(name = "verificationDate", description = "Fecha de verificación de la prueba para aplicar el filtro de oportunidad", required = false, order = 24)
    private Date verificationDate;
    @ApiObjectField(name = "time", description = "Tiempo de oportunidad en minutos de la prueba para aplicar el filtro de oportunidad", required = false, order = 25)
    private int time;
    @ApiObjectField(name = "stateOportunity", description = "Estado de Oportunidad: 0 -> No Vencida, 1 -> Vencida, 2 -> Proximos a vencer ", required = false, order = 26)
    private int stateOportunity;
    @ApiObjectField(name = "race", description = "Información de la raza del paciente", required = false, order = 27)
    private Race race;
    @ApiObjectField(name = "orderState", description = "Estado de la orden con respecto al procesamiento de las pruebas", required = true, order = 28)
    private int orderState;
    @ApiObjectField(name = "size", description = "Talla del paciente", required = false, order = 29)
    private Float size;
    @ApiObjectField(name = "weight", description = "Peso del paciente", required = false, order = 30)
    private Float weight;
    @ApiObjectField(name = "idOrderType", description = "Id del tipo de orden", required = true, order = 31)
    private Integer idOrderType;
    @ApiObjectField(name = "idService", description = "Id del servicio", required = true, order = 32)
    private Integer idService;
    @ApiObjectField(name = "codeService", description = "Codigo del tipo de orden", required = true, order = 33)
    private String codeService;
    @ApiObjectField(name = "preliminaryTestValidation", description = "El examen requiere validación preeliminar", required = true, order = 34)
    private boolean preliminaryTestValidation; 
     @ApiObjectField(name = "print", description = "El examen se imprime", required = true, order = 34)
    private boolean print; 
    @ApiObjectField(name = "validatedDate", description = "Fecha Ultimo Resultado Validado", required = false, order = 24)
    private Date validatedDate;
     @ApiObjectField(name = "sampleState", description = "Estado de la muestra", required = true, order = 35)
    private int sampleState;

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 37 * hash + (int) (this.order ^ (this.order >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final ResultOrder other = (ResultOrder) obj;
        return this.order == other.order;
    }

    public String getPatientName()
    {
        if (patientName2 == null)
        {
            return patientName1;
        } else
        {
            return patientName1 + " " + patientName2;
        }
    }

    public String getPatientLastName()
    {
        if (patientLastName2 == null)
        {
            return patientLastName1;
        } else
        {
            return patientLastName1 + " " + patientLastName2;
        }
    }
}
