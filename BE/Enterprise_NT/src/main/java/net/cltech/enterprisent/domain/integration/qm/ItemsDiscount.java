/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.qm;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa el objeto requerido para descontar articulos en QM
 * 
 * @version 1.0.0
 * @author omendez
 * @since 2021/01/12
 * @see Creación
 */

@ApiObject(
        group = "Integración",
        name = "QM",
        description = "Datos del examen"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemsDiscount 
{
    @ApiObjectField(name = "test", description = "Id del examen", required = true, order = 1)
    private Integer test;
    @ApiObjectField(name = "user", description = "Usuario autenticado", required = true, order = 2)
    private userQM user;
    
    public ItemsDiscount() {
        user = new userQM();
    }

    public Integer getTest() {
        return test;
    }

    public void setTest(Integer test) {
        this.test = test;
    }

    public userQM getUser() {
        return user;
    }

    public void setUser(userQM user) {
        this.user = user;
    }
}
