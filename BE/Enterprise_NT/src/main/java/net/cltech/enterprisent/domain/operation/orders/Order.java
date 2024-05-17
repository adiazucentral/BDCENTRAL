package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import net.cltech.enterprisent.domain.DTO.migracionIngreso.OrderNT;
import net.cltech.enterprisent.domain.document.Document;
import net.cltech.enterprisent.domain.masters.billing.Rate;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.Account;
import net.cltech.enterprisent.domain.masters.demographic.Branch;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.demographic.Physician;
import net.cltech.enterprisent.domain.masters.demographic.ServiceLaboratory;
import net.cltech.enterprisent.domain.masters.test.Diagnostic;
import net.cltech.enterprisent.domain.masters.test.OptionTemplate;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.appointment.Appointment;
import net.cltech.enterprisent.domain.operation.common.Reason;
import net.cltech.enterprisent.domain.operation.orders.billing.CashBox;
import net.cltech.enterprisent.domain.operation.results.HistoricalResult;
import net.cltech.enterprisent.domain.operation.results.ImageTest;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import net.cltech.enterprisent.tools.mappers.MigrationMapper;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa una orden en el sistema
 *
 * @version 1.0.0
 * @author dcortes
 * @since 5/07/2017
 * @see Creación
 */
@ApiObject(
        group = "Operación - Ordenes",
        name = "Orden de Laboratorio",
        description = "Representa una orden de laboratorio de la aplicación"
)
@JsonInclude(Include.NON_NULL)
public class Order
{

    @ApiObjectField(name = "orderNumber", description = "Numero de Orden", required = false, order = 1)
    private Long orderNumber;
    @ApiObjectField(name = "createdDateShort", description = "Fecha Creación en Formato yyyymmdd", required = true, order = 2)
    private Integer createdDateShort;
    @ApiObjectField(name = "type", description = "Tipo de la orden", required = true, order = 3)
    private OrderType type = new OrderType();
    @ApiObjectField(name = "createdDate", description = "Fecha de Creación", required = true, order = 4)
    private Date createdDate;
    @ApiObjectField(name = "patient", description = "Paciente de la orden", required = true, order = 5)
    private Patient patient = new Patient();
    @ApiObjectField(name = "homebound", description = "Si se realizo toma de muestra en casa", required = false, order = 6)
    private boolean homebound;
    @ApiObjectField(name = "miles", description = "Millas recorridas para la toma de muestra en casa", required = false, order = 7)
    private Integer miles;
    @ApiObjectField(name = "lastUpdateDate", description = "Fecha Ultima Modificación", required = false, order = 8)
    private Date lastUpdateDate;
    @ApiObjectField(name = "lastUpdateUser", description = "Usuario Ultima Modificación", required = false, order = 9)
    private User lastUpdateUser = new User();
    @ApiObjectField(name = "active", description = "Si esta activa o inactiva", required = true, order = 10)
    private boolean active;
    @ApiObjectField(name = "externalId", description = "Id de la orden del sistema externo", required = false, order = 11)
    private String externalId;
    @ApiObjectField(name = "branch", description = "Sede", required = false, order = 12)
    private Branch branch = new Branch();
    @ApiObjectField(name = "service", description = "Servicio", required = false, order = 13)
    private ServiceLaboratory service = new ServiceLaboratory();
    @ApiObjectField(name = "physician", description = "Medico", required = false, order = 14)
    private Physician physician = new Physician();
    @ApiObjectField(name = "account", description = "Cuenta", required = false, order = 15)
    private Account account = new Account();
    @ApiObjectField(name = "rate", description = "Tarifa", required = false, order = 16)
    private Rate rate = new Rate();
    @ApiObjectField(name = "demographics", description = "Demograficos", required = false, order = 17)
    private List<DemographicValue> demographics;
    @ApiObjectField(name = "samples", description = "Muestras", required = false, order = 18)
    private List<Sample> samples;
    @ApiObjectField(name = "tests", description = "examenes", required = false, order = 19)
    private List<Test> tests = new ArrayList<>();
    @ApiObjectField(name = "messageCode", description = "Codigo del mensaje (Verificación de la muestra): 0 -> Muestra Verificada o Rechazada, 1 -> No Tomada, 2 -> Verificada o Con Resultado, 3 -> Rechazada.", required = false, order = 20)
    private Integer messageCode;
    @ApiObjectField(name = "reason", description = "Mótivo de modificación/eliminacion", required = false, order = 21)
    private Reason reason = new Reason();
    @ApiObjectField(name = "state", description = "Estado de la orden", required = false, order = 22)
    private Integer state;
    @ApiObjectField(name = "previousState", description = "Estado anterior de la orden", required = false, order = 22)
    private Integer previousState;
    @ApiObjectField(name = "hisPatient", description = "Paciente como esta en el HIS", required = true, order = 23)
    private Patient hisPatient;
    @ApiObjectField(name = "resultTest", description = "Resultados de la orden", required = true, order = 24)
    private List<ResultTest> resultTest;
    @ApiObjectField(name = "comments", description = "comentarios de la orden", required = false, order = 25)
    private List<CommentOrder> comments = new ArrayList<>();
    @ApiObjectField(name = "attachments", description = "Adjuntos", required = true, order = 26)
    private List<Document> attachments;
    @ApiObjectField(name = "recallNumber", description = "Numero de Orden rellamado", required = false, order = 27)
    private Long recallNumber;
    @ApiObjectField(name = "deleteTests", description = "Lista de Ids examenes eliminados", required = false, order = 28)
    private List<Test> deleteTests;
    @ApiObjectField(name = "inconsistency", description = "Indica si el paciente asociado a la orden tiene registrada una incosistencia", required = true, order = 29)
    private boolean inconsistency;
    @ApiObjectField(name = "listDiagnostic", description = "Lista de los diagnosticos asociados", required = false, order = 30)
    private List<Diagnostic> listDiagnostic;
    @ApiObjectField(name = "turn", description = "Turno asociado a la orden", required = false, order = 31)
    private String turn;
    @ApiObjectField(name = "deliveryType", description = "Tipo de entrega (Listados del lis id = 59->Impreso 60->correo 61->Consulta Web 62->App)", required = false, order = 32)
    private Item deliveryType;
    @ApiObjectField(name = "templateorder", description = "Representa la plantilla de la orden", required = false, order = 33)
    private String templateorder;
    @ApiObjectField(name = "createUser", description = "Usuario que crea la orden", required = false, order = 34)
    private User createUser = new User();
    @ApiObjectField(name = "fatherOrder", description = "Numero de orden de rellamado de la cual proviene", required = false, order = 35)
    private Long fatherOrder;
    @ApiObjectField(name = "daughterOrder", description = "Lista Numeros de ordenes de rellamado hijas", required = false, order = 36)
    private List<Long> daughterOrder;
    @ApiObjectField(name = "orderHis", description = "Numero de orden que corresponde al his", required = false, order = 37)
    private String orderHis;
    @ApiObjectField(name = "idMotive", description = "Id del motivo por el cual se actualiza la orden", required = false, order = 38)
    private Integer idMotive;
    @ApiObjectField(name = "commentary", description = "Comentario de la trazabilidad de la orden", required = false, order = 39)
    private String commentary;
    @ApiObjectField(name = "ImageTest", description = "Listado de las graficas de una orden", required = false, order = 40)
    private List<ImageTest> ImageTest;
    @ApiObjectField(name = "billingAccount", description = "Si la orden esta facturada para un cliente", required = true, order = 41)
    private boolean billingAccount;
    @ApiObjectField(name = "languageReport", description = "Idioma del reporte", required = false, order = 42)
    private String languageReport;
    @ApiObjectField(name = "isReceiveReference", description = "Si la orden fue recibida en Referencia", required = true, order = 42)
    private boolean isReceiveReference;
    @ApiObjectField(name = "cashbox", description = "Caja de la orden", required = false, order = 43)
    private CashBox cashbox;
    @ApiObjectField(name = "auxiliaryPhysicians", description = "Médicos Auxiliares", required = false, order = 44)
    private List<Physician> auxiliaryPhysicians;
    @ApiObjectField(name = "observations", description = "Observaciones de la orden", required = false, order = 45)
    private String observations;
    @ApiObjectField(name = "historicalResult", description = "Historico de resultados", required = false, order = 46)
    private List<HistoricalResult> historicalResult;
    @ApiObjectField(name = "configPrint", description = "configuracion de la impresion", required = false, order = 45)
    private String configPrint;
    @ApiObjectField(name = "", description = "indicador para citas del sistema", required = false, order = 50)
    private Integer hasAppointment = 0;
    @ApiObjectField(name = "", description = "cita asociada a la orden", required = false, order = 50)
    private Appointment appointment = new Appointment();
 
    public Order()
    {
        demographics = new ArrayList<>(0);
        attachments = new ArrayList<>();
        deleteTests = new ArrayList<>(0);
    }

    public Order(OrderNT orderNT)
    {
        //this.orderNumber = Long.parseLong(orderNT.getOrder());
        this.type.setCode(orderNT.getType());
        this.createUser.setName(orderNT.getUser());
        CommentOrder co = new CommentOrder();
        co.setComment(orderNT.getComment());
        this.comments.add(co);
        this.patient = MigrationMapper.toDtoPatient(orderNT.getPatient());
        this.demographics = MigrationMapper.toDtoDemo(orderNT.getDemographics());
        this.tests = MigrationMapper.toDtoTest(orderNT.getTests());
        this.externalId = Objects.equals(null, orderNT.getOrdenHis()) ? "0" : orderNT.getOrdenHis();

    }

    public Order(Long orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public Order(long orderNumber, String comment)
    {
        this.orderNumber = orderNumber;
        this.comments.add(new CommentOrder((short) 1, comment));
    }

    public Long getOrderNumber()
    {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber)
    {
        this.orderNumber = orderNumber;
    }

    public Integer getCreatedDateShort()
    {
        return createdDateShort;
    }

    public void setCreatedDateShort(Integer createdDateShort)
    {
        this.createdDateShort = createdDateShort;
    }

    public OrderType getType()
    {
        return type;
    }

    public void setType(OrderType type)
    {
        this.type = type;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public Patient getPatient()
    {
        return patient;
    }

    public Order setPatient(Patient patient)
    {
        this.patient = patient;
        return this;
    }

    public boolean isHomebound()
    {
        return homebound;
    }

    public void setHomebound(boolean homebound)
    {
        this.homebound = homebound;
    }

    public Integer getMiles()
    {
        return miles;
    }

    public void setMiles(Integer miles)
    {
        this.miles = miles;
    }

    public Date getLastUpdateDate()
    {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate)
    {
        this.lastUpdateDate = lastUpdateDate;
    }

    public User getLastUpdateUser()
    {
        return lastUpdateUser;
    }

    public void setLastUpdateUser(User lastUpdateUser)
    {
        this.lastUpdateUser = lastUpdateUser;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public String getExternalId()
    {
        return externalId;
    }

    public void setExternalId(String externalId)
    {
        this.externalId = externalId;
    }

    public Branch getBranch()
    {
        return branch;
    }

    public void setBranch(Branch branch)
    {
        this.branch = branch;
    }

    public ServiceLaboratory getService()
    {
        return service;
    }

    public void setService(ServiceLaboratory service)
    {
        this.service = service;
    }

    public Physician getPhysician()
    {
        return physician;
    }

    public void setPhysician(Physician physician)
    {
        this.physician = physician;
    }

    public Account getAccount()
    {
        return account;
    }

    public void setAccount(Account account)
    {
        this.account = account;
    }

    public Rate getRate()
    {
        return rate;
    }

    public void setRate(Rate rate)
    {
        this.rate = rate;
    }

    public Integer getMessageCode()
    {
        return messageCode;
    }

    public void setMessageCode(Integer messageCode)
    {
        this.messageCode = messageCode;
    }

    public List<DemographicValue> getDemographics()
    {
        return demographics;
    }

    public User getCreateUser()
    {
        return createUser;
    }

    public void setCreateUser(User createUser)
    {
        this.createUser = createUser;
    }

    public String getObservations()
    {
        return observations;
    }

    public void setObservations(String observations)
    {
        this.observations = observations;
    }

    public String getConfigPrint()
    {
        return configPrint;
    }

    public void setConfigPrint(String configPrint)
    {
        this.configPrint = configPrint;
    }

    public List<DemographicValue> getAllDemographics()
    {
        if (patient != null && demographics != null)
        {
            List<DemographicValue> all = new ArrayList<>(demographics);
            if (patient.getDemographics() != null)
            {
                all.addAll(patient.getDemographics());
            }
            return all;
        }
        return new ArrayList<>();
    }

    public void setDemographics(List<DemographicValue> demographics)
    {
        this.demographics = demographics;
    }

    public List<Test> getTests()
    {
        return tests;
    }

    public Order setTests(List<Test> tests)
    {
        this.tests = tests;
        return this;
    }

    public List<Sample> getSamples()
    {
        return samples;
    }

    public void setSamples(List<Sample> samples)
    {
        this.samples = samples;
    }

    public Reason getReason()
    {
        return reason;
    }

    public void setReason(Reason reason)
    {
        this.reason = reason;
    }

    public Integer getState()
    {
        return state;
    }

    public void setState(Integer state)
    {
        this.state = state;
    }

    public Patient getHisPatient()
    {
        return hisPatient;
    }

    public void setHisPatient(Patient hisPatient)
    {
        this.hisPatient = hisPatient;
    }

    public List<ResultTest> getResultTest()
    {
        return resultTest;
    }

    public Order setResultTest(List<ResultTest> resultTest)
    {
        this.resultTest = resultTest;
        return this;
    }

    public Integer getPreviousState()
    {
        return previousState;
    }

    public void setPreviousState(Integer previousState)
    {
        this.previousState = previousState;
    }

    public List<CommentOrder> getComments()
    {
        return comments;
    }

    public void setComments(List<CommentOrder> comments)
    {
        this.comments = comments;
    }

    public List<Document> getAttachments()
    {
        return attachments;
    }

    public void setAttachments(List<Document> attachments)
    {
        this.attachments = attachments;
    }

    public Long getRecallNumber()
    {
        return recallNumber;
    }

    public void setRecallNumber(Long recallNumber)
    {
        this.recallNumber = recallNumber;
    }

    public List<Test> getDeleteTests()
    {
        return deleteTests;
    }

    public void setDeleteTests(List<Test> deleteTests)
    {
        this.deleteTests = deleteTests;
    }

    public boolean isInconsistency()
    {
        return inconsistency;
    }

    public void setInconsistency(boolean inconsistency)
    {
        this.inconsistency = inconsistency;
    }

    public CashBox getCashbox()
    {
        return cashbox;
    }

    public void setCashbox(CashBox cashbox)
    {
        this.cashbox = cashbox;
    }

    public Order clean()
    {
        if (this.getPatient().getDiagnostic() != null && !this.getPatient().getDiagnostic().isEmpty())
        {
            this.getPatient().getDiagnostic().stream().forEach((CommentOrder comment) ->
            {
                comment.setType(null);
                comment.setLastTransaction(null);
                comment.setUser(null);
                comment.setState(null);
            });
        }
        this.setPatient(new Patient(this.getPatient().getId(), this.getPatient().getPatientId(), this.getPatient().getDiagnostic()));
        if (this.getResultTest() != null && !this.getResultTest().isEmpty())
        {
            this.getResultTest().stream().forEach((ResultTest result) ->
            {
                result.setSampleState(0);
                result.setTakenDate(null);
                result.setTakenUserId(null);
                result.setVerificationDate(null);
                result.setVerificationUserId(null);
                result.setEntryTestType(null);
                if (result.getHasTemplate() && result.getOptionsTemplate() != null && !result.getOptionsTemplate().isEmpty())
                {
                    result.getOptionsTemplate().stream().forEach((OptionTemplate optionTemplate) ->
                    {
                        optionTemplate.setIdTest(null);
                        optionTemplate.setOrder(null);
                        optionTemplate.setResults(null);
                    });
                }
            });
        }
        if (this.getSamples() != null && !this.getSamples().isEmpty())
        {
            this.getSamples().stream().forEach((Sample sample) ->
            {
                sample.setTests(null);
            });
        }
        if (this.getComments() != null && !this.getComments().isEmpty())
        {
            this.getComments().stream().forEach((CommentOrder comment) ->
            {
                comment.setType(null);
                comment.setLastTransaction(null);
                comment.setUser(null);
                comment.setState(null);
            });
        }
        return this;
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
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
        final Order other = (Order) obj;
        if (!Objects.equals(this.orderNumber, other.orderNumber))
        {
            return false;
        }
        return true;
    }

    public List<Diagnostic> getListDiagnostic()
    {
        return listDiagnostic;
    }

    public void setListDiagnostic(List<Diagnostic> listDiagnostic)
    {
        this.listDiagnostic = listDiagnostic;
    }

    public String getTurn()
    {
        return turn;
    }

    public void setTurn(String turn)
    {
        this.turn = turn;
    }

    public Item getDeliveryType()
    {
        return deliveryType;
    }

    public void setDeliveryType(Item deliveryType)
    {
        this.deliveryType = deliveryType;
    }

    public String getTemplateorder()
    {
        return templateorder;
    }

    public void setTemplateorder(String templateorder)
    {
        this.templateorder = templateorder;
    }

    public Long getFatherOrder()
    {
        return fatherOrder;
    }

    public void setFatherOrder(Long fatherOrder)
    {
        this.fatherOrder = fatherOrder;
    }

    public List<Long> getDaughterOrder()
    {
        return daughterOrder;
    }

    public void setDaughterOrder(List<Long> daughterOrder)
    {
        this.daughterOrder = daughterOrder;
    }

    public String getOrderHis()
    {
        return orderHis;
    }

    public void setOrderHis(String orderHis)
    {
        this.orderHis = orderHis;
    }

    public Order cleanFull()
    {
        this.setType(null);
        this.setLastUpdateDate(null);
        this.setLastUpdateUser(null);
        this.setBranch(null);
        this.setService(null);
        this.setPhysician(null);
        this.setAccount(null);
        this.setRate(null);
        this.setDemographics(null);
        this.setTests(null);
        this.setReason(null);
        this.setComments(null);
        this.setAttachments(null);
        this.setDeleteTests(null);
        this.setTemplateorder(null);
        return this;
    }

    public Integer getIdMotive()
    {
        return idMotive;
    }

    public void setIdMotive(Integer idMotive)
    {
        this.idMotive = idMotive;
    }

    public String getCommentary()
    {
        return commentary;
    }

    public void setCommentary(String commentary)
    {
        this.commentary = commentary;
    }

    public List<ImageTest> getImageTest()
    {
        return ImageTest;
    }

    public void setImageTest(List<ImageTest> ImageTest)
    {
        this.ImageTest = ImageTest;
    }

    public boolean isBillingAccount()
    {
        return billingAccount;
    }

    public void setBillingAccount(boolean billingAccount)
    {
        this.billingAccount = billingAccount;
    }

    public String getLanguageReport()
    {
        return languageReport;
    }

    public void setLanguageReport(String languageReport)
    {
        this.languageReport = languageReport;
    }

    public boolean isIsReceiveReference()
    {
        return isReceiveReference;
    }

    public void setIsReceiveReference(boolean isReceiveReference)
    {
        this.isReceiveReference = isReceiveReference;
    }

    public List<Physician> getAuxiliaryPhysicians()
    {
        return auxiliaryPhysicians;
    }

    public void setAuxiliaryPhysicians(List<Physician> auxiliaryPhysicians)
    {
        this.auxiliaryPhysicians = auxiliaryPhysicians;
    }

    public List<HistoricalResult> getHistoricalResult()
    {
        return historicalResult;
    }

    public void setHistoricalResult(List<HistoricalResult> historicalResult)
    {
        this.historicalResult = historicalResult;
    }

    public Integer getHasAppointment() {
        return hasAppointment;
    }

    public void setHasAppointment(Integer hasAppointment) {
        this.hasAppointment = hasAppointment;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }


    @Override
    public String toString()
    {
        return "Order{" + "orderNumber=" + orderNumber + ", createdDateShort=" + createdDateShort + ", type=" + type + ", createdDate=" + createdDate + ", patient=" + patient + ", homebound=" + homebound + ", miles=" + miles + ", lastUpdateDate=" + lastUpdateDate + ", lastUpdateUser=" + lastUpdateUser + ", active=" + active + ", externalId=" + externalId + ", branch=" + branch + ", service=" + service + ", physician=" + physician + ", account=" + account + ", rate=" + rate + ", demographics=" + demographics + ", samples=" + samples + ", tests=" + tests + ", messageCode=" + messageCode + ", reason=" + reason + ", state=" + state + ", previousState=" + previousState + ", hisPatient=" + hisPatient + ", resultTest=" + resultTest + ", comments=" + comments + ", attachments=" + attachments + ", recallNumber=" + recallNumber + ", deleteTests=" + deleteTests + ", inconsistency=" + inconsistency + ", listDiagnostic=" + listDiagnostic + ", turn=" + turn + ", deliveryType=" + deliveryType + ", templateorder=" + templateorder + ", createUser=" + createUser + ", fatherOrder=" + fatherOrder + ", daughterOrder=" + daughterOrder + ", orderHis=" + orderHis + ", idMotive=" + idMotive + ", commentary=" + commentary + ", ImageTest=" + ImageTest + ", billingAccount=" + billingAccount + '}';
    }

}
