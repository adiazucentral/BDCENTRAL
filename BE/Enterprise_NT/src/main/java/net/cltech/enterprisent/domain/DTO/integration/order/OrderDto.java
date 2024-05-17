/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.DTO.integration.order;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.Data;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa una orden enviada de sitio externo
 *
 * @author oarango
 * @since 2022-04-14
 * @see Creación
 */
@ApiObject(
        name = "Orden",
        group = "Orden",
        description = "Orden de sitio externo"
)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDto
{

    @ApiObjectField(name = "order", description = "Número de orden", required = true)
    private long order;
    @ApiObjectField(name = "type", description = "Tipo de la orden", required = true)
    private String type;
    @ApiObjectField(name = "patient", description = "Paciente de la orden", required = true)
    private PatientDto patient;
    @ApiObjectField(name = "tests", description = "Exámenes de la orden", required = true)
    private List<TestDto> tests;
    @ApiObjectField(name = "user", description = "Usuario que crea la orden", required = true)
    private String user;
    @ApiObjectField(name = "comment", description = "Comentario de la orden")
    private String comment;
    @ApiObjectField(name = "centralSystem", description = "Sistema central asociado a la orden")
    private String centralSystem;
    @ApiObjectField(name = "centralSystemId", description = "Id del sistema central asociado a la orden", required = true)
    private int centralSystemId;
    @ApiObjectField(name = "options", description = "Opciones para la orden")
    private String options;
    @ApiObjectField(name = "demographics", description = "Demográficos de la orden", required = true)
    private List<DemographicDto> demographics;

    public OrderDto()
    {
    }

    public OrderDto(long order, String user)
    {
        this.order = order;
        this.user = user;
    }
}
