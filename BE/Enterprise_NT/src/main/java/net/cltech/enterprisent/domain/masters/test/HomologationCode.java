/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.test;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObjectField;


@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class HomologationCode {
    @ApiObjectField(name = "id", description = "Id del examen", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "codes", description = "Lista de códigos homologados para un examen", required = true, order = 2)
    private String codes;
}
