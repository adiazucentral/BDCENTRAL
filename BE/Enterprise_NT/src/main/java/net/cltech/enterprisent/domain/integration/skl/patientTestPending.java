/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.skl;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 *
 * @author Jeison BarbosaCltech
 */
@ApiObject(
        group = "SKL",
        name = "Ordene pendientes ",
        description = "informacion de los  examenes pendientes del paciente  "
)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
public class patientTestPending {
    
    @ApiObjectField(name = "idPatient", description = "Id del paciente consultado", required = true, order = 1)
    private Integer idPatient;
    @ApiObjectField(name = "identification", description = "Identificación del ususario", required = true, order = 10)
    private String identification;
    @ApiObjectField(name = "codTypeDoc", description = "codigo de tipo documento ", required = true, order = 10)
    private String codTypeDoc;
    @ApiObjectField(name = "nameTypeDoc", description = "nombre tipo documento", required = true, order = 10)
    private String nameTypeDoc;
    @ApiObjectField(name = "numOrder", description = "Numero de orden", required = true, order = 10)
    private String numOrder;
    @ApiObjectField(name = "pendExam", description = "Examenes pendientes", required = true, order = 10)
    private boolean pendExam;
    @ApiObjectField(name = "name1", description = "Nombre 1 del paciente", required = true, order = 10)
    private String name1;
    @ApiObjectField(name = "name2", description = "Nombre 2 del paciente", required = true, order = 10)
    private String name2;
    @ApiObjectField(name = "lastName1", description = "Apellido 1 del paciente", required = true, order = 10)
    private String lastName1;
    @ApiObjectField(name = "lastName2", description = "Apellido 2 del paciente", required = true, order = 10)
    private String lastName2;
    
}
