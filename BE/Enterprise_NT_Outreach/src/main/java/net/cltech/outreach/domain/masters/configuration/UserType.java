/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.outreach.domain.masters.configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un tipo de usuario de la aplicación
 *
 * @version 1.0.0
 * @author cmartin
 * @since 23/04/2018
 * @see Creacion
 */
@ApiObject(
        group = "Configuración",
        name = "Tipos de Usuario",
        description = "Representa un tipo de usuario de la aplicación"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserType
{
    @ApiObjectField(name = "type", description = "Tipo de Usuario", required = true, order = 1)
    private Integer type;
    @ApiObjectField(name = "message", description = "Mensaje", required = true, order = 2)
    private String message;
    @ApiObjectField(name = "quantityOrder", description = "Numero de ordenes: 0 -> Sin limite", required = true, order = 3)
    private Integer quantityOrder;
    @ApiObjectField(name = "image", description = "Imagen", required = true, order = 4)
    private String image;
    @ApiObjectField(name = "visible", description = "Indica si el tipo de usuario es visible en el inicio de sesión", required = true, order = 5)
    private boolean visible;
    @ApiObjectField(name = "confidential", description = "Indica si el tipo de usuario puede ver pruebas confidenciales", required = true, order = 6)
    private boolean confidential;
    @ApiObjectField(name = "onlyValidated", description = "Indica si el tipo de usuario solo puede ver pruebas validadas", required = true, order = 7)
    private boolean onlyValidated;
    
    public Integer getType()
    {
        return type;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Integer getQuantityOrder()
    {
        return quantityOrder;
    }

    public void setQuantityOrder(Integer quantityOrder)
    {
        this.quantityOrder = quantityOrder;
    }

    public String getImage()
    {
        return image;
    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public boolean isVisible()
    {
        return visible;
    }

    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }

    public boolean isConfidential() {
        return confidential;
    }

    public void setConfidential(boolean confidential) {
        this.confidential = confidential;
    }

    public boolean isOnlyValidated() {
        return onlyValidated;
    }

    public void setOnlyValidated(boolean onlyValidated) {
        this.onlyValidated = onlyValidated;
    }
    
    
}
