/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.cltech.enterprisent.domain.masters.common.PathologyAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro de contenedores de Patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 19/10/2020
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Contenedores",
        description = "Muestra informacion del maestro de contenedores de patologia que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContainerPathology extends PathologyAudit 
{
    @ApiObjectField(name = "id", description = "Id", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre", required = true, order = 2)
    private String name;
    @ApiObjectField(name = "image", description = "Imagen", required = false, order = 3)
    private String image;
    @ApiObjectField(name = "status", description = "Estado", required = true, order = 4)
    private Integer status;
    @ApiObjectField(name = "print", description = "Imprime Casete", required = true, order = 5)
    private Integer print;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    
    public Integer isState()
    {
        return status;
    }

    public Integer getPrint() {
        return print;
    }

    public void setPrint(Integer print) {
        this.print = print;
    }
}
