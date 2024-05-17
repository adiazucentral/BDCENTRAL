/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.cltech.enterprisent.domain.integration.band;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.jsondoc.core.annotation.ApiObject;
import org.jsondoc.core.annotation.ApiObjectField;

/**
 * Representa a una muestra de la aplicacion
 *
 * @version 1.0.0
 * @author omendez
 * @since 07/12/2020
 * @see Creacion
 */
@ApiObject(
    group = "Banda Transportadora",
    name = "Muestra",
    description = "Representa una muestra de la aplicación"
)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BandSample 
{
    @ApiObjectField(name = "idSample", description = "Id Muestra", required = true, order = 1)
    private Integer idSample;
    @ApiObjectField(name = "codeSample", description = "Codigo de la muestra", required = true, order = 2)
    private String codeSample;
    @ApiObjectField(name = "nameSample", description = "Nombre de la muestra", required = true, order = 3)
    private String nameSample;
    @ApiObjectField(name = "idContainer", description = "Id Contenedor", required = true, order = 4)
    private Integer idContainer;
    @ApiObjectField(name = "nameContainer", description = "Nombre del contenedor", required = true, order = 6)
    private String nameContainer;

    public Integer getIdSample() {
        return idSample;
    }

    public void setIdSample(Integer idSample) {
        this.idSample = idSample;
    }

    public String getCodeSample() {
        return codeSample;
    }

    public void setCodeSample(String codeSample) {
        this.codeSample = codeSample;
    }

    public String getNameSample() {
        return nameSample;
    }

    public void setNameSample(String nameSample) {
        this.nameSample = nameSample;
    }

    public Integer getIdContainer() {
        return idContainer;
    }

    public void setIdContainer(Integer idContainer) {
        this.idContainer = idContainer;
    }

    public String getNameContainer() {
        return nameContainer;
    }

    public void setNameContainer(String nameContainer) {
        this.nameContainer = nameContainer;
    }
}
