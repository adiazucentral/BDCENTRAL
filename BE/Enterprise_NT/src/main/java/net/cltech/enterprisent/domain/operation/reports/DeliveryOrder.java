/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.reports;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.cltech.enterprisent.domain.masters.demographic.DocumentType;
import org.jsondoc.core.annotation.ApiObjectField;

@Getter
@Setter
public class DeliveryOrder {
    @ApiObjectField(name = "orderNumber", description = "Orden", order = 3)
    private Long orderNumber;
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
    @ApiObjectField(name = "documentType", description = "Tipo Documento", required = false, order = 16)
    private DocumentType documentType = new DocumentType();
    @ApiObjectField(name = "tests", description = "Examenes", order = 2)
    private List<DeliveryDetail> tests = new ArrayList<>();
}
