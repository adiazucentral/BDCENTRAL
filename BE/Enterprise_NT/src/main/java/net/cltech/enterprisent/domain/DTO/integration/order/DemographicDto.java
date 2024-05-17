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
 * Representa un Demográfico recibido de sitio externo
 *
 * @author oarango
 * @since 2022-04-14
 * @see Creación
 */
@ApiObject(
        name = "Demográfico",
        group = "Orden",
        description = "Demográfico de sitio externo"
)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DemographicDto
{
    

    @ApiObjectField(name = "id", description = "Id LIS del demográfico")
    private int id;
    @ApiObjectField(name = "name", description = "Nombre del demográfico")
    private String name;
    @ApiObjectField(name = "code", description = "Código del demográfico")
    private String code;
    @ApiObjectField(name = "encoded", description = "Tipo del demográfico")
    private boolean encoded;
    @ApiObjectField(name = "autoCreate", description = "Si el demográfico debe crearse")
    private boolean autoCreate;

    public DemographicDto()
    {
    }

    public DemographicDto(int id, String name, String code)
    {
        this.id = id;
        this.name = name;
        this.code = code;
    }
}
