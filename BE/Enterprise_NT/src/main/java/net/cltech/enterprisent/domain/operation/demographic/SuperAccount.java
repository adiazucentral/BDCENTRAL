/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.demographic;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObjectField;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class SuperAccount {
    @ApiObjectField(name = "id", description = "Id del cliente", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "nit", description = "NIT del cliente", required = true, order = 2)
    private String nit;
    @ApiObjectField(name = "name", description = "Nombre del cliente", required = true, order = 3)
    private String name;
    @ApiObjectField(name = "encryptionReportResult", description = "Envio final o previo", required = true, order = 32)
    private boolean encryptionReportResult;
}
