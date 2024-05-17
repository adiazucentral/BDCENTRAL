/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import java.util.List;
import net.cltech.enterprisent.domain.masters.pathology.ContainerPathology;
import net.cltech.enterprisent.domain.masters.pathology.Fixative;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion de una muestra para el modulo de patologia
 *
 * @author omendez
 * @version 1.0.0
 * @since 12/04/2021
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Muestra Patología",
        description = "Representa una muestra para el modulo de patologia"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SamplePathology 
{
    @ApiObjectField(name = "id", description = "Id de la muestra", order = 1)
    private Integer id;
    @ApiObjectField(name = "quantity", description = "Cantidad de muestras del caso", required = true, order = 2)
    private Integer quantity;
    @ApiObjectField(name = "container", description = "Contenedor", required = true, order = 3)
    private ContainerPathology container = new ContainerPathology();
    @ApiObjectField(name = "fixative", description = "Fijador", required = true, order = 4)
    private Fixative fixative = new Fixative();
    @ApiObjectField(name = "casetes", description = "Casetes", required = false, order = 5)
    private List<SampleCasete> casetes = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ContainerPathology getContainer() {
        return container;
    }

    public void setContainer(ContainerPathology container) {
        this.container = container;
    }

    public Fixative getFixative() {
        return fixative;
    }

    public void setFixative(Fixative fixative) {
        this.fixative = fixative;
    }

    public List<SampleCasete> getCasetes() {
        return casetes;
    }

    public void setCasetes(List<SampleCasete> casetes) {
        this.casetes = casetes;
    }
}
