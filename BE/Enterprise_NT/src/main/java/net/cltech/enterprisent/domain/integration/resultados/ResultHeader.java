/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.resultados;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Filtros
 * 
 * @version 1.0.0
 * @author omendez
 * @since 17/02/2023
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Orden cabecera Interfaz de resultados",
        description = "Orden cabecera Interfaz de resultados"
)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class ResultHeader {
    @ApiObjectField(name = "names", description = "Nombres del paciente", required = true, order = 1)
    private String names;
    @ApiObjectField(name = "lastnames", description = "Apellidos del paciente", required = true, order = 2)
    private String lastnames;
    @ApiObjectField(name = "identification", description = "Identificación", required = true, order = 3)
    private String identification;
    @ApiObjectField(name = "gender", description = "Genero", required = true, order = 4)
    private String gender;
    @ApiObjectField(name = "birthday", description = "Fecha de nacimiento", required = true, order = 5)
    private String birthday;
    @ApiObjectField(name = "orderId", description = "Número de orden", required = true, order = 6)
    private Long orderId;
    @ApiObjectField(name = "createdDate", description = "Fecha de Creación", required = true, order = 7)
    private String createdDate;
    @ApiObjectField(name = "comment", description = "Comentario de la orden", required = true, order = 8)
    private String comment;
    @ApiObjectField(name = "diagnosis", description = "Diagnostico del paciente", required = true, order = 9)
    private String diagnosis;
    @ApiObjectField(name = "orderType", description = "Tipo de orden", required = true, order = 10)
    private String orderType;
    @ApiObjectField(name = "demographics", description = "Demograficos", required = false, order = 11)
    private List<DemoHeader> demographics = new ArrayList<>();  
    @ApiObjectField(name = "orderTypeId", description = "Id tipo de orden", required = true, order = 12)
    private Integer orderTypeId;

    public ResultHeader(Long orderId) {
        this.orderId = orderId;
    }

    public ResultHeader setDemographics(List<DemoHeader> demographics)
    {
        this.demographics = demographics;
        return this;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final ResultHeader other = (ResultHeader) obj;
        if (!Objects.equals(this.orderId, other.orderId))
        {
            return false;
        }
        return true;
    }
    
    
    
}