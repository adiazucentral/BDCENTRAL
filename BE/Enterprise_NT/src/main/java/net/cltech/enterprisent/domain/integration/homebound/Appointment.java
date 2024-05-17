package net.cltech.enterprisent.domain.integration.homebound;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de una cita desde Home Bound
 *
 * @version 1.0.0
 * @author JDuarte
 * @since 11/06/2020
 * @see Creación
 */
@ApiObject(
        group = "Operacion",
        name = "Cita",
        description = "Datos de una cita desde Home Bound"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class Appointment
{
    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "date", description = "Fecha de registro", required = true, order = 2)
    private Integer date;
    @ApiObjectField(name = "zone", description = "Zona donde se atendera el servicio", required = true, order = 3)
    private Zone zone;
    @ApiObjectField(name = "phlebotomist", description = "Flebotomista", required = true, order = 4)
    private UserHomeBound phlebotomist;
    @ApiObjectField(name = "shift", description = "Jornada", required = true, order = 5)
    private Shift shift;
    @ApiObjectField(name = "patient", description = "Paciente", required = true, order = 6)
    private PatientHomeBound patient;
    @ApiObjectField(name = "address", description = "Dirección", required = true, order = 7)
    private String address;
    @ApiObjectField(name = "phone", description = "Telefono", required = true, order = 8)
    private String phone;
    @ApiObjectField(name = "physician", description = "Medico", required = true, order = 9)
    private PhysicianHomeBound physician;
    @ApiObjectField(name = "account", description = "Cliente", required = true, order = 10)
    private AccountHomeBound account;
    @ApiObjectField(name = "rate", description = "Tarifa", required = true, order = 11)
    private RateHomeBound rate;
    @ApiObjectField(name = "serviceValue", description = "Valor del Servicio", required = true, order = 12)
    private BigDecimal serviceValue;
    @ApiObjectField(name = "totalValue", description = "Valor Total", required = true, order = 13)
    private BigDecimal totalValue;
    @ApiObjectField(name = "billingTests", description = "Examenes con precio", required = true, order = 14)
    private List<BillingTestHomeBound> billingTests = new ArrayList<>();
    @ApiObjectField(name = "lastTransaction", description = "Fecha de la creación o ultima actualización", required = true, order = 15)
    private Date lastTransaction;
    @ApiObjectField(name = "user", description = "Usuario que realiza la operación", required = true, order = 16)
    private AuthorizedUserHomeBound user;
    @ApiObjectField(name = "comment", description = "Comentario", required = true, order = 17)
    private String comment;
    @ApiObjectField(name = "orderNumber", description = "Numero de Orden", required = true, order = 18)
    private Long orderNumber;
    @ApiObjectField(name = "observation", description = "Observación o Comentario del Flebotomista", required = true, order = 19)
    private String observation;
    @ApiObjectField(name = "state", description = "Estado Actual", required = true, order = 20)
    private Integer state;
    @ApiObjectField(name = "type", description = "Sede a la que pertenece el rutero", required = true, order = 21)
    private String type;
    @ApiObjectField(name = "idService", description = "Sede a la que pertenece el rutero", required = true, order = 22)
    private Integer idService;
    @ApiObjectField(name = "branch", description = "Sede a la que pertenece el rutero", required = true, order = 23)
    private BranchHomeBound branch;
    @ApiObjectField(name = "reason", description = "Motivo de la cancelación o el rechazo", required = true, order = 24)
    private ReasonHomeBound reason;
    @ApiObjectField(name = "samples", description = "Muestras", required = true, order = 25)
    private List<SampleHomeBound> samples;
    @ApiObjectField(name = "interview", description = "Entrevista", required = true, order = 26)
    private List<QuestionHomeBound> interview;
    @ApiObjectField(name = "deletedTests", description = "Examenes con precio ha eliminar", required = true, order = 27)
    private List<BillingTestHomeBound> deletedTests;
    @ApiObjectField(name = "latitude", description = "Latitud de la direccion", required = true, order = 28)
    private String latitude;
    @ApiObjectField(name = "longitude", description = "Longitud de la direccion", required = true, order = 29)
    private String longitude;
    @ApiObjectField(name = "additionalInformation", description = "Informacion adicional de la direccion del paciente", required = false, order = 30)
    private String additionalInformation;
    @ApiObjectField(name = "questions", description = "Lista de preguntas calificadas", required = false, order = 31)
    private List<QuestionService> questions;
    @ApiObjectField(name = "allTests", description = "Lista de examenes", required = false, order = 32)
    private List<TestHomeBound> allTests;
    @ApiObjectField(name = "creationTransaction", description = "Fecha de la creación", required = true, order = 33)
    private Date creationTransaction;
    @ApiObjectField(name = "userCreation", description = "Usuario que crea la cita", required = true, order = 34)
    private AuthorizedUserHomeBound userCreation;
    @ApiObjectField(name = "demograficos", description = "Demograficos del paciente", required = false, order = 36)
    private List<DemographicHomeBound> demographics = new ArrayList<>();

    public Appointment()
    {
    }
}
