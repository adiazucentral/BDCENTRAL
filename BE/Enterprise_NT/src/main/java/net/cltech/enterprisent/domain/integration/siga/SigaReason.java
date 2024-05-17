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
 * Representa un receso para el Siga.
 *
 * @version 1.0.0
 * @author equijano
 * @since 16/10/2018
 * @see Creación
 */
@ApiObject(
        group = "Siga",
        name = "Motivo",
        description = "Representa el objeto de motivo de receso del Siga."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SigaReason {

    @ApiObjectField(name = "id", description = "Id del motivo", order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre del motivo", order = 2)
    private String name;
    @ApiObjectField(name = "description", description = "Descripcion del motivo", order = 3)
    private String description;
    @ApiObjectField(name = "registerDate", description = "Fecha de registro del log", order = 4)
    private Date registerDate;
    @ApiObjectField(name = "type", description = "Tipo del motivo", order = 5)
    private int type;
    @ApiObjectField(name = "state", description = "Estado del motivo", order = 6)
    private int state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

}
