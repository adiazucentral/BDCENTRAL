/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.orders.excel;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 * @author jvellojin
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderReportAidaAcs
{
    @ApiObjectField(name = "nit", description = "NIT del cliente", required = true, order = 1)
    private String nit;
    @ApiObjectField(name = "nameAccount", description = "Nombre del cliente", required = true, order = 2)
    private String nameAccount;
    @ApiObjectField(name = "orderTypeCode", description = "Codigo tipo de orden", required = true, order = 3)
    private String orderTypeCode;
    @ApiObjectField(name = "orderTypeName", description = "Nombre Tipo de orden", required = true, order = 4)
    private String orderTypeName;
    @ApiObjectField(name = "patientId", description = "Historia", required = true, order = 5)
    private String patientId;
    @ApiObjectField(name = "documentTypeName", description = "Nombre del tipo de documento", required = true, order = 6)
    private String documentTypeName;
    @ApiObjectField(name = "name1", description = "Nombre 1", required = true, order = 7)
    private String name1;
    @ApiObjectField(name = "name2", description = "Nombre 2", required = false, order = 8)
    private String name2;
    @ApiObjectField(name = "lastName", description = "Apellido 1", required = true, order = 9)
    private String lastName;
    @ApiObjectField(name = "surName", description = "Apellido 2", required = false, order = 10)
    private String surName;
    @ApiObjectField(name = "sexId", description = "Id del Sexo", required = false, order = 11)
    private int sexId;
    @ApiObjectField(name = "sex", description = "Sexo", required = false, order = 12)
    private String sex;
    @ApiObjectField(name = "birthday", description = "Fecha Nacimiento", required = false, order = 13)
    private Date birthday;
    @ApiObjectField(name = "orderNumber", description = "Numero de Orden", required = false, order = 14)
    private Long orderNumber;
    @ApiObjectField(name = "createdDateShort", description = "Fecha de creacion de la orden", required = false, order = 15)
    private Date createdDateShort;
    @ApiObjectField(name = "address", description = "Dirección", required = false, order = 16)
    private String address;
    @ApiObjectField(name = "phone", description = "Telefono", required = false, order = 17)
    private String phone;
    @ApiObjectField(name = "physicianName", description = "Nombre del medico", required = false, order = 18)
    private String physicianName;
    @ApiObjectField(name = "test", description = "Examenes", required = false, order = 19)
    private List<TestReportAida> tests = new ArrayList<>();
    @ApiObjectField(name = "demographics", description = "Demograficos", required = false, order = 20)
    private List<DemographicValue> AllDemographics;
    @ApiObjectField(name = "codeSex", description = "Código del Sexo", required = false, order = 21)
    private String codeSex;
    @ApiObjectField(name = "age", description = "Edad paciente", required = false, order = 22)
    private Integer age;
    @ApiObjectField(name = "ageUnit", description = "Unidad de la edad dias(1), meses(2), años(3)", required = false, order = 23)
    private Integer ageUnit;
    @ApiObjectField(name = "email", description = "Correo", required = false, order = 24)
    private String email;
    @ApiObjectField(name = "statusOrder", description = "Estado de la orden", required = false, order = 25)
    private Integer statusOrder;
    @ApiObjectField(name = "rateName", description = "Nombre de la tarifa", required = false, order = 26)
    private String rateName;

}
