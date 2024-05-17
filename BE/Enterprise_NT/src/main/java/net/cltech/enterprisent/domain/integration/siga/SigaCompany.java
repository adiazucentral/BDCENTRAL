/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.siga;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa una compañia para el Siga.
 *
 * @version 1.0.0
 * @author equijano
 * @since 22/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Siga",
        name = "Compañia",
        description = "Representa el objeto de la compañia del Siga."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaCompany {

    @ApiObjectField(name = "id", description = "Id de la compañia", order = 1)
    private Integer id;
    @ApiObjectField(name = "code", description = "Codigo de la compañia", order = 2)
    private String code;
    @ApiObjectField(name = "name", description = "Nombre de la compañia", order = 3)
    private String name;
    @ApiObjectField(name = "description", description = "Descripcion de la compañia", order = 4)
    private String description;
    @ApiObjectField(name = "registerDate", description = "Fecha de registro de la compañia", order = 5)
    private Date registerDate;
    @ApiObjectField(name = "priority", description = "Prioridad de la compañia", order = 6)
    private int priority;
    @ApiObjectField(name = "state", description = "Estado de la compañia", order = 7)
    private short state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }
    
    
    
}
