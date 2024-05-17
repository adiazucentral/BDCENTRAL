/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.databank;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa un usuario de databank  
 *
 * @version 1.0.0
 * @author omendez
 * @since 20/11/2020
 * @see Creación
 */
@ApiObject(
        group = "Integracion",
        name = "Usuario Databank",
        description = "Representa un usuario de databank"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDatabank 
{
    @ApiObjectField(name = "documentNumber", description = "Identificación", required = true, order = 1)
    private String documentNumber;
    @ApiObjectField(name = "userName", description = "Nombre de Usuario", required = true, order = 2)
    private String userName;

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
