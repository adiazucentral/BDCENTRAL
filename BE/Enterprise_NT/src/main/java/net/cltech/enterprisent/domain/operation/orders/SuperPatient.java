/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.orders;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.operation.demographic.SuperDocumentType;
import net.cltech.enterprisent.domain.operation.demographic.SuperRace;
import org.jsondoc.core.annotation.ApiObjectField;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class SuperPatient
{

    @ApiObjectField(name = "id", description = "Identificador", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "patientId", description = "Historia", required = true, order = 2)
    private String patientId;
    @ApiObjectField(name = "name1", description = "Nombre 1", required = true, order = 3)
    private String name1;
    @ApiObjectField(name = "name2", description = "Nombre 2", required = false, order = 4)
    private String name2;
    @ApiObjectField(name = "lastName", description = "Apellido 1", required = true, order = 5)
    private String lastName;
    @ApiObjectField(name = "surName", description = "Apellido 2", required = false, order = 6)
    private String surName;
    @ApiObjectField(name = "sex", description = "Sexo", required = false, order = 7)
    private Item sex = new Item();
    @ApiObjectField(name = "birthday", description = "Fecha Nacimiento", required = false, order = 8)
    private Date birthday;
    @ApiObjectField(name = "email", description = "Correo Electronico", required = false, order = 9)
    private String email;
    @ApiObjectField(name = "size", description = "Talla", required = false, order = 10)
    private BigDecimal size;
    @ApiObjectField(name = "weight", description = "Peso", required = false, order = 11)
    private BigDecimal weight;
    @ApiObjectField(name = "phone", description = "Telefono del Paciente", required = false, order = 22)
    private String phone;
    @ApiObjectField(name = "address", description = "Dirección del Paciente", required = false, order = 23)
    private String address;
    @ApiObjectField(name = "documentType", description = "Tipo Documento", required = false, order = 16)
    private SuperDocumentType documentType;
    @ApiObjectField(name = "race", description = "Raza", required = false, order = 15)
    private SuperRace race;
    @ApiObjectField(name = "demographics", description = "Demograficos", required = false, order = 17)
    private List<DemographicValue> demographics = new ArrayList<>(0);
    @ApiObjectField(name = "tribunalElectoral", description = "verfica si los datops del pacientes son traidos de tribula electoral", required = false)
    private Integer stateTE = null;
}
