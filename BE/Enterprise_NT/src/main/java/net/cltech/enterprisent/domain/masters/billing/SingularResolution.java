package net.cltech.enterprisent.domain.masters.billing;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.common.Item;
import net.cltech.enterprisent.domain.operation.demographic.SuperDocumentType;
import net.cltech.enterprisent.domain.operation.orders.DemographicValue;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la resolución 4505
 * 
 * @version 1.0.0
 * @author javila
 * @since 20/08/2021
 * @see Creación
 */
@ApiObject(
        name = "Resolución Singular (4505)",
        group = "Facturacion",
        description = "Representa la resolución singular (4505)"
)
@Getter
@Setter
public class SingularResolution
{
    @ApiObjectField(name = "orderNumber", description = "Número de la orden", required = true, order = 1)
    private long orderNumber;
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
    @ApiObjectField(name = "address", description = "Dirección del Paciente", required = false, order = 9)
    private String address;
    @ApiObjectField(name = "email", description = "Correo Electronico", required = false, order = 10)
    private String email;
    @ApiObjectField(name = "phone", description = "Telefono del Paciente", required = false, order = 11)
    private String phone;
    @ApiObjectField(name = "orderStatus", description = "Estado de la orden", required = true, order = 12)
    private int orderStatus;
    @ApiObjectField(name = "codeClient", description = "Cóigo del cliente", required = true, order = 13)
    private String codeClient;
    @ApiObjectField(name = "nameClient", description = "Nombre del cliente", required = true, order = 14)
    private String nameClient;
    @ApiObjectField(name = "codeRate", description = "Cóigo de la tarifa", required = true, order = 15)
    private String codeRate;
    @ApiObjectField(name = "nameRate", description = "Nombre de la tarifa", required = true, order = 16)
    private String nameRate;
    @ApiObjectField(name = "physician", description = "Medico", required = false, order = 17)
    private String physician;
    @ApiObjectField(name = "demographics", description = "Demograficos", required = false, order = 18)
    private List<DemographicValue> AllDemographics;
    @ApiObjectField(name = "codeBranch", description = "Código de la sede", required = true, order = 19)
    private String codeBranch;    
    @ApiObjectField(name = "nameBranch", description = "Nombre de la sede", required = true, order = 20)
    private String nameBranch;
    @ApiObjectField(name = "tests", description = "examenes", required = false, order = 21)
    private List<SingularResolutionTest> tests = new ArrayList<>();
    @ApiObjectField(name = "documentType", description = "Tipo Documento", required = false, order = 22)
    private SuperDocumentType documentType = new SuperDocumentType();
    @ApiObjectField(name = "orderDate", description = "Fecha de la orden", required = false, order = 23)
    private Date orderDate;
    
    public SingularResolution()
    {
    }
    
    public SingularResolution setTests(List<SingularResolutionTest> tests)
    {
        this.tests = tests;
        return this;
    }
}