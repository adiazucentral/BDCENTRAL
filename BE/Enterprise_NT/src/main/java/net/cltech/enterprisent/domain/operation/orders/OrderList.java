/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.masters.demographic.OrderType;
import net.cltech.enterprisent.domain.masters.interview.Question;
import net.cltech.enterprisent.domain.masters.test.Diagnostic;
import net.cltech.enterprisent.domain.masters.test.Sample;
import net.cltech.enterprisent.domain.masters.user.User;
import net.cltech.enterprisent.domain.operation.appointment.Appointment;
import net.cltech.enterprisent.domain.operation.demographic.SuperAccount;
import net.cltech.enterprisent.domain.operation.demographic.SuperBranch;
import net.cltech.enterprisent.domain.operation.demographic.SuperPhysician;
import net.cltech.enterprisent.domain.operation.demographic.SuperRate;
import net.cltech.enterprisent.domain.operation.demographic.SuperService;
import net.cltech.enterprisent.domain.operation.results.ResultTest;
import org.jsondoc.core.annotation.ApiObjectField;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class OrderList
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
    private SuperPatient patient;
    @ApiObjectField(name = "lastUpdateDate", description = "Fecha Ultima Modificación", required = false, order = 8)
    private Date lastUpdateDate;
    @ApiObjectField(name = "lastUpdateUser", description = "Usuario Ultima Modificación", required = false, order = 9)
    private User lastUpdateUser = new User();
    @ApiObjectField(name = "externalId", description = "Id de la orden del sistema externo", required = false, order = 11)
    private String externalId;
    @ApiObjectField(name = "branch", description = "Sede", required = false, order = 12)
    private SuperBranch branch;
    @ApiObjectField(name = "service", description = "Servicio", required = false, order = 13)
    private SuperService service;
    @ApiObjectField(name = "physician", description = "Medico", required = false, order = 14)
    private SuperPhysician physician;
    @ApiObjectField(name = "account", description = "Cuenta", required = false, order = 15)
    private SuperAccount account;
    @ApiObjectField(name = "rate", description = "Tarifa", required = false, order = 16)
    private SuperRate rate;
    @ApiObjectField(name = "demographics", description = "Demograficos", required = false, order = 17)
    private List<DemographicValue> AllDemographics;
    @ApiObjectField(name = "samples", description = "Muestras", required = false, order = 18)
    private List<Sample> samples = new ArrayList<>();
    @ApiObjectField(name = "tests", description = "examenes", required = false, order = 19)
    private List<TestList> tests = new ArrayList<>();
    @ApiObjectField(name = "resultTest", description = "Resultados de la orden", required = true, order = 24)
    private List<ResultTest> resultTest;
    @ApiObjectField(name = "comments", description = "comentarios de la orden", required = false, order = 25)
    private List<CommentOrder> comments = new ArrayList<>();
    @ApiObjectField(name = "listDiagnostic", description = "Lista de los diagnosticos asociados", required = false, order = 30)
    private List<Diagnostic> listDiagnostic;
    @ApiObjectField(name = "turn", description = "Turno asociado a la orden", required = false, order = 31)
    private String turn;
    @ApiObjectField(name = "deliveryType", description = "Tipo de entrega (Listados del lis id = 59->Impreso 60->correo 61->Consulta Web 62->App)", required = false, order = 32)
    private Item deliveryType;
    @ApiObjectField(name = "createUser", description = "Usuario que crea la orden", required = false, order = 34)
    private User createUser = new User();
    @ApiObjectField(name = "fatherOrder", description = "Numero de orden de rellamado de la cual proviene", required = false, order = 35)
    private Long fatherOrder;
    @ApiObjectField(name = "listQuestion", description = "Lista de las preguntas asociadas a la orden", required = false, order = 30)
    private List<Question> listQuestion;
    @ApiObjectField(name = "", description = "cita asociada a la orden", required = false, order = 50)
    private Appointment appointment = new Appointment();

    public OrderList setTests(List<TestList> tests)
    {
        this.tests = tests;
        return this;
    }

    public OrderList setResultTest(List<ResultTest> resultTest)
    {
        this.resultTest = resultTest;
        return this;
    }
}
