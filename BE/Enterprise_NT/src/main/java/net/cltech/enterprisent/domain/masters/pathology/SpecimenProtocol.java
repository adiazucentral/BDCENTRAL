/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.masters.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import net.cltech.enterprisent.domain.masters.common.PathologyAudit;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro de configuracion de protocolo de especimenes en patologia
 *
 * @version 1.0.0
 * @author omendez
 * @since 20/04/2021
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Protocolo Especimen",
        description = "Muestra información del maestro de configuracion de protocolo de especimenes en patologia que usa el API"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SpecimenProtocol extends PathologyAudit
{
    @ApiObjectField(name = "id", description = "Id del organo", required = true, order = 1)
    private Integer id;
    @ApiObjectField(name = "specimen", description = "Muestra", required = true, order = 2)
    private Specimen specimen = new Specimen();
    @ApiObjectField(name = "organ", description = "Organo", required = true, order = 3)
    private Organ organ = new Organ();
    @ApiObjectField(name = "casete", description = "Casete", required = true, order = 4)
    private Casete casete = new Casete();
    @ApiObjectField(name = "quantity", description = "Cantidad de casetes", required = true, order = 5)
    private Integer quantity;
    @ApiObjectField(name = "sheets", description = "Lista de laminas", required = true, order = 6)
    private List<Sheet> sheets;
    @ApiObjectField(name = "processingHours", description = "Horas de procesamiento", required = true, order = 7)
    private Integer processingHours;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Specimen getSpecimen() {
        return specimen;
    }

    public void setSpecimen(Specimen specimen) {
        this.specimen = specimen;
    }
    
    public Organ getOrgan() {
        return organ;
    }

    public void setOrgan(Organ organ) {
        this.organ = organ;
    }
    
    public Casete getCasete() {
        return casete;
    }

    public void setCasete(Casete casete) {
        this.casete = casete;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public List<Sheet> getSheets() {
        return sheets;
    }

    public void setSheets(List<Sheet> sheets) {
        this.sheets = sheets;
    }

    public Integer getProcessingHours() {
        return processingHours;
    }

    public void setProcessingHours(Integer processingHours) {
        this.processingHours = processingHours;
    }
}
