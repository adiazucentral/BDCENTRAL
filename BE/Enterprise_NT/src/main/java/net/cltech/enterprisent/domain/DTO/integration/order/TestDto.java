/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.integration.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un examen recibido de sitio externo
 *
 * @author oarango
 * @since 2022-04-14
 * @see Creación
 */
@ApiObject(
        name = "Examen",
        group = "Orden",
        description = "Examen de sitio externo"
)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TestDto
{

    @ApiObjectField(name = "code", description = "Código del examen", required = true)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre del examen")
    private String name;
    @ApiObjectField(name = "keyHIS", description = "Código de sistema central", required = true)
    private String keyHIS;
    @ApiObjectField(name = "actionCode", description = "Código de acción", required = true)
    private String actionCode;

    public TestDto()
    {
    }

    public TestDto(String code, String name, String keyHIS, String actionCode)
    {
        this.code = code;
        this.name = name;
        this.keyHIS = keyHIS;
        this.actionCode = actionCode;
    }

    public TestDto(String code, String name, String actionCode)
    {
        this.code = code;
        this.name = name;
        this.actionCode = actionCode;
    }
}
