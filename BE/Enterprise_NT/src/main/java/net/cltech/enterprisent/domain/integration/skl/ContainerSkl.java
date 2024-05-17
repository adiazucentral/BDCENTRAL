package net.cltech.enterprisent.domain.integration.skl;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Clase que representa la informacion del maestro Recipientes
 *
 * @author JDuarte
 * @version 1.0.0
 * @since 12/05/2020
 * @see Creación
 */
@ApiObject(
        group = "Prueba",
        name = "RecipienteSkl",
        description = "Representa un recipiente"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ContainerSkl {

    @ApiObjectField(name = "id", description = "Id del recipiente", order = 1)
    private Integer id;
    @ApiObjectField(name = "name", description = "Nombre del recipiente", order = 2)
    private String name;
    @ApiObjectField(name = "count", description = "Cuenta", order = 3)
    private Integer count;
    @ApiObjectField(name = "image", description = "Imagen del recipiente", order = 4)
    private String imageBase64;
    @ApiObjectField(name = "idSample", description = "Id de la muestra", order = 5)
    private Integer idSample;
    @ApiObjectField(name = "sampleName", description = "Nombre de la muestra", order = 6)
    private String sampleName;
    @ApiObjectField(name = "takePending", description = "indicador si ahi examenes pendientes de toma para la muestra", order = 6)
    private Boolean takePending;
    

    public ContainerSkl() {

    }

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

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

    public Integer getIdSample() {
        return idSample;
    }

    public void setIdSample(Integer idSample) {
        this.idSample = idSample;
    }

    public String getSampleName() {
        return sampleName;
    }

    public void setSampleName(String sampleName) {
        this.sampleName = sampleName;
    }

    public Boolean getTakePending() {
        return takePending;
    }

    public void setTakePending(Boolean takePending) {
        this.takePending = takePending;
    }
    
    
    

}
