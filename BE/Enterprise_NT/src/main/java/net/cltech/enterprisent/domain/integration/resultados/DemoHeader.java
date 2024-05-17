/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.resultados;

import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Demograficos cabecera
 * 
 * @version 1.0.0
 * @author omendez
 * @since 20/02/2023
 * @see Creación
 */
@ApiObject(
        group = "Integración",
        name = "Demograficos",
        description = "Demograficos"
)
// Lombok
@Getter
@Setter
@NoArgsConstructor
public class DemoHeader {
    
    @ApiObjectField(name = "id", description = "Id demografico", required = true, order = 1)
    private int id;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "idItem", description = "Id Item", required = true, order = 3)
    private int idItem;
    @ApiObjectField(name = "value", description = "Valor del demografico", required = true, order = 4)
    private String value;
    
    public DemoHeader(Integer id)
    {
        this.id = id;
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
        final DemoHeader other = (DemoHeader) obj;
        if (!Objects.equals(this.id, other.id))
        {
            return false;
        }
        return true;
    }
}
