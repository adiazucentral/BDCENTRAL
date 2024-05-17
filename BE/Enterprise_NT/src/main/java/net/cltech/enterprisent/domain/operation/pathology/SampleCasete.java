/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.operation.pathology;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.cltech.enterprisent.domain.masters.pathology.Casete;
import net.cltech.enterprisent.domain.masters.pathology.Specimen;
import net.cltech.enterprisent.domain.masters.pathology.SpecimenProtocol;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa la informacion de los casetes impresos para un caso de patologia.
 *
 * @version 1.0.0
 * @author omendez
 * @since 05/05/2021
 * @see Creación
 */
@ApiObject(
        group = "Patología",
        name = "Casetes Muestras",
        description = "Representa la informacion de los casetes impresos para un caso de patologia."
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SampleCasete 
{
    @ApiObjectField(name = "id", description = "Identificador", required = false, order = 1)
    private Integer id;
    @ApiObjectField(name = "sample", description = "Muestra", required = false, order = 2)
    private Integer sample;
    @ApiObjectField(name = "quantity", description = "Cantidad", required = false, order = 3)
    private Integer quantity;
    @ApiObjectField(name = "consecutive", description = "Consecutivo", required = true, order = 4)
    private String consecutive;
    @ApiObjectField(name = "casete", description = "Casete", required = true, order = 5)
    private Casete casete = new Casete();
    @ApiObjectField(name = "specimen", description = "Especimen", required = true, order = 6)
    private Specimen specimen = new Specimen();
    @ApiObjectField(name = "status", description = "Estado", required = false, order = 7)
    private Integer status;
    @ApiObjectField(name = "protocol", description = "Protocolo", required = true, order = 8)
    private SpecimenProtocol protocol = new SpecimenProtocol();
    @ApiObjectField(name = "tissueProcessor", description = "Procesador de tejidos", required = true, order = 9)
    private TissueProcessor tissueProcessor = new TissueProcessor();
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSample() {
        return sample;
    }

    public void setSample(Integer sample) {
        this.sample = sample;
    }
    
    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getConsecutive() {
        return consecutive;
    }

    public void setConsecutive(String consecutive) {
        this.consecutive = consecutive;
    }

    public Casete getCasete() {
        return casete;
    }

    public void setCasete(Casete casete) {
        this.casete = casete;
    }

    public Specimen getSpecimen() {
        return specimen;
    }

    public void setSpecimen(Specimen specimen) {
        this.specimen = specimen;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public SpecimenProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(SpecimenProtocol protocol) {
        this.protocol = protocol;
    } 

    public TissueProcessor getTissueProcessor() {
        return tissueProcessor;
    }

    public void setTissueProcessor(TissueProcessor tissueProcessor) {
        this.tissueProcessor = tissueProcessor;
    }
}
