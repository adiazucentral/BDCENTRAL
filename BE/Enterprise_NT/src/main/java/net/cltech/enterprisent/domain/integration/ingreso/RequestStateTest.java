/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.ingreso;


import lombok.Getter;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObjectField;

@Getter
@Setter
public class RequestStateTest {
    @ApiObjectField(name = "OBR", description = "OBR", order = 1)
    private String OBR;
    @ApiObjectField(name = "", description = "estado del examen", order = 2)
    private RequestTestStatus statustest;
}
